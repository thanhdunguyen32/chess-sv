package game.module.manor.logic;

import game.module.manor.bean.SurrenderPersuade;
import game.module.manor.dao.SurrenderPersuadeCache;
import game.module.manor.dao.SurrenderPersuadeDaoHelper;
import game.module.manor.dao.SurrenderTemplateCache;
import game.module.template.SurrenderTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author HeXuhui
 */
public class SurrenderPersuadeManager {

    private static Logger logger = LoggerFactory.getLogger(SurrenderPersuadeManager.class);

    static class SingletonHolder {
        static SurrenderPersuadeManager instance = new SurrenderPersuadeManager();
    }

    public static SurrenderPersuadeManager getInstance() {
        return SingletonHolder.instance;
    }

    public void passChapter(int playerId, int chapterId) {
        SurrenderTemplate surrenderTemplate = SurrenderTemplateCache.getInstance().getSurrenderTemplate(chapterId);
        if (surrenderTemplate == null) {
            return;
        }
        SurrenderPersuade surrenderPersuade = SurrenderPersuadeCache.getInstance().getSurrenderPersuade(playerId);
        if (surrenderPersuade == null) {
            surrenderPersuade = new SurrenderPersuade();
            surrenderPersuade.setPlayerId(playerId);
            Map<Integer, Integer> persuadeMap = new HashMap<>();
            surrenderPersuade.setSurrendermap(persuadeMap);
            SurrenderPersuadeCache.getInstance().addSurrenderPersuade(surrenderPersuade);
        }
        surrenderPersuade.getSurrendermap().put(chapterId, surrenderTemplate.getLOYAL().getBASE());
        //save
        if (surrenderPersuade.getId() == null) {
            SurrenderPersuadeDaoHelper.asyncInsertSurrenderPersuade(surrenderPersuade);
        } else {
            SurrenderPersuadeDaoHelper.asyncUpdateSurrenderPersuade(surrenderPersuade);
        }
    }

}
