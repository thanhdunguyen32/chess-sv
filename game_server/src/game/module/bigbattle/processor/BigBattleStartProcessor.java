package game.module.bigbattle.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.award.bean.GameConfig;
import game.module.award.logic.AwardUtils;
import game.module.battle.logic.BattleManager;
import game.module.bigbattle.dao.BigChapterTemplateCache;
import game.module.bigbattle.logic.BigBattleConstants;
import game.module.chapter.bean.BattleFormation;
import game.module.chapter.dao.BattleFormationCache;
import game.module.chapter.dao.BattleFormationDaoHelper;
import game.module.chapter.logic.BattleFormationManager;
import game.module.chapter.logic.PowerFormationManager;
import game.module.hero.bean.GeneralBean;
import game.module.hero.dao.GeneralCache;
import game.module.log.constants.LogConstants;
import game.module.mission.constants.MissionConstants;
import game.module.mythical.dao.MythicalTemplateCache;
import game.module.season.dao.SeasonCache;
import game.module.template.BigChapterTemplate;
import game.module.template.VipTemplate;
import game.module.user.bean.PlayerProp;
import game.module.user.dao.PlayerOtherCache;
import game.module.user.logic.PlayerInfoManager;
import game.module.vip.dao.VipTemplateCache;
import game.session.SessionManager;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;
import ws.WsMessageBattle;
import ws.WsMessageBigbattle;
import ws.WsMessageHall;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hexuhui
 */
