package login.http;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import lion.http.HttpPostMapHandler;
import login.LoginServer;
import login.bean.ServerList4Db;
import login.logic.ServerListManager;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.util.*;

public class ChongChongPayHandler extends HttpPostMapHandler {

	private static Logger logger = LoggerFactory.getLogger(ChongChongPayHandler.class);

	public ChongChongPayHandler(String name) {
		super(name);
	}

	@Override
	public String strHttpHandle(ChannelHandlerContext ctx, FullHttpRequest request, Map<String, String> queryMap)
			throws Exception {
		logger.info("chong chong pay queries={}", queryMap);
		String orderId = queryMap.get("orderId");
		String userId = queryMap.get("userId");
		String moneyStr = queryMap.get("money");
		int money = (int) Float.parseFloat(moneyStr);
		String sign = queryMap.get("sign");
		// 不参与加密
		queryMap.remove("sign");
		// 对所有不为空的参数按照参数名字母升序排列
		List<String> keyList = new ArrayList<>(queryMap.keySet());
		Collections.sort(keyList);
		StringBuilder sb = new StringBuilder(100);
		for (String akey : keyList) {
			sb.append(akey).append("=").append(queryMap.get(akey));
		}
		String signRawStr = sb.toString();
		// 加上key
		// appKey=123
		// sign(e93608df76c9ef61c1af34cc9ba63d9c)=(ext=money=0.01orderId=2016090510301942046545366partnerTransactionNo=20160905103017bfe90bd62157100001time=20160905103023010userId=2242592123)
		String payKey = "9c9e83fe236bdb3f0d4ba57014173efa";
		signRawStr += payKey;
		String mySighStr = DigestUtils.md5Hex(signRawStr);
		logger.info("signRawStr={},mySighStr={},sign={}", signRawStr, mySighStr, sign);
		if (mySighStr.equalsIgnoreCase(sign)) {
			String ext_str = queryMap.get("ext");
			String[] params = StringUtils.split(ext_str, "__");
			int server_id = Integer.valueOf(params[0]);
			String goodsId = params[1];
			// send to gs
			ServerList4Db targetGs = ServerListManager.getInstance().getServer(server_id);
			if (targetGs != null) {
				String gsHostName = targetGs.getIp();
				boolean reconnectRet = LoginServer.getInstance().getLanClientManager().connect(gsHostName,
						targetGs.getLanPort());
				if (reconnectRet) {
					LoginServer.getInstance().getLanClientManager().sendPayment2Gs(gsHostName, targetGs.getLanPort(),
							userId, orderId, money, (int) (System.currentTimeMillis() / 1000), goodsId,
							server_id);
				}
			}
			return "{\"code\":0,\"msg\":\"success!\"}";
		} else {
			return "{\"code\":1,\"msg\":\"fail!\"}";
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
