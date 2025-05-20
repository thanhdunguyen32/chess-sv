package game.module.pvp.logic;

import game.entity.PlayingRole;
import game.module.mail.logic.MailManager;
import game.module.pvp.bean.PvpBean;
import game.module.pvp.dao.PvpCache;
import game.module.pvp.dao.PvpRewardTemplateCache;
import game.module.template.PvpDayRewardTemplate;
import game.module.template.RewardTemplateSimple;
import game.module.user.logic.ScrollAnnoManager;
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
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author HeXuhui
 */
public class PvpManager implements Job {

    private static Logger logger = LoggerFactory.getLogger(PvpManager.class);

    private ReentrantReadWriteLock pvpRankLock = new ReentrantReadWriteLock();

    static class SingletonHolder {
        static PvpManager instance = new PvpManager();
    }

    public static PvpManager getInstance() {
        return SingletonHolder.instance;
    }

    public int getPvpScore(int playerId) {
        Map<Integer, Integer> pvpScore = PvpCache.getInstance().getPvpBean().getPvpScore();
        int myScore = PvpConstants.INIT_PVP_SCORE;
        if (pvpScore.containsKey(playerId)) {
            myScore = pvpScore.get(playerId);
        }
        return myScore;
    }

    public void saveScore(Integer playerId, int myPvpScore) {
        logger.info("pvp score change,player={},score={}", playerId, myPvpScore);
        PvpBean pvpBean = PvpCache.getInstance().getPvpBean();
        Map<Integer, Integer> pvpScore = pvpBean.getPvpScore();
        Integer oldPvpScore = pvpScore.get(playerId);
        pvpScore.put(playerId, myPvpScore);
        
        // Push score change to player
        PlayingRole playingRole = SessionManager.getInstance().getPlayer(playerId);
        if (playingRole != null) {
            WsMessageHall.PushPropChange pushMsg = new WsMessageHall.PushPropChange(100011, myPvpScore);
            playingRole.write(pushMsg.build(playingRole.alloc()));
        }

        // Update rank immediately
        pvpRankLock.writeLock().lock();
        try {
            updateRankAndNotify(playerId, myPvpScore, oldPvpScore, pvpBean);
        } finally {
            pvpRankLock.writeLock().unlock();
        }
    }

    private void updateRankAndNotify(Integer playerId, int myPvpScore, Integer oldPvpScore, PvpBean pvpBean) {
        Map<Integer, Integer> myRankMap = pvpBean.getMyRankMap();
        List<Integer> players = pvpBean.getPlayers();
        Map<Integer, Integer> pvpScore = pvpBean.getPvpScore();

        if (!myRankMap.containsKey(playerId)) {
            if (players.size() < PvpConstants.PVP_RANK_SIZE || 
                myPvpScore > pvpScore.get(players.get(players.size() - 1))) {
                stepPlayerRank(players, playerId, myPvpScore);
            }
        } else {
            int myRankIndex = myRankMap.get(playerId);
            Integer myPvpPlayer = players.get(myRankIndex);
            
            if (myPvpScore > oldPvpScore) {
                // Move up in rank
                for (int i = myRankIndex - 1; i >= 0; i--) {
                    Integer toCmpPlayer = players.get(i);
                    if (myPvpScore > pvpScore.get(toCmpPlayer)) {
                        // Swap positions
                        players.set(i, myPvpPlayer);
                        players.set(i + 1, toCmpPlayer);
                        myRankMap.put(playerId, i);
                        myRankMap.put(toCmpPlayer, i + 1);
                        
                        // Notify affected players
                        notifyRankChange(playerId, i + 1);
                        notifyRankChange(toCmpPlayer, i + 2);
                    } else {
                        break;
                    }
                }
            } else {
                // Move down in rank
                for (int i = myRankIndex + 1; i < players.size(); i++) {
                    Integer toCmpPlayer = players.get(i);
                    if (myPvpScore < pvpScore.get(toCmpPlayer)) {
                        players.set(i, myPvpPlayer);
                        players.set(i - 1, toCmpPlayer);
                        myRankMap.put(playerId, i);
                        myRankMap.put(toCmpPlayer, i - 1);
                        
                        // Notify affected players
                        notifyRankChange(playerId, i + 1);
                        notifyRankChange(toCmpPlayer, i);
                    } else {
                        break;
                    }
                }
            }
        }
    }

