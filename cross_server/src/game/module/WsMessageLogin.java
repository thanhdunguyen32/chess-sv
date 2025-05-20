package ws;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.netty.buffer.ByteBufAllocator;
import java.util.List;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.MySendToMessage;
import ws.WsMessageBase.IOServerHasRole;
import ws.WsMessageBase.ServerListItem;

public final class WsMessageLogin{
	public static final class C2STestLogin{
		private static final Logger logger = LoggerFactory.getLogger(C2STestLogin.class);
		public String uname;
		public String pwd;
		@Override
		public String toString() {
			return "C2STestLogin [uname="+uname+",pwd="+pwd+",]";
		}
		public static final int id = 51;

		public static C2STestLogin parse(MyRequestMessage request){
			C2STestLogin retObj = new C2STestLogin();
			try{
			retObj.uname=request.readString();
			retObj.pwd=request.readString();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CTestLogin{
		private static final Logger logger = LoggerFactory.getLogger(S2CTestLogin.class);
		public int uid;
		public long session_id;
		public S2CTestLogin(int puid,long psession_id){
			uid=puid;
			session_id=psession_id;
		}
		public S2CTestLogin(){}
		@Override
		public String toString() {
			return "S2CTestLogin [uid="+uid+",session_id="+session_id+",]";
		}
		public static final int msgCode = 52;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 52);
			retMsg.writeInt(uid);
			retMsg.writeLong(session_id);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SGuestLogin{
		private static final Logger logger = LoggerFactory.getLogger(C2SGuestLogin.class);
		public int uid;
		@Override
		public String toString() {
			return "C2SGuestLogin [uid="+uid+",]";
		}
		public static final int id = 53;

		public static C2SGuestLogin parse(MyRequestMessage request){
			C2SGuestLogin retObj = new C2SGuestLogin();
			try{
			retObj.uid=request.readInt();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CGuestLogin{
		private static final Logger logger = LoggerFactory.getLogger(S2CGuestLogin.class);
		public int uid;
		public long session_id;
		public S2CGuestLogin(int puid,long psession_id){
			uid=puid;
			session_id=psession_id;
		}
		public S2CGuestLogin(){}
		@Override
		public String toString() {
			return "S2CGuestLogin [uid="+uid+",session_id="+session_id+",]";
		}
		public static final int msgCode = 54;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 54);
			retMsg.writeInt(uid);
			retMsg.writeLong(session_id);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SOpenLogin{
		private static final Logger logger = LoggerFactory.getLogger(C2SOpenLogin.class);
		public String access_token;
		public String open_id;
		public int platform_type;
		@Override
		public String toString() {
			return "C2SOpenLogin [access_token="+access_token+",open_id="+open_id+",platform_type="+platform_type+",]";
		}
		public static final int id = 55;

		public static C2SOpenLogin parse(MyRequestMessage request){
			C2SOpenLogin retObj = new C2SOpenLogin();
			try{
			retObj.access_token=request.readString();
			retObj.open_id=request.readString();
			retObj.platform_type=request.readInt();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2COpenLogin{
		private static final Logger logger = LoggerFactory.getLogger(S2COpenLogin.class);
		public String open_id;
		public long session_id;
		public int platform_type;
		public S2COpenLogin(String popen_id,long psession_id,int pplatform_type){
			open_id=popen_id;
			session_id=psession_id;
			platform_type=pplatform_type;
		}
		public S2COpenLogin(){}
		@Override
		public String toString() {
			return "S2COpenLogin [open_id="+open_id+",session_id="+session_id+",platform_type="+platform_type+",]";
		}
		public static final int msgCode = 56;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 56);
			retMsg.writeString(open_id);
			retMsg.writeLong(session_id);
			retMsg.writeInt(platform_type);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SServerList{
		private static final Logger logger = LoggerFactory.getLogger(C2SServerList.class);
		public long session_id;
		@Override
		public String toString() {
			return "C2SServerList [session_id="+session_id+",]";
		}
		public static final int id = 57;

		public static C2SServerList parse(MyRequestMessage request){
			C2SServerList retObj = new C2SServerList();
			try{
			retObj.session_id=request.readLong();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CServerList{
		private static final Logger logger = LoggerFactory.getLogger(S2CServerList.class);
		public List<Integer> recommands;
		public List<IOServerHasRole> has_roles;
		public List<ServerListItem> alls;
		@Override
		public String toString() {
			return "S2CServerList [recommands="+recommands+",has_roles="+has_roles+",alls="+alls+",]";
		}
		public static final int msgCode = 58;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 58);
			if(recommands == null || recommands.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(recommands.size());
				for(Integer recommands1 : recommands){
			retMsg.writeInt(recommands1);
				}
			}
			if(has_roles == null || has_roles.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(has_roles.size());
				for(IOServerHasRole has_roles1 : has_roles){
					retMsg.writeInt(has_roles1.server_id);
					retMsg.writeInt(has_roles1.player_level);
				}
			}
			if(alls == null || alls.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(alls.size());
				for(ServerListItem alls1 : alls){
					retMsg.writeInt(alls1.id);
					retMsg.writeString(alls1.name);
					retMsg.writeString(alls1.ip_addr);
					retMsg.writeInt(alls1.port);
					retMsg.writeInt(alls1.status);
					retMsg.writeInt(alls1.port_ssl);
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SGetAnnounce{
		private static final Logger logger = LoggerFactory.getLogger(C2SGetAnnounce.class);
		public static final int id = 59;
	}
	public static final class S2CGetAnnounce{
		private static final Logger logger = LoggerFactory.getLogger(S2CGetAnnounce.class);
		public String content;
		public S2CGetAnnounce(String pcontent){
			content=pcontent;
		}
		public S2CGetAnnounce(){}
		@Override
		public String toString() {
			return "S2CGetAnnounce [content="+content+",]";
		}
		public static final int msgCode = 60;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 60);
			retMsg.writeString(content);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SSelectServer{
		private static final Logger logger = LoggerFactory.getLogger(C2SSelectServer.class);
		public int server_id;
		public long session_id;
		@Override
		public String toString() {
			return "C2SSelectServer [server_id="+server_id+",session_id="+session_id+",]";
		}
		public static final int id = 61;

		public static C2SSelectServer parse(MyRequestMessage request){
			C2SSelectServer retObj = new C2SSelectServer();
			try{
			retObj.server_id=request.readInt();
			retObj.session_id=request.readLong();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CSelectServer{
		private static final Logger logger = LoggerFactory.getLogger(S2CSelectServer.class);
		public int ret;
		public int server_id;
		public boolean has_role;
		public S2CSelectServer(int pret,int pserver_id,boolean phas_role){
			ret=pret;
			server_id=pserver_id;
			has_role=phas_role;
		}
		public S2CSelectServer(){}
		@Override
		public String toString() {
			return "S2CSelectServer [ret="+ret+",server_id="+server_id+",has_role="+has_role+",]";
		}
		public static final int msgCode = 62;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 62);
			retMsg.writeInt(ret);
			retMsg.writeInt(server_id);
			retMsg.writeBool(has_role);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SQunheiLogin{
		private static final Logger logger = LoggerFactory.getLogger(C2SQunheiLogin.class);
		public String username;
		public int serverid;
		public int isadult;
		public int time;
		public String flag;
		@Override
		public String toString() {
			return "C2SQunheiLogin [username="+username+",serverid="+serverid+",isadult="+isadult+",time="+time+",flag="+flag+",]";
		}
		public static final int id = 10031;

		public static C2SQunheiLogin parse(MyRequestMessage request){
			C2SQunheiLogin retObj = new C2SQunheiLogin();
			try{
			retObj.username=request.readString();
			retObj.serverid=request.readInt();
			retObj.isadult=request.readInt();
			retObj.time=request.readInt();
			retObj.flag=request.readString();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CH5OpenLogin{
		private static final Logger logger = LoggerFactory.getLogger(S2CH5OpenLogin.class);
		public long session_id;
		public S2CH5OpenLogin(long psession_id){
			session_id=psession_id;
		}
		public S2CH5OpenLogin(){}
		@Override
		public String toString() {
			return "S2CH5OpenLogin [session_id="+session_id+",]";
		}
		public static final int msgCode = 10032;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 10032);
			retMsg.writeLong(session_id);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SWanbaLogin{
		private static final Logger logger = LoggerFactory.getLogger(C2SWanbaLogin.class);
		public String appid;
		public String openid;
		public String openkey;
		public int platform;
		public String pf;
		@Override
		public String toString() {
			return "C2SWanbaLogin [appid="+appid+",openid="+openid+",openkey="+openkey+",platform="+platform+",pf="+pf+",]";
		}
		public static final int id = 10033;

		public static C2SWanbaLogin parse(MyRequestMessage request){
			C2SWanbaLogin retObj = new C2SWanbaLogin();
			try{
			retObj.appid=request.readString();
			retObj.openid=request.readString();
			retObj.openkey=request.readString();
			retObj.platform=request.readInt();
			retObj.pf=request.readString();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class C2SZhangMengLogin{
		private static final Logger logger = LoggerFactory.getLogger(C2SZhangMengLogin.class);
		public String uid;
		public String t;
		public String sign;
		@Override
		public String toString() {
			return "C2SZhangMengLogin [uid="+uid+",t="+t+",sign="+sign+",]";
		}
		public static final int id = 10035;

		public static C2SZhangMengLogin parse(MyRequestMessage request){
			C2SZhangMengLogin retObj = new C2SZhangMengLogin();
			try{
			retObj.uid=request.readString();
			retObj.t=request.readString();
			retObj.sign=request.readString();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class C2SThree33Login{
		private static final Logger logger = LoggerFactory.getLogger(C2SThree33Login.class);
		public int gameId;
		public int time;
		public int uid;
		public String userName;
		public String sign;
		@Override
		public String toString() {
			return "C2SThree33Login [gameId="+gameId+",time="+time+",uid="+uid+",userName="+userName+",sign="+sign+",]";
		}
		public static final int id = 10037;

		public static C2SThree33Login parse(MyRequestMessage request){
			C2SThree33Login retObj = new C2SThree33Login();
			try{
			retObj.gameId=request.readInt();
			retObj.time=request.readInt();
			retObj.uid=request.readInt();
			retObj.userName=request.readString();
			retObj.sign=request.readString();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class C2SChongChongLogin{
		private static final Logger logger = LoggerFactory.getLogger(C2SChongChongLogin.class);
		public String userId;
		public String time;
		public String sign;
		@Override
		public String toString() {
			return "C2SChongChongLogin [userId="+userId+",time="+time+",sign="+sign+",]";
		}
		public static final int id = 10039;

		public static C2SChongChongLogin parse(MyRequestMessage request){
			C2SChongChongLogin retObj = new C2SChongChongLogin();
			try{
			retObj.userId=request.readString();
			retObj.time=request.readString();
			retObj.sign=request.readString();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class C2S4399Login{
		private static final Logger logger = LoggerFactory.getLogger(C2S4399Login.class);
		public int gameId;
		public int time;
		public String userId;
		public String userName;
		public String sign;
		@Override
		public String toString() {
			return "C2S4399Login [gameId="+gameId+",time="+time+",userId="+userId+",userName="+userName+",sign="+sign+",]";
		}
		public static final int id = 10041;

		public static C2S4399Login parse(MyRequestMessage request){
			C2S4399Login retObj = new C2S4399Login();
			try{
			retObj.gameId=request.readInt();
			retObj.time=request.readInt();
			retObj.userId=request.readString();
			retObj.userName=request.readString();
			retObj.sign=request.readString();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class C2SYiJieLogin{
		private static final Logger logger = LoggerFactory.getLogger(C2SYiJieLogin.class);
		public String appId;
		public String channelId;
		public String userId;
		public String token;
		@Override
		public String toString() {
			return "C2SYiJieLogin [appId="+appId+",channelId="+channelId+",userId="+userId+",token="+token+",]";
		}
		public static final int id = 10043;

		public static C2SYiJieLogin parse(MyRequestMessage request){
			C2SYiJieLogin retObj = new C2SYiJieLogin();
			try{
			retObj.appId=request.readString();
			retObj.channelId=request.readString();
			retObj.userId=request.readString();
			retObj.token=request.readString();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class C2SgNetTopLogin{
		private static final Logger logger = LoggerFactory.getLogger(C2SgNetTopLogin.class);
		public String userId;
		public String token;
		@Override
		public String toString() {
			return "C2SgNetTopLogin [userId="+userId+",token="+token+",]";
		}
		public static final int id = 10045;

		public static C2SgNetTopLogin parse(MyRequestMessage request){
			C2SgNetTopLogin retObj = new C2SgNetTopLogin();
			try{
			retObj.userId=request.readString();
			retObj.token=request.readString();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class C2SYunbeeLogin{
		private static final Logger logger = LoggerFactory.getLogger(C2SYunbeeLogin.class);
		public String user_id;
		public String token;
		@Override
		public String toString() {
			return "C2SYunbeeLogin [user_id="+user_id+",token="+token+",]";
		}
		public static final int id = 10047;

		public static C2SYunbeeLogin parse(MyRequestMessage request){
			C2SYunbeeLogin retObj = new C2SYunbeeLogin();
			try{
			retObj.user_id=request.readString();
			retObj.token=request.readString();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class C2SLiuyeLogin1{
		private static final Logger logger = LoggerFactory.getLogger(C2SLiuyeLogin1.class);
		public String mem_id;
		public String user_token;
		@Override
		public String toString() {
			return "C2SLiuyeLogin1 [mem_id="+mem_id+",user_token="+user_token+",]";
		}
		public static final int id = 10049;

		public static C2SLiuyeLogin1 parse(MyRequestMessage request){
			C2SLiuyeLogin1 retObj = new C2SLiuyeLogin1();
			try{
			retObj.mem_id=request.readString();
			retObj.user_token=request.readString();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class C2SLiuyeLogin2{
		private static final Logger logger = LoggerFactory.getLogger(C2SLiuyeLogin2.class);
		public String mem_id;
		public String user_token;
		@Override
		public String toString() {
			return "C2SLiuyeLogin2 [mem_id="+mem_id+",user_token="+user_token+",]";
		}
		public static final int id = 10051;

		public static C2SLiuyeLogin2 parse(MyRequestMessage request){
			C2SLiuyeLogin2 retObj = new C2SLiuyeLogin2();
			try{
			retObj.mem_id=request.readString();
			retObj.user_token=request.readString();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class C2SPingPingLogin{
		private static final Logger logger = LoggerFactory.getLogger(C2SPingPingLogin.class);
		public String game_id;
		public String user_code;
		public String login_token;
		public String game_key;
		@Override
		public String toString() {
			return "C2SPingPingLogin [game_id="+game_id+",user_code="+user_code+",login_token="+login_token+",game_key="+game_key+",]";
		}
		public static final int id = 10053;

		public static C2SPingPingLogin parse(MyRequestMessage request){
			C2SPingPingLogin retObj = new C2SPingPingLogin();
			try{
			retObj.game_id=request.readString();
			retObj.user_code=request.readString();
			retObj.login_token=request.readString();
			retObj.game_key=request.readString();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class C2SXingTengLogin{
		private static final Logger logger = LoggerFactory.getLogger(C2SXingTengLogin.class);
		public String app_id;
		public String uin;
		public String login_token;
		@Override
		public String toString() {
			return "C2SXingTengLogin [app_id="+app_id+",uin="+uin+",login_token="+login_token+",]";
		}
		public static final int id = 10055;

		public static C2SXingTengLogin parse(MyRequestMessage request){
			C2SXingTengLogin retObj = new C2SXingTengLogin();
			try{
			retObj.app_id=request.readString();
			retObj.uin=request.readString();
			retObj.login_token=request.readString();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class C2SXingTengLogin2{
		private static final Logger logger = LoggerFactory.getLogger(C2SXingTengLogin2.class);
		public String app_id;
		public String uin;
		public String login_token;
		public String app_key;
		@Override
		public String toString() {
			return "C2SXingTengLogin2 [app_id="+app_id+",uin="+uin+",login_token="+login_token+",app_key="+app_key+",]";
		}
		public static final int id = 10057;

		public static C2SXingTengLogin2 parse(MyRequestMessage request){
			C2SXingTengLogin2 retObj = new C2SXingTengLogin2();
			try{
			retObj.app_id=request.readString();
			retObj.uin=request.readString();
			retObj.login_token=request.readString();
			retObj.app_key=request.readString();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class C2SChuangFuLogin{
		private static final Logger logger = LoggerFactory.getLogger(C2SChuangFuLogin.class);
		public String app_id;
		public String uin;
		public String login_token;
		public String app_key;
		@Override
		public String toString() {
			return "C2SChuangFuLogin [app_id="+app_id+",uin="+uin+",login_token="+login_token+",app_key="+app_key+",]";
		}
		public static final int id = 10059;

		public static C2SChuangFuLogin parse(MyRequestMessage request){
			C2SChuangFuLogin retObj = new C2SChuangFuLogin();
			try{
			retObj.app_id=request.readString();
			retObj.uin=request.readString();
			retObj.login_token=request.readString();
			retObj.app_key=request.readString();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class C2SQuLeLeLogin{
		private static final Logger logger = LoggerFactory.getLogger(C2SQuLeLeLogin.class);
		public String app_id;
		public String user_id;
		public String session_id;
		@Override
		public String toString() {
			return "C2SQuLeLeLogin [app_id="+app_id+",user_id="+user_id+",session_id="+session_id+",]";
		}
		public static final int id = 10061;

		public static C2SQuLeLeLogin parse(MyRequestMessage request){
			C2SQuLeLeLogin retObj = new C2SQuLeLeLogin();
			try{
			retObj.app_id=request.readString();
			retObj.user_id=request.readString();
			retObj.session_id=request.readString();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class C2SMiaoRenLogin{
		private static final Logger logger = LoggerFactory.getLogger(C2SMiaoRenLogin.class);
		public String app_id;
		public String account_id;
		public String token_key;
		@Override
		public String toString() {
			return "C2SMiaoRenLogin [app_id="+app_id+",account_id="+account_id+",token_key="+token_key+",]";
		}
		public static final int id = 10063;

		public static C2SMiaoRenLogin parse(MyRequestMessage request){
			C2SMiaoRenLogin retObj = new C2SMiaoRenLogin();
			try{
			retObj.app_id=request.readString();
			retObj.account_id=request.readString();
			retObj.token_key=request.readString();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class C2SQuickLogin{
		private static final Logger logger = LoggerFactory.getLogger(C2SQuickLogin.class);
		public String token;
		public String product_code;
		public String uid;
		public String channel_code;
		@Override
		public String toString() {
			return "C2SQuickLogin [token="+token+",product_code="+product_code+",uid="+uid+",channel_code="+channel_code+",]";
		}
		public static final int id = 10065;

		public static C2SQuickLogin parse(MyRequestMessage request){
			C2SQuickLogin retObj = new C2SQuickLogin();
			try{
			retObj.token=request.readString();
			retObj.product_code=request.readString();
			retObj.uid=request.readString();
			retObj.channel_code=request.readString();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class C2S185syLogin{
		private static final Logger logger = LoggerFactory.getLogger(C2S185syLogin.class);
		public String token;
		public String userID;
		@Override
		public String toString() {
			return "C2S185syLogin [token="+token+",userID="+userID+",]";
		}
		public static final int id = 10067;

		public static C2S185syLogin parse(MyRequestMessage request){
			C2S185syLogin retObj = new C2S185syLogin();
			try{
			retObj.token=request.readString();
			retObj.userID=request.readString();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
}
