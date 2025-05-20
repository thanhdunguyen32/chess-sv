package game.module.hero.logic;

import game.module.affair.logic.AffairManager;

import java.util.HashMap;
import java.util.Map;

public class GeneralConstants {

    public static final Map<String, AffairManager.CondTypeBean> GENERAL_COM_CONDS = new HashMap<>();

    static {
        GENERAL_COM_CONDS.put("1001", new AffairManager.CondTypeBean("star", 1));
        GENERAL_COM_CONDS.put("1002", new AffairManager.CondTypeBean("star", 2));
        GENERAL_COM_CONDS.put("1003", new AffairManager.CondTypeBean("star", 3));
        GENERAL_COM_CONDS.put("1004", new AffairManager.CondTypeBean("star", 4));
        GENERAL_COM_CONDS.put("1005", new AffairManager.CondTypeBean("star", 5));
        GENERAL_COM_CONDS.put("1006", new AffairManager.CondTypeBean("star", 6));
        GENERAL_COM_CONDS.put("1007", new AffairManager.CondTypeBean("star", 7));
        GENERAL_COM_CONDS.put("1008", new AffairManager.CondTypeBean("star", 8));
        GENERAL_COM_CONDS.put("1009", new AffairManager.CondTypeBean("star", 9));
        GENERAL_COM_CONDS.put("1010", new AffairManager.CondTypeBean("star", 10));
        GENERAL_COM_CONDS.put("1011", new AffairManager.CondTypeBean("star", 11));
        GENERAL_COM_CONDS.put("1012", new AffairManager.CondTypeBean("star", 12));
        GENERAL_COM_CONDS.put("1013", new AffairManager.CondTypeBean("star", 13));
        GENERAL_COM_CONDS.put("2001", new AffairManager.CondTypeBean("camp", 1));
        GENERAL_COM_CONDS.put("2002", new AffairManager.CondTypeBean("camp", 2));
        GENERAL_COM_CONDS.put("2003", new AffairManager.CondTypeBean("camp", 3));
        GENERAL_COM_CONDS.put("2004", new AffairManager.CondTypeBean("camp", 4));
        GENERAL_COM_CONDS.put("2005", new AffairManager.CondTypeBean("camp", 5));
        GENERAL_COM_CONDS.put("2006", new AffairManager.CondTypeBean("camp", 6));
        GENERAL_COM_CONDS.put("3001", new AffairManager.CondTypeBean("occu", 1));
        GENERAL_COM_CONDS.put("3002", new AffairManager.CondTypeBean("occu", 2));
        GENERAL_COM_CONDS.put("3003", new AffairManager.CondTypeBean("occu", 3));
        GENERAL_COM_CONDS.put("3004", new AffairManager.CondTypeBean("occu", 4));
        GENERAL_COM_CONDS.put("3005", new AffairManager.CondTypeBean("occu", 5));
    }

    public static final int[] WEI_SHU_WU_QUN_RATE = {6284, 1389, 1389, 778, 130, 21};

    public static final int[] DI_MO_RATE = {5340, 2140, 800, 800, 805, 64, 11};

    public static final int[] GENERAL_EXCHANGE_5STAR_RATE = {6276, 1046, 167};

    public static final int GENERAL_RESET_YB = 20;

    public static final int[] GENERAL_REBORN_GSID_COUNT = {2, 2, 2, 3, 5, 6, 6};

    public static final int[][] GENERAL_REBORN_RANDOM_GSID = {{5, 4}, {5, 8}, {5, 11, 6, 1}, {5, 13, 6, 2}, {5, 13, 6, 3, 9, 1}, {5, 13, 6, 3, 9, 2}, {5, 13, 6, 4, 9, 3}};

    public static final int WU_HUN_COUNT_MARK = 90341;

}
