package game.module.bigbattle.logic;

public class BigBattleConstants {

    public static final BigBattleCountGsid[] BIG_BATTLE_COUNT_CONFIG = {new BigBattleCountGsid(90021, 2, 90268),
            new BigBattleCountGsid(90022, 2, 90269), new BigBattleCountGsid(90023, 2, 90270)};

    public static final int COST_YB = 50;

    public static final class BigBattleCountGsid {
        public Integer FREE;
        public Integer COUNTLIMIT;
        public Integer COUNTGSID;

        public BigBattleCountGsid(Integer FREE, Integer COUNTLIMIT, Integer COUNTGSID) {
            this.FREE = FREE;
            this.COUNTLIMIT = COUNTLIMIT;
            this.COUNTGSID = COUNTGSID;
        }
    }

    public static int getTypeIndexByMapId(int mapid){
        mapid /= 100;
        mapid %= 10;
        return mapid;
    }

    public static final int MONTH_BOSS_SIZE = 3;

}
