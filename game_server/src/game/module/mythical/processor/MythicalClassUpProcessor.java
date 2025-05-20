package game.module.mythical.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.award.bean.GameConfig;
import game.module.award.logic.AwardUtils;
import game.module.item.bean.Item;
import game.module.item.dao.ItemCache;
import game.module.log.constants.LogConstants;
import game.module.mythical.bean.MythicalAnimal;
import game.module.mythical.dao.MythicalAnimalCache;
import game.module.mythical.dao.MythicalClassTemplateCache;
import game.module.mythical.logic.MythicalDaoHelper;
import game.module.template.MythicalClassTemplate;
import game.module.template.RewardTemplateSimple;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageHall;
import ws.WsMessageMythical;

import java.util.List;
import java.util.Map;

/**
 * @author hexuhui
 */
@MsgCodeAnn(msgcode = WsMessageMythical.C2SMythicalClassUp.id, accessLimit = 200)
public class MythicalClassUpProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(MythicalClassUpProcessor.class);

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
        WsMessageMythical.C2SMythicalClassUp reqmsg = WsMessageMythical.C2SMythicalClassUp.parse(request);
        logger.info("mythical class up!player={},msg={}", playerId, reqmsg);
        int mythicalId = reqmsg.mythical_id;
        //exist
        MythicalAnimal mythicalAnimal = MythicalAnimalCache.getInstance().getPlayerMythicalAnimal(playerId, mythicalId);
        if (mythicalAnimal == null) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageMythical.S2CMythicalClassUp.msgCode, 1201);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //max
        Integer pclass = mythicalAnimal.getPclass();
        if (pclass >= MythicalClassTemplateCache.getInstance().getClassSize()) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageMythical.S2CMythicalClassUp.msgCode, 1205);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //is level max
        MythicalClassTemplate mythicalClassTemplate = MythicalClassTemplateCache.getInstance().getMythicalClassTemplate(pclass - 1);
        if (mythicalAnimal.getLevel() < mythicalClassTemplate.getMAXLV()) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageMythical.S2CMythicalClassUp.msgCode, 1202);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //cost
        Map<Integer, Item> itemCache = ItemCache.getInstance().getItemTemplateKey(playerId);
        mythicalClassTemplate = MythicalClassTemplateCache.getInstance().getMythicalClassTemplate(pclass);
        List<RewardTemplateSimple> mythicalClassTemplateITEMS = mythicalClassTemplate.getITEMS();
        for (RewardTemplateSimple rewardTemplateSimple : mythicalClassTemplateITEMS) {
            Integer gsid = rewardTemplateSimple.getGSID();
            int itemCount = rewardTemplateSimple.getCOUNT();
            if (gsid == GameConfig.PLAYER.GOLD) {
                if (playingRole.getPlayerBean().getGold() < itemCount) {
                    WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageMythical.S2CMythicalClassUp.msgCode, 1203);
                    playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
                    return;
                }
            } else if (!itemCache.containsKey(gsid) || itemCache.get(gsid).getCount() < itemCount) {
                WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageMythical.S2CMythicalClassUp.msgCode, 1204);
                playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
                return;
            }
        }
        //do
        //cost
        for (RewardTemplateSimple rewardTemplateSimple : mythicalClassTemplateITEMS) {
            Integer gsid = rewardTemplateSimple.getGSID();
            int itemCount = rewardTemplateSimple.getCOUNT();
            AwardUtils.changeRes(playingRole, gsid, -itemCount, LogConstants.MODULE_MYTHICAL);
        }
        //save bean
        mythicalAnimal.setPclass(++pclass);
        mythicalAnimal.getPassiveSkills().set(pclass - 1, 1);
        MythicalDaoHelper.asyncUpdateMythicalAnimal(mythicalAnimal);
        //ret
        WsMessageMythical.S2CMythicalClassUp respmsg = new WsMessageMythical.S2CMythicalClassUp();
        respmsg.mythicals = MythicalListProcessor.buildMythicalAnimalList(playerId);
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
