package game.module.mapevent.processor;

import game.GameServer;
import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.award.logic.AwardUtils;
import game.module.log.constants.LogConstants;
import game.module.mapevent.bean.MapEvent;
import game.module.mapevent.bean.PlayerMapEvent;
import game.module.mapevent.dao.MapEventCache;
import game.module.mapevent.dao.MapEventDao;
import game.module.mapevent.dao.MapEventTemplateCache;
import game.module.template.MapEventTemplate;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;
import ws.WsMessageBattle;
import ws.WsMessageHall;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hexuhui
 */
@MsgCodeAnn(msgcode = WsMessageBattle.C2SMapEventFinish.id, accessLimit = 200)
public class MapEventFinishProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(MapEventFinishProcessor.class);

    @Override
    public void processByte(PlayingRole playingRole, RequestByteMessage request) throws Exception {

    }

    /**
     * 加载所有邮件
     */
    @Override
    public void processProto(PlayingRole playingRole, RequestProtoMessage request) throws Exception {
    }

    @Override
    public void processMy(PlayingRole playingRole, MyRequestMessage request) throws Exception {
        int playerId = playingRole.getId();
        WsMessageBattle.C2SMapEventFinish reqmsg = WsMessageBattle.C2SMapEventFinish.parse(request);
        logger.info("map event finish!player={},req={}", playerId, reqmsg);
        int cityId = reqmsg.cityId;
        int giveup = reqmsg.giveup;
        int option = reqmsg.option;
        //是否存在
        PlayerMapEvent mapEvent = MapEventCache.getInstance().getMapEvent(playerId);
        if (mapEvent == null || mapEvent.getDbMapEvent() == null || mapEvent.getDbMapEvent().getEvents() == null
                || !mapEvent.getDbMapEvent().getEvents().containsKey(cityId)) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageBattle.S2CMapEventFinish.msgCode, 1509);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        //do
        List<WsMessageBase.IORewardItem> rewardItems = new ArrayList<>();
        MapEvent mapEvent1 = mapEvent.getDbMapEvent().getEvents().get(cityId);
        Integer eventType = mapEvent1.getType();
        Integer eid = mapEvent1.getEid();
        switch (eventType) {
            case 1:
                MapEventTemplate.MapEvent1Template mapEvent11 = MapEventTemplateCache.getInstance().getMapEvent1(eid);
                MapEventTemplate.RewardTemplateSimple1 rewardTemplateSimple1 = mapEvent11.getItems().get(option);
                AwardUtils.changeRes(playingRole, rewardTemplateSimple1.getGsid(), rewardTemplateSimple1.getCount(), LogConstants.MODULE_MAP_EVENT);
                rewardItems.add(new WsMessageBase.IORewardItem(rewardTemplateSimple1.getGsid(), rewardTemplateSimple1.getCount()));
                break;
            case 2:
                MapEventTemplate.MapEvent2Template mapEvent12 = MapEventTemplateCache.getInstance().getMapEvent2(eid);
                MapEventTemplate.RewardTemplateSimple2 rewardTemplateSimple2 = mapEvent12.getItems().get(option);
                int randCount = RandomUtils.nextInt(rewardTemplateSimple2.getCount()[0], rewardTemplateSimple2.getCount()[1] + 1);
                AwardUtils.changeRes(playingRole, rewardTemplateSimple2.getGsid(), randCount, LogConstants.MODULE_MAP_EVENT);
                rewardItems.add(new WsMessageBase.IORewardItem(rewardTemplateSimple2.getGsid(), randCount));
                break;
            case 6:
                MapEventTemplate.MapEvent6Template mapEvent16 = MapEventTemplateCache.getInstance().getMapEvent6(eid);
                MapEventTemplate.RewardTemplateSimple6 rewardTemplateSimple6 = mapEvent16.getItems().get(option);
                randCount = RandomUtils.nextInt(rewardTemplateSimple6.getCount()[0], rewardTemplateSimple6.getCount()[1] + 1);
                AwardUtils.changeRes(playingRole, rewardTemplateSimple6.getGsid(), randCount, LogConstants.MODULE_MAP_EVENT);
                rewardItems.add(new WsMessageBase.IORewardItem(rewardTemplateSimple6.getGsid(), randCount));
                break;
        }
        //save bean
        mapEvent.getDbMapEvent().getEvents().remove(cityId);
        GameServer.executorService.execute(() -> {
            MapEventDao.getInstance().updateMapEvent(mapEvent);
        });
        //ret
        WsMessageBattle.S2CMapEventFinish respmsg = new WsMessageBattle.S2CMapEventFinish();
        respmsg.ret = 1;
        respmsg.reward = rewardItems;
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
