package lion.netty4.codec;

import java.util.List;

import com.google.protobuf.CodedInputStream;
import com.google.protobuf.CodedOutputStream;
import com.google.protobuf.Message;
import com.google.protobuf.MessageLite;

/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import lion.common.DataEncryption;
import lion.netty4.message.RequestProtoMessage;

/**
 * Decodes a received {@link ByteBuf} into a <a href="http://code.google.com/p/protobuf/">Google Protocol Buffers</a>
 * {@link Message} and {@link MessageLite}. Please note that this decoder must be used with a proper
 * {@link ByteToMessageDecoder} such as {@link ProtobufVarint32FrameDecoder} or {@link LengthFieldBasedFrameDecoder} if
 * you are using a stream-based transport such as TCP/IP. A typical setup for TCP/IP would be:
 * 
 * <pre>
 * {@link ChannelPipeline} pipeline = ...;
 * 
 * // Decoders
 * pipeline.addLast("frameDecoder",
 *                  new {@link LengthFieldBasedFrameDecoder}(1048576, 0, 4, 0, 4));
 * pipeline.addLast("ProtobufMsgCodeDecoder",
 *                  new {@link ProtobufMsgCodeDecoderEncrypt}(MyMessage.getDefaultInstance()));
 * 
 * // Encoder
 * pipeline.addLast("frameEncoder", new {@link LengthFieldPrepender}(4));
 * pipeline.addLast("protobufEncoder", new {@link ProtobufEncoder}());
 * </pre>
 * 
 * and then you can use a {@code MyMessage} instead of a {@link ByteBuf} as a message:
 * 
 * <pre>
 * void channelRead({@link ChannelHandlerContext} ctx, MyMessage req) {
 *     MyMessage res = MyMessage.newBuilder().setText(
 *                               "Did you say '" + req.getText() + "'?").build();
 *     ch.write(res);
 * }
 * </pre>
 */
@Sharable
public class ProtobufMsgCodeDecoderEncrypt extends MessageToMessageDecoder<ByteBuf> {

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
		byte[] array;
		int offset;
		int length = msg.readableBytes();
		if (msg.hasArray()) {
			array = msg.array();
			offset = msg.arrayOffset() + msg.readerIndex();
		} else {
			array = new byte[length];
			msg.getBytes(msg.readerIndex(), array, 0, length);
			offset = 0;
		}
		
		//decrypt,to remove
		byte[] decryptBytes = DataEncryption.decrypt(array, offset, length);

		int msgCode = CodedInputStream.newInstance(decryptBytes, 0, decryptBytes.length).readRawVarint32();
		int msgCodeLength = CodedOutputStream.computeUInt32SizeNoTag(msgCode);
		out.add(new RequestProtoMessage(msgCode, decryptBytes, msgCodeLength, decryptBytes.length - msgCodeLength));
	}

}
