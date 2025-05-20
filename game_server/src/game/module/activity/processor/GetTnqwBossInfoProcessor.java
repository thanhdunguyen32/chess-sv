package game.module.activity.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.activity.bean.ActTnqw;
import game.module.activity.dao.ActTnqwCache;
import game.module.activity.dao.ActivityWeekTemplateCache;
import game.module.template.ChapterBattleTemplate;
import game.module.template.RewardTemplateSimple;
import game.module.template.ZhdTnqwBossTemplate;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageActivity;
import ws.WsMessageBase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@MsgCodeAnn(msgcode = WsMessageActivity.C2SGetTnqwBossInfo.id, accessLimit = 200)
public class GetTnqwBossInfoProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(GetTnqwBossInfoProcessor.class);

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
        WsMessageActivity.C2SGetTnqwBossInfo reqmsg = WsMessageActivity.C2SGetTnqwBossInfo.parse(request);
        logger.info("get TnqwBossInfo,playerId={},req={}", playerId, reqmsg);
        //
        int boss_index = reqmsg.boss_index;
        WsMessageActivity.S2CGetTnqwBossInfo respmsg = new WsMessageActivity.S2CGetTnqwBossInfo();
        List<ZhdTnqwBossTemplate> tnqwBossTemplates = ActivityWeekTemplateCache.getInstance().getTnqwBossTemplates();
        ZhdTnqwBossTemplate zhdTnqwBossTemplate = tnqwBossTemplates.get(boss_index);
        respmsg.name = zhdTnqwBossTemplate.getName();
        respmsg.gsid = zhdTnqwBossTemplate.getGsid();
        respmsg.level = zhdTnqwBossTemplate.getLevel();
        respmsg.challrewards = new ArrayList<>();
        for (RewardTemplateSimple rewardTemplateSimple : zhdTnqwBossTemplate.getChallrewards()) {
            respmsg.challrewards.add(new WsMessageBase.RewardInfo(rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT()));
        }
        respmsg.killrewards = new ArrayList<>();
        for (RewardTemplateSimple rewardTemplateSimple : zhdTnqwBossTemplate.getKillrewards()) {
            respmsg.killrewards.add(new WsMessageBase.RewardInfo(rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT()));
        }
        //
        ActTnqw actTnqw = ActTnqwCache.getInstance().getActTnqw(playerId);
        respmsg.nowhp = zhdTnqwBossTemplate.getMaxhp();
        if (actTnqw != null && actTnqw.getNowHp() != null && actTnqw.getNowHp().containsKey(boss_index)) {
            respmsg.nowhp = actTnqw.getNowHp().get(boss_index);
        }
        respmsg.maxhp = zhdTnqwBossTemplate.getMaxhp();
        respmsg.lastdamge = 0;
        if (actTnqw != null && actTnqw.getLastDamage() != null && actTnqw.getLastDamage().containsKey(boss_index)) {
            respmsg.lastdamge = actTnqw.getLastDamage().get(boss_index);
        }
        respmsg.last = 0;
        respmsg.bset = new HashMap<>();
        for (Map.Entry<Integer, ChapterBattleTemplate> aEntry : zhdTnqwBossTemplate.getBset().entrySet()) {
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
