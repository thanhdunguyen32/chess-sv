package game.module.user.logic;

import game.entity.PlayingRole;
import game.module.award.logic.AwardUtils;
import game.module.item.logic.ItemConstants;
import game.module.log.constants.LogConstants;
import game.module.mail.dao.MailTemplateCache;
import game.module.mail.logic.MailManager;
import game.module.pay.logic.PaymentConstants;
import game.module.pay.logic.PaymentManager;
import game.module.template.MailZhdTemplate;
import game.module.template.RewardTemplateSimple;
import game.module.user.bean.PlayerBean;
import game.module.user.logic.TopupFeedbackManager.TopupFeedbackBean;
import game.module.vip.logic.VipManager;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerLogic {

    private static Logger logger = LoggerFactory.getLogger(PlayerLogic.class);

    static class SingletonHolder {
        static PlayerLogic instance = new PlayerLogic();
    }

    public static PlayerLogic getInstance() {
        return SingletonHolder.instance;
    }

    public void yueKaChengZhangFeedback(PlayingRole playingRole) {
        String openId = playingRole.getPlayerBean().getAccountId();
        if (StringUtils.isEmpty(openId)) {
            return;
        }
        logger.info("check yueka chengzhang feedback,open_id={}", openId);
        if (!TopupFeedbackManager.getInstance().isHasFeedback(openId)) {
            return;
        }
        //do
        logger.info("do payment feedback,open_id={}", openId);
        int curTime = (int) (System.currentTimeMillis() / 1000);
        List<TopupFeedbackBean> feedbackBeans = TopupFeedbackManager.getInstance().getTopupFeedback(openId);
        for (TopupFeedbackBean topupFeedbackBean : feedbackBeans) {
            //返利
            if (topupFeedbackBean.product_id.equals("free")) {
                continue;
            }
            int addYuan = topupFeedbackBean.add_point;
            if (topupFeedbackBean.product_id.equals("charge6") || topupFeedbackBean.product_id.equals("charge30") || topupFeedbackBean.product_id.equals(
                    "charge68")
                    || topupFeedbackBean.product_id.equals("charge128") || topupFeedbackBean.product_id.equals("charge198") || topupFeedbackBean.product_id.equals("charge328")
                    || topupFeedbackBean.product_id.equals("charge648") || topupFeedbackBean.product_id.equals("charge1") || topupFeedbackBean.product_id.equals("charge12")
                    || topupFeedbackBean.product_id.equals("charge98") || topupFeedbackBean.product_id.equals("charge448") || topupFeedbackBean.product_id.equals("charge60")) {
                addYuan *= 2;
            }
            PaymentManager.getInstance().payCallbackNoMessage(playingRole.getGamePlayer().getChannel(), openId,
                    topupFeedbackBean.transaction_code, addYuan, curTime, topupFeedbackBean.product_id,
                    playingRole.getPlayerBean().getServerId());
            if (!(topupFeedbackBean.product_id.equals("charge6") || topupFeedbackBean.product_id.equals("charge30") || topupFeedbackBean.product_id.equals(
                    "charge68") || topupFeedbackBean.product_id.equals("charge128") || topupFeedbackBean.product_id.equals("charge198")
                    || topupFeedbackBean.product_id.equals("charge328") || topupFeedbackBean.product_id.equals("charge648")
                    || topupFeedbackBean.product_id.equals("charge1") || topupFeedbackBean.product_id.equals("charge12")
                    || topupFeedbackBean.product_id.equals("charge98") || topupFeedbackBean.product_id.equals("charge448")
                    || topupFeedbackBean.product_id.equals("charge60"))) {
                // 玩家充值月卡、成长基金、年卡、英雄礼包 返还对应充值的卡或者礼包 双倍的另外一半发放对应的充值金额*300 的金砖 和VIP经验
                int addDiamond = addYuan * PaymentConstants.RMB_2_YUANBAO;
                PlayerInfoManager.getInstance().changeMoney(playingRole, addDiamond, LogConstants.MODULE_PAYMENT);
                // VIP增加
                VipManager.getInstance().rechargeVipLev(playingRole, addDiamond);
            }
        }
        //设置已经进行返利
        TopupFeedbackManager.getInstance().setFeedbackFinish(openId);
    }

    public void firstEnterMail(PlayingRole playingRole) {
        //已经登陆过
        if (playingRole.getPlayerBean().getDownlineTime() != null) {
            return;
        }
        int playerId = playingRole.getId();
        List<MailZhdTemplate> initTemplateList = MailTemplateCache.getInstance().getInitTemplateList();
        for (MailZhdTemplate mailZhdTemplate : initTemplateList) {
            Map<Integer, Integer> mailAtt = new HashMap<>();
            for (RewardTemplateSimple rewardTemplateSimple : mailZhdTemplate.getReward()) {
                mailAtt.put(rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT());
            }
            MailManager.getInstance().sendSysMailToSingle(playerId, mailZhdTemplate.getTitle(), mailZhdTemplate.getContent(), mailAtt);
        }
    }

}
