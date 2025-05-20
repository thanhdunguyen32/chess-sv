package game.module.pvp.logic;

import game.entity.PlayingRole;
import game.module.chapter.dao.BattleFormationDao;
import game.module.mail.logic.MailManager;
import game.module.pvp.bean.PvpBean;
import game.module.pvp.dao.PvpCache;
import game.module.pvp.dao.PvpDaoHelper;
import game.module.pvp.dao.PvpRewardTemplateCache;
import game.module.template.PvpDayRewardTemplate;
import game.module.template.RewardTemplateSimple;
import game.session.SessionManager;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageHall;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PvpWeekRewardManager implements Job {

    private static Logger logger = LoggerFactory.getLogger(PvpWeekRewardManager.class);

    static class SingletonHolder {
        static PvpWeekRewardManager instance = new PvpWeekRewardManager();
    }

    public static PvpWeekRewardManager getInstance() {
        return PvpWeekRewardManager.SingletonHolder.instance;
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info("pvp week award job execute!");
        long startTime = System.currentTimeMillis();
        PvpBean pvpBean = PvpCache.getInstance().getPvpBean();
        List<PvpDayRewardTemplate> templateMap = PvpRewardTemplateCache.getInstance().getWeekRewardTemplates();
        int rewardIndex = 0;
        PvpDayRewardTemplate pvpDayRewardTemplate = templateMap.get(rewardIndex);
        Map<Integer, Integer> mailAtt = new HashMap<>();
        for (RewardTemplateSimple rewardTemplateSimple : pvpDayRewardTemplate.getREWARDS()) {
            mailAtt.put(rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT());
        }
        String mailTitle = "Phần thưởng xếp hạng mùa đấu trường võ thuật cạnh tranh "; //"竞技演武场赛季排名奖励"
        String mailContent = "Chúc mừng bạn đã đạt được thứ hạng cuối cùng là %1$d trong đấu trường tuần này và bạn đã nhận được những phần thưởng sau: Chúc bạn tiếp tục phát huy tốt!"; //"恭喜您在本周竞技场最终排名第%1$d,获得如下奖励：愿你再接再厉！";
        if (pvpBean != null && pvpBean.getPlayers() != null) {
            List<Integer> rankPlayers = pvpBean.getPlayers();
            for (int i = 0; i < rankPlayers.size(); i++) {
                int aRank = i + 1;
                int playerId = rankPlayers.get(i);
                if (aRank > pvpDayRewardTemplate.getMAX()) {
                    rewardIndex++;
                    pvpDayRewardTemplate = templateMap.get(rewardIndex);
                    mailAtt = new HashMap<>();
                    for (RewardTemplateSimple rewardTemplateSimple : pvpDayRewardTemplate.getREWARDS()) {
                        mailAtt.put(rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT());
                    }
                }
                //do
                String myMailContent = String.format(mailContent, aRank);
                MailManager.getInstance().sendSysMailToSingle(playerId, mailTitle, myMailContent, mailAtt);
            }
        }
        //reset pvp
        logger.info("pvp reset!");
        if (pvpBean != null && pvpBean.getPlayers() != null) {
            pvpBean.getPlayers().clear();
            pvpBean.getMyRankMap().clear();
        }
        if (pvpBean != null && pvpBean.getPvpPlayerInfo() != null) {
            pvpBean.getPvpPlayerInfo().clear();
        }
        if (pvpBean != null && pvpBean.getPvpScore() != null) {
            pvpBean.getPvpScore().clear();
        }
        //
        initPvpPlayers(pvpBean);
        //save 2 db
        PvpDaoHelper.asyncUpdatePvpBean(pvpBean);
        //push
        for (PlayingRole playingRole : SessionManager.getInstance().getAllPlayers()) {
            WsMessageHall.PushPropChange pushmsg = new WsMessageHall.PushPropChange(100011, 1000);
            playingRole.writeAndFlush(pushmsg.build(playingRole.alloc()));
        }
        logger.info("pvp week award job success!cost={}ms", System.currentTimeMillis() - startTime);
    }

    public void initPvpPlayers(PvpBean pvpBean){
        List<Integer> pvpPlayers = BattleFormationDao.getInstance().getPvpPlayers();
        if (pvpPlayers.size() > 0) {
            //score
            for (int aPlayerId : pvpPlayers) {
                pvpBean.getPvpScore().put(aPlayerId, PvpConstants.INIT_PVP_SCORE);
            }
//            player list
            pvpBean.getPlayers().addAll(pvpPlayers);
//            rank
            Map<Integer, Integer> myRankMap = new HashMap<>();
            for (int i = 0; i < pvpPlayers.size(); i++) {
                myRankMap.put(pvpPlayers.get(i), i);
            }
            pvpBean.setMyRankMap(myRankMap);
        }
    }

}
