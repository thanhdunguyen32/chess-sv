package login.http;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import lion.http.HttpJsonHandler;
import login.LoginServer;
import login.bean.ServerList4Db;
import login.logic.ServerListManager;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class GNetopPayCallbackHanlder extends HttpJsonHandler {

	public GNetopPayCallbackHanlder(String name) {
		super(name);
	}

	public static final Logger logger = LoggerFactory.getLogger(GNetopPayCallbackHanlder.class);

	@Override
	public String strHttpHandle(ChannelHandlerContext ctx, FullHttpRequest request, Map<String, Object> paramMap)
			throws Exception {
		logger.info("gNetop paymanet callback,request params={}", paramMap);
		String transid = (String)paramMap.get("transid");
		String orderid = (String)paramMap.get("orderid");
		String userid = (String)paramMap.get("userid");
		String bundleid = (String)paramMap.get("bundleid");
		String productid = (String)paramMap.get("productid");
		String gameserverid = (String)paramMap.get("gameserverid");
		String price = (String)paramMap.get("price");
		String currency = (String)paramMap.get("currency");
		String sandbox = (String)paramMap.get("sandbox");
		String createdatems = (String)paramMap.get("createdatems");
		String attach = (String)paramMap.get("attach");
		String signStr = (String)paramMap.get("sign");
		
		String paykey = "gNetop0X7E2";
		String signRawStr = transid + orderid + userid + bundleid + productid + gameserverid + price + currency
				+ sandbox + createdatems + attach + paykey;
		
		String responseStr = "{\"status\":\"1\"}";
		try {
			// 验签
			logger.info("gNetop verify,source={},sign={}", signRawStr, signStr);
			String mySign = DigestUtils.md5Hex(signRawStr.toLowerCase()).toLowerCase();
			if (!signStr.equalsIgnoreCase(mySign)) {
				responseStr = "{\"status\":\"2\"}";
			} else {
				responseStr = "{\"status\":\"0\"}";
				int money = Float.valueOf(price).intValue();//RMB元为单位
				// 参数解压
				String ext1 = attach;
				int serverid = Integer.valueOf(gameserverid);
				// send to gs
				ServerList4Db targetGs = ServerListManager.getInstance().getServer(serverid);
				if (targetGs != null) {
					String gsHostName = targetGs.getIp();
					boolean reconnectRet = LoginServer.getInstance().getLanClientManager().connect(gsHostName,
							targetGs.getLanPort());
					if (reconnectRet) {
						int time = (int) (System.currentTimeMillis() / 1000);
						LoginServer.getInstance().getLanClientManager().sendPayment2Gs(gsHostName, targetGs.getLanPort(),
								userid, orderid, money, time, ext1, serverid);
					}
				}
			}
		} catch (Exception e) {
			logger.error("", e);
		}
		// ret
		return responseStr;
	}

}
