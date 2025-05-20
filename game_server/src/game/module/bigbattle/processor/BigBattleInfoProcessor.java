package game.module.bigbattle.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.bigbattle.dao.BigBattleTemplateCache;
import game.module.template.ChapterBattleTemplate;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;
import ws.WsMessageBigbattle;
import ws.WsMessageHall;

import java.util.ArrayList;
import java.util.Map;

/**
 * @author hexuhui
 */
@MsgCodeAnn(msgcode = WsMessageBigbattle.C2SBigBattleInfo.id, accessLimit = 200)
public class BigBattleInfoProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(BigBattleInfoProcessor.class);

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
        WsMessageBigbattle.C2SBigBattleInfo reqmsg = WsMessageBigbattle.C2SBigBattleInfo.parse(request);
        logger.info("big battle info!player={},req={}", playerId, reqmsg);
        int mapid = reqmsg.mapid;
        Map<Integer, ChapterBattleTemplate> bigBattleTeam = BigBattleTemplateCache.getInstance().getBigBattleByMapId(mapid);
        if (bigBattleTeam == null) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageBigbattle.S2CBigBattleInfo.msgCode, 1710);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        WsMessageBigbattle.S2CBigBattleInfo respmsg = new WsMessageBigbattle.S2CBigBattleInfo();
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
