package game.common.json;

import com.fasterxml.jackson.core.type.TypeReference;

public class Int2ListType extends TypeReference<int[][]> {

    public static final Int2ListType INSTANCE = new Int2ListType();

}
