package game.module.kingpvp.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.chapter.bean.BattleFormation;
import game.module.chapter.dao.BattleFormationCache;
import game.module.chapter.dao.BattleFormationDaoHelper;
import game.module.chapter.logic.BattleFormationManager;
import game.module.kingpvp.logic.KingPvpManager;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageHall;
import ws.WsMessagePvp;

import java.util.*;

@MsgCodeAnn(msgcode = WsMessagePvp.C2SKpSwitchOrder.id, accessLimit = 250)
public class KpSwitchOrderProcessor extends PlayingRoleMsgProcessor {

    private static final Logger logger = LoggerFactory.getLogger(KpSwitchOrderProcessor.class);

    @Override
    public void processByte(PlayingRole hero, RequestByteMessage request) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void processProto(PlayingRole playingRole, RequestProtoMessage request) throws Exception {

    }

    @Override
    public void processMy(PlayingRole playingRole, MyRequestMessage request) throws Exception {
        int playerId = playingRole.getId();
        WsMessagePvp.C2SKpSwitchOrder reqmsg = WsMessagePvp.C2SKpSwitchOrder.parse(request);
        logger.info("king pvp switch order,player={},req={}", playerId, reqmsg);
        int[] teamorder = reqmsg.teamorder;
        //param check
        if (teamorder == null || teamorder.length <= 1) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessagePvp.S2CKpSwitchOrder.msgCode, 30);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        int kpOnlineNum = KingPvpManager.getInstance().getKpOnlineNum(playerId);
        Set<Integer> duplicateCheckSet = new HashSet<>();
        for (int aPos : teamorder) {
            //重复
            if (duplicateCheckSet.contains(aPos)) {
                WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessagePvp.S2CKpSwitchOrder.msgCode, 1473);
                playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
                return;
            }
            //过大
            if (aPos > kpOnlineNum || aPos <= 0) {
                WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessagePvp.S2CKpSwitchOrder.msgCode, 1474);
                playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
                return;
            }
            duplicateCheckSet.add(aPos);
        }
        //do
        BattleFormation battleFormation = BattleFormationCache.getInstance().getBattleFormation(playerId);
        List<Map<Integer, Long>> formationList = new ArrayList<>();
        for (int aPos : teamorder) {
            String kingpvpFormation = "kingpvp" + aPos;
            int formationIndex = ArrayUtils.indexOf(BattleFormationManager.FormationTypeNameMap, kingpvpFormation);
            Map<Integer, Long> kpFormationSet = BattleFormationManager.getInstance().getFormationByType(formationIndex, battleFormation);
            formationList.add(kpFormationSet);
        }
        int pos = 1;
        for (Map<Integer, Long> kpFormationSet : formationList) {
            String kingpvpFormation = "kingpvp" + pos;
            int formationIndex = ArrayUtils.indexOf(BattleFormationManager.FormationTypeNameMap, kingpvpFormation);
            BattleFormationManager.getInstance().setFormationByType(formationIndex, battleFormation, kpFormationSet);
            pos++;
        }
        //update db
        BattleFormationDaoHelper.asyncUpdateBattleFormation(battleFormation);
        //ret
        WsMessagePvp.S2CKpSwitchOrder respmsg = new WsMessagePvp.S2CKpSwitchOrder();
        //send
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
