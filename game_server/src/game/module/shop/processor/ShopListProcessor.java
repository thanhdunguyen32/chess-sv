package game.module.shop.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.shop.bean.ShopBean;
import game.module.shop.dao.ShopCache;
import game.module.shop.dao.ShopTemplateCache;
import game.module.template.ShopTemplate;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;
import ws.WsMessageShop;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author hexuhui
 */
@MsgCodeAnn(msgcode = WsMessageShop.C2SShopList.id, accessLimit = 200)
public class ShopListProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(ShopListProcessor.class);

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
        WsMessageShop.C2SShopList reqmsg = WsMessageShop.C2SShopList.parse(request);
        logger.info("shop list!,player={},req={}",playerId, reqmsg);
        String shop_type = reqmsg.shop_type;
        WsMessageShop.S2CShopList respmsg = new WsMessageShop.S2CShopList();
        switch (shop_type) {
            case "day":
                ShopBean shopBean = ShopCache.getInstance().getPlayerShopBean(playerId);
                List<ShopTemplate> dayTemplates = ShopTemplateCache.getInstance().getDayTemplate();
                respmsg.item_list = new ArrayList<>(dayTemplates.size());
                int i = 0;
                for (ShopTemplate shopTemplate : dayTemplates) {
                    WsMessageBase.IOShopItem shopItem = new WsMessageBase.IOShopItem(shopTemplate.getGSID(), shopTemplate.getCOUNT(), shopTemplate.getBUYTIME()
                            , shopTemplate.getCOIN(), shopTemplate.getPRICE(), shopTemplate.getDISCOUNT());
                    respmsg.item_list.add(shopItem);
                    if (shopBean != null && shopBean.getDayBuyCount() != null && shopBean.getDayBuyCount().get(i) != null && DateUtils.isSameDay(shopBean.getLastDayBuyTime(), new Date())) {
                        shopItem.BUYTIME = shopItem.BUYTIME - shopBean.getDayBuyCount().get(i);
                    }
                    i++;
                }
                break;
            case "star":
                shopBean = ShopCache.getInstance().getPlayerShopBean(playerId);
                List<ShopTemplate> starTemplates = ShopTemplateCache.getInstance().getStarShopTemplates();
                respmsg.item_list = new ArrayList<>(starTemplates.size());
                i = 0;
                for (ShopTemplate shopTemplate : starTemplates) {
                    WsMessageBase.IOShopItem shopItem = new WsMessageBase.IOShopItem(shopTemplate.getGSID(), shopTemplate.getCOUNT(), shopTemplate.getBUYTIME()
                            , shopTemplate.getCOIN(), shopTemplate.getPRICE(), shopTemplate.getDISCOUNT());
                    respmsg.item_list.add(shopItem);
                    if (shopBean != null && shopBean.getStarBuyCount() != null && shopBean.getStarBuyCount().get(i) != null) {
                        shopItem.BUYTIME = shopItem.BUYTIME - shopBean.getStarBuyCount().get(i);
                    }
                    i++;
                }
                break;
            case "decomp":
                shopBean = ShopCache.getInstance().getPlayerShopBean(playerId);
                List<ShopTemplate> decompTemplates = ShopTemplateCache.getInstance().getDecompTemplates();
                respmsg.item_list = new ArrayList<>(decompTemplates.size());
                i = 0;
                for (ShopTemplate shopTemplate : decompTemplates) {
                    WsMessageBase.IOShopItem shopItem = new WsMessageBase.IOShopItem(shopTemplate.getGSID(), shopTemplate.getCOUNT(), shopTemplate.getBUYTIME()
                            , shopTemplate.getCOIN(), shopTemplate.getPRICE(), shopTemplate.getDISCOUNT());
                    respmsg.item_list.add(shopItem);
                    if (shopBean != null && shopBean.getDecompBuyCount() != null && shopBean.getDecompBuyCount().get(i) != null) {
                        shopItem.BUYTIME = shopItem.BUYTIME - shopBean.getDecompBuyCount().get(i);
                    }
                    i++;
                }
                break;
            case "legion":
                shopBean = ShopCache.getInstance().getPlayerShopBean(playerId);
                List<ShopTemplate> legionTemplates = ShopTemplateCache.getInstance().getLegionShopTemplates();
                respmsg.item_list = new ArrayList<>(legionTemplates.size());
                i = 0;
                for (ShopTemplate shopTemplate : legionTemplates) {
                    WsMessageBase.IOShopItem shopItem = new WsMessageBase.IOShopItem(shopTemplate.getGSID(), shopTemplate.getCOUNT(), shopTemplate.getBUYTIME()
                            , shopTemplate.getCOIN(), shopTemplate.getPRICE(), shopTemplate.getDISCOUNT());
                    respmsg.item_list.add(shopItem);
                    if (shopBean != null && shopBean.getLegionBuyCount() != null && shopBean.getLegionBuyCount().get(i) != null) {
                        shopItem.BUYTIME = shopItem.BUYTIME - shopBean.getLegionBuyCount().get(i);
                    }
                    i++;
                }
                break;
            case "general":
            case "kill":
            case "offical":
            case "pvp":
                List<ShopTemplate> shopTemplates = ShopTemplateCache.getInstance().getShopTemplates(shop_type);
                respmsg.item_list = new ArrayList<>(shopTemplates.size());
                for (ShopTemplate shopTemplate : shopTemplates) {
                    WsMessageBase.IOShopItem shopItem = new WsMessageBase.IOShopItem(shopTemplate.getGSID(), shopTemplate.getCOUNT(), shopTemplate.getBUYTIME()
                            , shopTemplate.getCOIN(), shopTemplate.getPRICE(), shopTemplate.getDISCOUNT());
                    respmsg.item_list.add(shopItem);
                }
                break;
            default:
                break;
        }
        //ret
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
