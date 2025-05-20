package game.module.bigbattle.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.award.bean.GameConfig;
import game.module.award.logic.AwardUtils;
import game.module.bigbattle.dao.BigChapterTemplateCache;
import game.module.bigbattle.logic.BigBattleConstants;
import game.module.log.constants.LogConstants;
import game.module.mission.constants.MissionConstants;
import game.module.season.logic.SeasonManager;
import game.module.template.BigChapterTemplate;
import game.module.template.RewardTemplateSimple;
import game.module.template.VipTemplate;
import game.module.user.bean.PlayerProp;
import game.module.user.dao.PlayerOtherCache;
import game.module.vip.dao.VipTemplateCache;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;
import ws.WsMessageBigbattle;
import ws.WsMessageHall;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author hexuhui
 */
@MsgCodeAnn(msgcode = WsMessageBigbattle.C2SBigBattleFarm.id, accessLimit = 200)
public class BigBattleFarmProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(BigBattleFarmProcessor.class);

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
        WsMessageBigbattle.C2SBigBattleFarm reqmsg = WsMessageBigbattle.C2SBigBattleFarm.parse(request);
        logger.info("big battle farm!player={},req={}", playerId, reqmsg);
        //can attack
        int mapid = reqmsg.mapid;
        BigChapterTemplate bigChapterTemplate = BigChapterTemplateCache.getInstance().getBigChapterTemplateById(mapid);
        //level
        if (playingRole.getPlayerBean().getLevel() < bigChapterTemplate.getLOCK()) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageBigbattle.S2CBigBattleFarm.msgCode, 1711);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //count
        Integer passid = bigChapterTemplate.getPASSID();
        Integer passcount = bigChapterTemplate.getPASSCOUNT();
        Map<Integer, PlayerProp> playerOthers = PlayerOtherCache.getInstance().getPlayerOther(playerId);
        int my_passcount = 0;
        if (playerOthers.containsKey(passid)) {
            my_passcount = playerOthers.get(passid).getCount();
        }
        if (my_passcount <= passcount - 1) {//还没通关，无法farm
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageBigbattle.S2CBigBattleFarm.msgCode, 1715);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //count max
        VipTemplate vipTemplate = VipTemplateCache.getInstance().getVipTemplate(playingRole.getPlayerBean().getVipLevel());
        int typeIndex = BigBattleConstants.getTypeIndexByMapId(mapid);
        BigBattleConstants.BigBattleCountGsid bigBattleCountGsid = BigBattleConstants.BIG_BATTLE_COUNT_CONFIG[typeIndex];
        int attackCount = 0;
        if (playerOthers.containsKey(bigBattleCountGsid.COUNTGSID)) {
            attackCount = playerOthers.get(bigBattleCountGsid.COUNTGSID).getCount();
        }
        if (attackCount >= vipTemplate.getRIGHT().getBIGBATTLE()) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageBigbattle.S2CBigBattleFarm.msgCode, 1713);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //cost yuanbao
        int freeCount = 0;
        if (playerOthers.containsKey(bigBattleCountGsid.FREE)) {
            freeCount = playerOthers.get(bigBattleCountGsid.FREE).getCount();
        }
        if (freeCount >= bigBattleCountGsid.COUNTLIMIT && playingRole.getPlayerBean().getMoney() < BigBattleConstants.COST_YB) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageBigbattle.S2CBigBattleFarm.msgCode, 1714);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //do
        //count++
        if (freeCount < bigBattleCountGsid.COUNTLIMIT) {
            AwardUtils.changeRes(playingRole, bigBattleCountGsid.FREE, 1, LogConstants.BIG_BATTLE);
        } else {
            AwardUtils.changeRes(playingRole, GameConfig.PLAYER.YB, -BigBattleConstants.COST_YB, LogConstants.BIG_BATTLE);
        }
        AwardUtils.changeRes(playingRole, bigBattleCountGsid.COUNTGSID, 1, LogConstants.BIG_BATTLE);
        //update mission progress
        AwardUtils.changeRes(playingRole, MissionConstants.BIG_BATTLE_PMARK, 1, LogConstants.BIG_BATTLE);
        AwardUtils.changeRes(playingRole, MissionConstants.BIG_BATTLE, 1, LogConstants.BIG_BATTLE);
        //award
        List<RewardTemplateSimple> rewards = bigChapterTemplate.getREWARDS();
        List<WsMessageBase.IORewardItem> rewardItems = new ArrayList<>();
        int seasonAddon = SeasonManager.getInstance().getBigBattleAddon();
        for (RewardTemplateSimple rewardTemplateSimple : rewards) {
            int itemCount = (int) (rewardTemplateSimple.getCOUNT() * (1 + seasonAddon / 100f));
            AwardUtils.changeRes(playingRole, rewardTemplateSimple.getGSID(), itemCount, LogConstants.BIG_BATTLE);
            rewardItems.add(new WsMessageBase.IORewardItem(rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT()));
        }
        //ret
        WsMessageBigbattle.S2CBigBattleFarm respmsg = new WsMessageBigbattle.S2CBigBattleFarm();
        respmsg.rewards = rewardItems;
        respmsg.times = 1;
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
