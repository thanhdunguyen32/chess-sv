package ws;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.netty.buffer.ByteBufAllocator;
import java.util.List;
import java.util.Map;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.MySendToMessage;
public final class WsMessageBase{
	public static final class IOMailFrom{
		public String type;
		public int fid;
		public int legion;
		public IOMailFrom(String ptype,int pfid,int plegion){
			this.type=ptype;
			this.fid=pfid;
			this.legion=plegion;
		}
		public IOMailFrom(){}
		@Override
		public String toString() {
			return "IOMailFrom [type="+type+",fid="+fid+",legion="+legion+",]";
		}
	}
	public static final class IOGeneralSimple{
		public int pos;
		public int gsid;
		public int level;
		public int hpcover;
		public int pclass;
		public long nowhp;
		public float exhp;
		public float exatk;
		public IOGeneralSimple(int ppos,int pgsid,int plevel,int phpcover,int ppclass,long pnowhp,float pexhp,float pexatk){
			this.pos=ppos;
			this.gsid=pgsid;
			this.level=plevel;
			this.hpcover=phpcover;
			this.pclass=ppclass;
			this.nowhp=pnowhp;
			this.exhp=pexhp;
			this.exatk=pexatk;
		}
		public IOGeneralSimple(){}
		@Override
		public String toString() {
			return "IOGeneralSimple [pos="+pos+",gsid="+gsid+",level="+level+",hpcover="+hpcover+",pclass="+pclass+",nowhp="+nowhp+",exhp="+exhp+",exatk="+exatk+",]";
		}
	}
	public static final class IOTnqwEvent{
		public int mark;
		public int limit;
		public String intro;
		public IOTnqwEvent(int pmark,int plimit,String pintro){
			this.mark=pmark;
			this.limit=plimit;
			this.intro=pintro;
		}
		public IOTnqwEvent(){}
		@Override
		public String toString() {
			return "IOTnqwEvent [mark="+mark+",limit="+limit+",intro="+intro+",]";
		}
	}
	public static final class IOLegionFactoryDonation{
		public String name;
		public int icon;
		public int headid;
		public int frameid;
		public int score;
		public int pos;
		public long last;
		public IOLegionFactoryDonation(String pname,int picon,int pheadid,int pframeid,int pscore,int ppos,long plast){
			this.name=pname;
			this.icon=picon;
			this.headid=pheadid;
			this.frameid=pframeid;
			this.score=pscore;
			this.pos=ppos;
			this.last=plast;
		}
		public IOLegionFactoryDonation(){}
		@Override
		public String toString() {
			return "IOLegionFactoryDonation [name="+name+",icon="+icon+",headid="+headid+",frameid="+frameid+",score="+score+",pos="+pos+",last="+last+",]";
		}
	}
	public static final class IOBattleSet{
		public int mythic;
		public int power;
		public Map<Integer,IOGeneralBean> team;
		@Override
		public String toString() {
			return "IOBattleSet [mythic="+mythic+",power="+power+",team="+team+",]";
		}
	}
	public static final class IODungeonPotion{
		public int id;
		public int count;
		public IODungeonPotion(int pid,int pcount){
			this.id=pid;
			this.count=pcount;
		}
		public IODungeonPotion(){}
		@Override
		public String toString() {
			return "IODungeonPotion [id="+id+",count="+count+",]";
		}
	}
	public static final class IOMjbgFinal1{
		public int gsid;
		public int count;
		public int maxnum;
		public IOMjbgFinal1(int pgsid,int pcount,int pmaxnum){
			this.gsid=pgsid;
			this.count=pcount;
			this.maxnum=pmaxnum;
		}
		public IOMjbgFinal1(){}
		@Override
		public String toString() {
			return "IOMjbgFinal1 [gsid="+gsid+",count="+count+",maxnum="+maxnum+",]";
		}
	}
	public static final class IORewardItemSelect{
		public int GSID;
		public int COUNT;
		public boolean real;
		public IORewardItemSelect(int pGSID,int pCOUNT,boolean preal){
			this.GSID=pGSID;
			this.COUNT=pCOUNT;
			this.real=preal;
		}
		public IORewardItemSelect(){}
		@Override
		public String toString() {
			return "IORewardItemSelect [GSID="+GSID+",COUNT="+COUNT+",real="+real+",]";
		}
	}
	public static final class IOMineHolder{
		public int rid;
		public String rname;
		public int level;
		public int iconid;
		public int frameid;
		public int fight;
		public int cd_time;
		public IOMineHolder(int prid,String prname,int plevel,int piconid,int pframeid,int pfight,int pcd_time){
			this.rid=prid;
			this.rname=prname;
			this.level=plevel;
			this.iconid=piconid;
			this.frameid=pframeid;
			this.fight=pfight;
			this.cd_time=pcd_time;
		}
		public IOMineHolder(){}
		@Override
		public String toString() {
			return "IOMineHolder [rid="+rid+",rname="+rname+",level="+level+",iconid="+iconid+",frameid="+frameid+",fight="+fight+",cd_time="+cd_time+",]";
		}
	}
	public static final class AwardItem{
		public int gsid;
		public int count;
		public AwardItem(int pgsid,int pcount){
			this.gsid=pgsid;
			this.count=pcount;
		}
		public AwardItem(){}
		@Override
		public String toString() {
			return "AwardItem [gsid="+gsid+",count="+count+",]";
		}
	}
	public static final class IOBattleReport{
		public List<IOBattleReportItem> left;
		public List<IOBattleReportItem> right;
		@Override
		public String toString() {
			return "IOBattleReport [left="+left+",right="+right+",]";
		}
	}
	public static final class IOGuozhanHistory{
		public int action_type;
		public String target_player_name;
		public List<Integer> params;
		public int add_time;
		@Override
		public String toString() {
			return "IOGuozhanHistory [action_type="+action_type+",target_player_name="+target_player_name+",params="+params+",add_time="+add_time+",]";
		}
	}
	public static final class IORankPlayer{
		public int rid;
		public String rname;
		public int iconid;
		public int headid;
		public int frameid;
		public int level;
		public int power;
		public int vip;
		public int rank_change;
		public int hero_stars;
		public int win_count;
		public int score;
		public int damage;
		public int tower;
		public int like_count;
		public int chapter;
		public int node;
		public int stage;
		public int star;
		public IORankPlayer(int prid,String prname,int piconid,int pheadid,int pframeid,int plevel,int ppower,int pvip,int prank_change,int phero_stars,int pwin_count,int pscore,int pdamage,int ptower,int plike_count,int pchapter,int pnode,int pstage,int pstar){
			this.rid=prid;
			this.rname=prname;
			this.iconid=piconid;
			this.headid=pheadid;
			this.frameid=pframeid;
			this.level=plevel;
			this.power=ppower;
			this.vip=pvip;
			this.rank_change=prank_change;
			this.hero_stars=phero_stars;
			this.win_count=pwin_count;
			this.score=pscore;
			this.damage=pdamage;
			this.tower=ptower;
			this.like_count=plike_count;
			this.chapter=pchapter;
			this.node=pnode;
			this.stage=pstage;
			this.star=pstar;
		}
		public IORankPlayer(){}
		@Override
		public String toString() {
			return "IORankPlayer [rid="+rid+",rname="+rname+",iconid="+iconid+",headid="+headid+",frameid="+frameid+",level="+level+",power="+power+",vip="+vip+",rank_change="+rank_change+",hero_stars="+hero_stars+",win_count="+win_count+",score="+score+",damage="+damage+",tower="+tower+",like_count="+like_count+",chapter="+chapter+",node="+node+",stage="+stage+",star="+star+",]";
		}
	}
	public static final class IORecruitFree{
		public long normal;
		public long premium;
		public IORecruitFree(long pnormal,long ppremium){
			this.normal=pnormal;
			this.premium=ppremium;
		}
		public IORecruitFree(){}
		@Override
		public String toString() {
			return "IORecruitFree [normal="+normal+",premium="+premium+",]";
		}
	}
	public static final class IOPvpRecord{
		public long videoid;
		public long time;
		public long version;
		public long seed;
		public String result;
		public IOBattleRecordSeason season;
		public Map<Integer,Integer> lper;
		public Map<Integer,Integer> rper;
		public int ltper;
		public int rtper;
		public IOBattleReport report;
		public IOBattleRecordSide left;
		public IOBattleRecordSide right;
		public String mark;
		@Override
		public String toString() {
			return "IOPvpRecord [videoid="+videoid+",time="+time+",version="+version+",seed="+seed+",result="+result+",season="+season+",lper="+lper+",rper="+rper+",ltper="+ltper+",rtper="+rtper+",report="+report+",left="+left+",right="+right+",mark="+mark+",]";
		}
	}
	public static final class IOSecretBoxAward{
		public int stage_index;
		public List<SecretItemInfo> award_list;
		public int is_get;
		@Override
		public String toString() {
			return "IOSecretBoxAward [stage_index="+stage_index+",award_list="+award_list+",is_get="+is_get+",]";
		}
	}
	public static final class IOSrenderState{
		public int gsid;
		public int loyal;
		public int state;
		public IOSrenderState(int pgsid,int ployal,int pstate){
			this.gsid=pgsid;
			this.loyal=ployal;
			this.state=pstate;
		}
		public IOSrenderState(){}
		@Override
		public String toString() {
			return "IOSrenderState [gsid="+gsid+",loyal="+loyal+",state="+state+",]";
		}
	}
	public static final class IOLegionBoss{
		public int chapter;
		public String name;
		public List<IORewardItem> rewards;
		public Map<Integer,IOGeneralLegion> bset;
		public long maxhp;
		public long nowhp;
		@Override
		public String toString() {
			return "IOLegionBoss [chapter="+chapter+",name="+name+",rewards="+rewards+",bset="+bset+",maxhp="+maxhp+",nowhp="+nowhp+",]";
		}
	}
	public static final class IOManorFieldEnemy{
		public List<IORewardItem> boxItem;
		public List<IORewardItem> reward;
		public int id;
		public int state;
		public int hasOpen;
		public List<Integer> cachehp;
		@Override
		public String toString() {
			return "IOManorFieldEnemy [boxItem="+boxItem+",reward="+reward+",id="+id+",state="+state+",hasOpen="+hasOpen+",cachehp="+cachehp+",]";
		}
	}
	public static final class IOSpecial{
		public long bw;
		public long qz;
		public long zx;
		public long lm;
		public long yd;
		public long cz;
		public long gz;
		public long zz;
		public IOSpecial(long pbw,long pqz,long pzx,long plm,long pyd,long pcz,long pgz,long pzz){
			this.bw=pbw;
			this.qz=pqz;
			this.zx=pzx;
			this.lm=plm;
			this.yd=pyd;
			this.cz=pcz;
			this.gz=pgz;
			this.zz=pzz;
		}
		public IOSpecial(){}
		@Override
		public String toString() {
			return "IOSpecial [bw="+bw+",qz="+qz+",zx="+zx+",lm="+lm+",yd="+yd+",cz="+cz+",gz="+gz+",zz="+zz+",]";
		}
	}
	public static final class IOMjbgItem{
		public int gsid;
		public int count;
		public int num;
		public IOMjbgItem(int pgsid,int pcount,int pnum){
			this.gsid=pgsid;
			this.count=pcount;
			this.num=pnum;
		}
		public IOMjbgItem(){}
		@Override
		public String toString() {
			return "IOMjbgItem [gsid="+gsid+",count="+count+",num="+num+",]";
		}
	}
	public static final class IOWorldBossLegion{
		public int rank;
		public int maxrank;
		public long damage;
		public IOWorldBossLegion(int prank,int pmaxrank,long pdamage){
			this.rank=prank;
			this.maxrank=pmaxrank;
			this.damage=pdamage;
		}
		public IOWorldBossLegion(){}
		@Override
		public String toString() {
			return "IOWorldBossLegion [rank="+rank+",maxrank="+maxrank+",damage="+damage+",]";
		}
	}
	public static final class IOBattleRecordSeason{
		public int season;
		public int left;
		public int right;
		public List<Integer> pos;
		@Override
		public String toString() {
			return "IOBattleRecordSeason [season="+season+",left="+left+",right="+right+",pos="+pos+",]";
		}
	}
	public static final class IOTeamInfo{
		public int type;
		public List<Long> pos_card_uuid;
		public int pet_id;
		@Override
		public String toString() {
			return "IOTeamInfo [type="+type+",pos_card_uuid="+pos_card_uuid+",pet_id="+pet_id+",]";
		}
	}
	public static final class IOLegionFactoryMission{
		public long key;
		public int id;
		public long stime;
		public long etime;
		public IOLegionFactoryMission(long pkey,int pid,long pstime,long petime){
			this.key=pkey;
			this.id=pid;
			this.stime=pstime;
			this.etime=petime;
		}
		public IOLegionFactoryMission(){}
		@Override
		public String toString() {
			return "IOLegionFactoryMission [key="+key+",id="+id+",stime="+stime+",etime="+etime+",]";
		}
	}
	public static final class IODjrwChk{
		public int MARK;
		public int NUM;
		public IODjrwChk(int pMARK,int pNUM){
			this.MARK=pMARK;
			this.NUM=pNUM;
		}
		public IODjrwChk(){}
		@Override
		public String toString() {
			return "IODjrwChk [MARK="+MARK+",NUM="+NUM+",]";
		}
	}
	public static final class IOExpedPlayer{
		public String rname;
		public int level;
		public int iconid;
		public int headid;
		public int frameid;
		public int power;
		public IOBattlesetEnemy battleset;
		@Override
		public String toString() {
			return "IOExpedPlayer [rname="+rname+",level="+level+",iconid="+iconid+",headid="+headid+",frameid="+frameid+",power="+power+",battleset="+battleset+",]";
		}
	}
	public static final class KvStringPair{
		public String key;
		public int val;
		public KvStringPair(String pkey,int pval){
			this.key=pkey;
			this.val=pval;
		}
		public KvStringPair(){}
		@Override
		public String toString() {
			return "KvStringPair [key="+key+",val="+val+",]";
		}
	}
	public static final class IOpstatus{
		public int send;
		public int receive;
		public IOpstatus(int psend,int preceive){
			this.send=psend;
			this.receive=preceive;
		}
		public IOpstatus(){}
		@Override
		public String toString() {
			return "IOpstatus [send="+send+",receive="+receive+",]";
		}
	}
	public static final class IOTnqwBosslist{
		public int status;
		public int actscore;
		public List<String> rewardgsids;
		@Override
		public String toString() {
			return "IOTnqwBosslist [status="+status+",actscore="+actscore+",rewardgsids="+rewardgsids+",]";
		}
	}
	public static final class IODungeonNode{
		public List<IODungeonNodePos> poslist;
		@Override
		public String toString() {
			return "IODungeonNode [poslist="+poslist+",]";
		}
	}
	public static final class IODungeonPosition{
		public int node;
		public int pos;
		public IODungeonPosition(int pnode,int ppos){
			this.node=pnode;
			this.pos=ppos;
		}
		public IODungeonPosition(){}
		@Override
		public String toString() {
			return "IODungeonPosition [node="+node+",pos="+pos+",]";
		}
	}
	public static final class IOBattleReportItem{
		public int gsid;
		public long hurm;
		public long heal;
		public int level;
		public int pclass;
		public IOBattleReportItem(int pgsid,long phurm,long pheal,int plevel,int ppclass){
			this.gsid=pgsid;
			this.hurm=phurm;
			this.heal=pheal;
			this.level=plevel;
			this.pclass=ppclass;
		}
		public IOBattleReportItem(){}
		@Override
		public String toString() {
			return "IOBattleReportItem [gsid="+gsid+",hurm="+hurm+",heal="+heal+",level="+level+",pclass="+pclass+",]";
		}
	}
	public static final class IOGuoZhanPvpPlayer{
		public GuozhanOfficePointPlayer base_info;
		public IOBattlesetEnemy battleset;
		@Override
		public String toString() {
			return "IOGuoZhanPvpPlayer [base_info="+base_info+",battleset="+battleset+",]";
		}
	}
	public static final class IOLegionBossSelf{
		public long lastdamge;
		public IOLegionBossSelf(long plastdamge){
			this.lastdamge=plastdamge;
		}
		public IOLegionBossSelf(){}
		@Override
		public String toString() {
			return "IOLegionBossSelf [lastdamge="+lastdamge+",]";
		}
	}
	public static final class IOStageInfo{
		public int schange;
		public int stage;
		public int star;
		public IOStageInfo(int pschange,int pstage,int pstar){
			this.schange=pschange;
			this.stage=pstage;
			this.star=pstar;
		}
		public IOStageInfo(){}
		@Override
		public String toString() {
			return "IOStageInfo [schange="+schange+",stage="+stage+",star="+star+",]";
		}
	}
	public static final class IOOcctaskPackinfo{
		public int ID;
		public List<RewardInfo> ITEMS;
		public int TYPE;
		public int VALUE;
		public int WEIGHT;
		@Override
		public String toString() {
			return "IOOcctaskPackinfo [ID="+ID+",ITEMS="+ITEMS+",TYPE="+TYPE+",VALUE="+VALUE+",WEIGHT="+WEIGHT+",]";
		}
	}
	public static final class IOAwardRandomGeneral{
		public int COUNT;
		public int STAR;
		public int CAMP;
		public int OCCU;
		public IOAwardRandomGeneral(int pCOUNT,int pSTAR,int pCAMP,int pOCCU){
			this.COUNT=pCOUNT;
			this.STAR=pSTAR;
			this.CAMP=pCAMP;
			this.OCCU=pOCCU;
		}
		public IOAwardRandomGeneral(){}
		@Override
		public String toString() {
			return "IOAwardRandomGeneral [COUNT="+COUNT+",STAR="+STAR+",CAMP="+CAMP+",OCCU="+OCCU+",]";
		}
	}
	public static final class IOFriendBoss{
		public int ownPlayerId;
		public int id;
		public int gsid;
		public String name;
		public int level;
		public List<IORewardItem> rewards;
		public long etime;
		public long last;
		public long maxhp;
		public long nowhp;
		public int lastdamges;
		public Map<Integer,IOGeneralSimple> bset;
		@Override
		public String toString() {
			return "IOFriendBoss [ownPlayerId="+ownPlayerId+",id="+id+",gsid="+gsid+",name="+name+",level="+level+",rewards="+rewards+",etime="+etime+",last="+last+",maxhp="+maxhp+",nowhp="+nowhp+",lastdamges="+lastdamges+",bset="+bset+",]";
		}
	}
	public static final class IOMapEvent{
		public String hash;
		public int type;
		public int eid;
		public IOMapEvent(String phash,int ptype,int peid){
			this.hash=phash;
			this.type=ptype;
			this.eid=peid;
		}
		public IOMapEvent(){}
		@Override
		public String toString() {
			return "IOMapEvent [hash="+hash+",type="+type+",eid="+eid+",]";
		}
	}
	public static final class IOGuozhanPointPlayer{
		public int rid;
		public String rname;
		public int level;
		public int iconid;
		public int frameid;
		public int fight;
		public IOGuozhanPointPlayer(int prid,String prname,int plevel,int piconid,int pframeid,int pfight){
			this.rid=prid;
			this.rname=prname;
			this.level=plevel;
			this.iconid=piconid;
			this.frameid=pframeid;
			this.fight=pfight;
		}
		public IOGuozhanPointPlayer(){}
		@Override
		public String toString() {
			return "IOGuozhanPointPlayer [rid="+rid+",rname="+rname+",level="+level+",iconid="+iconid+",frameid="+frameid+",fight="+fight+",]";
		}
	}
	public static final class KvIntPair{
		public int key;
		public int value;
		public KvIntPair(int pkey,int pvalue){
			this.key=pkey;
			this.value=pvalue;
		}
		public KvIntPair(){}
		@Override
		public String toString() {
			return "KvIntPair [key="+key+",value="+value+",]";
		}
	}
	public static final class IODungeonChooseDetail{
		public int id;
		public String name;
		public int gsid;
		public Map<Integer,IODungeonBset> set;
		public Map<Integer,Integer> hppercent;
		public List<Integer> buffs;
		public int disc;
		public List<IORewardItem> item;
		public List<IORewardItem> consume;
		public int quality;
		public int refnum;
		public List<IORewardItem> goods;
		@Override
		public String toString() {
			return "IODungeonChooseDetail [id="+id+",name="+name+",gsid="+gsid+",set="+set+",hppercent="+hppercent+",buffs="+buffs+",disc="+disc+",item="+item+",consume="+consume+",quality="+quality+",refnum="+refnum+",goods="+goods+",]";
		}
	}
	public static final class SimpleItemInfo{
		public int gsid;
		public int count;
		public SimpleItemInfo(int pgsid,int pcount){
			this.gsid=pgsid;
			this.count=pcount;
		}
		public SimpleItemInfo(){}
		@Override
		public String toString() {
			return "SimpleItemInfo [gsid="+gsid+",count="+count+",]";
		}
	}
	public static final class IODungeonTop{
		public int chapter;
		public int node;
		public IODungeonTop(int pchapter,int pnode){
			this.chapter=pchapter;
			this.node=pnode;
		}
		public IODungeonTop(){}
		@Override
		public String toString() {
			return "IODungeonTop [chapter="+chapter+",node="+node+",]";
		}
	}
	public static final class IOFriendChapter{
		public int status;
		public int chapterid;
		public int power;
		public int exploremin;
		public List<IOFriendEntity> friends;
		public long etime;
		@Override
		public String toString() {
			return "IOFriendChapter [status="+status+",chapterid="+chapterid+",power="+power+",exploremin="+exploremin+",friends="+friends+",etime="+etime+",]";
		}
	}
	public static final class IODungeonGlobalBuf{
		public int hp;
		public int atk;
		public int def;
		public int mdef;
		public IODungeonGlobalBuf(int php,int patk,int pdef,int pmdef){
			this.hp=php;
			this.atk=patk;
			this.def=pdef;
			this.mdef=pmdef;
		}
		public IODungeonGlobalBuf(){}
		@Override
		public String toString() {
			return "IODungeonGlobalBuf [hp="+hp+",atk="+atk+",def="+def+",mdef="+mdef+",]";
		}
	}
	public static final class IOWorldBossSelf{
		public int rank;
		public long damage;
		public IOWorldBossSelf(int prank,long pdamage){
			this.rank=prank;
			this.damage=pdamage;
		}
		public IOWorldBossSelf(){}
		@Override
		public String toString() {
			return "IOWorldBossSelf [rank="+rank+",damage="+damage+",]";
		}
	}
	public static final class IOLegionMember{
		public int id;
		public String name;
		public int icon;
		public int headid;
		public int frameid;
		public int lv;
		public long lastest;
		public int pos;
		public int score;
		public int power;
		public long time;
		public IOLegionMember(int pid,String pname,int picon,int pheadid,int pframeid,int plv,long plastest,int ppos,int pscore,int ppower,long ptime){
			this.id=pid;
			this.name=pname;
			this.icon=picon;
			this.headid=pheadid;
			this.frameid=pframeid;
			this.lv=plv;
			this.lastest=plastest;
			this.pos=ppos;
			this.score=pscore;
			this.power=ppower;
			this.time=ptime;
		}
		public IOLegionMember(){}
		@Override
		public String toString() {
			return "IOLegionMember [id="+id+",name="+name+",icon="+icon+",headid="+headid+",frameid="+frameid+",lv="+lv+",lastest="+lastest+",pos="+pos+",score="+score+",power="+power+",time="+time+",]";
		}
	}
	public static final class IOWorldBossWorldRank{
		public int sid;
		public long legion;
		public String lname;
		public long total;
		public int level;
		public int flag;
		public int index;
		public IOWorldBossWorldRank(int psid,long plegion,String plname,long ptotal,int plevel,int pflag,int pindex){
			this.sid=psid;
			this.legion=plegion;
			this.lname=plname;
			this.total=ptotal;
			this.level=plevel;
			this.flag=pflag;
			this.index=pindex;
		}
		public IOWorldBossWorldRank(){}
		@Override
		public String toString() {
			return "IOWorldBossWorldRank [sid="+sid+",legion="+legion+",lname="+lname+",total="+total+",level="+level+",flag="+flag+",index="+index+",]";
		}
	}
	public static final class IOCzlb{
		public int value;
		public int price;
		public List<RewardInfo> items;
		public int buytime;
		public int limit;
		public List<RewardInfo> special;
		public int exp;
		public String path;
		@Override
		public String toString() {
			return "IOCzlb [value="+value+",price="+price+",items="+items+",buytime="+buytime+",limit="+limit+",special="+special+",exp="+exp+",path="+path+",]";
		}
	}
	public static final class IORewardItem{
		public int GSID;
		public int COUNT;
		public IORewardItem(int pGSID,int pCOUNT){
			this.GSID=pGSID;
			this.COUNT=pCOUNT;
		}
		public IORewardItem(){}
		@Override
		public String toString() {
			return "IORewardItem [GSID="+GSID+",COUNT="+COUNT+",]";
		}
	}
	public static final class IOGuoZhanCity{
		public int player_id;
		public String player_name;
		public int player_size;
		public int nation_id;
		public boolean in_battle;
		public IOGuoZhanCity(int pplayer_id,String pplayer_name,int pplayer_size,int pnation_id,boolean pin_battle){
			this.player_id=pplayer_id;
			this.player_name=pplayer_name;
			this.player_size=pplayer_size;
			this.nation_id=pnation_id;
			this.in_battle=pin_battle;
		}
		public IOGuoZhanCity(){}
		@Override
		public String toString() {
			return "IOGuoZhanCity [player_id="+player_id+",player_name="+player_name+",player_size="+player_size+",nation_id="+nation_id+",in_battle="+in_battle+",]";
		}
	}
	public static final class DynamicActivityInfo{
		public int activeBigID;
		public int priority;
		public String des;
		public int StartTime;
		public int EndTime;
		public int nComplete;
		public List<ActivityInfoOne> sub_activities;
		public List<IoActivityHeroLiBao> hero_libao_config;
		public IoLotteryWheelConfig lottery_wheel_config;
		@Override
		public String toString() {
			return "DynamicActivityInfo [activeBigID="+activeBigID+",priority="+priority+",des="+des+",StartTime="+StartTime+",EndTime="+EndTime+",nComplete="+nComplete+",sub_activities="+sub_activities+",hero_libao_config="+hero_libao_config+",lottery_wheel_config="+lottery_wheel_config+",]";
		}
	}
	public static final class IOCjxg2{
		public int value;
		public int price;
		public List<RewardInfo> items;
		public int buytime;
		public String icon;
		public List<RewardInfo> special;
		public String bg1;
		public String bg2;
		public String hero;
		public String heroname;
		public String txbig;
		public String normal;
		public String check;
		@Override
		public String toString() {
			return "IOCjxg2 [value="+value+",price="+price+",items="+items+",buytime="+buytime+",icon="+icon+",special="+special+",bg1="+bg1+",bg2="+bg2+",hero="+hero+",heroname="+heroname+",txbig="+txbig+",normal="+normal+",check="+check+",]";
		}
	}
	public static final class IOCjxg1{
		public int viple;
		public List<RewardInfo> consume;
		public List<RewardInfo> items;
		public int buytime;
		@Override
		public String toString() {
			return "IOCjxg1 [viple="+viple+",consume="+consume+",items="+items+",buytime="+buytime+",]";
		}
	}
	public static final class IOSpinItem{
		public int GSID;
		public int COUNT;
		public int REPEAT;
		public IOSpinItem(int pGSID,int pCOUNT,int pREPEAT){
			this.GSID=pGSID;
			this.COUNT=pCOUNT;
			this.REPEAT=pREPEAT;
		}
		public IOSpinItem(){}
		@Override
		public String toString() {
			return "IOSpinItem [GSID="+GSID+",COUNT="+COUNT+",REPEAT="+REPEAT+",]";
		}
	}
	public static final class IOServerHasRole{
		public int server_id;
		public int player_level;
		public IOServerHasRole(int pserver_id,int pplayer_level){
			this.server_id=pserver_id;
			this.player_level=pplayer_level;
		}
		public IOServerHasRole(){}
		@Override
		public String toString() {
			return "IOServerHasRole [server_id="+server_id+",player_level="+player_level+",]";
		}
	}
	public static final class IOManorFieldBoss{
		public int state;
		public int lastdamage;
		public int maxhp;
		public int nowhp;
		public IOManorFieldBoss(int pstate,int plastdamage,int pmaxhp,int pnowhp){
			this.state=pstate;
			this.lastdamage=plastdamage;
			this.maxhp=pmaxhp;
			this.nowhp=pnowhp;
		}
		public IOManorFieldBoss(){}
		@Override
		public String toString() {
			return "IOManorFieldBoss [state="+state+",lastdamage="+lastdamage+",maxhp="+maxhp+",nowhp="+nowhp+",]";
		}
	}
	public static final class IOExclusive{
		public int level;
		public List<Integer> skill;
		public int gsid;
		public List<KvStringPair> property;
		@Override
		public String toString() {
			return "IOExclusive [level="+level+",skill="+skill+",gsid="+gsid+",property="+property+",]";
		}
	}
	public static final class IOBattleResult{
		public String ret;
		public int round;
		public Map<Integer,Long> lhp;
		public Map<Integer,Long> rhp;
		public Map<Integer,Integer> lper;
		public Map<Integer,Integer> rper;
		public int ltper;
		public int rtper;
		public int rlosthp;
		public IOBattleReport report;
		public long version;
		public IOBattleRetSide left;
		public IOBattleRetSide right;
		@Override
		public String toString() {
			return "IOBattleResult [ret="+ret+",round="+round+",lhp="+lhp+",rhp="+rhp+",lper="+lper+",rper="+rper+",ltper="+ltper+",rtper="+rtper+",rlosthp="+rlosthp+",report="+report+",version="+version+",left="+left+",right="+right+",]";
		}
	}
	public static final class ServerListItem{
		public int id;
		public String name;
		public String ip_addr;
		public int port;
		public int status;
		public int port_ssl;
		public ServerListItem(int pid,String pname,String pip_addr,int pport,int pstatus,int pport_ssl){
			this.id=pid;
			this.name=pname;
			this.ip_addr=pip_addr;
			this.port=pport;
			this.status=pstatus;
			this.port_ssl=pport_ssl;
		}
		public ServerListItem(){}
		@Override
		public String toString() {
			return "ServerListItem [id="+id+",name="+name+",ip_addr="+ip_addr+",port="+port+",status="+status+",port_ssl="+port_ssl+",]";
		}
	}
	public static final class GuozhanOfficePointPlayer{
		public int office_index;
		public int rid;
		public String rname;
		public int level;
		public int iconid;
		public int frameid;
		public int fight;
		public int hp_perc;
		public GuozhanOfficePointPlayer(int poffice_index,int prid,String prname,int plevel,int piconid,int pframeid,int pfight,int php_perc){
			this.office_index=poffice_index;
			this.rid=prid;
			this.rname=prname;
			this.level=plevel;
			this.iconid=piconid;
			this.frameid=pframeid;
			this.fight=pfight;
			this.hp_perc=php_perc;
		}
		public GuozhanOfficePointPlayer(){}
		@Override
		public String toString() {
			return "GuozhanOfficePointPlayer [office_index="+office_index+",rid="+rid+",rname="+rname+",level="+level+",iconid="+iconid+",frameid="+frameid+",fight="+fight+",hp_perc="+hp_perc+",]";
		}
	}
	public static final class IOLiBao1{
		public int price;
		public int buytime;
		public List<RewardInfo> items;
		@Override
		public String toString() {
			return "IOLiBao1 [price="+price+",buytime="+buytime+",items="+items+",]";
		}
	}
	public static final class IOBattleLine{
		public int power;
		public IOBattleLine(int ppower){
			this.power=ppower;
		}
		public IOBattleLine(){}
		@Override
		public String toString() {
			return "IOBattleLine [power="+power+",]";
		}
	}
	public static final class IOBattleRecordSide{
		public IOBattleRecordInfo info;
		public IOBattleRecordSet set;
		@Override
		public String toString() {
			return "IOBattleRecordSide [info="+info+",set="+set+",]";
		}
	}
	public static final class IOSecretHero{
		public int hero_type;
		public int hero_id;
		public int hp_percent;
		public IOSecretHero(int phero_type,int phero_id,int php_percent){
			this.hero_type=phero_type;
			this.hero_id=phero_id;
			this.hp_percent=php_percent;
		}
		public IOSecretHero(){}
		@Override
		public String toString() {
			return "IOSecretHero [hero_type="+hero_type+",hero_id="+hero_id+",hp_percent="+hp_percent+",]";
		}
	}
	public static final class IODungeonBuffList{
		public float ppthr;
		public float pskidam;
		public float atk;
		public float pcrid;
		public float pmthr;
		public float pcri;
		public IODungeonBuffList(float pppthr,float ppskidam,float patk,float ppcrid,float ppmthr,float ppcri){
			this.ppthr=pppthr;
			this.pskidam=ppskidam;
			this.atk=patk;
			this.pcrid=ppcrid;
			this.pmthr=ppmthr;
			this.pcri=ppcri;
		}
		public IODungeonBuffList(){}
		@Override
		public String toString() {
			return "IODungeonBuffList [ppthr="+ppthr+",pskidam="+pskidam+",atk="+atk+",pcrid="+pcrid+",pmthr="+pmthr+",pcri="+pcri+",]";
		}
	}
	public static final class IOBattleFormation{
		public String f_type;
		public int mythic;
		public List<IOFormationGeneralPos> items;
		@Override
		public String toString() {
			return "IOBattleFormation [f_type="+f_type+",mythic="+mythic+",items="+items+",]";
		}
	}
	public static final class IOCxryGenerals{
		public int gsid;
		public int isget;
		public IOCxryGenerals(int pgsid,int pisget){
			this.gsid=pgsid;
			this.isget=pisget;
		}
		public IOCxryGenerals(){}
		@Override
		public String toString() {
			return "IOCxryGenerals [gsid="+gsid+",isget="+isget+",]";
		}
	}
	public static final class IOXsdh1{
		public int grid;
		public List<RewardInfo> grch;
		public List<RewardInfo> consume;
		public int buytime;
		@Override
		public String toString() {
			return "IOXsdh1 [grid="+grid+",grch="+grch+",consume="+consume+",buytime="+buytime+",]";
		}
	}
	public static final class IOMjbgSource{
		public String intro;
		public String page;
		public IOMjbgSource(String pintro,String ppage){
			this.intro=pintro;
			this.page=ppage;
		}
		public IOMjbgSource(){}
		@Override
		public String toString() {
			return "IOMjbgSource [intro="+intro+",page="+page+",]";
		}
	}
	public static final class GuideStepInfo{
		public int module;
		public int step;
		public GuideStepInfo(int pmodule,int pstep){
			this.module=pmodule;
			this.step=pstep;
		}
		public GuideStepInfo(){}
		@Override
		public String toString() {
			return "GuideStepInfo [module="+module+",step="+step+",]";
		}
	}
	public static final class IODungeonNodePos{
		public int type;
		public int choose;
		public long finish;
		public IODungeonNodePos(int ptype,int pchoose,long pfinish){
			this.type=ptype;
			this.choose=pchoose;
			this.finish=pfinish;
		}
		public IODungeonNodePos(){}
		@Override
		public String toString() {
			return "IODungeonNodePos [type="+type+",choose="+choose+",finish="+finish+",]";
		}
	}
	public static final class IOMythicalAnimal{
		public int tid;
		public int pclass;
		public int level;
		public List<Integer> pskill;
		@Override
		public String toString() {
			return "IOMythicalAnimal [tid="+tid+",pclass="+pclass+",level="+level+",pskill="+pskill+",]";
		}
	}
	public static final class IOFriendEntity{
		public int id;
		public String rname;
		public int iconid;
		public int headid;
		public int frameid;
		public int level;
		public int vipLevel;
		public int power;
		public long lasttime;
		public IOpstatus pstatus;
		@Override
		public String toString() {
			return "IOFriendEntity [id="+id+",rname="+rname+",iconid="+iconid+",headid="+headid+",frameid="+frameid+",level="+level+",vipLevel="+vipLevel+",power="+power+",lasttime="+lasttime+",pstatus="+pstatus+",]";
		}
	}
	public static final class IOOcctask{
		public int index;
		public int occtype;
		public List<RewardInfo> rewards;
		public List<IOOccTask1> list;
		public List<RewardInfo> reward;
		public RewardInfo refcost;
		public IOOcctaskPackinfo packinfo;
		public List<Integer> prewards;
		@Override
		public String toString() {
			return "IOOcctask [index="+index+",occtype="+occtype+",rewards="+rewards+",list="+list+",reward="+reward+",refcost="+refcost+",packinfo="+packinfo+",prewards="+prewards+",]";
		}
	}
	public static final class IODjjfMission{
		public int NUM;
		public int cur;
		public String NAME;
		public List<RewardInfo> ITEMS;
		public int status;
		@Override
		public String toString() {
			return "IODjjfMission [NUM="+NUM+",cur="+cur+",NAME="+NAME+",ITEMS="+ITEMS+",status="+status+",]";
		}
	}
	public static final class IOManorBoss{
		public int bossid;
		public int maxhp;
		public int lastdamage;
		public int nowhp;
		public Map<Integer,IOGeneralSimple> bset;
		@Override
		public String toString() {
			return "IOManorBoss [bossid="+bossid+",maxhp="+maxhp+",lastdamage="+lastdamage+",nowhp="+nowhp+",bset="+bset+",]";
		}
	}
	public static final class IOLevelGift{
		public int level;
		public int price;
		public long end;
		public int buytime;
		public List<RewardInfo> items;
		@Override
		public String toString() {
			return "IOLevelGift [level="+level+",price="+price+",end="+end+",buytime="+buytime+",items="+items+",]";
		}
	}
	public static final class IOJfbxEvent{
		public int MARK;
		public int LIMIT;
		public String intro;
		public IOJfbxEvent(int pMARK,int pLIMIT,String pintro){
			this.MARK=pMARK;
			this.LIMIT=pLIMIT;
			this.intro=pintro;
		}
		public IOJfbxEvent(){}
		@Override
		public String toString() {
			return "IOJfbxEvent [MARK="+MARK+",LIMIT="+LIMIT+",intro="+intro+",]";
		}
	}
	public static final class IOManorFriend{
		public int icon;
		public int frameid;
		public int headid;
		public String name;
		public int id;
		public int level;
		public int power;
		public IOManorFriend(int picon,int pframeid,int pheadid,String pname,int pid,int plevel,int ppower){
			this.icon=picon;
			this.frameid=pframeid;
			this.headid=pheadid;
			this.name=pname;
			this.id=pid;
			this.level=plevel;
			this.power=ppower;
		}
		public IOManorFriend(){}
		@Override
		public String toString() {
			return "IOManorFriend [icon="+icon+",frameid="+frameid+",headid="+headid+",name="+name+",id="+id+",level="+level+",power="+power+",]";
		}
	}
	public static final class IOLegionLog{
		public List<String> params;
		public String event;
		public long create;
		@Override
		public String toString() {
			return "IOLegionLog [params="+params+",event="+event+",create="+create+",]";
		}
	}
	public static final class IoLotteryWheelConfig{
		public int my_free_count;
		public int my_pay_count;
		public int daily_free_count;
		public int pay_count;
		public int pay_price;
		public List<ActivitiesItem> reward;
		@Override
		public String toString() {
			return "IoLotteryWheelConfig [my_free_count="+my_free_count+",my_pay_count="+my_pay_count+",daily_free_count="+daily_free_count+",pay_count="+pay_count+",pay_price="+pay_price+",reward="+reward+",]";
		}
	}
	public static final class IOGeneralBean{
		public long guid;
		public int gsid;
		public int level;
		public int star;
		public int camp;
		public int occu;
		public int pclass;
		public int power;
		public List<Integer> talent;
		public int affairs;
		public int treasure;
		public IOProperty property;
		public List<Integer> equip;
		public List<Integer> skill;
		public IOExclusive exclusive;
		public int hppercent;
		@Override
		public String toString() {
			return "IOGeneralBean [guid="+guid+",gsid="+gsid+",level="+level+",star="+star+",camp="+camp+",occu="+occu+",pclass="+pclass+",power="+power+",talent="+talent+",affairs="+affairs+",treasure="+treasure+",property="+property+",equip="+equip+",skill="+skill+",exclusive="+exclusive+",hppercent="+hppercent+",]";
		}
	}
	public static final class IODungeonBset{
		public int gsid;
		public int level;
		public int pclass;
		public float exhp;
		public float exatk;
		public IODungeonBset(int pgsid,int plevel,int ppclass,float pexhp,float pexatk){
			this.gsid=pgsid;
			this.level=plevel;
			this.pclass=ppclass;
			this.exhp=pexhp;
			this.exatk=pexatk;
		}
		public IODungeonBset(){}
		@Override
		public String toString() {
			return "IODungeonBset [gsid="+gsid+",level="+level+",pclass="+pclass+",exhp="+exhp+",exatk="+exatk+",]";
		}
	}
	public static final class FixedActivityInfo{
		public int id;
		public int level_index;
		public int progress;
		public boolean can_get_award;
		public FixedActivityInfo(int pid,int plevel_index,int pprogress,boolean pcan_get_award){
			this.id=pid;
			this.level_index=plevel_index;
			this.progress=pprogress;
			this.can_get_award=pcan_get_award;
		}
		public FixedActivityInfo(){}
		@Override
		public String toString() {
			return "FixedActivityInfo [id="+id+",level_index="+level_index+",progress="+progress+",can_get_award="+can_get_award+",]";
		}
	}
	public static final class IODungeonList{
		public int isget;
		public IODungeonList(int pisget){
			this.isget=pisget;
		}
		public IODungeonList(){}
		@Override
		public String toString() {
			return "IODungeonList [isget="+isget+",]";
		}
	}
	public static final class IOOccTask1{
		public int status;
		public List<RewardInfo> rewards;
		public String intro;
		public int mark;
		public int limit;
		public String page;
		public int num;
		@Override
		public String toString() {
			return "IOOccTask1 [status="+status+",rewards="+rewards+",intro="+intro+",mark="+mark+",limit="+limit+",page="+page+",num="+num+",]";
		}
	}
	public static final class IOStarGift{
		public int gstar;
		public int price;
		public long end;
		public int buytime;
		public List<RewardInfo> items;
		@Override
		public String toString() {
			return "IOStarGift [gstar="+gstar+",price="+price+",end="+end+",buytime="+buytime+",items="+items+",]";
		}
	}
	public static final class ActivitiesItem{
		public int nID;
		public int nNum;
		public ActivitiesItem(int pnID,int pnNum){
			this.nID=pnID;
			this.nNum=pnNum;
		}
		public ActivitiesItem(){}
		@Override
		public String toString() {
			return "ActivitiesItem [nID="+nID+",nNum="+nNum+",]";
		}
	}
	public static final class IOBattleRecordInfo{
		public String rname;
		public int level;
		public int iconid;
		public int headid;
		public int frameid;
		public IOBattleRecordInfo(String prname,int plevel,int piconid,int pheadid,int pframeid){
			this.rname=prname;
			this.level=plevel;
			this.iconid=piconid;
			this.headid=pheadid;
			this.frameid=pframeid;
		}
		public IOBattleRecordInfo(){}
		@Override
		public String toString() {
			return "IOBattleRecordInfo [rname="+rname+",level="+level+",iconid="+iconid+",headid="+headid+",frameid="+frameid+",]";
		}
	}
	public static final class WanbaLoginGift{
		public int wanba_gift_id;
		public String appid;
		public String openid;
		public String openkey;
		public String pf;
		public WanbaLoginGift(int pwanba_gift_id,String pappid,String popenid,String popenkey,String ppf){
			this.wanba_gift_id=pwanba_gift_id;
			this.appid=pappid;
			this.openid=popenid;
			this.openkey=popenkey;
			this.pf=ppf;
		}
		public WanbaLoginGift(){}
		@Override
		public String toString() {
			return "WanbaLoginGift [wanba_gift_id="+wanba_gift_id+",appid="+appid+",openid="+openid+",openkey="+openkey+",pf="+pf+",]";
		}
	}
	public static final class IOLegionApplyReview{
		public String error;
		public IOLegionApplyReview(String perror){
			this.error=perror;
		}
		public IOLegionApplyReview(){}
		@Override
		public String toString() {
			return "IOLegionApplyReview [error="+error+",]";
		}
	}
	public static final class IOCxryZf{
		public int cur;
		public int max;
		public int prob;
		public IOCxryZf(int pcur,int pmax,int pprob){
			this.cur=pcur;
			this.max=pmax;
			this.prob=pprob;
		}
		public IOCxryZf(){}
		@Override
		public String toString() {
			return "IOCxryZf [cur="+cur+",max="+max+",prob="+prob+",]";
		}
	}
	public static final class IOJfbxBox{
		public int SCORE;
		public int state;
		public List<RewardInfo> REWARD;
		@Override
		public String toString() {
			return "IOJfbxBox [SCORE="+SCORE+",state="+state+",REWARD="+REWARD+",]";
		}
	}
	public static final class IOSeason{
		public long etime;
		public int year;
		public int season;
		public List<Integer> pos;
		@Override
		public String toString() {
			return "IOSeason [etime="+etime+",year="+year+",season="+season+",pos="+pos+",]";
		}
	}
	public static final class IOFormationGeneralPos{
		public int pos;
		public long general_uuid;
		public IOFormationGeneralPos(int ppos,long pgeneral_uuid){
			this.pos=ppos;
			this.general_uuid=pgeneral_uuid;
		}
		public IOFormationGeneralPos(){}
		@Override
		public String toString() {
			return "IOFormationGeneralPos [pos="+pos+",general_uuid="+general_uuid+",]";
		}
	}
	public static final class IOBHurt{
		public int gsid;
		public long hurm;
		public long heal;
		public long hp;
		public int born;
		public int hpperc;
		public long hpmax;
		public IOBHurt(int pgsid,long phurm,long pheal,long php,int pborn,int phpperc,long phpmax){
			this.gsid=pgsid;
			this.hurm=phurm;
			this.heal=pheal;
			this.hp=php;
			this.born=pborn;
			this.hpperc=phpperc;
			this.hpmax=phpmax;
		}
		public IOBHurt(){}
		@Override
		public String toString() {
			return "IOBHurt [gsid="+gsid+",hurm="+hurm+",heal="+heal+",hp="+hp+",born="+born+",hpperc="+hpperc+",hpmax="+hpmax+",]";
		}
	}
	public static final class IoActivityHeroLiBao{
		public int buy_count;
		public int buy_count_total;
		public int favor_rate;
		public int price;
		public int recharge_id;
		public int extra_diamond;
		public List<ActivitiesItem> reward;
		@Override
		public String toString() {
			return "IoActivityHeroLiBao [buy_count="+buy_count+",buy_count_total="+buy_count_total+",favor_rate="+favor_rate+",price="+price+",recharge_id="+recharge_id+",extra_diamond="+extra_diamond+",reward="+reward+",]";
		}
	}
	public static final class IOWorldBossInfo{
		public int id;
		public long endtime;
		public long lastdamage;
		public Map<Integer,IOGeneralLegion> bset;
		public int hasgift;
		@Override
		public String toString() {
			return "IOWorldBossInfo [id="+id+",endtime="+endtime+",lastdamage="+lastdamage+",bset="+bset+",hasgift="+hasgift+",]";
		}
	}
	public static final class QiZhenYiBaoPlayer{
		public int player_id;
		public String player_name;
		public int count;
		public QiZhenYiBaoPlayer(int pplayer_id,String pplayer_name,int pcount){
			this.player_id=pplayer_id;
			this.player_name=pplayer_name;
			this.count=pcount;
		}
		public QiZhenYiBaoPlayer(){}
		@Override
		public String toString() {
			return "QiZhenYiBaoPlayer [player_id="+player_id+",player_name="+player_name+",count="+count+",]";
		}
	}
	public static final class IOLegionInfo{
		public long id;
		public int lv;
		public String name;
		public int npnum;
		public int mpnum;
		public int minlv;
		public boolean ispass;
		public String ceo;
		public IOLegionInfo(long pid,int plv,String pname,int pnpnum,int pmpnum,int pminlv,boolean pispass,String pceo){
			this.id=pid;
			this.lv=plv;
			this.name=pname;
			this.npnum=pnpnum;
			this.mpnum=pmpnum;
			this.minlv=pminlv;
			this.ispass=pispass;
			this.ceo=pceo;
		}
		public IOLegionInfo(){}
		@Override
		public String toString() {
			return "IOLegionInfo [id="+id+",lv="+lv+",name="+name+",npnum="+npnum+",mpnum="+mpnum+",minlv="+minlv+",ispass="+ispass+",ceo="+ceo+",]";
		}
	}
	public static final class IOMineHistory{
		public int target_player_id;
		public String target_player_name;
		public boolean is_positive;
		public int is_success;
		public int mine_point;
		public int type;
		public int add_time;
		public List<Integer> gain;
		@Override
		public String toString() {
			return "IOMineHistory [target_player_id="+target_player_id+",target_player_name="+target_player_name+",is_positive="+is_positive+",is_success="+is_success+",mine_point="+mine_point+",type="+type+",add_time="+add_time+",gain="+gain+",]";
		}
	}
	public static final class IOBattleRecordSet{
		public IOMythicalAnimal mythic;
		public Map<Integer,IOGeneralBean> team;
		@Override
		public String toString() {
			return "IOBattleRecordSet [mythic="+mythic+",team="+team+",]";
		}
	}
	public static final class IOMintPoint{
		public IOMineHolder hold_player;
		@Override
		public String toString() {
			return "IOMintPoint [hold_player="+hold_player+",]";
		}
	}
	public static final class IODjrw1{
		public int knark;
		public List<IODjrwChk> chk;
		public int cnum;
		public String name;
		public List<RewardInfo> items;
		public int status;
		@Override
		public String toString() {
			return "IODjrw1 [knark="+knark+",chk="+chk+",cnum="+cnum+",name="+name+",items="+items+",status="+status+",]";
		}
	}
	public static final class IOMjbgFinal{
		public List<IOMjbgFinal1> list;
		@Override
		public String toString() {
			return "IOMjbgFinal [list="+list+",]";
		}
	}
	public static final class IOOtherPlayer{
		public int rid;
		public int rno;
		public String rname;
		public int sex;
		public int power;
		public int iconid;
		public int headid;
		public int frameid;
		public int imageid;
		public int level;
		public int vip;
		public int office_index;
		public List<IOGeneralBean> bestgeneral;
		public IOBattleSet battleset;
		public int points;
		public List<IOBattleLine> battlelines;
		public List<IOBattleSet> kpBattleset;
		@Override
		public String toString() {
			return "IOOtherPlayer [rid="+rid+",rno="+rno+",rname="+rname+",sex="+sex+",power="+power+",iconid="+iconid+",headid="+headid+",frameid="+frameid+",imageid="+imageid+",level="+level+",vip="+vip+",office_index="+office_index+",bestgeneral="+bestgeneral+",battleset="+battleset+",points="+points+",battlelines="+battlelines+",kpBattleset="+kpBattleset+",]";
		}
	}
	public static final class IOWorldBossRank{
		public int headid;
		public int frameid;
		public int icon;
		public int level;
		public int power;
		public long damge;
		public String name;
		public int rid;
		public IOWorldBossRank(int pheadid,int pframeid,int picon,int plevel,int ppower,long pdamge,String pname,int prid){
			this.headid=pheadid;
			this.frameid=pframeid;
			this.icon=picon;
			this.level=plevel;
			this.power=ppower;
			this.damge=pdamge;
			this.name=pname;
			this.rid=prid;
		}
		public IOWorldBossRank(){}
		@Override
		public String toString() {
			return "IOWorldBossRank [headid="+headid+",frameid="+frameid+",icon="+icon+",level="+level+",power="+power+",damge="+damge+",name="+name+",rid="+rid+",]";
		}
	}
	public static final class IOLegionRank{
		public String name;
		public long damge;
		public int power;
		public IOLegionRank(String pname,long pdamge,int ppower){
			this.name=pname;
			this.damge=pdamge;
			this.power=ppower;
		}
		public IOLegionRank(){}
		@Override
		public String toString() {
			return "IOLegionRank [name="+name+",damge="+damge+",power="+power+",]";
		}
	}
	public static final class IOMailAttach{
		public int gsid;
		public int count;
		public IOMailAttach(int pgsid,int pcount){
			this.gsid=pgsid;
			this.count=pcount;
		}
		public IOMailAttach(){}
		@Override
		public String toString() {
			return "IOMailAttach [gsid="+gsid+",count="+count+",]";
		}
	}
	public static final class IOManorBuilding{
		public int lv;
		public long rid;
		public int id;
		public int type;
		public int pos;
		public long lastgain;
		public List<IORewardItem> items;
		@Override
		public String toString() {
			return "IOManorBuilding [lv="+lv+",rid="+rid+",id="+id+",type="+type+",pos="+pos+",lastgain="+lastgain+",items="+items+",]";
		}
	}
	public static final class IOBattlesetEnemy{
		public IOMythicalAnimal mythic;
		public Map<Integer,IOGeneralBean> team;
		@Override
		public String toString() {
			return "IOBattlesetEnemy [mythic="+mythic+",team="+team+",]";
		}
	}
	public static final class IOLegionDonation{
		public int count;
		public long last;
		public IOLegionDonation(int pcount,long plast){
			this.count=pcount;
			this.last=plast;
		}
		public IOLegionDonation(){}
		@Override
		public String toString() {
			return "IOLegionDonation [count="+count+",last="+last+",]";
		}
	}
	public static final class IOSzhc{
		public List<RewardInfo> consume;
		public List<RewardInfo> demand;
		public int buytime;
		@Override
		public String toString() {
			return "IOSzhc [consume="+consume+",demand="+demand+",buytime="+buytime+",]";
		}
	}
	public static final class IOMailInfo{
		public int id;
		public IOMailFrom from;
		public int type;
		public int state;
		public String title;
		public String content;
		public List<IOMailAttach> reward;
		public long stime;
		public long etime;
		@Override
		public String toString() {
			return "IOMailInfo [id="+id+",from="+from+",type="+type+",state="+state+",title="+title+",content="+content+",reward="+reward+",stime="+stime+",etime="+etime+",]";
		}
	}
	public static final class RewardInfo{
		public int GSID;
		public int COUNT;
		public RewardInfo(int pGSID,int pCOUNT){
			this.GSID=pGSID;
			this.COUNT=pCOUNT;
		}
		public RewardInfo(){}
		@Override
		public String toString() {
			return "RewardInfo [GSID="+GSID+",COUNT="+COUNT+",]";
		}
	}
	public static final class IODungeonShop{
		public int chapter;
		public int node;
		public int disc;
		public List<IORewardItem> item;
		public List<IORewardItem> consume;
		public int quality;
		@Override
		public String toString() {
			return "IODungeonShop [chapter="+chapter+",node="+node+",disc="+disc+",item="+item+",consume="+consume+",quality="+quality+",]";
		}
	}
	public static final class IOShopItem{
		public int GSID;
		public int COUNT;
		public int BUYTIME;
		public int COIN;
		public int PRICE;
		public int DISCOUNT;
		public IOShopItem(int pGSID,int pCOUNT,int pBUYTIME,int pCOIN,int pPRICE,int pDISCOUNT){
			this.GSID=pGSID;
			this.COUNT=pCOUNT;
			this.BUYTIME=pBUYTIME;
			this.COIN=pCOIN;
			this.PRICE=pPRICE;
			this.DISCOUNT=pDISCOUNT;
		}
		public IOShopItem(){}
		@Override
		public String toString() {
			return "IOShopItem [GSID="+GSID+",COUNT="+COUNT+",BUYTIME="+BUYTIME+",COIN="+COIN+",PRICE="+PRICE+",DISCOUNT="+DISCOUNT+",]";
		}
	}
	public static final class IOBattleRetSide{
		public IOBattleRecordInfo info;
		public IOBattleRetSet set;
		@Override
		public String toString() {
			return "IOBattleRetSide [info="+info+",set="+set+",]";
		}
	}
	public static final class SecretItemInfo{
		public int itemId;
		public int cnt;
		public SecretItemInfo(int pitemId,int pcnt){
			this.itemId=pitemId;
			this.cnt=pcnt;
		}
		public SecretItemInfo(){}
		@Override
		public String toString() {
			return "SecretItemInfo [itemId="+itemId+",cnt="+cnt+",]";
		}
	}
	public static final class ActivityInfoOne{
		public int nSubCount;
		public int nNeedLevel;
		public int nNeedVipLevel;
		public int nNeedPassNoChapID;
		public int nState;
		public int nComplete;
		public String szSubDes;
		public List<ActivitiesItem> reward;
		@Override
		public String toString() {
			return "ActivityInfoOne [nSubCount="+nSubCount+",nNeedLevel="+nNeedLevel+",nNeedVipLevel="+nNeedVipLevel+",nNeedPassNoChapID="+nNeedPassNoChapID+",nState="+nState+",nComplete="+nComplete+",szSubDes="+szSubDes+",reward="+reward+",]";
		}
	}
	public static final class IOProperty{
		public int hp;
		public int atk;
		public int def;
		public int mdef;
		public float atktime;
		public int range;
		public int msp;
		public int pasp;
		public int pcri;
		public int pcrid;
		public int pdam;
		public int php;
		public int patk;
		public int pdef;
		public int pmdef;
		public int ppbs;
		public int pmbs;
		public int pefc;
		public int ppthr;
		public int patkdam;
		public int pskidam;
		public int pckatk;
		public int pmthr;
		public int pdex;
		public int pmdex;
		public int pmsatk;
		public int pmps;
		public int pcd;
		public IOProperty(int php,int patk,int pdef,int pmdef,float patktime,int prange,int pmsp,int ppasp,int ppcri,int ppcrid,int ppdam,int pphp,int ppatk,int ppdef,int ppmdef,int pppbs,int ppmbs,int ppefc,int pppthr,int ppatkdam,int ppskidam,int ppckatk,int ppmthr,int ppdex,int ppmdex,int ppmsatk,int ppmps,int ppcd){
			this.hp=php;
			this.atk=patk;
			this.def=pdef;
			this.mdef=pmdef;
			this.atktime=patktime;
			this.range=prange;
			this.msp=pmsp;
			this.pasp=ppasp;
			this.pcri=ppcri;
			this.pcrid=ppcrid;
			this.pdam=ppdam;
			this.php=pphp;
			this.patk=ppatk;
			this.pdef=ppdef;
			this.pmdef=ppmdef;
			this.ppbs=pppbs;
			this.pmbs=ppmbs;
			this.pefc=ppefc;
			this.ppthr=pppthr;
			this.patkdam=ppatkdam;
			this.pskidam=ppskidam;
			this.pckatk=ppckatk;
			this.pmthr=ppmthr;
			this.pdex=ppdex;
			this.pmdex=ppmdex;
			this.pmsatk=ppmsatk;
			this.pmps=ppmps;
			this.pcd=ppcd;
		}
		public IOProperty(){}
		@Override
		public String toString() {
			return "IOProperty [hp="+hp+",atk="+atk+",def="+def+",mdef="+mdef+",atktime="+atktime+",range="+range+",msp="+msp+",pasp="+pasp+",pcri="+pcri+",pcrid="+pcrid+",pdam="+pdam+",php="+php+",patk="+patk+",pdef="+pdef+",pmdef="+pmdef+",ppbs="+ppbs+",pmbs="+pmbs+",pefc="+pefc+",ppthr="+ppthr+",patkdam="+patkdam+",pskidam="+pskidam+",pckatk="+pckatk+",pmthr="+pmthr+",pdex="+pdex+",pmdex="+pmdex+",pmsatk="+pmsatk+",pmps="+pmps+",pcd="+pcd+",]";
		}
	}
	public static final class IOGeneralLegion{
		public int gsid;
		public int level;
		public int hpcover;
		public int pclass;
		public long exhp;
		public int exatk;
		public IOGeneralLegion(int pgsid,int plevel,int phpcover,int ppclass,long pexhp,int pexatk){
			this.gsid=pgsid;
			this.level=plevel;
			this.hpcover=phpcover;
			this.pclass=ppclass;
			this.exhp=pexhp;
			this.exatk=pexatk;
		}
		public IOGeneralLegion(){}
		@Override
		public String toString() {
			return "IOGeneralLegion [gsid="+gsid+",level="+level+",hpcover="+hpcover+",pclass="+pclass+",exhp="+exhp+",exatk="+exatk+",]";
		}
	}
	public static final class IOAffairItem{
		public int id;
		public int gnum;
		public int gstar;
		public int hour;
		public List<Integer> cond;
		public List<IORewardItem> reward;
		public int lock;
		public long create;
		public long etime;
		public List<Long> arr;
		@Override
		public String toString() {
			return "IOAffairItem [id="+id+",gnum="+gnum+",gstar="+gstar+",hour="+hour+",cond="+cond+",reward="+reward+",lock="+lock+",create="+create+",etime="+etime+",arr="+arr+",]";
		}
	}
	public static final class IOBattleRetSet{
		public int index;
		public Map<Integer,IOGeneralBean> team;
		@Override
		public String toString() {
			return "IOBattleRetSet [index="+index+",team="+team+",]";
		}
	}
}
