package game.module.hero.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.award.logic.AwardUtils;
import game.module.hero.bean.GeneralBean;
import game.module.hero.dao.GeneralCache;
import game.module.hero.dao.TalentCostTemplateCache;
import game.module.hero.dao.TalentInfoTemplateCache;
import game.module.hero.logic.GeneralConstants;
import game.module.hero.logic.GeneralDaoHelper;
import game.module.hero.logic.GeneralManager;
import game.module.item.logic.ItemManager;
import game.module.log.constants.LogConstants;
import game.module.template.RewardTemplateSimple;
import game.module.user.logic.PlayerManager;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageHall;
import ws.WsMessageHero;

import java.util.Map;

@MsgCodeAnn(msgcode = WsMessageHero.C2SGeneralWuhun.id, accessLimit = 100)
public class GeneralWuhunProcessor extends PlayingRoleMsgProcessor {

    private static final Logger logger = LoggerFactory.getLogger(GeneralWuhunProcessor.class);

    @Override
    public void processByte(PlayingRole playingRole, RequestByteMessage request) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void processProto(final PlayingRole hero, RequestProtoMessage request) throws Exception {

    }

    @Override
    public void processMy(final PlayingRole playingRole, MyRequestMessage request) throws Exception {
        WsMessageHero.C2SGeneralWuhun reqMsg = WsMessageHero.C2SGeneralWuhun.parse(request);
        int playerId = playingRole.getId();
        logger.info("general wuhun,player={},req={}", playerId, reqMsg);
        int pos_index = reqMsg.pos_index;
        int wuhun_id = reqMsg.wuhun_id;
        long general_uuid = reqMsg.general_uuid;
        //是否存在
        Map<Long, GeneralBean> heroCache = GeneralCache.getInstance().getHeros(playerId);
        if (!heroCache.containsKey(general_uuid)) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageHero.S2CGeneralWuhun.msgCode, 1301);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //check pos unlock
        int needStar = pos_index + 10;
        GeneralBean generalBean = heroCache.get(general_uuid);
        if (generalBean.getStar() < needStar) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageHero.S2CGeneralWuhun.msgCode, 1343);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //is wuhun unlock
        int talentInfoLockStar = TalentInfoTemplateCache.getInstance().getTalentInfoLock(wuhun_id);
        if (generalBean.getStar() < talentInfoLockStar) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageHero.S2CGeneralWuhun.msgCode, 1344);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //cost
        int wuhunCount = PlayerManager.getInstance().getOtherCount(playerId, GeneralConstants.WU_HUN_COUNT_MARK);
        RewardTemplateSimple talentCost = TalentCostTemplateCache.getInstance().getTalentCost(wuhunCount);
        if (!ItemManager.getInstance().checkItemEnough(playingRole.getPlayerBean(), talentCost.getGSID(), talentCost.getCOUNT())) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageHero.S2CGeneralWuhun.msgCode, 1345);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //do
        generalBean.getTalent().set(pos_index, wuhun_id);
        GeneralManager.getInstance().updatePropertyAndPower(generalBean);
        GeneralDaoHelper.asyncUpdateHero(generalBean);
        //cost
        AwardUtils.changeRes(playingRole, talentCost.getGSID(), -talentCost.getCOUNT(), LogConstants.MODULE_GENERAL);
        AwardUtils.changeRes(playingRole, GeneralConstants.WU_HUN_COUNT_MARK, 1, LogConstants.MODULE_GENERAL);
        //ret
        WsMessageHero.S2CGeneralWuhun respmsg = new WsMessageHero.S2CGeneralWuhun();
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
