package lion.netty4.message;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 此类是一个消息分发器，为不同的请求消息分配合适的处理器。<br/>
 * 内部使用一个{@link HashMap}来缓存消息处理器，试图对一个KEY绑定处理器进行覆盖操作是不会成功的。<br/>
 * 
 * @author serv_dev
 * 
 */
public class MsgDispatcher<T> {

	private static Logger logger = LoggerFactory.getLogger(MsgDispatcher.class);

	private Map<Integer, T> processors = null;

	public MsgDispatcher() {
		processors = new HashMap<Integer, T>();
	}

	/**
	 * 添加一个T实例，并和一个Integer绑定。<br/>
	 * 试图对一个KEY绑定处理器进行覆盖操作是不会成功的
	 * 
	 * @param name
	 *            key
	 * @param processor
	 *            value
	 */
	public void addMsgProcessor(Integer name, T processor) {
		processors.put(name, processor);
	}

	/**
	 * 根据给定的键取得绑定的处理器。
	 * 
	 * @param name
	 *            key
	 * @return 指定的键绑定的处理器
	 */
	public T getMsgProcessor(Integer name) {
		T msgProc = processors.get(name);
		if (msgProc == null) {
			logger.warn("can not find processor for:" + name);
		}
		return msgProc;
	}

	public Map<Integer, T> getMsgProccessor() {
		return processors;
	}

	public void dumpProcessors() {
		StringBuilder sb = new StringBuilder("dump IO message processors:\n");
		Set<Entry<Integer, T>> processorSet = processors.entrySet();
		List<Entry<Integer, T>> processorList = new ArrayList<Map.Entry<Integer, T>>();
		processorList.addAll(processorSet);
		Collections.sort(processorList, new Comparator<Entry<Integer, T>>() {
			@Override
			public int compare(Entry<Integer, T> o1, Entry<Integer, T> o2) {
				if (o1.getKey() > o2.getKey()) {
					return 1;
				} else if (o1.getKey() < o2.getKey()) {
					return -1;
				}
				return 0;
			}
		});
		for (Map.Entry<Integer, T> processorPair : processorList) {
			int msgCode = processorPair.getKey();
			sb.append(msgCode);
			sb.append("=");
			String clazz = processorPair.getValue().getClass().getName();
			sb.append(clazz);
			sb.append("\n");
		}
		logger.info(sb.toString());
	}

	public boolean containsMsgcode(int msgcode) {
		boolean ret = false;
		if (processors.containsKey(msgcode)) {
			ret = true;
		}
		return ret;
	}
}
