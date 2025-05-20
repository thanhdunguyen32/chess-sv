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
import game.module.mythical.dao.MythicalLvTemplateCache;
import game.module.mythical.dao.MythicalTemplateCache;
import game.module.mythical.logic.MythicalDaoHelper;
import game.module.mythical.logic.MythicalManager;
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
@MsgCodeAnn(msgcode = WsMessageMythical.C2SMythicalLevelUp.id, accessLimit = 200)
public class MythicalLevelUpProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(MythicalLevelUpProcessor.class);

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
        WsMessageMythical.C2SMythicalLevelUp reqmsg = WsMessageMythical.C2SMythicalLevelUp.parse(request);
        logger.info("mythical levelup!player={},req={}", playerId, reqmsg);
        //exist
        int mythicalId = reqmsg.mythical_id;
        if (!MythicalTemplateCache.getInstance().containsTemplate(mythicalId)) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageMythical.S2CMythicalLevelUp.msgCode, 1201);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //升级满了
        MythicalAnimal mythicalAnimal = MythicalAnimalCache.getInstance().getPlayerMythicalAnimal(playerId, mythicalId);
        if (mythicalAnimal != null) {
            Integer pclass = mythicalAnimal.getPclass();
            MythicalClassTemplate mythicalClassTemplate = MythicalClassTemplateCache.getInstance().getMythicalClassTemplate(pclass - 1);
            if (mythicalAnimal.getLevel() >= mythicalClassTemplate.getMAXLV()) {
                WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageMythical.S2CMythicalLevelUp.msgCode, 1202);
                playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
                return;
            }
        }
        //money enough
        int currentLevel = 0;
        if (mythicalAnimal != null) {
            currentLevel = mythicalAnimal.getLevel();
        }
        Map<Integer, Item> itemCache = ItemCache.getInstance().getItemTemplateKey(playerId);
        List<RewardTemplateSimple> mythicalLvTemplate = MythicalLvTemplateCache.getInstance().getMythicalLvTemplate(currentLevel);
        for (RewardTemplateSimple rewardTemplateSimple : mythicalLvTemplate) {
            Integer gsid = rewardTemplateSimple.getGSID();
            int itemCount = rewardTemplateSimple.getCOUNT();
            if (gsid == GameConfig.PLAYER.GOLD) {
                if (playingRole.getPlayerBean().getGold() < itemCount) {
                    WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageMythical.S2CMythicalLevelUp.msgCode, 1203);
                    playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
                    return;
                }
            } else if (!itemCache.containsKey(gsid) || itemCache.get(gsid).getCount() < itemCount) {
                WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageMythical.S2CMythicalLevelUp.msgCode, 1204);
                playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
                return;
            }
        }
        //do
        //cost
        for (RewardTemplateSimple rewardTemplateSimple : mythicalLvTemplate) {
            Integer gsid = rewardTemplateSimple.getGSID();
            int itemCount = rewardTemplateSimple.getCOUNT();
            AwardUtils.changeRes(playingRole, gsid, -itemCount, LogConstants.MODULE_MYTHICAL);
        }
        //save bean
        if (mythicalAnimal == null) {
            mythicalAnimal = MythicalManager.getInstance().createMythicalAnimal(playerId, mythicalId);
            mythicalAnimal.setLevel(2);
            MythicalDaoHelper.asyncInsertMythicalAnimal(mythicalAnimal);
            MythicalAnimalCache.getInstance().addMythicalAnimal(mythicalAnimal);
        } else {
            mythicalAnimal = MythicalAnimalCache.getInstance().getPlayerMythicalAnimal(playerId, mythicalId);
            mythicalAnimal.setLevel(mythicalAnimal.getLevel() + 1);
            MythicalDaoHelper.asyncUpdateMythicalAnimal(mythicalAnimal);
        }
        //ret
        WsMessageMythical.S2CMythicalLevelUp respmsg = new WsMessageMythical.S2CMythicalLevelUp();
        respmsg.mythicals = MythicalListProcessor.buildMythicalAnimalList(playerId);
        //send
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
