package game.module.chapter.logic;

import game.module.chapter.bean.BattleFormation;
import game.module.chapter.dao.BattleFormationCache;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BattleFormationManager {

    private static Logger logger = LoggerFactory.getLogger(BattleFormationManager.class);

    public BattleFormation createBattleFormation(int playerId) {
        BattleFormation battleFormation = new BattleFormation();
        battleFormation.setPlayerId(playerId);
        return battleFormation;
    }

    static class SingletonHolder {
        static BattleFormationManager instance = new BattleFormationManager();
    }

    public static BattleFormationManager getInstance() {
        return SingletonHolder.instance;
    }

    public List<WsMessageBase.IOBattleFormation> buildFormationList(int playerId) {
        List<WsMessageBase.IOBattleFormation> retlist = new ArrayList<>();
        BattleFormation battleFormation = BattleFormationCache.getInstance().getBattleFormation(playerId);
        if (battleFormation != null) {
            Map<Integer, Long> normalFormation = battleFormation.getNormal();
            if (normalFormation != null) {
                retlist.add(build1Formation("normal", normalFormation, battleFormation.getMythics()));
            }
            Map<Integer, Long> pre1Formation = battleFormation.getPre1();
            if (pre1Formation != null) {
                retlist.add(build1Formation("pre1", pre1Formation, battleFormation.getMythics()));
            }
            Map<Integer, Long> pre2Formation = battleFormation.getPre2();
            if (pre2Formation != null) {
                retlist.add(build1Formation("pre2", pre2Formation, battleFormation.getMythics()));
            }
            Map<Integer, Long> pvpattFormation = battleFormation.getPvpatt();
            if (pvpattFormation != null) {
                retlist.add(build1Formation("pvpatt", pvpattFormation, battleFormation.getMythics()));
            }
            Map<Integer, Long> pvpdefFormation = battleFormation.getPvpdef();
            if (pvpdefFormation != null) {
                retlist.add(build1Formation("pvpdef", pvpdefFormation, battleFormation.getMythics()));
            }
            Map<Integer, Long> towerFormation = battleFormation.getTower();
            if (towerFormation != null) {
                retlist.add(build1Formation("tower", towerFormation, battleFormation.getMythics()));
            }
            Map<Integer, Long> expeditionFormation = battleFormation.getExpedition();
            if (expeditionFormation != null) {
                retlist.add(build1Formation("expedition", expeditionFormation, battleFormation.getMythics()));
            }
            Map<Integer, Long> dungeonFormation = battleFormation.getDungeon();
            if (dungeonFormation != null) {
                retlist.add(build1Formation("dungeon", dungeonFormation, battleFormation.getMythics()));
            }
            Map<Integer, Long> teampvpFormation = battleFormation.getTeampvp();
            if (teampvpFormation != null) {
                retlist.add(build1Formation("teampvp", teampvpFormation, battleFormation.getMythics()));
            }
            Map<Integer, Long> kingpvp1Formation = battleFormation.getKingpvp1();
            if (kingpvp1Formation != null) {
                retlist.add(build1Formation("kingpvp1", kingpvp1Formation, battleFormation.getMythics()));
            }
            Map<Integer, Long> kingpvp2Formation = battleFormation.getKingpvp2();
            if (kingpvp2Formation != null) {
                retlist.add(build1Formation("kingpvp2", kingpvp2Formation, battleFormation.getMythics()));
            }
            Map<Integer, Long> kingpvp3Formation = battleFormation.getKingpvp3();
            if (kingpvp3Formation != null) {
                retlist.add(build1Formation("kingpvp3", kingpvp3Formation, battleFormation.getMythics()));
            }
        }
        return retlist;
    }

    private WsMessageBase.IOBattleFormation build1Formation(String formationName, Map<Integer, Long> aFormation, Map<Integer, Integer> mythics) {
        WsMessageBase.IOBattleFormation ioBattleFormation = new WsMessageBase.IOBattleFormation();
        ioBattleFormation.f_type = formationName;
        int aindex = ArrayUtils.indexOf(FormationTypeNameMap, formationName);
        int mythic = 0;
        if (mythics != null && mythics.containsKey(aindex)) {
            mythic = mythics.get(aindex);
        }
        ioBattleFormation.mythic = mythic;
        ioBattleFormation.items = new ArrayList<>(aFormation.size());
        for (Map.Entry<Integer, Long> aEntry : aFormation.entrySet()) {
            ioBattleFormation.items.add(new WsMessageBase.IOFormationGeneralPos(aEntry.getKey(), aEntry.getValue()));
        }
        return ioBattleFormation;
    }

    public Map<Integer, Long> getFormationByType(int ftypeIndex, BattleFormation battleFormation) {
        switch (ftypeIndex) {
            case 0:
                return battleFormation.getNormal();
            case 1:
                return battleFormation.getPre1();
            case 2:
                return battleFormation.getPre2();
            case 3:
                return battleFormation.getPvpatt();
            case 4:
                return battleFormation.getPvpdef();
            case 5:
                return battleFormation.getTower();
            case 6:
                return battleFormation.getExpedition();
            case 7:
                return battleFormation.getDungeon();
            case 8:
                return battleFormation.getTeampvp();
            case 9:
                return battleFormation.getKingpvp1();
            case 10:
                return battleFormation.getKingpvp2();
            case 11:
                return battleFormation.getKingpvp3();
        }
        return null;
    }

    public void setFormationByType(int ftypeIndex, BattleFormation battleFormation, Map<Integer, Long> formationMap) {
        switch (ftypeIndex) {
            case 0:
                battleFormation.setNormal(formationMap);
                break;
            case 1:
                battleFormation.setPre1(formationMap);
                break;
            case 2:
                battleFormation.setPre2(formationMap);
                break;
            case 3:
                battleFormation.setPvpatt(formationMap);
                break;
            case 4:
                battleFormation.setPvpdef(formationMap);
                break;
            case 5:
                battleFormation.setTower(formationMap);
                break;
            case 6:
                battleFormation.setExpedition(formationMap);
                break;
            case 7:
                battleFormation.setDungeon(formationMap);
                break;
            case 8:
                battleFormation.setTeampvp(formationMap);
                break;
            case 9:
                battleFormation.setKingpvp1(formationMap);
                break;
            case 10:
                battleFormation.setKingpvp2(formationMap);
                break;
            case 11:
                battleFormation.setKingpvp3(formationMap);
                break;
        }
    }

    public static final String[] FormationTypeNameMap = {"normal", "pre1", "pre2", "pvpatt", "pvpdef", "tower", "expedition", "dungeon", "teampvp", "kingpvp1",
            "kingpvp2", "kingpvp3"};

}
