package game.module.mine.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.battle.dao.BattlePlayerBase;
import game.module.chapter.bean.DbBattleset;
import game.module.hero.bean.GeneralBean;
import game.module.hero.logic.GeneralManager;
import game.module.mine.bean.DBMine;
import game.module.mine.bean.DBMinePlayer;
import game.module.mine.bean.DBMinePoint;
import game.module.mine.dao.MineCache;
import game.module.mine.logic.MineConstants;
import game.module.mine.logic.MineManager;
import game.module.mythical.processor.MythicalListProcessor;
import game.module.offline.bean.PlayerBaseBean;
import game.module.offline.logic.PlayerOfflineManager;
import game.module.user.dao.CommonTemplateCache;
import game.session.SessionManager;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;
import ws.WsMessageBase.IOMineHolder;
import ws.WsMessageHall.S2CErrorCode;
import ws.WsMessageMine.C2SMineEnemyDetail;
import ws.WsMessageMine.S2CMineEnemyDetail;

import java.util.*;
import java.util.stream.Collectors;

@MsgCodeAnn(msgcode = C2SMineEnemyDetail.id, accessLimit = 200)
public class MineEnemyDetailProcessor extends PlayingRoleMsgProcessor {

	private static Logger logger = LoggerFactory.getLogger(MineEnemyDetailProcessor.class);

	@Override
	public void processByte(PlayingRole hero, RequestByteMessage request) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void processProto(PlayingRole hero, RequestProtoMessage request) throws Exception {
	}

	@Override
	public void processMy(PlayingRole playingRole, MyRequestMessage request) throws Exception {
		int playerId = playingRole.getId();
		C2SMineEnemyDetail reqMsg = C2SMineEnemyDetail.parse(request);
		int level_index = reqMsg.level_index;
		int point_index = reqMsg.point_index;
		logger.info("mine enemy detail!playerId={},level_index={},point_index={}", playerId, level_index, point_index);
		List<Integer> mineLevelConfig = (List<Integer>) CommonTemplateCache.getInstance().getConfig("mine_levels");
		if (level_index < 0 || level_index >= mineLevelConfig.size() - 1) {
			S2CErrorCode retMsg = new S2CErrorCode(S2CMineEnemyDetail.msgCode, 130);
			playingRole.writeAndFlush(retMsg.build(playingRole.alloc()));
			return;
		}
		int min_level = mineLevelConfig.get(level_index);
		if (playingRole.getPlayerBean().getLevel() < min_level) {
			S2CErrorCode retMsg = new S2CErrorCode(S2CMineEnemyDetail.msgCode, 2101);
			playingRole.writeAndFlush(retMsg.build(playingRole.alloc()));
			return;
		}
		if (point_index < 0 || point_index >= MineConstants.PAGE_SIZE * MineConstants.POINT_SIZE_1_PAGE) {
			S2CErrorCode retMsg = new S2CErrorCode(S2CMineEnemyDetail.msgCode, 130);
			playingRole.writeAndFlush(retMsg.build(playingRole.alloc()));
			return;
		}
		DBMine mineEntity = MineCache.getInstance().getDBMine();
		if (mineEntity == null) {
			mineEntity = MineManager.getInstance().createMineEntity(level_index, point_index);
		} else {
			// 机器人阵容尚未生成
			DBMinePoint minePointEntity = mineEntity.getLevels().get(level_index).getMinePoints().get(point_index);
			int targetPlayerId = minePointEntity.getPlayerId();
			if (targetPlayerId == 0 && minePointEntity.getBattlePlayerMap() == null) {
				MineManager.getInstance().createMinePointRobots(level_index, point_index,
						mineEntity.getLevels().get(level_index).getMinePoints().get(point_index));
				MineCache.getInstance().setMineEntity(mineEntity);
			}
		}
		// ret
		S2CMineEnemyDetail respMsg = buildS2CMineEnemyDetail(mineEntity,playerId, level_index, point_index);
		// ret
		playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
	}

