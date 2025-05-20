package game.module.pay.processor;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.pay.logic.ChargeTemplateCache;
import game.session.SessionManager;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import ws.WsMessageHall.C2S4399PayPre;
import ws.WsMessageHall.S2C4399PayPre;
import ws.WsMessageHall.S2CErrorCode;

@MsgCodeAnn(msgcode = C2S4399PayPre.id, accessLimit = 200)
public class F4399PayPreProcessor extends PlayingRoleMsgProcessor {

	private static Logger logger = LoggerFactory.getLogger(F4399PayPreProcessor.class);

	@Override
	public void processByte(PlayingRole playingRole, RequestByteMessage requestMessage) throws Exception {

	}

	@Override
	public void processProto(PlayingRole playingRole, RequestProtoMessage request) throws Exception {
	}

	@Override
	public void processMy(PlayingRole playingRole, MyRequestMessage request) throws Exception {
//		C2S4399PayPre reqMsg = C2S4399PayPre.parse(request);
//		int goodsId = reqMsg.goodsId;
//		String goodsName =reqMsg.goodsName;
//		logger.info("4399 pay pre parse!goodsId={}", goodsId);
//		List<Map<String, Object>> paymentConfig = ChargeTemplateCache.getInstance().getPaymetList();
//		if (goodsId < 0) {
//			S2CErrorCode respMsg = new S2CErrorCode(S2C4399PayPre.msgCode, 130);
//			playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
//			return;
//		}
//		int query_money = 0;
//
//		if (goodsId < paymentConfig.size()) {
//			Map<String, Object> payConfig = paymentConfig.get(goodsId);
//			query_money = (int) payConfig.get("rmb");
//		} else {
//			// 青春基金或者成长基金或英雄礼包
//			Map<String, Object> activityPaymentConfig = ChargeTemplateCache.getInstance().getActivityPayment(String.valueOf(goodsId));
//			query_money = (int) activityPaymentConfig.get("rmb");
//		}
//		String egretOrderId = SessionManager.getInstance().generateSessionId().toString();
//		// ret
//		String pext = String.valueOf(goodsId);
//		S2C4399PayPre respMsg = new S2C4399PayPre(goodsId, query_money, pext, egretOrderId, goodsName);
//		playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
	}

}
