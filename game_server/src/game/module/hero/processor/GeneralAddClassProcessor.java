package game.module.hero.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.award.bean.GameConfig;
import game.module.award.logic.AwardUtils;
import game.module.hero.bean.GeneralBean;
import game.module.hero.dao.GeneralClassTemplateCache;
import game.module.hero.dao.GeneralTemplateCache;
import game.module.hero.dao.GeneralCache;
import game.module.hero.logic.GeneralDaoHelper;
import game.module.hero.logic.GeneralManager;
import game.module.item.bean.Item;
import game.module.item.dao.ItemCache;
import game.module.log.constants.LogConstants;
import game.module.template.GeneralClassTemplate;
import game.module.template.GeneralTemplate;
import game.module.template.RewardTemplateSimple;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageHall;
import ws.WsMessageHero;

import java.util.List;
import java.util.Map;

@MsgCodeAnn(msgcode = WsMessageHero.C2SGeneralAddClass.id, accessLimit = 200)
public class GeneralAddClassProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(GeneralAddClassProcessor.class);

    @Override
    public void processByte(PlayingRole playingRole, RequestByteMessage request) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void processProto(final PlayingRole hero, RequestProtoMessage request) throws Exception {

    }

    @Override
    public void processMy(final PlayingRole playingRole, MyRequestMessage request) throws Exception {
        WsMessageHero.C2SGeneralAddClass reqmsg = WsMessageHero.C2SGeneralAddClass.parse(request);
        int playerId = playingRole.getId();
        logger.info("general add class,player={},req={}", playerId, reqmsg);
        long generalUuid = reqmsg.general_uuid;
        //is exist
        Map<Long, GeneralBean> heroCache = GeneralCache.getInstance().getHeros(playerId);
        if (!heroCache.containsKey(generalUuid)) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageHero.S2CGeneralAddClass.msgCode, 1301);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //is max level
        GeneralBean generalBean = heroCache.get(generalUuid);
        GeneralTemplate generalTemplate = GeneralTemplateCache.getInstance().getHeroTemplate(generalBean.getTemplateId());
        Integer pclass = generalBean.getPclass();
        GeneralClassTemplate generalClassTemplate = GeneralClassTemplateCache.getInstance().getConfigByLevel(pclass);
        if (generalBean.getLevel() < generalClassTemplate.getMAXLV() + generalTemplate.getEXLV()) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageHero.S2CGeneralAddClass.msgCode, 1305);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //is max class
        if (pclass >= generalTemplate.getMAXCLASS()) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageHero.S2CGeneralAddClass.msgCode, 1306);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //cost
        Map<Integer, Item> itemCache = ItemCache.getInstance().getItemTemplateKey(playerId);
        generalClassTemplate = GeneralClassTemplateCache.getInstance().getConfigByLevel(pclass + 1);
        List<RewardTemplateSimple> generalClassTemplateITEMS = generalClassTemplate.getITEMS();
        for (RewardTemplateSimple rewardTemplateSimple : generalClassTemplateITEMS) {
            Integer gsid = rewardTemplateSimple.getGSID();
            int itemCount = rewardTemplateSimple.getCOUNT();
            if (gsid == GameConfig.PLAYER.GOLD) {
                if (playingRole.getPlayerBean().getGold() < itemCount) {
                    WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageHero.S2CGeneralAddClass.msgCode, 1303);
                    playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
                    return;
                }
            } else if (!itemCache.containsKey(gsid) || itemCache.get(gsid).getCount() < itemCount) {
                WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageHero.S2CGeneralAddClass.msgCode, 1304);
                playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
                return;
            }
        }
        //do
        //cost
        for (RewardTemplateSimple rewardTemplateSimple : generalClassTemplateITEMS) {
            Integer gsid = rewardTemplateSimple.getGSID();
            int itemCount = rewardTemplateSimple.getCOUNT();
            AwardUtils.changeRes(playingRole, gsid, -itemCount, LogConstants.MODULE_GENERAL);
        }
        //add skill
        Integer unlockSkillId = generalClassTemplate.getUNLOCK();
        List<Integer> skilllist = generalBean.getSkill();
        if (unlockSkillId > skilllist.size() && unlockSkillId <= generalTemplate.getSKILL().size()) {
            skilllist.add(generalTemplate.getSKILL().get(unlockSkillId - 1));
        }
        //save bean
        generalBean.setPclass(pclass + 1);
        GeneralManager.getInstance().updatePropertyAndPower(generalBean);
        GeneralDaoHelper.asyncUpdateHero(generalBean);
        //ret
        WsMessageHero.S2CGeneralAddClass respmsg = new WsMessageHero.S2CGeneralAddClass();
        respmsg.general_uuid = generalUuid;
        respmsg.general_bean = GeneralManager.getInstance().buildIoGeneral(generalBean);
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
