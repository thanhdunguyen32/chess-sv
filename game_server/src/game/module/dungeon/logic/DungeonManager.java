package game.module.dungeon.logic;

import game.module.dungeon.bean.DungeonBean;
import game.module.dungeon.bean.DungeonBuff;
import game.module.dungeon.bean.DungeonNode;
import game.module.dungeon.dao.DungeonCache;
import game.module.dungeon.dao.DungeonChooseBuffTemplateCache;
import game.module.dungeon.dao.DungeonDaoHelper;
import game.module.dungeon.dao.DungeonTemplateCache;
import game.module.exped.logic.FormationRobotManager;
import game.module.hero.dao.GeneralTemplateCache;
import game.module.template.*;
import lion.math.RandomDispatcher;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;

import java.util.*;

/**
 * @author HeXuhui
 */
public class DungeonManager {

    private static Logger logger = LoggerFactory.getLogger(DungeonManager.class);

    public DungeonNode.DungeonNodeDetail generateNodeDetail(int chapterIndex, DungeonNode.DungeonNodePos dungeonNodePos) {
        DungeonNode.DungeonNodeDetail dungeonNodeDetail = null;
        switch (dungeonNodePos.getType()) {
            case 1://小怪
                dungeonNodeDetail = new DungeonNode.DungeonNodeDetail();
                dungeonNodePos.setDungeonNodeDetail(dungeonNodeDetail);
                MyDungeonTemplate.MyDungeonTemplateStep dungeonTemplateStep = DungeonTemplateCache.getInstance().getDungeonTemplateStep(chapterIndex);
                int[] soldierConfig = dungeonTemplateStep.getSoldier();
                Map<Integer, ChapterBattleTemplate> battlePlayerBaseMap = generateEnemyRobots(soldierConfig);
                dungeonNodeDetail.setId(RandomUtils.nextInt((chapterIndex + 1) * 100, (chapterIndex + 1) * 100 + 10));
                int gsid = battlePlayerBaseMap.values().iterator().next().getGsid();
                dungeonNodeDetail.setGsid(gsid);
                GeneralTemplate heroTemplate = GeneralTemplateCache.getInstance().getHeroTemplate(gsid);
                String rname = heroTemplate.getNAME() + heroTemplate.getSTAR() + "星" + GeneralTemplateCache.getInstance().getOccuCn(heroTemplate.getOCCU());
                dungeonNodeDetail.setName(rname);
                Map<Integer, Integer> hppercent = new HashMap<>();
                for (int formationPos : battlePlayerBaseMap.keySet()) {
                    hppercent.put(formationPos, 10000);
                }
                dungeonNodeDetail.setEnemyHpPercent(hppercent);
                //rand buff
                List<Integer> bufflist = randBuffs(chapterIndex, 1, 3);
                dungeonNodeDetail.setBuffs(bufflist);
                dungeonNodeDetail.setBattleset(battlePlayerBaseMap);
                break;
            case 2://精英
                dungeonNodeDetail = new DungeonNode.DungeonNodeDetail();
                dungeonNodePos.setDungeonNodeDetail(dungeonNodeDetail);
                dungeonTemplateStep = DungeonTemplateCache.getInstance().getDungeonTemplateStep(chapterIndex);
                int[] eliteConfig = dungeonTemplateStep.getElite();
                battlePlayerBaseMap = generateEnemyRobots(eliteConfig);
                dungeonNodeDetail.setId(RandomUtils.nextInt((chapterIndex + 1) * 100, (chapterIndex + 1) * 100 + 10));
                gsid = battlePlayerBaseMap.values().iterator().next().getGsid();
                dungeonNodeDetail.setGsid(gsid);
                heroTemplate = GeneralTemplateCache.getInstance().getHeroTemplate(gsid);
                rname = heroTemplate.getNAME() + heroTemplate.getSTAR() + "星" + GeneralTemplateCache.getInstance().getOccuCn(heroTemplate.getOCCU());
                dungeonNodeDetail.setName(rname);
                hppercent = new HashMap<>();
                for (int formationPos : battlePlayerBaseMap.keySet()) {
                    hppercent.put(formationPos, 10000);
                }
                dungeonNodeDetail.setEnemyHpPercent(hppercent);
                //rand buff
                bufflist = randBuffs(chapterIndex, 2, 3);
                dungeonNodeDetail.setBuffs(bufflist);
                dungeonNodeDetail.setBattleset(battlePlayerBaseMap);
                break;
            case 3://BOSS
                dungeonNodeDetail = new DungeonNode.DungeonNodeDetail();
                dungeonNodePos.setDungeonNodeDetail(dungeonNodeDetail);
                dungeonTemplateStep = DungeonTemplateCache.getInstance().getDungeonTemplateStep(chapterIndex);
                int[] bossConfig = dungeonTemplateStep.getBoss();
                battlePlayerBaseMap = generateEnemyRobots(bossConfig);
                dungeonNodeDetail.setId(RandomUtils.nextInt((chapterIndex + 1) * 100, (chapterIndex + 1) * 100 + 10));
                gsid = battlePlayerBaseMap.values().iterator().next().getGsid();
                dungeonNodeDetail.setGsid(gsid);
                heroTemplate = GeneralTemplateCache.getInstance().getHeroTemplate(gsid);
                rname = heroTemplate.getNAME() + heroTemplate.getSTAR() + "星" + GeneralTemplateCache.getInstance().getOccuCn(heroTemplate.getOCCU());
                dungeonNodeDetail.setName(rname);
                hppercent = new HashMap<>();
                for (int formationPos : battlePlayerBaseMap.keySet()) {
                    hppercent.put(formationPos, 10000);
                }
                dungeonNodeDetail.setEnemyHpPercent(hppercent);
                //rand buff
                bufflist = randBuffs(chapterIndex, 3, 3);
                dungeonNodeDetail.setBuffs(bufflist);
                dungeonNodeDetail.setBattleset(battlePlayerBaseMap);
                break;
            case 5://问号
                int randomNodeType = getRandomSecretType();
                dungeonNodePos.setType(randomNodeType);
                dungeonNodeDetail = generateNodeDetail(chapterIndex, dungeonNodePos);
                break;
            case 6://商店
                dungeonNodeDetail = new DungeonNode.DungeonNodeDetail();
                dungeonNodePos.setDungeonNodeDetail(dungeonNodeDetail);
                List<MyDungeonTemplate.MyDungeonTemplateShop> dungeonTemplateShop = DungeonTemplateCache.getInstance().getDungeonTemplateShop();
                MyDungeonTemplate.MyDungeonTemplateShop myDungeonTemplateShop = dungeonTemplateShop.get(RandomUtils.nextInt(0, dungeonTemplateShop.size()));
                dungeonNodeDetail.setDisc(myDungeonTemplateShop.getDisc());
                List<WsMessageBase.IORewardItem> itemList = new ArrayList<>();
                for (RewardTemplateSimple rewardTemplateSimple : myDungeonTemplateShop.getItem()) {
                    itemList.add(new WsMessageBase.IORewardItem(rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT()));
                }
                dungeonNodeDetail.setItem(itemList);
                List<WsMessageBase.IORewardItem> consumeList = new ArrayList<>();
                for (RewardTemplateSimple rewardTemplateSimple : myDungeonTemplateShop.getConsume()) {
                    consumeList.add(new WsMessageBase.IORewardItem(rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT()));
                }
                dungeonNodeDetail.setConsume(consumeList);
                dungeonNodeDetail.setQuality(3);
                dungeonNodeDetail.setRefnum(0);
                break;
            case 8://buff转盘奖励
                dungeonNodeDetail = new DungeonNode.DungeonNodeDetail();
                dungeonNodePos.setDungeonNodeDetail(dungeonNodeDetail);
                //rand buff
                bufflist = randBuffs(chapterIndex, 8, 1);
                dungeonNodeDetail.setId(bufflist.get(0));
                break;
        }
        return dungeonNodeDetail;
    }

