package game.module.mine.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.award.bean.GameConfig;
import game.module.mine.bean.DBMine;
import game.module.mine.bean.DBMineBattleRecord;
import game.module.mine.bean.DBMinePlayer;
import game.module.mine.dao.MineCache;
import game.module.offline.bean.PlayerBaseBean;
import game.module.offline.logic.PlayerOfflineManager;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;
import ws.WsMessageMine.C2SMineHistory;
import ws.WsMessageMine.S2CMineHistory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@MsgCodeAnn(msgcode = C2SMineHistory.id, accessLimit = 200)
public class MineHistoryProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(MineHistoryProcessor.class);

    @Override
    public void processByte(PlayingRole playingRole, RequestByteMessage requestMessage) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void processProto(PlayingRole playingRole, RequestProtoMessage request) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void processMy(PlayingRole playingRole, MyRequestMessage request) throws Exception {
        int playerId = playingRole.getId();
        logger.info("mine history!playerId={}", playerId);
        DBMine mineEntity = MineCache.getInstance().getDBMine();
        S2CMineHistory respMsg = new S2CMineHistory();
        if (mineEntity == null) {
            playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
            return;
        }
        if (mineEntity.getPlayers() == null || !mineEntity.getPlayers().containsKey(playerId)) {
            playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
            return;
        }
        DBMinePlayer minePlayerEntity = mineEntity.getPlayers().get(playerId);
        List<DBMineBattleRecord> mineRecordList = minePlayerEntity.getBattleRecord();
        respMsg.records = new ArrayList<>(mineRecordList.size());
        int i = 0;
        for (DBMineBattleRecord dbMineBattleRecord : mineRecordList) {
            WsMessageBase.IOMineHistory mineHistory = new WsMessageBase.IOMineHistory();
            respMsg.records.add(mineHistory);
            int targetPlayerId = dbMineBattleRecord.getTargetPlayerId();
            mineHistory.target_player_id = targetPlayerId;
            String targetPlayerName = "";
            PlayerBaseBean poc = PlayerOfflineManager.getInstance().getPlayerOfflineCache(targetPlayerId);
            if (poc != null) {
                targetPlayerName = poc.getName();
            }
            mineHistory.target_player_name = targetPlayerName;
            mineHistory.is_positive = dbMineBattleRecord.getPositive();
            mineHistory.is_success = dbMineBattleRecord.getIsSuccess();
            mineHistory.mine_point = dbMineBattleRecord.getMinePoint();
            mineHistory.type = dbMineBattleRecord.getType();
            if(dbMineBattleRecord.getGains() != null) {
                Integer rewardCoins = dbMineBattleRecord.getGains().get(GameConfig.PLAYER.GOLD);
                Integer rewardDiamond = dbMineBattleRecord.getGains().get(GameConfig.PLAYER.YB);
                mineHistory.gain = Arrays.asList(rewardCoins != null ? rewardCoins : 0, rewardDiamond != null ? rewardDiamond : 0);
            }
            if(dbMineBattleRecord.getAddTime() != null) {
                mineHistory.add_time = (int) (dbMineBattleRecord.getAddTime() / 1000);
            }
        }
        // ret
        playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
    }

}
