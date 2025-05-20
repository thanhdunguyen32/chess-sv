package login.http;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import lion.http.HttpPostMapHandler;
import login.LoginServer;
import login.bean.ServerList4Db;
import login.logic.ServerListManager;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Map;

public class QuickPayCallbackHanlder extends HttpPostMapHandler {

	private static DocumentBuilder db = null;
	
	private static DocumentBuilderFactory dbFactory = null;
	
	private String md5Key;
	
	private String callbackkey;
	
	static{  
        try {  
            dbFactory = DocumentBuilderFactory.newInstance();  
            db = dbFactory.newDocumentBuilder();  
        } catch (ParserConfigurationException e) {  
            e.printStackTrace();  
        }  
    } 
	
	public QuickPayCallbackHanlder(String name,String md5Key,String callbackkey) {
		super(name);
		this.md5Key = md5Key;
		this.callbackkey = callbackkey;
	}

	public static final Logger logger = LoggerFactory.getLogger(QuickPayCallbackHanlder.class);

	@Override
	public String strHttpHandle(ChannelHandlerContext ctx, FullHttpRequest request, Map<String, String> paramMap)
			throws Exception {
		logger.info("quick paymanet callback,request params={}", paramMap);
		String nt_data = URLDecoder.decode(paramMap.get("nt_data"),"UTF-8");
		String sign = URLDecoder.decode(paramMap.get("sign"),"UTF-8");
		String md5Sign = paramMap.get("md5Sign");
		String signRawStr = nt_data+sign+md5Key;
		String responseStr = "failed";
		try {
			// 验签
			String mySign = DigestUtils.md5Hex(signRawStr);
			if (!md5Sign.equalsIgnoreCase(mySign)) {
				logger.error("quick verify fail!,source={},sign={}", signRawStr, md5Sign);
				responseStr = "SignError";
			} else {
				responseStr = "SUCCESS";
				//解析XML
				String channelid ="";
				String userid ="";
				String amountStr = "";
				String orderid = "";
				String extra_param = "";
				String xmlStr = QuickSDKUtil.decode(nt_data, callbackkey);
				logger.info("QuickPayCallback xml: {}",xmlStr);
				Document document = db.parse(new ByteArrayInputStream(xmlStr.getBytes()));
				Node messageNode = document.getFirstChild().getFirstChild();
				NodeList nodelist= messageNode.getChildNodes();
				for (int i=0;i<nodelist.getLength();i++) {
					Node aNode = nodelist.item(i);
					if(aNode.getNodeName().equals("channel")) {
						channelid = aNode.getTextContent();
					}else if(aNode.getNodeName().equals("channel_uid")) {
						userid = aNode.getTextContent();
					}else if(aNode.getNodeName().equals("order_no")) {
						orderid = aNode.getTextContent();
					}else if(aNode.getNodeName().equals("amount")) {
						amountStr = aNode.getTextContent();
					}else if(aNode.getNodeName().equals("extras_params")) {
						extra_param = aNode.getTextContent();
					}
				}
				int money = Float.valueOf(amountStr).intValue();// money以元为单位
				// 参数解压
				int sepaIndex = extra_param.lastIndexOf("_");
				String pid = StringUtils.substring(extra_param,0,sepaIndex);
				int serverid = Integer.parseInt(StringUtils.substring(extra_param,sepaIndex+1));
				// send to gs
				ServerList4Db targetGs = ServerListManager.getInstance().getServer(serverid);
				if (targetGs != null) {
					String gsHostName = targetGs.getIp();
					boolean reconnectRet = LoginServer.getInstance().getLanClientManager().connect(gsHostName,
							targetGs.getLanPort());
					if (reconnectRet) {
						int time = (int) (System.currentTimeMillis() / 1000);
						LoginServer.getInstance().getLanClientManager().sendPayment2Gs(gsHostName, targetGs.getLanPort(),
								channelid+"-"+userid, orderid, money, time, pid, serverid);
					}
				}
			}
		} catch (Exception e) {
			logger.error("", e);
		}
		// ret
		return responseStr;
	}
	
