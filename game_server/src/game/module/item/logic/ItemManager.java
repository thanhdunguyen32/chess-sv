package game.module.item.logic;

import game.entity.PlayingRole;
import game.module.award.bean.GameConfig;
import game.module.hero.dao.GeneralChipTemplateCache;
import game.module.item.bean.Item;
import game.module.item.dao.*;
import game.module.user.bean.PlayerBean;
import game.module.user.bean.PlayerProp;
import game.module.user.dao.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageHall;

import java.util.Map;

public class ItemManager {

    private static Logger logger = LoggerFactory.getLogger(ItemManager.class);

    static class SingletonHolder {

        static ItemManager instance = new ItemManager();
    }

    public static ItemManager getInstance() {
        return SingletonHolder.instance;
    }

    public void addItem(PlayingRole playingRole, int itemTemplateId, int count, int playerId) {
        logger.info("+++add item,playerId={},id={},count={}", playerId, itemTemplateId, count);
        Map<Integer, Item> itemMap = ItemCache.getInstance().getItemTemplateKey(playerId);
        // 道具已存在
        Item item;
        if (itemMap.containsKey(itemTemplateId)) {
            item = itemMap.get(itemTemplateId);
            item.setCount(item.getCount() + count);
            // save db
            ItemDaoHelper.asyncUpdateItem(item.getTemplateId(), item.getCount(), item.getPlayerId());
        } else {
            item = new Item();
            item.setPlayerId(playerId);
            item.setTemplateId(itemTemplateId);
            item.setCount(count);
            ItemCache.getInstance().addNew(playerId, item);
            // save db
            ItemDaoHelper.asyncInsertItem(playingRole, item);
        }
        WsMessageHall.PushPropChange pushMsg = new WsMessageHall.PushPropChange(itemTemplateId, item.getCount());
        playingRole.write(pushMsg.build(playingRole.alloc()));
    }

    public void subItem(PlayingRole playingRole, int itemTemplateId, int itemCount) {
        Map<Integer, Item> itemMap = ItemCache.getInstance().getItemTemplateKey(playingRole.getId());
        // 道具已存在
        Item item = itemMap.get(itemTemplateId);
        if (item != null) {
            subItem(playingRole, item, itemCount);
        }
    }

    private void subItem(PlayingRole playingRole, Item item, int itemCount) {
        logger.info("---sub item,playerId={},id={},count={}", playingRole.getId(), item.getTemplateId(), itemCount);
        if (itemCount >= item.getCount()) {
            item.setCount(0);
            ItemCache.getInstance().removeItem(playingRole.getId(), item);
            ItemDaoHelper.asyncRemoveItem(item.getTemplateId(), item.getPlayerId());
            // push item change
            WsMessageHall.PushPropChange pushMsg = new WsMessageHall.PushPropChange(item.getTemplateId(), 0);
            playingRole.write(pushMsg.build(playingRole.alloc()));
        } else {
            item.setCount(item.getCount() - itemCount);
            ItemDaoHelper.asyncUpdateItem(item.getTemplateId(), item.getCount(), item.getPlayerId());
            // push item change
            WsMessageHall.PushPropChange pushMsg = new WsMessageHall.PushPropChange(item.getTemplateId(), item.getCount());
            playingRole.write(pushMsg.build(playingRole.alloc()));
        }
    }

    public void setItem(PlayingRole playingRole, int gsid, int nowCount, boolean isPush) {
        int playerId = playingRole.getId();
        logger.info("set item,playerId={},id={},count={}", playerId, gsid, nowCount);
        Map<Integer, Item> playerItems = ItemCache.getInstance().getItemTemplateKey(playerId);
        nowCount = Math.max(nowCount, 0);
        if (nowCount > 0) {
            //change val
            Item itemBean = playerItems.get(gsid);
            if (itemBean == null) {
                itemBean = new Item();
                itemBean.setTemplateId(gsid);
                itemBean.setCount(nowCount);
                itemBean.setPlayerId(playerId);
                ItemCache.getInstance().addNew(playerId, itemBean);
                // save db
                ItemDaoHelper.asyncInsertItem(playingRole, itemBean);
            } else {
                itemBean.setCount(nowCount);
                ItemDaoHelper.asyncUpdateItem(gsid, nowCount, playerId);
            }
        } else {
            Item playerProp = playerItems.get(gsid);
            if (playerProp != null) {
                playerProp.setCount(0);
                playerItems.remove(gsid);
                ItemDaoHelper.asyncRemoveItem(gsid, playerId);
            }
        }
        if (isPush) {
            // push item change
            WsMessageHall.PushPropChange pushMsg = new WsMessageHall.PushPropChange(gsid, nowCount);
            playingRole.write(pushMsg.build(playingRole.alloc()));
        }
    }

