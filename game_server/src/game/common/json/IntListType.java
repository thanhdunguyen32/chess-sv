package game.common.json;

import com.fasterxml.jackson.core.type.TypeReference;

public class IntListType extends TypeReference<int[]> {

    public static final IntListType INSTANCE = new IntListType();

}
