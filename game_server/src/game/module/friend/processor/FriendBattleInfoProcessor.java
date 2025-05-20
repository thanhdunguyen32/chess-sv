package game.module.friend.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.chapter.bean.BattleFormation;
import game.module.hero.bean.GeneralBean;
import game.module.hero.logic.GeneralManager;
import game.module.mythical.bean.MythicalAnimal;
import game.module.mythical.processor.MythicalListProcessor;
import game.module.offline.logic.PlayerOfflineManager;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;
import ws.WsMessageBattle;

import java.util.HashMap;
import java.util.Map;

@MsgCodeAnn(msgcode = WsMessageBattle.C2SFriendBattleInfo.id, accessLimit = 200)
public class FriendBattleInfoProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(FriendBattleInfoProcessor.class);

    @Override
    public void processByte(PlayingRole playingRole, RequestByteMessage request) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void processProto(PlayingRole playingRole, RequestProtoMessage request) throws Exception {

    }

    @Override
    public void processMy(PlayingRole playingRole, MyRequestMessage request) throws Exception {
        int playerId = playingRole.getId();
        WsMessageBattle.C2SFriendBattleInfo reqmsg = WsMessageBattle.C2SFriendBattleInfo.parse(request);
        logger.info("friend battle info,player={},req={}", playerId, reqmsg);
        //ret
        int friendid = reqmsg.friendid;
        WsMessageBattle.S2CFriendBattleInfo respmsg = new WsMessageBattle.S2CFriendBattleInfo();
        Map<Long, GeneralBean> generalAll = PlayerOfflineManager.getInstance().getGeneralAll(friendid);
        BattleFormation battleFormation = PlayerOfflineManager.getInstance().getBattleFormation(friendid);
        if (battleFormation != null) {
            int normalFormationType = 4;
            if (battleFormation.getMythics() != null && battleFormation.getMythics().containsKey(normalFormationType)) {
                WsMessageBase.IOMythicalAnimal ioMythicalAnimal = new WsMessageBase.IOMythicalAnimal();
                respmsg.mythic = ioMythicalAnimal;
                Map<Integer, MythicalAnimal> mythicalAll = PlayerOfflineManager.getInstance().getMythicalAll(friendid);
                int mythicalId = battleFormation.getMythics().get(normalFormationType);
                MythicalAnimal mythicalAnimal = mythicalAll.get(mythicalId);
                MythicalListProcessor.buildIOMythicalAnimal(mythicalAnimal, mythicalId, ioMythicalAnimal);
            }
            if (battleFormation.getPvpdef() != null) {
                respmsg.team = new HashMap<>();
                for (Map.Entry<Integer, Long> aEntry : battleFormation.getNormal().entrySet()) {
                    GeneralBean generalBean = generalAll.get(aEntry.getValue());
                    if(generalBean == null){
                        continue;
                    }
                    WsMessageBase.IOGeneralBean ioGeneralBean = GeneralManager.getInstance().buildIoGeneral(generalBean);
                    Integer formationPos = aEntry.getKey();
                    respmsg.team.put(formationPos, ioGeneralBean);
                }
            }
        }
        //ret
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
