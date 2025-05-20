package game.module.tower.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.award.logic.AwardUtils;
import game.module.chapter.bean.ChapterBean;
import game.module.chapter.dao.ChapterCache;
import game.module.chapter.dao.ChapterTemplateCache;
import game.module.chapter.logic.ChapterDaoHelper;
import game.module.chapter.logic.ChapterManager;
import game.module.log.constants.LogConstants;
import game.module.mission.constants.MissionConstants;
import game.module.template.ChapterTemplate;
import game.module.template.RewardTemplateSimple;
import game.module.template.TowerAchieveTemplate;
import game.module.template.VipTemplate;
import game.module.tower.dao.TowerAchieveTemplateCache;
import game.module.user.logic.PlayerManager;
import game.module.user.logic.PlayerServerPropManager;
import game.module.vip.dao.VipTemplateCache;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;
import ws.WsMessageBattle;
import ws.WsMessageBigbattle;
import ws.WsMessageHall;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author hexuhui
 */
@MsgCodeAnn(msgcode = WsMessageBattle.C2STowerAchieveReward.id, accessLimit = 200)
public class TowerAchieveRewardProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(TowerAchieveRewardProcessor.class);

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
        logger.info("tower achieve reward!player={}", playerId);
        //progress
        int towerAchieveMark = TowerAchieveTemplateCache.getInstance().getMark();
        int achieveCount = PlayerManager.getInstance().getOtherCount(playerId, towerAchieveMark);
        TowerAchieveTemplate.TowerAchieve1 towerAchieveTemplate = TowerAchieveTemplateCache.getInstance().getTowerAchieveTemplate(achieveCount);
        //tower level
        int towerId = PlayerServerPropManager.getInstance().getTower(playerId);
        if(towerId < towerAchieveTemplate.getTOWER()){
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageBattle.S2CTowerAchieveReward.msgCode, 1431);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //do
        List<WsMessageBase.IORewardItem> rewardItems = new ArrayList<>();
        for (RewardTemplateSimple rewardTemplateSimple : towerAchieveTemplate.getREWARDS()){
            AwardUtils.changeRes(playingRole, rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT(), LogConstants.MODULE_TOWER);
            rewardItems.add(new WsMessageBase.IORewardItem(rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT()));
        }
        //save progress
        PlayerManager.getInstance().changeOther(playingRole, towerAchieveMark, 1);
        //ret
        WsMessageBattle.S2CTowerAchieveReward respmsg = new WsMessageBattle.S2CTowerAchieveReward();
        respmsg.rewards = rewardItems;
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
