package game.module.secret.processor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import lion.netty4.message.RequestByteMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import db.proto.ProtoMessageSecret.DBSecretAward;
import db.proto.ProtoMessageSecret.DBSecretBoxAward;
import db.proto.ProtoMessageSecret.DBSecretUsedHero;
import db.proto.ProtoMessageSecret.SecretStageAward;
import game.common.DateCommonUtils;
import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.secret.bean.Secret;
import game.module.secret.dao.SecretCache;
import game.module.secret.logic.SecretManager;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestProtoMessage;
import ws.WsMessageSecret.C2SSecretView;
import ws.WsMessageBase.IOSecretBoxAward;
import ws.WsMessageBase.IOSecretHero;
import ws.WsMessageSecret.S2CSecretView;
import ws.WsMessageBase.SecretItemInfo;

/**
 * 加载秘密基地信息
 * 
 * @author zhangning
 * 
 * @Date 2015年1月27日 下午1:59:07
 */
@MsgCodeAnn(msgcode = C2SSecretView.id, accessLimit = 500)
public class SecretViewProcessor extends PlayingRoleMsgProcessor {

	private static Logger logger = LoggerFactory.getLogger(SecretViewProcessor.class);

	@Override
	public void processByte(PlayingRole playingRole, RequestByteMessage request) throws Exception {

	}

	/**
	 * 加载秘密基地信息
	 */
	@Override
	public void processProto(PlayingRole playingRole, RequestProtoMessage request) throws Exception {
		
	}

	@Override
	public void processMy(PlayingRole playingRole, MyRequestMessage request) throws Exception {
		logger.info("list Secret Info");
		// 没有秘密基地记录
		SecretCache secretCache = SecretCache.getInstance();
		Secret secret = secretCache.getSecret(playingRole.getId());
		if (secret == null) {
			secret = new Secret();
			secret.setPlayerId(playingRole.getId());
			secret.setMapId(0);
			secret.setProgress(0);
			secret.setReviveCount(0);
			secret.setBoxAward(DBSecretBoxAward.newBuilder().build());
			secret.setFormationHeros(DBSecretUsedHero.newBuilder().build());
			secret.setMyCost(DBSecretUsedHero.newBuilder().build());
			secret.setEnemyCost(DBSecretUsedHero.newBuilder().build());
			secret.setResetTime(new Date());
			secretCache.addSecret(secret);
			SecretManager.getInstance().asyncInsertSecret(secret);
		}
		// send msg
		S2CSecretView respMsg = new S2CSecretView();
		respMsg.map_id = secret.getMapId();
		respMsg.progress = secret.getProgress();
		respMsg.could_reset = true;
		respMsg.revive_count = secret.getReviveCount();
		//能否重置
		Date lastResetTime = secret.getResetTime();
		if (lastResetTime != null && DateCommonUtils.isSameDay(lastResetTime, 5)) {
			respMsg.could_reset = false;
		}
		// 宝箱领取信息
		DBSecretBoxAward dbRanAward = secret.getBoxAward();
		List<SecretStageAward> randomAwards = dbRanAward.getAwardsList();
		if (randomAwards != null && !randomAwards.isEmpty()) {
			respMsg.boxAward = new ArrayList<>(randomAwards.size());
			int i = 0;
			for (SecretStageAward randomAward : randomAwards) {
				// 发送奖励领取信息
				IOSecretBoxAward secretBoxAward = new IOSecretBoxAward();
				respMsg.boxAward.add(secretBoxAward);
				secretBoxAward.stage_index = randomAward.getStageIndex();
				secretBoxAward.is_get = randomAward.getIsGet();
				//
				List<DBSecretAward> dbSecretAwards = randomAward.getSecretAwardList();
				if (dbSecretAwards != null && !dbSecretAwards.isEmpty()) {
					secretBoxAward.award_list = new ArrayList<>(dbSecretAwards.size());
					int j = 0;
					for (DBSecretAward dbSecretAward : dbSecretAwards) {
						secretBoxAward.award_list.add(new SecretItemInfo(dbSecretAward.getAwardId(),
								dbSecretAward.getAwardCnt()));
					}
				}
			}
		}
		// 上阵英雄+士兵
		DBSecretUsedHero dbSecretUsedHero = secret.getFormationHeros();
		if (dbSecretUsedHero != null) {
			Map<Integer, Integer> formationHeros = dbSecretUsedHero.getHeroUsedMap();
			List<IOSecretHero> heroRecordList = new ArrayList<>();
			if (formationHeros != null && formationHeros.size() > 0) {
				for (Map.Entry<Integer, Integer> dbSecretHeroLog : formationHeros.entrySet()) {
					IOSecretHero ioSecretHero = new IOSecretHero(dbSecretHeroLog.getValue(), 1, dbSecretHeroLog.getKey());
					heroRecordList.add(ioSecretHero);
				}
			}
			List<Integer> soldierList = dbSecretUsedHero.getSoldierUsedList();
			for (Integer aSoldierIndex : soldierList) {
				IOSecretHero ioSecretHero = new IOSecretHero(1000, 2, -aSoldierIndex);
				heroRecordList.add(ioSecretHero);
			}
			respMsg.online_formation = heroRecordList;
		}

		// 我方消耗
		DBSecretUsedHero myCostInfo = secret.getMyCost();
		if (myCostInfo != null) {
			Map<Integer, Integer> formationHeros = myCostInfo.getHeroUsedMap();
			List<IOSecretHero> heroRecordList = new ArrayList<>();
			if (formationHeros != null && formationHeros.size() > 0) {
				for (Map.Entry<Integer, Integer> dbSecretHeroLog : formationHeros.entrySet()) {
					IOSecretHero ioSecretHero = new IOSecretHero(dbSecretHeroLog.getValue(), 1, dbSecretHeroLog.getKey());
					heroRecordList.add(ioSecretHero);
				}
			}
			List<Integer> soldierList = myCostInfo.getSoldierUsedList();
			int idx = 0;
			for (Integer aSoldierHp : soldierList) {
				if(aSoldierHp != -1) {
					IOSecretHero ioSecretHero = new IOSecretHero(aSoldierHp, 2, -idx);
					heroRecordList.add(ioSecretHero);
				}
				idx++;
			}
			respMsg.my_cost = heroRecordList;
		}

		// 敌方消耗
		DBSecretUsedHero enemyCostInfo = secret.getEnemyCost();
		if (enemyCostInfo != null) {
			Map<Integer, Integer> formationHeros = enemyCostInfo.getHeroUsedMap();
			List<IOSecretHero> heroRecordList = new ArrayList<>();
			if (formationHeros != null && formationHeros.size() > 0) {
				for (Map.Entry<Integer, Integer> dbSecretHeroLog : formationHeros.entrySet()) {
					IOSecretHero ioSecretHero = new IOSecretHero(dbSecretHeroLog.getValue(), 1, dbSecretHeroLog.getKey());
					heroRecordList.add(ioSecretHero);
				}
			}
			List<Integer> soldierList = enemyCostInfo.getSoldierUsedList();
			int idx = 0;
			for (Integer aSoldierHp : soldierList) {
				if(aSoldierHp != -1) {
					IOSecretHero ioSecretHero = new IOSecretHero(aSoldierHp, 2, -idx);
					heroRecordList.add(ioSecretHero);
				}
				idx++;
			}
			respMsg.enemy_cost = heroRecordList;
		}
		playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
	}

}
