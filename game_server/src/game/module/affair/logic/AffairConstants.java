package game.module.affair.logic;

public class AffairConstants {

    public static final int[] AFFAIR_COST_HOUR = new int[]{1, 2, 4, 6, 8, 12, 16};

    public static final int[] AFFAIR_GENERAL_NUM = new int[]{1, 2, 2, 3, 3, 4, 4};

    public static final int AFFAIR_STATUS_NEW = 0;

    public static final int AFFAIR_STATUS_PROGRESS = 1;

    public static final int AFFAIR_STATUS_FINISH = 2;

    public static final int[] AFFAIR_SCROLL = {30021, 30022, 30030, 30031};

    public static final int[][] AFFAIR_REFRESH_RATE = {{1, 10}, {2, 20}, {3, 30}, {4, 30}, {5, 30}, {6, 10}, {7, 2}};

    public static final int INIT_AFFAIR_COUNT = 5;

    public static final int AFFAIR_REFRESH_PER_COST = 10;

    public static final int[] COND_NUM = {1, 1, 1, 2, 2, 3, 3};

    public static final int[] COND_STAR = {0, 0, 3, 4, 5, 5, 6};

    public static final int[] AFFAIR_CONDS = {1, 2, 3, 4, 5, 6, 7, 8, 9};

    public static final AffairManager.CondTypeBean[] COND_TYPES = {new AffairManager.CondTypeBean("camp", 1), new AffairManager.CondTypeBean("camp", 2),
            new AffairManager.CondTypeBean("camp", 3), new AffairManager.CondTypeBean("camp", 4), new AffairManager.CondTypeBean("occu", 1),
            new AffairManager.CondTypeBean("occu", 2), new AffairManager.CondTypeBean("occu", 3), new AffairManager.CondTypeBean("occu", 4),
            new AffairManager.CondTypeBean("occu", 5)};

}
