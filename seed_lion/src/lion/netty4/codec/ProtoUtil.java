package lion.netty4.codec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.MessageLite;
import com.google.protobuf.Parser;

import lion.netty4.message.ClientInputProtoMessage;
import lion.netty4.message.RequestProtoMessage;

public class ProtoUtil {

	private static Logger logger = LoggerFactory.getLogger(ProtoUtil.class);

	public static <T extends MessageLite> T getProtoObj(Parser<T> aParser, RequestProtoMessage requestProtoMessage) {
		if (requestProtoMessage.getLength() == 0) {
			return null;
		}
		try {
			return aParser.parseFrom(requestProtoMessage.getArray(), requestProtoMessage.getOffset(),
					requestProtoMessage.getLength());
		} catch (InvalidProtocolBufferException e) {
			logger.error("", e);
		}
		return null;
	}

	public static <T extends MessageLite> T getProtoObj(Parser<T> aParser,
			ClientInputProtoMessage clientInputProtoMessage) {
		if (clientInputProtoMessage.getLength() == 0) {
			return null;
		}
		try {
			return aParser.parseFrom(clientInputProtoMessage.getDatas(), clientInputProtoMessage.getOffset(),
					clientInputProtoMessage.getLength());
		} catch (InvalidProtocolBufferException e) {
			logger.error("", e);
		}
		return null;
	}

}
