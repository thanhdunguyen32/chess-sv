package game.module.mine.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.mine.bean.DBMine;
import game.module.mine.bean.DBMineLevel;
import game.module.mine.bean.DBMinePlayer;
import game.module.mine.bean.DBMinePoint;
import game.module.mine.dao.MineCache;
import game.module.mine.logic.MineConstants;
import game.module.mine.logic.MineManager;
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
import ws.WsMessageBase.IOMineHolder;
import ws.WsMessageBase.IOMintPoint;
import ws.WsMessageHall.S2CErrorCode;
import ws.WsMessageMine.C2SMineList;
import ws.WsMessageMine.S2CMineList;

import java.util.ArrayList;
import java.util.List;

@MsgCodeAnn(msgcode = C2SMineList.id, accessLimit = 200)
public class MineListProcessor extends PlayingRoleMsgProcessor {

	private static Logger logger = LoggerFactory.getLogger(MineListProcessor.class);

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
		C2SMineList reqMsg = C2SMineList.parse(request);
		int playerId = playingRole.getId();
		logger.info("mine list!playerId={},reqMsg={}", playerId, reqMsg);
		int level_index = reqMsg.level_index;
		List<Integer> mineLevelConfig = (List<Integer>)CommonTemplateCache.getInstance().getConfig("mine_levels");
		if(level_index <0 || level_index >=mineLevelConfig.size()-1) {
			S2CErrorCode retMsg = new S2CErrorCode(S2CMineList.msgCode, 130);
			playingRole.writeAndFlush(retMsg.build(playingRole.alloc()));
			return;
		}
		int min_level = mineLevelConfig.get(level_index);
		if(playingRole.getPlayerBean().getLevel()< min_level) {
			S2CErrorCode retMsg = new S2CErrorCode(S2CMineList.msgCode,2101);
			playingRole.writeAndFlush(retMsg.build(playingRole.alloc()));
			return;
		}
		int page_index = reqMsg.page_index;
		if(page_index <0 || page_index >= MineConstants.PAGE_SIZE) {
			S2CErrorCode retMsg = new S2CErrorCode(S2CMineList.msgCode,130);
			playingRole.writeAndFlush(retMsg.build(playingRole.alloc()));
			return;
		}
		sendList(playingRole, reqMsg.level_index, reqMsg.page_index);
	}

	public static void sendList(PlayingRole playingRole, int levelIdx, int pageIdx) {
		int playerId = playingRole.getId();
		DBMine mineEntity = MineCache.getInstance().getDBMine();
		if (mineEntity == null) {
			S2CMineList respMsg = new S2CMineList();
			respMsg.level_index = levelIdx;
			respMsg.page_index = pageIdx;
			respMsg.my_income = new ArrayList<>(2);
			respMsg.my_hold = new ArrayList<>();
			respMsg.mine_points = new ArrayList<>(MineConstants.POINT_SIZE_1_PAGE);
			for (int i = 0; i < MineConstants.POINT_SIZE_1_PAGE; i++) {
				respMsg.mine_points.add(new IOMintPoint());
			}
			playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
			return;
		} else {
			S2CMineList respMsg = new S2CMineList();
			respMsg.level_index = levelIdx;
			respMsg.page_index = pageIdx;
			//
			List<Integer> incomes = new ArrayList<>(2);
			DBMinePlayer minePlayerEntity = null;
			if (mineEntity.getPlayers() != null && mineEntity.getPlayers().containsKey(playerId)) {
				minePlayerEntity = mineEntity.getPlayers().get(playerId);
				List<Integer> gainList = minePlayerEntity.getGains();
				if (gainList != null && gainList.size() > 0) {
					incomes = gainList;
				}
			}
			respMsg.my_income = incomes;
			//
			List<Integer> myHold = new ArrayList<>();
			List<Integer> cdTimes = new ArrayList<>();
			if (minePlayerEntity != null) {
				List<Integer> ownPoints = minePlayerEntity.getOwnMinePoint();
				if (ownPoints != null && ownPoints.size() > 0) {
					myHold = ownPoints;
					//
					List<Integer> cdTimeList = new ArrayList<>();
					for (Integer aPointVal : ownPoints) {
						int[] pointPos = MineManager.getInstance().seperateMinePoint(aPointVal);
						long finishTime = mineEntity.getLevels().get(pointPos[0]).getMinePoints().get(pointPos[1]).getFinishTime();
						int cdTime = (int)((finishTime-System.currentTimeMillis())/1000);
						cdTime = cdTime < 0 ? 0 : cdTime;
						cdTimeList.add(cdTime);
					}
					cdTimes = cdTimeList;
				}
			}
			respMsg.my_hold = myHold;
			respMsg.my_cd_time = cdTimes;
			//
			List<IOMintPoint> mine_points = new ArrayList<>(MineConstants.POINT_SIZE_1_PAGE);
			respMsg.mine_points = mine_points;
			DBMineLevel mineLevelEntity = mineEntity.getLevels().get(levelIdx);
			if (mineLevelEntity != null) {
				int startIdx = pageIdx * MineConstants.POINT_SIZE_1_PAGE;
				List<DBMinePoint> minePointList = mineLevelEntity.getMinePoints().subList(startIdx,
						startIdx + MineConstants.POINT_SIZE_1_PAGE);
				int i = 0;
				for (DBMinePoint dbMinePoint : minePointList) {
					IOMintPoint mintPointEntity = new IOMintPoint();
					respMsg.mine_points.add(mintPointEntity);
					if (dbMinePoint.getPlayerId() > 0) {
						int targetPlayerId = dbMinePoint.getPlayerId();
						PlayingRole targetPr = SessionManager.getInstance().getPlayer(targetPlayerId);
						PlayerBaseBean poc = null;
						if (targetPr == null) {
							poc = PlayerOfflineManager.getInstance().getPlayerOfflineCache(targetPlayerId);
							mintPointEntity.hold_player = new IOMineHolder(targetPlayerId,poc.getName(), poc.getLevel(), poc.getIconid(),
									poc.getFrameid(), poc.getPower(), 0);
						} else {
							mintPointEntity.hold_player = new IOMineHolder(targetPlayerId, targetPr.getPlayerBean().getName(), targetPr.getPlayerBean().getLevel(),
									targetPr.getPlayerBean().getIconid(), targetPr.getPlayerBean().getFrameid(), targetPr.getPlayerBean().getPower(), 0);
						}
					}
				}
			} else {
				for (int i = 0; i < MineConstants.POINT_SIZE_1_PAGE; i++) {
					respMsg.mine_points.add(new IOMintPoint());
				}
			}
			playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
			return;
		}
	}

}
