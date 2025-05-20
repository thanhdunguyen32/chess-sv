package game.module.hero.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.activity.logic.ActivityWeekManager;
import game.module.award.logic.AwardUtils;
import game.module.hero.bean.GeneralBean;
import game.module.hero.bean.GeneralExchange;
import game.module.hero.dao.*;
import game.module.hero.logic.GeneralConstants;
import game.module.item.dao.RBoxTemplateCache;
import game.module.item.logic.ItemConstants;
import game.module.item.logic.ItemManager;
import game.module.log.constants.LogConstants;
import game.module.template.GeneralChipTemplate;
import game.module.template.GeneralTemplate;
import lion.common.MsgCodeAnn;
import lion.math.RandomDispatcher;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageHall;
import ws.WsMessageHero;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@MsgCodeAnn(msgcode = WsMessageHero.C2SGeneralExchangeRefresh.id, accessLimit = 200)
public class GeneralExchangeRefreshProcessor extends PlayingRoleMsgProcessor {

    private static final Logger logger = LoggerFactory.getLogger(GeneralExchangeRefreshProcessor.class);

    @Override
    public void processByte(PlayingRole playingRole, RequestByteMessage request) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void processProto(final PlayingRole hero, RequestProtoMessage request) throws Exception {

    }

    @Override
    public void processMy(final PlayingRole playingRole, MyRequestMessage request) throws Exception {
        WsMessageHero.C2SGeneralExchangeRefresh reqMsg = WsMessageHero.C2SGeneralExchangeRefresh.parse(request);
        int playerId = playingRole.getId();
        logger.info("general exchange refresh,player={},req={}", playerId, reqMsg);
        long general_uuid = reqMsg.general_uuid;
        //是否存在
        GeneralExchange generalExchange = GeneralExchangeCache.getInstance().getGeneralExchange(playerId);
        if (generalExchange != null) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageHero.S2CGeneralExchangeRefresh.msgCode, 1380);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //是否存在
        Map<Long, GeneralBean> heroCache = GeneralCache.getInstance().getHeros(playerId);
        if (!heroCache.containsKey(general_uuid)) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageHero.S2CGeneralExchangeRefresh.msgCode, 1381);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //只能置换4,5星，帝魔阵营不能置换
        GeneralBean generalBean = heroCache.get(general_uuid);
        GeneralTemplate generalTemplate = GeneralTemplateCache.getInstance().getHeroTemplate(generalBean.getTemplateId());
        Integer generalCamp = generalTemplate.getCAMP();
        if ((generalBean.getStar() != 4 && generalBean.getStar() != 5) || generalCamp == 5 || generalCamp == 6) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageHero.S2CGeneralExchangeRefresh.msgCode, 1382);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //cost
        if (!ItemManager.getInstance().checkItemEnough(playingRole.getPlayerBean(), ItemConstants.JIANG_HUN,
                ItemConstants.GENERAL_EXCHANGE_COST[generalBean.getStar() - 4])) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageHero.S2CGeneralExchangeRefresh.msgCode, 1383);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //do
        //cost
        AwardUtils.changeRes(playingRole, ItemConstants.JIANG_HUN, -ItemConstants.GENERAL_EXCHANGE_COST[generalBean.getStar() - 4],
                LogConstants.MODULE_GENERAL_EXCHANGE);
        //award
        int newGeneralTemplateId = 0;
        if (generalBean.getStar() == 4) {
            List<GeneralChipTemplate> star4TemplatesByCamp = GeneralChipTemplateCache.getInstance().getStar4TemplatesByCamp(generalCamp);
            int randIndex = RandomUtils.nextInt(0, star4TemplatesByCamp.size());
            GeneralChipTemplate generalChipTemplate = star4TemplatesByCamp.get(randIndex);
            newGeneralTemplateId = (Integer) generalChipTemplate.getGCOND();
        } else {//5星置换
            RandomDispatcher<Integer> rd = new RandomDispatcher<>();
            for (int i = 0; i < GeneralConstants.GENERAL_EXCHANGE_5STAR_RATE.length; i++) {
                rd.put(GeneralConstants.GENERAL_EXCHANGE_5STAR_RATE[i], i);
            }
            Integer randType = rd.random();
            List<Integer> camp5StarChips = new ArrayList<>();
            switch (randType) {
                case 0:
                    camp5StarChips = RBoxTemplateCache.getInstance().getCamp5StarNormalChips(generalCamp);
                    break;
                case 1:
                    camp5StarChips = RBoxTemplateCache.getInstance().getCamp5StarEliteChips(generalCamp);
                    break;
                case 2:
                    camp5StarChips = RBoxTemplateCache.getInstance().getCamp5StarLegendChips(generalCamp);
                    break;
            }
            int randIndex = RandomUtils.nextInt(0, camp5StarChips.size());
            int randChipId = camp5StarChips.get(randIndex);
            GeneralChipTemplate generalChipTemplate = GeneralChipTemplateCache.getInstance().getGeneralChipTemplate(randChipId);
            newGeneralTemplateId = (Integer) generalChipTemplate.getGCOND();
        }
        //save bean
        generalExchange = new GeneralExchange();
        generalExchange.setPlayerId(playerId);
        generalExchange.setOldGeneralUuid(general_uuid);
        generalExchange.setNewGeneralTemplateId(newGeneralTemplateId);
        GeneralExchangeDaoHelper.asyncInsertGeneralExchange(generalExchange);
        GeneralExchangeCache.getInstance().addGeneralExchange(generalExchange);
        //
        if (generalBean.getStar() == 5) {
            ActivityWeekManager.getInstance().jfbxGeneralExchange(playingRole);
        }
        //ret
        WsMessageHero.S2CGeneralExchangeRefresh respmsg = new WsMessageHero.S2CGeneralExchangeRefresh(general_uuid, newGeneralTemplateId);
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
