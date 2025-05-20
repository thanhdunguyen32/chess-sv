package game.module.hero.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.award.bean.GameConfig;
import game.module.award.logic.AwardUtils;
import game.module.hero.bean.GeneralBean;
import game.module.hero.dao.GeneralClassTemplateCache;
import game.module.hero.dao.GeneralLevelTemplateCache;
import game.module.hero.dao.GeneralCache;
import game.module.hero.dao.GeneralTemplateCache;
import game.module.hero.logic.GeneralDaoHelper;
import game.module.hero.logic.GeneralManager;
import game.module.item.bean.Item;
import game.module.item.dao.ItemCache;
import game.module.log.constants.LogConstants;
import game.module.mission.logic.MissionManager;
import game.module.template.GeneralClassTemplate;
import game.module.template.GeneralTemplate;
import game.module.template.RewardTemplateSimple;
import game.module.user.logic.ScrollAnnoManager;
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

@MsgCodeAnn(msgcode = WsMessageHero.C2SGeneralAddLevel.id, accessLimit = 100)
public class GeneralAddLevelProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(GeneralAddLevelProcessor.class);

    @Override
    public void processByte(PlayingRole playingRole, RequestByteMessage request) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void processProto(final PlayingRole hero, RequestProtoMessage request) throws Exception {

    }

    @Override
    public void processMy(final PlayingRole playingRole, MyRequestMessage request) throws Exception {
        WsMessageHero.C2SGeneralAddLevel reqmsg = WsMessageHero.C2SGeneralAddLevel.parse(request);
        int playerId = playingRole.getId();
        logger.info("general add level,player={},req={}", playerId, reqmsg);
        long general_uuid = reqmsg.general_uuid;
        //is exist
        Map<Long, GeneralBean> heroCache = GeneralCache.getInstance().getHeros(playerId);
        if (!heroCache.containsKey(general_uuid)) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageHero.S2CGeneralAddLevel.msgCode, 1301);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //is max level
        GeneralBean generalBean = heroCache.get(general_uuid);
        GeneralTemplate generalTemplate = GeneralTemplateCache.getInstance().getHeroTemplate(generalBean.getTemplateId());
        Integer pclass = generalBean.getPclass();
        GeneralClassTemplate generalClassTemplate = GeneralClassTemplateCache.getInstance().getConfigByLevel(pclass);
        if (generalBean.getLevel() >= generalClassTemplate.getMAXLV() + generalTemplate.getEXLV()) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageHero.S2CGeneralAddLevel.msgCode, 1302);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //cost
        Map<Integer, Item> itemCache = ItemCache.getInstance().getItemTemplateKey(playerId);
        List<RewardTemplateSimple> generalLevelsTemplates = GeneralLevelTemplateCache.getInstance().getConfigByLevel(generalBean.getLevel() - 1);
        for (RewardTemplateSimple generalLevelsTemplate : generalLevelsTemplates) {
            Integer gsid = generalLevelsTemplate.getGSID();
            int itemCount = generalLevelsTemplate.getCOUNT();
            if (gsid == GameConfig.PLAYER.GOLD) {
                if (playingRole.getPlayerBean().getGold() < itemCount) {
                    WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageHero.S2CGeneralAddLevel.msgCode, 1303);
                    playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
                    return;
                }
            } else if (!itemCache.containsKey(gsid) || itemCache.get(gsid).getCount() < itemCount) {
                WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageHero.S2CGeneralAddLevel.msgCode, 1304);
                playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
                return;
            }
        }
        //do
        //cost
        for (RewardTemplateSimple rewardTemplateSimple : generalLevelsTemplates) {
            Integer gsid = rewardTemplateSimple.getGSID();
            int itemCount = rewardTemplateSimple.getCOUNT();
            AwardUtils.changeRes(playingRole, gsid, -itemCount, LogConstants.MODULE_GENERAL);
        }
        //save bean
        generalBean.setLevel(generalBean.getLevel() + 1);
        GeneralManager.getInstance().updatePropertyAndPower(generalBean);
        GeneralDaoHelper.asyncUpdateHero(generalBean);
        //update mission progress
        MissionManager.getInstance().generalLevelup(playingRole,generalBean.getLevel());
        ScrollAnnoManager.getInstance().heroAddLevel(playingRole, playingRole.getPlayerBean().getName(), generalTemplate.getNAME(), generalBean.getLevel());
        //ret
        WsMessageHero.S2CGeneralAddLevel respmsg = new WsMessageHero.S2CGeneralAddLevel();
        respmsg.general_uuid = general_uuid;
        respmsg.general_bean = GeneralManager.getInstance().buildIoGeneral(generalBean);
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
