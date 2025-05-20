package game.module.dungeon.processor;

import game.GameServer;
import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.battle.logic.BattleManager;
import game.module.battle.logic.BattleSimulator;
import game.module.chapter.bean.BattleFormation;
import game.module.chapter.dao.BattleFormationCache;
import game.module.dungeon.bean.DungeonBean;
import game.module.dungeon.bean.DungeonNode;
import game.module.dungeon.dao.DungeonCache;
import game.module.dungeon.dao.DungeonDaoHelper;
import game.module.dungeon.logic.DungeonManager;
import game.module.template.ChapterBattleTemplate;
import game.session.SessionManager;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;
import ws.WsMessageBattle;
import ws.WsMessageHall;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hexuhui
 */
@MsgCodeAnn(msgcode = WsMessageBattle.C2SDungeonBattleEnd.id, accessLimit = 200)
public class DungeonBattleEndProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(DungeonBattleEndProcessor.class);

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
        WsMessageBattle.C2SDungeonBattleEnd reqmsg = WsMessageBattle.C2SDungeonBattleEnd.parse(request);
        logger.info("dungeon battle end!player={},req={}", playerId, reqmsg);
        //battle id check
        boolean battleIdValid = BattleManager.getInstance().checkBattleId(playerId, reqmsg.battleid);
        if(!battleIdValid){
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageBattle.S2CDungeonBattleEnd.msgCode,1468);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        Map<Integer,WsMessageBase.IOBHurt> leftHpMap = new HashMap<>();
        Map<Integer,WsMessageBase.IOBHurt> rightHpMap = new HashMap<>();
        Map<Integer,Integer> leftHpPercMap = new HashMap<>();
        Map<Integer,Integer> rightHpPercMap = new HashMap<>();
        if(reqmsg.as != null && reqmsg.as.length>0){
            for (WsMessageBase.IOBHurt iobHurt : reqmsg.as){
                rightHpMap.put(iobHurt.gsid,iobHurt);
                rightHpPercMap.put(iobHurt.born,iobHurt.hpperc);
            }
        }
        if(reqmsg.df != null && reqmsg.df.length>0){
            for (WsMessageBase.IOBHurt iobHurt : reqmsg.df){
                leftHpMap.put(iobHurt.gsid,iobHurt);
                leftHpPercMap.put(iobHurt.born,iobHurt.hpperc);
            }
        }
        //do
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
            DungeonBean dungeonBean = DungeonCache.getInstance().getDungeon(playerId);
            DungeonNode dungeonNode = dungeonBean.getDbDungeonNode().getNodelist().get(1);
            DungeonNode.DungeonNodeDetail dungeonNodeDetail = null;
            for (DungeonNode.DungeonNodePos dungeonNodePos : dungeonNode.getPoslist()) {
                if (dungeonNodePos.getChoose() != null && dungeonNodePos.getChoose().equals(1)) {
                    dungeonNodeDetail = dungeonNodePos.getDungeonNodeDetail();
                    break;
                }
            }
            BattleFormation battleFormation = BattleFormationCache.getInstance().getBattleFormation(playerId);
            Map<Integer, Long> dungeonBattleFormation = battleFormation.getDungeon();
            Map<Long, Integer> onlineGenerals = dungeonBean.getOnlineGenerals();
            WsMessageBase.IOBattleResult battleRet = BattleSimulator.getInstance().simulateDungeon(playerId, dungeonBattleFormation,
                    onlineGenerals, dungeonNodeDetail);
            logger.info("dungeon battle result:{}", battleRet);
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
            for(Map.Entry<Integer,Integer> aEntry : battleRet.lper.entrySet()){
                int aFormationPos = aEntry.getKey();
                if(leftHpPercMap.containsKey(aFormationPos)){
                    aEntry.setValue(leftHpPercMap.get(aFormationPos));
                }
            }
            if (battleRet.ret.equals("win")) {
                //save my hp
                for (Map.Entry<Integer, Long> aEntry : dungeonBattleFormation.entrySet()) {
                    long generalUuid = aEntry.getValue();
                    int formationPos = aEntry.getKey();
                    Integer savedHp = 0;
                    if(leftHpPercMap.containsKey(formationPos)){
                        savedHp = leftHpPercMap.get(formationPos);
                    }else {
                        savedHp = battleRet.lper.get(formationPos);
                    }
                    onlineGenerals.put(generalUuid, savedHp);
                }
                //save enemy hp
                Map<Integer, Integer> enemyHpPercent = dungeonNodeDetail.getEnemyHpPercent();
                for (Integer formationPos : dungeonNodeDetail.getBattleset().keySet()) {
                    enemyHpPercent.put(formationPos, 0);
                }
            } else {
                //save my hp
                for (Map.Entry<Integer, Long> aEntry : dungeonBattleFormation.entrySet()) {
                    long generalUuid = aEntry.getValue();
                    onlineGenerals.put(generalUuid, 0);
                }
                //save enemy hp
                Map<Integer, Integer> enemyHpPercent = dungeonNodeDetail.getEnemyHpPercent();
                for (Map.Entry<Integer, ChapterBattleTemplate> aEntry : dungeonNodeDetail.getBattleset().entrySet()) {
                    Integer formationPos = aEntry.getKey();
                    Integer hppercent = 0;
                    if(rightHpPercMap.containsKey(formationPos)){
                        hppercent = rightHpPercMap.get(formationPos);
                    }else {
                        hppercent = battleRet.rper.get(formationPos);
                    }
                    enemyHpPercent.put(formationPos, hppercent);
                }
            }
            //save bean
            DungeonDaoHelper.asyncUpdateDungeon(dungeonBean);
            //ret
            WsMessageBattle.S2CDungeonBattleEnd respmsg = new WsMessageBattle.S2CDungeonBattleEnd();
            respmsg.videoid = SessionManager.getInstance().generateSessionId();
            respmsg.result = battleRet;
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
        });

    }

}
