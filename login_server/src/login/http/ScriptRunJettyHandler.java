//package login.http;
//
//import java.io.IOException;
//import java.io.PrintWriter;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.apache.commons.lang3.StringUtils;
//import org.eclipse.jetty.server.Request;
//import org.eclipse.jetty.server.handler.AbstractHandler;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import game.util.GameScriptEngineManager;
//
//public class ScriptRunJettyHandler extends AbstractHandler {
//
//	private GameScriptEngineManager scriptManager = GameScriptEngineManager.getInstance();
//
//	public static final Logger logger = LoggerFactory.getLogger(ScriptRunJettyHandler.class);
//	
//	static class SingletonHolder {
//		static ScriptRunJettyHandler instance = new ScriptRunJettyHandler();
//	}
//
//	public static ScriptRunJettyHandler getInstance() {
//		return SingletonHolder.instance;
//	}
//
//	private void jumpInput(HttpServletResponse response) throws IOException {
//		StringBuilder responseStrBuilder = new StringBuilder();
//
//		// response body
//		responseStrBuilder.append("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\">\n");
//		responseStrBuilder.append("<HTML>\n");
//		responseStrBuilder.append("<HEAD>\n");
//		responseStrBuilder.append("<TITLE>脚本动态执行</TITLE>\n");
//		responseStrBuilder.append("<META NAME=\"Content-Type\" CONTENT=\"text/html; charset=UTF-8\">\n");
//		responseStrBuilder.append("</HEAD>\n");
//		responseStrBuilder.append("<BODY>\n");
//
//		responseStrBuilder.append("<h1 style=\"color:blue;\">脚本内容</h1>\n");
//		responseStrBuilder.append("<form action=\"script\" method=\"post\">\n");
//		responseStrBuilder.append("<textarea name=\"jsContent\" rows=\"20\" cols=\"100\">\n");
//		responseStrBuilder.append("</textarea>\n");
//		responseStrBuilder.append("<br /><input type=\"submit\" />&nbsp;&nbsp;&nbsp;<input type=\"reset\" />\n");
//		responseStrBuilder.append("</form>\n");
//
//		responseStrBuilder.append("</BODY>\n");
//		responseStrBuilder.append("</HTML>\n");
//
//		response.setContentType("text/html; charset=utf-8");
//		response.setStatus(HttpServletResponse.SC_OK);
//		PrintWriter out = response.getWriter();
//
//		out.println(responseStrBuilder.toString());
//
//	}
//
//	@Override
//	public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
//			throws IOException, ServletException {
//		logger.info("script run,content={}", request.getParameterMap());
//		String opType = request.getParameter("type");
//		if (opType != null && opType.equals("input")) {
//			jumpInput(response);
//		} else {
//			String queryString = request.getParameter("jsContent");
//			if (!StringUtils.isEmpty(queryString)) {
//				try {
//					String responseStr = scriptManager.dynamicExecute(queryString);
//					response.setContentType("text/html; charset=utf-8");
//					response.setStatus(HttpServletResponse.SC_OK);
//					PrintWriter out = response.getWriter();
//					out.println(responseStr);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		}
//		baseRequest.setHandled(true);
//	}
//
//}
