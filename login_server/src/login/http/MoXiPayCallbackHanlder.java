package login.http;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import lion.http.HttpGetHandler;
import login.LoginServer;
import login.bean.ServerList4Db;
import login.logic.ServerListManager;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class MoXiPayCallbackHanlder extends HttpGetHandler {

	private String app_secret;

	private String uid_prefix;
	
	public MoXiPayCallbackHanlder(String name,String app_secret,String uid_prefix) {
		super(name);
		this.app_secret = app_secret;
		this.uid_prefix = uid_prefix;
	}

	public static final Logger logger = LoggerFactory.getLogger(MoXiPayCallbackHanlder.class);

	@Override
	public String strHttpHandle(ChannelHandlerContext ctx, FullHttpRequest request, Map<String, String> paramMap)
			throws Exception {
		logger.info("moxi paymanet callback,request params={}", paramMap);
		String signStr = paramMap.get("sign");
		paramMap.remove("sign");
		// 对所有不为空的参数按照参数名字母升序排列
		List<String> keyList = new ArrayList<>(paramMap.keySet());
		Collections.sort(keyList);
		StringBuilder sb = new StringBuilder(100);
		for (String akey : keyList) {
			sb.append(akey).append("=").append(paramMap.get(akey)).append("&");
		}
		String signRawStr = sb.toString();
		signRawStr = signRawStr.substring(0, signRawStr.length() - 1);
		String responseStr = "failed";
		try {
			// 验签
			logger.info("yijie verify,source={},sign={}", signRawStr, signStr);
			String mySign = DigestUtils.md5Hex(signRawStr + app_secret);
			if (!signStr.equalsIgnoreCase(mySign)) {
				responseStr = "failed";
			} else {
				responseStr = "success";
				String userid = paramMap.get("account");
				String amountStr = paramMap.get("money");
				String orderid = paramMap.get("orderid");
				String extra_param = paramMap.get("cpinfo");
				int money = Float.valueOf(amountStr).intValue();// moneu以分为单位
				// 参数解压
				String[] extraStrs = StringUtils.split(extra_param, "_");
				String ext1 = extraStrs[0];
				int serverid = Integer.valueOf(extraStrs[1]);
				// send to gs
				ServerList4Db targetGs = ServerListManager.getInstance().getServer(serverid);
				if (targetGs != null) {
					String gsHostName = targetGs.getIp();
					boolean reconnectRet = LoginServer.getInstance().getLanClientManager().connect(gsHostName,
							targetGs.getLanPort());
					if (reconnectRet) {
						int time = (int) (System.currentTimeMillis() / 1000);
						LoginServer.getInstance().getLanClientManager().sendPayment2Gs(gsHostName, targetGs.getLanPort(),
								uid_prefix+userid, orderid, money, time, ext1, serverid);
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
