package lion.common;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BeanTool {

	private static Logger logger = LoggerFactory.getLogger(BeanTool.class);

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Map listToMap(List list, String properties, Class mapclass) {
		try {
			Map hashmap = (Map) mapclass.newInstance();
			for (Iterator iter = list.iterator(); iter.hasNext();) {
				Object item = iter.next();
				try {
					Object key = getPropValue(item, properties);
					hashmap.put(key, item);
				} catch (Exception ex) {
					throw new RuntimeException(ex.getMessage());
				}
			}
			return hashmap;
		} catch (InstantiationException e) {
			logger.error(e.getMessage(), e);
		} catch (IllegalAccessException e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	@SuppressWarnings({ "rawtypes" })
	public static Map listToMap(List list, String keyPropertiesName) {
		return listToMap(list, keyPropertiesName, HashMap.class);
	}

	private static String formatProperties(String name) {
		if (name.length() > 1) {
			if (Character.isLowerCase(name.charAt(0))) {// 小写
				if (Character.isLowerCase(name.charAt(1))) {// 小写
					return String.valueOf(name.charAt(0)).toUpperCase() + name.substring(1);
				} else {
					return String.valueOf(name.charAt(0)).toLowerCase() + name.substring(1);
				}
			} else {
				return name;
			}
		}
		return String.valueOf(name.charAt(0)).toUpperCase();
	}

	public static Object getPropValue(Object obj, String properties) throws Exception {
		String methodname = "get" + formatProperties(properties);
		Method method = obj.getClass().getMethod(methodname);
		Object keypropvalue = method.invoke(obj);
		return keypropvalue;
	}

	/**
	 * 
	 * 
	 * 
	 * @param oldmap
	 * @param newmap
	 * @param property
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void addOrUpdate(Map oldmap, Map newmap, String property) throws Exception {
		if (newmap == null) {
			return;
		}

		for (Object newobj : newmap.values()) {
			Object key = getPropValue(newobj, property);
			Object oldobj = oldmap.get(key);
			if (oldobj == null) {
				oldmap.put(key, newobj);
			} else {
				BeanUtils.copyProperties(newobj, oldobj);
			}
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void addOrUpdate(Map oldmap, Collection collection, String property) throws Exception {
		if (collection == null) {
			return;
		}
		for (Object newobj : collection) {
			Object key = getPropValue(newobj, property);
			Object oldobj = oldmap.get(key);
			if (oldobj == null) {
				oldmap.put(key, newobj);
			} else {
				BeanUtils.copyProperties(newobj, oldobj);
			}
		}
	}

	public static String getStackTraceInfo() {
		StackTraceElement[] ste = new Throwable().getStackTrace();
		StringBuilder sb = new StringBuilder();
		for (StackTraceElement stackTraceElement : ste) {
			sb.append(stackTraceElement);
			sb.append("\n");
		}
		return sb.toString();
	}
}
