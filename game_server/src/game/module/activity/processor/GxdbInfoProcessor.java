package game.module.activity.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.activity.dao.ActivityWeekTemplateCache;
import game.module.template.RewardTemplateSimple;
import game.module.template.ZhdDjjfTemplate;
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

@MsgCodeAnn(msgcode = WsMessageActivity.C2SGxdbInfo.id, accessLimit = 200)
public class GxdbInfoProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(GxdbInfoProcessor.class);

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
        logger.info("get Gxdb info,playerId={}", playerId);
        //
        WsMessageActivity.S2CGxdbInfo respmsg = new WsMessageActivity.S2CGxdbInfo();
        ZhdDjjfTemplate gxdbTemplate = ActivityWeekTemplateCache.getInstance().getGxdbTemplate();
        respmsg.looplimit = gxdbTemplate.getLooplimit();
        respmsg.currentloop = gxdbTemplate.getCurrentloop();
        respmsg.missions = new ArrayList<>();
        List<ZhdDjjfTemplate.ZhdDjjfMission> missions = gxdbTemplate.getMissions();
        for (ZhdDjjfTemplate.ZhdDjjfMission zhdDjjfMission : missions) {
            WsMessageBase.IODjjfMission ioDjjfMission = new WsMessageBase.IODjjfMission();
            ioDjjfMission.cur = zhdDjjfMission.getCur();
            ioDjjfMission.NAME = zhdDjjfMission.getNAME();
            ioDjjfMission.NUM = zhdDjjfMission.getNUM();
            ioDjjfMission.ITEMS = new ArrayList<>();
            for (RewardTemplateSimple rewardTemplateSimple : zhdDjjfMission.getITEMS()) {
                ioDjjfMission.ITEMS.add(new WsMessageBase.RewardInfo(rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT()));
            }
            respmsg.missions.add(ioDjjfMission);
        }
        //ret
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
