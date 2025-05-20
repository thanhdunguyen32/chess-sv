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
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import sdk.zy.WanbaOpenApi;
import ws.WsMessageHall;
import ws.WsMessageHall.C2SWanBaGetBalance;
import ws.WsMessageHall.S2CErrorCode;

@MsgCodeAnn(msgcode = C2SWanBaGetBalance.id, accessLimit = 200)
public class WanbaGetBalanceProcessor extends PlayingRoleMsgProcessor {

	private static Logger logger = LoggerFactory.getLogger(WanbaGetBalanceProcessor.class);

	@Override
	public void processByte(PlayingRole playingRole, RequestByteMessage requestMessage) throws Exception {

	}

	@Override
	public void processProto(PlayingRole playingRole, RequestProtoMessage request) throws Exception {
	}

	@Override
	public void processMy(final PlayingRole playingRole, MyRequestMessage request) throws Exception {
//		int playerId = playingRole.getId();
//		C2SWanBaGetBalance reqMsg = C2SWanBaGetBalance.parse(request);
//		final String appid = reqMsg.appid;
//		final String openid = reqMsg.openid;
//		final String openkey = reqMsg.openkey;
//		final String pf = reqMsg.pf;
//		final int os_platform = reqMsg.os_platfrom;
//		final int goodsId = reqMsg.goodsId;
//		logger.info("wan ba get balance!playerId={},os_platform={},goodsId={},pf={}", playerId, os_platform, goodsId, pf);
//		final String appKey = OpenConstants.WANBA_APP_KEY;
//		final String wanbaUrlBase = OpenConstants.WANBA_URL_BASE;
//		final String goodsName = reqMsg.goodsName;
//		GameServer.executorService.execute(new Runnable() {
//			@Override
//			public void run() {
//				Map<String, Object> retMap;
//				try {
//					retMap = WanbaOpenApi.getInstance().getUserInfo(openid, openkey, appid, pf, os_platform,
//							wanbaUrlBase, appKey);
//					if (retMap == null) {
//						logger.warn("network error!");
//						S2CErrorCode error_code = new S2CErrorCode(WsMessageHall.S2CWanBaGetBalance.msgCode, 103);
//						playingRole.writeAndFlush(error_code.build(playingRole.alloc()));
//						return;
//					}
//					logger.info("wan ba get balance result={}", retMap);
//					if (!retMap.containsKey("code") || (int) (retMap.get("code")) != 0) {
//						S2CErrorCode error_code = new S2CErrorCode(WsMessageHall.S2CWanBaGetBalance.msgCode, 105);
//						playingRole.writeAndFlush(error_code.build(playingRole.alloc()));
//						return;
//					}
//					// 获取余额
//					List<Map<String, Object>> dataList = (List<Map<String, Object>>) retMap.get("data");
//					int pbalance = (int) dataList.get(0).get("score");
//					//
//					int query_money = 0;
//					List<Map<String, Object>> paymentConfig = ChargeTemplateCache.getInstance().getPaymetList();
//					if (goodsId < paymentConfig.size()) {
//						Map<String, Object> payConfig = paymentConfig.get(goodsId);
//						query_money = (int) payConfig.get("rmb");
//					} else {
//						// 青春基金或者成长基金或英雄礼包
//						Map<String, Object> activityPaymentConfig = ChargeTemplateCache.getInstance()
//								.getActivityPayment(String.valueOf(goodsId));
//						query_money = (int) activityPaymentConfig.get("rmb");
//					}
//					// 余额是否够
//					boolean isBalanceEnough = true;
//					if (pbalance < query_money * 10) {
//						isBalanceEnough = false;
//					}
//					// 获取itemid
//					int itemid = ChargeTemplateCache.getInstance().getPayment2WanbaItemId(os_platform + "-" + goodsId);
//					String tmpOrderId = RandomStringUtils.randomAlphanumeric(32);
////					S2CWanBaGetBalance respMsg = new S2CWanBaGetBalance(itemid, isBalanceEnough, query_money * 10,
////							tmpOrderId, goodsName);
////					playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
//				} catch (UnsupportedEncodingException e) {
//					logger.error("", e);
//				}
//			}
//		});
	}

}
