package game.module.mythical.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.award.bean.GameConfig;
import game.module.award.logic.AwardUtils;
import game.module.item.bean.Item;
import game.module.item.dao.ItemCache;
import game.module.item.logic.ItemManager;
import game.module.log.constants.LogConstants;
import game.module.mythical.bean.MythicalAnimal;
import game.module.mythical.dao.MythicalAnimalCache;
import game.module.mythical.dao.MythicalClassTemplateCache;
import game.module.mythical.dao.MythicalPassiveSkillTemplateCache;
import game.module.mythical.dao.MythicalTemplateCache;
import game.module.mythical.logic.MythicalDaoHelper;
import game.module.mythical.logic.MythicalManager;
import game.module.template.MythicalClassTemplate;
import game.module.template.MythicalPassiveSkillTemplate;
import game.module.template.MythicalTemplate;
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
@MsgCodeAnn(msgcode = WsMessageMythical.C2SMythicalPskillUp.id, accessLimit = 200)
public class MythicalPskillUpProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(MythicalPskillUpProcessor.class);

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
        WsMessageMythical.C2SMythicalPskillUp reqmsg = WsMessageMythical.C2SMythicalPskillUp.parse(request);
        logger.info("mythical pskill up!player={},msg={}", playerId, reqmsg);
        int mythicalId = reqmsg.mythical_id;
        int pskill_index = reqmsg.pskill_index;
        //is unlock
        MythicalAnimal mythicalAnimal = MythicalAnimalCache.getInstance().getPlayerMythicalAnimal(playerId, mythicalId);
        if (mythicalAnimal == null) {
            mythicalAnimal = MythicalManager.getInstance().createMythicalAnimal(playerId, mythicalId);
        }
        MythicalTemplate mythicalTemplate = MythicalTemplateCache.getInstance().getMythicalTemplate(mythicalId);
        List<Integer> passiveSkills = mythicalAnimal.getPassiveSkills();
        if (pskill_index >= passiveSkills.size()) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageMythical.S2CMythicalPskillUp.msgCode, 1207);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //max
        MythicalClassTemplate mythicalClassTemplate = MythicalClassTemplateCache.getInstance().getMythicalClassTemplate(mythicalAnimal.getPclass() - 1);
        Integer pSkillMaxLv = mythicalClassTemplate.getPSK().get(pskill_index);
        Integer pSkillLevel = passiveSkills.get(pskill_index);
        if (pSkillLevel >= pSkillMaxLv) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageMythical.S2CMythicalPskillUp.msgCode, 1208);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //cost
        int pskillId = mythicalTemplate.getPSKILL().get(pskill_index);
        List<MythicalPassiveSkillTemplate> mythicalPassiveSkillTemplateList =
                MythicalPassiveSkillTemplateCache.getInstance().getMythicalPassiveSkillTemplate(pskillId);
        List<RewardTemplateSimple> costItemsTemplate = mythicalPassiveSkillTemplateList.get(pSkillLevel).getCOST();
        for (RewardTemplateSimple rewardTemplateSimple : costItemsTemplate) {
            Integer gsid = rewardTemplateSimple.getGSID();
            int itemCount = rewardTemplateSimple.getCOUNT();
            if (!ItemManager.getInstance().checkItemEnough(playingRole.getPlayerBean(), gsid,itemCount)) {
                WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageMythical.S2CMythicalPskillUp.msgCode, 1204);
                playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
                return;
            }
        }
        //do
        //cost
        for (RewardTemplateSimple rewardTemplateSimple : costItemsTemplate) {
            Integer gsid = rewardTemplateSimple.getGSID();
            int itemCount = rewardTemplateSimple.getCOUNT();
            AwardUtils.changeRes(playingRole, gsid, -itemCount, LogConstants.MODULE_MYTHICAL);
        }
        //save bean
        passiveSkills.set(pskill_index, ++pSkillLevel);
        if(mythicalAnimal.getId() == null){
            MythicalDaoHelper.asyncInsertMythicalAnimal(mythicalAnimal);
            MythicalAnimalCache.getInstance().addMythicalAnimal(mythicalAnimal);
        }else {
            MythicalDaoHelper.asyncUpdateMythicalAnimal(mythicalAnimal);
        }
        //ret
        WsMessageMythical.S2CMythicalPskillUp respmsg = new WsMessageMythical.S2CMythicalPskillUp();
        respmsg.mythicals = MythicalListProcessor.buildMythicalAnimalList(playerId);
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
