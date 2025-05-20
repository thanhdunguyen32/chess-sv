package game.module.dungeon.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.dungeon.bean.DungeonBean;
import game.module.dungeon.dao.DungeonCache;
import game.module.dungeon.logic.DungeonManager;
import game.module.hero.bean.GeneralBean;
import game.module.hero.dao.GeneralCache;
import game.module.hero.logic.GeneralManager;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;
import ws.WsMessageDungeon;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@MsgCodeAnn(msgcode = WsMessageDungeon.C2SDungeonChapterList.id, accessLimit = 200)
public class DungeonChapterListProcessor extends PlayingRoleMsgProcessor {

    private static final Logger logger = LoggerFactory.getLogger(DungeonChapterListProcessor.class);

    @Override
    public void processByte(PlayingRole playingRole, RequestByteMessage request) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void processProto(PlayingRole playingRole, RequestProtoMessage request) throws Exception {

    }

    @Override
    public void processMy(PlayingRole playingRole, MyRequestMessage request) throws Exception {
        int playerId = playingRole.getId();
        logger.info("dungeon chapter list,player={}", playerId);
        DungeonBean dungeonBean = DungeonCache.getInstance().getDungeon(playerId);
        if (dungeonBean != null) {
            DungeonManager.getInstance().resetCheck(dungeonBean);
        }
        //ret
        WsMessageDungeon.S2CDungeonChapterList respmsg = new WsMessageDungeon.S2CDungeonChapterList();
        if (dungeonBean == null || dungeonBean.getOnlineGenerals() == null || dungeonBean.getOnlineGenerals().size() == 0) {
            respmsg.formation_exist = false;
        } else {
            respmsg.formation_exist = true;
            //award get
            List<Integer> chapterAwardGet = dungeonBean.getChapterAwardGet();
            if (chapterAwardGet != null) {
                respmsg.dungeonlist = new ArrayList<>();
                for (int awardGet : chapterAwardGet) {
                    respmsg.dungeonlist.add(new WsMessageBase.IODungeonList(awardGet));
                }
            }
            //battle set
            respmsg.dungeonset = new ArrayList<>();
            Map<Long, GeneralBean> generalAll = GeneralCache.getInstance().getHeros(playerId);
            for (Map.Entry<Long, Integer> aEntry : dungeonBean.getOnlineGenerals().entrySet()) {
                long guid = aEntry.getKey();
                int hppercent = aEntry.getValue();
                GeneralBean generalBean = generalAll.get(guid);
                if (generalBean == null) {
                    continue;
                }
                WsMessageBase.IOGeneralBean ioGeneralBean = GeneralManager.getInstance().buildIoGeneral(generalBean);
                ioGeneralBean.hppercent = hppercent;
                respmsg.dungeonset.add(ioGeneralBean);
                //TODO golbal buff
            }
        }
        //ret
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
