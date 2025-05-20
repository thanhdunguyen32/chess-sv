package game.module.activity.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.activity.bean.ActMjbg;
import game.module.activity.dao.ActMjbgCache;
import game.module.activity.dao.ActivityWeekTemplateCache;
import game.module.template.ZhdMjbgTemplate;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageActivity;
import ws.WsMessageBase;

import java.util.ArrayList;
import java.util.List;

@MsgCodeAnn(msgcode = WsMessageActivity.C2SMjbgInfo.id, accessLimit = 200)
public class MjbgInfoProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(MjbgInfoProcessor.class);

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
        logger.info("get mjbg info,playerId={}", playerId);
        //
        WsMessageActivity.S2CMjbgInfo respmsg = new WsMessageActivity.S2CMjbgInfo();
        ActMjbg actMjbg = ActMjbgCache.getInstance().getActMjbg(playerId);
        ZhdMjbgTemplate mjbgTemplate = ActivityWeekTemplateCache.getInstance().getMjbgTemplate();
        respmsg.djgsid = mjbgTemplate.getDjgsid();
        respmsg.djcount = mjbgTemplate.getDjcount();
        respmsg.index = 0;
        if (actMjbg != null) {
            respmsg.index = actMjbg.getIndex();
        }
        respmsg.items = new ArrayList<>();
        int idx = 0;
        for (ZhdMjbgTemplate.RewardTemplateMjbg rewardTemplateMjbg : mjbgTemplate.getItems()) {
            int pnum = 0;
            if (actMjbg != null && actMjbg.getRewardOpen() != null && actMjbg.getRewardOpen().containsKey(idx)) {
                pnum = actMjbg.getRewardOpen().get(idx);
            }
            WsMessageBase.IOMjbgItem ioMjbgItem = new WsMessageBase.IOMjbgItem(rewardTemplateMjbg.getGsid(), rewardTemplateMjbg.getCount(), pnum);
            respmsg.items.add(ioMjbgItem);
            idx++;
        }
        //box open
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 35; i++) {
            int isOpen = 0;
            if (actMjbg != null && actMjbg.getBoxOpen() != null && actMjbg.getBoxOpen().containsKey(i)) {
                isOpen = actMjbg.getBoxOpen().get(i);
            }
            sb.append(isOpen);
        }
        respmsg.strbox = sb.toString();
        respmsg.finallist = new ArrayList<>();
        for (List<ZhdMjbgTemplate.RewardTemplateMjbg> rewardTemplateMjbgs : mjbgTemplate.getFinallist()) {
            WsMessageBase.IOMjbgFinal ioMjbgFinal = new WsMessageBase.IOMjbgFinal();
            ioMjbgFinal.list = new ArrayList<>();
            respmsg.finallist.add(ioMjbgFinal);
            for (ZhdMjbgTemplate.RewardTemplateMjbg rewardTemplateMjbg : rewardTemplateMjbgs) {
                WsMessageBase.IOMjbgFinal1 ioMjbgFinal1 = new WsMessageBase.IOMjbgFinal1(rewardTemplateMjbg.getGsid(), rewardTemplateMjbg.getCount(),
                        rewardTemplateMjbg.getMaxnum());
                ioMjbgFinal.list.add(ioMjbgFinal1);
            }
        }
        respmsg.list = new ArrayList<>();
        for (ZhdMjbgTemplate.MjbgPageJump mjbgPageJump : mjbgTemplate.getList()) {
            WsMessageBase.IOMjbgSource ioMjbgSource = new WsMessageBase.IOMjbgSource(mjbgPageJump.getIntro(), mjbgPageJump.getPage());
            respmsg.list.add(ioMjbgSource);
        }
        if (actMjbg != null && actMjbg.getBigRewardIndex() != null) {
            Integer bigRewardIndex = actMjbg.getBigRewardIndex();
            ZhdMjbgTemplate.RewardTemplateMjbg rewardTemplateMjbg = mjbgTemplate.getFinallist().get(actMjbg.getIndex()).get(bigRewardIndex);
            respmsg._final = new WsMessageBase.IOMjbgItem(rewardTemplateMjbg.getGsid(), rewardTemplateMjbg.getCount(), actMjbg.getBigRewardGet() ? 1 : 0);
        }
        //ret
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
