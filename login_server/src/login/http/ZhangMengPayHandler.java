package login.http;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import lion.http.HttpGetHandler;
import login.LoginServer;
import login.bean.ServerList4Db;
import login.logic.ServerListManager;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLDecoder;
import java.util.*;

public class ZhangMengPayHandler extends HttpGetHandler {

	private static Logger logger = LoggerFactory.getLogger(ZhangMengPayHandler.class);

	public ZhangMengPayHandler(String name) {
		super(name);
	}

	@Override
	public String strHttpHandle(ChannelHandlerContext ctx, FullHttpRequest request, Map<String, String> queryMap)
			throws Exception {
		logger.info("zhang meng pay queries={}", queryMap);
		String orderid = queryMap.get("orderid");
		String uid = queryMap.get("uid");
		String feeid = queryMap.get("feeid");
		String feemoney = queryMap.get("feemoney");
		String paytimeStr = queryMap.get("paytime");
		paytimeStr = URLDecoder.decode(paytimeStr,"UTF-8");
		Date payTime = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss").parse(paytimeStr);
		String extradata = queryMap.get("extradata");
		String sign = queryMap.get("sign");
		//
		String zm_secret_key = "71fde6d7928fb9b471097e8f59be34ba";
		// 第一次md5
		queryMap.remove("sign");
		//对所有不为空的参数按照参数名字母升序排列
		List<String> keyList = new ArrayList<>(queryMap.keySet());
		Collections.sort(keyList);
		StringBuilder sb = new StringBuilder(100);
		for (String akey : keyList) {
			sb.append(akey).append("=").append(queryMap.get(akey)).append("&");
		}
		String signRawStr = sb.toString();
		signRawStr = signRawStr.substring(0, signRawStr.length() - 1);
		//做一次md5处理并转换成32位小写，得到的加密串 1
		String mySighStr = DigestUtils.md5Hex(signRawStr);
		// 在加密串 1 末尾追加 private_key，做一次 md5 加密并转换成32位小写，得到的字符串就是签名 sign 的值
		String mySign2Str = DigestUtils.md5Hex(mySighStr + zm_secret_key);
		logger.info("signRawStr={},mySighStr={},mySign2Str={},sign={}", signRawStr, mySighStr, mySign2Str, sign);
		if (mySign2Str.equalsIgnoreCase(sign)) {
			// send to gs
			int serverid = Integer.valueOf(extradata);
			ServerList4Db targetGs = ServerListManager.getInstance().getServer(Integer.valueOf(serverid));
			if (targetGs != null) {
				String gsHostName = targetGs.getIp();
				boolean reconnectRet = LoginServer.getInstance().getLanClientManager().connect(gsHostName,
						targetGs.getLanPort());
				if (reconnectRet) {
					LoginServer.getInstance().getLanClientManager().sendPayment2Gs(gsHostName, targetGs.getLanPort(),
							uid, orderid, Integer.valueOf(feemoney) / 100, Long.valueOf(payTime.getTime()/1000).intValue(),
							feeid, Integer.valueOf(serverid));
				}
			}
			return "ok";
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
			logger.info("a={}",payTime);
			String s1 = URLDecoder.decode("2018%2D02%2D28 11%3A28%3A51","UTF-8");
			logger.info("s1={}",s1);
			String s2 = URLDecoder.decode(s1,"UTF-8");
			logger.info("s2={}",s2);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
