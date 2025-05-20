package lion.common;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.Channel;

public class ProcessorStatManager {

	private static Logger logger = LoggerFactory.getLogger(ProcessorStatManager.class);

	static class SingletonHolder {
		static ProcessorStatManager instance = new ProcessorStatManager();
	}

	private Map<Integer, Long> startTimeCache = new ConcurrentHashMap<Integer, Long>();

	private Map<ChannelMsgcodePair, Long> startTimeMap = new ConcurrentHashMap<ChannelMsgcodePair, Long>();

	public static ProcessorStatManager getInstance() {
		return SingletonHolder.instance;
	}

	public void startLog(int processorId, long startTime) {
		startTimeCache.put(processorId, startTime);
	}

	public void endStatLog(int processorId, long endTime) {
		Long startTime = startTimeCache.get(processorId);
		if (startTime != null) {
			int timeDiff = (int) (endTime - startTime);
			logger.warn("processor:{},cost={}ms", processorId, timeDiff);
			// logMaxTime(processorId,timeDiff);
			// Integer maxProcessorTime = maxProcessorTimeCache.get(processorId);

		}
	}

	public void logStart(int msgCode, Channel channel, Long startTime) {
		startTimeMap.put(new ChannelMsgcodePair(msgCode, channel), startTime);
	}

	public void logEnd(int msgCode, Channel channel) {
		long currentTime = System.currentTimeMillis();
		ChannelMsgcodePair aKey = new ChannelMsgcodePair(msgCode - 1, channel);
		Long aLog = startTimeMap.get(aKey);
		if (aLog != null) {
			logger.info("process={},cost={}ms", msgCode - 1, currentTime - aLog);
			startTimeMap.remove(aKey);
		}
	}

	public Map<ChannelMsgcodePair, Long> getStartTimeMap() {
		return startTimeMap;
	}

	public static final class ChannelMsgcodePair {

		private int msgCode;
		private Channel channel;

		public ChannelMsgcodePair(int msgCode, Channel channel) {
			super();
			this.msgCode = msgCode;
			this.channel = channel;
		}

		public int getMsgCode() {
			return msgCode;
		}

		public void setMsgCode(int msgCode) {
			this.msgCode = msgCode;
		}

		public Channel getChannel() {
			return channel;
		}

		public void setChannel(Channel channel) {
			this.channel = channel;
		}

		@Override
		public int hashCode() {
			int result = 1;
			result = 31 * result + (channel == null ? 0 : channel.hashCode());
			result = 31 * result + msgCode;

			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			} else {
				ChannelMsgcodePair otherObj = (ChannelMsgcodePair) obj;
				if (this.msgCode == otherObj.msgCode && this.channel == otherObj.channel) {
					return true;
				}
			}
			return false;
		}

		@Override
		public String toString() {
			return "msgCode=" + msgCode + ";channel=" + channel;
		}

	}

}
