package game.module.friend.processor_explore;

import game.GameServer;
import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.battle.logic.BattleManager;
import game.module.battle.logic.BattleSimulator;
import game.module.chapter.bean.BattleFormation;
import game.module.chapter.dao.BattleFormationCache;
import game.module.friend.bean.FriendBoss;
import game.module.friend.dao.FriendBossCache;
import game.module.friend.dao.FriendBossDaoHelper;
import game.module.friend.dao.MyFriendExploreTemplateCache;
import game.module.mail.logic.MailManager;
import game.module.manor.bean.DbBattleGeneral;
import game.module.manor.processor.ManorBattleEndProcessor;
import game.module.offline.bean.PlayerBaseBean;
import game.module.offline.logic.PlayerOfflineManager;
import game.module.template.MyFriendExploreTemplate;
import game.module.template.RewardTemplateSimple;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;
import ws.WsMessageBattle;
import ws.WsMessageHall;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author hexuhui
 */
@MsgCodeAnn(msgcode = WsMessageBattle.C2SGetFbossResult.id, accessLimit = 200)
public class GetFbossResultProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(GetFbossResultProcessor.class);

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
        WsMessageBattle.C2SGetFbossResult reqmsg = WsMessageBattle.C2SGetFbossResult.parse(request);
        logger.info("friend boss end!player={},req={}", playerId, reqmsg);
        //battle id check
        boolean battleIdValid = BattleManager.getInstance().checkBattleId(playerId, reqmsg.battleid);
        if(!battleIdValid){
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageBattle.S2CGetFbossResult.msgCode,1468);
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
            int boss_owner_id = reqmsg.boss_owner_id;
            FriendBoss friendBoss = FriendBossCache.getInstance().getFriendBoss(boss_owner_id);
            if (friendBoss == null) {
                WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageBattle.S2CGetFbossResult.msgCode, 1451);
                playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
                return;
            }
            //is dead
            FriendBoss.DbFriendBoss dbFriendBoss = friendBoss.getDbFriendBoss();
            Date now = new Date();
            if (dbFriendBoss.getEtime().before(now) || dbFriendBoss.getNowhp() <= 0) {
                WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageBattle.S2CGetFbossResult.msgCode, 1452);
                playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
                return;
            }
            WsMessageBase.IOBattleResult battleRet = BattleSimulator.getInstance().simulateFriendBoss(playerId, battleFormation.getNormal(),
                    dbFriendBoss);
            logger.info("friend boss battle result:{}", battleRet);
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
            //get hurmnum
            long myDamage = 0;
            if (reqmsg.df != null && reqmsg.df.length > 0) {
                myDamage = ManorBattleEndProcessor.getClientHurmSum(reqmsg.df);
            } else {
                myDamage = getMyDamage(battleRet);
            }
            //save last damage
            Map<Integer, Long> playerHurm = dbFriendBoss.getPlayerHurm();
            playerHurm.put(playerId, myDamage);
            if (battleRet.ret.equals("win")) {
                //send award
                String mailTitle = "Phần thưởng hoạt động BOSS bạn bè"; //"好友BOSS击杀奖励"
                String mailContent = "Người chơi【%1$s】BOSS của bạn bè đã bị bạn bè của bạn giết chết, phần thưởng như sau:"; //"玩家【%1$s】的BOSS被他的好友击杀，以下是奖励：";
                for (int aPlayerId : playerHurm.keySet()) {
                    MyFriendExploreTemplate friendExploreConfig = MyFriendExploreTemplateCache.getInstance().getFriendExploreConfig(dbFriendBoss.getId());
                    Map<Integer, Integer> mailAtt = new HashMap<>();
                    for (RewardTemplateSimple rewardTemplateSimple : friendExploreConfig.getRewards()) {
                        int gsid = rewardTemplateSimple.getGSID();
                        int itemCount = rewardTemplateSimple.getCOUNT();
                        //最后击杀奖励X6
                        if (playerId == aPlayerId) {
                            itemCount *= 6;
                        }
                        mailAtt.put(gsid, itemCount);
                    }
                    //send mail
                    PlayerBaseBean playerOfflineCache = PlayerOfflineManager.getInstance().getPlayerOfflineCache(boss_owner_id);
                    String formatMailContent = String.format(mailContent, playerOfflineCache.getName());
                    MailManager.getInstance().sendSysMailToSingle(aPlayerId, mailTitle, formatMailContent, mailAtt);
                }
                //remove
                FriendBossCache.getInstance().removeFriendBoss(boss_owner_id);
                FriendBossDaoHelper.asyncRemoveFriendBoss(friendBoss.getId());
            } else {
                //save enemy hp
                Map<Integer, DbBattleGeneral> formationHeros = dbFriendBoss.getFormationHeros();
                for (Map.Entry<Integer, DbBattleGeneral> aEntry : formationHeros.entrySet()) {
                    int formationPos = aEntry.getKey();
                    DbBattleGeneral dbBattleGeneral = aEntry.getValue();
                    int oldHp = dbBattleGeneral.getNowhp();
                    Long nowhp = battleRet.rhp.get(formationPos);
                    dbBattleGeneral.setNowhp(nowhp.intValue());
                    if (oldHp > 0 && dbBattleGeneral.getNowhp() <= 0) {
                        dbBattleGeneral.setNowhp(0);
                    }
                }
                dbFriendBoss.setNowhp(dbFriendBoss.getNowhp() - myDamage);
                //save bean
                FriendBossDaoHelper.asyncUpdateFriendBoss(friendBoss);
            }
            //ret
            WsMessageBattle.S2CGetFbossResult respmsg = new WsMessageBattle.S2CGetFbossResult();
            respmsg.result = battleRet;
            respmsg.kill = battleRet.ret.equals("win") ? 1 : 0;
            respmsg.bosshurm = myDamage;
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
        });

    }

    private long getMyDamage(WsMessageBase.IOBattleResult battleRet) {
        long ret = 0;
        for (WsMessageBase.IOBattleReportItem ioBattleReportItem : battleRet.report.left) {
            ret += ioBattleReportItem.hurm;
        }
        return ret;
    }

}
