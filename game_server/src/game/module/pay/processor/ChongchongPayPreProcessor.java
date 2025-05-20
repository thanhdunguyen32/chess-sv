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
import ws.WsMessageHall;
import ws.WsMessageHall.C2SChongChongPayPre;
import ws.WsMessageHall.S2CErrorCode;

@MsgCodeAnn(msgcode = C2SChongChongPayPre.id, accessLimit = 200)
public class ChongchongPayPreProcessor extends PlayingRoleMsgProcessor {

	private static Logger logger = LoggerFactory.getLogger(ChongchongPayPreProcessor.class);

	@Override
	public void processByte(PlayingRole playingRole, RequestByteMessage requestMessage) throws Exception {

	}

	@Override
	public void processProto(PlayingRole playingRole, RequestProtoMessage request) throws Exception {
	}

	@Override
	public void processMy(PlayingRole playingRole, MyRequestMessage request) throws Exception {
//		C2SChongChongPayPre reqMsg = C2SChongChongPayPre.parse(request);
//		int goodsId = reqMsg.goodsId;
//		String goodsName = reqMsg.goodsName;
//		logger.info("chong chong pay pre parse!goodsId={}", goodsId);
//		List<Map<String, Object>> paymentConfig = ChargeTemplateCache.getInstance().getPaymetList();
//		if (goodsId < 0) {
//			S2CErrorCode respMsg = new S2CErrorCode(WsMessageHall.S2CChongChongPayPre.msgCode, 130);
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
//		int serverId = playingRole.getPlayerBean().getServerId();
//		String pext = serverId + "__" + goodsId;
//		S2CChongChongPayPre respMsg = new S2CChongChongPayPre(pext, query_money, goodsId, egretOrderId, goodsName);
//		playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
	}

}
