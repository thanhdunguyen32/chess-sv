package game.module.manor.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.manor.bean.DbBattleGeneral;
import game.module.manor.bean.ManorBean;
import game.module.manor.dao.ManorCache;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;
import ws.WsMessageBattle;
import ws.WsMessageHall;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author hexuhui
 */
@MsgCodeAnn(msgcode = WsMessageBattle.C2SManorEnemyInfo.id, accessLimit = 200)
public class ManorEnemyInfoProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(ManorEnemyInfoProcessor.class);

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
        WsMessageBattle.C2SManorEnemyInfo reqmsg = WsMessageBattle.C2SManorEnemyInfo.parse(request);
        logger.info("manor enemy info!player={},req={}", playerId, reqmsg);
        int enemy_index = reqmsg.index;
        ManorBean manorBean = ManorCache.getInstance().getManor(playerId);
        ManorBean.DbManorField manorField = manorBean.getManorField();
        if (enemy_index >= manorField.getEnemys().size()) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageBattle.S2CManorBattleStart.msgCode, 1386);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //ret
        WsMessageBattle.S2CManorEnemyInfo respmsg = new WsMessageBattle.S2CManorEnemyInfo();
        if (enemy_index == -1) {//boss info
            respmsg.boss = new WsMessageBase.IOManorBoss();
            respmsg.boss.bossid = manorField.getBossid();
            respmsg.boss.maxhp = manorField.getBossMaxHp();
            respmsg.boss.nowhp = manorField.getBossNowHp();
            respmsg.boss.lastdamage = manorField.getBossLastDamage();
            respmsg.boss.bset = new HashMap<>(manorField.getBossFormationHeros().size());
            for (Map.Entry<Integer, DbBattleGeneral> aEntry : manorField.getBossFormationHeros().entrySet()) {
                WsMessageBase.IOGeneralSimple ioGeneralSimple = new WsMessageBase.IOGeneralSimple();
                ioGeneralSimple.pos = aEntry.getKey();
                DbBattleGeneral dbBattleGeneral = aEntry.getValue();
                ioGeneralSimple.gsid = dbBattleGeneral.getChapterBattleTemplate().getGsid();
                ioGeneralSimple.level = dbBattleGeneral.getChapterBattleTemplate().getLevel();
                ioGeneralSimple.hpcover = dbBattleGeneral.getChapterBattleTemplate().getHpcover();
                ioGeneralSimple.pclass = dbBattleGeneral.getChapterBattleTemplate().getPclass();
                ioGeneralSimple.exhp = dbBattleGeneral.getChapterBattleTemplate().getExhp();
                ioGeneralSimple.exatk = dbBattleGeneral.getChapterBattleTemplate().getExatk();
                respmsg.boss.bset.put(aEntry.getKey(), ioGeneralSimple);
            }
        } else {
            ManorBean.DbManorEnemy enemyMap = manorField.getEnemys().get(enemy_index);
            respmsg.enemy = new HashMap<>(enemyMap.getFormationHeros().size());
            for (Map.Entry<Integer, DbBattleGeneral> aEntry : enemyMap.getFormationHeros().entrySet()) {
                WsMessageBase.IOGeneralSimple ioGeneralSimple = new WsMessageBase.IOGeneralSimple();
                ioGeneralSimple.pos = aEntry.getKey();
                DbBattleGeneral dbBattleGeneral = aEntry.getValue();
                ioGeneralSimple.gsid = dbBattleGeneral.getChapterBattleTemplate().getGsid();
                ioGeneralSimple.level = dbBattleGeneral.getChapterBattleTemplate().getLevel();
                ioGeneralSimple.hpcover = dbBattleGeneral.getChapterBattleTemplate().getHpcover();
                ioGeneralSimple.pclass = dbBattleGeneral.getChapterBattleTemplate().getPclass();
                ioGeneralSimple.exhp = dbBattleGeneral.getChapterBattleTemplate().getExhp();
                ioGeneralSimple.exatk = dbBattleGeneral.getChapterBattleTemplate().getExatk();
                ioGeneralSimple.nowhp = dbBattleGeneral.getNowhp();
                respmsg.enemy.put(aEntry.getKey(), ioGeneralSimple);
            }
        }
        //ret
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
