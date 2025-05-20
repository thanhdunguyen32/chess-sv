package login.http;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import lion.http.HttpHtmlHandler;
import lion.netty4.core.BaseProtoIoExecutor;
import lion.netty4.core.ServerStat;
import login.LoginServer;

import java.util.Date;

public class StatHandler extends HttpHtmlHandler {

	public StatHandler(String name) {
		super(name);
	}

	private long calcQueryPerSecond() {
		long ret = 0;
		long currentTime = System.currentTimeMillis();
		long lastVisitTime = ServerStat.lastVisitTime;
		if (lastVisitTime == 0) {
			ret = 0;
		} else {
			long elapseTime = currentTime - lastVisitTime;
			int elapseQueryCount = ServerStat.queryCount.get() - ServerStat.lastQueryCount;
			ret = elapseQueryCount * 1000L / elapseTime;
		}
		ServerStat.lastVisitTime = currentTime;
		ServerStat.lastQueryCount = ServerStat.queryCount.get();
		return ret;
	}

	@Override
	public String strHttpHandle(ChannelHandlerContext ctx, FullHttpRequest request, String content) throws Exception {
		StringBuilder responseStrBuilder = new StringBuilder();
		responseStrBuilder
				.append("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\">\n");
		responseStrBuilder.append("<HTML>\n");
		responseStrBuilder.append("<HEAD>\n");
		responseStrBuilder.append("<TITLE>game server stat</TITLE>\n");
		responseStrBuilder
				.append("<META NAME=\"Content-Type\" CONTENT=\"text/html; charset=UTF-8\">\n");
		responseStrBuilder.append("</HEAD>\n");
		responseStrBuilder.append("<BODY>\n");
		responseStrBuilder.append("<table border=\"1\">\n");
		responseStrBuilder.append("<tr><th>参数</th><th>值</th></tr>\n");
		responseStrBuilder.append("<tr><td>当前连接数</td><td>" + BaseProtoIoExecutor.getOnlineCount()
				+ "</td></tr>\n");
		responseStrBuilder.append("<tr><td>executorPool</td><td>" + LoginServer.executorService
				+ "</td></tr>\n");
		responseStrBuilder.append("<tr><td>启动时间</td><td>" + new Date(ServerStat.startUpTime)
				+ "</td></tr>\n");
		responseStrBuilder.append("<tr><td>最大包的大小</td><td>" + ServerStat.maxPackageSize
				+ "</td></tr>\n");
		responseStrBuilder
				.append("<tr><td>请求总次数</td><td>" + ServerStat.queryCount + "</td></tr>\n");
		long queryPerSecond = calcQueryPerSecond();
		responseStrBuilder.append("<tr><td>每秒处理次数</td><td>" + queryPerSecond + "</td></tr>\n");
		//responseStrBuilder.append("<tr><td>匹配状态</td><td>" + QuestionManager.getInstance().getMatchMap() + "</td></tr>\n");
		//responseStrBuilder.append("<tr><td>比赛状态</td><td>" + QuestionManager.getInstance().getPkEntityMap() + "</td></tr>\n");
		responseStrBuilder.append("</table>\n");
		responseStrBuilder.append("</BODY>\n");
		responseStrBuilder.append("</HTML>\n");
		String responseStr = responseStrBuilder.toString();
		return responseStr;
	}
}
