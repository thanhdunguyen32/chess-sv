package game.common;

import org.apache.commons.lang3.ArrayUtils;
import ws.WsMessageBase;
import ws.WsMessageHero;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class IoBeanConvertor {

    public static List<WsMessageBase.KvStringPair> map2Pairs(Map<String,Integer> map){
        List<WsMessageBase.KvStringPair> retlist = new ArrayList<>(map.size());
        for(Map.Entry<String,Integer> aEntry : map.entrySet()){
            retlist.add(new WsMessageBase.KvStringPair(aEntry.getKey(),aEntry.getValue()));
        }
        return retlist;
    }

//    public static int[] intList2Primitive(List<Integer> integerList){
//        return ArrayUtils.toPrimitive(integerList.toArray(new Integer[]{}));
//    }
//
//    public static long[] longList2Primitive(List<Long> integerList){
//        return ArrayUtils.toPrimitive(integerList.toArray(new Long[]{}));
//    }
}