    private Map<Integer, ChapterBattleTemplate> generateEnemyRobots(int[] soldierConfig) {
        int gLevel = soldierConfig[0];
        int gCount = soldierConfig[1];
        return FormationRobotManager.getInstance().generateRobotRaw(gLevel, gCount);
    }

    private List<Integer> randBuffs(int chapterIndex, int bufftype, int buffSum) {
        List<Integer> retlist = new ArrayList<>();
        List<DungeonChooseBuffTemplate> buffTemplateList = DungeonChooseBuffTemplateCache.getInstance().getTemplateList(chapterIndex + 1, bufftype);
        RandomDispatcher<Integer> rd = new RandomDispatcher<>();
        for (DungeonChooseBuffTemplate dungeonChooseBuffTemplate : buffTemplateList) {
            rd.put(1, dungeonChooseBuffTemplate.getId());
        }
        for (int i = 0; i < buffSum; i++) {
            int buffid = rd.randomRemove();
            retlist.add(buffid);
        }
        return retlist;
    }

    public void move1Step(DungeonBean dungeonBean) {
        Integer myNode = dungeonBean.getNode();
        logger.info("dungeon move1step,chapter={},node={},pos={}", dungeonBean.getChapterIndex(), myNode, dungeonBean.getPos());
        //chapter++
        if (myNode >= 14) {
            dungeonBean.setChapterIndex(dungeonBean.getChapterIndex() + 1);
            dungeonBean.setNode(-1);
            dungeonBean.setPos(0);
            dungeonBean.setDungeonBuff(dungeonBean.getSpBuff());
            dungeonBean.setSpBuff(null);
            //award get
            dungeonBean.getChapterAwardGet().add(0);
            DungeonNode.DbDungeonNode dbDungeonNode = initNodeList();
            dungeonBean.setDbDungeonNode(dbDungeonNode);
        } else {
            //node ++
            dungeonBean.setNode(++myNode);
            //TODO check finish
            List<DungeonNode> nodelist = dungeonBean.getDbDungeonNode().getNodelist();
            DungeonNode dungeonNode1 = nodelist.get(1);
            List<DungeonNode.DungeonNodePos> node1Poslist = dungeonNode1.getPoslist();
            int posIndex = 0;
            for (DungeonNode.DungeonNodePos dungeonNodePos : node1Poslist) {
                if (dungeonNodePos.getChoose() != null && dungeonNodePos.getChoose().equals(1)) {
                    dungeonNodePos.setFinish(System.currentTimeMillis());
                    dungeonBean.setPos(posIndex);
                }
                posIndex++;
            }
            //
            nodelist.remove(0);
            if (myNode < 14) {
                DungeonNode dungeonNode = new DungeonNode();
                List<DungeonNode.DungeonNodePos> dungeonNodePosList = generateNodeList(myNode + 2);
                dungeonNode.setPoslist(dungeonNodePosList);
                nodelist.add(dungeonNode);
            }
        }
    }

