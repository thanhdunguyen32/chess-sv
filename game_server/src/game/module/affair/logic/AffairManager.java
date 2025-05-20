package game.module.affair.logic;

import game.module.affair.bean.AffairBean;
import game.module.affair.bean.PlayerAffairs;
import game.module.affair.dao.AffairRewardTemplateCache;
import game.module.affair.dao.AffairTemplateCache;
import game.module.award.bean.GameConfig;
import game.module.pay.logic.ChargeInfoManager;
import game.module.template.AffairRewardTemplate;
import game.module.template.AffairsTemplate;
import game.module.template.RewardTemplateSimple;
import game.module.template.VipTemplate;
import game.module.vip.dao.VipTemplateCache;
import lion.math.RandomDispatcher;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author HeXuhui
 */
public class AffairManager {

    private static Logger logger = LoggerFactory.getLogger(AffairManager.class);

    static class SingletonHolder {

        static AffairManager instance = new AffairManager();


    }

    public static AffairManager getInstance() {
        return SingletonHolder.instance;
    }

    public PlayerAffairs createPlayerAffairs(int playerId) {
        PlayerAffairs playerAffairs = new PlayerAffairs();
        playerAffairs.setPlayerId(playerId);
        playerAffairs.setLastVisitTime(new Date());
        return playerAffairs;
    }

    public AffairBean createAffairBean(int templateId) {
        AffairBean affairBean = new AffairBean();
        affairBean.setTemplateId(templateId);
        affairBean.setLock(0);
        affairBean.setStatus(AffairConstants.AFFAIR_STATUS_NEW);
        //rewards
        AffairRewardTemplate affairRewardTemplate = AffairRewardTemplateCache.getInstance().getAffairRewardTemplate(templateId);
        if (affairRewardTemplate == null) {
            int randMoney = RandomUtils.nextInt(20, 81);
            affairBean.setReward(new RewardTemplateSimple(GameConfig.PLAYER.YB, randMoney));
        } else {
            List<List<Integer>> rewardList = affairRewardTemplate.getReward();
            int randIndex = RandomUtils.nextInt(0, rewardList.size());
            List<Integer> randReward = rewardList.get(randIndex);
            affairBean.setReward(new RewardTemplateSimple(randReward.get(0), randReward.get(1)));
        }
        //rand conds
        List<Integer> condsList = new ArrayList<>();
        AffairsTemplate affairsTemplate = AffairTemplateCache.getInstance().getAffairsTemplate(templateId);
        int condNum = AffairConstants.COND_NUM[affairsTemplate.getSTAR() - 1];
        RandomDispatcher<Integer> rd = new RandomDispatcher<>();
        for (int i = 0; i < AffairConstants.AFFAIR_CONDS.length; i++) {
            rd.put(1, AffairConstants.AFFAIR_CONDS[i]);
        }
        for (int i = 0; i < condNum; i++) {
            condsList.add(rd.randomRemove());
        }
        affairBean.setCond(condsList);
        return affairBean;
    }

    public AffairsTemplate randAffairByStar(int targetStar) {
        List<AffairsTemplate> affairsByQuality = AffairTemplateCache.getInstance().getAffairsByQuality(targetStar);
        if (affairsByQuality == null) {
            return null;
        }
        int randIndex = RandomUtils.nextInt(0, affairsByQuality.size());
        return affairsByQuality.get(randIndex);
    }

    /**
     * 随机生成5个内政任务
     *
     * @return
     */
    public List<AffairBean> generateAffairs(int generateNum,int specialNum) {
        List<AffairBean> retlist = new ArrayList<>();
        RandomDispatcher<Integer> rd = new RandomDispatcher<>();
        for (int[] aStar : AffairConstants.AFFAIR_REFRESH_RATE) {
            rd.put(aStar[1], aStar[0]);
        }
        for (int i = 0; i < generateNum-specialNum; i++) {
            Integer randStar = rd.random();
            AffairsTemplate affairsTemplate = randAffairByStar(randStar);
            AffairBean affairBean = createAffairBean(affairsTemplate.getID());
            retlist.add(affairBean);
        }
        if (specialNum > 0) {
            rd = new RandomDispatcher<>();
            rd.put(AffairConstants.AFFAIR_REFRESH_RATE[4][1], AffairConstants.AFFAIR_REFRESH_RATE[4][0]);
            rd.put(AffairConstants.AFFAIR_REFRESH_RATE[5][1], AffairConstants.AFFAIR_REFRESH_RATE[5][0]);
            rd.put(AffairConstants.AFFAIR_REFRESH_RATE[6][1], AffairConstants.AFFAIR_REFRESH_RATE[6][0]);
            for (int i = 0; i < specialNum; i++) {
                Integer randStar = rd.random();
                AffairsTemplate affairsTemplate = randAffairByStar(randStar);
                AffairBean affairBean = createAffairBean(affairsTemplate.getID());
                retlist.add(affairBean);
            }
        }
        return retlist;
    }

    public int getAffairNum(int playerId, int vipLevel) {
        VipTemplate vipTemplate = VipTemplateCache.getInstance().getVipTemplate(vipLevel);
        int qzAddon = ChargeInfoManager.getInstance().getQzAddon(playerId);
        return AffairConstants.INIT_AFFAIR_COUNT + vipTemplate.getRIGHT().getMISSION() + qzAddon;
    }

    public static final class CondTypeBean {
        private String condType;
        private Integer condValue;

        public CondTypeBean(String condType, Integer condValue) {
            this.condType = condType;
            this.condValue = condValue;
        }

        public String getCondType() {
            return condType;
        }

        public void setCondType(String condType) {
            this.condType = condType;
        }

        public Integer getCondValue() {
            return condValue;
        }

        public void setCondValue(Integer condValue) {
            this.condValue = condValue;
        }
    }
}