@MsgCodeAnn(msgcode = WsMessageBigbattle.C2SBigBattleStart.id, accessLimit = 200)
public class BigBattleStartProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(BigBattleStartProcessor.class);

    @Override
    public void processByte(PlayingRole playingRole, RequestByteMessage request) throws Exception {

    }

    /**
     * 加载所有邮件
     */
    @Override
    public void processProto(PlayingRole playingRole, RequestProtoMessage request) throws Exception {
    }

    @Override
    public void processMy(PlayingRole playingRole, MyRequestMessage request) throws Exception {
        int playerId = playingRole.getId();
        WsMessageBigbattle.C2SBigBattleStart reqmsg = WsMessageBigbattle.C2SBigBattleStart.parse(request);
        logger.info("big battle start!player={},req={}", playerId, reqmsg);
        //can attack
        int mapid = reqmsg.mapid;
        BigChapterTemplate bigChapterTemplate = BigChapterTemplateCache.getInstance().getBigChapterTemplateById(mapid);
        //level
        if (playingRole.getPlayerBean().getLevel() < bigChapterTemplate.getLOCK()) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageBigbattle.S2CBigBattleStart.msgCode, 1711);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //count
        Integer passid = bigChapterTemplate.getPASSID();
        Integer passcount = bigChapterTemplate.getPASSCOUNT();
        Map<Integer, PlayerProp> playerOthers = PlayerOtherCache.getInstance().getPlayerOther(playerId);
        int my_passcount = 0;
        if (playerOthers.containsKey(passid)) {
            my_passcount = playerOthers.get(passid).getCount();
        }
        if (my_passcount != passcount - 1) {//已经通关，或者需要通关上一关卡
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageBigbattle.S2CBigBattleStart.msgCode, 1712);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //count max
        VipTemplate vipTemplate = VipTemplateCache.getInstance().getVipTemplate(playingRole.getPlayerBean().getVipLevel());
        int typeIndex = BigBattleConstants.getTypeIndexByMapId(mapid);
        BigBattleConstants.BigBattleCountGsid bigBattleCountGsid = BigBattleConstants.BIG_BATTLE_COUNT_CONFIG[typeIndex];
        int attackCount = 0;
        if (playerOthers.containsKey(bigBattleCountGsid.COUNTGSID)) {
            attackCount = playerOthers.get(bigBattleCountGsid.COUNTGSID).getCount();
        }
        if (attackCount >= vipTemplate.getRIGHT().getBIGBATTLE()) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageBigbattle.S2CBigBattleStart.msgCode, 1713);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //cost yuanbao
        int freeCount = 0;
        if (playerOthers.containsKey(bigBattleCountGsid.FREE)) {
            freeCount = playerOthers.get(bigBattleCountGsid.FREE).getCount();
        }
        if (freeCount >= bigBattleCountGsid.COUNTLIMIT && playingRole.getPlayerBean().getMoney() < BigBattleConstants.COST_YB) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageBigbattle.S2CBigBattleStart.msgCode, 1714);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //do
        //count++
        if (freeCount < bigBattleCountGsid.COUNTLIMIT) {
            AwardUtils.changeRes(playingRole, bigBattleCountGsid.FREE, 1, LogConstants.BIG_BATTLE);
        } else {
            AwardUtils.changeRes(playingRole, GameConfig.PLAYER.YB, -BigBattleConstants.COST_YB, LogConstants.BIG_BATTLE);
        }
        AwardUtils.changeRes(playingRole, bigBattleCountGsid.COUNTGSID, 1, LogConstants.BIG_BATTLE);
        //save normal formation
        BattleFormation battleFormation = BattleFormationCache.getInstance().getBattleFormation(playerId);
        if (battleFormation == null) {
            battleFormation = BattleFormationManager.getInstance().createBattleFormation(playerId);
        }
        int normalFormationIndex = 0;
        int mythic = reqmsg.mythic;
        if (mythic > 0) {
            //check mythic exist
            boolean containsTemplate = MythicalTemplateCache.getInstance().containsTemplate(mythic);
            if (!containsTemplate) {
                WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageBigbattle.S2CBigBattleStart.msgCode, 1420);
                playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
                return;
            }
            Map<Integer, Integer> mythicsMap = battleFormation.getMythics();
            if (mythicsMap == null) {
                mythicsMap = new HashMap<>();
                battleFormation.setMythics(mythicsMap);
            }
            mythicsMap.put(normalFormationIndex, mythic);
        }
        //team
        Map<Integer, Long> normalMap = battleFormation.getNormal();
        if (normalMap == null) {
            normalMap = new HashMap<>();
            battleFormation.setNormal(normalMap);
        } else {
            normalMap.clear();
        }
        WsMessageBase.IOFormationGeneralPos[] items = reqmsg.items;
        for (WsMessageBase.IOFormationGeneralPos aitem : items) {
            normalMap.put(aitem.pos, aitem.general_uuid);
        }
        //save formation
        if (battleFormation.getId() != null) {
            BattleFormationDaoHelper.asyncUpdateBattleFormation(battleFormation);
        } else {
            BattleFormationCache.getInstance().addBattleFormation(battleFormation);
            BattleFormationDaoHelper.asyncInsertBattleFormation(battleFormation);
        }
        //update player power
        int sumPower = 0;
        Map<Long, GeneralBean> generalMap = GeneralCache.getInstance().getHeros(playerId);
        for (WsMessageBase.IOFormationGeneralPos aitem : items) {
            long general_uuid = aitem.general_uuid;
            GeneralBean generalBean = generalMap.get(general_uuid);
            if (generalBean != null) {
                sumPower += generalBean.getPower();
            }
        }
        PowerFormationManager.getInstance().refreshPowerFormation(playingRole.getPlayerBean().getPower(),sumPower,mythic,normalMap,playingRole);
        PlayerInfoManager.getInstance().saveMaxPower(playingRole, sumPower);
        //update mission progress
        AwardUtils.changeRes(playingRole, MissionConstants.BIG_BATTLE_PMARK, 1, LogConstants.BIG_BATTLE);
        AwardUtils.changeRes(playingRole, MissionConstants.BIG_BATTLE, 1, LogConstants.BIG_BATTLE);
        //ret
        WsMessageBigbattle.S2CBigBattleStart respmsg = new WsMessageBigbattle.S2CBigBattleStart();
        respmsg.battleid = SessionManager.getInstance().generateSessionId();
        BattleManager.getInstance().saveBattleId(playerId, respmsg.battleid);
        respmsg.seed = SessionManager.getInstance().generateSessionId();
        respmsg.season = SeasonCache.getInstance().getBattleSeason().getSeason();
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
