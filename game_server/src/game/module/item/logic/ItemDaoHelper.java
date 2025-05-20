package game.module.item.logic;

import game.GameServer;
import game.entity.PlayingRole;
import game.module.item.bean.Item;
import game.module.item.dao.ItemDao;

public class ItemDaoHelper {

    public static void asyncInsertItem(final PlayingRole playingRole, final Item item) {
        GameServer.executorService.execute(() -> ItemDao.getInstance().addItem(item));
    }

    public static void asyncUpdateItem(final int itemTplId, final int newCount, final int playerId) {
        GameServer.executorService.execute(() -> ItemDao.getInstance().updateItemCount(itemTplId, newCount, playerId));
    }

    public static void asyncRemoveItem(final int itemTplId, final int playerId) {
        GameServer.executorService.execute(() -> ItemDao.getInstance().removeItem(itemTplId, playerId));
    }

    public static void asyncAddItemCheck(int playerId, int itemId, int itemCount) {
        GameServer.executorService.execute(() -> {
            boolean checkItemExist = ItemDao.getInstance().checkItemExist(playerId, itemId);
            if (checkItemExist) {
                ItemDao.getInstance().addItemCount(itemId, itemCount, playerId);
            } else {
                Item itembean = new Item();
                itembean.setPlayerId(playerId);
                itembean.setTemplateId(itemId);
                itembean.setCount(itemCount);
                ItemDao.getInstance().addItem(itembean);
            }
        });
    }
}
