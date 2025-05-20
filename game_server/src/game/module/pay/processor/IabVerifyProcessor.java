package game.module.pay.processor;

import com.android.vending.billing.util.Security;
import game.GameServer;
import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.activity.constants.ActivityConstants;
import game.module.activity.logic.ActivityManager;
import game.module.activity.logic.ActivityManager.BeanFirstRechargeTriple;
import game.module.activity.logic.ActivityRecordManager;
import game.module.log.constants.LogConstants;
import game.module.pay.dao.PaymentLogDao;
import game.module.pay.logic.ChargeTemplateCache;
import game.module.pay.logic.PaymentManager;
import game.module.template.RechargeTemplate;
import game.module.user.bean.PlayerBean;
import game.module.user.dao.PlayerDao;
import game.module.user.logic.PlayerInfoManager;
import game.module.vip.logic.VipManager;
import game.session.PlayerOnlineCacheMng;
import game.session.SessionManager;
import lion.common.JacksonUtils;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

@MsgCodeAnn(msgcode = 10821, accessLimit = 200)
public class IabVerifyProcessor extends PlayingRoleMsgProcessor {

	private static Logger logger = LoggerFactory.getLogger(IabVerifyProcessor.class);

//	private static PrivateKey privateKey;
//
//	static {
//		try {
//			privateKey = SecurityUtils.loadPrivateKeyFromKeyStore(SecurityUtils.getPkcs12KeyStore(),
//					new FileInputStream(new File("google_iab_hw_tw.p12")), // 生成的P12文件
//					"notasecret", "privatekey", "notasecret");
//		} catch (Exception e) {
//			logger.error("", e);
//		}
//	}

	@Override
	public void processByte(PlayingRole playingRole, RequestByteMessage requestMessage) throws Exception {

	}

