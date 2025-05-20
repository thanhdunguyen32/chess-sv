package game.module.legion.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.award.bean.GameConfig;
import game.module.award.logic.AwardUtils;
import game.module.item.logic.ItemManager;
import game.module.legion.bean.LegionBean;
import game.module.legion.bean.LegionPlayer;
import game.module.legion.dao.LegionCache;
import game.module.legion.dao.LegionDaoHelper;
import game.module.legion.dao.MyLegionTemplateCache;
import game.module.legion.logic.LegionConstants;
import game.module.legion.logic.LegionManager;
import game.module.log.constants.LogConstants;
import game.module.pay.logic.ChargeInfoManager;
import game.module.season.logic.SeasonManager;
import game.module.template.MyLegionTemplate;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;
import ws.WsMessageHall;
import ws.WsMessageLegion;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;

/**
 * @author hexuhui
 */

@MsgCodeAnn(msgcode = WsMessageLegion.C2SLegionSign.id, accessLimit = 200)
public class LegionSignProcessor extends PlayingRoleMsgProcessor {
    
    private static final Logger logger = LoggerFactory.getLogger(LegionSignProcessor.class);
    
    // Cấu hình donate (cho lượt thứ 1 đến 5)
    private static final int[] GOLD_COST = {0, 2000, 6000, 8000, 10000};
    private static final int[] GOLD_DONATE_EXP = {10, 15, 15, 20, 20};
    private static final int[] GOLD_LEGION_EXP = {10, 15, 15, 20, 20};
    private static final int[] GOLD_REWARD = {20, 25, 25, 30, 30};
    
    private static final int[] MONEY_COST = {5, 10, 15, 20, 30};
    private static final int[] MONEY_DONATE_EXP = {15, 20, 25, 30, 35};
    private static final int[] MONEY_LEGION_EXP = {15, 20, 25, 30, 35};
    private static final int[] MONEY_REWARD = {30, 35, 40, 45, 50};
    
    private static final int MAX_DONATION_COUNT = 5; // Mỗi ngày 5 lượt
    private static final long TEST_REFRESH_INTERVAL_MS = 20000; // 20 giây cho test
    @Override
    public void processByte(PlayingRole playingRole, RequestByteMessage request) throws Exception {
        // Không sử dụng
    }
    
    @Override
    public void processProto(PlayingRole playingRole, RequestProtoMessage request) throws Exception {
        // Không sử dụng
    }
    
