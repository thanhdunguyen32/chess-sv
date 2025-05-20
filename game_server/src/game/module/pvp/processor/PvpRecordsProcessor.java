package game.module.pvp.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.pvp.bean.PvpRecord;
import game.module.pvp.dao.PvpRecordCache;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;
import ws.WsMessageBattle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author hexuhui
 */
@MsgCodeAnn(msgcode = WsMessageBattle.C2SPvpRecords.id, accessLimit = 200)
public class PvpRecordsProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(PvpRecordsProcessor.class);

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
        logger.info("pvp records!player={}", playerId);
        //remove >10
        PvpRecord pvpRecord = PvpRecordCache.getInstance().getPvpRecord(playerId);
        if (pvpRecord != null && pvpRecord.getDbPvpRecord() != null) {
            List<WsMessageBase.IOPvpRecord> records = pvpRecord.getDbPvpRecord().getRecords();
            while (records.size() > 10) {
                records.remove(0);
            }
        }
        //ret
        WsMessageBattle.S2CPvpRecords respmsg = new WsMessageBattle.S2CPvpRecords();
        if (pvpRecord != null) {
            respmsg.records = new ArrayList<>(pvpRecord.getDbPvpRecord().getRecords());
            Collections.reverse(respmsg.records);
        }
        //ret
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