	public static S2CMineEnemyDetail buildS2CMineEnemyDetail(DBMine mineEntity,int playerId, int level_index, int point_index) {
		S2CMineEnemyDetail respMsg = new S2CMineEnemyDetail();
		respMsg.level_index = level_index;
		respMsg.point_index = point_index;
		DBMinePoint minePointEntity = mineEntity.getLevels().get(level_index).getMinePoints().get(point_index);
		int targetPlayerId = minePointEntity.getPlayerId();
		if (targetPlayerId > 0) {
			long finishTime = minePointEntity.getFinishTime();
			int cdTime = (int)((finishTime-System.currentTimeMillis())/1000);
			cdTime = Math.max(cdTime, 0);
			PlayingRole targetPr = SessionManager.getInstance().getPlayer(targetPlayerId);
			PlayerBaseBean poc = null;
			if (targetPr == null) {
				poc = PlayerOfflineManager.getInstance().getPlayerOfflineCache(targetPlayerId);
			} else {
				poc = targetPr.getPlayerBean();
			}
			respMsg.base_info = new IOMineHolder(targetPlayerId, poc.getName(), poc.getLevel(), poc.getIconid(), poc.getFrameid(),
					poc.getPower(), cdTime);
			//英雄已经不存在判断
//			DbBattleset dbBattleset1 = minePointEntity.getDbBattleset();
//			DBStageFormation stageFormationEntity = minePointEntity.getDefenceFormation();
//			DBStageFormation.Builder formationBuilder = stageFormationEntity.toBuilder();
//			List<Integer> cardIdList = formationBuilder.getCardTplIdList();
//			boolean isCardMiss = false;
//			int i=0;
//			for (Integer cardTplId : cardIdList) {
//				if(cardTplId > 0) {
//					CardEntity ce = null;
//					Map<Integer, CardEntity> cardCacheAll = CardCache.getInstance()
//							.getCardEntityTemplateKey(targetPlayerId);
//					if (cardCacheAll != null) {
//						ce = cardCacheAll.get(cardTplId);
//					} else {
//						ce = poc.getCardCacheMap().get(cardTplId);
//					}
//					if(ce == null) {
//						formationBuilder.setCardTplId(i, 0);
//						isCardMiss = true;
//					}
//				}
//				i++;
//			}
//			if(isCardMiss) {
//				stageFormationEntity = formationBuilder.build();
//				MineManager.getInstance().changeMineDefenceFormation(mineEntity,level_index,point_index,stageFormationEntity);
//			}
			// 阵容
			DbBattleset dbBattleset = minePointEntity.getDbBattleset();
			respMsg.battleset = new WsMessageBase.IOBattlesetEnemy();
			if (dbBattleset.getMythic() != null) {
				WsMessageBase.IOMythicalAnimal ioMythicalAnimal = new WsMessageBase.IOMythicalAnimal();
				respMsg.battleset.mythic = ioMythicalAnimal;
				MythicalListProcessor.buildIOMythicalAnimal(dbBattleset.getMythic(), 101, ioMythicalAnimal);
			}
			respMsg.battleset.team = new HashMap<>();
			Map<Integer, GeneralBean> battlesetTeam = dbBattleset.getTeam();
			for (Map.Entry<Integer, GeneralBean> aEntry : battlesetTeam.entrySet()) {
				GeneralBean generalBean = aEntry.getValue();
				if (generalBean == null) {
					continue;
				}
				WsMessageBase.IOGeneralBean ioGeneralBean = GeneralManager.getInstance().buildIoGeneral(generalBean);
				Integer formationPos = aEntry.getKey();
				respMsg.battleset.team.put(formationPos, ioGeneralBean);
			}
		} else {
			// 机器人阵容
			Map<Integer, BattlePlayerBase> battlePlayerMap = minePointEntity.getBattlePlayerMap();
			respMsg.battleset = new WsMessageBase.IOBattlesetEnemy();
			respMsg.battleset.team = new HashMap<>();
			for (Map.Entry<Integer, BattlePlayerBase> aEntry : battlePlayerMap.entrySet()) {
				BattlePlayerBase battlePlayer = aEntry.getValue();
				WsMessageBase.IOGeneralBean ioGeneralBean = GeneralManager.getInstance().buildIoGeneral(battlePlayer);
				Integer formationPos = aEntry.getKey();
				respMsg.battleset.team.put(formationPos, ioGeneralBean);
			}
			respMsg.base_info = new IOMineHolder(minePointEntity.getPlayerId(), minePointEntity.getRname(), minePointEntity.getLevel(),
					minePointEntity.getIconid(), minePointEntity.getFrameid(), minePointEntity.getPower(), 0);
		}
		//排除的英雄
		if (targetPlayerId == playerId) {
			//占领的其他矿防守阵容
			int thisMinePointVal = MineManager.getInstance().genereteMintPoint(level_index, point_index);
			DBMinePlayer minePlayer = mineEntity.getPlayers().get(playerId);
			int otherMinePointVal = -1;
			if(minePlayer != null && minePlayer.getOwnMinePoint().size()>0) {
				for(int minePointVal : minePlayer.getOwnMinePoint()) {
					if(minePointVal != thisMinePointVal) {
						otherMinePointVal = minePointVal;
						break;
					}
				}
			}
			if(otherMinePointVal >= 0) {
				int[] otherMinePointPos = MineManager.getInstance().seperateMinePoint(otherMinePointVal);
				DBMinePoint otherhMinePoint = mineEntity.getLevels().get(otherMinePointPos[0]).getMinePoints().get(otherMinePointPos[1]);
				if(otherhMinePoint.getDbBattleset() != null) {
					Collection<GeneralBean> defenceCards = otherhMinePoint.getDbBattleset().getTeam().values();
					List<Long> defenceCardUuids = new ArrayList<>();
					for (GeneralBean generalBean : defenceCards){
						defenceCardUuids.add(generalBean.getUuid());
					}
					respMsg.exclude_cards = defenceCardUuids;
				}
			}
		}
		//
		long randKey = SessionManager.getInstance().generateSessionId();
		respMsg.rand_key = randKey;
		MineManager.getInstance().putBattleRandKey(playerId, randKey);
		return respMsg;
	}

	public static void main(String[] args) {
		List<Integer> defenceCards = new ArrayList<>();
		defenceCards.add(1);
		defenceCards.add(0);
		defenceCards.add(2);
		defenceCards.add(0);
		defenceCards.add(3);
		defenceCards = defenceCards.stream().filter(a -> a>0).collect(Collectors.toList());
		logger.info("a={}",defenceCards);
	}
}