    @Override
    public void processMy(PlayingRole playingRole, MyRequestMessage request) throws Exception {
        int playerId = playingRole.getId();
        logger.info("Legion donate request from player={}", playerId);
        
        // Reset số lượt donate nếu chưa được làm mới (dựa vào lastDonateTime)
        resetDailyDonationIfNeeded(playingRole);
        
        // Đọc donateType: 1 (gold), 2 (money)
        int donateType = request.readInt();
        
        // Kiểm tra player có trong quân đoàn không
        long legionId = LegionManager.getInstance().getLegionId(playerId);
        if (legionId == 0) {
            WsMessageHall.S2CErrorCode resp = new WsMessageHall.S2CErrorCode(
                    WsMessageLegion.S2CLegionSign.msgCode, 1517);
            playingRole.writeAndFlush(resp.build(playingRole.alloc()));
            return;
        }
        
        // Lấy LegionPlayer từ LegionBean
        LegionBean legionBean = LegionCache.getInstance().getLegionBean(legionId);
        if (legionBean == null) return;
        LegionPlayer legionPlayer = legionBean.getDbLegionPlayers().getMembers().get(playerId);
        if (legionPlayer == null) return;
        
        // Lấy số lượt còn lại (từ DB)
        int remaining;
        if (donateType == 1) {
            remaining = legionPlayer.getDailyDonationGold();
        } else if (donateType == 2) {
            remaining = legionPlayer.getDailyDonationMoney();
        } else {
            WsMessageHall.S2CErrorCode resp = new WsMessageHall.S2CErrorCode(
                    WsMessageLegion.S2CLegionSign.msgCode, 1523);
            playingRole.writeAndFlush(resp.build(playingRole.alloc()));
            return;
        }
        
        if (remaining <= 0) {
            // Không còn lượt
            WsMessageHall.S2CErrorCode resp = new WsMessageHall.S2CErrorCode(
                    WsMessageLegion.S2CLegionSign.msgCode, 1520);
            playingRole.writeAndFlush(resp.build(playingRole.alloc()));
            return;
        }
        
        // Số lượt đã dùng = MAX - remaining
        int usedCount = MAX_DONATION_COUNT - remaining;
        int turnIndex = usedCount; // 0-based
        
        int cost, donateExp, legionExp, rewardCount;
        if (donateType == 1) {
            cost = GOLD_COST[turnIndex];
            donateExp = GOLD_DONATE_EXP[turnIndex];
            legionExp = GOLD_LEGION_EXP[turnIndex];
            rewardCount = GOLD_REWARD[turnIndex];
            if (turnIndex != 0 && !ItemManager.getInstance().checkItemEnough(
                    playingRole.getPlayerBean(), GameConfig.PLAYER.GOLD, cost)) {
                WsMessageHall.S2CErrorCode resp = new WsMessageHall.S2CErrorCode(
                        WsMessageLegion.S2CLegionSign.msgCode, 1521);
                playingRole.writeAndFlush(resp.build(playingRole.alloc()));
                return;
            }
        } else { // donateType == 2
            cost = MONEY_COST[turnIndex];
            donateExp = MONEY_DONATE_EXP[turnIndex];
            legionExp = MONEY_LEGION_EXP[turnIndex];
            rewardCount = MONEY_REWARD[turnIndex];
            if (!ItemManager.getInstance().checkItemEnough(
                    playingRole.getPlayerBean(), GameConfig.PLAYER.YB, cost)) {
                WsMessageHall.S2CErrorCode resp = new WsMessageHall.S2CErrorCode(
                        WsMessageLegion.S2CLegionSign.msgCode, 1522);
                playingRole.writeAndFlush(resp.build(playingRole.alloc()));
                return;
            }
        }
        
        // Trừ tài nguyên nếu cần
        if (cost > 0) {
            AwardUtils.changeRes(playingRole,
                    (donateType == 1 ? GameConfig.PLAYER.GOLD : GameConfig.PLAYER.YB),
                    -cost, LogConstants.MODULE_LEGION);
        }
        
        // Cộng kinh nghiệm cho quân đoàn
        LegionManager.getInstance().addExp(legionId, legionExp);
        
        // Cộng điểm cá nhân
        legionPlayer.setScore(legionPlayer.getScore() + donateExp);
        
        // Giảm số lượt còn lại đi 1 và cập nhật DB (bạn cần tích hợp DB update)
        if (donateType == 1) {
            legionPlayer.setDailyDonationGold(remaining - 1);
            logger.info("Player {} daily donation gold decreased to {}", playerId, remaining - 1);
        } else {
            legionPlayer.setDailyDonationMoney(remaining - 1);
            logger.info("Player {} daily donation money decreased to {}", playerId, remaining - 1);
        }
        // TODO: Gọi hàm cập nhật DB cho LegionPlayer (ví dụ: LegionDaoHelper.asyncUpdateLegionPlayer(legionPlayer);)
        //log số   lần còn  lại 2 trường hợp
        logger.info("Player {} daily donation gold decreased to {}", legionPlayer.getDailyDonationGold());
        logger.info("Player {} daily donation money decreased to {}", legionPlayer.getDailyDonationMoney());
        // Cấp phần thưởng cho player
        AwardUtils.changeRes(playingRole, LegionConstants.LEGION_COIN, rewardCount, LogConstants.MODULE_LEGION);
        
        WsMessageLegion.S2CLegionSign resp = new WsMessageLegion.S2CLegionSign();
        resp.exp = legionExp;
        resp.reward = Collections.singletonList(new WsMessageBase.IORewardItem(LegionConstants.LEGION_COIN, rewardCount));
        playingRole.writeAndFlush(resp.build(playingRole.alloc()));
    }
    
    /**
     * Kiểm tra ngày hiện tại với ngày của lastDonateTime (đã có trong LegionPlayer).
     * Nếu không trùng, reset số lượt cống hiến về MAX_DONATION_COUNT.
     */
    private void resetDailyDonationIfNeeded(PlayingRole playingRole) {
        int playerId = playingRole.getId();
        long legionId = LegionManager.getInstance().getLegionId(playerId);
        if (legionId == 0) return;
        LegionBean legionBean = LegionCache.getInstance().getLegionBean(legionId);
        if (legionBean == null) return;
        LegionPlayer legionPlayer = legionBean.getDbLegionPlayers().getMembers().get(playerId);
        if (legionPlayer == null) return;
        
        long now = System.currentTimeMillis();
        Date lastDonate = legionPlayer.getLastDonateTime();
        // Nếu chưa có lần donate nào, hoặc đã đủ 20 giây kể từ lần donate cuối
        //log đếm ngược 20 giây
        logger.info("lastDonateTime: {}", lastDonate);
        if (lastDonate == null || (now - lastDonate.getTime() >= TEST_REFRESH_INTERVAL_MS)) {
            // Reset số lượt về MAX_DONATION_COUNT (5 lượt)
            legionPlayer.setDailyDonationGold(MAX_DONATION_COUNT);
            legionPlayer.setDailyDonationMoney(MAX_DONATION_COUNT);
            // Cập nhật lastDonateTime thành thời gian hiện tại
            legionPlayer.setLastDonateTime(new Date());
            logger.info("Reset daily donation counts for player {} to {} (test refresh every {} ms)",
                    playerId, MAX_DONATION_COUNT, TEST_REFRESH_INTERVAL_MS);
            // TODO: Update DB cho LegionPlayer
        }
    }
}