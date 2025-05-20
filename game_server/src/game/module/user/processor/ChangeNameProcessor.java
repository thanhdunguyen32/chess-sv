package game.module.user.processor;

import game.GameServer;
import game.common.CommonUtils;
import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.award.bean.GameConfig;
import game.module.award.logic.AwardUtils;
import game.module.badword.logic.BadWordFilter;
import game.module.item.logic.ItemConstants;
import game.module.log.constants.LogConstants;
import game.module.user.dao.PlayerDao;
import game.module.user.logic.PlayerManager;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageHall.C2SChangeName;
import ws.WsMessageHall.S2CChangeName;
import ws.WsMessageHall.S2CErrorCode;

@MsgCodeAnn(msgcode = C2SChangeName.id, accessLimit = 200)
public class ChangeNameProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(ChangeNameProcessor.class);

    @Override
    public void processByte(PlayingRole playingRole, RequestByteMessage request) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void processProto(final PlayingRole hero, RequestProtoMessage request) throws Exception {

    }

    @Override
    public void processMy(final PlayingRole playingRole, MyRequestMessage request) throws Exception {
        C2SChangeName reqMsg = C2SChangeName.parse(request);
        final String newName = reqMsg.name.trim();
        int playerId = playingRole.getId();
        logger.info("change name,player={},name={}", playerId, newName);
        if (StringUtils.isBlank(newName) || CommonUtils.getStrLength(newName) > 12) {
            S2CErrorCode respMsg = new S2CErrorCode(S2CChangeName.msgCode, 110);
            playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
            return;
        }
        if (newName.equals(playingRole.getPlayerBean().getName())) {
            S2CErrorCode respMsg = new S2CErrorCode(S2CChangeName.msgCode, 112);
            playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
            return;
        }
        // 包含\n
        if (newName.indexOf('\n') > -1) {
            S2CErrorCode respMsg = new S2CErrorCode(S2CChangeName.msgCode, 110);
            playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
            return;
        }
        // 钻石是否够
        int changeNameFreeCount = PlayerManager.getInstance().getOtherCount(playerId, ItemConstants.CHANGE_NAME_1_MARK);
        final int costMoney = 500;
        if (changeNameFreeCount <= 0 && playingRole.getPlayerBean().getMoney() < costMoney) {
            S2CErrorCode respMsg = new S2CErrorCode(S2CChangeName.msgCode, 114);
            playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
            return;
        }
        // 名字是否重复
        GameServer.executorService.execute(() -> {
            boolean existUser = PlayerDao.getInstance().checkExistByUserName(newName);
            if (existUser) {
                S2CErrorCode respMsg = new S2CErrorCode(S2CChangeName.msgCode, 112);
                playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
            } else {
                // bad word filter
                boolean hasBadWord = BadWordFilter.getInstance().hasBadWords(newName);
                if (hasBadWord) {
                    S2CErrorCode respMsg = new S2CErrorCode(S2CChangeName.msgCode, 151);
                    playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
                    return;
                }
                playingRole.getPlayerBean().setName(newName);
                // 扣钻石
                if(changeNameFreeCount >0 ){
                    AwardUtils.setRes(playingRole, ItemConstants.CHANGE_NAME_1_MARK, 0, true);
                }else {
                    AwardUtils.changeRes(playingRole, GameConfig.PLAYER.YB, -costMoney, LogConstants.MODULE_CHANGE_NAME);
                }
                // ret
                S2CChangeName respMsg = new S2CChangeName(newName);
                playingRole.writeAndFlush(respMsg.build(playingRole.alloc()));
            }
        });
    }

}
