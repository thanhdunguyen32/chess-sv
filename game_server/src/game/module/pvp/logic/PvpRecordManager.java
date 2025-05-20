package game.module.pvp.logic;

import game.GameServer;
import game.module.chapter.bean.BattleFormation;
import game.module.chapter.dao.BattleFormationCache;
import game.module.hero.bean.GeneralBean;
import game.module.hero.dao.GeneralCache;
import game.module.hero.logic.GeneralManager;
import game.module.mythical.bean.MythicalAnimal;
import game.module.mythical.dao.MythicalAnimalCache;
import game.module.mythical.processor.MythicalListProcessor;
import game.module.offline.bean.PlayerBaseBean;
import game.module.offline.logic.PlayerOfflineManager;
import game.module.pvp.bean.PvpRecord;
import game.module.pvp.dao.PvpRecordCache;
import game.module.pvp.dao.PvpRecordDao;
import game.module.pvp.dao.PvpRecordDaoHelper;
import game.module.season.logic.SeasonManager;
import game.module.user.bean.PlayerBean;
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
public class PvpRecordManager {

    private static Logger logger = LoggerFactory.getLogger(PvpRecordManager.class);

    static class SingletonHolder {

        static PvpRecordManager instance = new PvpRecordManager();

    }

    public static PvpRecordManager getInstance() {
        return SingletonHolder.instance;
    }

