package game.module.dungeon.logic;

import java.util.Arrays;
import java.util.List;

public class DungeonConstants {

    public static final int[] DUNGEON_NODE_SIZE = {1, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 1};

    public static final int[][] DUNGEON_TYPE_RATE = {{1, 43}, {2, 57}, {4, 7}, {5, 16}, {6, 27}, {7, 7}};

    public static final int REWARD_POTION_ID = 101;

    public static final List<Integer> OPEN_TIME = Arrays.asList(1, 2, 5, 6);

}
