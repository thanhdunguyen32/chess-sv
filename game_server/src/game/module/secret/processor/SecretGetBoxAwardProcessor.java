package game.module.secret.processor;

import java.util.ArrayList;
import java.util.List;

import game.module.award.bean.GameConfig;
import game.module.award.logic.AwardUtils;
import lion.netty4.message.RequestByteMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import db.proto.ProtoMessageSecret.DBSecretBoxAward;
import db.proto.ProtoMessageSecret.SecretStageAward;
import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.activity.constants.ActivityConstants;
import game.module.activity.logic.ActivityManager;
import game.module.award.logic.AwardManager;
import game.module.box.bean.ItemPair;
import game.module.gm.logic.GmManager;
import game.module.log.constants.LogConstants;
import game.module.pay.logic.PaymentConstants;
import game.module.secret.bean.Secret;
import game.module.secret.bean.SecretTemplate;
import game.module.secret.dao.SecretCache;
import game.module.secret.dao.SecretTemplateCache;
import game.module.secret.logic.SecretManager;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestProtoMessage;
import ws.WsMessageHall.S2CErrorCode;
import ws.WsMessageSecret.C2SSecretGetAward;
import ws.WsMessageSecret.S2CSecretGetAward;
import ws.WsMessageBase.SecretItemInfo;

/**
 * 领取随机奖励
 * 
 * @author zhangning
 * 
 * @Date 2015年1月27日 下午6:29:25
 */
@MsgCodeAnn(msgcode = C2SSecretGetAward.id, accessLimit = 500)
public class SecretGetBoxAwardProcessor extends PlayingRoleMsgProcessor {

	private static Logger logger = LoggerFactory.getLogger(SecretGetBoxAwardProcessor.class);

	@Override
	public void processByte(PlayingRole playingRole, RequestByteMessage request) throws Exception {

	}

	/**
	 * 领取随机奖励
	 */
	@Override
	public void processProto(PlayingRole playingRole, RequestProtoMessage request) throws Exception {
		
	}

	private List<ItemPair> getBoxAward(String playerName, Secret secret, int stageIndex) {
		int mapId = secret.getMapId();
		SecretTemplate secretTemplate = SecretTemplateCache.getInstance().getSecretTemp(mapId);
		int[] stageStrList = secretTemplate.getAward_chest();
		int awardId = stageStrList[stageIndex];
		List<ItemPair> retList = AwardManager.getInstance().getAward(awardId);
		//
		int[] copper_award_list = secretTemplate.getCopper_award();
		int copper_award = copper_award_list[stageIndex];
		retList.add(new ItemPair(GameConfig.PLAYER.GOLD, copper_award));
		//
		int superAwardId = secretTemplate.getSuper_award();
		List<ItemPair> superList = AwardManager.getInstance().getAward(superAwardId);
		if (superList.size() > 0) {
			GmManager.getInstance().sendSecretBoxMarquee(playerName);
		}
		retList.addAll(superList);
		return retList;
	}

	/**
	 * 添加奖励
	 * 
	 * @param playingRole
	 */
	public void addReward(final PlayingRole playingRole, List<ItemPair> dbSecretAwards) {
		if (dbSecretAwards == null || dbSecretAwards.isEmpty()) {
			return;
		}
		int awardDouble = ActivityManager.getInstance().getMoudelMultiple(ActivityConstants.AWARD_DOUBLE_SECRET);
		for (ItemPair award : dbSecretAwards) {
			AwardUtils.changeRes(playingRole, award.getItemTemplateId(), award.getCount()*awardDouble*2,
					PaymentConstants.PAYMENT_SECRET_RANDOM_AWARD);
		}
	}

	@Override
	public void processMy(PlayingRole playingRole, MyRequestMessage request) throws Exception {
		C2SSecretGetAward randomAwardParam = C2SSecretGetAward.parse(request);
		int stageIndex = randomAwardParam.stage_index;
		int playerId = playingRole.getId();
		logger.info("Secret get box stageIndex={},playerId={}", stageIndex, playerId);
		// 参数有误
		if (stageIndex < 0) {
			S2CErrorCode respMsg = new S2CErrorCode(S2CSecretGetAward.msgCode, 601);
			playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
			return;
		}

		// 没有秘密基地记录
		SecretCache secretCache = SecretCache.getInstance();
		Secret secret = secretCache.getSecret(playingRole.getId());
		if (secret == null) {
			S2CErrorCode respMsg = new S2CErrorCode(S2CSecretGetAward.msgCode,601);
			playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
			return;
		}

		// 副本未通关
		if (stageIndex >= secret.getProgress()) {
			S2CErrorCode respMsg = new S2CErrorCode(S2CSecretGetAward.msgCode,606);
			playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
			return;
		}

		// 是否已经领取
		DBSecretBoxAward dbRandomAward = secret.getBoxAward();
		if (dbRandomAward != null) {
			List<SecretStageAward> randomAwards = dbRandomAward.getAwardsList();
			if (randomAwards != null) {
				for (SecretStageAward secretStageAward : randomAwards) {
					if (secretStageAward.getStageIndex() == stageIndex && secretStageAward.getIsGet() > 0) {
						S2CErrorCode respMsg = new S2CErrorCode(S2CSecretGetAward.msgCode,620);
						playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
						return;
					}
				}
			}
		}
		// 领奖
		List<ItemPair> awardList = getBoxAward(playingRole.getPlayerBean().getName(), secret, stageIndex);
		int awardDouble = ActivityManager.getInstance().getMoudelMultiple(ActivityConstants.AWARD_DOUBLE_SECRET);
		if (awardList != null) {
			addReward(playingRole, awardList);
			// 添加记录
			DBSecretBoxAward.Builder boxAwardBuilder = dbRandomAward.toBuilder();
			SecretStageAward.Builder award1Builder = boxAwardBuilder.getAwardsBuilder(stageIndex).setIsGet(1);
			for (ItemPair aItemPair : awardList) {
				award1Builder.addSecretAwardBuilder().setAwardId(aItemPair.getItemTemplateId()).setAwardCnt(aItemPair.getCount()*awardDouble);
			}
			dbRandomAward = boxAwardBuilder.build();
			secret.setBoxAward(dbRandomAward);
			SecretManager.getInstance().asyncUpdateRandomAward(secret);
			// send msg
			S2CSecretGetAward respMsg = new S2CSecretGetAward();
			respMsg.stage_index =stageIndex;
			respMsg.reward_items = new ArrayList<>(awardList.size());
			int i = 0;
			for (ItemPair aItemPair : awardList) {
				respMsg.reward_items.add(new SecretItemInfo(aItemPair.getItemTemplateId(), aItemPair.getCount()*awardDouble*2));
			}
			playingRole.getGamePlayer().writeAndFlush(respMsg.build(playingRole.alloc()));
		}
	}

}
