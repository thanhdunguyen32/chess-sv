package lion.netty4.message;

import com.google.protobuf.CodedOutputStream;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.Unpooled;
import io.netty.util.ReferenceCountUtil;

import java.io.IOException;

public class MySendToMessage {

    private CodedOutputStream codedOutputStream;

    private ByteBuf buffer;

    private int msgCode = 0;

    private Integer markReaderIndex;

    public MySendToMessage(ByteBufAllocator alloc, int pMsgCode) throws IOException {
        buffer = alloc.buffer(16);
        codedOutputStream = CodedOutputStream.newInstance(new ByteBufOutputStream(buffer), 32);
        //
        codedOutputStream.writeInt32NoTag(pMsgCode);
        this.msgCode = pMsgCode;
    }

    public ByteBuf entireMsg() throws IOException {
        if (markReaderIndex == null) {
            codedOutputStream.flush();
            markReaderIndex = buffer.readerIndex();
            return buffer;
        }
        //群发消息处理
        return buffer.copy(markReaderIndex, buffer.writerIndex() - markReaderIndex);
    }

    public MySendToMessage writeBool(boolean b) throws IOException {
        codedOutputStream.writeBoolNoTag(b);
        return this;
    }

    public MySendToMessage writeInt(int i) throws IOException {
        codedOutputStream.writeInt32NoTag(i);
        return this;
    }

    public MySendToMessage writeLong(long i) throws IOException {
        codedOutputStream.writeInt64NoTag(i);
        return this;
    }

    public MySendToMessage writeByte(byte i) throws IOException {
        codedOutputStream.writeRawByte(i);
        return this;
    }

    public MySendToMessage writeFloat(float val) throws IOException {
        codedOutputStream.writeFloatNoTag(val);
        return this;
    }

    public int getMsgCode() {
        return msgCode;
    }

    public void setMsgCode(int msgCode) {
        this.msgCode = msgCode;
    }

    public MySendToMessage writeString(String str) throws IOException {
        if (str == null) {
            str = "";
        }
        codedOutputStream.writeStringNoTag(str);
        return this;
    }

    public MySendToMessage writeBytes(byte[] bytes) throws IOException {
        codedOutputStream.writeRawBytes(bytes);
        return this;
    }

    public void release() {
        ReferenceCountUtil.release(buffer);
    }

    public static void main(String[] args) {
        ByteBuf buffer = Unpooled.buffer(10);
        for (int i = 0; i < 10; i++) {
            buffer.writeByte(i);
        }
        for (int i = 0; i < 5; i++) {
            buffer.readByte();
        }
        System.out.println(buffer.readerIndex());
        System.out.println(buffer.writerIndex());
        ByteBuf copyBuf = buffer.copy(3, 7);
        System.out.println(copyBuf.readerIndex());
        System.out.println(copyBuf.writerIndex());

    }

}
