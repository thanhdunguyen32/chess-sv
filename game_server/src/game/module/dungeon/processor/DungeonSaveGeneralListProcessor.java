package game.module.dungeon.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.award.logic.AwardUtils;
import game.module.dungeon.bean.DungeonBean;
import game.module.dungeon.dao.DungeonCache;
import game.module.dungeon.dao.DungeonDaoHelper;
import game.module.dungeon.dao.DungeonTemplateCache;
import game.module.dungeon.logic.DungeonManager;
import game.module.hero.bean.GeneralBean;
import game.module.hero.dao.GeneralCache;
import game.module.hero.logic.GeneralManager;
import game.module.log.constants.LogConstants;
import game.module.template.MyDungeonTemplate;
import game.module.template.RewardTemplateSimple;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;
import ws.WsMessageDungeon;
import ws.WsMessageHall;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@MsgCodeAnn(msgcode = WsMessageDungeon.C2SDungeonSaveGeneralList.id, accessLimit = 200)
public class DungeonSaveGeneralListProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(DungeonSaveGeneralListProcessor.class);

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
        WsMessageDungeon.C2SDungeonSaveGeneralList reqmsg = WsMessageDungeon.C2SDungeonSaveGeneralList.parse(request);
        logger.info("dungeon save general list,player={},req={}", playerId, reqmsg);
        long[] guids = reqmsg.guids;
        if (guids == null) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageDungeon.S2CDungeonSaveGeneralList.msgCode, 1443);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //general not exist
        Map<Long, GeneralBean> generalAll = GeneralCache.getInstance().getHeros(playerId);
        for (long guid : guids) {
            if (!generalAll.containsKey(guid)) {
                WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageDungeon.S2CDungeonSaveGeneralList.msgCode, 1444);
                playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
                return;
            }
        }
        //formation exist
        DungeonBean dungeonBean = DungeonCache.getInstance().getDungeon(playerId);
        if (dungeonBean != null && dungeonBean.getOnlineGenerals() != null && dungeonBean.getOnlineGenerals().size() >0) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageDungeon.S2CDungeonSaveGeneralList.msgCode, 1445);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //do
        if (dungeonBean == null) {
            dungeonBean = DungeonManager.getInstance().createDungeon(playerId);
            DungeonCache.getInstance().addDungeon(dungeonBean);
        }
        Map<Long, Integer> onlineGenerals = new HashMap<>();
        for (long guid : guids) {
            onlineGenerals.put(guid, 10000);
        }
        dungeonBean.setOnlineGenerals(onlineGenerals);
        //ret
        WsMessageDungeon.S2CDungeonSaveGeneralList respmsg = new WsMessageDungeon.S2CDungeonSaveGeneralList();
        //battle set
        respmsg.generals = new ArrayList<>();
        for (Map.Entry<Long, Integer> aEntry : dungeonBean.getOnlineGenerals().entrySet()) {
            long guid = aEntry.getKey();
            int hppercent = aEntry.getValue();
            GeneralBean generalBean = generalAll.get(guid);
            WsMessageBase.IOGeneralBean ioGeneralBean = GeneralManager.getInstance().buildIoGeneral(generalBean);
            ioGeneralBean.hppercent = hppercent;
            respmsg.generals.add(ioGeneralBean);
            //TODO golbal buff
        }
        //reward
        Integer chapterIndex = dungeonBean.getChapterIndex();
        Map<Integer, Integer> rewardMap = new HashMap<>();
        for (int i = 0; i < chapterIndex; i++) {
            MyDungeonTemplate.MyDungeonTemplateStep myDungeonTemplate = DungeonTemplateCache.getInstance().getDungeonTemplateStep(i);
            List<RewardTemplateSimple> reward = myDungeonTemplate.getReward();
            for (RewardTemplateSimple rewardTemplateSimple : reward) {
                Integer gsid = rewardTemplateSimple.getGSID();
                Integer count = rewardTemplateSimple.getCOUNT();
                if (rewardMap.containsKey(gsid)) {
                    rewardMap.put(gsid, rewardMap.get(gsid) + count);
                } else {
                    rewardMap.put(gsid, count);
                }
            }
        }
        respmsg.reward = new ArrayList<>(rewardMap.size());
        for (Map.Entry<Integer, Integer> aEntry : rewardMap.entrySet()) {
            respmsg.reward.add(new WsMessageBase.IORewardItem(aEntry.getKey(), aEntry.getValue()));
            AwardUtils.changeRes(playingRole, aEntry.getKey(), aEntry.getValue(), LogConstants.MODULE_DUNGEON);
        }
        //set award get
        for (int i = 0; i < chapterIndex; i++) {
            dungeonBean.getChapterAwardGet().set(i, 1);
        }
        //save bean
        if (dungeonBean.getId() == null) {
            DungeonDaoHelper.asyncInsertDungeon(dungeonBean);
        } else {
            DungeonDaoHelper.asyncUpdateDungeon(dungeonBean);
        }
        //potion
        if (dungeonBean.getPotions() != null) {
            respmsg.potion = new ArrayList<>();
            for (Map.Entry<Integer, Integer> aEntry : dungeonBean.getPotions().entrySet()) {
                respmsg.potion.add(new WsMessageBase.IODungeonPotion(aEntry.getKey(), aEntry.getValue()));
            }
        }
        //ret
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
