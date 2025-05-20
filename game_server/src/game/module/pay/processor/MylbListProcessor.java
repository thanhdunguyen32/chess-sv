package game.module.pay.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.activity_month.logic.ActivityMonthManager;
import game.module.pay.dao.MzlbMylbTemplateCache;
import game.module.pay.logic.LibaoBuyManager;
import game.module.template.MzlbPayTemplate;
import game.module.template.RewardTemplateSimple;
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

@MsgCodeAnn(msgcode = WsMessageActivity.C2SMylbList.id, accessLimit = 200)
public class MylbListProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(MylbListProcessor.class);

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
        logger.info("mylb list,playerId={}", playerId);
        //
        WsMessageActivity.S2CMylbList respmsg = new WsMessageActivity.S2CMylbList();
        respmsg.end = ActivityMonthManager.getInstance().getYdhdEndTime().getTime();
        respmsg.items = new ArrayList<>();
        List<MzlbPayTemplate> mylbTemplates = MzlbMylbTemplateCache.getInstance().getMylbTemplates();
        int idx = 0;
        for (MzlbPayTemplate mylbPayTemplate : mylbTemplates) {
            WsMessageBase.IOLiBao1 ioLiBao1 = new WsMessageBase.IOLiBao1();
            int libaoBuy = LibaoBuyManager.getInstance().getLibaoBuy(playerId, "mylb" + idx);
            ioLiBao1.buytime = mylbPayTemplate.getBuytime() - libaoBuy;
            ioLiBao1.price = mylbPayTemplate.getPrice();
            ioLiBao1.items = new ArrayList<>();
            for (RewardTemplateSimple rewardTemplateSimple : mylbPayTemplate.getItems()) {
                ioLiBao1.items.add(new WsMessageBase.RewardInfo(rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT()));
            }
            respmsg.items.add(ioLiBao1);
            idx++;
        }
        //ret
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
