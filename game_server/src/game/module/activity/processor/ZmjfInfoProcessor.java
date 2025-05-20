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

@MsgCodeAnn(msgcode = WsMessageActivity.C2SZmjfInfo.id, accessLimit = 200)
public class ZmjfInfoProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(ZmjfInfoProcessor.class);

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
        logger.info("get zmjf info,playerId={}", playerId);
        //
        WsMessageActivity.S2CZmjfInfo respmsg = new WsMessageActivity.S2CZmjfInfo();
        ZhdDjjfTemplate zmjfTemplate = ActivityWeekTemplateCache.getInstance().getZmjfTemplate();
        respmsg.looplimit = zmjfTemplate.getLooplimit();
        respmsg.currentloop = zmjfTemplate.getCurrentloop();
        respmsg.missions = new ArrayList<>();
        List<ZhdDjjfTemplate.ZhdDjjfMission> missions = zmjfTemplate.getMissions();
        for (ZhdDjjfTemplate.ZhdDjjfMission zhdZmjfMission : missions) {
            WsMessageBase.IODjjfMission ioZmjfMission = new WsMessageBase.IODjjfMission();
            ioZmjfMission.cur = zhdZmjfMission.getCur();
            ioZmjfMission.NAME = zhdZmjfMission.getNAME();
            ioZmjfMission.NUM = zhdZmjfMission.getNUM();
            ioZmjfMission.ITEMS = new ArrayList<>();
            for (RewardTemplateSimple rewardTemplateSimple : zhdZmjfMission.getITEMS()) {
                ioZmjfMission.ITEMS.add(new WsMessageBase.RewardInfo(rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT()));
            }
            respmsg.missions.add(ioZmjfMission);
        }
        //ret
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
