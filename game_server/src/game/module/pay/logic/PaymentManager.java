/// file paymentmanager.java => các gói nạp đều trong này 

package game.module.pay.logic;

import game.GameServer;
import game.common.DateCommonUtils;
import game.entity.PlayingRole;
import game.module.activity.bean.ActivityBean;
import game.module.activity.bean.ActivityPlayer;
import game.module.activity.constants.ActivityConstants;
import game.module.activity.dao.*;
import game.module.activity.logic.ActivityManager;
import game.module.activity.logic.ActivityRecordManager;
import game.module.activity.logic.ActivityWeekManager;
import game.module.activity.logic.ActivityXiangouManager;
import game.module.activity.processor.ActivityListProcessor;
import game.module.award.bean.GameConfig;
import game.module.award.logic.AwardUtils;
import game.module.log.constants.LogConstants;
import game.module.mail.logic.MailManager;
import game.module.occtask.bean.OccTask;
import game.module.occtask.dao.OccTaskCache;
import game.module.occtask.dao.OccTaskDaoHelper;
import game.module.occtask.logic.MyOccTaskTemplateCache;
import game.module.pay.bean.ChargeEntity;
import game.module.pay.dao.*;
import game.module.season.dao.SeasonCache;
import game.module.template.*;
import game.module.user.bean.PlayerBean;
import game.module.user.dao.PlayerDao;
import game.module.user.logic.PlayerInfoManager;
import game.module.vip.logic.VipManager;
import game.session.PlayerOnlineCacheMng;
import game.session.SessionManager;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageHall.PushFirstPay;
import ws.WsMessageHall.PushPaymentResult;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;

public class PaymentManager {

    private static final Logger logger = LoggerFactory.getLogger(PaymentManager.class);

    static class SingletonHolder {
        static PaymentManager instance = new PaymentManager();
    }

    public static PaymentManager getInstance() {
        return SingletonHolder.instance;
    }

    private Set<Integer> payPlayers = Collections.newSetFromMap(new ConcurrentHashMap<Integer, Boolean>());

