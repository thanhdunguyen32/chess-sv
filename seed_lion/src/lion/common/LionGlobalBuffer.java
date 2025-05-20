package lion.common;

import io.protostuff.LinkedBuffer;

public class LionGlobalBuffer {

	private static final ThreadLocal<LinkedBuffer> buffers = new ThreadLocal<LinkedBuffer>();

	/**
	 * 每个线程有自己的buffer，可以共享
	 * 
	 * @return
	 */
	public static LinkedBuffer getProtoBuffer() {
		LinkedBuffer buffer = buffers.get();
		if (buffer == null) {
			buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
			buffers.set(buffer);
		}
		buffer.clear();
		return buffer;
	}

}
