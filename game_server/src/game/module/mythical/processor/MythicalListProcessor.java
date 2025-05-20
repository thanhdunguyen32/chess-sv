package game.module.mythical.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.mythical.bean.MythicalAnimal;
import game.module.mythical.dao.MythicalAnimalCache;
import game.module.mythical.dao.MythicalTemplateCache;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;
import ws.WsMessageMythical;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * @author hexuhui
 */
@MsgCodeAnn(msgcode = WsMessageMythical.C2SMythicalList.id, accessLimit = 200)
public class MythicalListProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(MythicalListProcessor.class);

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
        sendResponse(playingRole);
    }

    public static void sendResponse(PlayingRole playingRole) {
        logger.info("mythical list!");
        int playerId = playingRole.getId();
        WsMessageMythical.S2CMythicalList respmsg = new WsMessageMythical.S2CMythicalList();
        respmsg.mythical_list = buildMythicalAnimalList(playerId);
        //ret
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

    public static final List<WsMessageBase.IOMythicalAnimal> buildMythicalAnimalList(int playerId){
        Set<Integer> mythicalTemplateIds = MythicalTemplateCache.getInstance().getMythicalTemplateIds();
        List<WsMessageBase.IOMythicalAnimal> retlist = new ArrayList<>(mythicalTemplateIds.size());
        for (int aTemplateId : mythicalTemplateIds) {
            MythicalAnimal mythicalAnimal = MythicalAnimalCache.getInstance().getPlayerMythicalAnimal(playerId, aTemplateId);
            if (mythicalAnimal == null) {
                WsMessageBase.IOMythicalAnimal ioMythicalAnimal = new WsMessageBase.IOMythicalAnimal();
                ioMythicalAnimal.tid = aTemplateId;
                ioMythicalAnimal.level = 1;
                ioMythicalAnimal.pclass = 1;
                ioMythicalAnimal.pskill = Arrays.asList(1, 0, 0, 0);
                retlist.add(ioMythicalAnimal);
            } else {
                WsMessageBase.IOMythicalAnimal ioMythicalAnimal = new WsMessageBase.IOMythicalAnimal();
                buildIOMythicalAnimal(mythicalAnimal, aTemplateId, ioMythicalAnimal);
                retlist.add(ioMythicalAnimal);
            }
        }
        return retlist;
    }

    public static void buildIOMythicalAnimal(MythicalAnimal mythicalAnimal,int gsid, WsMessageBase.IOMythicalAnimal ioMythicalAnimal){
        if(mythicalAnimal != null) {
            ioMythicalAnimal.tid = mythicalAnimal.getTemplateId();
            ioMythicalAnimal.level = mythicalAnimal.getLevel();
            ioMythicalAnimal.pclass = mythicalAnimal.getPclass();
            ioMythicalAnimal.pskill = mythicalAnimal.getPassiveSkills();
        }else {
            ioMythicalAnimal.tid = gsid;
            ioMythicalAnimal.level = 1;
            ioMythicalAnimal.pclass = 1;
            ioMythicalAnimal.pskill = Arrays.asList(1, 0, 0, 0);
        }
    }

}
