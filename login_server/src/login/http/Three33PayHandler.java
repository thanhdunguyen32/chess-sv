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

import java.net.URLDecoder;
import java.text.ParseException;
import java.util.*;

public class Three33PayHandler extends HttpPostMapHandler {

	private static Logger logger = LoggerFactory.getLogger(Three33PayHandler.class);

	public Three33PayHandler(String name) {
		super(name);
	}

	@Override
	public String strHttpHandle(ChannelHandlerContext ctx, FullHttpRequest request, Map<String, String> queryMap)
			throws Exception {
		logger.info("333 pay queries={}", queryMap);
		String orderId = queryMap.get("orderId");
		String uid = queryMap.get("uid");
		String goodsIdStr = queryMap.get("goodsId");
		String goodsId = goodsIdStr;
		String moneyStr = queryMap.get("money");
		int money = (int) Float.parseFloat(moneyStr);
		int pay_time = Integer.valueOf(queryMap.get("time"));
		String server_str = queryMap.get("server");
		int server_id = Integer.valueOf(server_str);
		String sign = queryMap.get("sign");
		// 不参与加密
		queryMap.remove("sign");
		queryMap.remove("ext");
		queryMap.remove("signType");
		// 对所有不为空的参数按照参数名字母升序排列
		List<String> keyList = new ArrayList<>(queryMap.keySet());
		Collections.sort(keyList);
		StringBuilder sb = new StringBuilder(100);
		for (String akey : keyList) {
			sb.append(akey).append("=").append(URLDecoder.decode(queryMap.get(akey),"UTF-8")).append("&");
		}
		String signRawStr = sb.toString();
		// 加上key
		String payKey = "06aeee1068759173fc86c13b4b41a69a";
		signRawStr += "key=" + payKey;
		// MD5(cpOrderId=1475049097&gameId=11&goodsId=1&goodsName=测试商品&money=1.00&orderId=201705231751455104&role=1&server=1&status=success&time=1475049098&uid=6298253&userName=yx6298253&key=testpaykey)
		String mySighStr = DigestUtils.md5Hex(signRawStr);
		logger.info("signRawStr={},mySighStr={},sign={}", signRawStr, mySighStr, sign);
		if (mySighStr.equalsIgnoreCase(sign)) {
			// send to gs
			ServerList4Db targetGs = ServerListManager.getInstance().getServer(server_id);
			if (targetGs != null) {
				String gsHostName = targetGs.getIp();
				boolean reconnectRet = LoginServer.getInstance().getLanClientManager().connect(gsHostName,
						targetGs.getLanPort());
				if (reconnectRet) {
					LoginServer.getInstance().getLanClientManager().sendPayment2Gs(gsHostName, targetGs.getLanPort(),
							uid, orderId, money, pay_time, goodsId, server_id);
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
