package game.module.item.dao;

import game.module.item.bean.Item;
import game.module.template.EquipTemplate;
import game.module.template.TreasureTemplate;
import lion.common.BeanTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ItemCache {

    private static Logger logger = LoggerFactory.getLogger(ItemCache.class);

    static class SingletonHolder {
        static ItemCache instance = new ItemCache();
    }

    public static ItemCache getInstance() {
        return SingletonHolder.instance;
    }

    private Map<Integer, Map<Integer, Item>> itemMapTemplateIdKey = new ConcurrentHashMap<Integer, Map<Integer, Item>>();
    private Map<Integer, TreeSet<Item>> equipCache = new ConcurrentHashMap<>();
    private Map<Integer, TreeSet<Item>> treasureCache = new ConcurrentHashMap<>();

    public void loadFromDb(int playerId) {
        if (itemMapTemplateIdKey.get(playerId) != null) {
            return;
        }
        List<Item> items = ItemDao.getInstance().getItemsByPlayerId(playerId);
        // add cache with template id key
        Map<Integer, Item> itemMapTemplateIdKeyOne = new HashMap<Integer, Item>();
        try {
            BeanTool.addOrUpdate(itemMapTemplateIdKeyOne, items, "templateId");
            itemMapTemplateIdKey.put(playerId, itemMapTemplateIdKeyOne);
            //equip cache
            TreeSet<Item> equipMap = new TreeSet<>((o1, o2) -> {
                EquipTemplate equipTemplate1 = EquipTemplateCache.getInstance().getEquipTemplateById(o1.getTemplateId());
                EquipTemplate equipTemplate2 = EquipTemplateCache.getInstance().getEquipTemplateById(o2.getTemplateId());
                if(!equipTemplate1.getQUALITY().equals(equipTemplate2.getQUALITY())){
                    return equipTemplate2.getQUALITY() - equipTemplate1.getQUALITY();
                }else if(!equipTemplate2.getSTAR().equals(equipTemplate1.getSTAR())){
                    return equipTemplate2.getSTAR() - equipTemplate1.getSTAR();
                }else{
                    return equipTemplate2.getId() - equipTemplate1.getId();
                }
            });
            TreeSet<Item> treasureMap = new TreeSet<>((o1, o2) -> {
                TreasureTemplate equipTemplate1 = TreasureTemplateCache.getInstance().getTreasureTemplateById(o1.getTemplateId());
                TreasureTemplate equipTemplate2 = TreasureTemplateCache.getInstance().getTreasureTemplateById(o2.getTemplateId());
                if(!equipTemplate1.getQUALITY().equals(equipTemplate2.getQUALITY())){
                    return equipTemplate2.getQUALITY() - equipTemplate1.getQUALITY();
                }else if(!equipTemplate2.getSTAR().equals(equipTemplate1.getSTAR())){
                    return equipTemplate2.getSTAR() - equipTemplate1.getSTAR();
                }else{
                    return equipTemplate2.getId() - equipTemplate1.getId();
                }
            });
            for (Map.Entry<Integer, Item> aEntry : itemMapTemplateIdKeyOne.entrySet()) {
                Integer templateId = aEntry.getKey();
                if (EquipTemplateCache.getInstance().containsId(templateId)) {
                    equipMap.add(aEntry.getValue());
                }else if(TreasureTemplateCache.getInstance().containsId(templateId)){
                    treasureMap.add(aEntry.getValue());
                }
            }
            equipCache.put(playerId, equipMap);
            treasureCache.put(playerId, treasureMap);
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    public Map<Integer, Item> getItemTemplateKey(int playerId) {
        return itemMapTemplateIdKey.get(playerId);
    }

    public TreeSet<Item> getEquips(int playerId){
        return equipCache.get(playerId);
    }

    public TreeSet<Item> getTreasures(int playerId){
        return treasureCache.get(playerId);
    }

    public void addNew(int playerId, Item item) {
        Integer templateId = item.getTemplateId();
        itemMapTemplateIdKey.get(playerId).put(templateId, item);
        if (EquipTemplateCache.getInstance().containsId(templateId)) {
            TreeSet<Item> equipMap = equipCache.get(playerId);
            equipMap.add(item);
        }else if(TreasureTemplateCache.getInstance().containsId(templateId)){
            TreeSet<Item> treasureMap = treasureCache.get(playerId);
            treasureMap.add(item);
        }
    }

    public void remove(int playerId) {
        itemMapTemplateIdKey.remove(playerId);
        equipCache.remove(playerId);
        treasureCache.remove(playerId);
    }

    public Collection<Item> getItemAll(int playerId) {
        return itemMapTemplateIdKey.get(playerId).values();
    }

    public void removeItem(int playerId, Item item) {
        int templateId = item.getTemplateId();
        Map<Integer, Item> templateIdKeyMap = getItemTemplateKey(playerId);
        if (templateIdKeyMap != null) {
            templateIdKeyMap.remove(templateId);
        }
        if (EquipTemplateCache.getInstance().containsId(templateId)) {
            TreeSet<Item> equipSet = equipCache.get(playerId);
            equipSet.remove(item);
        }else if(TreasureTemplateCache.getInstance().containsId(templateId)){
            TreeSet<Item> treasureSet = treasureCache.get(playerId);
            treasureSet.remove(item);
        }
    }

}
