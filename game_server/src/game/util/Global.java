package game.util;

import io.protostuff.LinkedBuffer;

public final class Global {

	private static final ThreadLocal<LinkedBuffer> buffers = new ThreadLocal<LinkedBuffer>();

	/**
	 * 每个线程有自己的buffer，可以共享
	 * @return
	 */
	public static LinkedBuffer getProtoBuffer() {
		LinkedBuffer buffer = buffers.get();
		if (buffer == null) {
			buffer = LinkedBuffer.allocate(8192);
			buffers.set(buffer);
		}
		buffer.clear();
		return buffer;
	}

}
