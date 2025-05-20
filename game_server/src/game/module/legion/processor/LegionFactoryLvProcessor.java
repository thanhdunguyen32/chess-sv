package game.module.legion.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.award.logic.AwardUtils;
import game.module.item.logic.ItemManager;
import game.module.legion.bean.LegionBean;
import game.module.legion.bean.LegionPlayer;
import game.module.legion.dao.LegionCache;
import game.module.legion.dao.LegionDaoHelper;
import game.module.legion.dao.LegionFDonationTemplateCache;
import game.module.legion.dao.LegionFLevelTemplateCache;
import game.module.legion.logic.LegionConstants;
import game.module.legion.logic.LegionManager;
import game.module.log.constants.LogConstants;
import game.module.pay.logic.ChargeInfoManager;
import game.module.season.logic.SeasonManager;
import game.module.template.LegionFDonationTemplate;
import game.module.template.LegionFLevelTemplate;
import game.module.template.RewardTemplateSimple;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;
import ws.WsMessageHall;
import ws.WsMessageLegion;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author hexuhui
 */
@MsgCodeAnn(msgcode = WsMessageLegion.C2SLegionFactoryLv.id, accessLimit = 200)
public class LegionFactoryLvProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(LegionFactoryLvProcessor.class);

    @Override
    public void processByte(PlayingRole playingRole, RequestByteMessage request) throws Exception {

    }

    /**
     * 加载所有邮件
     */
    @Override
    public void processProto(PlayingRole playingRole, RequestProtoMessage request) throws Exception {
    }

    @Override
    public void processMy(PlayingRole playingRole, MyRequestMessage request) throws Exception {
        int playerId = playingRole.getId();
        WsMessageLegion.C2SLegionFactoryLv reqmsg = WsMessageLegion.C2SLegionFactoryLv.parse(request);
        logger.info("legion factory lv!player={},req={}", playerId, reqmsg);
        //not in legion
        long legionId = LegionManager.getInstance().getLegionId(playerId);
        if (legionId == 0) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageLegion.S2CLegionFactoryLv.msgCode, 1516);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //
        LegionBean legionBean = LegionCache.getInstance().getLegionBean(legionId);
        if (legionBean.getLevel() < 8) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageLegion.S2CLegionFactoryLv.msgCode, 1532);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //count max
        int donateCount = ItemManager.getInstance().getCount(playingRole.getPlayerBean(), LegionConstants.LEGION_FACTORY_DONATE_MARK);
        if ((reqmsg.type == 1 || reqmsg.type == 2) && donateCount >= 3) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageLegion.S2CLegionFactoryLv.msgCode, 1533);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //is new man
        LegionPlayer legionPlayer = legionBean.getDbLegionPlayers().getMembers().get(playerId);
        Date now = new Date();
        boolean isNewman = DateUtils.isSameDay(legionPlayer.getAddTime(), now);
        if ((reqmsg.type == 1 || reqmsg.type == 2) && isNewman) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageLegion.S2CLegionFactoryLv.msgCode, 1535);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        int flevel = legionBean.getFlevel();
        int fexp = legionBean.getFexp();
        List<WsMessageBase.IORewardItem> rewardItems = new ArrayList<>();
        if (reqmsg.type == 1 || reqmsg.type == 2) {
            //material cost
            List<LegionFDonationTemplate> legionFDonationTemplates = LegionFDonationTemplateCache.getInstance().getLegionFDonationTemplate(reqmsg.type);
            LegionFDonationTemplate legionFDonationTemplate = legionFDonationTemplates.get(donateCount);
            List<RewardTemplateSimple> castitems = legionFDonationTemplate.getCASTITEMS();
            for (RewardTemplateSimple rewardTemplateSimple : castitems) {
                if (!ItemManager.getInstance().checkItemEnough(playingRole.getPlayerBean(), rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT())) {
                    WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageLegion.S2CLegionFactoryLv.msgCode, 1534);
                    playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
                    return;
                }
            }
            //do
            synchronized (legionBean) {
                fexp += legionFDonationTemplate.getADDEXP();
                LegionFLevelTemplate nextLegionFlevelTemplate = LegionFLevelTemplateCache.getInstance().getLegionFLevelTemplate(flevel + 1);
                if (fexp >= nextLegionFlevelTemplate.getEXP()) {
                    flevel++;
                    fexp -= nextLegionFlevelTemplate.getEXP();
                }
                legionBean.setFlevel(flevel);
                legionBean.setFexp(fexp);
                //
                legionPlayer.setDonateSum(legionPlayer.getDonateSum() + legionFDonationTemplate.getADDEXP());
                legionPlayer.setLastDonateTime(now);
                LegionDaoHelper.asyncUpdateLegionBean(legionBean);
            }
            //cost
            for (RewardTemplateSimple rewardTemplateSimple : castitems) {
                AwardUtils.changeRes(playingRole, rewardTemplateSimple.getGSID(), -rewardTemplateSimple.getCOUNT(), LogConstants.MODULE_LEGION);
            }
            //reward
            int addLegionCoin = reqmsg.type == 1 ? 20 : 40;
            int seasonAddon = SeasonManager.getInstance().getLegionAddon();
            int jtAddon = ChargeInfoManager.getInstance().getJtAddon(playerId);
            AwardUtils.changeRes(playingRole, LegionConstants.LEGION_COIN, (int)(addLegionCoin*(1+(seasonAddon+jtAddon)/100f)), LogConstants.MODULE_LEGION);
            rewardItems.add(new WsMessageBase.IORewardItem(LegionConstants.LEGION_COIN, addLegionCoin));
            //mark
            AwardUtils.changeRes(playingRole, LegionConstants.LEGION_FACTORY_DONATE_MARK, 1, LogConstants.MODULE_LEGION);
        }
        //ret
        WsMessageLegion.S2CLegionFactoryLv respmsg = new WsMessageLegion.S2CLegionFactoryLv();
        respmsg.lv = flevel;
        respmsg.exp = fexp;
        respmsg.newman = isNewman ? 1 : 0;
        respmsg.rewards = rewardItems;
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
