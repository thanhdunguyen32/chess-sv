package game.module.legion.processor;

import game.common.GameConstants;
import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.award.bean.GameConfig;
import game.module.award.logic.AwardUtils;
import game.module.legion.bean.LegionBean;
import game.module.legion.bean.LegionMission;
import game.module.legion.bean.LegionPlayer;
import game.module.legion.dao.LegionCache;
import game.module.legion.dao.LegionDaoHelper;
import game.module.legion.dao.LegionFLevelTemplateCache;
import game.module.legion.dao.LegionMissionTemplateCache;
import game.module.legion.logic.LegionConstants;
import game.module.legion.logic.LegionManager;
import game.module.log.constants.LogConstants;
import game.module.pay.logic.ChargeInfoManager;
import game.module.season.logic.SeasonManager;
import game.module.template.LegionFLevelTemplate;
import game.module.template.LegionMissionTemplate;
import game.module.template.RewardTemplateRange;
import lion.common.MsgCodeAnn;
import lion.netty4.message.GamePlayer;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.apache.commons.lang3.RandomUtils;
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
@MsgCodeAnn(msgcode = WsMessageLegion.C2SLegionFactoryMissionFinish.id, accessLimit = 200)
public class LegionFMissionFinishProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(LegionFMissionFinishProcessor.class);

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
        WsMessageLegion.C2SLegionFactoryMissionFinish reqmsg = WsMessageLegion.C2SLegionFactoryMissionFinish.parse(request);
        logger.info("legion factory mission finish!player={},req={}", playerId, reqmsg);
        //not in legion
        long legionId = LegionManager.getInstance().getLegionId(playerId);
        if (legionId == 0) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageLegion.S2CLegionFactoryMissionFinish.msgCode, 1517);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //mission not exist
        long missionKey = reqmsg.key;
        LegionBean legionBean = LegionCache.getInstance().getLegionBean(legionId);
        LegionPlayer legionPlayer = legionBean.getDbLegionPlayers().getMembers().get(playerId);
        if (legionPlayer.getMissions() == null || !legionPlayer.getMissions().containsKey(missionKey)) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageLegion.S2CLegionFactoryMissionFinish.msgCode, 1537);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        LegionMission legionMission = legionPlayer.getMissions().get(missionKey);
        //not start or not finish
        Date now = new Date();
        if (legionMission.getEndTime() == null || legionMission.getEndTime().after(now)) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageLegion.S2CLegionFactoryMissionFinish.msgCode, 1541);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //do
        int missionId = legionMission.getId();
        LegionMissionTemplate legionMissionTemplate = LegionMissionTemplateCache.getInstance().getLegionMissionTemplate(missionId);
        List<RewardTemplateRange> items = legionMissionTemplate.getITEMS();
        List<WsMessageBase.IORewardItem> itemList = new ArrayList<>();
        int seasonAddon = SeasonManager.getInstance().getLegionAddon();
        int jtAddon = ChargeInfoManager.getInstance().getJtAddon(playerId);
        LegionFLevelTemplate legionFLevelTemplate = LegionFLevelTemplateCache.getInstance().getLegionFLevelTemplate(legionBean.getFlevel());
        int factoryAddon = legionFLevelTemplate.getRADD();
        for (RewardTemplateRange rewardTemplateRange : items) {
            int gsid = rewardTemplateRange.getGSID();
            int itemCount = RandomUtils.nextInt(rewardTemplateRange.getCOUNT().get(0), rewardTemplateRange.getCOUNT().get(1) + 1);
            float seasonAddonMy = factoryAddon / 100f;
            if (gsid == GameConfig.PLAYER.GOLD || gsid == LegionConstants.LEGION_COIN) {
                seasonAddonMy += (seasonAddon + jtAddon) / 100f;
            }
            AwardUtils.changeRes(playingRole, gsid, (int)(itemCount * (1+seasonAddonMy)), LogConstants.MODULE_LEGION);
            itemList.add(new WsMessageBase.IORewardItem(gsid, itemCount));
        }
        //update bean
        legionPlayer.getMissions().remove(missionKey);
        LegionDaoHelper.asyncUpdateLegionBean(legionBean);
        //ret
        WsMessageLegion.S2CLegionFactoryMissionFinish respmsg = new WsMessageLegion.S2CLegionFactoryMissionFinish();
        respmsg.items = itemList;
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