	@SuppressWarnings("unchecked")
	@Override
	public void processProto(final PlayingRole playingRole, RequestProtoMessage request) throws Exception {
//		C2SAndroidPayVerify iosVerifyMsg = ProtoUtil.getProtoObj(C2SAndroidPayVerify.parser(), request);
		final String jsonPurchaseInfo = "";
		final String signature = "";
		final String openId = playingRole.getPlayerBean().getAccountId();
		final String myNickName = playingRole.getPlayerBean().getName();
		final int serverId = playingRole.getPlayerBean().getServerId();
		final int playerId = playingRole.getId();
		logger.info("iab verify,jsonPurchaseInfo={},signature={}", jsonPurchaseInfo, signature);
		String ipAddrRaw = playingRole.getGamePlayer().getAddress();
		final String ipAddr = getIpAddr(ipAddrRaw);
		// String md5_receipt = IOSVerify.md5(receipt);
//		playingRole.getGamePlayer().writeAndFlush(10822, S2CIosVerify.newBuilder().build());
		GameServer.executorService.execute(new Runnable() {
			public void run() {
				try {
					boolean verifyRet = true;
					String rawJsonData = StringUtils.replace(jsonPurchaseInfo, "'", "\"");
					// 跟苹果验证有返回结果------------------
					Map<String, Object> returnJson;
					returnJson = JacksonUtils.getInstance().readValue(StringUtils.replace(jsonPurchaseInfo, "'", "\""), Map.class);
					//
					String packageName = (String) returnJson.get("packageName");
					// 产品ID TODO 判断是否是我们产品
					String product_id = (String) returnJson.get("productId");
					// 订单id
					String transaction_id = (String) returnJson.get("developerPayload");
					// token
					String purchaseToken = (String) returnJson.get("purchaseToken");
					//

//					HttpTransport transport = GoogleNetHttpTransport.newTrustedTransport();
//					// 读取配置文件
//
//					GoogleCredential credential = new GoogleCredential.Builder().setTransport(transport)
//							.setJsonFactory(JacksonFactory.getDefaultInstance())
//							.setServiceAccountId("147902164683-compute@developer.gserviceaccount.com") // e.g.:
//							.setServiceAccountScopes(AndroidPublisherScopes.all())
//							.setServiceAccountPrivateKey(privateKey).build();
//
//					AndroidPublisher publisher = new AndroidPublisher.Builder(transport,
//							JacksonFactory.getDefaultInstance(), credential).build();
//
//					AndroidPublisher.Purchases.Products products = publisher.purchases().products();
//
//					// 参数详细说明:
//					// https://developers.google.com/android-publisher/api-ref/purchases/products/get
//					AndroidPublisher.Purchases.Products.Get product = products.get(packageName, product_id,
//							purchaseToken);
//
//					// 获取订单信息
//					// 返回信息说明:
//					// https://developers.google.com/android-publisher/api-ref/purchases/products
//					// 通过consumptionState, purchaseState可以判断订单的状态
//					ProductPurchase purchase = product.execute();
//					if (purchase != null) {
//						logger.info("developerPayload={}", purchase.getDeveloperPayload());
//					}
//					if (purchase == null || purchase.getPurchaseState() == null
//							|| purchase.getPurchaseState().intValue() != 0) {
//						verifyRet = false;
//					}
					//本地验证订单
					String base64PublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAgq2LhXUFkWjID1uKFoUPhOvJF5BuWusp6Dti0xkuPAnde//IRqnIAhFvWDmiX3tTNWbLJ3FzIbJe6/NsD8TN4YzBbLzlX2SOJEM+as3VtMseOsddg8sx/3YJMDIlfc9wWo48H+wwNZ5qZair8leCYowH3zXzbpRcO9P54FSgcR2Y8fIXQe4OBxFzucMdrRVomd6jDtB+5VFv6qxxbqi/jvrB2i6DAqIDPUE08f9j93T4Cyo7B1upzCL5ruLpMvqbY2gPoy60kPbORT8s2XKdR1/VxUa/zzp2ZhPJdjvkIWwdtF9St4bi8WfZAuD/sXHPsg3mi8D/oPcV3njKE/PEYQIDAQAB";
					verifyRet = Security.verifyPurchase(base64PublicKey, rawJsonData, signature);
					if (!verifyRet) {
						// 账单无效
//						playingRole.getGamePlayer().writeAndFlush(10824,
//								PushAndroidPayVerify.newBuilder().setRet(4).setType(0).build());
					} else {

						// {"orderId":"1111111111.111111111111","packageName":"com.abc.item","productId":"com.abc.item.1",
						// "purchaseTime":1423197856877,"purchaseState":0,
						// "purchaseToken":"dccfjnioeeojanngnfspekea.AO-J1OzsBdFJhqhLtvtybnQbBMxELYL4M-wClITbJFd-rpnPzYWCOlHyK69xgXBYN8lx99XfMBhD8JPg6u3SsgNvPt2hhbvogszRxjtA15rP-qWBYv_Rytw"}
						// 订单是否存在
						boolean orderExist = PaymentLogDao.getInstance().checkOrderExist(transaction_id);
						if (orderExist) {
							logger.info("android tw payment,order exist1,playerId={},orderid={}", playingRole.getId(),
									transaction_id);
//							playingRole.getGamePlayer().writeAndFlush(10824,
//									PushAndroidPayVerify.newBuilder().setRet(2).setType(0).build());
							return;
						}
						// 商品是否存在
						if (!packageName.equals("com.lejigame.cszjtw2")) {
//							playingRole.getGamePlayer().writeAndFlush(10824,
//									PushAndroidPayVerify.newBuilder().setRet(3).setType(0).build());
							return;
						}
						//
						RechargeTemplate iosRechargeTemplate = ChargeTemplateCache.getInstance().getRechargeTemplate(product_id);
						if (iosRechargeTemplate == null) {
//							playingRole.getGamePlayer().writeAndFlush(10824,
//									PushAndroidPayVerify.newBuilder().setRet(3).setType(0).build());
							return;
						}
						if (PaymentManager.getInstance().checkOrderExist(transaction_id)) {
							logger.info("android tw payment,order exist2,playerId={},orderid={}", playingRole.getId(),
									transaction_id);
//							playingRole.getGamePlayer().writeAndFlush(10824,
//									PushAndroidPayVerify.newBuilder().setRet(2).setType(0).build());
							return;
						}
						// 保存log
						PlayerBean pb = PlayerOnlineCacheMng.getInstance().getCache(openId, serverId);
						PaymentManager.getInstance().addPayPlayer(pb != null ? pb.getId() : 0);
						PaymentManager.getInstance().addOrderId(transaction_id);
						// 玩家是否在线
						PlayingRole pr = SessionManager.getInstance().getPlayer(playerId);
						// 是否进行首冲
						int addDiamond = iosRechargeTemplate.getYB();
						boolean isFirstRecharge = false;
//						if (pr != null) {
//							PlayerExtraBean playerExtraBean = PlayerExtraCache.getInstance()
//									.getPlayerExtraBean(playerId);
//							if (playerExtraBean == null || playerExtraBean.getIsRecharge() == null
//									|| playerExtraBean.getIsRecharge() == 0) {
//								isFirstRecharge = true;
//							}
//						} else {
//							isFirstRecharge = PlayerExtraDao.getInstance().isFirstRecharge(playerId);
//						}
						// 是否为青春基金
						boolean isQingChunJiJIn = false;
						// 进行支付
						if (product_id.contains("diamond.0")) {
							if (playingRole != null && playingRole.isChannelActive()) {
//								TaskManager.getInstance().addYueKaTask(playingRole);
//								playingRole.getGamePlayer().writeAndFlush(10824, PushAndroidPayVerify.newBuilder()
//										.setRet(0).setType(1).setTransactionId(transaction_id).build());
							} else {
								List<PlayerBean> playerAll = PlayerDao.getInstance().getPlayerByOpenId(openId,
										serverId);
								if (playerAll == null || playerAll.size() == 0) {
									return;
								}
								PlayerBean playerBean = playerAll.get(0);
//								TaskManager.getInstance().addYueKaTask(playerBean.getId());
							}
						} else if (product_id.contains("chengzhang")) {
							if (playingRole != null && playingRole.isChannelActive()) {
								ActivityManager.getInstance().chengZhangJiJin(playingRole);
//								playingRole.getGamePlayer().writeAndFlush(10824, PushAndroidPayVerify.newBuilder()
//										.setRet(0).setType(2).setTransactionId(transaction_id).build());
							} else {
								List<PlayerBean> playerAll = PlayerDao.getInstance().getPlayerByOpenId(openId,
										serverId);
								if (playerAll == null || playerAll.size() == 0) {
									return;
								}
								PlayerBean playerBean = playerAll.get(0);
								ActivityManager.getInstance().chengZhangJiJinOffline(playerBean.getId());
							}
						} else if (product_id.contains("hero_libao")) {// 英雄礼包
							if (pr != null) {
//								ActivityManager.getInstance().heroLibao(pr, iosRechargeTemplate.getPID(),
//										addDiamond);
//								pr.getGamePlayer().writeAndFlush(10824, PushAndroidPayVerify.newBuilder()
//										.setRet(0).setType(3).setTransactionId(transaction_id).build());
							} else {
								List<PlayerBean> playerAll = PlayerDao.getInstance().getPlayerByOpenId(openId,
										serverId);
								if (playerAll == null || playerAll.size() == 0) {
									return;
								}
								PlayerBean playerBean = playerAll.get(0);
//								ActivityManager.getInstance().heroLibaoOffline(playerBean.getId(),
//										iosRechargeTemplate.getPID(), openId, serverId);
							}
						} else if (product_id.contains("qcjj")) {// 青春基金
							if (pr != null) {
//								ActivityRecordManager.getInstance().qingChunJiJin(pr,
//										iosRechargeTemplate.getPID(), addDiamond);
//								pr.getGamePlayer().writeAndFlush(10824, PushAndroidPayVerify.newBuilder()
//										.setRet(0).setType(4).setTransactionId(transaction_id).build());
							} else {
								List<PlayerBean> playerAll = PlayerDao.getInstance().getPlayerByOpenId(openId,
										serverId);
								if (playerAll == null || playerAll.size() == 0) {
									return;
								}
								PlayerBean playerBean = playerAll.get(0);
//								ActivityRecordManager.getInstance().qingChunJiJinOffline(playerBean.getId(),
//										iosRechargeTemplate.getProduct_id(), openId, serverId);
							}
							isQingChunJiJIn = true;
						} else {
							int sumDiamond = addDiamond;
							// 是否有首冲3倍活动
							BeanFirstRechargeTriple isFirstRechargeTriple = ActivityManager.getInstance()
									.isFirstRechargeTriple(playerId, Integer.valueOf(iosRechargeTemplate.getPID()));
							if (isFirstRechargeTriple.ret == true) {
								int favorRate = isFirstRechargeTriple.favorRate;
								sumDiamond = Math.round(addDiamond * favorRate / 100);
							} else {
								// 首冲奖励钻石
								if (!PaymentLogDao.getInstance().checkRechargeExist(iosRechargeTemplate.getPID(),
										openId, serverId)) {
									sumDiamond = addDiamond * 2;
								}
							}
							// 更新数据库
							logger.info("update db,addDiamond={},openId={}", sumDiamond, openId);
							PlayerDao.getInstance().addMoney(sumDiamond, openId);
							// 更新玩家钻石
							if (pr != null) {
								PlayerInfoManager.getInstance().changeMoney(pr, sumDiamond,
										LogConstants.MODULE_PAYMENT);
//								pr.getGamePlayer().writeAndFlush(10824, PushAndroidPayVerify.newBuilder().setRet(0)
//										.setType(0).setDiamond(sumDiamond).setTransactionId(transaction_id).build());
							} else {
								if (pb != null) {
									pb.setMoney(pb.getMoney() + sumDiamond);
								}
							}
							// 首冲3倍
							if (isFirstRechargeTriple.ret == true) {
//								ActivityManager.getInstance().doFirstRechargeTriple(playerId,
//										iosRechargeTemplate.getPID());
							}
						}
						// 保存log
						PaymentLogDao.getInstance().saveTopupLog(pb != null ? pb.getId() : 0,
								"", transaction_id, iosRechargeTemplate.getYB(),
								openId, serverId);
						// 更新VIP经验和活动
						if (pr != null) {
							// VIP增加
							VipManager.getInstance().rechargeVipLev(pr, addDiamond);
							ActivityManager.getInstance().recharge(pr, addDiamond);
							if (!isQingChunJiJIn) {
								// 每日首充活动
								ActivityRecordManager.getInstance().meiRiShouChong(pr);
								// 连续充值活动
								ActivityRecordManager.getInstance().lianXuChongZhi(pr);
								// 单笔充值活动
								ActivityRecordManager.getInstance().danBiChongZhi(pr, addDiamond * 10);
								// 首冲奖励
								if (isFirstRecharge) {
									PaymentManager.firstRechargeAward(pr);
								}
							}
							// 充值榜活动
							ActivityRecordManager.getInstance().chongZhiBang(pr.getId(), addDiamond * 10);
							// 天天好礼活动
							ActivityManager.getInstance().tianTianHaoLi(pr, addDiamond);
							// 活跃度奖励
							ActivityManager.getInstance().addDailyActive(pr,
									ActivityConstants.ACTIVE_RECHARGE * addDiamond);
						} else {
							// 离线更新VIP
							List<PlayerBean> playerAll = PlayerDao.getInstance().getPlayerByOpenId(openId,
									serverId);
							if (playerAll == null || playerAll.size() == 0) {
								return;
							}
							PlayerBean playerBean = playerAll.get(0);
							int nowVipExp = playerBean.getVipExp();
							nowVipExp += addDiamond;
							int vipLev = VipManager.getInstance().addVipExp(nowVipExp);
							PlayerDao.getInstance().updateVipLev(vipLev, nowVipExp, playerBean.getId());
							if (pb != null) {
								pb.setVipLevel(vipLev);
								pb.setVipExp(nowVipExp);
							}
							// 更新充值活动
							PaymentManager.offlineRechargeActivity(playerBean.getId(), addDiamond);
							if (!isQingChunJiJIn) {
								// 每日首充
								ActivityRecordManager.getInstance().meiRiShouChongOffline(playerBean.getId());
								// 连续充值活动
								ActivityRecordManager.getInstance().lianXuChongZhiOffline(playerBean.getId());
								// 单笔充值活动
								ActivityRecordManager.getInstance().danBiChongZhiOffline(playerBean.getId(),
										addDiamond * 10);
								// 首冲奖励
								if (isFirstRecharge) {
//									PaymentManager.firstRechargeAwardOffline(playerBean.getId());
								}
							}
							// 充值榜活动
							ActivityRecordManager.getInstance().chongZhiBang(playerBean.getId(), addDiamond * 10);
							// 天天好礼活动
							ActivityRecordManager.getInstance().tianTianHaoLiOffline(playerBean.getId(), addDiamond);
						}
					}
				} catch (Exception e) {
					logger.error("", e);
//					playingRole.getGamePlayer().writeAndFlush(10824,
//							PushAndroidPayVerify.newBuilder().setRet(4).setType(0).build());
				}
			}
		});
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
		String base64PublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAxIdySL34rtsceqs1FyJAn5LpXTfAgBZ3cZ3Q5nMuoBR3Kg+9axbbYfY0J9fKcGCQ29oblsx3kpb4VBEQzAKTzrF3Tw2f5Qv1T2xa0X9VhrN3C81qkG/zKyk4L7RDrkaXmWJ25Q73YMB3A/yZUFk5sDnT8u8Cjpasl/xGwSBr97EE6Y/Tb20WciY4+kVy7yIGDDd5htrxuLNjhKVbw8ItRs5M7mdmA2YCqJJthgecto63RLQcVOhEb8aVeDbZ/1S7FuIXzdu3GP28ci4dVWJJKbCDOxL+nYllhruRCPcEioVBEJXc1kpREQl6xU2QGDHNrzeDuT/7IMniKgT8VGQWYwIDAQAB";
		String signedData = "{\"packageName\":\"com.lejigame.cszjtw2\",\"productId\":\"diamond.6\",\"purchaseTime\":1477884531810,\"purchaseState\":0,\"developerPayload\":\"792930763157798912\",\"purchaseToken\":\"padaffjfmkjnpeikdafalhhi.AO-J1Ox6HOFF_LMsqGAxVY5weaP_6jMtL_L8plRwqAxkSllzfbcwl2sijLZ7hw-VsSVRirXG2Jl1WhgAPQ13aAGIzc9jldf9WrPaNqIw98PeH75fzH6Bg8o\"}";
		String signature = "rZs3zfK8qwqBfEJ1E1Pe9OolQSdTsYSUoQdeHRGJEym+D8hp46dd2MB+kTm7amx0qxkqVi8yvehquRinLI1EcKirDnA+WO7EhkrrHGnNqwkYkiGLBYPVgymtX5IlEsXRAXrnskUWlFkKAGvhrNuyGmt7yiHP5tNI/VJxN1/2GyZsPqIErl/m8IEHZNEBmKKtv8SjtSlyo9M8fYxk5FGI0SHbJB3vIl5XINC0DFhodEwJCcxxqMcHiJSF1Kt51hWcuoNXdiA4fWpetVU2dPGYZul/GDAiz3qAbJefxTF8rhsFLjX6EEnD1lORWPQyl6cPI6dq4GGOVYfduSgZTkY4MQ==";
		boolean ret = Security.verifyPurchase(base64PublicKey, signedData, signature);
		System.out.println(ret);
	}

	@Override
	public void processMy(PlayingRole playingRole, MyRequestMessage request) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
