package game.module.activity.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.activity.dao.ActivityWeekTemplateCache;
import game.module.template.ChapterBattleTemplate;
import game.module.template.ZhdTgslTemplate;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageActivity;
import ws.WsMessageBase;

import java.util.HashMap;
import java.util.Map;

@MsgCodeAnn(msgcode = WsMessageActivity.C2STgslInfo.id, accessLimit = 200)
public class TgslInfoProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(TgslInfoProcessor.class);

    @Override
    public void processByte(PlayingRole playingRole, RequestByteMessage request) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void processProto(PlayingRole playingRole, RequestProtoMessage request) throws Exception {

    }

    @Override
    public void processMy(PlayingRole playingRole, MyRequestMessage request) throws Exception {
        int playerId = playingRole.getId();
        logger.info("tgsl info,playerId={}", playerId);
        //
        WsMessageActivity.S2CTgslInfo respmsg = new WsMessageActivity.S2CTgslInfo();
        ZhdTgslTemplate tgslTemplate = ActivityWeekTemplateCache.getInstance().getTgslTemplate();
        respmsg.gsid = tgslTemplate.getBoss().getGsid();
        respmsg.name = tgslTemplate.getBoss().getName();
        respmsg.mark = tgslTemplate.getBoss().getMark();
        respmsg.skill = tgslTemplate.getBoss().getSkill();
        respmsg.challrewards = tgslTemplate.getBoss().getChallrewards();
        respmsg.level = tgslTemplate.getBoss().getLevel();
        respmsg.bset = new HashMap<>();
        for (Map.Entry<Integer, ChapterBattleTemplate> aEntry : tgslTemplate.getBoss().getBset().entrySet()) {
            respmsg.bset.put(aEntry.getKey(), buildBsetGeneral(aEntry.getValue()));
        }
        //ret
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

    private WsMessageBase.IOGeneralLegion buildBsetGeneral(ChapterBattleTemplate chapterBattleTemplate) {
        return new WsMessageBase.IOGeneralLegion(chapterBattleTemplate.getGsid(), chapterBattleTemplate.getLevel(),
                chapterBattleTemplate.getHpcover(), chapterBattleTemplate.getPclass(), chapterBattleTemplate.getExhp().longValue(),
                chapterBattleTemplate.getExatk().intValue());
    }

}
