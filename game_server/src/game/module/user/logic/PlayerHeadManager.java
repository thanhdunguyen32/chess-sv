package game.module.user.logic;

import game.module.hero.dao.GeneralTemplateCache;
import game.module.template.GeneralTemplate;
import game.module.template.HeadIconTemplate;
import game.module.user.bean.PlayerHead;
import game.module.user.dao.HeadIconTemplateCache;
import game.module.user.dao.HeadImageTemplateCache;
import game.module.user.dao.PlayerHeadCache;
import game.module.user.dao.PlayerHeadDaoHelper;
import lion.common.SimpleTextConvert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class PlayerHeadManager {

    private static Logger logger = LoggerFactory.getLogger(PlayerHeadManager.class);

    static class SingletonHolder {

        static PlayerHeadManager instance = new PlayerHeadManager();
    }

    public static PlayerHeadManager getInstance() {
        return SingletonHolder.instance;
    }

    public static final int[] ICONS = new int[]{12281, 10261, 12241, 14241, 16321, 10241, 17101, 14381, 12361, 10401, 16302, 12202, 16341, 12301, 18101,
            14301, 16361, 14021,
            10201, 14321, 12201, 14201, 10001, 10321, 16261, 14281, 10341, 16301, 10281, 12262, 16241, 10061, 17081, 18061, 10202, 14341, 16401, 14361, 16262
            , 10222, 10301, 10221, 14221, 12222, 10181, 16281, 10242, 14101, 12221, 16222, 16381, 10381, 12061, 10361, 12321, 12261, 17061, 12001, 14181,
            12041, 12341, 12161, 16001, 14161, 14242, 12121, 16221, 12181, 14261, 14141, 10302, 16141, 10161, 14202, 16041, 16242, 16121, 16021, 14262, 10121
            , 14222, 18081, 18082, 10282, 16181, 14081, 10101, 18041};

    /**
     * @param playerId
     * @param generalTemplateId
     */
    public void addGeneralChangeHead(int playerId, int generalTemplateId) {
        GeneralTemplate generalTemplate = GeneralTemplateCache.getInstance().getHeroTemplate(generalTemplateId);
        String generalName = generalTemplate.getNAME();
        int headIconId = getHeadid(generalTemplateId);
        int headImageId = HeadImageTemplateCache.getInstance().getHeadImageIdByName(generalName, generalTemplate.getSTAR());
        PlayerHead playerHead = PlayerHeadCache.getInstance().getPlayerHead(playerId);
        if (playerHead == null) {
            playerHead = new PlayerHead();
            playerHead.setPlayerId(playerId);
            playerHead.setHeadIcons(new HashSet<>(Collections.singletonList(headIconId)));
            playerHead.setHeadImages(new HashSet<>(Collections.singletonList(headImageId)));
            PlayerHeadCache.getInstance().addPlayerHead(playerHead);
            PlayerHeadDaoHelper.asyncInsertPlayerHead(playerHead);
        } else if (playerHead.getHeadIcons() == null || !playerHead.getHeadIcons().contains(headIconId)) {
            if (playerHead.getHeadIcons() == null) {
                Set<Integer> headIcons = new HashSet<>();
                playerHead.setHeadIcons(headIcons);
                Set<Integer> headImages = new HashSet<>();
                playerHead.setHeadImages(headImages);
            }
            playerHead.getHeadIcons().add(headIconId);
            playerHead.getHeadImages().add(headImageId);
            //save 2 db
            Set<Integer> headIcons = playerHead.getHeadIcons();
            String headIconsStr = SimpleTextConvert.encodeCollection(headIcons);
            Set<Integer> headFrames = playerHead.getHeadFrames();
            String headFramesStr = SimpleTextConvert.encodeCollection(headFrames);
            Set<Integer> headImages = playerHead.getHeadImages();
            String headImagesStr = SimpleTextConvert.encodeCollection(headImages);
            PlayerHeadDaoHelper.asyncUpdatePlayerHead(playerHead.getId(), headIconsStr, headFramesStr, headImagesStr);
        }
    }

    public void addHeadFrame(int playerId, int headFrameId) {
        PlayerHead playerHead = PlayerHeadCache.getInstance().getPlayerHead(playerId);
        if (playerHead == null) {
            playerHead = new PlayerHead();
            playerHead.setPlayerId(playerId);
            playerHead.setHeadFrames(new HashSet<>(Collections.singletonList(headFrameId)));
            PlayerHeadDaoHelper.asyncInsertPlayerHead(playerHead);
            PlayerHeadCache.getInstance().addPlayerHead(playerHead);
        } else if (playerHead.getHeadFrames() == null || !playerHead.getHeadFrames().contains(headFrameId)) {
            if (playerHead.getHeadFrames() == null) {
                Set<Integer> headFrames = new HashSet<>();
                playerHead.setHeadFrames(headFrames);
            }
            playerHead.getHeadFrames().add(headFrameId);
            //save 2 db
            Set<Integer> headIcons = playerHead.getHeadIcons();
            String headIconsStr = SimpleTextConvert.encodeCollection(headIcons);
            Set<Integer> headFrames = playerHead.getHeadFrames();
            String headFramesStr = SimpleTextConvert.encodeCollection(headFrames);
            Set<Integer> headImages = playerHead.getHeadImages();
            String headImagesStr = SimpleTextConvert.encodeCollection(headImages);
            PlayerHeadDaoHelper.asyncUpdatePlayerHead(playerHead.getId(), headIconsStr, headFramesStr, headImagesStr);
        }
    }

    public int getHeadid(int generalTemplateId){
        GeneralTemplate generalTemplate = GeneralTemplateCache.getInstance().getHeroTemplate(generalTemplateId);
        String generalName = generalTemplate.getNAME();
        int headIconId = HeadIconTemplateCache.getInstance().getHeadIconIdByName(generalName);
        return headIconId;
    }

    public int headId2IconId(int headId) {
        HeadIconTemplate headIconTemplate = HeadIconTemplateCache.getInstance().getHeadIconTemplate(headId);
        String generalName = headIconTemplate.getNAME();
        int generalIcon = GeneralTemplateCache.getInstance().getIconIdByName(generalName);
        return generalIcon;
    }

}
