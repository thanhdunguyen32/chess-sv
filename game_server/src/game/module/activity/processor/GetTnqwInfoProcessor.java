package game.module.activity.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.activity.bean.ActTnqw;
import game.module.activity.dao.ActTnqwCache;
import game.module.activity.dao.ActivityWeekTemplateCache;
import game.module.item.logic.ItemManager;
import game.module.template.ZhdTnqwTemplate;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageActivity;
import ws.WsMessageBase;

import java.util.ArrayList;
import java.util.Map;

@MsgCodeAnn(msgcode = WsMessageActivity.C2SGetTnqwInfo.id, accessLimit = 200)
public class GetTnqwInfoProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(GetTnqwInfoProcessor.class);

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
        logger.info("get TnqwInfo,playerId={}", playerId);
        //
        WsMessageActivity.S2CGetTnqwInfo respmsg = new WsMessageActivity.S2CGetTnqwInfo();
        ZhdTnqwTemplate tnqwTemplate = ActivityWeekTemplateCache.getInstance().getTnqwTemplate();
        respmsg.event = new ArrayList<>();
        int markSum = 0;
        for (ZhdTnqwTemplate.ZhdTnqwEvent zhdTnqwEvent : tnqwTemplate.getEvent()) {
            WsMessageBase.IOTnqwEvent tnqwEvent = new WsMessageBase.IOTnqwEvent(zhdTnqwEvent.getMark(), zhdTnqwEvent.getLimit(), zhdTnqwEvent.getIntro());
            respmsg.event.add(tnqwEvent);
            int markCount = ItemManager.getInstance().getCount(playingRole.getPlayerBean(), zhdTnqwEvent.getMark());
            markSum += markCount;
        }
        respmsg.bosslist = new ArrayList<>();
        int idx = 0;
        ActTnqw actTnqw = ActTnqwCache.getInstance().getActTnqw(playerId);
        Map<Integer, Long> nowHpMap = null;
        if (actTnqw != null) {
            nowHpMap = actTnqw.getNowHp();
        }
        for (ZhdTnqwTemplate.ZhdTnqwBosslist zhdTnqwBosslist : tnqwTemplate.getBosslist()) {
            WsMessageBase.IOTnqwBosslist tnqwBosslist = new WsMessageBase.IOTnqwBosslist();
            tnqwBosslist.status = 0;
            if (zhdTnqwBosslist.getActscore() != null) {
                tnqwBosslist.status = markSum >= zhdTnqwBosslist.getActscore() ? 1 : 0;
                tnqwBosslist.actscore = zhdTnqwBosslist.getActscore();
            } else {
                //大 boss
                boolean littleAllDie = true;
                for (int i = 0; i < tnqwTemplate.getBosslist().size() - 1; i++) {
                    if (actTnqw == null || !actTnqw.getNowHp().containsKey(i) || actTnqw.getNowHp().get(i) > 0) {
                        littleAllDie = false;
                        break;
                    }
                }
                if (littleAllDie) {
                    tnqwBosslist.status = 1;
                }
            }
            //已经击杀
            if (nowHpMap != null && nowHpMap.containsKey(idx) && nowHpMap.get(idx) <= 0) {
                tnqwBosslist.status = 2;
            }
            tnqwBosslist.rewardgsids = zhdTnqwBosslist.getRewardgsids();
            respmsg.bosslist.add(tnqwBosslist);
            idx++;
        }
        //ret
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
