package game.module.sign.processor;

import game.common.CommonUtils;
import game.common.DateCommonUtils;
import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.award.logic.AwardUtils;
import game.module.log.constants.LogConstants;
import game.module.sign.bean.SignInRewardsTemplate;
import game.module.sign.dao.SignTemplateCache;
import game.module.sign.logic.SignManager;
import game.module.user.bean.PlayerProp;
import game.module.user.dao.PlayerOtherCache;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageAward;
import ws.WsMessageAward.S2CGetSignAward;
import ws.WsMessageBase;
import ws.WsMessageHall.S2CErrorCode;

import java.util.Collections;
import java.util.Date;
import java.util.Map;

/**
 * 领取签到奖励
 *
 * @author zhangning
 * @Date 2015年1月12日 下午8:18:05
 */
@MsgCodeAnn(msgcode = WsMessageAward.C2SGetSignAward.id, accessLimit = 200)
public class GetSignAwardProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(GetSignAwardProcessor.class);

    @Override
    public void processByte(PlayingRole playingRole, RequestByteMessage request) throws Exception {

    }

    /**
     * 领取签到奖励
     */
    @Override
    public void processProto(PlayingRole playingRole, RequestProtoMessage request) throws Exception {

    }

    /**
     * 添加奖励
     *
     * @param playingRole
     */
    public void addItem(final PlayingRole playingRole, SignInRewardsTemplate signInRewardsTemplate) {
        if (signInRewardsTemplate == null) {
            return;
        }
        int cycle = 1;
//		if (vipDouble > 0 && playingRole.getPlayerBean().getVipLevel() >= vipDouble) {
//			cycle = 2;
//		}
//		int addCount = rewardArra[2] * cycle;
        AwardUtils.changeRes(playingRole, signInRewardsTemplate.getGSID(), signInRewardsTemplate.getCOUNT(), LogConstants.MODULE_SIGN);
    }

    @Override
    public void processMy(PlayingRole playingRole, MyRequestMessage request) throws Exception {
        int playerId = playingRole.getId();
        logger.info("sign get award： player={}", playerId);
        // params check
        Map<Integer, PlayerProp> playerOthers = PlayerOtherCache.getInstance().getPlayerOther(playerId);
        int getCount = 0;
        if (playerOthers.containsKey(SignManager.SIGN_GET_MARK)) {
            getCount = playerOthers.get(SignManager.SIGN_GET_MARK).getCount();
        }
        int signinCount = 0;
        if (playerOthers.containsKey(SignManager.SIGN_COUNT_MARK)) {
            signinCount = playerOthers.get(SignManager.SIGN_COUNT_MARK).getCount();
        }
        // 奖励已领
        if (getCount >= signinCount) {
            S2CErrorCode retMsg = new S2CErrorCode(WsMessageAward.S2CGetSignAward.msgCode, 1701);
            playingRole.writeAndFlush(retMsg.build(playingRole.alloc()));
            return;
        }
        // 配置表不存在
        SignInRewardsTemplate signInRewardsTemplate = SignTemplateCache.getInstance().getSignTemp(getCount);
        if (signInRewardsTemplate == null) {
            logger.error("Player ID: {} When receiving sign-in rewards, the reward configuration table was not found! Receive rewards on day {}", playingRole.getId(), signinCount);
            return;
        }
        // 道具奖励
        addItem(playingRole, signInRewardsTemplate);
        // 保存记录
        AwardUtils.changeRes(playingRole, SignManager.SIGN_GET_MARK, 1, LogConstants.MODULE_SIGN);
        // send msg
        S2CGetSignAward retMsg = new S2CGetSignAward();
        retMsg.awards = Collections.singletonList(new WsMessageBase.AwardItem(signInRewardsTemplate.getGSID(), signInRewardsTemplate.getCOUNT()));
        playingRole.getGamePlayer().writeAndFlush(retMsg.build(playingRole.alloc()));
    }

}
