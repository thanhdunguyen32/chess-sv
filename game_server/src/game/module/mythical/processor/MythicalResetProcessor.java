package game.module.mythical.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.award.bean.GameConfig;
import game.module.award.logic.AwardUtils;
import game.module.log.constants.LogConstants;
import game.module.mythical.bean.MythicalAnimal;
import game.module.mythical.dao.*;
import game.module.mythical.logic.MythicalConstants;
import game.module.mythical.logic.MythicalDaoHelper;
import game.module.template.MythicalClassTemplate;
import game.module.template.MythicalPassiveSkillTemplate;
import game.module.template.MythicalTemplate;
import game.module.template.RewardTemplateSimple;
import game.module.user.bean.PlayerProp;
import game.module.user.dao.PlayerOtherCache;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;
import ws.WsMessageHall;
import ws.WsMessageMythical;

import java.util.*;

/**
 * @author hexuhui
 */
@MsgCodeAnn(msgcode = WsMessageMythical.C2SMythicalReset.id, accessLimit = 200)
public class MythicalResetProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(MythicalResetProcessor.class);

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
        WsMessageMythical.C2SMythicalReset reqmsg = WsMessageMythical.C2SMythicalReset.parse(request);
        logger.info("mythical reset!player={},req={}", playerId, reqmsg);
        int mythicalId = reqmsg.mythical_id;
        //cost
        MythicalAnimal mythicalAnimal = MythicalAnimalCache.getInstance().getPlayerMythicalAnimal(playerId, mythicalId);
        if (mythicalAnimal == null) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageMythical.S2CMythicalReset.msgCode, 1201);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        Map<Integer, PlayerProp> playerOthers = PlayerOtherCache.getInstance().getPlayerOther(playerId);
        Integer resetCount = 0;
        if (playerOthers.containsKey(MythicalConstants.MYTHICAL_RESET_MARK)) {
            resetCount = playerOthers.get(MythicalConstants.MYTHICAL_RESET_MARK).getCount();
        }
        if (resetCount > 0 && playingRole.getPlayerBean().getMoney() < MythicalConstants.RESET_COST_MONEY) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageMythical.S2CMythicalReset.msgCode, 1209);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //do
        //level up back
        Map<Integer, Integer> backItemMap = new HashMap<>();
        List<List<RewardTemplateSimple>> templateAll = MythicalLvTemplateCache.getInstance().getTemplateAll();
        for (int i = mythicalAnimal.getLevel() - 1; i > 0; i--) {
            List<RewardTemplateSimple> rewardTemplateSimples = templateAll.get(i);
            for (RewardTemplateSimple rewardTemplateSimple : rewardTemplateSimples) {
                putRewards(backItemMap, rewardTemplateSimple);
            }
        }
        //class up back
        for (int i = mythicalAnimal.getPclass() - 1; i >= 0; i--) {
            MythicalClassTemplate mythicalClassTemplate = MythicalClassTemplateCache.getInstance().getMythicalClassTemplate(i);
            for (RewardTemplateSimple rewardTemplateSimple : mythicalClassTemplate.getITEMS()) {
                putRewards(backItemMap, rewardTemplateSimple);
            }
        }
        //pskill up back
        MythicalTemplate mythicalTemplate = MythicalTemplateCache.getInstance().getMythicalTemplate(mythicalId);
        List<Integer> passiveSkills = mythicalAnimal.getPassiveSkills();
        int pSkillIndex = 0;
        for (Integer passiveSkillLv : passiveSkills) {
            Integer pSkillId = mythicalTemplate.getPSKILL().get(pSkillIndex);
            if (passiveSkillLv > 1) {
                List<MythicalPassiveSkillTemplate> mythicalPassiveSkillTemplate =
                        MythicalPassiveSkillTemplateCache.getInstance().getMythicalPassiveSkillTemplate(pSkillId);
                for (int i = passiveSkillLv - 1; i > 0; i--) {
                    MythicalPassiveSkillTemplate mythicalPassiveSkillTemplate1 = mythicalPassiveSkillTemplate.get(i);
                    for (RewardTemplateSimple rewardTemplateSimple : mythicalPassiveSkillTemplate1.getCOST()) {
                        putRewards(backItemMap, rewardTemplateSimple);
                    }
                }
            }
            pSkillIndex++;
        }
        //all back
        List<WsMessageBase.IORewardItem> backRewards = new ArrayList<>();
        for (Map.Entry<Integer, Integer> aEntry : backItemMap.entrySet()) {
            AwardUtils.changeRes(playingRole, aEntry.getKey(), aEntry.getValue(), LogConstants.MODULE_MYTHICAL);
            backRewards.add(new WsMessageBase.IORewardItem(aEntry.getKey(), aEntry.getValue()));
        }
        //money cost
        if (resetCount > 0) {
            AwardUtils.changeRes(playingRole, GameConfig.PLAYER.YB, -MythicalConstants.RESET_COST_MONEY, LogConstants.MODULE_MYTHICAL);
        }
        //save bean
        mythicalAnimal.setPclass(1);
        mythicalAnimal.setLevel(1);
        passiveSkills = mythicalAnimal.getPassiveSkills();
        Collections.fill(passiveSkills, 0);
        passiveSkills.set(0, 1);
        MythicalDaoHelper.asyncUpdateMythicalAnimal(mythicalAnimal);
        //save mark
        AwardUtils.changeRes(playingRole, MythicalConstants.MYTHICAL_RESET_MARK, 1, LogConstants.MODULE_MYTHICAL);
        //ret
        WsMessageMythical.S2CMythicalReset respmsg = new WsMessageMythical.S2CMythicalReset();
        respmsg.mythicals = MythicalListProcessor.buildMythicalAnimalList(playerId);
        respmsg.rewards = backRewards;
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

    private void putRewards(Map<Integer, Integer> backItemMap, RewardTemplateSimple rewardTemplateSimple) {
        Integer gsid = rewardTemplateSimple.getGSID();
        Integer itemNum = rewardTemplateSimple.getCOUNT();
        if (backItemMap.containsKey(gsid)) {
            Integer existCount = backItemMap.get(gsid);
            backItemMap.put(gsid, existCount + itemNum);
        } else {
            backItemMap.put(gsid, itemNum);
        }
    }

}
