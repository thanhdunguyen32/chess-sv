package login.http;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import lion.common.GameScriptEngineManager;
import lion.http.HttpHtmlHandler;
import login.LoginServer;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class ScriptRunNettyHandler extends HttpHtmlHandler {

	public ScriptRunNettyHandler(String name) {
		super(name);
	}

	private String jumpInput() throws IOException {
		StringBuilder responseStrBuilder = new StringBuilder();

		// response body
		responseStrBuilder.append("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\">\n");
		responseStrBuilder.append("<HTML>\n");
		responseStrBuilder.append("<HEAD>\n");
		responseStrBuilder.append("<TITLE>脚本动态执行</TITLE>\n");
		responseStrBuilder.append("<META NAME=\"Content-Type\" CONTENT=\"text/html; charset=UTF-8\">\n");
		responseStrBuilder.append("</HEAD>\n");
		responseStrBuilder.append("<BODY>\n");

		responseStrBuilder.append("<h1 style=\"color:blue;\">脚本内容</h1>\n");
		responseStrBuilder.append("<form action=\"script\" method=\"post\">\n");
		responseStrBuilder.append("<textarea name=\"jsContent\" rows=\"20\" cols=\"100\">\n");
		responseStrBuilder.append("</textarea>\n");
		responseStrBuilder.append("<br /><input type=\"submit\" />&nbsp;&nbsp;&nbsp;<input type=\"reset\" />\n");
		responseStrBuilder.append("</form>\n");

		responseStrBuilder.append("</BODY>\n");
		responseStrBuilder.append("</HTML>\n");

		String responseStr = responseStrBuilder.toString();
		return responseStr;
	}

	@Override
	public String strHttpHandle(ChannelHandlerContext ctx, FullHttpRequest request, String content) throws Exception {
		Map<String, String> paramMap = null;
		if (request.method().equals(HttpMethod.GET)) {
			paramMap = ScriptRunNettyHandler.extractGetParameters(request.uri());
		} else if (request.method().equals(HttpMethod.POST)) {
			paramMap = ScriptRunNettyHandler.extractPostParameters(content);
		}
		String opType = paramMap.get("type");
		String responseStr = "";
		if (opType != null && opType.equals("input")) {
			responseStr = jumpInput();
		} else {
			String queryString = paramMap.get("jsContent");
			if (!StringUtils.isEmpty(queryString)) {
				try {
					responseStr = GameScriptEngineManager.getInstance(LoginServer.getInstance()).dynamicExecute(queryString);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return responseStr;
	}

	public static Map<String, String> extractPostParameters(String content) throws IOException {
		return extractParameters(content);
	}

	public static Map<String, String> extractGetParameters(String uri) throws IOException {
		int paramStartIndex = uri.indexOf('?');
		if (paramStartIndex > -1) {
			String queryString = uri.substring(paramStartIndex + 1);
			return extractParameters(queryString);
		} else {
			return extractParameters("");
		}
	}

	public static Map<String, String> extractParameters(String queryString) throws IOException {
		Map<String, String> retMap = new HashMap<String, String>();
		String[] retList = StringUtils.split(queryString, "&");
		if (ArrayUtils.isNotEmpty(retList)) {
			for (String parameterOne : retList) {
				String[] paramPair = StringUtils.split(parameterOne, "=");
				if (ArrayUtils.isNotEmpty(paramPair) && paramPair.length == 2) {
					retMap.put(paramPair[0], URLDecoder.decode(paramPair[1], "UTF-8"));
				}
			}
		}
		return retMap;
	}

}
