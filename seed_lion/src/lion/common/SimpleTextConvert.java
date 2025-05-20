package lion.common;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class SimpleTextConvert {

    @SafeVarargs
    public static <T> List<T> asList(T... a) {
        List<T> retList = new ArrayList<T>(a.length);
        for (T aT : a) {
            retList.add(aT);
        }
        return retList;
    }

    public static int[] intObj2Raw(Integer[] intArray) {
        if (intArray == null) {
            return null;
        }
        int arraySize = intArray.length;
        int[] retlist = new int[arraySize];
        for (int i = 0; i < arraySize; i++) {
            retlist[i] = intArray[i];
        }
        return retlist;
    }

    public static List<Integer> decodeIntList(String int_list_str) {
        List<Integer> retlist = null;
        if (StringUtils.isNotBlank(int_list_str)) {
            String[] id_list_strs = StringUtils.split(int_list_str, StringConstants.SEPARATOR_DOU);
            retlist = new ArrayList<>(id_list_strs.length);
            for (String id_str : id_list_strs) {
                retlist.add(Integer.valueOf(id_str));
            }
        }
        return retlist;
    }

    public static List<List<Integer>> decodeIntListList(String int_list_str) {
        List<List<Integer>> retlist = null;
        if (StringUtils.isNotBlank(int_list_str)) {
            String[] id_list_strs = StringUtils.split(int_list_str, StringConstants.SEPARATOR_DI);
            retlist = new ArrayList<>(id_list_strs.length);
            for (String id_str : id_list_strs) {
                String[] id_list_strs2 = StringUtils.split(id_str, StringConstants.SEPARATOR_DOU);
                List<Integer> list2 = new ArrayList<>();
                for (String id_str2 : id_list_strs2) {
                    list2.add(Integer.valueOf(id_str2));
                }
                retlist.add(list2);
            }
        }
        return retlist;
    }

    public static Map<Integer, Integer> decodeIntMap(String int_pair_str) {
        Map<Integer, Integer> ret = null;
        if (StringUtils.isNotBlank(int_pair_str)) {
            String[] id_list_strs = StringUtils.split(int_pair_str, StringConstants.SEPARATOR_DOU);
            ret = new HashMap<>(id_list_strs.length / 2);
            for (int i = 0; i < id_list_strs.length; i += 2) {
                ret.put(Integer.valueOf(id_list_strs[i]), Integer.valueOf(id_list_strs[i + 1]));
            }
        }
        return ret;
    }

    public static Map<String, Integer> decodeStrIntMap(String int_pair_str) {
        Map<String, Integer> ret = null;
        if (StringUtils.isNotBlank(int_pair_str)) {
            String[] id_list_strs = StringUtils.split(int_pair_str, StringConstants.SEPARATOR_DOU);
            ret = new HashMap<>(id_list_strs.length / 2);
            for (int i = 0; i < id_list_strs.length; i += 2) {
                ret.put(id_list_strs[i], Integer.valueOf(id_list_strs[i + 1]));
            }
        }
        return ret;
    }

    public static Map<Long, Integer> decodeLongMap(String int_pair_str) {
        Map<Long, Integer> ret = null;
        if (StringUtils.isNotBlank(int_pair_str)) {
            String[] id_list_strs = StringUtils.split(int_pair_str, StringConstants.SEPARATOR_DOU);
            ret = new HashMap<>(id_list_strs.length / 2);
            for (int i = 0; i < id_list_strs.length; i += 2) {
                ret.put(Long.valueOf(id_list_strs[i]), Integer.valueOf(id_list_strs[i + 1]));
            }
        }
        return ret;
    }

    public static Map<Integer, Long> decodeIntLongMap(String int_pair_str) {
        Map<Integer, Long> ret = null;
        if (StringUtils.isNotBlank(int_pair_str)) {
            String[] id_list_strs = StringUtils.split(int_pair_str, StringConstants.SEPARATOR_DOU);
            ret = new HashMap<>(id_list_strs.length / 2);
            for (int i = 0; i < id_list_strs.length; i += 2) {
                ret.put(Integer.valueOf(id_list_strs[i]), Long.valueOf(id_list_strs[i + 1]));
            }
        }
        return ret;
    }

    public static Map<Long, Long> decodeLongLongMap(String int_pair_str) {
        Map<Long, Long> ret = null;
        if (StringUtils.isNotBlank(int_pair_str)) {
            String[] id_list_strs = StringUtils.split(int_pair_str, StringConstants.SEPARATOR_DOU);
            ret = new HashMap<>(id_list_strs.length / 2);
            for (int i = 0; i < id_list_strs.length; i += 2) {
                ret.put(Long.valueOf(id_list_strs[i]), Long.valueOf(id_list_strs[i + 1]));
            }
        }
        return ret;
    }

    public static String encodeIntListList(List<List<Integer>> idList) {
        String ret = null;
        StringBuilder sb = new StringBuilder();
        if (idList != null && idList.size() > 0) {
            int i=0;
            for(List<Integer> aPair : idList){
                if(i>0){
                    sb.append(StringConstants.SEPARATOR_DI);
                }
                sb.append(StringUtils.join(aPair.iterator(), StringConstants.SEPARATOR_DOU));
                i++;
            }
            ret = sb.toString();
        }
        return ret;
    }

    public static <K,T> String encodeMap(Map<K, T> idMap) {
        String ret = null;
        if (idMap != null && idMap.size() > 0) {
            StringBuilder sb = new StringBuilder();
            int idx = 0;
            for (Map.Entry<K, T> aEntry : idMap.entrySet()) {
                if (idx > 0) {
                    sb.append(StringConstants.SEPARATOR_DOU);
                }
                sb.append(aEntry.getKey()).append(StringConstants.SEPARATOR_DOU).append(aEntry.getValue());
                idx++;
            }
            ret = sb.toString();
        }
        return ret;
    }

    public static void main(String[] args) {
        List<Integer> alist = new ArrayList<>();
        alist.add(123);
        alist.add(111);
        String ret = StringUtils.join(alist.iterator(), StringConstants.SEPARATOR_DOU);
        System.out.println(ret);
    }

    public static Set<Integer> decodeIntSet(String int_set_str) {
        Set<Integer> retlist = null;
        if (StringUtils.isNotBlank(int_set_str)) {
            String[] id_list_strs = StringUtils.split(int_set_str, StringConstants.SEPARATOR_DOU);
            retlist = new HashSet<>(id_list_strs.length);
            for (String id_str : id_list_strs) {
                retlist.add(Integer.valueOf(id_str));
            }
        }
        return retlist;
    }

    public static List<Long> decodeLongList(String long_list_str) {
        List<Long> retlist = null;
        if (StringUtils.isNotBlank(long_list_str)) {
            String[] id_list_strs = StringUtils.split(long_list_str, StringConstants.SEPARATOR_DOU);
            retlist = new ArrayList<>(id_list_strs.length);
            for (String id_str : id_list_strs) {
                retlist.add(Long.valueOf(id_str));
            }
        }
        return retlist;
    }

    public static <T> String encodeCollection(Collection<T> longList) {
        String ret = null;
        if (longList != null && longList.size() > 0) {
            ret = StringUtils.join(longList.iterator(), StringConstants.SEPARATOR_DOU);
        }
        return ret;
    }
}
