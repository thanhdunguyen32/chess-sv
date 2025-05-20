package cross.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayerCacheStatus.PlayerPosition;
import game.entity.PlayingRole;
import ws.WsMessageBattle.C2SCraftSkillEffect;
import ws.WsMessageBattle.PVP_BUFF_TYPE;
import ws.WsMessageBattle.PushCraftAddBuff;
import ws.WsMessageBattle.S2CCraftSkillEffect;
import ws.WsMessageBattle.S2CPvpSwitchState;
import game.module.craft.bean.CraftRoom;
import game.module.craft.bean.CraftRoom.CraftPlayerInfo;
import game.module.craft.bean.CraftRoomPlayer;
import game.module.craft.logic.CraftManager;
import game.module.pvp.dao.PvpSkillTemplateCache;
import game.module.template.PvpSkillTemplate;
import lion.common.MsgCodeAnn;
import lion.netty4.codec.ProtoUtil;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestMessage;
import lion.netty4.message.RequestProtoMessage;
import lion.netty4.proto.RpcBaseProto.RetCode;

@MsgCodeAnn(msgcode = 36015, accessLimit = 0)
public class CraftSkillEffectProcessor extends PlayingRoleMsgProcessor {

	private static Logger logger = LoggerFactory.getLogger(CraftSkillEffectProcessor.class);

	@Override
	public void process(PlayingRole playingRole, RequestMessage requestMessage) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void processProto(PlayingRole playingRole, RequestProtoMessage request) throws Exception {
		C2SCraftSkillEffect skillEffectMsg = ProtoUtil.getProtoObj(C2SCraftSkillEffect.PARSER, request);
		logger.info("craft after skill effect,player={}", playingRole.getId());
		// 玩家状态不正确
		if (playingRole.getPlayerCacheStatus().getPosition() != PlayerPosition.PLAYER_POSITION_CRAFT_BATTLE) {
			logger.error("playerId={},error={}", playingRole.getId(), RetCode.CRAFT_PLAYER_STATUS_ERROR);
			playingRole.getGamePlayer().writeAndFlush(36016, S2CPvpSwitchState.newBuilder().build());
			return;
		}
		// 战斗是否存在
		Integer craftRoomId = playingRole.getPlayerCacheStatus().getCraftRoomId();
		if (craftRoomId == null || CraftManager.getInstance().getCraftRoom(craftRoomId) == null) {
			playingRole.getGamePlayer().writeAndFlush(36016, RetCode.CRAFT_BATTLE_NOT_EXIST);
			return;
		}
		// 玩家信息是否存在
		final CraftRoom craftRoom = CraftManager.getInstance().getCraftRoom(craftRoomId);
		int playerId = playingRole.getId();
		int serverId = playingRole.getPlayerBean().getServerId();
		final CraftRoomPlayer craftRoomPlayer = craftRoom.getOnlinePlayers().get(new CraftPlayerInfo(serverId,playerId));
		if (craftRoomPlayer == null) {
			playingRole.getGamePlayer().writeAndFlush(36016, RetCode.CRAFT_PLAYER_NOT_EXIST);
			return;
		}
		// do
		int attackerId = skillEffectMsg.getAttackerId();
		int attackerServerId = skillEffectMsg.getAttackerServerId();
		int attackerHeroId = skillEffectMsg.getAttackerHeroId();
		int attackeeId = skillEffectMsg.getAttackeeId();
		int attackeeServerId = skillEffectMsg.getAttackeeServerId();
		int attackeeHeroId = skillEffectMsg.getAttackeeHeroId();
		int skillId = skillEffectMsg.getSkillId();
		int skillLevel = skillEffectMsg.getSkillLevel();
		int hurtVal = skillEffectMsg.getHurtVal();
		PvpSkillTemplate skillTemplate = PvpSkillTemplateCache.getInstance().getPvpSkillTemplateById(skillId);
		int buffType = skillTemplate.getBuff_type();
		// 命中概率
//		boolean isHit = true;
//		if (buffType == 1 || buffType == 2 || buffType == 7 || buffType == 9 || buffType == 10 || buffType == 11
//				|| buffType == 14 || buffType == 15 || buffType == 16 || buffType == 17) {
//			HeroEntity targetHeroEntity = HeroCache.getInstance().getHero(attackeeId, attackeeHeroId);
//			int attackeeLevel = ExpTemplateCache.getInstance().getMaxLevel();
//			if(targetHeroEntity != null){
//				attackeeLevel = targetHeroEntity.getLevel();
//			}
//			float yunRate = BattleFormula.yunHitRate(skillLevel, attackeeLevel);
//			if (RandomUtils.nextFloat(0, 1) >= yunRate) {
//				isHit = false;
//			}
//		}
//		if (!isHit) {
//			playingRole.getGamePlayer().writeAndFlush(36016, S2CSkillEffect.newBuilder());
//			return;
//		}
		// do
		PVP_BUFF_TYPE buff_TYPE = PVP_BUFF_TYPE.PASSIVE;
		if (buffType == 1 || buffType == 2 || buffType == 7 || buffType == 10 || buffType == 11 || buffType == 14
				|| buffType == 16|| buffType == 25 || buffType == 27 || buffType == 31) {
			// action buff
			buff_TYPE = PVP_BUFF_TYPE.ACTION;
		}
		// push
		PushCraftAddBuff.Builder pushBuider = PushCraftAddBuff.newBuilder().setAttackerId(attackerId)
				.setAttackeeId(attackeeId).setAttackerServerId(attackerServerId).setAttackeeServerId(attackeeServerId)
				.setAttackerHeroId(attackerHeroId).setAttackeeHeroId(attackeeHeroId).setBuffType(buff_TYPE)
				.setHurtVal(hurtVal).setSkillId(skillId).setSkillLevel(skillLevel).addAllEffectPosition(skillEffectMsg.getEffectPositionList());
		for (CraftRoomPlayer aPlayer : craftRoom.getOnlinePlayers().values()) {
			if (aPlayer.getPlayingRole() != null && aPlayer.getPlayingRole().getGamePlayer() != null) {
				aPlayer.getPlayingRole().getGamePlayer().writeAndFlush(36018, pushBuider);
			}
		}
		// ret
		playingRole.getGamePlayer().writeAndFlush(36016, S2CCraftSkillEffect.newBuilder().build());
	}

	@Override
	public void processWebsocket(PlayingRole playingRole, MyRequestMessage request) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