    private void notifyRankChange(int playerId, int newRank) {
        PlayingRole player = SessionManager.getInstance().getPlayer(playerId);
        if (player != null) {
            WsMessageHall.PushPvpRankChange pushMsg = new WsMessageHall.PushPvpRankChange();
            pushMsg.rank = newRank;
            player.write(pushMsg.build(player.alloc()));
            
            // Thông báo thay đổi rank trên thanh thông báo
            if (newRank <= 3) {
                ScrollAnnoManager.getInstance().pvp(player, newRank);
            }
        }
    }

    public void initMyPvpScore(int playerId) {
        Map<Integer, Integer> pvpScore = PvpCache.getInstance().getPvpBean().getPvpScore();
        if (!pvpScore.containsKey(playerId)) {
            pvpScore.put(playerId, PvpConstants.INIT_PVP_SCORE);
            Integer myPvpScore = pvpScore.get(playerId);
            //insert into rank
            PvpBean pvpBean = PvpCache.getInstance().getPvpBean();
            pvpRankLock.writeLock().lock();
            List<Integer> players = pvpBean.getPlayers();
            if (players.size() < PvpConstants.PVP_RANK_SIZE || myPvpScore > pvpScore.get(players.get(players.size() - 1))) {
                stepPlayerRank(players, playerId, myPvpScore);
            }
            pvpRankLock.writeLock().unlock();
        }
    }

    private void stepPlayerRank(List<Integer> players, int playerId, Integer myPvpScore) {
        PvpBean pvpBean = PvpCache.getInstance().getPvpBean();
        Map<Integer, Integer> pvpScore = pvpBean.getPvpScore();
        Map<Integer, Integer> myRankMap = pvpBean.getMyRankMap();
        if (players.size() < PvpConstants.PVP_RANK_SIZE) {
            //add to tail
            players.add(playerId);
        } else {
            Integer oldPlayerId = players.get(players.size() - 1);
            //replace tail
            players.set(players.size() - 1, playerId);
            myRankMap.remove(oldPlayerId);
        }
        myRankMap.put(playerId, players.size() - 1);
        //sorted list step foward
        for (int i = players.size() - 2; i >= 0; i--) {
            Integer toCmpPlayer = players.get(i);
            if (myPvpScore > pvpScore.get(toCmpPlayer)) {
                int oldPlayerId = toCmpPlayer;
                players.set(i, playerId);
                players.set(i + 1, toCmpPlayer);
                //update rank map
                myRankMap.put(playerId, i);
                myRankMap.put(oldPlayerId, i + 1);
            } else {
                break;
            }
        }
    }

    public ReentrantReadWriteLock getPvpRankLock() {
        return pvpRankLock;
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info("pvp day award job execute!");
        long startTime = System.currentTimeMillis();
        PvpBean pvpBean = PvpCache.getInstance().getPvpBean();
        List<PvpDayRewardTemplate> templateMap = PvpRewardTemplateCache.getInstance().getTemplateMap();
        int rewardIndex = 0;
        PvpDayRewardTemplate pvpDayRewardTemplate = templateMap.get(rewardIndex);
        Map<Integer, Integer> mailAtt = new HashMap<>();
        for (RewardTemplateSimple rewardTemplateSimple : pvpDayRewardTemplate.getREWARDS()) {
            mailAtt.put(rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT());
        }
        String mailTitle = "Phần thưởng xếp hạng hàng ngày tại Đấu Trường Diễn Võ";
        String mailContent = "Chúc mừng bạn đã đạt được vị trí thứ %1$d trong đấu trường diễn võ, bạn đã nhận được các phần thưởng sau: Hãy tiếp tục cố gắng!";
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
        logger.info("pvp day award job success!cost={}ms", System.currentTimeMillis() - startTime);
    }
}
