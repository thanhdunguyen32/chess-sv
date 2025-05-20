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

public class O185SyRhFanliHandler extends HttpPostMapHandler {

	private static Logger logger = LoggerFactory.getLogger(O185SyRhFanliHandler.class);

	private String ServerKey;
	
	public O185SyRhFanliHandler(String name, String ServerKey) {
		super(name);
		this.ServerKey = ServerKey;
	}

	@Override
	public String strHttpHandle(ChannelHandlerContext ctx, FullHttpRequest request, Map<String, String> queryMap)
			throws Exception {
		logger.info("185sy RH pay queries={}", queryMap);
		StringBuilder sb = new StringBuilder();
		sb.append("orderID=").append(queryMap.get("orderID")).append("&").append("appid=").append(queryMap.get("appid"))
				.append("&").append("serverID=").append(queryMap.get("serverID")).append("&").append("channel=")
				.append(queryMap.get("channel")).append("&").append("vip=").append(queryMap.get("vip")).append("&")
				.append("gift=").append(queryMap.get("gift")).append("&").append("username=")
				.append(queryMap.get("username")).append("&").append("roleID=").append(queryMap.get("roleID"))
				.append("&").append("roleName=").append(queryMap.get("roleName")).append("&").append("amount=")
				.append(queryMap.get("amount")).append("&").append("time=").append(queryMap.get("time")).append("&")
				.append("key=").append(ServerKey);// 后台 ServerKey
		String mySighStr = DigestUtils.md5Hex(sb.toString()).toLowerCase();
		Integer server_id = Integer.valueOf(queryMap.get("serverID"));
		Integer is_vip = Integer.valueOf(queryMap.get("vip"));
		String username = (String)queryMap.get("username");
		String orderId = (String)queryMap.get("orderID");
		Integer moneyFen = Integer.valueOf(queryMap.get("amount"));
		String sign = (String)queryMap.get("sign");
		Integer passTime = Integer.valueOf(queryMap.get("time"));
		// 对所有不为空的参数按照参数名字母升序排列
		List<String> keyList = new ArrayList<>(queryMap.keySet());
		Collections.sort(keyList);
		logger.info("signRawStr={},mySighStr={},sign={}", sb.toString(), mySighStr, sign);
		if (mySighStr.equalsIgnoreCase(sign)) {
			// send to gs
			ServerList4Db targetGs = ServerListManager.getInstance().getServer(server_id);
			if (targetGs != null) {
				String gsHostName = targetGs.getIp();
				boolean reconnectRet = LoginServer.getInstance().getLanClientManager().connect(gsHostName,
						targetGs.getLanPort());
				if (reconnectRet) {
					LoginServer.getInstance().getLanClientManager().send185SyFanli(gsHostName, targetGs.getLanPort(),
							username, orderId, moneyFen / 100, passTime, is_vip, server_id);
				}
			}
			return "SUCCESS";
		} else {
			return "FAIL";
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
