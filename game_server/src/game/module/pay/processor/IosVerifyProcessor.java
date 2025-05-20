package game.module.pay.processor;

import game.GameServer;
import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.pay.dao.PaymentLogDao;
import game.module.pay.logic.PaymentManager;
import lion.common.JacksonUtils;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sdk.iap.IOSVerify;
import ws.WsMessageHall.C2SIosIAPVerify;
import ws.WsMessageHall.PushIosIAPVerify;
import ws.WsMessageHall.S2CIosIAPVerify;

import java.util.Map;

@MsgCodeAnn(msgcode = C2SIosIAPVerify.id, accessLimit = 200)
public class IosVerifyProcessor extends PlayingRoleMsgProcessor {

	private static Logger logger = LoggerFactory.getLogger(IosVerifyProcessor.class);

	@Override
	public void processByte(PlayingRole playingRole, RequestByteMessage requestMessage) throws Exception {

	}

	@Override
	public void processProto(final PlayingRole playingRole, RequestProtoMessage request) throws Exception {
		
	}

	public static String getIpAddr(String ipAddrRaw) {
		String retStr = ipAddrRaw;
		int fenIndex = ipAddrRaw.indexOf(":");
		if (fenIndex >= 0) {
			retStr = retStr.substring(0, fenIndex);
		}
		if (retStr.startsWith("/")) {
			retStr = retStr.substring(1);
		}
		return retStr;
	}
	public static void main(String[] args) {
		String vipProduceStr = "cn.xxcaifu.chengzhang1001";
		String ret = vipProduceStr.substring(vipProduceStr.lastIndexOf(".") + 1);
		System.out.println(ret);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void processMy(PlayingRole playingRole, MyRequestMessage request) throws Exception {
		C2SIosIAPVerify iosVerifyMsg = C2SIosIAPVerify.parse(request);
		final String receipt = iosVerifyMsg.receipt;
		final String openId = playingRole.getPlayerBean().getAccountId();
		final int serverId = playingRole.getPlayerBean().getServerId();
		final int playerId = playingRole.getId();
		String ipAddrRaw = playingRole.getGamePlayer().getAddress();
		final String ipAddr = getIpAddr(ipAddrRaw);
		logger.info("ios verify,playerId={},ipAddr={}", playerId, ipAddr);
		// String md5_receipt = IOSVerify.md5(receipt);
		S2CIosIAPVerify respMsg = new S2CIosIAPVerify();
		playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
		GameServer.executorService.execute(new Runnable() {
			public void run() {
				try {
					String verifyResult = IOSVerify.buyAppVerify(receipt,
							GameServer.getInstance().getServerConfig().isIosIapIsSandbox());
					logger.info("verifyResult={}", verifyResult);
					// {"receipt":{"original_purchase_date_pst":"2016-03-30
					// 18:59:03
					// America/Los_Angeles", "purchase_date_ms":"1459389543146",
					// "unique_identifier":"927e4e26aeb27d3bee73e32a5923838b432ea784",
					// "original_transaction_id":"1000000202536175",
					// "bvrs":"1.0",
					// "transaction_id":"1000000202536175", "quantity":"1",
					// "unique_vendor_identifier":"8104B566-30F0-4DC1-8B45-DCA0703B4447",
					// "item_id":"1098078290",
					// "product_id":"cn.xxcaifu.diamond001",
					// "purchase_date":"2016-03-31 01:59:03 Etc/GMT",
					// "original_purchase_date":"2016-03-31 01:59:03 Etc/GMT",
					// "purchase_date_pst":"2016-03-30 18:59:03
					// America/Los_Angeles", "bid":"com.xxcaifu.game",
					// "original_purchase_date_ms":"1459389543146"}, "status":0}
					if (verifyResult == null) {
						// 苹果服务器没有返回验证结果
						PushIosIAPVerify respMsg = new PushIosIAPVerify(801);
						playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
					} else {
						// 跟苹果验证有返回结果------------------
						Map<String, Object> job = JacksonUtils.getInstance().readValue(verifyResult, Map.class);
						Integer status = (Integer) job.get("status");
						if (status == 0)// 验证成功
						{
							Map<String, Object> returnJson = (Map<String, Object>) job.get("receipt");
							// 产品ID TODO 判断是否是我们产品
							String product_id = (String) returnJson.get("product_id");
							// 数量
							String quantity = (String) returnJson.get("quantity");
							// TODO 判断Transaction Identifier是否已经存在
							String transaction_id = (String) returnJson.get("transaction_id");
							// 交易日期
							String purchase_date = (String) returnJson.get("purchase_date");
							// 订单是否存在
							boolean orderExist = PaymentLogDao.getInstance().checkOrderExist(transaction_id);
							if (orderExist) {
								logger.info("ios payment,order exist1,playerId={},orderid={}", playingRole.getId(),
										transaction_id);
								PushIosIAPVerify respMsg = new PushIosIAPVerify(803);
								playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
								return;
							}
							// 商品是否存在
							if (!product_id.startsWith("com.taoyuan.usbiot")
									&& !product_id.startsWith("com.tyjq.brother")
									&& !product_id.startsWith("cn.lejinet.mygame")) {
								PushIosIAPVerify respMsg = new PushIosIAPVerify(804);
								playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
								return;
							}
							//
							product_id = product_id.substring(product_id.lastIndexOf(".") + 1);
//							IosRechargeTemplate iosRechargeTemplate = IosRechargeTemplateCache.getInstance()
//									.getIosRechargeTemplateById(product_id);
//							if (iosRechargeTemplate == null) {
//								PushIosIAPVerify respMsg = new PushIosIAPVerify(804);
//								playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
//								return;
//							}
							if (PaymentManager.getInstance().checkOrderExist(transaction_id)) {
								logger.info("ios payment,order exist2,playerId={},orderid={}", playingRole.getId(),
										transaction_id);
								PushIosIAPVerify respMsg = new PushIosIAPVerify(803);
								playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
								return;
							}
							// 保存log
							PaymentManager.getInstance().addOrderId(transaction_id);
							//do
//							PaymentManager.getInstance().payCallback(null, openId, transaction_id,
//									iosRechargeTemplate.getYuan(), (int) (System.currentTimeMillis() / 1000),
//									iosRechargeTemplate.getProduct_id(), serverId);
						} else {
							// 账单无效
							PushIosIAPVerify respMsg = new PushIosIAPVerify(802);
							playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
						}
					}
				} catch (Exception e) {
					logger.error("", e);
					PushIosIAPVerify respMsg = new PushIosIAPVerify(801);
					playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
				}
			}
		});
	}

}
