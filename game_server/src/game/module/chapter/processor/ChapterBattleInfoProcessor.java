package game.module.chapter.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.chapter.bean.ChapterBean;
import game.module.chapter.dao.ChapterBattleTemplateCache;
import game.module.chapter.dao.ChapterCache;
import game.module.chapter.logic.ChapterManager;
import game.module.template.ChapterBattleTemplate;
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
@MsgCodeAnn(msgcode = WsMessageBattle.C2SChapterInfo.id, accessLimit = 200)
public class ChapterBattleInfoProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(ChapterBattleInfoProcessor.class);

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
        logger.info("chapter battle info!player={}", playerId);
        ChapterBean chapterBean = ChapterCache.getInstance().getPlayerChapter(playerId);
        Integer maxMapId = 0;
        if (chapterBean == null) {
            maxMapId = ChapterManager.getInstance().getInitMapId();
        } else {
            maxMapId = chapterBean.getMaxMapId();
        }

        Map<Integer, ChapterBattleTemplate> chapterBattleItem = ChapterBattleTemplateCache.getInstance().getChapterBattleById(maxMapId);
        if (chapterBattleItem == null) {
            WsMessageHall.S2CErrorCode respmsg = new WsMessageHall.S2CErrorCode(WsMessageBattle.S2CChapterInfo.msgCode, 1710);
            playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
            return;
        }
        WsMessageBattle.S2CChapterInfo respmsg = new WsMessageBattle.S2CChapterInfo();
        respmsg.items = new ArrayList<>(chapterBattleItem.size());
        for (Map.Entry<Integer, ChapterBattleTemplate> aEntry : chapterBattleItem.entrySet()) {
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
