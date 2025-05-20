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
import ws.WsMessageHall.C2SQunheiPayPre;
import ws.WsMessageHall.S2CErrorCode;

@MsgCodeAnn(msgcode = C2SQunheiPayPre.id, accessLimit = 200)
public class QunheiPayPreProcessor extends PlayingRoleMsgProcessor {

	private static Logger logger = LoggerFactory.getLogger(QunheiPayPreProcessor.class);

	@Override
	public void processByte(PlayingRole playingRole, RequestByteMessage requestMessage) throws Exception {

	}

	@Override
	public void processProto(PlayingRole playingRole, RequestProtoMessage request) throws Exception {
	}

	@Override
	public void processMy(PlayingRole playingRole, MyRequestMessage request) throws Exception {
//		C2SQunheiPayPre reqMsg = C2SQunheiPayPre.parse(request);
//		int charge_index = reqMsg.charge_index;
//		String goodsName = reqMsg.goodsName;
//		logger.info("qun hei pay pre parse!charge_index={}", charge_index);
//		List<Map<String, Object>> paymentConfig = ChargeTemplateCache.getInstance().getPaymetList();
//		if (charge_index < 0) {
//			S2CErrorCode respMsg = new S2CErrorCode(WsMessageHall.S2CQunheiPayPre.msgCode, 130);
//			playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
//			return;
//		}
//		int query_money = 0;
//		if (charge_index < paymentConfig.size()) {
//			Map<String, Object> payConfig = paymentConfig.get(charge_index);
//			query_money = (int) payConfig.get("rmb");
//		} else {
//			// 青春基金或者成长基金或英雄礼包
//			Map<String, Object> activityPaymentConfig = ChargeTemplateCache.getInstance().getActivityPayment(String.valueOf(charge_index));
//			query_money = (int) activityPaymentConfig.get("rmb");
//		}
//		// 生成sign
//		// 验证签名，签名方式为：MD5(money + userId + ext + key)这里的key是提交到后台的充值key,加号为连接符
//		String userId = playingRole.getPlayerBean().getAccountId();
//		// ext
//		String ordernum = SessionManager.getInstance().generateSessionId().toString();
//		String ext_str = ordernum + "__" + charge_index;
//		String key_str = "7472ddfe5f6c9547e526c10ec6e853c2";
//		String signRawStr = query_money + userId + ext_str + key_str;
//		String mySighStr = DigestUtils.md5Hex(signRawStr);
//		logger.info("qunhei pay params,raw={},sign={}", signRawStr, mySighStr);
		// ret
//		S2CQunheiPayPre respMsg = new S2CQunheiPayPre(ext_str,query_money, mySighStr, charge_index, goodsName);
//		playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
	}

}
