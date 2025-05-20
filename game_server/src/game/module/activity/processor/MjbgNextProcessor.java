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

@MsgCodeAnn(msgcode = WsMessageActivity.C2SMjbgNext.id, accessLimit = 200)
public class MjbgNextProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(MjbgNextProcessor.class);

    @Override
    public void processByte(PlayingRole playingRole, RequestByteMessage request) throws Exception {
    }

    @Override
    public void processProto(PlayingRole playingRole, RequestProtoMessage request) throws Exception {
    }

    @Override
    public void processMy(PlayingRole playingRole, MyRequestMessage request) throws Exception {
        int playerId = playingRole.getId();
        logger.info("mjbg next,player={}", playerId);
        //
        ActMjbg actMjbg = ActMjbgCache.getInstance().getActMjbg(playerId);
        if (actMjbg == null) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageActivity.S2CMjbgNext.msgCode, 30);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        ZhdMjbgTemplate mjbgTemplate = ActivityWeekTemplateCache.getInstance().getMjbgTemplate();
        List<List<ZhdMjbgTemplate.RewardTemplateMjbg>> finallist = mjbgTemplate.getFinallist();
        if (actMjbg.getIndex() >= finallist.size() - 1) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageActivity.S2CMjbgNext.msgCode, 30);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //is big reward
        if (!actMjbg.getBigRewardGet()) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageActivity.S2CMjbgNext.msgCode, 1467);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //do
        actMjbg.setIndex(actMjbg.getIndex() + 1);
        actMjbg.setBigRewardGet(false);
        actMjbg.setBigRewardIndex(null);
        if (actMjbg.getBoxOpen() != null) {
            actMjbg.getBoxOpen().clear();
        }
        if (actMjbg.getRewardOpen() != null) {
            actMjbg.getRewardOpen().clear();
        }
        ActMjbgDaoHelper.asyncUpdateActMjbg(actMjbg);
        //ret
        WsMessageActivity.S2CMjbgNext respmsg = new WsMessageActivity.S2CMjbgNext();
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
