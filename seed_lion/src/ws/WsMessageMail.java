package ws;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.netty.buffer.ByteBufAllocator;
import java.util.List;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.MySendToMessage;
import ws.WsMessageBase.IOMailInfo;
import ws.WsMessageBase.IOMailFrom;
import ws.WsMessageBase.IOMailAttach;

public final class WsMessageMail{
	public static final class C2SMailList{
		private static final Logger logger = LoggerFactory.getLogger(C2SMailList.class);
		public static final int id = 701;
	}
	public static final class S2CMailList{
		private static final Logger logger = LoggerFactory.getLogger(S2CMailList.class);
		public List<IOMailInfo> list;
		@Override
		public String toString() {
			return "S2CMailList [list="+list+",]";
		}
		public static final int msgCode = 702;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 702);
			if(list == null || list.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(list.size());
				for(IOMailInfo list1 : list){
					retMsg.writeInt(list1.id);
					if(list1.from == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeString(list1.from.type);
					retMsg.writeInt(list1.from.fid);
					retMsg.writeInt(list1.from.legion);
			}
					retMsg.writeInt(list1.type);
					retMsg.writeInt(list1.state);
					retMsg.writeString(list1.title);
					retMsg.writeString(list1.content);
					if(list1.reward == null || list1.reward.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(list1.reward.size());
				for(IOMailAttach list1_reward1 : list1.reward){
					retMsg.writeInt(list1_reward1.gsid);
					retMsg.writeInt(list1_reward1.count);
				}
			}
					retMsg.writeLong(list1.stime);
					retMsg.writeLong(list1.etime);
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SGetMailAttach{
		private static final Logger logger = LoggerFactory.getLogger(C2SGetMailAttach.class);
		public int[] mail_id;
		@Override
		public String toString() {
			return "C2SGetMailAttach [mail_id="+java.util.Arrays.toString(mail_id)+",]";
		}
		public static final int id = 703;

		public static C2SGetMailAttach parse(MyRequestMessage request){
			C2SGetMailAttach retObj = new C2SGetMailAttach();
			try{
			int mail_id_size = request.readInt();
				retObj.mail_id = new int[mail_id_size];
				for(int i=0;i<mail_id_size;i++){
					retObj.mail_id[i]=request.readInt();
				}
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CGetMailAttach{
		private static final Logger logger = LoggerFactory.getLogger(S2CGetMailAttach.class);
		public List<IOMailAttach> rewards;
		@Override
		public String toString() {
			return "S2CGetMailAttach [rewards="+rewards+",]";
		}
		public static final int msgCode = 704;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 704);
			if(rewards == null || rewards.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(rewards.size());
				for(IOMailAttach rewards1 : rewards){
					retMsg.writeInt(rewards1.gsid);
					retMsg.writeInt(rewards1.count);
				}
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SSetMailRead{
		private static final Logger logger = LoggerFactory.getLogger(C2SSetMailRead.class);
		public int[] mail_id;
		@Override
		public String toString() {
			return "C2SSetMailRead [mail_id="+java.util.Arrays.toString(mail_id)+",]";
		}
		public static final int id = 705;

		public static C2SSetMailRead parse(MyRequestMessage request){
			C2SSetMailRead retObj = new C2SSetMailRead();
			try{
			int mail_id_size = request.readInt();
				retObj.mail_id = new int[mail_id_size];
				for(int i=0;i<mail_id_size;i++){
					retObj.mail_id[i]=request.readInt();
				}
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CSetMailRead{
		private static final Logger logger = LoggerFactory.getLogger(S2CSetMailRead.class);
		public static final int msgCode = 706;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 706);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SDelMail{
		private static final Logger logger = LoggerFactory.getLogger(C2SDelMail.class);
		public int[] mail_id;
		@Override
		public String toString() {
			return "C2SDelMail [mail_id="+java.util.Arrays.toString(mail_id)+",]";
		}
		public static final int id = 707;

		public static C2SDelMail parse(MyRequestMessage request){
			C2SDelMail retObj = new C2SDelMail();
			try{
			int mail_id_size = request.readInt();
				retObj.mail_id = new int[mail_id_size];
				for(int i=0;i<mail_id_size;i++){
					retObj.mail_id[i]=request.readInt();
				}
			}catch(Exception e){logger.error("bulid protocol error!",e);}
			return retObj;
		}
	}
	public static final class S2CDelMail{
		private static final Logger logger = LoggerFactory.getLogger(S2CDelMail.class);
		public static final int msgCode = 708;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 708);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class PushNewMail{
		private static final Logger logger = LoggerFactory.getLogger(PushNewMail.class);
		public IOMailInfo mail_info;
		@Override
		public String toString() {
			return "PushNewMail [mail_info="+mail_info+",]";
		}
		public static final int msgCode = 710;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 710);
			if(mail_info == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeInt(mail_info.id);
					if(mail_info.from == null){
				retMsg.writeBool(false);
			}else{
				retMsg.writeBool(true);
					retMsg.writeString(mail_info.from.type);
					retMsg.writeInt(mail_info.from.fid);
					retMsg.writeInt(mail_info.from.legion);
			}
					retMsg.writeInt(mail_info.type);
					retMsg.writeInt(mail_info.state);
					retMsg.writeString(mail_info.title);
					retMsg.writeString(mail_info.content);
					if(mail_info.reward == null || mail_info.reward.size() == 0){
				retMsg.writeInt(0);
			}else{
				retMsg.writeInt(mail_info.reward.size());
				for(IOMailAttach mail_info_reward1 : mail_info.reward){
					retMsg.writeInt(mail_info_reward1.gsid);
					retMsg.writeInt(mail_info_reward1.count);
				}
			}
					retMsg.writeLong(mail_info.stime);
					retMsg.writeLong(mail_info.etime);
			}
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
	public static final class C2SMailRedNotice{
		private static final Logger logger = LoggerFactory.getLogger(C2SMailRedNotice.class);
		public static final int id = 711;
	}
	public static final class S2CMailRedNotice{
		private static final Logger logger = LoggerFactory.getLogger(S2CMailRedNotice.class);
		public boolean ret;
		public S2CMailRedNotice(boolean pret){
			ret=pret;
		}
		public S2CMailRedNotice(){}
		@Override
		public String toString() {
			return "S2CMailRedNotice [ret="+ret+",]";
		}
		public static final int msgCode = 712;
		public MySendToMessage build(ByteBufAllocator alloc){
			try{
			MySendToMessage retMsg = new MySendToMessage(alloc, 712);
			retMsg.writeBool(ret);
			return retMsg;
			}catch(Exception e){logger.error("bulid protocol error!",e);return null;}
		}
	}
}
