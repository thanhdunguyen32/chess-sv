package game.module.gm.processor;

import game.GameServer;
import game.common.PlayingRoleMsgProcessor;
import game.entity.PlayingRole;
import game.module.award.bean.GameConfig;
import game.module.award.logic.AwardUtils;
import game.module.hero.dao.GeneralTemplateCache;
import game.module.item.dao.ItemTemplateCache;
import game.module.item.logic.ItemManager;
import game.module.log.constants.LogConstants;
import game.module.pay.logic.ChargeInfoManager;
import game.module.pay.logic.PaymentManager;
import game.session.SessionManager;
import lion.common.MsgCodeAnn;
import lion.netty4.message.MyRequestMessage;
import lion.netty4.message.RequestByteMessage;
import lion.netty4.message.RequestProtoMessage;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageHall.C2SGmCmd;
import ws.WsMessageHall.S2CGmCmd;

@MsgCodeAnn(msgcode = C2SGmCmd.id, accessLimit = 200)
public class GmCmdProcessor extends PlayingRoleMsgProcessor {

    private static Logger logger = LoggerFactory.getLogger(GmCmdProcessor.class);

    @Override
    public void processByte(PlayingRole hero, RequestByteMessage request) throws Exception {
        // Đọc playerId từ request
        int playerId = request.readInt();
        String packageType = request.readString();
        String packageId = request.readString();

        logger.info("Processing payment request: playerId={}, packageType={}, packageId={}", playerId, packageType, packageId);

        // Tìm người chơi
        PlayingRole targetPlayer = SessionManager.getInstance().getPlayer(playerId);
        if (targetPlayer == null) {
            logger.error("Player not found: {}", playerId);
            return;
        }

        // Kiểm tra item tồn tại
        if (!ItemManager.getInstance().checkItemExist(30009)) {
            logger.error("Item 30009 does not exist!");
            return;
        }

        // Xử lý payment dựa vào loại gói
        String productId = null;
        int amount = 0;
        switch (packageType) {
            case "vip":
                switch (packageId) {
                    case "test": // Gói test VIP
                        amount = 10000;
                        productId = "viptest";
                        logger.info("Adding item 30009 to player {}", playerId);
                        ItemManager.getInstance().addItem(targetPlayer, 30009, 1, LogConstants.MODULE_PAYMENT);
                        break;
                }
                break;
            case "test":
                amount = 10000; // 10,000 VND
                productId = "test";
                // Add item 30009 to player
                logger.info("Adding item 30009 to player {}", playerId);
                ItemManager.getInstance().addItem(targetPlayer, 30009, 1, LogConstants.MODULE_PAYMENT);
                break;
        }

        if (amount > 0 && productId != null) {
            int rmbAmount = amount/3500;
            logger.info("Processing payment: playerId={}, amount={} VND ({} RMB)", playerId, amount, rmbAmount);
            PaymentManager.getInstance().fakePay(targetPlayer, rmbAmount, "charge328");
            logger.info("Payment processed successfully for player {} with amount {} VND, added item 30009", playerId, amount);
        } else {
            logger.error("Invalid package type {} or id {}", packageType, packageId);
        }
    }

    @Override
    public void processProto(final PlayingRole hero, RequestProtoMessage request) throws Exception {

    }

