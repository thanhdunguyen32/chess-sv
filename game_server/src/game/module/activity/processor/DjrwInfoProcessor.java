package game.module.activity.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.activity.dao.ActivityWeekTemplateCache;
import game.module.template.RewardTemplateSimple;
import game.module.template.ZhdDjrwTemplate;
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

@MsgCodeAnn(msgcode = WsMessageActivity.C2SDjrwInfo.id, accessLimit = 200)
public class DjrwInfoProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(DjrwInfoProcessor.class);

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
        logger.info("get djrw info,playerId={}", playerId);
        //
        WsMessageActivity.S2CDjrwInfo respmsg = new WsMessageActivity.S2CDjrwInfo();
        respmsg.list = new ArrayList<>();
        List<ZhdDjrwTemplate> djrwTemplates = ActivityWeekTemplateCache.getInstance().getDjrwTemplates();
        for (ZhdDjrwTemplate zhdDjrwTemplate : djrwTemplates) {
            WsMessageBase.IODjrw1 ioDjrw1 = new WsMessageBase.IODjrw1();
            if (zhdDjrwTemplate.getChk().size() == 1) {
                ioDjrw1.knark = zhdDjrwTemplate.getChk().get(0).getMARK();
            } else {
                ioDjrw1.chk = new ArrayList<>();
                for (ZhdDjrwTemplate.ZhdDjrwCheck zhdDjrwCheck : zhdDjrwTemplate.getChk()) {
                    ioDjrw1.chk.add(new WsMessageBase.IODjrwChk(zhdDjrwCheck.getMARK(), zhdDjrwCheck.getNUM()));
                }
            }
            ioDjrw1.cnum = zhdDjrwTemplate.getCnum();
            ioDjrw1.name = zhdDjrwTemplate.getName();
            ioDjrw1.items = new ArrayList<>();
            for (RewardTemplateSimple rewardTemplateSimple : zhdDjrwTemplate.getItems()) {
                ioDjrw1.items.add(new WsMessageBase.RewardInfo(rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT()));
            }
            ioDjrw1.status = zhdDjrwTemplate.getStatus();
            respmsg.list.add(ioDjrw1);
        }
        //ret
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
