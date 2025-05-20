package game.module.pay.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.activity.bean.ActivityXiangou;
import game.module.activity.dao.ActivityXiangouCache;
import game.module.activity.dao.XiangouTemplateCache;
import game.module.activity.logic.ActivityWeekManager;
import game.module.pay.dao.LibaoBuyCache;
import game.module.pay.dao.MzlbMylbTemplateCache;
import game.module.pay.logic.LibaoBuyManager;
import game.module.template.MyXiangouTemplate;
import game.module.template.MzlbPayTemplate;
import game.module.template.RewardTemplateSimple;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageActivity;
import ws.WsMessageBase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@MsgCodeAnn(msgcode = WsMessageActivity.C2SMzlbList.id, accessLimit = 200)
public class MzlbListProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(MzlbListProcessor.class);

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
        logger.info("mzlb list,playerId={}", playerId);
        //
        WsMessageActivity.S2CMzlbList respmsg = new WsMessageActivity.S2CMzlbList();
        respmsg.end = ActivityWeekManager.getInstance().getWeekActivityEndTime().getTime();
        respmsg.items = new ArrayList<>();
        List<MzlbPayTemplate> mzlbTemplates = MzlbMylbTemplateCache.getInstance().getMzlbTemplates();
        int idx = 0;
        for (MzlbPayTemplate mzlbPayTemplate : mzlbTemplates){
            WsMessageBase.IOLiBao1 ioLiBao1 = new WsMessageBase.IOLiBao1();
            int libaoBuy = LibaoBuyManager.getInstance().getLibaoBuy(playerId, "mzlb" + idx);
            ioLiBao1.buytime = mzlbPayTemplate.getBuytime() - libaoBuy;
            ioLiBao1.price = mzlbPayTemplate.getPrice();
            ioLiBao1.items = new ArrayList<>();
            for (RewardTemplateSimple rewardTemplateSimple : mzlbPayTemplate.getItems()){
                ioLiBao1.items.add(new WsMessageBase.RewardInfo(rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT()));
            }
            respmsg.items.add(ioLiBao1);
            idx++;
        }
        //ret
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
