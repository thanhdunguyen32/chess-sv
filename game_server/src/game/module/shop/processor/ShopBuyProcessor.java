package game.module.shop.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.award.logic.AwardUtils;
import game.module.item.logic.ItemManager;
import game.module.log.constants.LogConstants;
import game.module.shop.bean.ShopBean;
import game.module.shop.dao.ShopCache;
import game.module.shop.dao.ShopTemplateCache;
import game.module.shop.logic.ShopDaoHelper;
import game.module.shop.logic.ShopManager;
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

import java.util.*;

/**
 * @author hexuhui
 */
@MsgCodeAnn(msgcode = WsMessageShop.C2SShopBuy.id, accessLimit = 200)
public class ShopBuyProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(ShopBuyProcessor.class);

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
        WsMessageShop.C2SShopBuy reqmsg = WsMessageShop.C2SShopBuy.parse(request);
        logger.info("shop buy!player={},req={}", playerId, reqmsg);
        String shop_type = reqmsg.shop_type;
        int item_index = reqmsg.item_index;
        switch (shop_type){
            case "day":
                buyDayShop(playingRole,shop_type,item_index);
                break;
            case "decomp":
                buyDecompShop(playingRole,shop_type,item_index);
                break;
            case "star":
                buyStarShop(playingRole,shop_type,item_index);
                break;
            case "legion":
                buyLegionShop(playingRole,shop_type,item_index);
                break;
            default:
                buyCommonShop(playingRole,shop_type,item_index);
        }
    }

    private void buyCommonShop(PlayingRole playingRole, String shop_type, int item_index) {
        int playerId = playingRole.getId();
        //是否存在
        List<ShopTemplate> dayTemplates = null;
        switch (shop_type){
            case "general":
                dayTemplates = ShopTemplateCache.getInstance().getGeneralShopTemplates();
                break;
            case "kill":
                dayTemplates = ShopTemplateCache.getInstance().getKillShopTemplates();
                break;
            case "offical":
                dayTemplates = ShopTemplateCache.getInstance().getOfficalShopTemplates();
                break;
            case "pvp":
                dayTemplates = ShopTemplateCache.getInstance().getPvpShopTemplates();
                break;
        }
        if (dayTemplates == null || item_index < 0 || item_index >= dayTemplates.size()) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageShop.S2CShopBuy.msgCode, 130);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        ShopTemplate shopTemplate = dayTemplates.get(item_index);
        //cost
        boolean itemEnough = ItemManager.getInstance().checkItemEnough(playingRole.getPlayerBean(), shopTemplate.getCOIN(), shopTemplate.getPRICE());
        if (!itemEnough) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageShop.S2CShopBuy.msgCode, 1312);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //do
        //cost
        AwardUtils.changeRes(playingRole, shopTemplate.getCOIN(), -shopTemplate.getPRICE(), LogConstants.MODULE_SHOP);
        //award
        AwardUtils.changeRes(playingRole, shopTemplate.getGSID(), shopTemplate.getCOUNT(), LogConstants.MODULE_SHOP);
        //ret
        WsMessageShop.S2CShopBuy respmsg = new WsMessageShop.S2CShopBuy();
        respmsg.awards = new ArrayList<>(1);
        respmsg.awards.add(new WsMessageBase.SimpleItemInfo(shopTemplate.getGSID(), shopTemplate.getCOUNT()));
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

    private void buyDecompShop(PlayingRole playingRole, String shop_type, int item_index) {
        int playerId = playingRole.getId();
        //是否存在
        List<ShopTemplate> decompTemplates = ShopTemplateCache.getInstance().getDecompTemplates();
        if (item_index < 0 || item_index >= decompTemplates.size()) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageShop.S2CShopBuy.msgCode, 130);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //是否买光
        ShopTemplate shopTemplate = decompTemplates.get(item_index);
        Date now = new Date();
        ShopBean shopBean = ShopCache.getInstance().getPlayerShopBean(playerId);
        if (shopBean != null && shopBean.getDecompBuyCount() != null) {
            if (shopBean.getDecompBuyCount().get(item_index) != null && shopBean.getDecompBuyCount().get(item_index) >= shopTemplate.getBUYTIME()) {
                WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageShop.S2CShopBuy.msgCode, 1311);
                playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
                return;
            }
        }
        //cost
        boolean itemEnough = ItemManager.getInstance().checkItemEnough(playingRole.getPlayerBean(), shopTemplate.getCOIN(), shopTemplate.getPRICE());
        if (!itemEnough) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageShop.S2CShopBuy.msgCode, 1312);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //do
        //cost
        AwardUtils.changeRes(playingRole, shopTemplate.getCOIN(), -shopTemplate.getPRICE(), LogConstants.MODULE_SHOP);
        //award
        AwardUtils.changeRes(playingRole, shopTemplate.getGSID(), shopTemplate.getCOUNT(), LogConstants.MODULE_SHOP);
        //save bean
        if (shopBean == null) {
            shopBean = ShopManager.getInstance().createShopBean(playerId, item_index,shop_type);
            ShopDaoHelper.asyncInsertShopBean(shopBean);
            ShopCache.getInstance().addShopBean(shopBean);
        } else {
            Map<Integer, Integer> decompBuyCount = shopBean.getDecompBuyCount();
            if(decompBuyCount == null){
                decompBuyCount = new HashMap<>();
                shopBean.setDecompBuyCount(decompBuyCount);
            }
            if (decompBuyCount.containsKey(item_index)) {
                decompBuyCount.put(item_index, decompBuyCount.get(item_index) + 1);
            } else {
                decompBuyCount.put(item_index, 1);
            }
            ShopDaoHelper.asyncUpdateShopBean(shopBean);
        }
        //ret
        WsMessageShop.S2CShopBuy respmsg = new WsMessageShop.S2CShopBuy();
        respmsg.awards = new ArrayList<>(1);
        respmsg.awards.add(new WsMessageBase.SimpleItemInfo(shopTemplate.getGSID(), shopTemplate.getCOUNT()));
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

    private void buyStarShop(PlayingRole playingRole, String shop_type, int item_index) {
        int playerId = playingRole.getId();
        //是否存在
        List<ShopTemplate> starTemplates = ShopTemplateCache.getInstance().getStarShopTemplates();
        if (item_index < 0 || item_index >= starTemplates.size()) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageShop.S2CShopBuy.msgCode, 130);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //是否买光
        ShopTemplate shopTemplate = starTemplates.get(item_index);
        Date now = new Date();
        ShopBean shopBean = ShopCache.getInstance().getPlayerShopBean(playerId);
        if (shopBean != null && shopBean.getStarBuyCount() != null) {
            if (shopBean.getStarBuyCount().get(item_index) != null && shopBean.getStarBuyCount().get(item_index) >= shopTemplate.getBUYTIME()) {
                WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageShop.S2CShopBuy.msgCode, 1311);
                playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
                return;
            }
        }
        //cost
        boolean itemEnough = ItemManager.getInstance().checkItemEnough(playingRole.getPlayerBean(), shopTemplate.getCOIN(), shopTemplate.getPRICE());
        if (!itemEnough) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageShop.S2CShopBuy.msgCode, 1312);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //do
        //cost
        AwardUtils.changeRes(playingRole, shopTemplate.getCOIN(), -shopTemplate.getPRICE(), LogConstants.MODULE_SHOP);
        //award
        AwardUtils.changeRes(playingRole, shopTemplate.getGSID(), shopTemplate.getCOUNT(), LogConstants.MODULE_SHOP);
        //save bean
        if (shopBean == null) {
            shopBean = ShopManager.getInstance().createShopBean(playerId, item_index,shop_type);
            ShopDaoHelper.asyncInsertShopBean(shopBean);
            ShopCache.getInstance().addShopBean(shopBean);
        } else {
            Map<Integer, Integer> starBuyCount = shopBean.getStarBuyCount();
            if(starBuyCount == null){
                starBuyCount = new HashMap<>();
                shopBean.setStarBuyCount(starBuyCount);
            }
            if (starBuyCount.containsKey(item_index)) {
                starBuyCount.put(item_index, starBuyCount.get(item_index) + 1);
            } else {
                starBuyCount.put(item_index, 1);
            }
            ShopDaoHelper.asyncUpdateShopBean(shopBean);
        }
        //ret
        WsMessageShop.S2CShopBuy respmsg = new WsMessageShop.S2CShopBuy();
        respmsg.awards = new ArrayList<>(1);
        respmsg.awards.add(new WsMessageBase.SimpleItemInfo(shopTemplate.getGSID(), shopTemplate.getCOUNT()));
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }
    
    private void buyLegionShop(PlayingRole playingRole, String shop_type, int item_index) {
        int playerId = playingRole.getId();
        //是否存在
        List<ShopTemplate> legionTemplates = ShopTemplateCache.getInstance().getLegionShopTemplates();
        if (item_index < 0 || item_index >= legionTemplates.size()) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageShop.S2CShopBuy.msgCode, 130);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //是否买光
        ShopTemplate shopTemplate = legionTemplates.get(item_index);
        Date now = new Date();
        ShopBean shopBean = ShopCache.getInstance().getPlayerShopBean(playerId);
        if (shopBean != null && shopBean.getLegionBuyCount() != null) {
            if (shopBean.getLegionBuyCount().get(item_index) != null && shopBean.getLegionBuyCount().get(item_index) >= shopTemplate.getBUYTIME()) {
                WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageShop.S2CShopBuy.msgCode, 1311);
                playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
                return;
            }
        }
        //cost
        boolean itemEnough = ItemManager.getInstance().checkItemEnough(playingRole.getPlayerBean(), shopTemplate.getCOIN(), shopTemplate.getPRICE());
        if (!itemEnough) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageShop.S2CShopBuy.msgCode, 1312);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //do
        //cost
        AwardUtils.changeRes(playingRole, shopTemplate.getCOIN(), -shopTemplate.getPRICE(), LogConstants.MODULE_SHOP);
        //award
        AwardUtils.changeRes(playingRole, shopTemplate.getGSID(), shopTemplate.getCOUNT(), LogConstants.MODULE_SHOP);
        //save bean
        if (shopBean == null) {
            shopBean = ShopManager.getInstance().createShopBean(playerId, item_index,shop_type);
            ShopDaoHelper.asyncInsertShopBean(shopBean);
            ShopCache.getInstance().addShopBean(shopBean);
        } else {
            Map<Integer, Integer> legionBuyCount = shopBean.getLegionBuyCount();
            if(legionBuyCount == null){
                legionBuyCount = new HashMap<>();
                shopBean.setLegionBuyCount(legionBuyCount);
            }
            if (legionBuyCount.containsKey(item_index)) {
                legionBuyCount.put(item_index, legionBuyCount.get(item_index) + 1);
            } else {
                legionBuyCount.put(item_index, 1);
            }
            ShopDaoHelper.asyncUpdateShopBean(shopBean);
        }
        //ret
        WsMessageShop.S2CShopBuy respmsg = new WsMessageShop.S2CShopBuy();
        respmsg.awards = new ArrayList<>(1);
        respmsg.awards.add(new WsMessageBase.SimpleItemInfo(shopTemplate.getGSID(), shopTemplate.getCOUNT()));
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

    private void buyDayShop(PlayingRole playingRole, String shop_type, int item_index) {
        int playerId = playingRole.getId();
        //是否存在
        List<ShopTemplate> dayTemplates = ShopTemplateCache.getInstance().getDayTemplate();
        if (item_index < 0 || item_index >= dayTemplates.size()) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageShop.S2CShopBuy.msgCode, 130);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //是否买光
        ShopTemplate shopTemplate = dayTemplates.get(item_index);
        Date now = new Date();
        ShopBean shopBean = ShopCache.getInstance().getPlayerShopBean(playerId);
        if (shopBean != null && DateUtils.isSameDay(shopBean.getLastDayBuyTime(), now)) {
            if (shopBean.getDayBuyCount().get(item_index) != null && shopBean.getDayBuyCount().get(item_index) >= shopTemplate.getBUYTIME()) {
                WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageShop.S2CShopBuy.msgCode, 1311);
                playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
                return;
            }
        }
        //cost
        boolean itemEnough = ItemManager.getInstance().checkItemEnough(playingRole.getPlayerBean(), shopTemplate.getCOIN(), shopTemplate.getPRICE());
        if (!itemEnough) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageShop.S2CShopBuy.msgCode, 1312);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //do
        //cost
        AwardUtils.changeRes(playingRole, shopTemplate.getCOIN(), -shopTemplate.getPRICE(), LogConstants.MODULE_SHOP);
        //award
        AwardUtils.changeRes(playingRole, shopTemplate.getGSID(), shopTemplate.getCOUNT(), LogConstants.MODULE_SHOP);
        //save bean
        if (shopBean == null) {
            shopBean = ShopManager.getInstance().createShopBean(playerId, item_index,shop_type);
            ShopDaoHelper.asyncInsertShopBean(shopBean);
            ShopCache.getInstance().addShopBean(shopBean);
        } else {
            Date lastDayBuyTime = shopBean.getLastDayBuyTime();
            if (!DateUtils.isSameDay(lastDayBuyTime, now)) {
                shopBean.setLastDayBuyTime(now);
                shopBean.getDayBuyCount().clear();
                shopBean.getDayBuyCount().put(item_index, 1);
            } else {
                Map<Integer, Integer> dayBuyCount = shopBean.getDayBuyCount();
                if(dayBuyCount == null){
                    dayBuyCount = new HashMap<>();
                    shopBean.setDayBuyCount(dayBuyCount);
                }
                if (dayBuyCount.containsKey(item_index)) {
                    dayBuyCount.put(item_index, dayBuyCount.get(item_index) + 1);
                } else {
                    dayBuyCount.put(item_index, 1);
                }
                shopBean.setLastDayBuyTime(now);
            }
            ShopDaoHelper.asyncUpdateShopBean(shopBean);
        }
        //ret
        WsMessageShop.S2CShopBuy respmsg = new WsMessageShop.S2CShopBuy();
        respmsg.awards = Collections.singletonList(new WsMessageBase.SimpleItemInfo(shopTemplate.getGSID(), shopTemplate.getCOUNT()));
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
