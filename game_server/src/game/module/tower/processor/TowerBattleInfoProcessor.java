package game.module.tower.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.template.ChapterBattleTemplate;
import game.module.tower.dao.TowerBattleTemplateCache;
import game.module.user.logic.PlayerServerPropManager;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;
import ws.WsMessageBattle;
import ws.WsMessageHall;

import java.util.ArrayList;
import java.util.Map;

/**
 * @author hexuhui
 */
@MsgCodeAnn(msgcode = WsMessageBattle.C2STowerBattleInfo.id, accessLimit = 200)
public class TowerBattleInfoProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(TowerBattleInfoProcessor.class);

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
        logger.info("tower battle info!player={}", playerId);
        int towerId = PlayerServerPropManager.getInstance().getTower(playerId);
        Map<Integer, ChapterBattleTemplate> bigBattleTeam = TowerBattleTemplateCache.getInstance().getTowerBattleById(towerId);
        if (bigBattleTeam == null) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageBattle.S2CTowerBattleInfo.msgCode, 1710);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        WsMessageBattle.S2CTowerBattleInfo respmsg = new WsMessageBattle.S2CTowerBattleInfo();
        respmsg.items = new ArrayList<>(bigBattleTeam.size());
        for (Map.Entry<Integer, ChapterBattleTemplate> aEntry : bigBattleTeam.entrySet()) {
            WsMessageBase.IOGeneralSimple ioGeneralSimple = new WsMessageBase.IOGeneralSimple();
            ioGeneralSimple.pos = aEntry.getKey();
            ChapterBattleTemplate generalConfig = aEntry.getValue();
            ioGeneralSimple.gsid = generalConfig.getGsid();
            ioGeneralSimple.level = generalConfig.getLevel();
            ioGeneralSimple.pclass = generalConfig.getPclass();
            if (generalConfig.getExhp() != null) {
                ioGeneralSimple.exhp = generalConfig.getExhp();
            }
            if (generalConfig.getExatk() != null) {
                ioGeneralSimple.exatk = generalConfig.getExatk();
            }
            respmsg.items.add(ioGeneralSimple);
        }
        //ret
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
