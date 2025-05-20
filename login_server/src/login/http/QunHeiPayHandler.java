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

import java.util.Map;

public class QunHeiPayHandler extends HttpGetHandler {

	private static Logger logger = LoggerFactory.getLogger(QunHeiPayHandler.class);

	public QunHeiPayHandler(String name) {
		super(name);
	}

	@Override
	public String strHttpHandle(ChannelHandlerContext ctx, FullHttpRequest request, Map<String, String> queryMap)
			throws Exception {
		logger.info("qun hei pay querys={}", queryMap);
		String orderno = queryMap.get("orderno");
		String username = queryMap.get("username");
		String serverid = queryMap.get("serverid");
		String addgold = queryMap.get("addgold");
		String rmb = queryMap.get("rmb");
		String paytime = queryMap.get("paytime");
		String ext = queryMap.get("ext");
		String sign = queryMap.get("sign");
		//
		String qunhei_key = "";
		String signRawStr = orderno + username + serverid + addgold + rmb + paytime + ext + qunhei_key;
		String mySighStr = DigestUtils.md5Hex(signRawStr);
		logger.info("signRawStr={},mySighStr={},sign={}", signRawStr, mySighStr, sign);
		if (mySighStr.equalsIgnoreCase(sign)) {
			// send to gs
			ServerList4Db targetGs = ServerListManager.getInstance().getServer(Integer.valueOf(serverid));
			if (targetGs != null) {
				String gsHostName = targetGs.getIp();
				boolean reconnectRet = LoginServer.getInstance().getLanClientManager().connect(gsHostName,
						targetGs.getLanPort());
				if (reconnectRet) {
					String[] extList = StringUtils.split(ext, "__");
					String chargeIndex = extList[1];
					LoginServer.getInstance().getLanClientManager().sendPayment2Gs(gsHostName, targetGs.getLanPort(),
							username, orderno, Integer.valueOf(rmb), Integer.valueOf(paytime), chargeIndex,
							Integer.valueOf(serverid));
				}
			}
			return "1";
		} else {
			return "-4";
		}
	}
}