    private Set<String> existOrderIdAll = Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>());

    public static void firstRechargeAward(PlayingRole playingRole) {
        // PlayerExtraBean playerExtraBean =
        // PlayerExtraCache.getInstance().getPlayerExtraBean(playingRole.getId());
        // if (playerExtraBean != null && playerExtraBean.getIsRecharge() != null &&
        // playerExtraBean.getIsRecharge() > 0) {
        // return;
        // }
        // if (playerExtraBean == null) {
        // playerExtraBean = new PlayerExtraBean();
        // playerExtraBean.setPlayerId(playingRole.getId());
        // playerExtraBean.setIsRecharge(1);
        // PlayerLogic.getInstance().asyncAddPlayerExtraRecharge(playerExtraBean);
        // PlayerExtraCache.getInstance().addNewEntity(playerExtraBean);
        // } else if (playerExtraBean.getIsRecharge() == null ||
        // playerExtraBean.getIsRecharge() == 0) {
        // playerExtraBean.setIsRecharge(1);
        // PlayerLogic.getInstance().asyncUpdatePlayerExtraRecharge(playerExtraBean);
        // }
        // 添加活动
        if (ActivityManager.getInstance().is3DayFirstRecharge()) {
            ActivityManager.getInstance().add3DayFirstRecharge(playingRole);
        } else {
            // 发奖励
            // PushFirstRechargeAward retBuilder = new PushFirstRechargeAward();
            //
            // ChargeTemplateCache.getInstance().get_first_charge_Template();
            //
            // String itemAward = OtherConfigCache.getInstance().getOtherConfig(1709);
            // String[] itemPairList = StringUtils.split(itemAward,
            // StringConstants.SEPARATOR_SHU);
            // for (String oneItemStr : itemPairList) {
            // String[] oneItems = StringUtils.split(oneItemStr,
            // StringConstants.SEPARATOR_DI);
            // int itemId = Integer.parseInt(oneItems[0]);
            // int itemCount = Integer.parseInt(oneItems[1]);
            // AwardUtils.changeCoinOrItem(playingRole, itemId, itemCount,
            // PaymentConstants.PAYMENT_RECHARGE,
            // LogConstants.MODULE_PAYMENT, LogConstants.YES);
            // retBuilder.addItemListBuilder().setTemplateId(itemId).setCount(itemCount).setId(0);
            // }
            // playingRole.getGamePlayer().writeAndFlush(10808, retBuilder.build());
        }
    }

    /**
     * @param channel
     * @param userId
     * @param orderid
     * @param money    充值金额，单位为人民币 元
     * @param time
     * @param serverid
     */
    @SuppressWarnings("null")
    public void payCallback(final Channel channel, final String userId, final String orderid, final int money, int time,
            final String pid, final int serverid) {
        logger.info("lan payment callback,userId={},orderid={},money={},time={},productId={}", userId, orderid, money,
                time, pid);
        int addDiamond = money * PaymentConstants.RMB_2_YUANBAO;
        String productId = pid;
        // 是否为小7充值
        if (productId.equals("x7sy")) {
            int productIdX7sy = PaymentLogDao.getInstance().getToPayProductId(orderid);
            if (productIdX7sy <= 0) {
                logger.info("xiao7 order not exist,userId={},orderid={}", userId, orderid);
                throw new RuntimeException("xiao7 order not exist");
            }
        } else {
            boolean orderExist = PaymentLogDao.getInstance().checkOrderExist(orderid);
            if (orderExist) {
                logger.info("order exist,userId={},orderid={}", userId, orderid);
                throw new RuntimeException("order exist");
            }
        }
        PlayerBean pb = PlayerOnlineCacheMng.getInstance().getCache(userId, serverid);
        // \
        int playerId = 0;
        if (pb != null) {
            playerId = pb.getId();
        } else {
            List<PlayerBean> playerAll = PlayerDao.getInstance().getPlayerByOpenId(userId, serverid);
            if (playerAll == null || playerAll.size() == 0) {
                throw new RuntimeException("player not exist");
            }
            pb = playerAll.get(0);
            playerId = playerAll.get(0).getId();
        }
        // 是否为青春基金
        boolean isQingChunJiJIn = false;
        // 月卡处理
        // 玩家是否在线
        PlayingRole pr = SessionManager.getInstance().getPlayer(playerId);
        // 是否进行首冲

        boolean isFirstRecharge = false;
        if (pr != null) {
            ChargeEntity chargeEntity = ChargeCache.getInstance().getChargeEntity(playerId);
            if (chargeEntity == null || chargeEntity.getFirstPayTime() == null) {
                isFirstRecharge = true;
            }
        } else {
            isFirstRecharge = ChargeDao.getInstance().isFirstPay(playerId);
        }
        // Check whether the recharge product id exists and verify the recharge amount.
        RechargeTemplate rechargeTemplate = ChargeTemplateCache.getInstance().getRechargeTemplate(productId);
        logger.info("rechargeTemplate={}", rechargeTemplate);
        if (productId.startsWith(PayConstants.PRODUCT_ID_THLB2)) {// TODO
            List<ZhdCzlbTemplate> gzCzlbTemplates = ActivityWeekTemplateCache.getInstance().getGzCzlbTemplates();
            int libaoIndex = Integer.parseInt(productId.substring(5));
            ZhdCzlbTemplate zhdCzlbTemplate = gzCzlbTemplates.get(libaoIndex);
            if (!zhdCzlbTemplate.getPrice().equals(money)) {
                logger.warn("ZhdCzlbTemplate product id not exist,userId={},productId={},money={}", userId, productId,
                        money);
                productId = "money";
            }
        } else if (productId.equals("specialgz")) {// Thẻ hàng tháng cao quý
            logger.info("specialgz productId={}", productId);
            if (pr != null) {
                ChargeInfoManager.getInstance().addGzYueka(pr);
                // 发元宝
                MCardTemplate gzMCardTemplate = MCardTemplateCache.getInstance().getGzMCardTemplate();
                logger.info("gzMCardTemplate={}", gzMCardTemplate.toString());
                int addYuanbao = gzMCardTemplate.getARRACT().get(0).get("ACTCOUNT");
                AwardUtils.changeRes(pr, GameConfig.PLAYER.YB, addYuanbao, LogConstants.MODULE_RECHARGE);
                AwardUtils.changeRes(pr, 90271, 1, LogConstants.MODULE_RECHARGE);
                PushPaymentResult respMsg = new PushPaymentResult();
                respMsg.yb = addYuanbao;
                respMsg.pid = productId;
                pr.writeAndFlush(respMsg.build(pr.alloc()));
            } else {
                ChargeInfoManager.getInstance().addGzYuekaOffline(playerId);
            }
        } else if (productId.equals("specialzz")) {// Thẻ hàng tháng tối cao
            if (pr != null) {
                ChargeInfoManager.getInstance().addZzYueka(pr);
                MCardTemplate zzMCardTemplate = MCardTemplateCache.getInstance().getZzMCardTemplate();
                int addYuanbao = zzMCardTemplate.getARRACT().get(0).get("ACTCOUNT");
                AwardUtils.changeRes(pr, GameConfig.PLAYER.YB, addYuanbao, LogConstants.MODULE_RECHARGE);
                AwardUtils.changeRes(pr, 90272, 1, LogConstants.MODULE_RECHARGE);
                PushPaymentResult respMsg = new PushPaymentResult();
                respMsg.yb = addYuanbao;
                respMsg.pid = productId;
                pr.writeAndFlush(respMsg.build(pr.alloc()));
            } else {
                ChargeInfoManager.getInstance().addZzYuekaOffline(playerId);
            }
        } else if (productId.equals("specialcz")) {// quỹ tăng trưởng
            logger.info("specialcz productId={}", productId);
            if (pr != null) {
                logger.info("specialcz pr={}", pr.toString());
                ChargeInfoManager.getInstance().addCz(pr);
                PushPaymentResult respMsg = new PushPaymentResult();
                respMsg.yb = 0;
                respMsg.pid = productId;
                pr.writeAndFlush(respMsg.build(pr.alloc()));
            } else {
                ActivityManager.getInstance().chengZhangJiJinOffline(playerId);
            }
            // 发奖励
            int rewardYbSum = 0;
            Integer playerLevel = pb.getLevel();
            List<Integer> rewardLevels = new ArrayList<>();
            List<MActivityGpTemplate> templateMap = MActivityGpTemplateCache.getInstance().getTemplateMap();
            for (MActivityGpTemplate mActivityGpTemplate : templateMap) {
                if (playerLevel >= mActivityGpTemplate.getLEVEL()) {
                    rewardYbSum += mActivityGpTemplate.getYBNUM();
                    rewardLevels.add(mActivityGpTemplate.getLEVEL());
                }
            }
            if (rewardYbSum > 0) {
                Map<Integer, Integer> mailAtt = new HashMap<>();
                mailAtt.put(GameConfig.PLAYER.YB, rewardYbSum);
                String mailTitle = "Phần thưởng mua Quỹ Trưởng Thành";
                // %1$s level player active
                String mailContent = String.format("Bạn đã hoàn thành cấp độ: %1$s, phần thưởng như sau:",
                        rewardLevels.toString());
                MailManager.getInstance().sendSysMailToSingle(playerId, mailTitle, mailContent, mailAtt);
            }
        } else if (productId.equals("specialbw")) {// Đặc quyền của chúa tể
            if (pr != null) {
                ChargeInfoManager.getInstance().addBw(pr);
                
                // Lấy template phần thưởng
                RightTemplate rightTemplate = RightTemplateCache.getInstance().getRightTemplate(productId);
                if (rightTemplate != null && rightTemplate.getREWARD() != null) {
                    // Gửi phần thưởng trực tiếp
                    for (RewardTemplateSimple rewardTemplateSimple : rightTemplate.getREWARD()) {
                        AwardUtils.changeRes(pr, rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT(),
                                LogConstants.MODULE_RECHARGE);
                    }
                    
                    // Gửi thêm mail xác nhận
                    Map<Integer, Integer> mailAtt = new HashMap<>();
                    for (RewardTemplateSimple rewardTemplateSimple : rightTemplate.getREWARD()) {
                        mailAtt.put(rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT());
                    }
                    String mailTitle = "Phần thưởng mua Đặc quyền Bá Vương";
                    String mailContent = "Bạn đã nhận được các phần thưởng sau:";
                    MailManager.getInstance().sendSysMailToSingle(playerId, mailTitle, mailContent, mailAtt);
                }

                // Gửi thông báo kết quả nạp
                PushPaymentResult respMsg = new PushPaymentResult();
                respMsg.yb = 0;
                respMsg.pid = productId;
                pr.writeAndFlush(respMsg.build(pr.alloc()));
                
                // Flush để đảm bảo update ngay
                pr.flush();
                
                // Log để debug
                logger.info("Processed specialbw payment for player {}, rewards sent via mail", playerId);
                
            } else {
                // Xử lý offline
                ChargeInfoManager.getInstance().addBwOffline(playerId);
                
                // Gửi phần thưởng qua mail
                RightTemplate rightTemplate = RightTemplateCache.getInstance().getRightTemplate(productId);
                if (rightTemplate != null && rightTemplate.getREWARD() != null) {
                    Map<Integer, Integer> mailAtt = new HashMap<>();
                    for (RewardTemplateSimple rewardTemplateSimple : rightTemplate.getREWARD()) {
                        mailAtt.put(rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT());
                    }
                    String mailTitle = "Phần thưởng mua Đặc quyền Bá Vương";
                    String mailContent = "Phần thưởng như sau:";
                    MailManager.getInstance().sendSysMailToSingle(playerId, mailTitle, mailContent, mailAtt);
                    
                    // Log để debug
                    logger.info("Processed offline specialbw payment for player {}, rewards sent via mail", playerId);
                }
            }
        } else if (productId.equals("specialqz")) {// Đặc ân là siêng năng và quan tâm đến mọi người
            if (pr != null) {
                ChargeInfoManager.getInstance().addQz(pr);
                
                // Phần thưởng trực tiếp khi online
                RightTemplate rightTemplate = RightTemplateCache.getInstance().getRightTemplate("specialqz");
                if (rightTemplate != null && rightTemplate.getREWARD() != null) {
                    // Gửi phần thưởng trực tiếp
                    for (RewardTemplateSimple rewardTemplateSimple : rightTemplate.getREWARD()) {
                        AwardUtils.changeRes(pr, rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT(),
                                LogConstants.MODULE_RECHARGE);
                    }
                    
                    // Gửi thêm mail xác nhận
                    Map<Integer, Integer> mailAtt = new HashMap<>();
                    for (RewardTemplateSimple rewardTemplateSimple : rightTemplate.getREWARD()) {
                        mailAtt.put(rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT());
                    }
                    String mailTitle = "Phần thưởng mua Đặc quyền Cần Chính Ái Dân";
                    String mailContent = "Bạn đã nhận được các phần thưởng sau:";
                    MailManager.getInstance().sendSysMailToSingle(playerId, mailTitle, mailContent, mailAtt);
                }

                // Gửi thông báo kết quả nạp
                PushPaymentResult respMsg = new PushPaymentResult();
                respMsg.yb = 0; 
                respMsg.pid = "specialqz";
                pr.writeAndFlush(respMsg.build(pr.alloc()));
                
                // Flush để đảm bảo update ngay
                pr.flush();
                
            } else {
                // Xử lý offline
                ChargeInfoManager.getInstance().addQzOffline(playerId);
                
                // Gửi phần thưởng qua mail
                RightTemplate rightTemplate = RightTemplateCache.getInstance().getRightTemplate("specialqz");
                if (rightTemplate != null && rightTemplate.getREWARD() != null) {
                    Map<Integer, Integer> mailAtt = new HashMap<>();
                    for (RewardTemplateSimple rewardTemplateSimple : rightTemplate.getREWARD()) {
                        mailAtt.put(rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT());
                    }
                    String mailTitle = "Phần thưởng mua Đặc quyền Cần Chính Ái Dân";
                    String mailContent = "Phần thưởng như sau:";
                    MailManager.getInstance().sendSysMailToSingle(playerId, mailTitle, mailContent, mailAtt);
                }
            }
        } else if (productId.equals("specialyd")) {// đặc quyền hàng tháng
            // Kiểm tra xem đã mua gói này chưa
            ChargeEntity chargeEntity = ChargeCache.getInstance().getChargeEntity(playerId);
            
            // Log để debug
            logger.info("Check specialyd for player {}: chargeEntity={}, ydEndTime={}", 
                playerId, 
                chargeEntity,
                chargeEntity != null ? chargeEntity.getYdEndTime() : null);

            if (chargeEntity != null && chargeEntity.getYdEndTime() != null && 
                chargeEntity.getYdEndTime().after(new Date())) {
                
                // Log warning
                logger.warn("Player {} already has active specialyd package until {}", 
                    playerId, chargeEntity.getYdEndTime());
                
                // Gửi thông báo cho client qua mail thay vì message
                if (pr != null) {
                    String mailTitle = "Thông báo Đặc quyền Hàng Tháng";
                    String mailContent = "Bạn đã có gói Đặc quyền Hàng Tháng đang hoạt động đến " + 
                        new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(chargeEntity.getYdEndTime());
                    
                    // Gửi mail thông báo
                    Map<Integer, Integer> mailAtt = new HashMap<>();
                    MailManager.getInstance().sendSysMailToSingle(playerId, mailTitle, mailContent, mailAtt);
                    
                    // Gửi response về client
                    PushPaymentResult respMsg = new PushPaymentResult();
                    respMsg.yb = 0;
                    respMsg.pid = productId;
                    pr.writeAndFlush(respMsg.build(pr.alloc()));
                }
                return;
            }

            // Tạo format cho date
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

            if (pr != null) {
                // Lưu thời gian kết thúc (30 ngày)
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DATE, 30);
                Date endTime = calendar.getTime();
                
                ChargeInfoManager.getInstance().addYd(pr);
                
                // Cập nhật thời gian vào DB
                if (chargeEntity == null) {
                    chargeEntity = new ChargeEntity();
                    chargeEntity.setPlayerId(playerId);
                    chargeEntity.setYdEndTime(endTime);
                    ChargeDao.getInstance().addChargeEntity(chargeEntity);
                    ChargeCache.getInstance().addNewEntity(chargeEntity);
                } else {
                    chargeEntity.setYdEndTime(endTime);
                    ChargeDao.getInstance().updateCharge(chargeEntity);
                }

                // Gửi phần thưởng
                RightTemplate rightTemplate = RightTemplateCache.getInstance().getRightTemplate(productId);
                if (rightTemplate != null && rightTemplate.getREWARD() != null) {
                    // Gửi phần thưởng trực tiếp
                    for (RewardTemplateSimple rewardTemplateSimple : rightTemplate.getREWARD()) {
                        AwardUtils.changeRes(pr, rewardTemplateSimple.getGSID(), 
                            rewardTemplateSimple.getCOUNT(), LogConstants.MODULE_RECHARGE);
                    }

                    // Gửi mail xác nhận
                    Map<Integer, Integer> mailAtt = new HashMap<>();
                    for (RewardTemplateSimple rewardTemplateSimple : rightTemplate.getREWARD()) {
                        mailAtt.put(rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT());
                    }
                    String mailTitle = "Phần thưởng mua Đặc quyền Hàng Tháng";
                    String mailContent = "Bạn đã kích hoạt Đặc quyền Hàng Tháng đến " + 
                        dateFormat.format(endTime);
                    MailManager.getInstance().sendSysMailToSingle(playerId, mailTitle, mailContent, mailAtt);
                }

                PushPaymentResult respMsg = new PushPaymentResult();
                respMsg.yb = 0;
                respMsg.pid = productId;
                pr.writeAndFlush(respMsg.build(pr.alloc()));
                pr.flush();

                logger.info("Activated specialyd for player {} until {}", playerId, endTime);

            } else {
                // Xử lý offline
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DATE, 30);
                Date endTime = calendar.getTime();

                ChargeInfoManager.getInstance().addYdOffline(playerId);

                // Cập nhật thời gian vào DB
                if (chargeEntity == null) {
                    chargeEntity = new ChargeEntity();
                    chargeEntity.setPlayerId(playerId);
                    chargeEntity.setYdEndTime(endTime); 
                    ChargeDao.getInstance().addChargeEntity(chargeEntity);
                } else {
                    chargeEntity.setYdEndTime(endTime);
                    ChargeDao.getInstance().updateCharge(chargeEntity);
                }

                // Gửi phần thưởng qua mail
                RightTemplate rightTemplate = RightTemplateCache.getInstance().getRightTemplate(productId);
                if (rightTemplate != null && rightTemplate.getREWARD() != null) {
                    Map<Integer, Integer> mailAtt = new HashMap<>();
                    for (RewardTemplateSimple rewardTemplateSimple : rightTemplate.getREWARD()) {
                        mailAtt.put(rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT());
                    }
                    String mailTitle = "Phần thưởng mua Đặc quyền Hàng Tháng";
                    String mailContent = "Bạn đã kích hoạt Đặc quyền Hàng Tháng đến " + 
                        dateFormat.format(endTime);
                    MailManager.getInstance().sendSysMailToSingle(playerId, mailTitle, mailContent, mailAtt);
                }

                logger.info("Activated offline specialyd for player {} until {}", playerId, endTime);
            }
        } else if (productId.startsWith("herolibao")) {// Gói quà anh hùng
            if (pr != null) {
                ActivityManager.getInstance().heroLibao(pr, Integer.parseInt(productId.substring(9)), addDiamond);
                PushPaymentResult respMsg = new PushPaymentResult();
                respMsg.yb = 0;
                respMsg.pid = productId;
                pr.writeAndFlush(respMsg.build(pr.alloc()));
            } else {
                ActivityManager.getInstance().heroLibaoOffline(playerId, Integer.parseInt(productId.substring(9)),
                        userId, serverid);
            }
        } else if (productId.startsWith("qcjj")) {// Quỹ thanh niên
            if (pr != null) {
                ActivityRecordManager.getInstance().qingChunJiJin(pr, Integer.parseInt(productId.substring(4)),
                        addDiamond);
                PushPaymentResult respMsg = new PushPaymentResult();
                respMsg.yb = 0;
                respMsg.pid = productId;
                pr.writeAndFlush(respMsg.build(pr.alloc()));
            } else {
                ActivityRecordManager.getInstance().qingChunJiJinOffline(playerId,
                        Integer.parseInt(productId.substring(4)), userId,
                        serverid);
            }
            isQingChunJiJIn = true;
        } else if (productId.startsWith("mzlb")) {// Gói quà hàng tuần
            // Chuẩn hóa productId - bỏ dấu _ nếu có
            String normalizedId = productId.replace("_", "");
            int libaoIndex;
            try {
                libaoIndex = Integer.parseInt(normalizedId.substring(4));
            } catch (NumberFormatException e) {
                logger.error("Invalid mzlb index format in productId: {}", productId);
                return;
            }
            
            List<MzlbPayTemplate> mzlbTemplates = MzlbMylbTemplateCache.getInstance().getMzlbTemplates();
            
            // Log để debug
            logger.info("Processing mzlb payment: productId={}, normalizedId={}, index={}, templates.size={}", 
                productId, normalizedId, libaoIndex, mzlbTemplates.size());
            
            // Kiểm tra index hợp lệ
            if (libaoIndex < 0 || libaoIndex >= mzlbTemplates.size()) {
                logger.error("Invalid mzlb index: {}", libaoIndex);
                return;
            }

            MzlbPayTemplate mzlbPayTemplate = mzlbTemplates.get(libaoIndex);
            
            // Kiểm tra giá tiền
            if (mzlbPayTemplate.getPrice() != money) {
                logger.error("Price mismatch for mzlb package. Expected: {}, Got: {}", 
                    mzlbPayTemplate.getPrice(), money);
                return;
            }
            
            // Kiểm tra số lần mua
            int buyCount = LibaoBuyManager.getInstance().getLibaoBuy(playerId, productId);
            logger.info("Current buy count for player {} package {}: {}/{}", 
                playerId, productId, buyCount, mzlbPayTemplate.getBuytime());

            if (buyCount >= mzlbPayTemplate.getBuytime()) {
                logger.warn("Player {} has reached max buy times {} for package {}", 
                    playerId, mzlbPayTemplate.getBuytime(), productId);
                
                // Gửi mail thông báo
                if (pr != null) {
                    String mailTitle = "Thông báo Gói quà hàng tuần";
                    String mailContent = "Bạn đã mua tối đa số lần cho gói này trong tuần.";
                    Map<Integer, Integer> mailAtt = new HashMap<>();
                    MailManager.getInstance().sendSysMailToSingle(playerId, mailTitle, mailContent, mailAtt);
                }
                return;
            }

            if (pr != null) {
                // Xử lý online
                LibaoBuyManager.getInstance().addLibaoBuy(playerId, productId);
                
                // Log số lần mua sau khi update
                int newBuyCount = LibaoBuyManager.getInstance().getLibaoBuy(playerId, productId);
                logger.info("New buy count after purchase for player {} package {}: {}/{}", 
                    playerId, productId, newBuyCount, mzlbPayTemplate.getBuytime());
                
                // Gửi phần thưởng trực tiếp
                for (RewardTemplateSimple rewardTemplateSimple : mzlbPayTemplate.getItems()) {
                    AwardUtils.changeRes(pr, rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT(),
                            LogConstants.MODULE_PAYMENT);
                }

                // Gửi mail xác nhận
                Map<Integer, Integer> mailAtt = new HashMap<>();
                for (RewardTemplateSimple rewardTemplateSimple : mzlbPayTemplate.getItems()) {
                    mailAtt.put(rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT());
                }
                String mailTitle = "Phần thưởng mua Gói quà hàng tuần";
                String mailContent = String.format("Phần thưởng lần mua thứ %d/%d:", 
                    buyCount + 1, mzlbPayTemplate.getBuytime());
                MailManager.getInstance().sendSysMailToSingle(playerId, mailTitle, mailContent, mailAtt);

                // Gửi response
                PushPaymentResult respMsg = new PushPaymentResult();
                respMsg.yb = 0;
                respMsg.pid = productId;
                respMsg.vipExp = mzlbPayTemplate.getPrice() * 10; // VipExp = price * 10 
                pr.writeAndFlush(respMsg.build(pr.alloc()));
                pr.flush();

                // Gửi lệnh refresh UI
                try {
                    // Update LibaoBuy in manager and cache
                    LibaoBuyManager.getInstance().addLibaoBuy(playerId, productId);
                    
                    // Refresh activity list UI
                    new ActivityListProcessor().processMy(pr, null);
                    
                } catch (Exception e) {
                    logger.error("Error refreshing activity info for player {}: {}", playerId, e.getMessage());
                }

                logger.info("Processed mzlb payment for player {}, index {}, buy count {}/{}", 
                    playerId, libaoIndex, buyCount + 1, mzlbPayTemplate.getBuytime());

            } else {
                // Xử lý offline
                LibaoBuyManager.getInstance().addLibaoBuyOffline(playerId, productId);
                
                // Gửi phần thưởng qua mail
                Map<Integer, Integer> mailAtt = new HashMap<>();
                for (RewardTemplateSimple rewardTemplateSimple : mzlbPayTemplate.getItems()) {
                    mailAtt.put(rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT());
                }
                String mailTitle = "Phần thưởng mua Gói quà hàng tuần";
                String mailContent = String.format("Phần thưởng lần mua thứ %d/%d:", 
                    buyCount + 1, mzlbPayTemplate.getBuytime());
                MailManager.getInstance().sendSysMailToSingle(playerId, mailTitle, mailContent, mailAtt);

                logger.info("Processed offline mzlb payment for player {}, index {}, buy count {}/{}", 
                    playerId, libaoIndex, buyCount + 1, mzlbPayTemplate.getBuytime());
            }
        } else if (productId.startsWith("mylb")) {// gói quà hàng tháng
            List<MzlbPayTemplate> mylbTemplates = MzlbMylbTemplateCache.getInstance().getMylbTemplates();
            int libaoIndex = Integer.parseInt(productId.substring(4));
            MzlbPayTemplate mylbPayTemplate = mylbTemplates.get(libaoIndex);
            
            // Luôn gửi phần thưởng qua mail
            Map<Integer, Integer> mailAtt = new HashMap<>();
            for (RewardTemplateSimple rewardTemplateSimple : mylbPayTemplate.getItems()) {
                mailAtt.put(rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT());
            }
            String mailTitle = "Phần thưởng mua Gói quà hàng tháng";
            String mailContent = "Phần thưởng như sau:";
            MailManager.getInstance().sendSysMailToSingle(playerId, mailTitle, mailContent, mailAtt);

            // Cập nhật số lần mua
            if (pr != null) {
                LibaoBuyManager.getInstance().addLibaoBuy(playerId, productId);
                PushPaymentResult respMsg = new PushPaymentResult();
                respMsg.yb = 0;
                respMsg.pid = productId;
                pr.writeAndFlush(respMsg.build(pr.alloc()));
            } else {
                LibaoBuyManager.getInstance().addLibaoBuyOffline(playerId, productId);
            }
        } else if (productId.startsWith(PayConstants.PRODUCT_ID_CJXG2)) {// Hoạt động tuần của cjxg2
            Integer seasonId = SeasonCache.getInstance().getBattleSeason().getSeason();
            List<ZhdCjxg2Template> cjxg2Templates = ActivityWeekTemplateCache.getInstance().getCjxg2Templates()
                    .get(seasonId - 1);
            int libaoIndex = Integer.parseInt(productId.substring(6));
            
            // Kiểm tra index hợp lệ
            if (libaoIndex < 0 || libaoIndex >= cjxg2Templates.size()) {
                logger.error("Invalid cjxg2 index: {}", libaoIndex);
                return;
            }

            ZhdCjxg2Template zhdCjxg2Template = cjxg2Templates.get(libaoIndex);
            
            if (pr != null) {
                logger.debug(productId,"");
                // Xử lý online
                LibaoBuyManager.getInstance().addLibaoBuy(playerId, productId);
                
                // Gửi phần thưởng trực tiếp
                for (RewardTemplateSimple rewardTemplateSimple : zhdCjxg2Template.getItems()) {
                    AwardUtils.changeRes(pr, rewardTemplateSimple.getGSID(), 
                        rewardTemplateSimple.getCOUNT(), LogConstants.MODULE_PAYMENT);
                }
                
                // Gửi phần thưởng đặc biệt nếu có
                if (zhdCjxg2Template.getSpecial() != null) {
                    for (RewardTemplateSimple rewardTemplateSimple : zhdCjxg2Template.getSpecial()) {
                        AwardUtils.changeRes(pr, rewardTemplateSimple.getGSID(), 
                            rewardTemplateSimple.getCOUNT(), LogConstants.MODULE_PAYMENT);
                    }
                }

                // Gửi mail xác nhận
                Map<Integer, Integer> mailAtt = new HashMap<>();
                for (RewardTemplateSimple rewardTemplateSimple : zhdCjxg2Template.getItems()) {
                    mailAtt.put(rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT());
                }
                if (zhdCjxg2Template.getSpecial() != null) {
                    for (RewardTemplateSimple rewardTemplateSimple : zhdCjxg2Template.getSpecial()) {
                        mailAtt.put(rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT());
                    }
                }
                String mailTitle = "Phần thưởng mua Gói quà giá trị đặc biệt";
                String mailContent = "Phần thưởng như sau:";
                MailManager.getInstance().sendSysMailToSingle(playerId, mailTitle, mailContent, mailAtt);

                // Gửi response
                PushPaymentResult respMsg = new PushPaymentResult();
                respMsg.yb = 0;
                respMsg.pid = productId;
                respMsg.vipExp = rechargeTemplate.getVIPEXP();
                pr.writeAndFlush(respMsg.build(pr.alloc()));
                pr.flush();

                logger.info("Processed cjxg2 payment for player {}, index {}", playerId, libaoIndex);

            } else {
                // Xử lý offline
                LibaoBuyManager.getInstance().addLibaoBuyOffline(playerId, productId);
                
                // Gửi phần thưởng qua mail
                Map<Integer, Integer> mailAtt = new HashMap<>();
                for (RewardTemplateSimple rewardTemplateSimple : zhdCjxg2Template.getItems()) {
                    mailAtt.put(rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT());
                }
                if (zhdCjxg2Template.getSpecial() != null) {
                    for (RewardTemplateSimple rewardTemplateSimple : zhdCjxg2Template.getSpecial()) {
                        mailAtt.put(rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT());
                    }
                }
                String mailTitle = "Phần thưởng mua Gói quà giá trị đặc biệt";
                String mailContent = "Phần thưởng như sau:";
                MailManager.getInstance().sendSysMailToSingle(playerId, mailTitle, mailContent, mailAtt);

                logger.info("Processed offline cjxg2 payment for player {}, index {}", playerId, libaoIndex);
            }
        } else if (productId.startsWith(PayConstants.PRODUCT_ID_GXLB)) {// Sự kiện tuần lễ gói quà ngắm sao
            List<ZhdCzlbTemplate> gxlbTemplates = ActivityWeekTemplateCache.getInstance().getGxCzlbTemplates();
            int libaoIndex = Integer.parseInt(productId.substring(5));
            ZhdCzlbTemplate zhdGxlbTemplate = gxlbTemplates.get(libaoIndex);

            LibaoBuyManager.getInstance().addLibaoBuyOffline(playerId, productId);
            // 发奖励
            Map<Integer, Integer> mailAtt = new HashMap<>();
            for (RewardTemplateSimple rewardTemplateSimple : zhdGxlbTemplate.getItems()) {
                mailAtt.put(rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT());
            }
            String mailTitle = "Phần thưởng mua Gói quà giá trị Quan Sao";
            String mailContent = "Phần thưởng như sau:";
            MailManager.getInstance().sendSysMailToSingle(playerId, mailTitle, mailContent, mailAtt);

            if (pr != null) {
                PushPaymentResult respMsg = new PushPaymentResult();
                respMsg.yb = 0;
                respMsg.pid = productId;
                respMsg.vipExp = rechargeTemplate.getVIPEXP();
                pr.writeAndFlush(respMsg.build(pr.alloc()));
            } else {

            }
        } else if (productId.startsWith(PayConstants.PRODUCT_ID_GJLB)) {// Treo gói quà sự kiện tuần
            List<ZhdCzlbTemplate> gjCzlbTemplates = ActivityWeekTemplateCache.getInstance().getGjCzlbTemplates();
            int libaoIndex = Integer.parseInt(productId.substring(5));
            ZhdCzlbTemplate zhdGxlbTemplate = gjCzlbTemplates.get(libaoIndex);

            LibaoBuyManager.getInstance().addLibaoBuyOffline(playerId, productId);
            // 发奖励
            Map<Integer, Integer> mailAtt = new HashMap<>();
            for (RewardTemplateSimple rewardTemplateSimple : zhdGxlbTemplate.getItems()) {
                mailAtt.put(rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT());
            }
            String mailTitle = "Phần thưởng mua Gói quà giá trị Treo Máy";
            String mailContent = "Phần thưởng như sau:";
            MailManager.getInstance().sendSysMailToSingle(playerId, mailTitle, mailContent, mailAtt);

            if (pr != null) {
                PushPaymentResult respMsg = new PushPaymentResult();
                respMsg.yb = 0;
                respMsg.pid = productId;
                respMsg.vipExp = rechargeTemplate.getVIPEXP();
                pr.writeAndFlush(respMsg.build(pr.alloc()));
            } else {

            }
        } else if (productId.startsWith(PayConstants.PRODUCT_ID_GZLB)) {// Sự kiện tuần gói quà công nghệ cao
            List<ZhdCzlbTemplate> gzCzlbTemplates = ActivityWeekTemplateCache.getInstance().getGzCzlbTemplates();
            int libaoIndex = Integer.parseInt(productId.substring(5));
            ZhdCzlbTemplate zhdCzlbTemplate = gzCzlbTemplates.get(libaoIndex);

            LibaoBuyManager.getInstance().addLibaoBuyOffline(playerId, productId);
            // 发奖励
            Map<Integer, Integer> mailAtt = new HashMap<>();
            for (RewardTemplateSimple rewardTemplateSimple : zhdCzlbTemplate.getItems()) {
                mailAtt.put(rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT());
            }
            String mailTitle = "Phần thưởng mua Gói quà giá trị Cao Chiêu";
            String mailContent = "Phần thưởng như sau:";
            MailManager.getInstance().sendSysMailToSingle(playerId, mailTitle, mailContent, mailAtt);

            if (pr != null) {
                PushPaymentResult respMsg = new PushPaymentResult();
                respMsg.yb = 0;
                respMsg.pid = productId;
                respMsg.vipExp = rechargeTemplate.getVIPEXP();
                pr.writeAndFlush(respMsg.build(pr.alloc()));
            } else {

            }
        } else if (productId.startsWith(PayConstants.PRODUCT_ID_CZLB)) {// Sự kiện tuần gói quà siêu giá trị
            List<ZhdCzlbTemplate> czlbTemplates = ActivityWeekTemplateCache.getInstance().getCzlbTemplates();
            int libaoIndex = Integer.parseInt(productId.substring(5));
            ZhdCzlbTemplate zhdCzlbTemplate = czlbTemplates.get(libaoIndex);

            LibaoBuyManager.getInstance().addLibaoBuyOffline(playerId, productId);
            // Trao phần thưởng
            Map<Integer, Integer> mailAtt = new HashMap<>();
            for (RewardTemplateSimple rewardTemplateSimple : zhdCzlbTemplate.getItems()) {
                mailAtt.put(rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT());
            }
            if (zhdCzlbTemplate.getSpecial() != null) {
                for (RewardTemplateSimple rewardTemplateSimple : zhdCzlbTemplate.getSpecial()) {
                    mailAtt.put(rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT());
                }
            }
            String mailTitle = "Phần thưởng mua Gói quà giá trị đặc biệt";
            String mailContent = "Phần thưởng như sau:";
            MailManager.getInstance().sendSysMailToSingle(playerId, mailTitle, mailContent, mailAtt);

            if (pr != null) {
                PushPaymentResult respMsg = new PushPaymentResult();
                respMsg.yb = 0;
                respMsg.pid = productId;
                respMsg.vipExp = rechargeTemplate.getVIPEXP();
                pr.writeAndFlush(respMsg.build(pr.alloc()));
            } else {

            }
        } else if (productId.startsWith(PayConstants.PRODUCT_ID_STARGIFT)) {// Gói quà tặng giới hạn thời gian của Star
            int gstar = Integer.parseInt(productId.substring(8));
            MyXiangouTemplate gstarXiangouTemplate = XiangouTemplateCache.getInstance().getGstarXiangouTemplate(gstar);

            LibaoBuyManager.getInstance().addLibaoBuyOffline(playerId, productId);
            // 发奖励
            Map<Integer, Integer> mailAtt = new HashMap<>();
            for (RewardTemplateSimple rewardTemplateSimple : gstarXiangouTemplate.getItems()) {
                mailAtt.put(rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT());
            }
            String mailTitle = "Phần thưởng mua Gói quà giới hạn theo Sao";
            String mailContent = "Phần thưởng như sau:";
            MailManager.getInstance().sendSysMailToSingle(playerId, mailTitle, mailContent, mailAtt);

            if (pr != null) {
                PushPaymentResult respMsg = new PushPaymentResult();
                respMsg.yb = 0;
                respMsg.pid = productId;
                respMsg.vipExp = rechargeTemplate.getVIPEXP();
                pr.writeAndFlush(respMsg.build(pr.alloc()));
            } else {
               
            }
        } else if (productId.startsWith(PayConstants.PRODUCT_ID_LEVELGIFT)) {// Gói quà tặng giới hạn cấp độ
            int alevel = Integer.parseInt(productId.substring(9));
            MyXiangouTemplate levelXiangouTemplate = XiangouTemplateCache.getInstance().getLevelXiangouTemplate(alevel);

            LibaoBuyManager.getInstance().addLibaoBuyOffline(playerId, productId);
                // 发奖励
                Map<Integer, Integer> mailAtt = new HashMap<>();
                for (RewardTemplateSimple rewardTemplateSimple : levelXiangouTemplate.getItems()) {
                    mailAtt.put(rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT());
                }
                String mailTitle = "Phần thưởng mua Gói quà giới hạn theo Cấp độ";
                String mailContent = "Phần thưởng như sau:";
                MailManager.getInstance().sendSysMailToSingle(playerId, mailTitle, mailContent, mailAtt);

            if (pr != null) {
                PushPaymentResult respMsg = new PushPaymentResult();
                respMsg.yb = 0;
                respMsg.pid = productId;
                respMsg.vipExp = rechargeTemplate.getVIPEXP();
                pr.writeAndFlush(respMsg.build(pr.alloc()));
            } else {
                
            }
        } else if (productId.startsWith(PayConstants.PRODUCT_ID_OCC)) {// Gói quà tặng sự nghiệp
            MyOccTaskTemplate.OccTaskConfig3 config3 = MyOccTaskTemplateCache.getInstance().getConfig3();
            int rewardId = Integer.parseInt(productId.substring(4));
            MyOccTaskTemplate.OccTaskRewardItem occTaskRewardItem = null;
            for (MyOccTaskTemplate.OccTaskRewardItem occTaskRewardItem1 : config3.getREWARDS()) {
                if (occTaskRewardItem1.getID().equals(rewardId)) {
                    occTaskRewardItem = occTaskRewardItem1;
                    break;
                }
            }
            if (pr != null) {
                // add buy record
                OccTask occTask = OccTaskCache.getInstance().getOccTask(playerId);
                if (occTask != null) {
                    occTask.setIndex(4);
                    OccTaskDaoHelper.asyncUpdateOccTask(occTask);
                }
                // 发奖励
                for (RewardTemplateSimple rewardTemplateSimple : occTaskRewardItem.getITEMS()) {
                    AwardUtils.changeRes(pr, rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT(),
                            LogConstants.MODULE_PAYMENT);
                }
                PushPaymentResult respMsg = new PushPaymentResult();
                respMsg.yb = 0;
                respMsg.pid = productId;
                respMsg.vipExp = rechargeTemplate.getVIPEXP();
                pr.writeAndFlush(respMsg.build(pr.alloc()));
            } else {
                // LibaoBuyManager.getInstance().addLibaoBuyOffline(playerId,productId);
                // 发奖励
                Map<Integer, Integer> mailAtt = new HashMap<>();
                for (RewardTemplateSimple rewardTemplateSimple : occTaskRewardItem.getITEMS()) {
                    mailAtt.put(rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT());
                }
                String mailTitle = "Phần thưởng mua Gói quà Nhiệm vụ Nghề nghiệp";
                String mailContent = "Phần thưởng như sau:";
                MailManager.getInstance().sendSysMailToSingle(playerId, mailTitle, mailContent, mailAtt);
            }
        } else {
            // Tính toán số kim cương được cộng thêm
            int sumDiamond = addDiamond;

            // Kiểm tra nạp lần đầu - nhân đôi kim cương
            boolean firstRecharge = !PaymentLogDao.getInstance().checkRechargeExist(productId, userId, serverid); 
            logger.info("Check first recharge result: {}, current sumDiamond: {}", firstRecharge, sumDiamond);

            if (firstRecharge) {
                logger.info("Is first recharge, double diamond from {} to {}", sumDiamond, sumDiamond * 2);
                sumDiamond = sumDiamond * 2;
            }

            // Log final amount
            logger.info("Final sumDiamond to add: {}", sumDiamond);

            // Kiểm tra template hợp lệ
            if (rechargeTemplate == null || (!rechargeTemplate.getPRICE().equals(money)
                    && (rechargeTemplate.getFIRST() == null || !rechargeTemplate.getFIRST().equals(money)))) {
                logger.warn("RechargeTemplate product id not exist,userId={},productId={},money={}", userId, productId,
                        money);
                productId = "money";
            }

            // Cập nhật kim cương vào DB
            PlayerDao.getInstance().addMoney(sumDiamond, userId);
            if (pb != null) {
                pb.setMoney(pb.getMoney() + sumDiamond);
            }

            // Cập nhật VIP
            List<PlayerBean> playerAll = PlayerDao.getInstance().getPlayerByOpenId(userId, serverid);
            PlayerBean playerBean = playerAll.get(0);
            int nowVipExp = playerBean.getVipExp();
            nowVipExp += addDiamond;
            int vipLev = VipManager.getInstance().addVipExp(nowVipExp);
            PlayerDao.getInstance().updateVipLev(vipLev, nowVipExp, playerBean.getId());

            // Cập nhật cache nếu có
            if (pb != null) {
                pb.setVipLevel(vipLev);
                pb.setVipExp(nowVipExp);
            }

            // Xử lý các hoạt động liên quan đến nạp tiền
            if (pr != null) {
                // Force update player info - chỉ gọi 1 lần
                PlayerInfoManager.getInstance().changeMoney(pr, sumDiamond, LogConstants.MODULE_PAYMENT);
                
                // Gửi kết quả nạp tiền kèm theo vipExp
                PushPaymentResult respMsg = new PushPaymentResult();
                respMsg.yb = sumDiamond;
                respMsg.pid = productId;
                respMsg.vipExp = rechargeTemplate.getVIPEXP();
                pr.writeAndFlush(respMsg.build(pr.alloc()));
                pr.flush();
            }

            // Xử lý các hoạt động liên quan đến nạp tiền cho cả online và offline - chỉ gọi 1 lần duy nhất
            ActivityManager.getInstance().recharge(pr, money * 10);
            ActivityManager.getInstance().dailyRecharge(pr, money * 10);
            if (pr != null) {
                ActivityManager.getInstance().addDailyActive(pr, ActivityConstants.ACTIVE_RECHARGE * addDiamond);
            }

            if (!isQingChunJiJIn) {
                // Xử lý hoạt động nạp lần đầu trong ngày
                ActivityRecordManager.getInstance().meiRiShouChongOffline(playerId);
                // Xử lý hoạt động nạp liên tục
                ActivityRecordManager.getInstance().lianXuChongZhiOffline(playerId);
                // Xử lý hoạt động nạp một lần
                ActivityRecordManager.getInstance().danBiChongZhiOffline(playerId, money);
                // Xử lý nạp lần đầu
                if (isFirstRecharge) {
                    firstRechargeSaveOffline(playerId);
                }
            }

            // Cập nhật bảng xếp hạng nạp tiền
            ActivityRecordManager.getInstance().chongZhiBang(playerId, money);
        }


         // 插入log
         if (productId.equals("x7sy")) {
            PaymentLogDao.getInstance().updatePayLog(money, orderid);
        } else {
            PaymentLogDao.getInstance().saveTopupLog(pb != null ? pb.getId() : 0, productId, orderid, money,
                    userId, serverid);
        }

        PaymentManager.getInstance().addPayPlayer(playerId);
       
    }

    public static void firstRechargeSave(PlayingRole pr) {
        int playerId = pr.getId();
        ChargeEntity chargeEntity = ChargeCache.getInstance().getChargeEntity(playerId);
        if (chargeEntity == null) {
            chargeEntity = new ChargeEntity();
            chargeEntity.setPlayerId(playerId);
            chargeEntity.setFirstPayTime(new Date());
            ChargeDao.getInstance().addChargeEntity(chargeEntity);
            ChargeCache.getInstance().addNewEntity(chargeEntity);
        } else {
            chargeEntity.setFirstPayTime(new Date());
            ChargeDao.getInstance().updateCharge(chargeEntity);
        }
        // push
        PushFirstPay respMsg = new PushFirstPay();
        pr.writeAndFlush(respMsg.build(pr.alloc()));
    }

    public void firstRechargeSaveOffline(Integer playerId) {
        ChargeEntity chargeEntity = ChargeCache.getInstance().getChargeEntity(playerId);
        if (chargeEntity == null) {
            chargeEntity = ChargeDao.getInstance().getChargeEntityByPlayerId(playerId);
        }
        if (chargeEntity == null) {
            chargeEntity = new ChargeEntity();
            chargeEntity.setPlayerId(playerId);
            chargeEntity.setFirstPayTime(new Date());
            ChargeDao.getInstance().addChargeEntity(chargeEntity);
        } else {
            chargeEntity.setFirstPayTime(new Date());
            ChargeDao.getInstance().updateCharge(chargeEntity);
        }
    }

    public static Map<Integer, Integer> getMailAtt(Map<Integer, Integer> rewardItems) {
        int activityDouble = ActivityManager.getInstance().getMoudelMultiple(ActivityConstants.AWARD_DOUBLE_CRAFT);
        Map<Integer, Integer> dbMailAtt = new HashMap<>();
        for (Map.Entry<Integer, Integer> itemPair : rewardItems.entrySet()) {
            int itemId = itemPair.getKey();
            int count = itemPair.getValue();
            dbMailAtt.put(itemId, count * activityDouble);
        }
        return dbMailAtt;
    }

    public static void offlineRechargeActivity(Integer playerId, int addDiamond) {
        ActivityBean activityBean = ActivityCache.getInstance().getOneActivity(ActivityConstants.DYNAMIC_TYPE_RECHARGE);
        if (activityBean == null) {
            return;
        }
        // 活动开启，是否在有效时间内
        if (activityBean.getIsOpen() == ActivityConstants.NO
                || !DateCommonUtils.betweenDate(activityBean.getStartTime(), activityBean.getEndTime())) {
            return;
        }
        ActivityPlayer targetActivityPlayer = ActivityPlayerCache.getInstance().getActivityPlayer(playerId,
                ActivityConstants.DYNAMIC_TYPE_RECHARGE);
        if (targetActivityPlayer == null) {
            List<ActivityPlayer> activitysPlayer = ActivityPlayerDao.getInstance().getActivityPlayer(playerId);
            for (ActivityPlayer activityPlayer : activitysPlayer) {
                if (activityPlayer.getType() == ActivityConstants.DYNAMIC_TYPE_RECHARGE) {
                    targetActivityPlayer = activityPlayer;
                }
            }
        }
        if (targetActivityPlayer == null) {
            targetActivityPlayer = new ActivityPlayer();
            targetActivityPlayer.setPlayerId(playerId);
            targetActivityPlayer.setProgress(addDiamond);
            targetActivityPlayer.setType(ActivityConstants.DYNAMIC_TYPE_RECHARGE);
            ActivityPlayerDao.getInstance().addActivityPlayer(targetActivityPlayer);
        } else {
            targetActivityPlayer.setProgress(targetActivityPlayer.getProgress() + addDiamond);
            ActivityPlayerDao.getInstance().updateActivityPlayerProgress(targetActivityPlayer);
        }
    }

    public void addOrderId(String orderId) {
        existOrderIdAll.add(orderId);
    }

    public boolean checkOrderExist(String orderId) {
        return existOrderIdAll.contains(orderId);
    }

    public Set<Integer> getPayPlayers() {
        return payPlayers;
    }

    public void addPayPlayer(int playerId) {
        int oldSize = payPlayers.size();
        if (playerId > 0) {
            payPlayers.add(playerId);
        }
        int newSize = payPlayers.size();
        if (newSize > oldSize) {
            // 发送活动更新通知
            // Collection<PlayingRole> playerAll =
            // SessionManager.getInstance().getAllPlayers();
            // PushActivityProgressUpdate pushMsg = new PushActivityProgressUpdate(newSize,
            // ActivityConstants.TYPE_ACTIVITY_TYPE_QUAN_MIN_FU_LI);
            // MySendToMessage retMsg = null;
            // for (PlayingRole playingRole : playerAll) {
            // if (playingRole.isChannelActive()) {
            // if (retMsg == null) {
            // retMsg = pushMsg.build(playingRole.alloc());
            // }
            // playingRole.getGamePlayer().writeAndFlushAll(retMsg);
            // }
            // if (retMsg != null) {
            // retMsg.release();
            // }
        }
    }

    public void loadPayPlayers() {
        payPlayers = PaymentLogDao.getInstance().getPaymentPlayers();
    }

    public static void offline3DayRechargeActivity(Integer playerId) {
        ActivityBean activityBean = ActivityCache.getInstance()
                .getOneActivity(ActivityConstants.TYPE_ACTIVITY_TYPE_3DAY_FIRST_RECHARGE);
        if (activityBean == null) {
            return;
        }
        // 活动开启，是否在有效时间内
        if (activityBean.getIsOpen() == ActivityConstants.NO
                || !DateCommonUtils.betweenDate(activityBean.getStartTime(), activityBean.getEndTime())) {
            return;
        }
        ActivityPlayer targetActivityPlayer = ActivityPlayerCache.getInstance().getActivityPlayer(playerId,
                ActivityConstants.TYPE_ACTIVITY_TYPE_3DAY_FIRST_RECHARGE);
        if (targetActivityPlayer == null) {
            List<ActivityPlayer> activitysPlayer = ActivityPlayerDao.getInstance().getActivityPlayer(playerId);
            for (ActivityPlayer activityPlayer : activitysPlayer) {
                if (activityPlayer.getType() == ActivityConstants.TYPE_ACTIVITY_TYPE_3DAY_FIRST_RECHARGE) {
                    targetActivityPlayer = activityPlayer;
                }
            }
        }
        if (targetActivityPlayer == null) {
            targetActivityPlayer = new ActivityPlayer();
            targetActivityPlayer.setPlayerId(playerId);
            targetActivityPlayer.setProgress(0);
            targetActivityPlayer.setType(ActivityConstants.TYPE_ACTIVITY_TYPE_3DAY_FIRST_RECHARGE);
            ActivityPlayerDao.getInstance().addActivityPlayer(targetActivityPlayer);
        }
    }

    public void fanliCallback(Channel channel, String userId, String orderid, int money, int time, int isVip,
            int serverid) {
        logger.info("185sy fanli callback parse,userId={},orderid={},money={},time={},isVip={}", userId, orderid, money,
                time, isVip);
        GameServer.executorService.execute(new Runnable() {
            public void run() {
                int addDiamond = money * PaymentConstants.RMB_2_YUANBAO;
                String productId = "charge328";
                // 订单是否存在
                boolean orderExist = PaymentLogDao.getInstance().checkOrderExist(orderid);
                if (orderExist) {
                    logger.info("order exist,userId={},orderid={}", userId, orderid);
                    return;
                }
                PlayerBean pb = PlayerOnlineCacheMng.getInstance().getCache(userId, serverid);
                // 查询玩家id
                int playerId = 0;
                if (pb != null) {
                    playerId = pb.getId();
                } else {
                    List<PlayerBean> playerAll = PlayerDao.getInstance().getPlayerByOpenId(userId, serverid);
                    if (playerAll == null || playerAll.size() == 0) {
                        return;
                    }
                    pb = playerAll.get(0);
                    playerId = pb.getId();
                }
                // 玩家是否在线
                PlayingRole pr = SessionManager.getInstance().getPlayer(playerId);
                int sumDiamond = addDiamond;
                // 更新数据库
                logger.info("update db,addDiamond={},openId={}", sumDiamond, userId);
                PlayerDao.getInstance().addMoney(sumDiamond, userId);
                // 更新玩家钻石
                if (pr != null) {
                    PlayerInfoManager.getInstance().changeMoney(pr, sumDiamond, LogConstants.MODULE_PAYMENT);
                    PushPaymentResult respMsg = new PushPaymentResult();
                    respMsg.yb = sumDiamond;
                    respMsg.pid = productId;
                    pr.writeAndFlush(respMsg.build(pr.alloc()));
                } else {
                    if (pb != null) {
                        pb.setMoney(pb.getMoney() + sumDiamond);
                    }
                }
                // 插入log
                PaymentLogDao.getInstance().saveTopupLogFanli(pb != null ? pb.getId() : 0, productId, orderid, money,
                        userId,
                        serverid);
                // 更新VIP经验
                if (isVip == 1) {
                    if (pr != null) {
                        // VIP增加
                        VipManager.getInstance().rechargeVipLev(pr, addDiamond);
                        pr.flush();
                    } else {
                        // 离线更新VIP
                        if (pb != null) {
                            int nowVipExp = pb.getVipExp();
                            nowVipExp += addDiamond;
                            int vipLev = VipManager.getInstance().addVipExp(nowVipExp);
                            PlayerDao.getInstance().updateVipLev(vipLev, nowVipExp, pb.getId());
                            pb.setVipLevel(vipLev);
                            pb.setVipExp(nowVipExp);
                        }
                    }
                }
            }
        });
    }

    public void payCallbackNoMessage(final Channel channel, final String userId, final String orderid, final int money,
            int time,
            final String pid, final int serverid) {
        logger.info("payment feedback,userId={},orderid={},money={},other={}", userId, orderid, money, pid);
        int addDiamond = money * PaymentConstants.RMB_2_YUANBAO;
        // 是否为小7充值
        if (pid.equals("x7sy")) {
            int productId = PaymentLogDao.getInstance().getToPayProductId(orderid);
            if (productId <= 0) {
                logger.info("xiao7 order not exist,userId={},orderid={}", userId, orderid);
                return;
            }
        } else {
            boolean orderExist = PaymentLogDao.getInstance().checkOrderExist(orderid);
            if (orderExist) {
                logger.info("order exist,userId={},orderid={}", userId, orderid);
                return;
            }
        }
        PlayerBean pb = PlayerOnlineCacheMng.getInstance().getCache(userId, serverid);
        String productId = pid;
        // 查询玩家id
        int playerId = 0;
        if (pb != null) {
            playerId = pb.getId();
        } else {
            List<PlayerBean> playerAll = PlayerDao.getInstance().getPlayerByOpenId(userId, serverid);
            if (playerAll == null || playerAll.size() == 0) {
                return;
            }
            playerId = playerAll.get(0).getId();
        }
        // 充值人数
        PaymentManager.getInstance().addPayPlayer(playerId);
        // 玩家是否在线
        PlayingRole pr = SessionManager.getInstance().getPlayer(playerId);
        // 是否进行首冲
        boolean isFirstRecharge = false;
        if (pr != null) {
            ChargeEntity chargeEntity = ChargeCache.getInstance().getChargeEntity(playerId);
            if (chargeEntity == null || chargeEntity.getFirstPayTime() == null) {
                isFirstRecharge = true;
            }
        } else {
            isFirstRecharge = ChargeDao.getInstance().isFirstPay(playerId);
        }
        // 是否为青春基金
        boolean isQingChunJiJIn = false;
        // 月卡处理
        if (pid.equals("specialgz")) {
            // DailyMissionManager.getInstance().addMonthCardDailyMission(playerId);
            if (pr != null) {
                ChargeInfoManager.getInstance().addGzYueka(pr);
                // 发元宝
                MCardTemplate gzMCardTemplate = MCardTemplateCache.getInstance().getGzMCardTemplate();
                int addYuanbao = gzMCardTemplate.getARRACT().get(0).get("ACTCOUNT");
                AwardUtils.changeRes(pr, GameConfig.PLAYER.YB, addYuanbao, LogConstants.MODULE_RECHARGE);
                AwardUtils.changeRes(pr, 90271, 1, LogConstants.MODULE_RECHARGE);
            } else {
                List<PlayerBean> playerAll = PlayerDao.getInstance().getPlayerByOpenId(userId, serverid);
                if (playerAll == null || playerAll.size() == 0) {
                    return;
                }
                PlayerBean playerBean = playerAll.get(0);
                ChargeInfoManager.getInstance().addGzYuekaOffline(playerBean.getId());
            }
        } else if (pid.equals("specialzz")) {
            // DailyMissionManager.getInstance().addForeverMonthCardDailyMission(playerId);
            if (pr != null) {
                ChargeInfoManager.getInstance().addZzYueka(pr);
                MCardTemplate zzMCardTemplate = MCardTemplateCache.getInstance().getGzMCardTemplate();
                int addYuanbao = zzMCardTemplate.getARRACT().get(0).get("ACTCOUNT");
                AwardUtils.changeRes(pr, GameConfig.PLAYER.YB, addYuanbao, LogConstants.MODULE_RECHARGE);
                AwardUtils.changeRes(pr, 90272, 1, LogConstants.MODULE_RECHARGE);
            } else {
                List<PlayerBean> playerAll = PlayerDao.getInstance().getPlayerByOpenId(userId, serverid);
                if (playerAll == null || playerAll.size() == 0) {
                    return;
                }
                PlayerBean playerBean = playerAll.get(0);
                ChargeInfoManager.getInstance().addZzYuekaOffline(playerBean.getId());
            }
        } else if (pid.equals("specialcz")) {// 成长基金
            if (pr != null) {
                ActivityManager.getInstance().chengZhangJiJin(pr);
            } else {
                List<PlayerBean> playerAll = PlayerDao.getInstance().getPlayerByOpenId(userId, serverid);
                if (playerAll == null || playerAll.size() == 0) {
                    return;
                }
                PlayerBean playerBean = playerAll.get(0);
                ActivityManager.getInstance().chengZhangJiJinOffline(playerBean.getId());
            }
        } else if (pid.startsWith("herolibao")) {// 英雄礼包
            if (pr != null) {
                ActivityManager.getInstance().heroLibao(pr, Integer.parseInt(pid.substring(9)), addDiamond);
            } else {
                List<PlayerBean> playerAll = PlayerDao.getInstance().getPlayerByOpenId(userId, serverid);
                if (playerAll == null || playerAll.size() == 0) {
                    return;
                }
                PlayerBean playerBean = playerAll.get(0);
                ActivityManager.getInstance().heroLibaoOffline(playerBean.getId(), Integer.parseInt(pid.substring(9)),
                        userId, serverid);
            }
        } else if (pid.startsWith("qcjj")) {// 青春基金
            if (pr != null) {
                ActivityRecordManager.getInstance().qingChunJiJin(pr, Integer.parseInt(pid.substring(4)), addDiamond);
            } else {
                List<PlayerBean> playerAll = PlayerDao.getInstance().getPlayerByOpenId(userId, serverid);
                if (playerAll == null || playerAll.size() == 0) {
                    return;
                }
                PlayerBean playerBean = playerAll.get(0);
                ActivityRecordManager.getInstance().qingChunJiJinOffline(playerBean.getId(),
                        Integer.parseInt(pid.substring(4)), userId,
                        serverid);
            }
            isQingChunJiJIn = true;
        } else {
            int sumDiamond = addDiamond;
            // 是否有首冲3倍活动
            // BeanFirstRechargeTriple isFirstRechargeTriple = ActivityManager.getInstance()
            // .isFirstRechargeTriple(playerId, productId);
            // logger.info("isFirstRechargeTriple,ret={},favorRate={}",
            // isFirstRechargeTriple.ret,
            // isFirstRechargeTriple.favorRate);
            // if (isFirstRechargeTriple.ret == true) {
            // int favorRate = isFirstRechargeTriple.favorRate;
            // sumDiamond = Math.round(addDiamond * favorRate / 100f);
            // } else {
            // 首冲奖励钻石
            if (!PaymentLogDao.getInstance().checkRechargeExist(productId, userId, serverid)) {
                logger.info("isFirstRecharge,rate=2");
                sumDiamond = addDiamond * 2;
            }
            // }
            // 更新数据库
            logger.info("update db,addDiamond={},openId={}", sumDiamond, userId);
            PlayerDao.getInstance().addMoney(sumDiamond, userId);
            // 更新玩家钻石
            if (pr != null) {
                PlayerInfoManager.getInstance().changeMoney(pr, sumDiamond, LogConstants.MODULE_PAYMENT);
            } else {
                if (pb != null) {
                    pb.setMoney(pb.getMoney() + sumDiamond);
                }
            }
            // 首冲3倍
            // if (isFirstRechargeTriple.ret == true) {
            // ActivityManager.getInstance().doFirstRechargeTriple(playerId, productId);
            // }
        }
        // 更新VIP经验
        if (pr != null) {
            // VIP增加
            VipManager.getInstance().rechargeVipLev(pr, addDiamond);
            // 充值活动
            ActivityManager.getInstance().recharge(pr, money * 10);
            // 每日充值活动
            ActivityManager.getInstance().dailyRecharge(pr, money * 10);
            if (!isQingChunJiJIn) {
                // 每日首充活动
                ActivityRecordManager.getInstance().meiRiShouChong(pr);
                // 连续充值活动
                ActivityRecordManager.getInstance().lianXuChongZhi(pr);
                // 单笔充值活动
                ActivityRecordManager.getInstance().danBiChongZhi(pr, money);
                // 首冲奖励
                if (isFirstRecharge) {
                    firstRechargeSave(pr);
                }
            }
            // 充值榜活动
            ActivityRecordManager.getInstance().chongZhiBang(pr.getId(), money);
            // 天天好礼活动
            ActivityManager.getInstance().tianTianHaoLi(pr, addDiamond);
            // 活跃度奖励
            ActivityManager.getInstance().addDailyActive(pr, ActivityConstants.ACTIVE_RECHARGE * addDiamond);
        } else {
            // 离线更新VIP
            List<PlayerBean> playerAll = PlayerDao.getInstance().getPlayerByOpenId(userId, serverid);
            if (playerAll == null || playerAll.size() == 0) {
                return;
            }
            PlayerBean playerBean = playerAll.get(0);
            int nowVipExp = playerBean.getVipExp();
            nowVipExp += addDiamond;
            int vipLev = VipManager.getInstance().addVipExp(nowVipExp);
            PlayerDao.getInstance().updateVipLev(vipLev, nowVipExp, playerBean.getId());
            if (pb != null) {
                pb.setVipLevel(vipLev);
                pb.setVipExp(nowVipExp);
            }
            // 更新充值活动
            offlineRechargeActivity(playerBean.getId(), money * 10);
            if (!isQingChunJiJIn) {
                // 每日首充
                ActivityRecordManager.getInstance().meiRiShouChongOffline(playerBean.getId());
                // 连续充值活动
                ActivityRecordManager.getInstance().lianXuChongZhiOffline(playerBean.getId());
                // 单笔充值活动
                ActivityRecordManager.getInstance().danBiChongZhiOffline(playerBean.getId(), money);
                // 首冲奖励
                if (isFirstRecharge) {
                    firstRechargeSaveOffline(playerBean.getId());
                }
            }
            // 充值榜活动
            // ActivityRecordManager.getInstance().chongZhiBang(playerBean.getId(), money);
            // 天天好礼活动
            // ActivityRecordManager.getInstance().tianTianHaoLiOffline(playerBean.getId(),
            // addDiamond);
        }
    }

    public void fakePay(PlayingRole pr, int moneyYuan, String productId) {
        logger.info("fakepay,player={},rmb={}", pr.getId(), moneyYuan);
        int addDiamond = moneyYuan * PaymentConstants.RMB_2_YUANBAO;
        PlayerInfoManager.getInstance().changeMoney(pr, addDiamond, LogConstants.MODULE_PAYMENT);
        PushPaymentResult respMsg = new PushPaymentResult();
        respMsg.yb = addDiamond;
        respMsg.pid = productId;
        pr.getGamePlayer().write(respMsg.build(pr.alloc()));
        // 充值金额
        // AwardUtils.changeRes(pr, PayConstants.PAY_MONEY_GSID, money, LogConstants.MODULE_PAYMENT);
        // VIP增加
        VipManager.getInstance().rechargeVipLev(pr, addDiamond);
        // 充值活动
        ActivityManager.getInstance().recharge(pr, moneyYuan * 10);
        // 每日充值活动
        ActivityManager.getInstance().dailyRecharge(pr, moneyYuan * 10);
        // 每日首充活动
        ActivityRecordManager.getInstance().meiRiShouChong(pr);
        // 连续充值活动
        ActivityRecordManager.getInstance().lianXuChongZhi(pr);
        // 单笔充值活动
        ActivityRecordManager.getInstance().danBiChongZhi(pr, moneyYuan);
        // 充值榜活动
        ActivityRecordManager.getInstance().chongZhiBang(pr.getId(), moneyYuan);
        // 天天好礼活动
        ActivityManager.getInstance().tianTianHaoLi(pr, addDiamond);
        // 活跃度奖励
        ActivityManager.getInstance().addDailyActive(pr, ActivityConstants.ACTIVE_RECHARGE * addDiamond);
        // 首冲奖励
        PaymentManager.firstRechargeSave(pr);
        pr.flush();
    }

    // Thêm hàm kiểm tra gói nạp hợp lệ
    private boolean isValidProductId(String productId) {
        if (productId == null) return false;
        
        // Kiểm tra các loại gói nạp hợp lệ
        return productId.equals("specialgz") || // Thẻ hàng tháng cao quý
               productId.equals("specialzz") || // Thẻ hàng tháng tối cao  
               productId.equals("specialcz") || // Quỹ tăng trưởng
               productId.equals("specialbw") || // Đặc quyền của chúa tể
               productId.equals("specialqz") || // Đặc ân là siêng năng và quan tâm đến mọi người
               productId.equals("specialyd") || // Đặc quyền hàng tháng
               productId.startsWith("herolibao") || // Gói quà anh hùng
               productId.startsWith("qcjj") || // Quỹ thanh niên
               productId.startsWith("mzlb") || // Gói quà hàng tuần
               productId.startsWith("mylb") || // Gói quà hàng tháng
               productId.startsWith(PayConstants.PRODUCT_ID_CJXG2) || // Hoạt động tuần của cjxg2
               productId.startsWith(PayConstants.PRODUCT_ID_GXLB) || // Sự kiện tuần lễ gói quà ngắm sao
               productId.startsWith(PayConstants.PRODUCT_ID_GJLB) || // Treo gói quà sự kiện tuần
               productId.startsWith(PayConstants.PRODUCT_ID_GZLB) || // Sự kiện tuần gói quà công nghệ cao
               productId.startsWith(PayConstants.PRODUCT_ID_CZLB) || // Sự kiện tuần gói quà siêu giá trị
               productId.startsWith(PayConstants.PRODUCT_ID_STARGIFT) || // Gói quà tặng giới hạn thời gian của Star
               productId.startsWith(PayConstants.PRODUCT_ID_LEVELGIFT) || // Gói quà tặng giới hạn cấp độ
               productId.startsWith(PayConstants.PRODUCT_ID_OCC); // Gói quà tặng sự nghiệp
    }

    // Sửa lại hàm xử lý nạp tiền chính
    public void handlePayment(String productId, int playerId, String userId, int serverid, int money, String orderid) {
        // Kiểm tra gói nạp hợp lệ 
        if (!isValidProductId(productId)) {
            logger.error("Invalid product id: {}", productId);
            return;
        }

        // Kiểm tra order id trùng lặp
        if (existOrderIdAll.contains(orderid)) {
            logger.error("Duplicate order id: {}", orderid);
            return;
        }
        existOrderIdAll.add(orderid);

        // Lấy thông tin player
        PlayingRole pr = SessionManager.getInstance().getPlayer(playerId); // Sửa lại cách lấy player
        PlayerBean pb = PlayerDao.getInstance().getPlayerById(playerId);

        // Xử lý nạp tiền theo loại gói
        try {
            if (productId.equals("specialgz")) {
                handleSpecialGz(pr, playerId);
            } else if (productId.equals("specialzz")) {
                handleSpecialZz(pr, playerId);
            } else if (productId.equals("specialcz")) {
                handleSpecialCz(pr, pb, playerId);
            } else if (productId.equals("specialbw")) {
                handleSpecialBw(pr, playerId);
            } else if (productId.equals("specialqz")) {
                handleSpecialQz(pr, playerId);
            } else if (productId.equals("specialyd")) {
                handleSpecialYd(pr, playerId);
            } else if (productId.startsWith("herolibao")) {
                // Truyền thêm userId và serverid khi gọi
                handleHeroLibao(pr, playerId, Integer.parseInt(productId.substring(9)), 
                    money * PaymentConstants.RMB_2_YUANBAO, userId, serverid);
            }
            // ... xử lý các gói khác

            // Xử lý VIP và thông báo
            updateVipAndNotify(pr, pb, money * PaymentConstants.RMB_2_YUANBAO);

            // Log giao dịch
            savePaymentLog(playerId, productId, orderid, money, userId, serverid);

        } catch (Exception e) {
            logger.error("Error processing payment", e);
        }
    }

    // Các hàm xử lý từng loại gói nạp
    private void handleSpecialGz(PlayingRole pr, int playerId) {
        if (pr != null) {
            ChargeInfoManager.getInstance().addGzYueka(pr);
            MCardTemplate template = MCardTemplateCache.getInstance().getGzMCardTemplate();
            int addYuanbao = template.getARRACT().get(0).get("ACTCOUNT");
            AwardUtils.changeRes(pr, GameConfig.PLAYER.YB, addYuanbao, LogConstants.MODULE_RECHARGE);
            AwardUtils.changeRes(pr, 90271, 1, LogConstants.MODULE_RECHARGE);
            sendPaymentResult(pr, addYuanbao, "specialgz");
        } else {
            ChargeInfoManager.getInstance().addGzYuekaOffline(playerId);
        }
    }

    private void handleSpecialZz(PlayingRole pr, int playerId) {
        if (pr != null) {
            ChargeInfoManager.getInstance().addZzYueka(pr);
            MCardTemplate template = MCardTemplateCache.getInstance().getZzMCardTemplate();
            int addYuanbao = template.getARRACT().get(0).get("ACTCOUNT");
            AwardUtils.changeRes(pr, GameConfig.PLAYER.YB, addYuanbao, LogConstants.MODULE_RECHARGE);
            AwardUtils.changeRes(pr, 90272, 1, LogConstants.MODULE_RECHARGE);
            sendPaymentResult(pr, addYuanbao, "specialzz");
        } else {
            ChargeInfoManager.getInstance().addZzYuekaOffline(playerId);
        }
    }

    private void handleSpecialCz(PlayingRole pr, PlayerBean pb, int playerId) {
        if (pr != null) {
            ChargeInfoManager.getInstance().addCz(pr);
            sendPaymentResult(pr, 0, "specialcz");
        } else {
            ActivityManager.getInstance().chengZhangJiJinOffline(playerId);
        }
        
        // Xử lý phần thưởng
        int rewardYbSum = 0;
        Integer playerLevel = pb.getLevel();
        List<Integer> rewardLevels = new ArrayList<>();
        List<MActivityGpTemplate> templateMap = MActivityGpTemplateCache.getInstance().getTemplateMap();
        for (MActivityGpTemplate template : templateMap) {
            if (playerLevel >= template.getLEVEL()) {
                rewardYbSum += template.getYBNUM();
                rewardLevels.add(template.getLEVEL());
            }
        }
        
        if (rewardYbSum > 0) {
            Map<Integer, Integer> mailAtt = new HashMap<>();
            mailAtt.put(GameConfig.PLAYER.YB, rewardYbSum);
            String mailTitle = "Phần thưởng mua Quỹ Trưởng Thành";
            String mailContent = String.format("Bạn đã hoàn thành cấp độ: %1$s, phần thưởng như sau:", 
                rewardLevels.toString());
            MailManager.getInstance().sendSysMailToSingle(playerId, mailTitle, mailContent, mailAtt);
        }
    }

    // Hàm xử lý VIP và thông báo
    private void updateVipAndNotify(PlayingRole pr, PlayerBean pb, int addDiamond) {
        if (pr != null) {
            VipManager.getInstance().rechargeVipLev(pr, addDiamond);
        } else if (pb != null) {
            int nowVipExp = pb.getVipExp() + addDiamond;
            int vipLev = VipManager.getInstance().addVipExp(nowVipExp);
            PlayerDao.getInstance().updateVipLev(vipLev, nowVipExp, pb.getId());
            pb.setVipLevel(vipLev);
            pb.setVipExp(nowVipExp);
        }
    }

    // Sửa lại hàm lưu log giao dịch
    private void savePaymentLog(int playerId, String productId, String orderid, int money, String userId, int serverid) {
        // Sử dụng phương thức có sẵn để lưu log
        PaymentLogDao.getInstance().checkRechargeExist(productId, userId, serverid);
    }

    // Thêm hàm gửi kết quả nạp tiền
    private void sendPaymentResult(PlayingRole pr, int yuanbao, String productId) {
        PushPaymentResult respMsg = new PushPaymentResult();
        respMsg.yb = yuanbao;
        respMsg.pid = productId;
        pr.writeAndFlush(respMsg.build(pr.alloc()));
    }

    // Thêm các hàm xử lý gói nạp còn thiếu
    private void handleSpecialBw(PlayingRole pr, int playerId) {
        if (pr != null) {
            ChargeInfoManager.getInstance().addBw(pr);
            PushPaymentResult respMsg = new PushPaymentResult();
            respMsg.yb = 0;
            respMsg.pid = "specialbw";
            pr.writeAndFlush(respMsg.build(pr.alloc()));
            // Phần thưởng
            RightTemplate rightTemplate = RightTemplateCache.getInstance().getRightTemplate("specialbw");
            for (RewardTemplateSimple rewardTemplateSimple : rightTemplate.getREWARD()) {
                AwardUtils.changeRes(pr, rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT(),
                        LogConstants.MODULE_RECHARGE);
            }
        } else {
            ChargeInfoManager.getInstance().addBwOffline(playerId);
            // Gửi mail phần thưởng
            Map<Integer, Integer> mailAtt = new HashMap<>();
            RightTemplate rightTemplate = RightTemplateCache.getInstance().getRightTemplate("specialbw");
            for (RewardTemplateSimple rewardTemplateSimple : rightTemplate.getREWARD()) {
                mailAtt.put(rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT());
            }
            String mailTitle = "Phần thưởng mua Đặc quyền Bá Vương";
            String mailContent = "Phần thưởng như sau:";
            MailManager.getInstance().sendSysMailToSingle(playerId, mailTitle, mailContent, mailAtt);
        }
    }

    private void handleSpecialQz(PlayingRole pr, int playerId) {
        if (pr != null) {
            ChargeInfoManager.getInstance().addQz(pr);
            
            // Phần thưởng trực tiếp khi online
            RightTemplate rightTemplate = RightTemplateCache.getInstance().getRightTemplate("specialqz");
            if (rightTemplate != null && rightTemplate.getREWARD() != null) {
                // Gửi phần thưởng trực tiếp
                for (RewardTemplateSimple rewardTemplateSimple : rightTemplate.getREWARD()) {
                    AwardUtils.changeRes(pr, rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT(),
                            LogConstants.MODULE_RECHARGE);
                }
                
                // Gửi thêm mail xác nhận
                Map<Integer, Integer> mailAtt = new HashMap<>();
                for (RewardTemplateSimple rewardTemplateSimple : rightTemplate.getREWARD()) {
                    mailAtt.put(rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT());
                }
                String mailTitle = "Phần thưởng mua Đặc quyền Cần Chính Ái Dân";
                String mailContent = "Bạn đã nhận được các phần thưởng sau:";
                MailManager.getInstance().sendSysMailToSingle(playerId, mailTitle, mailContent, mailAtt);
            }

            // Gửi thông báo kết quả nạp
            PushPaymentResult respMsg = new PushPaymentResult();
            respMsg.yb = 0; 
            respMsg.pid = "specialqz";
            pr.writeAndFlush(respMsg.build(pr.alloc()));
            
            // Flush để đảm bảo update ngay
            pr.flush();
            
        } else {
            // Xử lý offline
            ChargeInfoManager.getInstance().addQzOffline(playerId);
            
            // Gửi phần thưởng qua mail
            RightTemplate rightTemplate = RightTemplateCache.getInstance().getRightTemplate("specialqz");
            if (rightTemplate != null && rightTemplate.getREWARD() != null) {
                Map<Integer, Integer> mailAtt = new HashMap<>();
                for (RewardTemplateSimple rewardTemplateSimple : rightTemplate.getREWARD()) {
                    mailAtt.put(rewardTemplateSimple.getGSID(), rewardTemplateSimple.getCOUNT());
                }
                String mailTitle = "Phần thưởng mua Đặc quyền Cần Chính Ái Dân";
                String mailContent = "Phần thưởng như sau:";
                MailManager.getInstance().sendSysMailToSingle(playerId, mailTitle, mailContent, mailAtt);
            }
        }
        
        // Log để debug
        logger.info("Processed specialqz payment for player {}, online status: {}", 
            playerId, pr != null ? "online" : "offline");
    }

    private void handleSpecialYd(PlayingRole pr, int playerId) {
        if (pr != null) {
            ChargeInfoManager.getInstance().addYd(pr);
            PushPaymentResult respMsg = new PushPaymentResult();
            respMsg.yb = 0;
            respMsg.pid = "specialyd";
            pr.writeAndFlush(respMsg.build(pr.alloc()));
        } else {
            ChargeInfoManager.getInstance().addYdOffline(playerId);
        }
    }

    private void handleHeroLibao(PlayingRole pr, int playerId, int heroIndex, int addDiamond, String userId, int serverid) {
        if (pr != null) {
            ActivityManager.getInstance().heroLibao(pr, heroIndex, addDiamond);
            PushPaymentResult respMsg = new PushPaymentResult();
            respMsg.yb = 0;
            respMsg.pid = "herolibao" + heroIndex;
            pr.writeAndFlush(respMsg.build(pr.alloc()));
        } else {
            ActivityManager.getInstance().heroLibaoOffline(playerId, heroIndex, userId, serverid);
        }
    }

    // ... existing code ...
}