    public void resetCheck(DungeonBean dungeonBean) {
        Date updateTime = dungeonBean.getUpdateTime();
        Calendar now = Calendar.getInstance();
        if (now.getTime().getTime() - updateTime.getTime() >= 3600 * 24 * 2 * 1000L) {
            int dayOfWeek = now.get(Calendar.DAY_OF_WEEK);
            if (DungeonConstants.OPEN_TIME.contains(dayOfWeek - 1)) {
                resetDungeon(dungeonBean);
            }
        }
    }

    public WsMessageBase.IODungeonTop getDungeonInfo(Integer playerId) {
        DungeonBean dungeonBean = DungeonCache.getInstance().getDungeon(playerId);
        if (dungeonBean == null) {
            return new WsMessageBase.IODungeonTop(0, -1);
        } else {
            return new WsMessageBase.IODungeonTop(dungeonBean.getChapterIndex(), dungeonBean.getNode());
        }
    }

    static class SingletonHolder {

        static DungeonManager instance = new DungeonManager();


    }

    public static DungeonManager getInstance() {
        return SingletonHolder.instance;
    }

    public DungeonBean createDungeon(int playerId) {
        DungeonBean dungeonBean = new DungeonBean();
        dungeonBean.setPlayerId(playerId);
        dungeonBean.setChapterIndex(0);
        dungeonBean.setNode(-1);
        dungeonBean.setPos(0);
        dungeonBean.setChapterAwardGet(new ArrayList<>(Collections.singletonList(0)));
        DungeonNode.DbDungeonNode dbDungeonNode = initNodeList();
        dungeonBean.setDbDungeonNode(dbDungeonNode);
        dungeonBean.setUpdateTime(new Date());
        return dungeonBean;
    }

    private DungeonNode.DbDungeonNode initNodeList() {
        DungeonNode.DbDungeonNode dbDungeonNode = new DungeonNode.DbDungeonNode();
        List<DungeonNode> nodelist = new ArrayList<>();
        dbDungeonNode.setNodelist(nodelist);
        //-1
        DungeonNode dungeonNode = new DungeonNode();
        List<DungeonNode.DungeonNodePos> dungeonNodePosList = new ArrayList<>();
        dungeonNode.setPoslist(dungeonNodePosList);
        dungeonNodePosList.add(new DungeonNode.DungeonNodePos(-1, 0, 0L));
        nodelist.add(dungeonNode);
        //0
        DungeonNode dungeonNode0 = new DungeonNode();
        dungeonNodePosList = generateNodeList(0);
        dungeonNode0.setPoslist(dungeonNodePosList);
        nodelist.add(dungeonNode0);
        //1
        DungeonNode dungeonNode1 = new DungeonNode();
        dungeonNodePosList = generateNodeList(1);
        dungeonNode1.setPoslist(dungeonNodePosList);
        nodelist.add(dungeonNode1);
        return dbDungeonNode;
    }

