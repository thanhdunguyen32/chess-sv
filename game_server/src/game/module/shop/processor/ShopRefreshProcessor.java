package game.module.shop.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.award.logic.AwardUtils;
import game.module.item.logic.ItemManager;
import game.module.log.constants.LogConstants;
import game.module.shop.bean.ShopBean;
import game.module.shop.dao.ShopCache;
import game.module.shop.dao.ShopRefreshTemplateCache;
import game.module.shop.dao.ShopTemplateCache;
import game.module.shop.logic.ShopDaoHelper;
import game.module.template.ShopTemplate;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;
import ws.WsMessageHall;
import ws.WsMessageShop;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author hexuhui
 */
@MsgCodeAnn(msgcode = WsMessageShop.C2SShopRefresh.id, accessLimit = 200)
public class ShopRefreshProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(ShopRefreshProcessor.class);

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
        WsMessageShop.C2SShopRefresh reqmsg = WsMessageShop.C2SShopRefresh.parse(request);
        logger.info("shop refresh!player={},req={}", playerId, reqmsg);
        String shop_type = reqmsg.shop_type;
        //not exist
        boolean containsShop = ShopRefreshTemplateCache.getInstance().containsShop(shop_type);
        if (!containsShop) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageShop.S2CShopRefresh.msgCode, 1315);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //cost
        List<Map<String, Integer>> shopRefreshCosts = ShopRefreshTemplateCache.getInstance().getShopRefreshCosts(shop_type);
        for (Map<String, Integer> aCostPair : shopRefreshCosts) {
            Integer gsid = aCostPair.get("GSID");
            Integer count = aCostPair.get("COUNT");
            if (!ItemManager.getInstance().checkItemEnough(playingRole.getPlayerBean(), gsid, count)) {
                WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageShop.S2CShopRefresh.msgCode, 1316);
                playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
                return;
            }
        }
        //do
        ShopBean shopBean = ShopCache.getInstance().getPlayerShopBean(playerId);
        if (shopBean != null) {
            switch (shop_type) {
                case "star":
                    shopBean.setStarBuyCount(null);
                    break;
                case "decomp":
                    shopBean.setDecompBuyCount(null);
                    break;
                case "legion":
                    shopBean.setLegionBuyCount(null);
                    break;
            }
            ShopDaoHelper.asyncUpdateShopBean(shopBean);
        }
        //cost
        for (Map<String, Integer> aCostPair : shopRefreshCosts) {
            Integer gsid = aCostPair.get("GSID");
            Integer count = aCostPair.get("COUNT");
            AwardUtils.changeRes(playingRole, gsid, -count, LogConstants.MODULE_SHOP);
        }
        //ret
        WsMessageShop.S2CShopRefresh respmsg = new WsMessageShop.S2CShopRefresh();
        respmsg.shop_type = shop_type;
        List<ShopTemplate> shopTemplates = null;
        switch (shop_type) {
            case "day":
                shopTemplates = ShopTemplateCache.getInstance().getDayTemplate();
                break;
            case "star":
                shopTemplates = ShopTemplateCache.getInstance().getStarShopTemplates();
                break;
            case "decomp":
                shopTemplates = ShopTemplateCache.getInstance().getDecompTemplates();
                break;
            case "legion":
                shopTemplates = ShopTemplateCache.getInstance().getLegionShopTemplates();
                break;
            default:
                break;
        }
        respmsg.item_list = new ArrayList<>(shopTemplates.size());
        for (ShopTemplate shopTemplate : shopTemplates) {
            WsMessageBase.IOShopItem shopItem = new WsMessageBase.IOShopItem(shopTemplate.getGSID(), shopTemplate.getCOUNT(), shopTemplate.getBUYTIME()
                    , shopTemplate.getCOIN(), shopTemplate.getPRICE(), shopTemplate.getDISCOUNT());
            respmsg.item_list.add(shopItem);
        }
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
