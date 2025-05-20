package game.module.user.processor;

import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.user.bean.PlayerHead;
import game.module.user.dao.PlayerHeadCache;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageHall;

import java.util.ArrayList;
import java.util.Set;

@MsgCodeAnn(msgcode = WsMessageHall.C2SHeadsFramesImagesList.id, accessLimit = 200)
public class HeadsFramesImagesListProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(HeadsFramesImagesListProcessor.class);

    @Override
    public void processByte(PlayingRole playingRole, RequestByteMessage request) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void processProto(final PlayingRole hero, RequestProtoMessage request) throws Exception {

    }

    @Override
    public void processMy(final PlayingRole playingRole, MyRequestMessage request) throws Exception {
        int playerId = playingRole.getId();
        logger.info("list head icons,player={}", playerId);
        //do
        PlayerHead playerHeads = PlayerHeadCache.getInstance().getPlayerHead(playerId);
        WsMessageHall.S2CHeadsFramesImagesList respmsg = new WsMessageHall.S2CHeadsFramesImagesList();
        respmsg._icons = "12281|10261|12241|14241|16321|10241|17101|14381|12361|10401|16302|12202|16341|12301|18101|14301|16361|14021|10201|14321|12201|14201" +
                "|10001|10321|16261|14281|10341|16301|10281|12262|16241|10061|17081|18061|10202|14341|16401|14361|16262|10222|10301|10221|14221|12222|10181" +
                "|16281|10242|14101|12221|16222|16381|10381|12061|10361|12321|12261|17061|12001|14181|12041|12341|12161|16001|14161|14242|12121|16221|12181" +
                "|14261|14141|10302|16141|10161|14202|16041|16242|16121|16021|14262|10121|14222|18081|18082|10282|16181|14081|10101|18041";
        if (playerHeads != null) {
            if (playerHeads.getHeadIcons() != null) {
                Set<Integer> headIcons = playerHeads.getHeadIcons();
                respmsg.heads = new ArrayList<>(headIcons);
            }
            if (playerHeads.getHeadFrames() != null) {
                Set<Integer> headFrames = playerHeads.getHeadFrames();
                respmsg.frames = new ArrayList<>(headFrames);
            }
            if (playerHeads.getHeadImages() != null) {
                Set<Integer> headImages = playerHeads.getHeadImages();
                respmsg.images = new ArrayList<>(headImages);
            }
        }
        //ret
        playingRole.writeAndFlush(respmsg.build(playingRole.alloc()));
    }

}
