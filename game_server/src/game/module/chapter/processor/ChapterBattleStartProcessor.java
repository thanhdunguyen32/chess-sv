package game.module.chapter.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.battle.logic.BattleManager;
import game.module.chapter.bean.BattleFormation;
import game.module.chapter.bean.ChapterBean;
import game.module.chapter.dao.BattleFormationCache;
import game.module.chapter.dao.BattleFormationDaoHelper;
import game.module.chapter.dao.ChapterCache;
import game.module.chapter.dao.ChapterTemplateCache;
import game.module.chapter.logic.BattleFormationManager;
import game.module.chapter.logic.ChapterManager;
import game.module.chapter.logic.PowerFormationManager;
import game.module.hero.bean.GeneralBean;
import game.module.hero.dao.GeneralCache;
import game.module.mythical.dao.MythicalTemplateCache;
import game.module.season.dao.SeasonCache;
import game.module.template.ChapterTemplate;
import game.module.user.logic.PlayerInfoManager;
import game.session.SessionManager;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;
import ws.WsMessageBattle;
import ws.WsMessageHall;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hexuhui
 */
@MsgCodeAnn(msgcode = WsMessageBattle.C2SChapterBattleStart.id, accessLimit = 200)
public class ChapterBattleStartProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(ChapterBattleStartProcessor.class);

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
        WsMessageBattle.C2SChapterBattleStart reqmsg = WsMessageBattle.C2SChapterBattleStart.parse(request);
        logger.info("chapter battle start!player={},req={}", playerId, reqmsg);
        //level check
        ChapterBean chapterBean = ChapterCache.getInstance().getPlayerChapter(playerId);
        Integer maxMapId = 0;
        if (chapterBean == null) {
            maxMapId = ChapterManager.getInstance().getInitMapId();
        } else {
            maxMapId = chapterBean.getMaxMapId();
        }
        ChapterTemplate chapterTemplate = ChapterTemplateCache.getInstance().getChapterTemplateById(maxMapId);
        if (playingRole.getPlayerBean().getLevel() < chapterTemplate.getLIMIT()) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageBattle.S2CChapterBattleStart.msgCode, 1716);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
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
                WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageBattle.S2CChapterBattleStart.msgCode, 1420);
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
        //ret
        WsMessageBattle.S2CChapterBattleStart respmsg = new WsMessageBattle.S2CChapterBattleStart();
        respmsg.battleid = SessionManager.getInstance().generateSessionId();
        BattleManager.getInstance().saveBattleId(playerId, respmsg.battleid);
        respmsg.seed = SessionManager.getInstance().generateSessionId();
        respmsg.season = SeasonCache.getInstance().getBattleSeason().getSeason();
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
