package game.module.activity.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.activity.dao.ActivityWeekTemplateCache;
import game.module.template.RewardTemplateSimple;
import game.module.template.ZhdJfbxTemplate;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageActivity;
import ws.WsMessageBase;

import java.util.ArrayList;

@MsgCodeAnn(msgcode = WsMessageActivity.C2SGetJfbx.id, accessLimit = 200)
public class GetJfbxProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(GetJfbxProcessor.class);

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
        logger.info("get jfbx,playerId={}", playerId);
        //
        WsMessageActivity.S2CGetJfbx respmsg = new WsMessageActivity.S2CGetJfbx();
        ZhdJfbxTemplate jfbxTemplate = ActivityWeekTemplateCache.getInstance().getJfbxTemplate();
        respmsg.event = new ArrayList<>();
        for (ZhdJfbxTemplate.ZhdJfbxEvent zhdJfbxEvent : jfbxTemplate.getEvent()) {
            WsMessageBase.IOJfbxEvent jfbxEvent = new WsMessageBase.IOJfbxEvent(zhdJfbxEvent.getMARK(), zhdJfbxEvent.getLIMIT(), zhdJfbxEvent.getIntro());
            respmsg.event.add(jfbxEvent);
        }
        respmsg.box = new ArrayList<>();
        for (ZhdJfbxTemplate.ZhdJfbxBox zhdJfbxBox : jfbxTemplate.getBox()) {
            WsMessageBase.IOJfbxBox ioJfbxBox = new WsMessageBase.IOJfbxBox();
            ioJfbxBox.SCORE = zhdJfbxBox.getSCORE();
            ioJfbxBox.state = zhdJfbxBox.getState();
            ioJfbxBox.REWARD = new ArrayList<>();
            for (RewardTemplateSimple rewardTemplateSimple : zhdJfbxBox.getREWARD()) {
                ioJfbxBox.REWARD.add(new WsMessageBase.RewardInfo(rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT()));
            }
            respmsg.box.add(ioJfbxBox);
        }
        //ret
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
