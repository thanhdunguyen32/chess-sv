package cross.processor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cross.logic.CraftRequestManager;
import game.entity.PlayerCacheStatus.PlayerPosition;
import game.entity.PlayingRole;
import game.module.battle.ProtoMessageBattle.C2SBeginMatchCross;
import game.module.battle.ProtoMessageBattle.S2CBeginMatchCross;
import game.module.craft.bean.CraftBean;
import game.module.craft.bean.CraftRoom.CraftPlayerInfo;
import game.module.craft.dao.CraftBeanCache;
import game.module.craft.logic.CraftMatchManager;
import game.module.craft.logic.CraftMatchManager.CraftMatchEntity;
import game.module.cross.bean.CrossCraftBegin;
import game.module.cross.bean.CrossHeroEntity;
import game.module.hero.bean.HeroEntity;
import game.module.hero.dao.HeroCache;
import game.module.user.bean.PlayerBean;
import game.session.SessionManager;
import lion.common.MsgCodeAnn;
import lion.netty4.codec.ProtoUtil;
import lion.netty4.message.GamePlayer;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestMessage;
import lion.netty4.message.RequestProtoMessage;
import lion.netty4.processor.MsgProcessor;
import lion.netty4.proto.RpcBaseProto.RetCode;

@MsgCodeAnn(msgcode = 36055, accessLimit = 200)
public class CrossCraftBeginMatchProcessor extends MsgProcessor {

	private static Logger logger = LoggerFactory.getLogger(CrossCraftBeginMatchProcessor.class);

	@Override
	public void process(GamePlayer session, RequestMessage request) throws Exception {

	}

	@Override
	public void process(GamePlayer player, RequestProtoMessage request) throws Exception {
		C2SBeginMatchCross reqMsg = ProtoUtil.getProtoObj(C2SBeginMatchCross.PARSER, request);
		long sessionId = reqMsg.getSessionId();
		logger.info("cross craft begin,sessionId={}", sessionId);
		CrossCraftBegin crossCraftBegin = CraftRequestManager.getInstance().checkExist(sessionId);
		if (crossCraftBegin == null) {
			player.writeAndFlush(36056, RetCode.CRAFT_BATTLE_NOT_EXIST);
			return;
		}
		// 玩家信息
		PlayingRole playingRole = null;
		int playerId = crossCraftBegin.getPlayerId();
		int serverId = crossCraftBegin.getServerId();
		Long existSessionId = player.getSessionId();
		if (existSessionId == null) {
			// 添加玩家
			PlayerBean pb = new PlayerBean();
			pb.setId(playerId);
			pb.setServerId(serverId);
			pb.setName(crossCraftBegin.getPlayerName());
			pb.setIcon(crossCraftBegin.getPlayerIcon());
			pb.setHeadFrame(crossCraftBegin.getHeadFrame());
			playingRole = new PlayingRole(player, pb);
			Long nettySessionId = SessionManager.getInstance().create(playingRole);
			player.saveSessionId(nettySessionId);
		} else {
			playingRole = SessionManager.getInstance().visit(existSessionId);
		}
		// 添加pvp缓存
		CraftBean craftBean = new CraftBean();
		craftBean.setPlayerId(crossCraftBegin.getPlayerId());
		craftBean.setLevel(crossCraftBegin.getCraftLevel());
		CraftBeanCache.getInstance().addNewEntity(crossCraftBegin.getPlayerId(), crossCraftBegin.getServerId(),
				craftBean);
		// 添加英雄缓存
		List<CrossHeroEntity> heList = crossCraftBegin.getHeroEntityList();
		Map<Integer, HeroEntity> heroAll = new HashMap<>();
		for (CrossHeroEntity crossHeroEntity : heList) {
			HeroEntity he = new HeroEntity();
			he.setTemplateId(crossHeroEntity.getTemplateId());
			he.setLevel(crossHeroEntity.getLevel());
			he.setAdvanceGrade(crossHeroEntity.getQuality());
			he.setEvolutionGrade(crossHeroEntity.getStar());
			he.setEquimentPack(crossHeroEntity.getEquipPack());
			he.setRunePack(crossHeroEntity.getRunePack());
			he.setSkillPack(crossHeroEntity.getSkillPack());
			heroAll.put(he.getTemplateId(), he);
		}
		HeroCache.getInstance().setCraftPlayerHeros(new CraftPlayerInfo(serverId, playerId), heroAll);
		playingRole.getPlayerCacheStatus().setPosition(PlayerPosition.PLAYER_POSITION_CRAFT_MATCHING);
		// 添加匹配
		CraftMatchManager.getInstance().addNew(playingRole.getId(), playingRole.getPlayerBean().getServerId(),
				new CraftMatchEntity(playingRole), craftBean);
		CraftRequestManager.getInstance().remove(sessionId);
		// ret
		player.writeAndFlush(36056, S2CBeginMatchCross.newBuilder().setRet(0));
	}

	@Override
	public void process(GamePlayer session, MyRequestMessage request) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
