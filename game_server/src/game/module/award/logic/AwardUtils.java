package game.module.award.logic;

import game.entity.PlayingRole;
import game.module.activity.logic.ActivityWeekManager;
import game.module.award.bean.GameConfig;
import game.module.hero.logic.GeneralManager;
import game.module.item.dao.EquipTemplateCache;
import game.module.item.dao.TreasureTemplateCache;
import game.module.item.logic.ItemManager;
import game.module.log.logic.LogItemGoManager;
import game.module.mission.logic.MissionManager;
import game.module.user.logic.PlayerHeadManager;
import game.module.user.logic.PlayerInfoManager;
import game.module.user.logic.PlayerManager;
import game.module.user.logic.ScrollAnnoManager;
import game.module.vip.logic.VipManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 奖励工具类
 *
 * @author zhangning
 * @Date 2015年2月15日 下午4:48:28
 */
public class AwardUtils {

    private static Logger logger = LoggerFactory.getLogger(AwardUtils.class);

    public static Object changeRes(PlayingRole playingRole, int gsid, int itemCount, int logModule, Object... extraParam) {
        Object ret = null;
        switch (gsid) {
            case GameConfig.PLAYER.GOLD:
                PlayerInfoManager.getInstance().addCopper(playingRole, itemCount);
                break;
            case GameConfig.PLAYER.YB:
                PlayerInfoManager.getInstance().changeMoney(playingRole, itemCount, logModule);
                break;
            case GameConfig.PLAYER.EXP:
                PlayerInfoManager.getInstance().addExp(playingRole, itemCount);
                break;
            case GameConfig.PLAYER.VIPEXP:
                VipManager.getInstance().rechargeVipLev(playingRole, itemCount);
                break;
            default:
                int itemTypeByGsid = ItemManager.getInstance().getItemTypeByGsid(gsid);
                switch (itemTypeByGsid) {
                    case GameConfig.ItemType.general:
                        GeneralManager.getInstance().addHero(playingRole, gsid, itemCount);
                        PlayerHeadManager.getInstance().addGeneralChangeHead(playingRole.getId(), gsid);
                        ActivityWeekManager.getInstance().djrwActivity(playingRole, gsid);
                        break;
                    case GameConfig.ItemType.hide:
                        PlayerManager.getInstance().changeHide(playingRole, gsid, itemCount);
                        break;
                    case GameConfig.ItemType.headframe:
                        PlayerHeadManager.getInstance().addHeadFrame(playingRole.getId(), gsid);
                        break;
                    case GameConfig.ItemType.none:
                        PlayerManager.getInstance().changeOther(playingRole, gsid, itemCount);
                        break;
                    default:
                        if (itemCount > 0) {
                            // 添加物品
                            ItemManager.getInstance().addItem(playingRole, gsid, itemCount, playingRole.getId());
                            //check is equip or treasure
                            if(EquipTemplateCache.getInstance().containsId(gsid)){
                                MissionManager.getInstance().equipChangeProgress(playingRole,gsid,itemCount);
                                ScrollAnnoManager.getInstance().equipQuality(playingRole,gsid);
                                ScrollAnnoManager.getInstance().equipStar(playingRole,gsid);
                            }else if(TreasureTemplateCache.getInstance().containsId(gsid)){
                                MissionManager.getInstance().treasureChangeProgress(playingRole,gsid,itemCount);
                                ScrollAnnoManager.getInstance().treasure(playingRole,gsid);
                            }
                        } else if (itemCount < 0) {
                            // 扣除道具
                            ItemManager.getInstance().subItem(playingRole, gsid, -itemCount);
                        }
                        break;
                }
                break;
        }
        // 添加日志
        LogItemGoManager.getInstance().addLog(playingRole.getId(), logModule, gsid, itemCount);
        return ret;
    }

    public static void setRes(PlayingRole playingRole, int gsid, int itemCount, boolean isPush){
        int itemTypeByGsid = ItemManager.getInstance().getItemTypeByGsid(gsid);
        switch (itemTypeByGsid) {
            case GameConfig.ItemType.general:
//                GeneralManager.getInstance().addHero(playingRole, gsid, itemCount);
//                PlayerHeadManager.getInstance().addGeneralChangeHead(playingRole.getId(), gsid);
                break;
            case GameConfig.ItemType.hide:
                PlayerManager.getInstance().setHide(playingRole, gsid, itemCount, isPush);
                break;
            case GameConfig.ItemType.none:
                PlayerManager.getInstance().setOther(playingRole, gsid, itemCount, isPush);
                break;
            default:
                ItemManager.getInstance().setItem(playingRole, gsid, itemCount, isPush);
                break;
        }
    }

}
