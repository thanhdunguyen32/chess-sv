package game.module.user.processor;

import com.google.common.primitives.Ints;
import game.common.CommonUtils;
import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.template.GoldTemplate;
import game.module.user.bean.GoldBuy;
import game.module.user.dao.GoldBuyCache;
import game.module.user.dao.GoldTemplateCache;
import game.module.user.logic.GoldBuyManager;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageHall;

import java.util.*;

@MsgCodeAnn(msgcode = WsMessageHall.C2SGoldBuyList.id, accessLimit = 200)
public class GoldBuyListProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(GoldBuyListProcessor.class);

    @Override
    public void processByte(PlayingRole playingRole, RequestByteMessage requestMessage) throws Exception {

    }

    @Override
    public void processProto(PlayingRole playingRole, RequestProtoMessage request) throws Exception {

    }

    @Override
    public void processMy(PlayingRole playingRole, MyRequestMessage request) throws Exception {
        int playerId = playingRole.getId();
        //
        GoldBuyManager.getInstance().updateGoldBuyProperty(playingRole, true);
        //ret
        WsMessageHall.S2CGoldBuyList respmsg = new WsMessageHall.S2CGoldBuyList();
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

    public static void main(String[] args) {
        List<Integer> alist = new ArrayList<>(3);
        Collections.fill(alist,0);
        System.out.println(alist);
    }
}
