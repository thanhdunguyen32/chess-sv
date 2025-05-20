package game.module.activity.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.activity.bean.ActMjbg;
import game.module.activity.dao.ActMjbgCache;
import game.module.activity.dao.ActMjbgDaoHelper;
import game.module.activity.dao.ActivityWeekTemplateCache;
import game.module.template.ZhdMjbgTemplate;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageActivity;
import ws.WsMessageHall;

import java.util.List;

@MsgCodeAnn(msgcode = WsMessageActivity.C2SMjbgChange.id, accessLimit = 200)
public class MjbgChangeProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(MjbgChangeProcessor.class);

    @Override
    public void processByte(PlayingRole playingRole, RequestByteMessage request) throws Exception {
    }

    @Override
    public void processProto(PlayingRole playingRole, RequestProtoMessage request) throws Exception {
    }

    @Override
    public void processMy(PlayingRole playingRole, MyRequestMessage request) throws Exception {
        int playerId = playingRole.getId();
        WsMessageActivity.C2SMjbgChange reqmsg = WsMessageActivity.C2SMjbgChange.parse(request);
        logger.info("mjbg change,player={},req={}", playerId, reqmsg);
        //
        int index = reqmsg.index;
        int itemindex = reqmsg.itemindex;
        ActMjbg actMjbg = ActMjbgCache.getInstance().getActMjbg(playerId);
        if (actMjbg == null) {
            index = 0;
        }
        ZhdMjbgTemplate mjbgTemplate = ActivityWeekTemplateCache.getInstance().getMjbgTemplate();
        List<ZhdMjbgTemplate.RewardTemplateMjbg> rewardTemplateMjbgs = mjbgTemplate.getFinallist().get(index);
        if (itemindex >= rewardTemplateMjbgs.size()) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageActivity.S2CMjbgChange.msgCode, 30);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        if (actMjbg == null) {
            actMjbg = new ActMjbg();
            actMjbg.setPlayerId(playerId);
            actMjbg.setIndex(0);
            actMjbg.setBigRewardGet(false);
            actMjbg.setBigRewardIndex(itemindex);
            ActMjbgCache.getInstance().addActMjbg(actMjbg);
        } else {
            actMjbg.setBigRewardIndex(itemindex);
        }
        if (actMjbg.getId() == null) {
            ActMjbgDaoHelper.asyncInsertActMjbg(actMjbg);
        } else {
            ActMjbgDaoHelper.asyncUpdateActMjbg(actMjbg);
        }
        //ret
        WsMessageActivity.S2CMjbgChange respmsg = new WsMessageActivity.S2CMjbgChange();
        ZhdMjbgTemplate.RewardTemplateMjbg rewardTemplateMjbg = rewardTemplateMjbgs.get(itemindex);
        respmsg.gsid = rewardTemplateMjbg.getGsid();
        respmsg.count = rewardTemplateMjbg.getCount();
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
