package login.http;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import lion.http.HttpPostMapHandler;
import login.LoginServer;
import login.bean.ServerList4Db;
import login.logic.ServerListManager;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class YunbeePayCallbackHanlder extends HttpPostMapHandler {

	private String gameKey;
	
	public YunbeePayCallbackHanlder(String name,String payKey) {
		super(name);
		this.gameKey = payKey;
	}

	public static final Logger logger = LoggerFactory.getLogger(YunbeePayCallbackHanlder.class);

	@Override
	public String strHttpHandle(ChannelHandlerContext ctx, FullHttpRequest request, Map<String, String> paramMap)
			throws Exception {
		logger.info("yunbee paymanet callback,request params={}", paramMap);
		String out_trade_no = paramMap.get("out_trade_no");
		String priceStr = (String)paramMap.get("price");
		String pay_statusStr = (String)paramMap.get("pay_status");
		String extendStr = (String)paramMap.get("extend");
		String signStr = (String)paramMap.get("sign");
		
		String signRawStr = out_trade_no + priceStr + pay_statusStr + extendStr + gameKey;
		
		String responseStr = "";
		try {
			// 验签
			String mySign = DigestUtils.md5Hex(signRawStr);
			logger.info("yunbee verify,source={},pass_sign={},my_sign={}", signRawStr, signStr, mySign);
			if (!signStr.equalsIgnoreCase(mySign)) {
				responseStr = "fail";
			} else {
				responseStr = "success";
				int money = Double.valueOf(priceStr).intValue();//RMB元为单位
				// 参数解压
				String[] extendStrs = StringUtils.split(extendStr,"_");
				String ext1 = extendStrs[0];
				int serverid = Integer.valueOf(extendStrs[1]);
				String userid = extendStrs[2];
				// send to gs
				ServerList4Db targetGs = ServerListManager.getInstance().getServer(serverid);
				if (targetGs != null) {
					String gsHostName = targetGs.getIp();
					boolean reconnectRet = LoginServer.getInstance().getLanClientManager().connect(gsHostName,
							targetGs.getLanPort());
					if (reconnectRet) {
						int time = (int) (System.currentTimeMillis() / 1000);
						LoginServer.getInstance().getLanClientManager().sendPayment2Gs(gsHostName, targetGs.getLanPort(),
								userid, out_trade_no, money, time, ext1, serverid);
					}
				}
			}
		} catch (Exception e) {
			responseStr = "fail";
			logger.error("", e);
		}
		// ret
		return responseStr;
	}

}
