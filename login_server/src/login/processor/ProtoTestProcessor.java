//package login.processor;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import lion.common.MsgCodeAnn;
//import lion.netty4.codec.ProtoUtil;
//import lion.netty4.message.GamePlayer;
//import lion.netty4.message.MyRequestMessage;
//import lion.netty4.message.RequestByteMessage;
//import lion.netty4.message.RequestProtoMessage;
//import lion.netty4.processor.MsgProcessor;
//import proto.ProtoMessageLogin.C2SProtoTest;
//import proto.ProtoMessageLogin.S2CProtoTest;
//
//@MsgCodeAnn(msgcode = 1001, accessLimit = 200)
//public class ProtoTestProcessor extends MsgProcessor {
//
//	private static Logger logger = LoggerFactory.getLogger(ProtoTestProcessor.class);
//
//	@Override
//	public void process(GamePlayer session, RequestByteMessage request) throws Exception {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void process(GamePlayer player, RequestProtoMessage request) throws Exception {
//		C2SProtoTest reqMsg = ProtoUtil.getProtoObj(C2SProtoTest.parser(), request);
//		String astr = reqMsg.getAstr();
//		logger.info("in protobuf message,req={},astr={},membes={}",reqMsg, astr, reqMsg.getMembersList());
//		//ret
//		S2CProtoTest.Builder builder = S2CProtoTest.newBuilder();
//		builder.setAbool(false).setAfloat(-56.12F).setAint(-781).setAlong(-7683920593923L).setAstr("astar打了个d3#$)_+");
//		builder.addMembers(66).addMembers(67).addMembers(68).addMembers(69);
//		int i=1;
//		S2CProtoTest respMsg = builder.build();
//		while(true) {
//			player.write(1002,respMsg);
//			Thread.sleep(300);
//			if(i%10==0) {
//				player.flush();
//			}
//			i++;
//		}
////		player.writeAndFlush(1002, builder.build());
////		Thread.sleep(1000);
////		player.writeAndFlush(1002, builder.build());
//	}
//
//	@Override
//	public void process(GamePlayer session, MyRequestMessage request) throws Exception {
//		int param1 = request.readInt();
//		int param2 = request.readInt();
//		logger.info("p1={},p2={}", param1, param2);
//	}
//	
//	public static void main(String[] args) throws Exception {
//		String s = "show\\346\\230\\257\\346\\210\\221\\345\\210\\253#4v";
//		logger.info("s1={}",byteToHex(s.getBytes("utf-8")));
//		
//		String s2 = "show是我别#4v";
//		logger.info("s2={}",byteToHex(s2.getBytes("utf-8")));
//		
//		C2SProtoTest reqMsg = C2SProtoTest.newBuilder().setAint(10086).setAlong(1204333532244339L).setAbool(true)
//				.setAfloat(3.14F).setAstr("show是我别#4v").build();
//		byte[] bytes = reqMsg.toByteArray();
//		logger.info("s2={}", byteToHex(bytes));
//		
//		C2SProtoTest reqMsg1 = C2SProtoTest.parseFrom(bytes);
////		String strs = reqMsg.getAstr();
//		logger.info("in protobuf message,req={}",reqMsg1.getAstr());
//	}
//	
//	/**
//     * byte数组转hex
//     * @param bytes
//     * @return
//     */
//    public static String byteToHex(byte[] bytes){
//        String strHex = "";
//        StringBuilder sb = new StringBuilder("");
//        for (int n = 0; n < bytes.length; n++) {
//            strHex = Integer.toHexString(bytes[n] & 0xFF);
//            sb.append((strHex.length() == 1) ? "0" + strHex : strHex); // 每个字节由两个字符表示，位数不够，高位补0
//            sb.append(" ");
//        }
//        return sb.toString().trim();
//    }
//
//}
