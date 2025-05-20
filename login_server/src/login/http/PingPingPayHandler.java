package login.http;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import lion.http.HttpPostMapHandler;
import login.LoginServer;
import login.bean.ServerList4Db;
import login.logic.ServerListManager;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.util.*;

public class PingPingPayHandler extends HttpPostMapHandler {

	private static Logger logger = LoggerFactory.getLogger(PingPingPayHandler.class);

	private String payKey;
	
	public PingPingPayHandler(String name, String payKey) {
		super(name);
		this.payKey = payKey;
	}

	@Override
	public String strHttpHandle(ChannelHandlerContext ctx, FullHttpRequest request, Map<String, String> queryMap)
			throws Exception {
		logger.info("ping ping pay queries={}", queryMap);
		Integer server_id = Integer.valueOf(queryMap.get("ServerId"));
		String UserCode = (String)queryMap.get("UserCode");
		String orderId = (String)queryMap.get("OrderId");
		Integer moneyYuan = Float.valueOf(queryMap.get("Amount")).intValue();
		String ExtraInfo = (String)queryMap.get("ExtraInfo");
		String sign = (String)queryMap.get("Sign");
		// 不参与加密
		queryMap.remove("Sign");
		// 对所有不为空的参数按照参数名字母升序排列
		List<String> keyList = new ArrayList<>(queryMap.keySet());
		Collections.sort(keyList);
		StringBuilder sb = new StringBuilder(100);
		for (String akey : keyList) {
			sb.append(akey).append("=").append(queryMap.get(akey)).append("&");
		}
		String signRawStr = sb.toString();
		// 加上key
		signRawStr += "PayKey="+this.payKey;
		String mySighStr = DigestUtils.md5Hex(signRawStr);
		logger.info("signRawStr={},mySighStr={},sign={}", signRawStr, mySighStr, sign);
		if (mySighStr.equalsIgnoreCase(sign)) {
			String goodsId = ExtraInfo;
			// send to gs
			ServerList4Db targetGs = ServerListManager.getInstance().getServer(server_id);
			if (targetGs != null) {
				String gsHostName = targetGs.getIp();
				boolean reconnectRet = LoginServer.getInstance().getLanClientManager().connect(gsHostName,
						targetGs.getLanPort());
				if (reconnectRet) {
					LoginServer.getInstance().getLanClientManager().sendPayment2Gs(gsHostName, targetGs.getLanPort(),
							"pp-"+UserCode, orderId, moneyYuan, (int) (System.currentTimeMillis() / 1000), goodsId,
							server_id);
				}
			}
			return "success";
		} else {
			return "fail";
		}
	}

	public static void main(String[] args) {
		String signRawStr = "appid=1052&extradata=20170307135213SkfBjDM&feeid=1&feemoney=100&orderid=3151703071404286&paystatus=1&paytime=2017-03-07 13:52:14&prover=1&sdkindx=315&uid=f734d3f81b6e21e952b4ca3074d90a30";
		String mySighStr = DigestUtils.md5Hex(signRawStr);
		System.out.println(mySighStr);

		Date payTime;
		try {
			payTime = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss").parse("2018-01-17 09:45:56");
			logger.info("a={}", payTime);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
