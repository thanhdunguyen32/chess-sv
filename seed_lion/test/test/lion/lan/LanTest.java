//package test.lion.lan;
//
//import lion.lan.LanClient;
//import lion.lan.LanClientSession;
//import lion.lan.LanServer;
//import lion.lan.Netty4LanHandler;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//public class LanTest {
//
//	private static Logger logger = LoggerFactory.getLogger(LanTest.class);
//
//	public static void main(String[] args) {
//		// init
//		Netty4LanHandler.setLanIoExecutor(new LanIoExecutor());
//		int serverPort = 9001;
//		LanServer ls = new LanServer(serverPort);
//		try {
//			ls.run();
//		} catch (Exception e) {
//			logger.info("", e);
//		}
//		LanClient lc = new LanClient();
//		LanClientSession lcs = new LanClientSession(lc);
//		lcs.connect("127.0.0.1", serverPort);
//		SayHelloSendToMsg sayHelloMsg = new SayHelloSendToMsg(lcs.alloc(), 10085);
//		sayHelloMsg.setMsg(8823, "灰机ssal3301");
//		lcs.writeAndFlush(sayHelloMsg);
//
//		try {
//			Thread.sleep(5000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//
//		ls.shutdown();
//		lcs.close();
//		lc.close();
//	}
//
//}
