package game.module.tower.logic;

import game.module.chapter.bean.BattleFormation;
import game.module.chapter.dao.BattleFormationCache;
import game.module.chapter.logic.BattleFormationManager;
import game.module.hero.bean.GeneralBean;
import game.module.hero.dao.GeneralCache;
import game.module.hero.dao.GeneralTemplateCache;
import game.module.hero.logic.GeneralManager;
import game.module.mythical.bean.MythicalAnimal;
import game.module.mythical.dao.MythicalAnimalCache;
import game.module.mythical.processor.MythicalListProcessor;
import game.module.season.logic.SeasonManager;
import game.module.template.ChapterBattleTemplate;
import game.module.template.GeneralTemplate;
import game.module.template.TowerTemplate;
import game.module.tower.bean.TowerReplay;
import game.module.tower.dao.TowerBattleTemplateCache;
import game.module.tower.dao.TowerReplayCache;
import game.module.tower.dao.TowerReplayDaoHelper;
import game.module.tower.dao.TowerTemplateCache;
import game.module.user.bean.PlayerBean;
import game.module.user.dao.HeadIconTemplateCache;
import game.module.user.dao.HeadImageTemplateCache;
import game.session.SessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author HeXuhui
 */
public class TowerReplayManager {

    private static Logger logger = LoggerFactory.getLogger(TowerReplayManager.class);

    static class SingletonHolder {

        static TowerReplayManager instance = new TowerReplayManager();

    }

    public static TowerReplayManager getInstance() {
        return SingletonHolder.instance;
    }

    public void addTowerReplay(PlayerBean playerBean, int towerLevel, WsMessageBase.IOBattleResult battleRet) {
        int playerId = playerBean.getId();
        BattleFormation battleFormation = BattleFormationCache.getInstance().getBattleFormation(playerId);
        int towerBattleIndex = 5;
        Map<Integer, Long> battleFormationTower = BattleFormationManager.getInstance().getFormationByType(towerBattleIndex, battleFormation);
        WsMessageBase.IOPvpRecord ioPvpRecord = new WsMessageBase.IOPvpRecord();
        ioPvpRecord.videoid = SessionManager.getInstance().generateSessionId();
        ioPvpRecord.time = System.currentTimeMillis();
        ioPvpRecord.version = 202004292337L;
        ioPvpRecord.seed = SessionManager.getInstance().generateSessionId();
        ioPvpRecord.result = "win";
        ioPvpRecord.lper = battleRet.lper;
        ioPvpRecord.rper = battleRet.rper;
        ioPvpRecord.ltper = battleRet.ltper;
        ioPvpRecord.rtper = battleRet.rtper;
        ioPvpRecord.report = battleRet.report;
        //season
        ioPvpRecord.season = SeasonManager.getInstance().buildIoBattleRecordSeason();
        //generate left generals
        ioPvpRecord.left = new WsMessageBase.IOBattleRecordSide();
        ioPvpRecord.left.info = new WsMessageBase.IOBattleRecordInfo(playerBean.getName(), playerBean.getLevel(), playerBean.getIconid(), playerBean.getHeadid()
                , playerBean.getFrameid());
        ioPvpRecord.left.set = new WsMessageBase.IOBattleRecordSet();
        //mythical
        Map<Integer, Integer> mythicsMap = battleFormation.getMythics();
        if (mythicsMap != null) {
            Integer mythicGsid = mythicsMap.get(towerBattleIndex);
            if (mythicGsid != null) {
                MythicalAnimal mythicalAnimal = MythicalAnimalCache.getInstance().getPlayerMythicalAnimal(playerId, mythicGsid);
                ioPvpRecord.left.set.mythic = new WsMessageBase.IOMythicalAnimal();
                MythicalListProcessor.buildIOMythicalAnimal(mythicalAnimal, mythicGsid, ioPvpRecord.left.set.mythic);
            }
        }
        //generals
        ioPvpRecord.left.set.team = new HashMap<>();
        Map<Long, GeneralBean> generalAll = GeneralCache.getInstance().getHeros(playerId);
        for (Map.Entry<Integer, Long> aEntry : battleFormationTower.entrySet()) {
            GeneralBean generalBean = generalAll.get(aEntry.getValue());
            WsMessageBase.IOGeneralBean ioGeneralBean = GeneralManager.getInstance().buildIoGeneral(generalBean);
            ioPvpRecord.left.set.team.put(aEntry.getKey(), ioGeneralBean);
        }
        //right
        ioPvpRecord.right = new WsMessageBase.IOBattleRecordSide();
        TowerTemplate towerTemplate = TowerTemplateCache.getInstance().getTowerTemplateById(towerLevel - 1);
        Integer enemyGsid = towerTemplate.getGENERAL();
        GeneralTemplate generalTemplate = GeneralTemplateCache.getInstance().getHeroTemplate(enemyGsid);
        String generalName = generalTemplate.getNAME();
        int headIconId = HeadIconTemplateCache.getInstance().getHeadIconIdByName(generalName);
        int headImageId = HeadImageTemplateCache.getInstance().getHeadImageIdByName(generalName, generalTemplate.getSTAR());
        ioPvpRecord.right.info = new WsMessageBase.IOBattleRecordInfo(String.valueOf(towerLevel), towerTemplate.getGLEVEL(), headIconId, headImageId, 51001);
        ioPvpRecord.right.set = new WsMessageBase.IOBattleRecordSet();
        //generals
        ioPvpRecord.right.set.team = new HashMap<>();
        Map<Integer, ChapterBattleTemplate> towerBattleTeam = TowerBattleTemplateCache.getInstance().getTowerBattleById(towerLevel);
        for (Map.Entry<Integer, ChapterBattleTemplate> aEntry : towerBattleTeam.entrySet()) {
            ChapterBattleTemplate chapterBattleTemplate = aEntry.getValue();
            WsMessageBase.IOGeneralBean ioGeneralBean = GeneralManager.getInstance().buildIoGeneral(chapterBattleTemplate);
            ioPvpRecord.right.set.team.put(aEntry.getKey(), ioGeneralBean);
        }
        //save
        TowerReplay towerReplay = TowerReplayCache.getInstance().getTowerReplay(towerLevel);
        if (towerReplay == null) {
            towerReplay = createTowerReplay(towerLevel);
            TowerReplayCache.getInstance().addTowerReplay(towerReplay);
        }
        synchronized (towerReplay) {
            if (towerReplay.getDbTowerReplay() == null || towerReplay.getDbTowerReplay().getRecords() == null) {
                TowerReplay.DbTowerReplay dbTowerReplay = new TowerReplay.DbTowerReplay();
                towerReplay.setDbTowerReplay(dbTowerReplay);
                List<WsMessageBase.IOPvpRecord> records = new ArrayList<>();
                dbTowerReplay.setRecords(records);
            }
            List<WsMessageBase.IOPvpRecord> records = towerReplay.getDbTowerReplay().getRecords();
            records.add(ioPvpRecord);
            if (records.size() > 2) {
                records.remove(0);
            }
        }
        //save db
        if (towerReplay.getId() == null) {
            TowerReplayDaoHelper.asyncInsertTowerReplay(towerReplay);
        } else {
            TowerReplayDaoHelper.asyncUpdateTowerReplay(towerReplay);
        }
    }

    private TowerReplay createTowerReplay(int towerLevel) {
        TowerReplay towerReplay = new TowerReplay();
        towerReplay.setTowerLevel(towerLevel);
        return towerReplay;
    }
}
