package game.module.pay.processor;

import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.pay.logic.ChargeTemplateCache;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import ws.WsMessageHall;
import ws.WsMessageHall.C2SZMPayCheck;
import ws.WsMessageHall.S2CErrorCode;

@MsgCodeAnn(msgcode = C2SZMPayCheck.id, accessLimit = 200)
public class ZMPayCheckProcessor extends PlayingRoleMsgProcessor {

	private static Logger logger = LoggerFactory.getLogger(ZMPayCheckProcessor.class);

	@Override
	public void processByte(PlayingRole playingRole, RequestByteMessage requestMessage) throws Exception {

	}

	@Override
	public void processProto(PlayingRole playingRole, RequestProtoMessage request) throws Exception {
	}

	@Override
	public void processMy(PlayingRole playingRole, MyRequestMessage request) throws Exception {
//		C2SZMPayCheck reqMsg = C2SZMPayCheck.parse(request);
//		int fee_id = reqMsg.fee_id;
//		String goodsName = reqMsg.goodsName;
//		logger.info("zhang meng pay pre parse!fee_id={}", fee_id);
//		List<Map<String, Object>> paymentConfig = ChargeTemplateCache.getInstance().getPaymetList();
//		if (fee_id < 0) {
//			S2CErrorCode respMsg = new S2CErrorCode(WsMessageHall.S2CZMPayCheck.msgCode, 130);
//			playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
//			return;
//		}
//		int query_money = 0;
//		if (fee_id < paymentConfig.size()) {
//			Map<String, Object> payConfig = paymentConfig.get(fee_id);
//			query_money = (int) payConfig.get("rmb");
//		} else {
//			// 青春基金或者成长基金或英雄礼包
//			Map<String, Object> activityPaymentConfig = ChargeTemplateCache.getInstance().getActivityPayment(String.valueOf(fee_id));
//			query_money = (int) activityPaymentConfig.get("rmb");
//		}
//		// 生成sign
//		// 验证签名，签名方式为：MD5(md5(fee+feeid+seceret_key)),加号为连接符
//		int fee = query_money * 100;
//		String secret_key = "71fde6d7928fb9b471097e8f59be34ba";
//
//		String userId = playingRole.getPlayerBean().getAccountId();
//		// ext
//		String ext_str = playingRole.getPlayerBean().getServerId().toString();
//		String signRawStr = String.valueOf(fee) + fee_id + secret_key;
//		String mySighStr = DigestUtils.md5Hex(signRawStr);
//		logger.info("zhang meng pay params,raw={},sign={}", signRawStr, mySighStr);
		// ret
//		S2CZMPayCheck respMsg = new S2CZMPayCheck(query_money, ext_str, String.valueOf(fee_id), mySighStr, goodsName);
//		playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
	}

}
