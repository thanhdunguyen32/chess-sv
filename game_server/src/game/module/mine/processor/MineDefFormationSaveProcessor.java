package game.module.mine.processor;

import com.google.common.primitives.Ints;
import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.chapter.bean.DbBattleset;
import game.module.hero.bean.GeneralBean;
import game.module.hero.dao.GeneralCache;
import game.module.mine.bean.DBMine;
import game.module.mine.bean.DBMinePlayer;
import game.module.mine.bean.DBMinePoint;
import game.module.mine.dao.MineCache;
import game.module.mine.logic.MineManager;
import game.module.mythical.bean.MythicalAnimal;
import game.module.mythical.dao.MythicalAnimalCache;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;
import ws.WsMessageHall.S2CErrorCode;
import ws.WsMessageMine.C2SMineDefFormationSave;
import ws.WsMessageMine.S2CMineDefFormationSave;
import ws.WsMessageMine.S2CMineEnemyDetail;

import java.util.*;

@MsgCodeAnn(msgcode = C2SMineDefFormationSave.id, accessLimit = 200)
public class MineDefFormationSaveProcessor extends PlayingRoleMsgProcessor {

	private static Logger logger = LoggerFactory.getLogger(MineDefFormationSaveProcessor.class);

	@Override
	public void processByte(PlayingRole playingRole, RequestByteMessage requestMessage) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void processProto(PlayingRole playingRole, RequestProtoMessage request) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void processMy(PlayingRole playingRole, MyRequestMessage request) throws Exception {
		int playerId = playingRole.getId();
		C2SMineDefFormationSave reqMsg = C2SMineDefFormationSave.parse(request);
		int level_index = reqMsg.level_index;
		int point_index = reqMsg.point_index;
		logger.info("mine defence formation save!playerId={},reqMsg={}", playerId, reqMsg);
		DBMine mineEntity = MineCache.getInstance().getDBMine();
		if (mineEntity == null) {
			S2CErrorCode retMsg = new S2CErrorCode(S2CMineDefFormationSave.msgCode, 2104);
			playingRole.writeAndFlush(retMsg.build(playingRole.alloc()));
			return;
		}
		// 英雄是否存在
		WsMessageBase.IOFormationGeneralPos[] items = reqMsg.items;
		if (items == null || items.length == 0) {
			S2CErrorCode retMsg = new S2CErrorCode(S2CMineDefFormationSave.msgCode, 130);
			playingRole.writeAndFlush(retMsg.build(playingRole.alloc()));
			return;
		}
		Set<Long> heroSet = new HashSet<>();
		Map<Long, GeneralBean> cardAll = GeneralCache.getInstance().getHeros(playerId);
		for (WsMessageBase.IOFormationGeneralPos generalPos : items) {
			long generalUuid = generalPos.general_uuid;
			if (generalUuid == 0) {
				continue;
			}
			// 英雄不存在
			if (!cardAll.containsKey(generalUuid)) {
				S2CErrorCode retMsg = new S2CErrorCode(S2CMineDefFormationSave.msgCode, 130);
				playingRole.writeAndFlush(retMsg.build(playingRole.alloc()));
				return;
			}
			// 英雄是否重复
			if (heroSet.contains(generalUuid)) {
				S2CErrorCode retMsg = new S2CErrorCode(S2CMineDefFormationSave.msgCode, 130);
				playingRole.writeAndFlush(retMsg.build(playingRole.alloc()));
				return;
			}
			heroSet.add(generalUuid);
		}
		// 当前是否我占领
		DBMinePoint minePointBuider = mineEntity.getLevels().get(level_index) .getMinePoints().get(point_index);
		if (minePointBuider == null) {
			S2CErrorCode retMsg = new S2CErrorCode(S2CMineDefFormationSave.msgCode, 2106);
			playingRole.writeAndFlush(retMsg.build(playingRole.alloc()));
			return;
		}
		// 当前不是我占领
		if (minePointBuider.getPlayerId() != playerId) {
			S2CErrorCode retMsg = new S2CErrorCode(S2CMineDefFormationSave.msgCode, 2107);
			playingRole.writeAndFlush(retMsg.build(playingRole.alloc()));
			return;
		}
		// 占领的其他矿防守阵容
		int thisMinePointVal = MineManager.getInstance().genereteMintPoint(level_index, point_index);
		DBMinePlayer minePlayer = mineEntity.getPlayers().get(playerId);
		int otherMinePointVal = -1;
		if (minePlayer != null && minePlayer.getOwnMinePoint().size() > 0) {
			for (int minePointVal : minePlayer.getOwnMinePoint()) {
				if (minePointVal != thisMinePointVal) {
					otherMinePointVal = minePointVal;
					break;
				}
			}
		}
		if (otherMinePointVal >= 0) {
			int[] otherMinePointPos = MineManager.getInstance().seperateMinePoint(otherMinePointVal);
			DBMinePoint otherhMinePoint = mineEntity.getLevels().get(otherMinePointPos[0])
					.getMinePoints().get(otherMinePointPos[1]);
			if (otherhMinePoint.getDbBattleset() != null) {
				Collection<GeneralBean> defenceCards = otherhMinePoint.getDbBattleset().getTeam().values();
				logger.info("ohter mine point,val={},cards={}",otherMinePointVal,defenceCards);
				for (GeneralBean generalBean : defenceCards) {
					long aCardId = generalBean.getUuid();
					if(aCardId == 0) {
						continue;
					}
					// 其他防守阵型中也有这个英雄
					if (heroSet.contains(aCardId)) {
						S2CErrorCode retMsg = new S2CErrorCode(S2CMineDefFormationSave.msgCode, 2108);
						playingRole.writeAndFlush(retMsg.build(playingRole.alloc()));
						return;
					}
				}
			}
		}
		// do
		DbBattleset dbBattleset = new DbBattleset();
		minePointBuider.setDbBattleset(dbBattleset);
		if (reqMsg.mythic > 0) {
			MythicalAnimal mythicalAnimal = MythicalAnimalCache.getInstance().getPlayerMythicalAnimal(playerId, reqMsg.mythic);
			if (mythicalAnimal != null) {
				dbBattleset.setMythic(mythicalAnimal);
			}
		}
		Map<Integer, GeneralBean> generalBeanMap = new HashMap<>();
		Map<Long, GeneralBean> generalAll = GeneralCache.getInstance().getHeros(playerId);
		for (WsMessageBase.IOFormationGeneralPos generalPos : items) {
			long generalUuid = generalPos.general_uuid;
			int pos = generalPos.pos;
			GeneralBean generalBean = generalAll.get(generalUuid);
			if(generalBean == null) {
				continue;
			}
			generalBeanMap.put(pos, generalBean);
		}
		dbBattleset.setTeam(generalBeanMap);
		MineCache.getInstance().setMineEntity(mineEntity);
		// ret
		S2CMineDefFormationSave respMsg = new S2CMineDefFormationSave();
		// 更新矿点信息
        respMsg.mine_point_detail = MineEnemyDetailProcessor.buildS2CMineEnemyDetail(mineEntity, playerId,
                level_index, point_index);
		playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
	}

	public static void main(String[] args) {
		int[] heros = new int[] { 12, 34, 234, 4 };
		List<Integer> ints = Ints.asList(heros);
		System.out.println(ints);
	}

}
