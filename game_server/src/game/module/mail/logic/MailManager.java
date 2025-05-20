package game.module.mail.logic;

import game.GameServer;
import game.entity.PlayingRole;
import game.module.mail.bean.MailBean;
import game.module.mail.constants.MailConstants;
import game.module.mail.dao.MailCache;
import game.module.mail.dao.MailDao;
import game.module.mail.dao.MailTemplateCache;
import game.module.offline.bean.PlayerBaseBean;
import game.module.offline.logic.PlayerOfflineManager;
import game.module.user.bean.PlayerBean;
import game.module.user.dao.PlayerDao;
import game.session.SessionManager;
import lion.common.StringConstants;
import lion.netty4.message.GamePlayer;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;
import ws.WsMessageBase.IOMailInfo;
import ws.WsMessageMail.PushNewMail;

import java.util.*;

/**
 * 邮件逻辑处理
 *
 * @author zhangning
 * @Date 2014年12月29日 下午5:10:00
 */
public class MailManager implements Job {

    private static Logger logger = LoggerFactory.getLogger(MailManager.class);

    MailDao mailDao = MailDao.getInstance();

    static class SingletonHolder {
        static MailManager instance = new MailManager();
    }

    public static MailManager getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * 每日5时： Check过期邮件
     */
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        // 玩家邮件是否过期
        checkPastDueMail();
    }

    /**
     * 检查过期邮件
     */
    public void checkPastDueMail() {
        Collection<List<MailBean>> mailCacheAll = MailCache.getInstance().getMailCacheAll();
        Date now = new Date();
        for (List<MailBean> mailList : mailCacheAll){
            // 在线玩家,删除缓存过期数据
            mailList.removeIf(mailBean -> mailBean.getEndTime().before(now));
        }
        mailDao.checkPastDueMail();
    }

    /**
     * 发邮件,给所有玩家(系统邮件)
     *
     */
    public void sendSysMailToAll(String mailTitle, String mailContent, Map<Integer, Integer> mailAtt) {
        // 发送邮件给所有玩家
        SessionManager sessionManager = SessionManager.getInstance();
        List<PlayerBean> allPlayers = PlayerDao.getInstance().getAllPlayers();
        if (allPlayers != null && allPlayers.size() > 0) {
            for (PlayerBean playerBean : allPlayers) {
                PlayingRole playingRole = sessionManager.getPlayer(playerBean.getId());
                boolean isOnline = false;
                GamePlayer gamePlayer = null;
                if (playingRole != null) {
                    isOnline = true;
                    gamePlayer = playingRole.getGamePlayer();
                }
                if (!isOnline && MailCache.getInstance().getMailCache(playerBean.getId()) != null) {
                    isOnline = true;
                }
                createMail(gamePlayer, playerBean.getId(), MailConstants.MAIL_TYPE_SYS, 0, 0, mailTitle, mailContent, mailAtt, isOnline);
            }
        }
    }

    /**
     * 发邮件, 给单个玩家(系统邮件)
     *
     * @param playerId   : 接收人
     * @param mailAtt
     */
    public void sendSysMailToSingle(final int playerId, String mailTitle, String mailContent, Map<Integer, Integer> mailAtt) {
        sendSysMailToSingle(playerId, mailTitle, mailContent, mailAtt, null);
    }

    public void sendSysMailToSingle(final int playerId, String mailTitle, String mailContent, Map<Integer, Integer> mailAtt, Date endTime) {
        PlayingRole playingRole = SessionManager.getInstance().getPlayer(playerId);
        GamePlayer gamePlayer = null;
        boolean isOnline = false;
        if (playingRole != null) {
            isOnline = true;
            gamePlayer = playingRole.getGamePlayer();
        }
        if (!isOnline && MailCache.getInstance().getMailCache(playerId) != null) {
            isOnline = true;
        }

        createMail(gamePlayer, playerId, MailConstants.MAIL_TYPE_SYS, 0, 0, mailTitle, mailContent, mailAtt, isOnline, endTime);
    }

    public void sendRoleMail(int playerId, int fromPlayerId, String mailTitle, String mailContent, Map<Integer, Integer> mailAtt){
        PlayingRole playingRole = SessionManager.getInstance().getPlayer(playerId);
        GamePlayer gamePlayer = null;
        boolean isOnline = false;
        if (playingRole != null) {
            isOnline = true;
            gamePlayer = playingRole.getGamePlayer();
        }
        if (!isOnline && MailCache.getInstance().getMailCache(playerId) != null) {
            isOnline = true;
        }
        createMail(gamePlayer, playerId, MailConstants.MAIL_TYPE_PLAYER, fromPlayerId, 0, mailTitle, mailContent, mailAtt, isOnline);
    }

    public void sendLegionMail(int playerId, int legionId, String mailTitle, String mailContent, Map<Integer, Integer> mailAtt){
        PlayingRole playingRole = SessionManager.getInstance().getPlayer(playerId);
        GamePlayer gamePlayer = null;
        boolean isOnline = false;
        if (playingRole != null) {
            isOnline = true;
            gamePlayer = playingRole.getGamePlayer();
        }
        if (!isOnline && MailCache.getInstance().getMailCache(playerId) != null) {
            isOnline = true;
        }
        createMail(gamePlayer, playerId, MailConstants.MAIL_TYPE_LEGION, 0, legionId, mailTitle, mailContent, mailAtt, isOnline);
    }

    public void sendGMMail(byte addressee, String receiveId, String sender, String title, String content, String attach,
                           int validity) {
        // Xử lý ID người nhận - hỗ trợ cả dấu | và xuống dòng
        String[] playerIdList = null;
        if (receiveId != null) {
            // Thay thế xuống dòng bằng dấu |
            receiveId = receiveId.replaceAll("[\r\n]+", "|");
            // Split theo dấu |
            playerIdList = receiveId.split("\\|");
        }

        if (addressee == MailConstants.NOMINEE && (playerIdList == null || playerIdList.length == 0)) {
            logger.error("gm send mail item error! playerIdList=null");
            return;
        }

        if (StringUtils.isEmpty(title) || StringUtils.isEmpty(content)) {
            logger.error("gm send mail item error! title='' hoặc content=''");
            return;
        }

        Map<Integer, Integer> mailAtt = getMailAtt(attach);
        
        // Luôn sử dụng validity từ GM tool, nếu không có thì mặc định 30 ngày
        int validityDays = validity > 0 ? validity : MailConstants.SYS_MAIL_MAX_DELTIME;
        
        // Tính toán thời gian hết hạn chính xác
        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        // Reset time về 23:59:59 của ngày hết hạn
        cal.add(Calendar.DATE, validityDays);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        Date endTime = cal.getTime();
        
        logger.info("Creating mail with validity {} days, end time: {}", validityDays, endTime);

        if (addressee == MailConstants.NOMINEE && playerIdList != null) {
            for (String playerIdStr : playerIdList) {
                try {
                    int playerId = Integer.parseInt(playerIdStr.trim());
                    sendSysMailToSingle(playerId, title, content, mailAtt, endTime);
                } catch (NumberFormatException e) {
                    logger.error("Invalid player ID format: {}", playerIdStr);
                }
            }
            logger.info("gm send nominee mail item, playerIdList={}", Arrays.toString(playerIdList));
        } else if (addressee == MailConstants.EVERYONE) {
            Collection<PlayerBaseBean> pocAll = PlayerOfflineManager.getInstance().getAll();
            for (PlayerBaseBean playerOfflineCache : pocAll) {
                sendSysMailToSingle(playerOfflineCache.getId(), title, content, mailAtt, endTime);
            }
            logger.info("gm send everyone mail item");
        }
    }

    /**
     * 邮件附件
     *
     * @return
     */
    public Map<Integer, Integer> getMailAtt(String attach) {
        if (StringUtils.isEmpty(attach)) {
            return new HashMap<>();
        }
        Map<Integer, Integer> dbMailAtt = new HashMap<>();
        String[] awardStrList = StringUtils.split(attach, StringConstants.SEPARATOR_HENG);
        for (String awardStrPair : awardStrList) {
            String[] awardPairList = StringUtils.split(awardStrPair, StringConstants.SEPARATOR_SHU);
            dbMailAtt.put(Integer.valueOf(awardPairList[0]), Integer.valueOf(awardPairList[1]));
        }

        return dbMailAtt;
    }

    /**
     * 创建邮件
     * @param gamePlayer
     * @param playerId
     * @param mailType
     * @param fromPlayerId
     * @param fromLegionId
     * @param title
     * @param content
     * @param mailAtt
     * @param isOnline
     */
    private void createMail(final GamePlayer gamePlayer, final int playerId, byte mailType, int fromPlayerId, int fromLegionId,
                            String title, String content, Map<Integer, Integer> mailAtt, final boolean isOnline) {
        createMail(gamePlayer, playerId, mailType, fromPlayerId, fromLegionId, title, content, mailAtt, isOnline, null);
    }

    private void createMail(GamePlayer gamePlayer, int playerId, byte type, int fromPlayerId, int fromLegionId,
                          String title, String content, Map<Integer, Integer> mailAtt, boolean isOnline, Date customEndTime) {
        List<MailBean> mailCache = null;
        if (isOnline) {
            mailCache = MailCache.getInstance().getMailCache(playerId);
            if (mailCache == null) {
                mailCache = mailDao.getPlayerMailAll(playerId);
                if (mailCache == null) {
                    mailCache = new ArrayList<>();
                }
                MailCache.getInstance().addMailCache(playerId, mailCache);
            }
        }
        MailBean mailBean = new MailBean();
        mailBean.setPlayerId(playerId);
        mailBean.setType(type);
        mailBean.setState(MailConstants.MAIL_STATUS_NEW);
        switch (type) {
            case MailConstants.MAIL_TYPE_PLAYER:
                mailBean.setFromType("role");
                break;
            case MailConstants.MAIL_TYPE_LEGION:
                mailBean.setFromType("legion");
                break;
            case MailConstants.MAIL_TYPE_SYS:
                mailBean.setFromType("sys");
                break;
        }
        mailBean.setFromFid(fromPlayerId);
        mailBean.setFromLegionId(fromLegionId);
        mailBean.setTitle(title);
        mailBean.setContent(content);
        Date now = new Date();
        mailBean.setCreateTime(now);
        mailBean.setEndTime(customEndTime != null ? customEndTime : DateUtils.addDays(now, MailConstants.SYS_MAIL_MAX_DELTIME));
        mailBean.setAttachs(mailAtt);
        //添加cache
        if (mailCache != null) {
            mailCache.add(mailBean);
        }
        // 推送主界面消息
        if (gamePlayer != null && mailCache != null) {
            PushNewMail pushMsg = new PushNewMail();
            pushMsg.mail_info = build1MailMsg(mailBean, mailCache.size());
            gamePlayer.writeAndFlush(pushMsg.build(gamePlayer.alloc()));
        }
        asyncAddMail(mailBean);
    }

    public IOMailInfo build1MailMsg(MailBean mailBean,int mailId) {
        IOMailInfo mail_info = new IOMailInfo();
        mail_info.id = mailId;
        mail_info.from = new WsMessageBase.IOMailFrom(mailBean.getFromType(), mailBean.getFromFid(), mailBean.getFromLegionId());
        mail_info.type = mailBean.getType();
        mail_info.state = mailBean.getState();
        mail_info.title = mailBean.getTitle();
        mail_info.content = mailBean.getContent();
        mail_info.reward = new ArrayList<>();
        Map<Integer, Integer> attachs = mailBean.getAttachs();
        if (attachs != null) {
            for (Map.Entry<Integer, Integer> aEntry : attachs.entrySet()) {
                mail_info.reward.add(new WsMessageBase.IOMailAttach(aEntry.getKey(), aEntry.getValue()));
            }
        }
        mail_info.stime = mailBean.getCreateTime().getTime();
        mail_info.etime = mailBean.getEndTime().getTime();
        return mail_info;
    }

    /**
     * 邮件参数
     *
     * @return
     */
    public List<String> getDbMailParam(int sendNameType, String paramName, int titleType, String titleParam,
                                       int contentType, String contentParam) {
        List<String> dbMailParam = new ArrayList<>();
        // 内容中的参数
        if (contentType == MailConstants.PARAM_CONTENT) {
            dbMailParam.add(contentParam);
        }

        return dbMailParam;
    }

    public void asyncAddMail(MailBean mailBean){
        GameServer.executorService.execute(() -> {
            mailDao.addOneMail(mailBean);
        });
    }

    /**
     * 异步更新玩家邮件
     *
     * @param mailBean
     */
    public void asyncUpdateMailStatus(MailBean mailBean) {
        GameServer.executorService.execute(() -> mailDao.updateMailStatus(mailBean));
    }

    /**
     * 异步删除邮件
     *
     * @param id ：邮件唯一ID
     */
    public void asyncRemoveMail(final int id) {
        GameServer.executorService.execute(() -> mailDao.removeMail(id));
    }

    public boolean checkHasNewMail(int playerId) {
        boolean ret = false;
        List<MailBean> mailList = MailCache.getInstance().getMailCache(playerId);
        if (mailList != null && mailList.size() > 0) {
            ret = mailList.get(mailList.size() - 1).getState() == MailConstants.MAIL_STATUS_NEW;
        }
        return ret;
    }

}
