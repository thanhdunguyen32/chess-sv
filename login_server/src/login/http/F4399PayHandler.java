package login.http;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import lion.http.HttpPostMapHandler;
import login.LoginServer;
import login.bean.ServerList4Db;
import login.logic.ServerListManager;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

public class F4399PayHandler extends HttpPostMapHandler {

	private static Logger logger = LoggerFactory.getLogger(F4399PayHandler.class);

	public F4399PayHandler(String name) {
		super(name);
	}

	@Override
	public String strHttpHandle(ChannelHandlerContext ctx, FullHttpRequest request, Map<String, String> queryMap)
			throws Exception {
		logger.info("4399 pay queries={}", queryMap);
		String orderId = (String) queryMap.get("orderId");
		String gameId = queryMap.get("gameId");
		String userId = queryMap.get("userId");
		String userName = URLDecoder.decode(URLDecoder.decode(queryMap.get("userName"),"UTF-8"),"UTF-8");
		String money = queryMap.get("money");
		String gameMoney = queryMap.get("gameMoney");
		String mark = queryMap.get("mark");
		String server = queryMap.get("server");
		String time = queryMap.get("time");
		String ext = queryMap.get("ext");
		String sign = queryMap.get("sign");
		//
		String callbackKey = "608fb2210cc52bdf6376cc9e415bd58a";
		String signRawStr = String.format("gameId=%1$smark=%2$smoney=%3$sorderId=%4$sserver=%5$stime=%6$suserId=%7$suserName=%8$s%9$s",
				gameId, mark, money, orderId, server, time, userId, userName, callbackKey);
		String mySighStr = DigestUtils.md5Hex(signRawStr);
		logger.info("signRawStr={},mySighStr={},sign={}", signRawStr, mySighStr, sign);
		if (mySighStr.equalsIgnoreCase(sign)) {
			String goodsId = ext;
			// send to gs
			int server_id = Integer.valueOf(server);
			ServerList4Db targetGs = ServerListManager.getInstance().getServer(server_id);
			if (targetGs != null) {
				String gsHostName = targetGs.getIp();
				boolean reconnectRet = LoginServer.getInstance().getLanClientManager().connect(gsHostName,
						targetGs.getLanPort());
				if (reconnectRet) {
					LoginServer.getInstance().getLanClientManager().sendPayment2Gs(gsHostName, targetGs.getLanPort(),
							String.valueOf(userId), orderId, Integer.valueOf(money), Integer.valueOf(time), goodsId,
							server_id);
				}
			}
			return "1";
		} else {
			return "-1";
		}
	}

	public static void main(String[] args) {
		String a1;
		try {
			a1 = URLDecoder.decode(URLDecoder.decode("ceshi%25404399","UTF-8"),"UTF-8");
//			a1 = URLDecoder.decode("This%20is%20a%20%2Burl%2B%21","UTF-8");
//			String a2 = URLEncoder.encode("@","UTF-8");
//			System.out.println(a2);
			System.out.println(a1);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
