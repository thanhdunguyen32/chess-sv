package game.module.manor.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.award.bean.GameConfig;
import game.module.award.logic.AwardUtils;
import game.module.log.constants.LogConstants;
import game.module.manor.bean.ManorBean;
import game.module.manor.dao.ManorCache;
import game.module.manor.dao.ManorDaoHelper;
import game.module.manor.dao.ManorTemplateCache;
import game.module.manor.logic.ManorConstants;
import game.module.manor.logic.ManorManager;
import game.module.template.ManorTemplate;
import game.module.user.logic.PlayerManager;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageHall;
import ws.WsMessageManor;

import java.util.Date;

@MsgCodeAnn(msgcode = WsMessageManor.C2SManorPatrol.id, accessLimit = 100)
public class ManorFieldPatrolProcessor extends PlayingRoleMsgProcessor {

    private static final Logger logger = LoggerFactory.getLogger(ManorFieldPatrolProcessor.class);

    @Override
    public void processByte(PlayingRole playingRole, RequestByteMessage request) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void processProto(final PlayingRole hero, RequestProtoMessage request) throws Exception {

    }

    @Override
    public void processMy(final PlayingRole playingRole, MyRequestMessage request) throws Exception {
        int playerId = playingRole.getId();
        logger.info("manor field patrol,player={}", playerId);
        //今日次数上限
        int todayPatrolCount = PlayerManager.getInstance().getOtherCount(playerId, ManorConstants.MANOR_PATROL_COUNT_MARK);
        if (todayPatrolCount >= ManorConstants.MANOR_FIELD_PATROL_COST.length) {
            WsMessageHall.S2CErrorCode retMsg = new WsMessageHall.S2CErrorCode(WsMessageManor.S2CManorPatrol.msgCode, 1384);
            playingRole.writeAndFlush(retMsg.build(playingRole.alloc()));
            return;
        }
        //cost
        int costYb = ManorConstants.MANOR_FIELD_PATROL_COST[todayPatrolCount];
        if (playingRole.getPlayerBean().getMoney() < costYb) {
            WsMessageHall.S2CErrorCode retMsg = new WsMessageHall.S2CErrorCode(WsMessageManor.S2CManorPatrol.msgCode, 1385);
            playingRole.writeAndFlush(retMsg.build(playingRole.alloc()));
            return;
        }
        //do
        ManorBean manorBean = ManorCache.getInstance().getManor(playerId);
        ManorBean.DbManorField dbManorField = manorBean.getManorField();
        int manorLevel = manorBean.getLevel();
        dbManorField.getEnemys().clear();
        ManorTemplate.ManorHomeTemplate manorHomeTemplate = ManorTemplateCache.getInstance().getManorHomeTemplate(manorLevel);
        Integer fieldCount = manorHomeTemplate.getCOUNT();
        for (int i = 0; i < fieldCount; i++) {
            ManorBean.DbManorEnemy fieldEnemy1 = ManorManager.getInstance().createFieldEnemy1(i, manorLevel);
            dbManorField.getEnemys().add(fieldEnemy1);
        }
        Date now = new Date();
        dbManorField.setEnemyRefreshTime(DateUtils.addHours(now, ManorConstants.ENEMY_REFERSH_HOUR));
        ManorDaoHelper.asyncUpdateManor(manorBean);
        //mark
        AwardUtils.changeRes(playingRole, ManorConstants.MANOR_PATROL_COUNT_MARK, 1, LogConstants.MODULE_MANOR);
        //cost
        AwardUtils.changeRes(playingRole, GameConfig.PLAYER.YB, -costYb, LogConstants.MODULE_MANOR);
        //ret
        WsMessageManor.S2CManorPatrol respmsg = new WsMessageManor.S2CManorPatrol();
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