    /**
     * 判断道具是否足够
     *
     * @param playerBean
     * @param gsid
     * @param cnt
     * @return
     */
    public boolean checkItemEnough(PlayerBean playerBean, int gsid, int cnt) {
        switch (gsid) {
            case GameConfig.PLAYER.GOLD:
                return playerBean.getGold() >= cnt;
            case GameConfig.PLAYER.YB:
                return playerBean.getMoney() >= cnt;
            default:
                int playerId = playerBean.getId();
                if (isHidden(gsid)) {
                    Map<Integer, PlayerProp> playerHiddens = PlayerHideCache.getInstance().getPlayerHidden(playerId);
                    if (playerHiddens.containsKey(gsid) && playerHiddens.get(gsid).getCount() >= cnt) {
                        return true;
                    }
                } else {
                    Map<Integer, Item> itemAll = ItemCache.getInstance().getItemTemplateKey(playerId);
                    Map<Integer, PlayerProp> playerOthers = PlayerOtherCache.getInstance().getPlayerOther(playerId);
                    if (itemAll.containsKey(gsid) && itemAll.get(gsid).getCount() >= cnt || playerOthers.containsKey(gsid) && playerOthers.get(gsid).getCount() >= cnt) {
                        return true;
                    }
                }
        }
        return false;
    }

    public int getCount(PlayerBean playerBean, int gsid) {
        int ret = 0;
        switch (gsid) {
            case GameConfig.PLAYER.GOLD:
                ret = playerBean.getGold();
            case GameConfig.PLAYER.YB:
                ret = playerBean.getMoney();
            default:
                int playerId = playerBean.getId();
                if (isHidden(gsid)) {
                    Map<Integer, PlayerProp> playerHiddens = PlayerHideCache.getInstance().getPlayerHidden(playerId);
                    if (playerHiddens.containsKey(gsid)) {
                        ret = playerHiddens.get(gsid).getCount();
                    }
                } else {
                    Map<Integer, Item> itemAll = ItemCache.getInstance().getItemTemplateKey(playerId);
                    Map<Integer, PlayerProp> playerOthers = PlayerOtherCache.getInstance().getPlayerOther(playerId);
                    if (itemAll.containsKey(gsid)) {
                        ret = itemAll.get(gsid).getCount();
                    } else if (playerOthers.containsKey(gsid)) {
                        ret = playerOthers.get(gsid).getCount();
                    }
                }
        }
        return ret;
    }

    public boolean checkProps(int itemType, int itemCount, int playerId) {
        boolean ret = false;
        Map<Integer, Item> itemMap = ItemCache.getInstance().getItemTemplateKey(playerId);
        if (itemMap.containsKey(itemType) && itemMap.get(itemType).getCount() >= itemCount) {
            ret = true;
        }
        return ret;
    }

    public int getItemTypeByGsid(int gsid) {
        return gsid >= 1e4 && gsid <= 19999 ? GameConfig.ItemType.general : gsid >= 2e4 && gsid <= 29999 ? GameConfig.ItemType.generalchip :
                gsid >= 3e4 && gsid <= 30999 ? GameConfig.ItemType.prop : gsid >= 31e3 && gsid <= 31999 ? GameConfig.ItemType.box :
                        gsid >= 32e3 && gsid <= 32499 ? GameConfig.ItemType.rbox : gsid >= 32500 && gsid <= 32999 ? GameConfig.ItemType.cbox :
                                gsid >= 33e3 && gsid <= 33999 ? GameConfig.ItemType.equip : gsid >= 34e3 && gsid <= 34999 ? GameConfig.ItemType.treas :
                                        gsid >= 35e3 && gsid <= 35999 ? GameConfig.ItemType.treaschip : gsid >= 36e3 && gsid <= 36999 ?
                                                GameConfig.ItemType.grandomchip : gsid >= 37e3 && gsid <= 37999 ? GameConfig.ItemType.trandomchip :
                                                gsid >= 38e3 && gsid <= 38999 ? GameConfig.ItemType.hide : gsid >= 39e3 && gsid <= 39999 ?
                                                        GameConfig.ItemType.exclusive : gsid >= 5e4 && gsid <= 50999 ? GameConfig.ItemType.headicon :
                                                        gsid >= 51e3 && gsid <= 51999 ? GameConfig.ItemType.headframe : gsid >= 52e3 && gsid <= 53999 ?
                                                                GameConfig.ItemType.headimage : GameConfig.ItemType.none;
    }

    public boolean checkItemExist(int gsid) {
        return GeneralChipTemplateCache.getInstance().containsId(gsid) || ItemTemplateCache.getInstance().containsId(gsid) || RBoxTemplateCache.getInstance().containsId(gsid) ||
                EquipTemplateCache.getInstance().containsId(gsid) || TreasureTemplateCache.getInstance().containsId(gsid) || TreasureChipTemplateCache.getInstance().containsId(gsid) ||
                HeadIconTemplateCache.getInstance().containsId(gsid) || HeadFrameTemplateCache.getInstance().containsId(gsid) || HeadImageTemplateCache.getInstance().containsId(gsid) ||
                PropHideTemplateCache.getInstance().containsId(gsid);
    }

    public boolean isGeneral(int gsid) {
        return gsid >= 1e4 && gsid <= 19999;
    }

    public boolean isHidden(int gsid) {
        return gsid >= 38e3 && gsid <= 38999;
    }

}
