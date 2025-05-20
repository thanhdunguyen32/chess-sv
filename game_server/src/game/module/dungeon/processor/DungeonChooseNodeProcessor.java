package game.module.dungeon.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.award.logic.AwardUtils;
import game.module.dungeon.bean.DungeonBean;
import game.module.dungeon.bean.DungeonNode;
import game.module.dungeon.dao.DungeonCache;
import game.module.dungeon.dao.DungeonDaoHelper;
import game.module.dungeon.dao.DungeonTemplateCache;
import game.module.dungeon.logic.DungeonConstants;
import game.module.dungeon.logic.DungeonManager;
import game.module.log.constants.LogConstants;
import game.module.template.ChapterBattleTemplate;
import game.module.template.MyDungeonTemplate;
import game.module.template.RewardTemplateRange;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;
import ws.WsMessageDungeon;
import ws.WsMessageHall;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@MsgCodeAnn(msgcode = WsMessageDungeon.C2SDungeonChooseNode.id, accessLimit = 200)
public class DungeonChooseNodeProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(DungeonChooseNodeProcessor.class);

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
        WsMessageDungeon.C2SDungeonChooseNode reqmsg = WsMessageDungeon.C2SDungeonChooseNode.parse(request);
        logger.info("dungeon choose node,player={},req={}", playerId, reqmsg);
        DungeonBean dungeonBean = DungeonCache.getInstance().getDungeon(playerId);
        if (dungeonBean == null || dungeonBean.getDbDungeonNode() == null) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageDungeon.S2CDungeonChooseNode.msgCode, 1442);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        DungeonNode dungeonNode = dungeonBean.getDbDungeonNode().getNodelist().get(1);
        DungeonNode.DungeonNodePos dungeonNodePos = dungeonNode.getPoslist().get(reqmsg.pos);
        dungeonNodePos.setChoose(1);
        //generate node detail
        DungeonNode.DungeonNodeDetail dungeonNodeDetail = dungeonNodePos.getDungeonNodeDetail();
        if (dungeonNodeDetail == null) {
            dungeonNodeDetail = DungeonManager.getInstance().generateNodeDetail(dungeonBean.getChapterIndex(), dungeonNodePos);
        }
        //直接奖励
        switch (dungeonNodePos.getType()) {
            case 4:
                dungeonNodeDetail = new DungeonNode.DungeonNodeDetail();
                MyDungeonTemplate.MyDungeonTemplateStep dungeonTemplateStep =
                        DungeonTemplateCache.getInstance().getDungeonTemplateStep(dungeonBean.getChapterIndex());
                List<MyDungeonTemplate.RewardTemplateSimpleBox> box = dungeonTemplateStep.getBox();
                int randBoxIndex = RandomUtils.nextInt(0, box.size());
                MyDungeonTemplate.RewardTemplateSimpleBox rewardTemplateSimpleBox = box.get(randBoxIndex);
                List<WsMessageBase.IORewardItem> goods = new ArrayList<>();
                for (RewardTemplateRange rewardTemplateRange : rewardTemplateSimpleBox.getGoods()) {
                    int gsid = rewardTemplateRange.getGSID();
                    List<Integer> count = rewardTemplateRange.getCOUNT();
                    int rewardCount = RandomUtils.nextInt(count.get(0), count.get(1) + 1);
                    AwardUtils.changeRes(playingRole, gsid, rewardCount, LogConstants.MODULE_DUNGEON);
                    goods.add(new WsMessageBase.IORewardItem(gsid, rewardCount));
                }
                dungeonNodeDetail.setId(rewardTemplateSimpleBox.getId());
                dungeonNodeDetail.setQuality(rewardTemplateSimpleBox.getQuality());
                dungeonNodeDetail.setGoods(goods);
                break;
            case 7:
                Map<Integer, Integer> potions = dungeonBean.getPotions();
                if (potions == null) {
                    potions = new HashMap<>();
                    dungeonBean.setPotions(potions);
                }
                if (potions.containsKey(DungeonConstants.REWARD_POTION_ID)) {
                    potions.put(DungeonConstants.REWARD_POTION_ID, potions.get(DungeonConstants.REWARD_POTION_ID) + 1);
                } else {
                    potions.put(DungeonConstants.REWARD_POTION_ID, 1);
                }
                break;
            case 8:
                Integer buffid = dungeonNodeDetail.getId();
                //add buff
                DungeonManager.getInstance().addSpBuff(dungeonBean, buffid);
                break;
        }
        if (dungeonNodePos.getType().equals(4) || dungeonNodePos.getType().equals(7) || dungeonNodePos.getType().equals(8)) {
            DungeonManager.getInstance().move1Step(dungeonBean);
        }
        //save
        DungeonDaoHelper.asyncUpdateDungeon(dungeonBean);
        //ret
        WsMessageDungeon.S2CDungeonChooseNode respmsg = new WsMessageDungeon.S2CDungeonChooseNode();
        respmsg.type = dungeonNodePos.getType();
        respmsg.detail = new WsMessageBase.IODungeonChooseDetail();
        switch (dungeonNodePos.getType()) {
            case 1:
            case 2:
            case 3:
                respmsg.detail.id = dungeonNodeDetail.getId();
                respmsg.detail.name = dungeonNodeDetail.getName();
                respmsg.detail.gsid = dungeonNodeDetail.getGsid();
                respmsg.detail.set = new HashMap<>();
                for (Map.Entry<Integer, ChapterBattleTemplate> aEntry : dungeonNodeDetail.getBattleset().entrySet()) {
                    ChapterBattleTemplate chapterBattleTemplate = aEntry.getValue();
                    respmsg.detail.set.put(aEntry.getKey(), new WsMessageBase.IODungeonBset(chapterBattleTemplate.getGsid(), chapterBattleTemplate.getLevel(),
                            chapterBattleTemplate.getPclass(), chapterBattleTemplate.getExhp(), chapterBattleTemplate.getExatk()));
                }
                respmsg.detail.hppercent = dungeonNodeDetail.getEnemyHpPercent();
                respmsg.detail.buffs = dungeonNodeDetail.getBuffs();
                break;
            case 4://宝箱奖励
                respmsg.detail.id = dungeonNodeDetail.getId();
                respmsg.detail.quality = dungeonNodeDetail.getQuality();
                respmsg.detail.goods = dungeonNodeDetail.getGoods();
                break;
            case 6://shop
                respmsg.detail.disc = dungeonNodeDetail.getDisc();
                respmsg.detail.item = dungeonNodeDetail.getItem();
                respmsg.detail.consume = dungeonNodeDetail.getConsume();
                respmsg.detail.quality = dungeonNodeDetail.getQuality();
                respmsg.detail.refnum = dungeonNodeDetail.getRefnum();
                break;
            case 7:
                respmsg.detail.id = DungeonConstants.REWARD_POTION_ID;
                break;
            case 8:
                respmsg.detail.id = dungeonNodeDetail.getId();
                break;
        }
        //ret
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
