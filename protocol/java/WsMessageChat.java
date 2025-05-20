package ws;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.netty.buffer.ByteBufAllocator;
import java.util.List;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.MySendToMessage;

public final class WsMessageChat{
	public static final class C2SChat{
		private static final Logger logger = LoggerFactory.getLogger(C2SChat.class);
		public int msgtype;
		public String content;
		@Override
		public String toString() {
			return "C2SChat [msgtype="+msgtype+",content="+content+",]";
		}
		public static final int id = 601;

		public static C2SChat parse(MyRequestMessage request){
			C2SChat retObj = new C2SChat();
			try{
			retObj.msgtype=request.readInt();
			retObj.content=request.readString();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CChat{
		private static final Logger logger = LoggerFactory.getLogger(S2CChat.class);
		public static final int msgCode = 602;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 602);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class S2CChatPush{
		private static final Logger logger = LoggerFactory.getLogger(S2CChatPush.class);
		public int msgtype;
		public String senderid;
		public int rid;
		public String rname;
		public int iconid;
		public int headid;
		public int frameid;
		public int level;
		public int vip;
		public int office_index;
		public int serverid;
		public String content;
		public long videoId;
		public long send_time;
		public int id;
		public S2CChatPush(int pmsgtype,String psenderid,int prid,String prname,int piconid,int pheadid,int pframeid,int plevel,int pvip,int poffice_index,int pserverid,String pcontent,long pvideoId,long psend_time,int pid){
			msgtype=pmsgtype;
			senderid=psenderid;
			rid=prid;
			rname=prname;
			iconid=piconid;
			headid=pheadid;
			frameid=pframeid;
			level=plevel;
			vip=pvip;
			office_index=poffice_index;
			serverid=pserverid;
			content=pcontent;
			videoId=pvideoId;
			send_time=psend_time;
			id=pid;
		}
		public S2CChatPush(){}
		@Override
		public String toString() {
			return "S2CChatPush [msgtype="+msgtype+",senderid="+senderid+",rid="+rid+",rname="+rname+",iconid="+iconid+",headid="+headid+",frameid="+frameid+",level="+level+",vip="+vip+",office_index="+office_index+",serverid="+serverid+",content="+content+",videoId="+videoId+",send_time="+send_time+",id="+id+",]";
		}
		public static final int msgCode = 604;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 604);
			retMsg.writeInt(msgtype);
			retMsg.writeString(senderid);
			retMsg.writeInt(rid);
			retMsg.writeString(rname);
			retMsg.writeInt(iconid);
			retMsg.writeInt(headid);
			retMsg.writeInt(frameid);
			retMsg.writeInt(level);
			retMsg.writeInt(vip);
			retMsg.writeInt(office_index);
			retMsg.writeInt(serverid);
			retMsg.writeString(content);
			retMsg.writeLong(videoId);
			retMsg.writeLong(send_time);
			retMsg.writeInt(id);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SChatView{
		private static final Logger logger = LoggerFactory.getLogger(C2SChatView.class);
		public int chat_type;
		@Override
		public String toString() {
			return "C2SChatView [chat_type="+chat_type+",]";
		}
		public static final int id = 605;

		public static C2SChatView parse(MyRequestMessage request){
			C2SChatView retObj = new C2SChatView();
			try{
			retObj.chat_type=request.readInt();
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CChatView{
		private static final Logger logger = LoggerFactory.getLogger(S2CChatView.class);
		public int chat_type;
		public List<S2CChatPush> chat_content;
		public List<Boolean> new_msg_list;
		@Override
		public String toString() {
			return "S2CChatView [chat_type="+chat_type+",chat_content="+chat_content+",new_msg_list="+new_msg_list+",]";
		}
		public static final int msgCode = 606;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 606);
			retMsg.writeInt(chat_type);
			if(chat_content == null || chat_content.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(chat_content.size());
				for(S2CChatPush chat_content1 : chat_content){
					retMsg.writeInt(chat_content1.msgtype);
					retMsg.writeString(chat_content1.senderid);
					retMsg.writeInt(chat_content1.rid);
					retMsg.writeString(chat_content1.rname);
					retMsg.writeInt(chat_content1.iconid);
					retMsg.writeInt(chat_content1.headid);
					retMsg.writeInt(chat_content1.frameid);
					retMsg.writeInt(chat_content1.level);
					retMsg.writeInt(chat_content1.vip);
					retMsg.writeInt(chat_content1.office_index);
					retMsg.writeInt(chat_content1.serverid);
					retMsg.writeString(chat_content1.content);
					retMsg.writeLong(chat_content1.videoId);
					retMsg.writeLong(chat_content1.send_time);
					retMsg.writeInt(chat_content1.id);
				}
			}
			if(new_msg_list == null || new_msg_list.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(new_msg_list.size());
				for(Boolean new_msg_list1 : new_msg_list){
			retMsg.writeBool(new_msg_list1);
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SChatVisit{
		private static final Logger logger = LoggerFactory.getLogger(C2SChatVisit.class);
		public static final int id = 607;
	}
	public static final class S2CChatVisit{
		private static final Logger logger = LoggerFactory.getLogger(S2CChatVisit.class);
		public static final int msgCode = 608;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 608);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SChatNewmsg{
		private static final Logger logger = LoggerFactory.getLogger(C2SChatNewmsg.class);
		public static final int id = 609;
	}
	public static final class S2CChatNewmsg{
		private static final Logger logger = LoggerFactory.getLogger(S2CChatNewmsg.class);
		public boolean has_newmsg;
		public S2CChatNewmsg(boolean phas_newmsg){
			has_newmsg=phas_newmsg;
		}
		public S2CChatNewmsg(){}
		@Override
		public String toString() {
			return "S2CChatNewmsg [has_newmsg="+has_newmsg+",]";
		}
		public static final int msgCode = 610;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 610);
			retMsg.writeBool(has_newmsg);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
}
