package lion.common;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Arrays;

import javax.sql.rowset.serial.SerialException;

public class MyBlob implements Blob {

	private byte[] buf;

	private Blob blob;

	private long len;

	private long origLen;

	public MyBlob(byte[] pbuf) {
		this.buf = pbuf;
		this.len = pbuf.length;
		this.origLen = pbuf.length;
	}

	@Override
	public long length() throws SQLException {
		return this.len;
	}

	@Override
	public byte[] getBytes(long pos, int length) throws SQLException {
		isValid();
		if (length > len) {
			length = (int) len;
		}

		if (pos < 1 || len - pos < 0) {
			throw new SerialException(
					"Invalid arguments: position cannot be " + "less than 1 or greater than the length of the MyBlob");
		}

		pos--; // correct pos to array index
		if(pos == 0 && length == len){
			return buf;
		}
		byte[] b = new byte[length];

		for (int i = 0; i < length; i++) {
			b[i] = this.buf[(int) pos];
			pos++;
		}
		return b;
	}

	@Override
	public InputStream getBinaryStream() throws SQLException {
		isValid();
		InputStream stream = new ByteArrayInputStream(buf);
		return stream;
	}

	@Override
	public long position(byte[] pattern, long start) throws SQLException {
		isValid();
		if (start < 1 || start > len) {
			return -1;
		}

		int pos = (int) start - 1; // internally Blobs are stored as arrays.
		int i = 0;
		long patlen = pattern.length;

		while (pos < len) {
			if (pattern[i] == buf[pos]) {
				if (i + 1 == patlen) {
					return (pos + 1) - (patlen - 1);
				}
				i++;
				pos++; // increment pos, and i
			} else if (pattern[i] != buf[pos]) {
				pos++; // increment pos only
			}
		}
		return -1; // not found
	}

	@Override
	public long position(Blob pattern, long start) throws SQLException {
		isValid();
		return position(pattern.getBytes(1, (int) (pattern.length())), start);
	}

	@Override
	public int setBytes(long pos, byte[] bytes) throws SQLException {
		return setBytes(pos, bytes, 0, bytes.length);
	}

	@Override
	public int setBytes(long pos, byte[] bytes, int offset, int length) throws SQLException {
		isValid();
		if (offset < 0 || offset > bytes.length) {
			throw new SQLException("Invalid offset in byte array set");
		}

		if (pos < 1 || pos > this.length()) {
			throw new SQLException("Invalid position in BLOB object set");
		}

		if ((long) (length) > origLen) {
			throw new SQLException("Buffer is not sufficient to hold the value");
		}

		if ((length + offset) > bytes.length) {
			throw new SQLException(
					"Invalid OffSet. Cannot have combined offset " + "and length that is greater that the Blob buffer");
		}

		int i = 0;
		pos--; // correct to array indexing
		while (i < length || (offset + i + 1) < (bytes.length - offset)) {
			this.buf[(int) pos + i] = bytes[offset + i];
			i++;
		}
		return i;
	}

	@Override
	public OutputStream setBinaryStream(long pos) throws SQLException {
		isValid();
		if (this.blob != null) {
			return this.blob.setBinaryStream(pos);
		} else {
			throw new SQLException("Unsupported operation. MyBlob cannot "
					+ "return a writable binary stream, unless instantiated with a Blob object "
					+ "that provides a setBinaryStream() implementation");
		}
	}

	@Override
	public void truncate(long length) throws SQLException {
		isValid();
		if (length > len) {
			throw new SerialException("Length more than what can be truncated");
		} else if ((int) length == 0) {
			buf = new byte[0];
			len = length;
		} else {
			len = length;
			buf = this.getBytes(1, (int) len);
		}
	}

	@Override
	public void free() throws SQLException {
		if (buf != null) {
			buf = null;
			if (blob != null) {
				blob.free();
			}
			blob = null;
		}
	}

	@Override
	public InputStream getBinaryStream(long pos, long length) throws SQLException {
		isValid();
		if (pos < 1 || pos > this.length()) {
			throw new SerialException("Invalid position in BLOB object set");
		}
		if (length < 1 || length > len - pos + 1) {
			throw new SerialException("length is < 1 or pos + length > total number of bytes");
		}
		return new ByteArrayInputStream(buf, (int) pos - 1, (int) length);
	}

	private void isValid() throws SQLException {
		if (buf == null) {
			throw new SQLException(
					"Error: You cannot call a method on a " + "MyBlob instance once free() has been called.");
		}
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj instanceof MyBlob) {
			MyBlob sb = (MyBlob) obj;
			if (this.len == sb.len) {
				return Arrays.equals(buf, sb.buf);
			}
		}
		return false;
	}

	/**
	 * Returns a hash code for this {@code MyBlob}.
	 * 
	 * @return a hash code value for this object.
	 */
	public int hashCode() {
		return ((31 + Arrays.hashCode(buf)) * 31 + (int) len) * 31 + (int) origLen;
	}

	/**
	 * Returns a clone of this {@code MyBlob}. The copy will contain a reference
	 * to a clone of the internal byte array, not a reference to the original
	 * internal byte array of this {@code MyBlob} object. The underlying
	 * {@code Blob} object will be set to null.
	 *
	 * @return a clone of this MyBlob
	 */
	public Object clone() {
		try {
			MyBlob sb = (MyBlob) super.clone();
			sb.buf = (buf != null) ? Arrays.copyOf(buf, (int) len) : null;
			sb.blob = null;
			return sb;
		} catch (CloneNotSupportedException ex) {
			// this shouldn't happen, since we are Cloneable
			throw new InternalError();
		}
	}

}
