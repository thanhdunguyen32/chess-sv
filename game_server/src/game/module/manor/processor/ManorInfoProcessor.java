package game.module.manor.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.award.bean.GameConfig;
import game.module.award.logic.AwardUtils;
import game.module.item.logic.ItemManager;
import game.module.log.constants.LogConstants;
import game.module.manor.bean.ManorBean;
import game.module.manor.dao.ManorCache;
import game.module.manor.dao.ManorDaoHelper;
import game.module.manor.dao.ManorTemplateCache;
import game.module.manor.logic.ManorConstants;
import game.module.manor.logic.ManorManager;
import game.module.template.ManorTemplate;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;
import ws.WsMessageManor;

import java.util.ArrayList;
import java.util.Date;

@MsgCodeAnn(msgcode = WsMessageManor.C2SManorInfo.id, accessLimit = 200)
public class ManorInfoProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(ManorInfoProcessor.class);

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
        logger.info("manor info,playerId={}", playerId);
        WsMessageManor.S2CManorInfo respmsg = new WsMessageManor.S2CManorInfo();
        //manor send food
        ManorBean manorBean = ManorCache.getInstance().getManor(playerId);
        if (manorBean != null) {
            Integer manorLevel = manorBean.getLevel();
            Date gainFoodTime = manorBean.getGainFoodTime();
            int foodCount = ItemManager.getInstance().getCount(playingRole.getPlayerBean(), GameConfig.PLAYER.FOOD);
            ManorTemplate.ManorHomeTemplate manorHomeTemplate = ManorTemplateCache.getInstance().getManorHomeTemplate(manorLevel);
            Integer maxFood = manorHomeTemplate.getUPITEM().get(0).getCOUNT();
            if (foodCount < maxFood && (System.currentTimeMillis() - gainFoodTime.getTime()) / 1000 / 3600 >= ManorConstants.MANOR_FODD_HOUR) {
                int addFood = (int) ((System.currentTimeMillis() - gainFoodTime.getTime()) / 1000 / 3600 / ManorConstants.MANOR_FODD_HOUR);
                if (addFood + foodCount > maxFood) {
                    addFood = maxFood - foodCount;
                }
                //reward
                AwardUtils.changeRes(playingRole, GameConfig.PLAYER.FOOD, addFood, LogConstants.MODULE_MANOR);
//                foodCount = ItemManager.getInstance().getCount(playingRole.getPlayerBean(), GameConfig.PLAYER.FOOD);
//                respmsg.items = Collections.singletonList(new WsMessageBase.IORewardItem(GameConfig.PLAYER.FOOD, foodCount));
            }
            //save
            int leftSeconds = (int) ((System.currentTimeMillis() - gainFoodTime.getTime()) / 1000) % (3600 * ManorConstants.MANOR_FODD_HOUR);
            manorBean.setGainFoodTime(DateUtils.addSeconds(new Date(), -leftSeconds));
            ManorDaoHelper.asyncUpdateManor(manorBean);
        }
        //ret
        if (manorBean == null) {
            manorBean = ManorManager.getInstance().createManor(playingRole);
            ManorCache.getInstance().addManor(manorBean);
            ManorDaoHelper.asyncInsertManor(manorBean);
        }
        respmsg.manorlv = manorBean.getLevel();
        if (manorBean.getManorBuilding() != null) {
            respmsg.buildings = new ArrayList<>(manorBean.getManorBuilding().getBuildings().size());
            for (ManorBean.DbManorBuilding1 manorBuilding1 : manorBean.getManorBuilding().getBuildings().values()) {
                WsMessageBase.IOManorBuilding ioManorBuilding = new WsMessageBase.IOManorBuilding();
                respmsg.buildings.add(ioManorBuilding);
                ioManorBuilding.id = manorBuilding1.getId();
                ioManorBuilding.lv = manorBuilding1.getLevel();
                ioManorBuilding.pos = manorBuilding1.getPos();
                ioManorBuilding.type = manorBuilding1.getType();
                ioManorBuilding.rid = manorBuilding1.getRid();
                ioManorBuilding.lastgain = manorBuilding1.getLastGain();
                //rewards
            }
        }
        //ret
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
