package game.module.pay.processor;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import game.GameServer;
import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.pay.OpenConstants;
import game.module.pay.logic.ChargeTemplateCache;
import game.module.pay.logic.PaymentManager;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import sdk.zy.WanbaOpenApi;
import ws.WsMessageHall.C2SWanBaPay;
import ws.WsMessageHall.S2CErrorCode;

@MsgCodeAnn(msgcode = C2SWanBaPay.id, accessLimit = 200)
public class WanbaPayProcessor extends PlayingRoleMsgProcessor {

	private static Logger logger = LoggerFactory.getLogger(WanbaPayProcessor.class);

	@Override
	public void processByte(PlayingRole playingRole, RequestByteMessage requestMessage) throws Exception {

	}

	@Override
	public void processProto(PlayingRole playingRole, RequestProtoMessage request) throws Exception {
	}

	@Override
	public void processMy(final PlayingRole playingRole, MyRequestMessage request) throws Exception {
//		int playerId = playingRole.getId();
//		C2SWanBaPay reqMsg = C2SWanBaPay.parse(request);
//		final String appid = reqMsg.appid;
//		final String openid = reqMsg.openid;
//		final String openkey = reqMsg.openkey;
//		final String pf = reqMsg.pf;
//		final int os_platform = reqMsg.os_platfrom;
//		final int itemid = reqMsg.itemid;
//		logger.info("wan ba pay!playerId={},itemid={},os_platform={}", playerId, itemid, os_platform);
//		final String appKey = OpenConstants.WANBA_APP_KEY;
//		final String wanbaUrlBase = OpenConstants.WANBA_URL_BASE;
//		final String billno = RandomStringUtils.randomAlphanumeric(32);
//		final int myServerId = playingRole.getPlayerBean().getServerId();
//		GameServer.executorService.execute(new Runnable() {
//			@Override
//			public void run() {
//				Map<String, Object> retMap;
//				try {
//					retMap = WanbaOpenApi.getInstance().buyItem(openid, openkey, appid, pf, String.valueOf(os_platform),
//							billno, String.valueOf(itemid), wanbaUrlBase, appKey);
//					if (retMap == null) {
//						logger.warn("network error!");
//						S2CErrorCode error_code = new S2CErrorCode(C2SWanBaPay.id+1, 103);
//						playingRole.writeAndFlush(error_code.build(playingRole.alloc()));
//						return;
//					}
//					logger.info("wan ba pay result={}", retMap);
//					if (!retMap.containsKey("code") || (int) (retMap.get("code")) != 0) {
//						S2CErrorCode error_code = new S2CErrorCode(C2SWanBaPay.id+1, 105);
//						playingRole.writeAndFlush(error_code.build(playingRole.alloc()));
//						return;
//					}
//					// 发货
//					int other = ChargeTemplateCache.getInstance().getWanbaItemIdMap(String.valueOf(itemid));
//					int query_money = 0;
//					List<Map<String, Object>> paymentConfig = ChargeTemplateCache.getInstance().getPaymetList();
//					if (other < paymentConfig.size()) {
//						Map<String, Object> payConfig = paymentConfig.get(other);
//						query_money = (int) payConfig.get("rmb");
//					} else {
//						// 青春基金或者成长基金或英雄礼包
//						Map<String, Object> activityPaymentConfig = ChargeTemplateCache.getInstance()
//								.getActivityPayment(String.valueOf(other));
//						query_money = (int) activityPaymentConfig.get("rmb");
//					}
//					PaymentManager.getInstance().payCallback(null, os_platform+"-"+openid, billno, query_money,
//							(int) (System.currentTimeMillis() / 1000), String.valueOf(other), myServerId);
//				} catch (UnsupportedEncodingException e) {
//					logger.error("", e);
//				}
//			}
//		});
	}

}
