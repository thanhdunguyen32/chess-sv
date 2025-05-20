package game.module.mail.logic;

import game.module.mail.dao.MailTemplateCache;
import game.module.season.dao.SeasonCache;
import game.module.template.MailZhdTemplate;
import game.module.template.RewardTemplateSimple;
import game.module.user.bean.PlayerBean;
import game.module.user.dao.PlayerDao;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MailDailyZhdManager implements Job {

    private static final Logger logger = LoggerFactory.getLogger(MailDailyZhdManager.class);

    static class SingletonHolder {
        static MailDailyZhdManager instance = new MailDailyZhdManager();
    }

    public static MailDailyZhdManager getInstance() {
        return MailDailyZhdManager.SingletonHolder.instance;
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        long startTime = System.currentTimeMillis();
        logger.info("mail daily zhd start-------------------------");
        List<PlayerBean> allPlayers = PlayerDao.getInstance().getAllPlayers();
        //set zhd type
        int mailIndex = SeasonCache.getInstance().getBattleSeason().getSeason() - 1;
        MailZhdTemplate mailZhdTemplate = MailTemplateCache.getInstance().getMailZhdTemplate(mailIndex);
        Map<Integer, Integer> mailAtt = new HashMap<>();
        for (RewardTemplateSimple rewardTemplateSimple : mailZhdTemplate.getReward()) {
            mailAtt.put(rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT());
        }
        for (PlayerBean playerBean : allPlayers) {
            int playerId = playerBean.getId();
            MailManager.getInstance().sendSysMailToSingle(playerId, mailZhdTemplate.getTitle(), mailZhdTemplate.getContent(), mailAtt);
        }
        logger.info("mail daily zhd finish,cost:{} s-------------------------", (System.currentTimeMillis() - startTime) / 1000f);
    }

}