	public static void main(String[] args) throws SAXException, IOException {
//		//解析XML
//		String channelid ="";
//		String userid ="";
//		String amountStr = "";
//		String orderid = "";
//		String extra_param = "";
//		String xmlStr = QuickSDKUtil.decode("@108@119@174@165@158@82@167@152@171@166@161@162@159@115@90@106@94@103@83@85@154@166@156@161@155@160@166@155@118@90@139@141@118@101@110@90@82@165@165@148@167@151@153@159@160@164@157@118@82@165@160@87@116@118@117@165@162@176@165@163@168@166@169@152@157@157@169@171@147@153@150@113@117@160@157@166@164@151@159@158@110@115@166@158@153@118@109@110@102@172@161@152@119@116@162@168@151@161@164@151@160@147@158@152@119@164@169@164@162@167@169@117@95@163@160@156@158@166@152@160@152@164@157@114@117@167@171@173@143@167@168@156@151@164@144@161@168@113@116@98@160@171@172@152@159@169@149@154@167@151@167@161@117@115@167@166@157@157@168@152@158@167@116@104@99@98@97@101@105@100@108@100@98@102@109@106@101@106@100@101@102@105@106@100@111@103@107@103@117@103@165@171@148@157@168@151@160@161@111@111@169@148@177@146@165@159@165@158@110@105@97@102@105@101@106@99@100@103@109@84@106@109@112@108@99@114@103@108@110@97@161@148@178@146@172@156@158@155@118@117@145@164@160@170@163@172@119@98@101@103@105@112@104@153@163@168@165@166@170@118@110@165@165@148@173@168@171@113@97@114@103@172@164@152@165@170@168@118@117@151@175@171@170@149@172@151@166@154@162@153@163@171@112@170@169@171@117@98@157@171@165@168@153@172@143@167@146@167@150@165@172@112@115@102@165@153@172@171@151@160@149@118@114@103@165@157@170@160@168@162@166@166@144@163@157@172@163@152@152@154@115", "08682213938316890715589277849869");
//		Document document = db.parse(new ByteArrayInputStream(xmlStr.getBytes()));
//		Node messageNode = document.getFirstChild().getFirstChild();
//		NodeList nodelist= messageNode.getChildNodes();
//		for (int i=0;i<nodelist.getLength();i++) {
//			Node aNode = nodelist.item(i);
//			if(aNode.getNodeName().equals("channel")) {
//				channelid = aNode.getNodeValue();
//			}else if(aNode.getNodeName().equals("uid")) {
//				userid = aNode.getTextContent();
//			}else if(aNode.getNodeName().equals("order_no")) {
//				orderid = aNode.getNodeValue();
//			}else if(aNode.getNodeName().equals("amount")) {
//				amountStr = aNode.getNodeValue();
//			}else if(aNode.getNodeName().equals("extras_params")) {
//				extra_param = aNode.getNodeValue();
//			}
//		}
//		System.out.println(1);
//		String extra_param = "cjxg_1_20";
//		int sepaIndex = extra_param.lastIndexOf("_");
//		String pid = StringUtils.substring(extra_param,0,sepaIndex);
//		int serverid = Integer.parseInt(StringUtils.substring(extra_param,sepaIndex+1));
//		logger.info("pid={},server={}",pid,serverid);


//		String mySign = DigestUtils.md5Hex("@110@119@170@158@159@83@169@154@167@163@153@168@166@118@91@102@101@105@86@80@155@160@156@164@154@162@161@160@114@88@137@140@120@101@106@83@83@166@167@150@163@148@145@165@167@167@158@114@89@167@163@82@117@112@117@166@171@162@150@164@168@154@159@151@159@157@165@164@148@154@152@115@113@157@149@172@171@154@160@154@117@117@157@163@149@166@158@168@170@119@99@117@100@159@167@151@166@157@165@165@113@111@150@157@150@158@158@158@164@119@110@106@112@117@99@147@158@147@167@163@155@165@113@117@152@158@149@166@160@157@158@144@161@148@160@154@115@108@95@156@160@154@167@163@156@165@147@158@151@159@158@115@114@156@155@154@163@164@153@164@145@173@155@149@113@101@102@109@107@102@101@108@110@117@104@152@159@154@162@158@155@158@152@170@159@157@113@117@152@158@149@166@160@157@158@144@162@165@151@154@167@110@98@105@106@105@106@102@105@112@102@102@109@100@113@106@109@114@111@104@152@158@149@166@160@157@158@144@162@165@151@154@167@110@108@160@153@166@158@148@166@171@152@149@168@112@154@108@110@158@151@158@102@111@149@109@151@107@102@106@102@105@149@106@108@145@100@114@157@110@158@107@109@107@105@101@109@104@117@100@157@154@160@158@148@165@166@156@151@170@112@109@162@165@151@154@167@143@158@168@118@110@110@110@105@105@102@96@103@99@107@108@103@106@104@106@102@105@105@108@102@106@106@100@108@100@102@113@100@159@162@157@157@171@152@163@166@119@112@160@151@171@152@169@159@166@152@119@103@102@102@104@95@105@99@94@101@106@83@102@102@106@101@106@114@106@109@113@102@169@149@169@149@166@162@162@155@119@111@154@162@165@169@166@166@118@104@95@99@99@111@100@150@157@159@174@166@173@119@113@170@173@149@164@171@165@119@101@114@104@166@173@150@170@169@171@112@116@151@169@167@165@148@168@148@160@145@171@153@166@172@115@154@161@149@162@157@151@111@148@104@117@98@158@173@170@166@153@165@151@162@146@165@148@160@168@115@108@95@166@157@172@172@150@158@158@114@108@101@163@174@158@153@164@166@157@160@149@161@157@165@171@147@152@152@113@149@104@101@97@100@102@106@108@109@97@145@111@111@158@109@103@108@154@108@96@107@148@110@107@156@154@108@158@106@152@101@108qjxm3jiajlzchvl4uf88s6g8253chxr7");
//		logger.info("{}",mySign);


		String nt_data = "%40110%40119%40170%40158%40159%4083%40169%40154%40167%40163%40153%40168%40166%40118%4091%40102%40101%40105%4086%4080%40155%40160%40156%40164%40154%40162%40161%40160%40114%4088%40137%40140%40120%40101%40106%4083%4083%40166%40167%40150%40163%40148%40145%40165%40167%40167%40158%40114%4089%40167%40163%4082%40117%40112%40117%40166%40171%40162%40150%40164%40168%40154%40159%40151%40159%40157%40165%40164%40148%40154%40152%40115%40113%40157%40149%40172%40171%40154%40160%40154%40117%40117%40157%40163%40149%40166%40158%40168%40170%40119%4099%40117%40100%40159%40167%40151%40166%40157%40165%40165%40113%40111%40150%40157%40150%40158%40158%40158%40164%40119%40110%40106%40112%40117%4099%40147%40158%40147%40167%40163%40155%40165%40113%40117%40152%40158%40149%40166%40160%40157%40158%40144%40161%40148%40160%40154%40115%40108%4095%40156%40160%40154%40167%40163%40156%40165%40147%40158%40151%40159%40158%40115%40114%40156%40155%40154%40163%40164%40153%40164%40145%40173%40155%40149%40113%40101%40102%40109%40107%40102%40101%40108%40110%40117%40104%40152%40159%40154%40162%40158%40155%40158%40152%40170%40159%40157%40113%40117%40152%40158%40149%40166%40160%40157%40158%40144%40162%40165%40151%40154%40167%40110%4098%40105%40106%40105%40106%40102%40105%40112%40102%40102%40109%40100%40113%40106%40109%40114%40111%40104%40152%40158%40149%40166%40160%40157%40158%40144%40162%40165%40151%40154%40167%40110%40108%40160%40153%40166%40158%40148%40166%40171%40152%40149%40168%40112%40154%40108%40110%40158%40151%40158%40102%40111%40149%40109%40151%40107%40102%40106%40102%40105%40149%40106%40108%40145%40100%40114%40157%40110%40158%40107%40109%40107%40105%40101%40109%40104%40117%40100%40157%40154%40160%40158%40148%40165%40166%40156%40151%40170%40112%40109%40162%40165%40151%40154%40167%40143%40158%40168%40118%40110%40110%40110%40105%40105%40102%4096%40103%4099%40107%40108%40103%40106%40104%40106%40102%40105%40105%40108%40102%40106%40106%40100%40108%40100%40102%40113%40100%40159%40162%40157%40157%40171%40152%40163%40166%40119%40112%40160%40151%40171%40152%40169%40159%40166%40152%40119%40103%40102%40102%40104%4095%40105%4099%4094%40101%40106%4083%40102%40102%40106%40101%40106%40114%40106%40109%40113%40102%40169%40149%40169%40149%40166%40162%40162%40155%40119%40111%40154%40162%40165%40169%40166%40166%40118%40104%4095%4099%4099%40111%40100%40150%40157%40159%40174%40166%40173%40119%40113%40170%40173%40149%40164%40171%40165%40119%40101%40114%40104%40166%40173%40150%40170%40169%40171%40112%40116%40151%40169%40167%40165%40148%40168%40148%40160%40145%40171%40153%40166%40172%40115%40154%40161%40149%40162%40157%40151%40111%40148%40104%40117%4098%40158%40173%40170%40166%40153%40165%40151%40162%40146%40165%40148%40160%40168%40115%40108%4095%40166%40157%40172%40172%40150%40158%40158%40114%40108%40101%40163%40174%40158%40153%40164%40166%40157%40160%40149%40161%40157%40165%40171%40147%40152%40152%40113";
		nt_data = URLDecoder.decode(nt_data,"UTF-8");
		String xmlStr = QuickSDKUtil.decode(nt_data, "28213335500989957940629569395648");
		logger.info("xmlStr={}",xmlStr);
	}

}
