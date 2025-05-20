package game.module.affair.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.affair.bean.AffairBean;
import game.module.affair.bean.DbAffairs;
import game.module.affair.bean.PlayerAffairs;
import game.module.affair.dao.AffairCache;
import game.module.affair.dao.AffairDaoHelper;
import game.module.affair.dao.AffairTemplateCache;
import game.module.affair.logic.AffairConstants;
import game.module.affair.logic.AffairManager;
import game.module.pay.logic.ChargeInfoManager;
import game.module.template.AffairsTemplate;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageAffair;
import ws.WsMessageBase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author hexuhui
 */
@MsgCodeAnn(msgcode = WsMessageAffair.C2SAffairList.id, accessLimit = 200)
public class AffairListProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(AffairListProcessor.class);

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
        logger.info("affair list!");
        int playerId = playingRole.getId();
        Date now = new Date();
        boolean needCreateAffairs = false;
        PlayerAffairs playerAffairs = AffairCache.getInstance().getAffairs(playerId);
        if (playerAffairs == null) {
            playerAffairs = AffairManager.getInstance().createPlayerAffairs(playerId);
            needCreateAffairs = true;
        } else {
            if (!DateUtils.isSameDay(playerAffairs.getLastVisitTime(), now)) {
                //remove unaccept affairs
                if (playerAffairs.getDbAffairs() != null && playerAffairs.getDbAffairs().getAffairList() != null) {
                    List<AffairBean> affairMap = playerAffairs.getDbAffairs().getAffairList();
                    affairMap.removeIf(affairBean -> affairBean.getStatus() == AffairConstants.AFFAIR_STATUS_NEW);
                }
                needCreateAffairs = true;
            }
        }
        //add init affairs
        if (needCreateAffairs) {
            int generateAffairNum = AffairManager.getInstance().getAffairNum(playerId, playingRole.getPlayerBean().getVipLevel());
            int specialNum = ChargeInfoManager.getInstance().getQzAddon(playerId);
            List<AffairBean> affairBeans = AffairManager.getInstance().generateAffairs(generateAffairNum, specialNum);
            if (playerAffairs.getDbAffairs() == null || playerAffairs.getDbAffairs().getAffairList() == null) {
                List<AffairBean> affairBeanList = new ArrayList<>();
                DbAffairs dbAffairs = new DbAffairs();
                dbAffairs.setAffairList(affairBeanList);
                playerAffairs.setDbAffairs(dbAffairs);
            }
            playerAffairs.getDbAffairs().getAffairList().addAll(affairBeans);
        }
        //
        playerAffairs.setLastVisitTime(now);
        if (playerAffairs.getId() == null) {
            AffairCache.getInstance().addPlayerAffair(playerAffairs);
            AffairDaoHelper.asyncInsertAffair(playerAffairs);
        } else {
            AffairDaoHelper.asyncUpdateAffair(playerAffairs);
        }
        //ret
        WsMessageAffair.S2CAffairList respmsg = new WsMessageAffair.S2CAffairList();
        respmsg.item_list = buildAffairList(playerId);
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

    public static List<WsMessageBase.IOAffairItem> buildAffairList(int playerId) {
        PlayerAffairs playerAffairs = AffairCache.getInstance().getAffairs(playerId);
        if (playerAffairs != null && playerAffairs.getDbAffairs() != null && playerAffairs.getDbAffairs().getAffairList() != null) {
            List<WsMessageBase.IOAffairItem> retlist = new ArrayList<>(playerAffairs.getDbAffairs().getAffairList().size());
            for (AffairBean affairBean : playerAffairs.getDbAffairs().getAffairList()) {
                WsMessageBase.IOAffairItem ioAffairItem = new WsMessageBase.IOAffairItem();
                retlist.add(ioAffairItem);
                ioAffairItem.id = affairBean.getTemplateId();
                //
                AffairsTemplate affairsTemplate = AffairTemplateCache.getInstance().getAffairsTemplate(affairBean.getTemplateId());
                Integer affairStar = affairsTemplate.getSTAR();
                ioAffairItem.gnum = AffairConstants.AFFAIR_GENERAL_NUM[affairStar - 1];
                ioAffairItem.gstar = affairStar;
                ioAffairItem.hour = AffairConstants.AFFAIR_COST_HOUR[affairStar - 1];
                ioAffairItem.cond = new ArrayList<>(affairBean.getCond());
                ioAffairItem.reward = Collections.singletonList(new WsMessageBase.IORewardItem(affairBean.getReward().getGSID(),
                        affairBean.getReward().getCOUNT()));
                ioAffairItem.lock = affairBean.getLock();
                ioAffairItem.create = affairBean.getStartTime() != null ? affairBean.getStartTime().getTime() : 0;
                ioAffairItem.etime = 0;
                if (affairBean.getStatus() == AffairConstants.AFFAIR_STATUS_PROGRESS) {
                    int costHour = AffairConstants.AFFAIR_COST_HOUR[affairStar - 1];
                    ioAffairItem.etime = DateUtils.addHours(affairBean.getStartTime(), costHour).getTime();
                } else if (affairBean.getStatus() == AffairConstants.AFFAIR_STATUS_FINISH) {
                    ioAffairItem.etime = affairBean.getStartTime().getTime();
                }
                if (affairBean.getOnlineGenerals() != null) {
                    ioAffairItem.arr = new ArrayList<>(affairBean.getOnlineGenerals());
                }
            }
            return retlist;
        } else {
            return new ArrayList<>();
        }
    }

}