    private List<DungeonNode.DungeonNodePos> generateNodeList(int nodeIndex) {
        if (nodeIndex + 1 >= DungeonConstants.DUNGEON_NODE_SIZE.length) {
            return null;
        }
        int nodeNum = DungeonConstants.DUNGEON_NODE_SIZE[nodeIndex + 1];
        List<DungeonNode.DungeonNodePos> dungeonNodePosList = new ArrayList<>();
        for (int i = 0; i < nodeNum; i++) {
            if (nodeIndex >= 15) {//boss节点
                dungeonNodePosList.add(new DungeonNode.DungeonNodePos(3, 0, 0L));
            } else {
                dungeonNodePosList.add(new DungeonNode.DungeonNodePos(getRandomNodeType(), 0, 0L));
            }
        }
        return dungeonNodePosList;
    }

    public int getRandomNodeType() {
        RandomDispatcher<Integer> rd = new RandomDispatcher<>();
        for (int[] randlist : DungeonConstants.DUNGEON_TYPE_RATE) {
            int aType = randlist[0];
            rd.put(randlist[1], aType);
        }
        return rd.random();
    }

    public int getRandomSecretType() {
        int[] rewardlist = {1, 2, 4, 8};
        return rewardlist[RandomUtils.nextInt(0, rewardlist.length)];
    }

    public void addBuff(DungeonBean dungeonBean, int buffid) {
        DungeonChooseBuffTemplate dungeonChooseBuffTemplate = DungeonChooseBuffTemplateCache.getInstance().getDungeonChooseBuffTemplate(buffid);
        DungeonBuff dungeonBuff = dungeonBean.getDungeonBuff();
        if (dungeonBuff == null) {
            dungeonBuff = new DungeonBuff(0f, 0f, 0f, 0f, 0f, 0f);
            dungeonBean.setDungeonBuff(dungeonBuff);
        }
        add1Buff(dungeonBuff, dungeonChooseBuffTemplate);
    }

    public void addSpBuff(DungeonBean dungeonBean, int buffid) {
        DungeonChooseBuffTemplate dungeonChooseBuffTemplate = DungeonChooseBuffTemplateCache.getInstance().getDungeonChooseBuffTemplate(buffid);
        DungeonBuff dungeonBuff = dungeonBean.getSpBuff();
        if (dungeonBuff == null) {
            dungeonBuff = new DungeonBuff(0f, 0f, 0f, 0f, 0f, 0f);
            dungeonBean.setSpBuff(dungeonBuff);
        }
        add1Buff(dungeonBuff, dungeonChooseBuffTemplate);
    }

    private void add1Buff(DungeonBuff dungeonBuff, DungeonChooseBuffTemplate dungeonChooseBuffTemplate) {
        switch (dungeonChooseBuffTemplate.getNATUREID()) {
            case "pskidam":
                dungeonBuff.setPskidam(dungeonBuff.getPskidam() + dungeonChooseBuffTemplate.getADDITION());
                break;
            case "pcrid":
                dungeonBuff.setPcrid(dungeonBuff.getPcrid() + dungeonChooseBuffTemplate.getADDITION());
                break;
            case "pcri":
                dungeonBuff.setPcri(dungeonBuff.getPcri() + dungeonChooseBuffTemplate.getADDITION());
                break;
            case "atk":
                dungeonBuff.setAtk(dungeonBuff.getAtk() + dungeonChooseBuffTemplate.getADDITION());
                break;
            case "ppthr":
                dungeonBuff.setPpthr(dungeonBuff.getPpthr() + dungeonChooseBuffTemplate.getADDITION());
                break;
            case "pmthr":
                dungeonBuff.setPmthr(dungeonBuff.getPmthr() + dungeonChooseBuffTemplate.getADDITION());
                break;
        }
    }

    public void resetDungeon(DungeonBean dungeonBean) {
        Map<Long, Integer> onlineGenerals = dungeonBean.getOnlineGenerals();
        if (onlineGenerals != null) {
            onlineGenerals.clear();
        }
        dungeonBean.setNode(-1);
        dungeonBean.setPos(0);
        List<Integer> chapterAwardGet = dungeonBean.getChapterAwardGet();
        if (chapterAwardGet != null) {
            for (int i = 0; i < chapterAwardGet.size(); i++) {
                chapterAwardGet.set(i, 0);
            }
        }
        DungeonNode.DbDungeonNode dbDungeonNode = initNodeList();
        dungeonBean.setDbDungeonNode(dbDungeonNode);
        dungeonBean.setDungeonBuff(null);
        dungeonBean.setSpBuff(null);
        Set<Integer> shopBuy = dungeonBean.getShopBuy();
        if (shopBuy != null) {
            shopBuy.clear();
        }
        dungeonBean.setUpdateTime(new Date());
        //save 2 db
        DungeonDaoHelper.asyncUpdateDungeon(dungeonBean);
    }

}
