package game.module.cdkey.processor;

import game.GameServer;
import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.award.bean.GameConfig;
import game.module.award.logic.AwardUtils;
import game.module.award.logic.FixAwardManager;
import game.module.cdkey.bean.Cdk;
import game.module.cdkey.bean.Cdkey;
import game.module.cdkey.dao.CdkDao;
import game.module.cdkey.dao.CdkeyCache;
import game.module.cdkey.dao.CdkeyDao;
import game.module.log.constants.LogConstants;
import game.module.pay.logic.PaymentConstants;
import game.module.pay.logic.PaymentManager;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageAward.C2SKeyConvert;
import ws.WsMessageAward.S2CKeyConvert;
import ws.WsMessageBase.AwardItem;
import ws.WsMessageHall.S2CErrorCode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 激活码兑换
 *
 * @author zhangning
 * @Date 2015年2月13日 上午7:35:25
 */
@MsgCodeAnn(msgcode = C2SKeyConvert.id, accessLimit = 200)
public class CdkeyConvertProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(CdkeyConvertProcessor.class);

    @Override
    public void processByte(PlayingRole playingRole, RequestByteMessage request) throws Exception {

    }

    /**
     * 兑换激活码
     */
    @Override
    public void processProto(final PlayingRole playingRole, RequestProtoMessage request) throws Exception {

    }

    @Override
    public void processMy(final PlayingRole playingRole, MyRequestMessage request) throws Exception {
        C2SKeyConvert keyConvert = C2SKeyConvert.parse(request);
        final String key = keyConvert.key.trim();
        logger.info("cdkey convert：key={}", key);

        // TODO 平台ID, 区-- 暂定
        int platform = 1;
        int area = 0;

        // 参数错误
        if (StringUtils.isEmpty(key)) {
            S2CErrorCode retMsg = new S2CErrorCode(S2CKeyConvert.msgCode, 130);
            playingRole.writeAndFlush(retMsg.build(playingRole.alloc()));
            return;
        }

        // 激活码已过期
        final CdkeyCache cache = CdkeyCache.getInstance();
        // if (cache.isPastDue(area, key)) {
        // playingRole.getGamePlayer().writeAndFlush(CdkeyProtoConstants.S2C_KEY_CONVERT,
        // RetCode.KEY_PAST_DUE);
        // return;
        // }


        // 数据库处理
        GameServer.executorService.execute(new Runnable() {
            public void run() {
                // 激活码不存在
                Cdk cdkTemplate = CdkDao.getInstance().getCdk(key);
                if (cdkTemplate == null) {
                    S2CErrorCode retMsg = new S2CErrorCode(S2CKeyConvert.msgCode, 1705);
                    playingRole.writeAndFlush(retMsg.build(playingRole.alloc()));
                    return;
                }
                // 该激活码已被使用过，请勿重复使用
                if (cache.isUsedCdkey(area, key) && cdkTemplate.getIsReuse() == 0) {
                    S2CErrorCode retMsg = new S2CErrorCode(S2CKeyConvert.msgCode, 1704);
                    playingRole.writeAndFlush(retMsg.build(playingRole.alloc()));
                    return;
                }
                // 该玩家是否领取过这个类型的礼包
                int area = cdkTemplate.getArea();
                boolean isPlayerGetArea = CdkeyCache.getInstance().isPlayerGetArea(playingRole.getId(), area);
                if (isPlayerGetArea) {
                    S2CErrorCode retMsg = new S2CErrorCode(S2CKeyConvert.msgCode, 1706);
                    playingRole.writeAndFlush(retMsg.build(playingRole.alloc()));
                    return;
                }
                // 发送奖励
                List<AwardItem> rewardItems = new ArrayList<>();
                if (cdkTemplate.getMoneyYuan() != null && cdkTemplate.getMoneyYuan() > 0) {
                    if (playingRole.getPlayerBean().getLevel() < 10) {
                        S2CErrorCode retMsg = new S2CErrorCode(S2CKeyConvert.msgCode, 1317);
                        playingRole.writeAndFlush(retMsg.build(playingRole.alloc()));
                        return;
                    }
                    PaymentManager.getInstance().fakePay(playingRole, cdkTemplate.getMoneyYuan(), "money");
                    rewardItems.add(new AwardItem(GameConfig.PLAYER.YB, cdkTemplate.getMoneyYuan() * PaymentConstants.RMB_2_YUANBAO));
                } else {
                    // 直接发放背包
                    Map<Integer, Integer> awards = FixAwardManager.getInstance().getAward(cdkTemplate.getAwardId());
                    for (Map.Entry<Integer, Integer> itemPair : awards.entrySet()) {
                        Integer gsid = itemPair.getKey();
                        Integer itemcount = itemPair.getValue();
                        AwardUtils.changeRes(playingRole, gsid, itemcount, LogConstants.MODULE_CDK_AWARD);
                        rewardItems.add(new AwardItem(gsid, itemcount));
                    }
                }
                // 添加一条使用记录
                Cdkey cdkey = new Cdkey();
                cdkey.setPlatform(cdkTemplate.getPlatform());
                cdkey.setArea(cdkTemplate.getArea());
                cdkey.setCdkey(key);
                cdkey.setPlayerId(playingRole.getId());
                cdkey.setCdkId(0);
                cache.addOneCdkey(cdkTemplate.getArea(), cdkey);
                CdkeyDao.getInstance().addOneCdkey(cdkey);
                // ret
                S2CKeyConvert retMsg = new S2CKeyConvert();
                retMsg.result_id = 1;
                retMsg.reward_item = rewardItems;
                playingRole.getGamePlayer().writeAndFlush(retMsg.build(playingRole.alloc()));
            }
        });

    }

}
