package game.module.rank.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.offline.bean.PlayerBaseBean;
import game.module.offline.logic.PlayerOfflineManager;
import game.module.pvp.bean.PvpBean;
import game.module.pvp.dao.PvpCache;
import game.module.pvp.logic.PvpConstants;
import game.module.rank.bean.*;
import game.module.rank.dao.RankCache;
import game.module.rank.dao.RankLikeCache;
import game.module.rank.logic.RankManager.RankMyItem;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase.IORankPlayer;
import ws.WsMessageHall.C2SRankView;
import ws.WsMessageHall.S2CErrorCode;
import ws.WsMessageHall.S2CRankView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@MsgCodeAnn(msgcode = C2SRankView.id, accessLimit = 0)
public class RankViewProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(RankViewProcessor.class);

    @Override
    public void processByte(PlayingRole hero, RequestByteMessage request) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void processProto(PlayingRole hero, RequestProtoMessage request) throws Exception {
    }

    @Override
    public void processMy(PlayingRole playingRole, MyRequestMessage request) throws Exception {
        int playerId = playingRole.getId();
        C2SRankView reqMsg = C2SRankView.parse(request);
        String rank_type = reqMsg.rtype;
        logger.info("rank view!playerId={},rank_type={}", playerId, rank_type);
        switch (rank_type) {
            case "level":
                rankPlayerLevel(playingRole, rank_type);
                break;
            case "power":
                rankTeamBf(playingRole, rank_type);
                break;
            case "tower":
                rankTower(playingRole, rank_type);
                break;
            case "pvp":
                rankArena(playingRole, rank_type);
                break;
            case "stars":
                rankHeroStars(playingRole, rank_type);
                break;
            case "pvpteam":
                rankPvpTeam(playingRole, rank_type);
                break;
            case "pvpmulti":
                rankPvpMulti(playingRole, rank_type);
                break;
            case "dungeon":
                rankDungeon(playingRole, rank_type);
                break;
            case "pvpstage"://王者演武竞技段位排行
                rankKingPvp(playingRole, rank_type);
                break;
            case "pvpking"://王者演武竞技王者排行
                rankKingPvpMaster(playingRole, rank_type);
                break;
            default:
                S2CErrorCode respMsg = new S2CErrorCode(S2CRankView.msgCode, 1202);
                playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
                break;
        }
    }

    private void rankDungeon(PlayingRole playingRole, String rank_type) {
        List<DbRankDungeon.DbRankDungeon1> rankList = RankCache.getInstance().getDungeonRankList();
        S2CRankView respMsg = new S2CRankView();
        respMsg.rtype = rank_type;
        respMsg.selfrank = 501;
        respMsg.my_rank_change = 0;
        DbRankDungeon.DbRankDungeon1 rankItem = RankCache.getInstance().getRankDungeonById(playingRole.getId());
        if (rankItem != null) {
            int myRank = rankList.indexOf(rankItem) + 1;
            respMsg.selfrank = myRank;
            respMsg.my_rank_change = rankItem.getRankChange();
        } else {
            RankMyItem rankMyItem = RankCache.getInstance().getDungeonRankMy(playingRole.getId());
            if (rankMyItem != null) {
                respMsg.selfrank = rankMyItem.getRank();
                respMsg.my_rank_change = rankMyItem.getRankChange();
            }
        }
        respMsg.list = new ArrayList<>(rankList.size());
        for (DbRankDungeon.DbRankDungeon1 dbRankDungeon1 : rankList) {
            PlayerBaseBean playerOfflineCache = PlayerOfflineManager.getInstance()
                    .getPlayerOfflineCache(dbRankDungeon1.getPlayerId());
            int rankLikeCount = RankLikeCache.getInstance().getPlayerBeLikeCount(dbRankDungeon1.getPlayerId());
            respMsg.list.add(new IORankPlayer(playerOfflineCache.getId(), playerOfflineCache.getName(), playerOfflineCache.getIconid(),
                    playerOfflineCache.getHeadid(), playerOfflineCache.getFrameid(), playerOfflineCache.getLevel(), playerOfflineCache.getPower(),
                    playerOfflineCache.getVipLevel(), dbRankDungeon1.getRankChange(), 0, 0, 0, 0, 0, rankLikeCount, dbRankDungeon1.getChapter(),
                    dbRankDungeon1.getNode(), 0, 0));
        }
        playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
    }

    private void rankKingPvpMaster(PlayingRole playingRole, String rank_type) {
        List<DbRankDungeon.DbRankDungeon1> rankList = RankCache.getInstance().getDungeonRankList();
        S2CRankView respMsg = new S2CRankView();
        respMsg.rtype = rank_type;
        respMsg.selfrank = 501;
        respMsg.my_rank_change = 0;
        DbRankDungeon.DbRankDungeon1 rankItem = RankCache.getInstance().getRankDungeonById(playingRole.getId());
        if (rankItem != null) {
            int myRank = rankList.indexOf(rankItem) + 1;
            respMsg.selfrank = myRank;
            respMsg.my_rank_change = rankItem.getRankChange();
        } else {
            RankMyItem rankMyItem = RankCache.getInstance().getDungeonRankMy(playingRole.getId());
            if (rankMyItem != null) {
                respMsg.selfrank = rankMyItem.getRank();
                respMsg.my_rank_change = rankMyItem.getRankChange();
            }
        }
        respMsg.list = new ArrayList<>(rankList.size());
        playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
    }

    private void rankKingPvp(PlayingRole playingRole, String rank_type) {
        List<DbRankKingPvp.DbRankKingPvp1> rankList = RankCache.getInstance().getKingPvpRankList();
        S2CRankView respMsg = new S2CRankView();
        respMsg.rtype = rank_type;
        respMsg.selfrank = 501;
        respMsg.my_rank_change = 0;
        DbRankKingPvp.DbRankKingPvp1 rankItem = RankCache.getInstance().getRankKingPvpById(playingRole.getId());
        if (rankItem != null) {
            int myRank = rankList.indexOf(rankItem) + 1;
            respMsg.selfrank = myRank;
            respMsg.my_rank_change = rankItem.getRankChange();
        } else {
            RankMyItem rankMyItem = RankCache.getInstance().getKingPvpRankMy(playingRole.getId());
            if (rankMyItem != null) {
                respMsg.selfrank = rankMyItem.getRank();
                respMsg.my_rank_change = rankMyItem.getRankChange();
            }
        }
        respMsg.list = new ArrayList<>(rankList.size());
        for (DbRankKingPvp.DbRankKingPvp1 dbRankKingPvp1 : rankList) {
            PlayerBaseBean playerOfflineCache = PlayerOfflineManager.getInstance()
                    .getPlayerOfflineCache(dbRankKingPvp1.getPlayerId());
            int rankLikeCount = RankLikeCache.getInstance().getPlayerBeLikeCount(dbRankKingPvp1.getPlayerId());
            respMsg.list.add(new IORankPlayer(playerOfflineCache.getId(), playerOfflineCache.getName(), playerOfflineCache.getIconid(),
                    playerOfflineCache.getHeadid(), playerOfflineCache.getFrameid(), playerOfflineCache.getLevel(), playerOfflineCache.getPower(),
                    playerOfflineCache.getVipLevel(), dbRankKingPvp1.getRankChange(), 0, 0, 0, 0, 0, rankLikeCount,
                    0, 0, dbRankKingPvp1.getStage(), dbRankKingPvp1.getStar()));
        }
        playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
    }

    private void rankPvpMulti(PlayingRole playingRole, String rank_type) {
        S2CRankView respMsg = new S2CRankView();
        respMsg.rtype = rank_type;
        respMsg.selfrank = 501;
        respMsg.my_rank_change = 0;
        respMsg.list = new ArrayList<>();
        playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
    }

    private void rankPvpTeam(PlayingRole playingRole, String rank_type) {
        S2CRankView respMsg = new S2CRankView();
        respMsg.rtype = rank_type;
        respMsg.selfrank = 501;
        respMsg.my_rank_change = 0;
        respMsg.list = new ArrayList<>();
        playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
    }

    private void rankHeroStars(PlayingRole playingRole, String rank_type) {
        List<DBRankHeroStars1> rankList = RankCache.getInstance().getDbRankHeroStars();
        S2CRankView respMsg = new S2CRankView();
        respMsg.rtype = rank_type;
        respMsg.selfrank = 501;
        respMsg.my_rank_change = 0;
        DBRankHeroStars1 rankItem = RankCache.getInstance().getDBRankHeroStarsById(playingRole.getId());
        if (rankItem != null) {
            int myRank = rankList.indexOf(rankItem) + 1;
            respMsg.selfrank = myRank;
            respMsg.my_rank_change = rankItem.getRankChange();
        } else {
            RankMyItem rankMyItem = RankCache.getInstance().getHeroStarsRankMy(playingRole.getId());
            if (rankMyItem != null) {
                respMsg.selfrank = rankMyItem.getRank();
                respMsg.my_rank_change = rankMyItem.getRankChange();
            }
        }
        respMsg.list = new ArrayList<>(rankList.size());
        int i = 0;
        for (DBRankHeroStars1 rankItem1 : rankList) {
            PlayerBaseBean playerOfflineCache = PlayerOfflineManager.getInstance()
                    .getPlayerOfflineCache(rankItem1.getPlayerId());
            int rankLikeCount = RankLikeCache.getInstance().getPlayerBeLikeCount(rankItem1.getPlayerId());
//			respMsg.list[i++] = new IORankPlayer(rankLikeCount, playerOfflineCache.getModelId(),
//					playerOfflineCache.getLevel(), playerOfflineCache.getIcon(), rankItem1.getStarSum(),
//					playerOfflineCache.getHeadFrame(), 0, playerOfflineCache.getVipLevel(),
//					rankItem1.getRankChange(), playerOfflineCache.getName(), 0,
//					playerOfflineCache.getBattle_force(), rankItem1.getPlayerId());
        }
        playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
    }

    private void rankTeamBf(PlayingRole playingRole, String rank_type) {
        List<DBRankBattleForce1> rankList = RankCache.getInstance().getDbRankTeamBattleForce();
        S2CRankView respMsg = new S2CRankView();
        respMsg.rtype = rank_type;
        respMsg.selfrank = 501;
        respMsg.my_rank_change = 0;
        DBRankBattleForce1 rankItem = RankCache.getInstance().getDBRankTeamBattleForceById(playingRole.getId());
        if (rankItem != null) {
            int myRank = rankList.indexOf(rankItem) + 1;
            respMsg.selfrank = myRank;
            respMsg.my_rank_change = rankItem.getRankChange();
        } else {
            RankMyItem rankTeamBattleForceMyItem = RankCache.getInstance()
                    .getTeamBattleForceRankMy(playingRole.getId());
            if (rankTeamBattleForceMyItem != null) {
                respMsg.selfrank = rankTeamBattleForceMyItem.getRank();
                respMsg.my_rank_change = rankTeamBattleForceMyItem.getRankChange();
            }
        }
        respMsg.list = new ArrayList<>(rankList.size());
        int i = 0;
        for (DBRankBattleForce1 rankItem1 : rankList) {
            PlayerBaseBean playerOfflineCache = PlayerOfflineManager.getInstance()
                    .getPlayerOfflineCache(rankItem1.getPlayerId());
            int rankLikeCount = RankLikeCache.getInstance().getPlayerBeLikeCount(rankItem1.getPlayerId());
//			respMsg.list[i++] = new IORankPlayer(rankLikeCount, playerOfflineCache.getModelId(),
//					playerOfflineCache.getLevel(), playerOfflineCache.getIcon(), 0,
//					playerOfflineCache.getHeadFrame(), 0, playerOfflineCache.getVipLevel(),
//					rankItem1.getRankChange(), playerOfflineCache.getName(), 0,
//					playerOfflineCache.getBattle_force(), rankItem1.getPlayerId());
        }
        playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
    }

    private void rankPlayerLevel(PlayingRole playingRole, String rankType) {
        List<DBPlayerLevelRank1> rankList = RankCache.getInstance().getPlayerLevelRanks();
        S2CRankView respMsg = new S2CRankView();
        respMsg.rtype = rankType;
        respMsg.selfrank = 501;
        respMsg.my_rank_change = 0;
        DBPlayerLevelRank1 rankItem = RankCache.getInstance().getPlayerLevelRankById(playingRole.getId());
        if (rankItem != null) {
            int myRank = rankList.indexOf(rankItem) + 1;
            respMsg.selfrank = myRank;
            respMsg.my_rank_change = rankItem.getRankChange();
        } else {
            RankMyItem rankMyItem = RankCache.getInstance().getPlayerLevelRankMy(playingRole.getId());
            if (rankMyItem != null) {
                respMsg.selfrank = rankMyItem.getRank();
                respMsg.my_rank_change = rankMyItem.getRankChange();
            }
        }
        respMsg.list = new ArrayList<>(rankList.size());
        for (DBPlayerLevelRank1 dbArenaWinRank1 : rankList) {
            PlayerBaseBean playerOfflineCache = PlayerOfflineManager.getInstance()
                    .getPlayerOfflineCache(dbArenaWinRank1.getPlayerId());
            int rankLikeCount = RankLikeCache.getInstance().getPlayerBeLikeCount(dbArenaWinRank1.getPlayerId());
            respMsg.list.add(new IORankPlayer(playerOfflineCache.getId(), playerOfflineCache.getName(), playerOfflineCache.getIconid(),
                    playerOfflineCache.getHeadid(), playerOfflineCache.getFrameid(), playerOfflineCache.getLevel(), playerOfflineCache.getPower(),
                    playerOfflineCache.getVipLevel(), dbArenaWinRank1.getRankChange(), 0, 0, 0, 0, 0, rankLikeCount, 0, 0, 0, 0));
        }
        playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
    }

    private void rankTower(PlayingRole playingRole, String rankType) {
        List<DbRankTower.DbRankTower1> rankList = RankCache.getInstance().getTowerRankList();
        if (rankList == null) {
            logger.warn("rankList is null in rankTower");
            rankList = new ArrayList<>(); // Khởi tạo danh sách rỗng để tránh lỗi
        }
        
        S2CRankView respMsg = new S2CRankView();
        respMsg.rtype = rankType;
        respMsg.selfrank = 501;
        respMsg.my_rank_change = 0;
        DbRankTower.DbRankTower1 rankItem = RankCache.getInstance().getRankTowerById(playingRole.getId());
        if (rankItem != null) {
            int myRank = rankList.indexOf(rankItem) + 1;
            respMsg.selfrank = myRank;
            respMsg.my_rank_change = rankItem.getRankChange();
        } else {
            RankMyItem rankMyItem = RankCache.getInstance().getTowerRankMy(playingRole.getId());
            if (rankMyItem != null) {
                respMsg.selfrank = rankMyItem.getRank();
                respMsg.my_rank_change = rankMyItem.getRankChange();
            }
        }
        respMsg.list = new ArrayList<>(rankList.size());
        int i = 0;
        for (DbRankTower.DbRankTower1 dbArenaWinRank1 : rankList) {
            if (dbArenaWinRank1 == null) {
                logger.warn("Found null element in rankList");
                continue; // Bỏ qua phần tử null
            }
            PlayerBaseBean playerOfflineCache = PlayerOfflineManager.getInstance()
                    .getPlayerOfflineCache(dbArenaWinRank1.getPlayerId());
            if (playerOfflineCache == null) {
                logger.warn("Player {} not found in offline cache", dbArenaWinRank1.getPlayerId());
                continue; // Bỏ qua phần tử này
            }
            int rankLikeCount = RankLikeCache.getInstance().getPlayerBeLikeCount(dbArenaWinRank1.getPlayerId());
            respMsg.list.add(new IORankPlayer(playerOfflineCache.getId(), playerOfflineCache.getName(), playerOfflineCache.getIconid(),
                    playerOfflineCache.getHeadid(), playerOfflineCache.getFrameid(), playerOfflineCache.getLevel(), playerOfflineCache.getPower(),
                    playerOfflineCache.getVipLevel(), dbArenaWinRank1.getRankChange(), 0, 0, 0, 0, dbArenaWinRank1.getLevel(), rankLikeCount, 0, 0, 0, 0));
        }
        logger.info("Processing rankTower for playerId={}, rankType={}", playingRole.getId(), rankType);
        logger.info("Rank list size: {}", rankList.size());
        logger.info("Rank list content: {}", rankList);
        
        playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
    }

    private void rankArena(PlayingRole playingRole, String rankType) {
        // Chỉ xử lý cho rank_type="pvp"
        if (!rankType.equals("pvp")) {
            return;
        }

        int playerId = playingRole.getId();
        PvpBean pvpBean = PvpCache.getInstance().getPvpBean();
        List<Integer> rankPlayers = pvpBean.getPlayers();
        Map<Integer, Integer> myRankMap = pvpBean.getMyRankMap();
        Map<Integer, Integer> pvpScore = pvpBean.getPvpScore();

        S2CRankView respMsg = new S2CRankView();
        respMsg.rtype = rankType;
        
        // Lấy rank của người chơi hiện tại
        Integer myRankIndex = myRankMap.get(playerId);
        if (myRankIndex != null) {
            respMsg.selfrank = myRankIndex + 1;
        } else {
            respMsg.selfrank = 501; // Ngoài top 500
        }
        respMsg.my_rank_change = 0;

        // Tạo danh sách rank
        respMsg.list = new ArrayList<>();
        for (int i = 0; i < rankPlayers.size(); i++) {
            Integer pid = rankPlayers.get(i);
            PlayerBaseBean playerOfflineCache = PlayerOfflineManager.getInstance()
                    .getPlayerOfflineCache(pid);
            if (playerOfflineCache == null) {
                logger.warn("Player {} not found in offline cache", pid);
                continue;
            }

            int rankLikeCount = RankLikeCache.getInstance().getPlayerBeLikeCount(pid);
            int score = pvpScore.getOrDefault(pid, PvpConstants.INIT_PVP_SCORE);

            respMsg.list.add(new IORankPlayer(
                playerOfflineCache.getId(),
                playerOfflineCache.getName(), 
                playerOfflineCache.getIconid(),
                playerOfflineCache.getHeadid(), 
                playerOfflineCache.getFrameid(),
                playerOfflineCache.getLevel(),
                playerOfflineCache.getPower(),
                playerOfflineCache.getVipLevel(),
                0, // rank_change 
                0, 0,
                score, // Hiển thị điểm PvP
                0, 0,
                rankLikeCount,
                0, 0, 0, 0
            ));
        }

        playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
    }

}
