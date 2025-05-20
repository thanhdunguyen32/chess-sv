package game.module.tower.processor;

import game.GameServer;
import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.award.logic.AwardUtils;
import game.module.battle.logic.BattleManager;
import game.module.battle.logic.BattleSimulator;
import game.module.chapter.bean.BattleFormation;
import game.module.chapter.dao.BattleFormationCache;
import game.module.item.logic.ItemConstants;
import game.module.log.constants.LogConstants;
import game.module.rank.bean.DbRankTower;
import game.module.rank.dao.RankCache;
import game.module.rank.dao.RankDao;
import game.module.template.ChapterBattleTemplate;
import game.module.template.RewardTemplateSimple;
import game.module.template.TowerTemplate;
import game.module.tower.dao.TowerBattleTemplateCache;
import game.module.tower.dao.TowerTemplateCache;
import game.module.tower.logic.TowerReplayManager;
import game.module.user.logic.PlayerServerPropManager;
import game.module.user.logic.ScrollAnnoManager;
import game.session.SessionManager;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hexuhui
 */
@MsgCodeAnn(msgcode = WsMessageBattle.C2STowerBattleEnd.id, accessLimit = 200)
public class TowerBattleEndProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(TowerBattleEndProcessor.class);

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
        WsMessageBattle.C2STowerBattleEnd reqmsg = WsMessageBattle.C2STowerBattleEnd.parse(request);
        logger.info("tower battle end!player={},req={}", playerId, reqmsg);
        //battle id check
        boolean battleIdValid = BattleManager.getInstance().checkBattleId(playerId, reqmsg.battleid);
        if(!battleIdValid){
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageBattle.S2CTowerBattleEnd.msgCode,1468);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        Map<Integer,WsMessageBase.IOBHurt> leftHpMap = new HashMap<>();
        Map<Integer,WsMessageBase.IOBHurt> rightHpMap = new HashMap<>();
        if(reqmsg.as != null && reqmsg.as.length>0){
            for (WsMessageBase.IOBHurt iobHurt : reqmsg.as){
                rightHpMap.put(iobHurt.gsid,iobHurt);
            }
        }
        if(reqmsg.df != null && reqmsg.df.length>0){
            for (WsMessageBase.IOBHurt iobHurt : reqmsg.df){
                leftHpMap.put(iobHurt.gsid,iobHurt);
            }
        }
        GameServer.executorService.execute(() -> {
            //胜负判断
            WsMessageBase.IOBHurt[] rightHp = reqmsg.as;
            String battleResult = null;
            if(rightHp != null && rightHp.length>0) {
                boolean isRightAllDie = true;
                for (WsMessageBase.IOBHurt iobHurt : rightHp){
                    if(iobHurt.hp>0){
                        isRightAllDie = false;
                        break;
                    }
                }
                if(isRightAllDie){
                    battleResult = "win";
                }else{
                    battleResult = "lose";
                }
            }
            BattleFormation battleFormation = BattleFormationCache.getInstance().getBattleFormation(playerId);
            int towerLevel = PlayerServerPropManager.getInstance().getTower(playerId);
            Map<Integer, ChapterBattleTemplate> towerBattleTeam = TowerBattleTemplateCache.getInstance().getTowerBattleById(towerLevel);
            WsMessageBase.IOBattleResult battleRet = BattleSimulator.getInstance().simulateTemplate(playerId, battleFormation.getTower(), towerBattleTeam);
            logger.info("tower battle result:{}", battleRet);
            //战斗结果修正
            if(battleResult != null){
                battleRet.ret = battleResult;
            }
            for(WsMessageBase.IOBattleReportItem ioBattleReportItem : battleRet.report.left){
                if(leftHpMap.get(ioBattleReportItem.gsid) != null){
                    WsMessageBase.IOBHurt iobHurt = leftHpMap.get(ioBattleReportItem.gsid);
                    ioBattleReportItem.hurm = iobHurt.hurm;
                    ioBattleReportItem.heal = iobHurt.heal;
                }
            }
            for(WsMessageBase.IOBattleReportItem ioBattleReportItem : battleRet.report.right){
                if(rightHpMap.get(ioBattleReportItem.gsid) != null){
                    WsMessageBase.IOBHurt iobHurt = rightHpMap.get(ioBattleReportItem.gsid);
                    ioBattleReportItem.hurm = iobHurt.hurm;
                    ioBattleReportItem.heal = iobHurt.heal;
                }
            }
            List<WsMessageBase.IORewardItem> rewardItems = new ArrayList<>();
            if (battleRet.ret.equals("win")) {
                //save replay
                TowerReplayManager.getInstance().addTowerReplay(playingRole.getPlayerBean(), towerLevel, battleRet);
                //win add tower id
                int addNum = 1;
                if (towerLevel == 1) {
                    addNum = 2;
                }
                PlayerServerPropManager.getInstance().changeServerProp(playingRole, ItemConstants.TOWER_ID, addNum);
                
                //award
                TowerTemplate towerTemplate = TowerTemplateCache.getInstance().getTowerTemplateById(towerLevel - 1);
                for (RewardTemplateSimple rewardTemplateSimple : towerTemplate.getREWARDS()) {
                    AwardUtils.changeRes(playingRole, rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT(), LogConstants.MODULE_TOWER);
                    rewardItems.add(new WsMessageBase.IORewardItem(rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT()));
                }
                ScrollAnnoManager.getInstance().tower(playingRole,PlayerServerPropManager.getInstance().getServerPropCount(playerId,ItemConstants.TOWER_ID)-1);
                
                // Lấy tower level hiện tại từ PlayerServerProp
                int currentTowerLevel = PlayerServerPropManager.getInstance().getTower(playerId);
                
                // Cập nhật rank tower
                DbRankTower.DbRankTower1 towerRank = RankCache.getInstance().getTowerRankMap().get(playerId);
                if (towerRank == null) {
                    // Chỉ tạo mới và add vào list nếu chưa tồn tại
                    towerRank = new DbRankTower.DbRankTower1();
                    towerRank.setPlayerId(playerId);
                    towerRank.setLevel(currentTowerLevel);
                    RankCache.getInstance().getTowerRankList().add(towerRank);
                    RankCache.getInstance().getTowerRankMap().put(playerId, towerRank);
                } else {
                    // Nếu đã tồn tại thì chỉ cập nhật level
                    towerRank.setLevel(currentTowerLevel);
                }
                
                // Sắp xếp lại rank list theo level giảm dần
                List<DbRankTower.DbRankTower1> rankList = RankCache.getInstance().getTowerRankList();
                rankList.sort((a, b) -> Integer.compare(b.getLevel(), a.getLevel()));
                
                // Cập nhật vào DB
                DbRankTower dbRankTower = new DbRankTower();
                dbRankTower.setRankItem(rankList);
                RankDao.getInstance().updateDbRankTower(dbRankTower);
            }
            //ret
            WsMessageBattle.S2CTowerBattleEnd respmsg = new WsMessageBattle.S2CTowerBattleEnd();
            respmsg.result = battleRet;
            respmsg.items = rewardItems;
            respmsg.videoid = SessionManager.getInstance().generateSessionId();
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
        });

    }

}
