package game.module.affair.processor;

import com.google.common.primitives.Longs;
import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.affair.bean.AffairBean;
import game.module.affair.bean.PlayerAffairs;
import game.module.affair.dao.AffairCache;
import game.module.affair.dao.AffairDaoHelper;
import game.module.affair.dao.AffairTemplateCache;
import game.module.affair.logic.AffairConstants;
import game.module.affair.logic.AffairManager;
import game.module.hero.bean.GeneralBean;
import game.module.hero.dao.GeneralTemplateCache;
import game.module.hero.dao.GeneralCache;
import game.module.template.AffairsTemplate;
import game.module.template.GeneralTemplate;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageAffair;
import ws.WsMessageHall;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author hexuhui
 */
@MsgCodeAnn(msgcode = WsMessageAffair.C2SAffairStart.id, accessLimit = 200)
public class AffairStartProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(AffairStartProcessor.class);

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
        WsMessageAffair.C2SAffairStart reqmsg = WsMessageAffair.C2SAffairStart.parse(request);
        int playerId = playingRole.getId();
        logger.info("affair start!player={},req={}", playerId, reqmsg);
        int affair_index = reqmsg.affair_index;
        PlayerAffairs playerAffairs = AffairCache.getInstance().getAffairs(playerId);
        if (playerAffairs == null || playerAffairs.getDbAffairs() == null || playerAffairs.getDbAffairs().getAffairList() == null ||
                affair_index<0 || affair_index >= playerAffairs.getDbAffairs().getAffairList().size()) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageAffair.S2CAffairStart.msgCode, 30);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //is new
        AffairBean affairBean = playerAffairs.getDbAffairs().getAffairList().get(affair_index);
        if (affairBean.getStatus() != AffairConstants.AFFAIR_STATUS_NEW) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageAffair.S2CAffairStart.msgCode, 1420);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //general is exist
        long[] general_uuids = reqmsg.arr;
        Map<Long, GeneralBean> heroCache = GeneralCache.getInstance().getHeros(playerId);
        for (long aGeneralUuid : general_uuids) {
            if (!heroCache.containsKey(aGeneralUuid)) {
                WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageAffair.S2CAffairStart.msgCode, 1422);
                playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
                return;
            }
        }
        //general already online
//        List<AffairBean> affairMap = playerAffairs.getDbAffairs().getAffairList();
//        for (AffairBean affairBean1 : affairMap) {
//            List<Long> onlineGenerals = affairBean1.getOnlineGenerals();
//            for (long aGeneralUuid : general_uuids) {
//                if (onlineGenerals != null && onlineGenerals.contains(aGeneralUuid)) {
//                    WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageAffair.S2CAffairStart.msgCode, 1423);
//                    playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
//                    return;
//                }
//            }
//        }
        //判断cond
        Integer templateId = affairBean.getTemplateId();
        AffairsTemplate affairsTemplate = AffairTemplateCache.getInstance().getAffairsTemplate(templateId);
        Integer affairStar = affairsTemplate.getSTAR();
        int condStar = AffairConstants.COND_STAR[affairStar - 1];
        boolean isStarMatch = false;
        for (long aGeneralUuid : general_uuids) {
            GeneralBean generalBean = heroCache.get(aGeneralUuid);
            if (generalBean.getStar() >= condStar) {
                isStarMatch = true;
                break;
            }
        }
        if (!isStarMatch) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageAffair.S2CAffairStart.msgCode, 1424);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //cond match
        List<Integer> condList = affairBean.getCond();
        for (int aCond : condList) {
            boolean is1Match = false;
            AffairManager.CondTypeBean condType = AffairConstants.COND_TYPES[aCond - 1];
            switch (condType.getCondType()) {
                case "camp":
                    for (long aGeneralUuid : general_uuids) {
                        GeneralBean generalBean = heroCache.get(aGeneralUuid);
                        Integer generalTemplateId = generalBean.getTemplateId();
                        GeneralTemplate generalTemplate = GeneralTemplateCache.getInstance().getHeroTemplate(generalTemplateId);
                        if (generalTemplate.getCAMP().equals(condType.getCondValue())) {
                            is1Match = true;
                            break;
                        }
                    }
                    break;
                case "occu":
                    for (long aGeneralUuid : general_uuids) {
                        GeneralBean generalBean = heroCache.get(aGeneralUuid);
                        Integer generalTemplateId = generalBean.getTemplateId();
                        GeneralTemplate generalTemplate = GeneralTemplateCache.getInstance().getHeroTemplate(generalTemplateId);
                        if (generalTemplate.getOCCU().equals(condType.getCondValue())) {
                            is1Match = true;
                            break;
                        }
                    }
                    break;
            }
            if (!is1Match) {
                WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageAffair.S2CAffairStart.msgCode, 1425);
                playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
                return;
            }
        }
        //do
        affairBean.setStatus(AffairConstants.AFFAIR_STATUS_PROGRESS);
        affairBean.setOnlineGenerals(Longs.asList(general_uuids));
        affairBean.setStartTime(new Date());
        affairBean.setLock(1);
        AffairDaoHelper.asyncUpdateAffair(playerAffairs);
        //ret
        WsMessageAffair.S2CAffairStart respmsg = new WsMessageAffair.S2CAffairStart();
        respmsg.affair_index = affair_index;
        respmsg.item_list = AffairListProcessor.buildAffairList(playerId);
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

    public static void main(String[] args) {
        long[] l1 = new long[]{1, 2, 3, 4};
        List<Long> longs = Longs.asList(l1);
        System.out.println(longs);
    }

}
