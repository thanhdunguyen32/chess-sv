package game.module.pay.processor;

import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
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
import ws.WsMessageHall.C2S333PayPre;
import ws.WsMessageHall.S2CErrorCode;

@MsgCodeAnn(msgcode = C2S333PayPre.id, accessLimit = 200)
public class Three33PayPreProcessor extends PlayingRoleMsgProcessor {

	private static Logger logger = LoggerFactory.getLogger(Three33PayPreProcessor.class);

	@Override
	public void processByte(PlayingRole playingRole, RequestByteMessage requestMessage) throws Exception {

	}

	@Override
	public void processProto(PlayingRole playingRole, RequestProtoMessage request) throws Exception {
	}

	@Override
	public void processMy(PlayingRole playingRole, MyRequestMessage request) throws Exception {
//		C2S333PayPre reqMsg = C2S333PayPre.parse(request);
//		int gameId = reqMsg.gameId;
//		int goodsId = reqMsg.goodsId;
//		String goodsName = reqMsg.goodsName;
//		logger.info("333 pay pre parse!goodsId={}", goodsId);
//		List<Map<String, Object>> paymentConfig = ChargeTemplateCache.getInstance().getPaymetList();
//		if (goodsId < 0) {
//			S2CErrorCode respMsg = new S2CErrorCode(WsMessageHall.S2C333PayPre.msgCode, 130);
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
//		//333特殊处理
//		goodsId++;
//		// 生成sign
//		// 验证签名，签名方式为：MD5(md5(fee+feeid+seceret_key)),加号为连接符
//		String pay_key = "06aeee1068759173fc86c13b4b41a69a";
//		String userId = playingRole.getPlayerBean().getAccountId();
//		String cpOrderId = SessionManager.getInstance().generateSessionId().toString();
//		int time = (int) (System.currentTimeMillis() / 1000);
//		String server = playingRole.getPlayerBean().getServerId().toString();
//		String role = String.valueOf(playingRole.getId());
//		// 签名原始串
//		String signRawStr = String.format(
//				"cpOrderId=%1$s&gameId=%2$d&goodsId=%3$d&goodsName=%4$s&money=%5$d&role=%6$s&server=%7$s&time=%8$d&uid=%9$s&key=%10$s",
//				cpOrderId, gameId, goodsId, goodsName, query_money, role, server, time, userId, pay_key);
//		String mySighStr = DigestUtils.md5Hex(signRawStr);
//		logger.info("333 pay params,raw={},sign={}", signRawStr, mySighStr);
		// ret
//		S2C333PayPre respMsg = new S2C333PayPre(server, role, query_money, String.valueOf(goodsId), mySighStr, time,
//				cpOrderId, goodsName);
//		playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
	}

}