    public void addPvpRecord(PlayerBean playerBean, Map<Integer, Long> myFormation, int rightPlayerId, Map<Integer, Long> rightFormation,
                             WsMessageBase.IOBattleResult battleRet, int myScore, int myScoreChange, int enemyScore, int enemyScoreChange) {
        int playerId = playerBean.getId();
        BattleFormation battleFormation = BattleFormationCache.getInstance().getBattleFormation(playerId);
        int pvpBattleIndex = 3;
        WsMessageBase.IOPvpRecord ioPvpRecord = new WsMessageBase.IOPvpRecord();
        ioPvpRecord.videoid = SessionManager.getInstance().generateSessionId();
        ioPvpRecord.time = System.currentTimeMillis();
        ioPvpRecord.version = 202004292337L;
        ioPvpRecord.seed = SessionManager.getInstance().generateSessionId();
        ioPvpRecord.result = battleRet.ret;
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
            Integer mythicGsid = mythicsMap.get(pvpBattleIndex);
            if (mythicGsid != null) {
                MythicalAnimal mythicalAnimal = MythicalAnimalCache.getInstance().getPlayerMythicalAnimal(playerId, mythicGsid);
                ioPvpRecord.left.set.mythic = new WsMessageBase.IOMythicalAnimal();
                MythicalListProcessor.buildIOMythicalAnimal(mythicalAnimal, mythicGsid, ioPvpRecord.left.set.mythic);
            }
        }
        //generals
        ioPvpRecord.left.set.team = new HashMap<>();
        Map<Long, GeneralBean> generalAll = GeneralCache.getInstance().getHeros(playerId);
        for (Map.Entry<Integer, Long> aEntry : myFormation.entrySet()) {
            GeneralBean generalBean = generalAll.get(aEntry.getValue());
            WsMessageBase.IOGeneralBean ioGeneralBean = GeneralManager.getInstance().buildIoGeneral(generalBean);
            ioPvpRecord.left.set.team.put(aEntry.getKey(), ioGeneralBean);
        }
        //right
        ioPvpRecord.right = new WsMessageBase.IOBattleRecordSide();
        PlayerBaseBean rightPlayerOffline = PlayerOfflineManager.getInstance().getPlayerOfflineCache(rightPlayerId);
        ioPvpRecord.right.info = new WsMessageBase.IOBattleRecordInfo(rightPlayerOffline.getName(), rightPlayerOffline.getLevel(),
                rightPlayerOffline.getIconid(), rightPlayerOffline.getHeadid(), rightPlayerOffline.getFrameid());
        ioPvpRecord.right.set = new WsMessageBase.IOBattleRecordSet();
        //right mythical
        BattleFormation rightBattleFormation = PlayerOfflineManager.getInstance().getBattleFormation(rightPlayerId);
        Map<Integer, Integer> rightMythicMap = rightBattleFormation.getMythics();
        if (rightMythicMap != null) {
            Integer mythicGsid = rightMythicMap.get(pvpBattleIndex);
            if (mythicGsid != null) {
                Map<Integer, MythicalAnimal> rightMythicalAll = PlayerOfflineManager.getInstance().getMythicalAll(rightPlayerId);
                MythicalAnimal mythicalAnimal = rightMythicalAll.get(mythicGsid);
                ioPvpRecord.right.set.mythic = new WsMessageBase.IOMythicalAnimal();
                MythicalListProcessor.buildIOMythicalAnimal(mythicalAnimal, mythicGsid, ioPvpRecord.right.set.mythic);
            }
        }
        //generals
        ioPvpRecord.right.set.team = new HashMap<>();
        Map<Long, GeneralBean> rightGeneralAll = PlayerOfflineManager.getInstance().getGeneralAll(rightPlayerId);
        for (Map.Entry<Integer, Long> aEntry : rightFormation.entrySet()) {
            GeneralBean generalBean = rightGeneralAll.get(aEntry.getValue());
            if (generalBean == null) {
                continue;
            }
            WsMessageBase.IOGeneralBean ioGeneralBean = GeneralManager.getInstance().buildIoGeneral(generalBean);
            ioPvpRecord.right.set.team.put(aEntry.getKey(), ioGeneralBean);
        }
        //score
        ioPvpRecord.mark = battleRet.ret + "." + myScore + "." + myScoreChange;
        //save
        //save my
        PvpRecord pvpRecord = PvpRecordCache.getInstance().getPvpRecord(playerId);
        if (pvpRecord == null) {
            pvpRecord = createPvpRecord(playerId);
            PvpRecordCache.getInstance().addPvpRecord(pvpRecord);
        }
        if (pvpRecord.getDbPvpRecord() == null || pvpRecord.getDbPvpRecord().getRecords() == null) {
            PvpRecord.DbPvpRecord dbPvpRecord = new PvpRecord.DbPvpRecord();
            pvpRecord.setDbPvpRecord(dbPvpRecord);
            List<WsMessageBase.IOPvpRecord> records = new ArrayList<>();
            dbPvpRecord.setRecords(records);
        }
        //最大10条
        List<WsMessageBase.IOPvpRecord> recordList = pvpRecord.getDbPvpRecord().getRecords();
        while (recordList.size() > 9) {
            recordList.remove(0);
        }
        //add
        recordList.add(ioPvpRecord);
        //save db
        if (pvpRecord.getId() == null) {
            PvpRecordDaoHelper.asyncInsertPvpRecord(pvpRecord);
        } else {
            PvpRecordDaoHelper.asyncUpdatePvpRecord(pvpRecord);
        }
        //save other
        WsMessageBase.IOPvpRecord ioPvpRecordEnemy = new WsMessageBase.IOPvpRecord();
        ioPvpRecordEnemy.videoid = ioPvpRecord.videoid;
        ioPvpRecordEnemy.time = ioPvpRecord.time;
        ioPvpRecordEnemy.version = ioPvpRecord.version;
        ioPvpRecordEnemy.seed = ioPvpRecord.seed;
        ioPvpRecordEnemy.result = battleRet.ret;
        ioPvpRecordEnemy.lper = battleRet.lper;
        ioPvpRecordEnemy.rper = battleRet.rper;
        ioPvpRecordEnemy.ltper = battleRet.ltper;
        ioPvpRecordEnemy.rtper = battleRet.rtper;
        ioPvpRecordEnemy.report = battleRet.report;
        ioPvpRecordEnemy.left = ioPvpRecord.left;
        ioPvpRecordEnemy.right = ioPvpRecord.right;
        ioPvpRecordEnemy.mark = (battleRet.ret.equals("win") ? "lose" : "win") + "." + enemyScore + "." + enemyScoreChange;
        //season
        ioPvpRecord.season = SeasonManager.getInstance().buildIoBattleRecordSeason();
        PvpRecord rightPvpRecord = PvpRecordCache.getInstance().getPvpRecord(rightPlayerId);
        if (rightPvpRecord == null) {
            GameServer.executorService.execute(() -> {
                PvpRecord rightPvpRecordDb = PvpRecordDao.getInstance().getPvpRecord(rightPlayerId);
                if (rightPvpRecordDb == null) {
                    return;
                }
                if (rightPvpRecordDb.getDbPvpRecord() == null || rightPvpRecordDb.getDbPvpRecord().getRecords() == null) {
                    PvpRecord.DbPvpRecord dbPvpRecord = new PvpRecord.DbPvpRecord();
                    rightPvpRecordDb.setDbPvpRecord(dbPvpRecord);
                    List<WsMessageBase.IOPvpRecord> records = new ArrayList<>();
                    dbPvpRecord.setRecords(records);
                }
                //最大10条
                List<WsMessageBase.IOPvpRecord> rightRecrodList = rightPvpRecordDb.getDbPvpRecord().getRecords();
                while (rightRecrodList.size() > 9) {
                    rightRecrodList.remove(0);
                }
                //add
                rightRecrodList.add(ioPvpRecordEnemy);
                //save db
                if (rightPvpRecordDb.getId() == null) {
                    PvpRecordDao.getInstance().addPvpRecord(rightPvpRecordDb);
                } else {
                    PvpRecordDao.getInstance().updatePvpRecord(rightPvpRecordDb);
                }
            });
        } else {
            //最大10条
            List<WsMessageBase.IOPvpRecord> rightRecrodList = rightPvpRecord.getDbPvpRecord().getRecords();
            while (rightRecrodList.size() > 9) {
                rightRecrodList.remove(0);
            }
            //add
            rightRecrodList.add(ioPvpRecordEnemy);
            PvpRecordDaoHelper.asyncUpdatePvpRecord(rightPvpRecord);
        }
    }

    private PvpRecord createPvpRecord(int playerId) {
        PvpRecord pvpRecord = new PvpRecord();
        pvpRecord.setPlayerId(playerId);
        return pvpRecord;
    }
}
