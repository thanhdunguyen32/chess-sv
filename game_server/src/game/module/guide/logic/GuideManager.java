package game.module.guide.logic;

import game.entity.PlayingRole;
import game.module.award.logic.AwardUtils;
import game.module.item.logic.ItemConstants;
import game.module.log.constants.LogConstants;
import game.module.user.logic.PlayerManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GuideManager {

    private static Logger logger = LoggerFactory.getLogger(GuideManager.class);

    public void upgradeSendItems(PlayingRole playingRole, int newLevel, int oldLevel) {
//        int playerId = playingRole.getId();
//        int guideProgress = PlayerManager.getInstance().getOtherCount(playerId, GuideConstants.GUIDE_PROGRESS_MARK);
        //观星引导赠送观星
        if (oldLevel < 14 && newLevel >= 14) {
            AwardUtils.changeRes(playingRole, 30008, 1, LogConstants.MODULE_GUILD);
        }
    }

    static class SingletonHolder {
        static GuideManager instance = new GuideManager();
    }

    public static GuideManager getInstance() {
        return SingletonHolder.instance;
    }

    public void sendInitGoods(PlayingRole playingRole) {
        // 不是首次登陆
        if (playingRole.getPlayerBean().getDownlineTime() != null) {
            return;
        }
        logger.info("send init goods!playerId={}", playingRole.getId());
        //添加免费改名次数
        PlayerManager.getInstance().setOther(playingRole, ItemConstants.CHANGE_NAME_1_MARK, 1, false);
//        int[][] initPropsConfig = SystemTemplateCache.getInstance().getIntArraysConfig(21);
//        for (int[] list : initPropsConfig) {
//            if (list[0] == GameEnum.REWARD_TYPE.PROP) {
//                AwardUtils.changeRes(playingRole, list[0], list[1], list[2], LogConstants.MODULE_NEW_GUIDE);
//            }
//        }
    }

}