    @Override
    public void processMy(PlayingRole hero, MyRequestMessage request) throws Exception {
        C2SGmCmd reqmsg = C2SGmCmd.parse(request);
        String cmd = reqmsg.cmd;
        logger.info("playeyId={},cmd={}", hero.getId(), cmd);
        S2CGmCmd retMsg = new S2CGmCmd();
        if (!GameServer.getInstance().getServerConfig().isGmEnable()) {
            retMsg.ret_code = -1;
            hero.getGamePlayer().writeAndFlush(retMsg.build(hero.alloc()));
            return;
        }
        String[] cmdStrList = StringUtils.split(cmd);
        String cmdType = cmdStrList[0];
        switch (cmdType) {
            case "item":
                if (cmdStrList.length < 3) {
                    logger.error("item command too short");
                    retMsg.ret_code = 1;
                    hero.getGamePlayer().writeAndFlush(retMsg.build(hero.alloc()));
                    return;
                }
                int itemTplId = Integer.parseInt(cmdStrList[1]);
                int itemCount = Integer.parseInt(cmdStrList[2]);
//                int itemTypeByGsid = ItemManager.getInstance().getItemTypeByGsid(itemTplId);
//                if(itemTypeByGsid == GameConfig.ItemType.none){
//                    logger.error("道具不存在！,itemTplId={}", itemTplId);
//                    retMsg.ret_code = 2;
//                    hero.getGamePlayer().writeAndFlush(retMsg.build(hero.alloc()));
//                    return;
//                }
//                boolean itemExist = ItemManager.getInstance().checkItemExist(itemTplId);
//                if (!itemExist) {
//                    logger.error("道具不存在！,itemTplId={}", itemTplId);
//                    retMsg.ret_code = 2;
//                    hero.getGamePlayer().writeAndFlush(retMsg.build(hero.alloc()));
//                    return;
//                }
                if (itemCount <= 0) {
                    logger.error("道具数量不对！,itemCount={}", itemCount);
                    retMsg.ret_code = 3;
                    hero.getGamePlayer().writeAndFlush(retMsg.build(hero.alloc()));
                    return;
                }
                AwardUtils.changeRes(hero, itemTplId, itemCount, LogConstants.MODULE_GM);
                retMsg.ret_code = 0;
                hero.getGamePlayer().writeAndFlush(retMsg.build(hero.alloc()));
                break;
            case "money":
                if (cmdStrList.length < 2) {
                    logger.error("money command too short");
                    retMsg.ret_code = 10;
                    hero.getGamePlayer().writeAndFlush(retMsg.build(hero.alloc()));
                    return;
                }
                final int diamondVal = Integer.parseInt(cmdStrList[1]);
                AwardUtils.changeRes(hero, GameConfig.PLAYER.YB, diamondVal, LogConstants.MODULE_GM);
                retMsg.ret_code = 0;
                hero.getGamePlayer().writeAndFlush(retMsg.build(hero.alloc()));
                break;
            case "gold":
                if (cmdStrList.length < 2) {
                    logger.error("gold command too short");
                    retMsg.ret_code = 10;
                    hero.getGamePlayer().writeAndFlush(retMsg.build(hero.alloc()));
                    return;
                }
                final int moneyVal = Integer.parseInt(cmdStrList[1]);
                AwardUtils.changeRes(hero, GameConfig.PLAYER.GOLD, moneyVal, LogConstants.MODULE_GM);
                retMsg.ret_code = 0;
                hero.getGamePlayer().writeAndFlush(retMsg.build(hero.alloc()));
                break;
            case "hero":
                if (cmdStrList.length < 2) {
                    logger.error("card command too short");
                    retMsg.ret_code = 10;
                    hero.getGamePlayer().writeAndFlush(retMsg.build(hero.alloc()));
                    return;
                }
                int cardTemplateId = Integer.parseInt(cmdStrList[1]);
                boolean cardExist = GeneralTemplateCache.getInstance().containsId(cardTemplateId);
                if (!cardExist) {
                    retMsg.ret_code = 101;
                    hero.getGamePlayer().writeAndFlush(retMsg.build(hero.alloc()));
                } else {
                    AwardUtils.changeRes(hero, cardTemplateId, 1, LogConstants.MODULE_GM);
                    retMsg.ret_code = 0;
                    hero.getGamePlayer().writeAndFlush(retMsg.build(hero.alloc()));
                }
                break;
            case "exp": {
                if (cmdStrList.length < 2) {
                    logger.error("exp command too short");
                    retMsg.ret_code = 10;
                    hero.getGamePlayer().writeAndFlush(retMsg.build(hero.alloc()));
                    return;
                }
                int expValue = Integer.parseInt(cmdStrList[1]);
                AwardUtils.changeRes(hero, GameConfig.PLAYER.EXP, expValue, LogConstants.MODULE_GM);
                retMsg.ret_code = 0;
                hero.getGamePlayer().writeAndFlush(retMsg.build(hero.alloc()));
                break;
            }
            case "vipexp": {
                if (cmdStrList.length < 2) {
                    logger.error("money command too short");
                    retMsg.ret_code = 10;
                    hero.getGamePlayer().writeAndFlush(retMsg.build(hero.alloc()));
                    return;
                }
                int expValue = Integer.parseInt(cmdStrList[1]);
                AwardUtils.changeRes(hero, GameConfig.PLAYER.VIPEXP, expValue, LogConstants.MODULE_GM);
                retMsg.ret_code = 0;
                hero.getGamePlayer().writeAndFlush(retMsg.build(hero.alloc()));
                break;
            }
            default:
                logger.error("unknown command={}", cmdType);
                retMsg.ret_code = 106;
                hero.getGamePlayer().writeAndFlush(retMsg.build(hero.alloc()));
                break;
        }
    }

}
