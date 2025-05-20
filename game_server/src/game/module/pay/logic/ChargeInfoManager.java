package game.module.pay.logic;

import game.entity.PlayingRole;
import game.module.award.bean.GameConfig;
import game.module.mail.logic.MailManager;
import game.module.pay.bean.ChargeEntity;
import game.module.pay.dao.ChargeCache;
import game.module.pay.dao.ChargeDao;
import game.module.template.MActivityGpTemplate;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.WsMessageBase;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChargeInfoManager {

    private static Logger logger = LoggerFactory.getLogger(ChargeInfoManager.class);

    static class SingletonHolder {
        static ChargeInfoManager instance = new ChargeInfoManager();
    }

    public static ChargeInfoManager getInstance() {
        return SingletonHolder.instance;
    }

    public void sendPaymentInfo(PlayingRole playingRole) {
//		int playerId = playingRole.getId();
//		S2CChargeInfo retMsg = new S2CChargeInfo();
//		ChargeEntity chargeEntity = ChargeCache.getInstance().getChargeEntity(playerId);
//		if (chargeEntity != null) {
//			retMsg.is_first_pay = chargeEntity.getIsFirstPay();
//			retMsg.first_award_get = chargeEntity.getFirstPayRewardGet();
//			// VIP礼包领取信息
//			DbVipPackGet dbVipPackGet = chargeEntity.getDbVipPackGet();
//			if (dbVipPackGet != null) {
//				Set<Integer> vipLevelsList = dbVipPackGet.getVipLevels();
//				retMsg.vip_gift_get = new ArrayList<>(vipLevelsList.size());
//				retMsg.vip_gift_get.addAll(vipLevelsList);
//			}
//			// 购买过的不同支付档位
//			DbPaymentLevels dbPaymentLevels = chargeEntity.getDbPaymentLevels();
//			if (dbPaymentLevels != null) {
//				Set<Integer> paymentLevelList = dbPaymentLevels.getPaymentLevels();
//				retMsg.payment_level = new ArrayList<>(paymentLevelList.size());
//				retMsg.payment_level.addAll(paymentLevelList);
//			}
//			// 月卡信息
//			Date yuekaEndTime = chargeEntity.getYueKaEndTime();
//			Date now = new Date();
//			if (yuekaEndTime != null && yuekaEndTime.after(now)) {
//				retMsg.yueka_left_day = (int) ((yuekaEndTime.getTime() - now.getTime()) / (1000 * 3600 * 24));
//			} else {
//				retMsg.yueka_left_day = 0;
//			}
//			Date yuekaGetTime = chargeEntity.getYuekaGetTime();
//			if (yuekaEndTime != null && DateCommonUtils.isSameDay(yuekaGetTime, CommonUtils.RESET_HOUR)) {
//				retMsg.yueka_get = true;
//			} else {
//				retMsg.yueka_get = false;
//			}
//			// 永久月卡信息
//			Boolean isLongYueka = chargeEntity.getIsLongYueka();
//			if (isLongYueka != null && isLongYueka) {
//				retMsg.is_long_yueka = true;
//			} else {
//				retMsg.is_long_yueka = false;
//			}
//			Date longYuekaGetTime = chargeEntity.getLongYuekaGetTime();
//			if (longYuekaGetTime != null && DateCommonUtils.isSameDay(longYuekaGetTime, CommonUtils.RESET_HOUR)) {
//				retMsg.long_yueka_get = true;
//			} else {
//				retMsg.long_yueka_get = false;
//			}
//		}
//		playingRole.getGamePlayer().write(retMsg.build(playingRole.alloc()));
    }

    public WsMessageBase.IOSpecial getChargeInfo(Integer playerId) {
        WsMessageBase.IOSpecial respmsg = new WsMessageBase.IOSpecial();
        ChargeEntity chargeEntity = ChargeCache.getInstance().getChargeEntity(playerId);
        if (chargeEntity != null) {
            respmsg.cz = chargeEntity.getCzjj() != null && chargeEntity.getCzjj() ? 1 : 0;
            Date now = new Date();
            if (chargeEntity.getGzYuekaEndTime() != null && chargeEntity.getGzYuekaEndTime().after(now)) {
                respmsg.gz = (int) ((chargeEntity.getGzYuekaEndTime().getTime() - now.getTime()) / (1000 * 3600 * 24));
            }
            if (chargeEntity.getZzYuekaEndTime() != null && chargeEntity.getZzYuekaEndTime().after(now)) {
                respmsg.zz = (int) ((chargeEntity.getZzYuekaEndTime().getTime() - now.getTime()) / (1000 * 3600 * 24));
            }
            if (chargeEntity.getBwEndTime() != null && chargeEntity.getBwEndTime().after(now)) {
                respmsg.bw = chargeEntity.getBwEndTime().getTime();
            }
            if (chargeEntity.getQzEndTime() != null && chargeEntity.getQzEndTime().after(now)) {
                respmsg.qz = chargeEntity.getQzEndTime().getTime();
            }
            if (chargeEntity.getJtEndTime() != null && chargeEntity.getJtEndTime().after(now)) {
                respmsg.lm = chargeEntity.getJtEndTime().getTime();
            }
            if (chargeEntity.getYdEndTime() != null && chargeEntity.getYdEndTime().after(now)) {
                respmsg.yd = chargeEntity.getYdEndTime().getTime();
            }
            if (chargeEntity.getZxns() != null && chargeEntity.getZxns()) {
                respmsg.zx = DateUtils.addDays(now, 300).getTime();
            }
        }
        return respmsg;
    }

    /**
     * 贵族月卡
     *
     * @param playingRole
     */
    public void addGzYueka(PlayingRole playingRole) {
        int playerId = playingRole.getId();
        ChargeEntity chargeEntity = ChargeCache.getInstance().getChargeEntity(playerId);
        Date now = new Date();
        if (chargeEntity == null) {
            chargeEntity = new ChargeEntity();
            chargeEntity.setPlayerId(playerId);
            chargeEntity.setFirstPayTime(now);
            // 月卡结束时间
            Date yuekaEndTime = DateUtils.addMonths(now, 1);
            chargeEntity.setGzYuekaEndTime(yuekaEndTime);
            ChargeDao.getInstance().addChargeEntity(chargeEntity);
            ChargeCache.getInstance().addNewEntity(chargeEntity);
        } else {
            Date oldEndTime = chargeEntity.getGzYuekaEndTime();
            Date yuekaEndTime = null;
            if (oldEndTime != null && oldEndTime.after(now)) {
                yuekaEndTime = DateUtils.addMonths(oldEndTime, 1);
            } else {
                yuekaEndTime = DateUtils.addMonths(now, 1);
            }
            chargeEntity.setGzYuekaEndTime(yuekaEndTime);
            ChargeDao.getInstance().updateCharge(chargeEntity);
        }
        // ret
        sendPaymentInfo(playingRole);
    }

    public void addGzYuekaOffline(Integer playerId) {
        ChargeEntity chargeEntity = ChargeCache.getInstance().getChargeEntity(playerId);
        if (chargeEntity == null) {
            chargeEntity = ChargeDao.getInstance().getChargeEntityByPlayerId(playerId);
        }
        // 月卡结束时间
        Date now = new Date();
        Date yuekaEndTime = DateUtils.addMonths(now, 1);
        if (chargeEntity == null) {
            chargeEntity = new ChargeEntity();
            chargeEntity.setPlayerId(playerId);
            chargeEntity.setFirstPayTime(now);
            chargeEntity.setGzYuekaEndTime(yuekaEndTime);
            ChargeDao.getInstance().addChargeEntity(chargeEntity);
        } else {
            chargeEntity.setGzYuekaEndTime(yuekaEndTime);
            ChargeDao.getInstance().updateCharge(chargeEntity);
        }
    }

    /**
     * 至尊月卡
     *
     * @param playingRole
     */
    public void addZzYueka(PlayingRole playingRole) {
        int playerId = playingRole.getId();
        ChargeEntity chargeEntity = ChargeCache.getInstance().getChargeEntity(playerId);
        Date now = new Date();
        if (chargeEntity == null) {
            chargeEntity = new ChargeEntity();
            chargeEntity.setPlayerId(playerId);
            chargeEntity.setFirstPayTime(now);
            // 月卡结束时间
            Date yuekaEndTime = DateUtils.addMonths(now, 1);
            chargeEntity.setZzYuekaEndTime(yuekaEndTime);
            ChargeDao.getInstance().addChargeEntity(chargeEntity);
            ChargeCache.getInstance().addNewEntity(chargeEntity);
        } else {
            Date oldEndTime = chargeEntity.getZzYuekaEndTime();
            Date yuekaEndTime = null;
            if (oldEndTime != null && oldEndTime.after(now)) {
                yuekaEndTime = DateUtils.addMonths(oldEndTime, 1);
            } else {
                yuekaEndTime = DateUtils.addMonths(now, 1);
            }
            chargeEntity.setZzYuekaEndTime(yuekaEndTime);
            ChargeDao.getInstance().updateCharge(chargeEntity);
        }
        // ret
        sendPaymentInfo(playingRole);
    }

    public void addZzYuekaOffline(Integer playerId) {
        ChargeEntity chargeEntity = ChargeCache.getInstance().getChargeEntity(playerId);
        if (chargeEntity == null) {
            chargeEntity = ChargeDao.getInstance().getChargeEntityByPlayerId(playerId);
        }
        // 月卡结束时间
        Date now = new Date();
        Date yuekaEndTime = DateUtils.addMonths(now, 1);
        if (chargeEntity == null) {
            chargeEntity = new ChargeEntity();
            chargeEntity.setPlayerId(playerId);
            chargeEntity.setFirstPayTime(now);
            chargeEntity.setZzYuekaEndTime(yuekaEndTime);
            ChargeDao.getInstance().addChargeEntity(chargeEntity);
        } else {
            chargeEntity.setZzYuekaEndTime(yuekaEndTime);
            ChargeDao.getInstance().updateCharge(chargeEntity);
        }
    }

    public void addBw(PlayingRole playingRole) {
        int playerId = playingRole.getId();
        ChargeEntity chargeEntity = ChargeCache.getInstance().getChargeEntity(playerId);
        Date now = new Date();
        if (chargeEntity == null) {
            chargeEntity = new ChargeEntity();
            chargeEntity.setPlayerId(playerId);
            chargeEntity.setFirstPayTime(now);
            // 月卡结束时间
            Date yuekaEndTime = DateUtils.addMonths(now, 1);
            chargeEntity.setBwEndTime(yuekaEndTime);
            ChargeDao.getInstance().addChargeEntity(chargeEntity);
            ChargeCache.getInstance().addNewEntity(chargeEntity);
        } else {
            Date oldEndTime = chargeEntity.getBwEndTime();
            Date yuekaEndTime = null;
            if (oldEndTime != null && oldEndTime.after(now)) {
                yuekaEndTime = DateUtils.addMonths(oldEndTime, 1);
            } else {
                yuekaEndTime = DateUtils.addMonths(now, 1);
            }
            chargeEntity.setBwEndTime(yuekaEndTime);
            ChargeDao.getInstance().updateCharge(chargeEntity);
        }
        // ret
        sendPaymentInfo(playingRole);
    }

    public void addBwOffline(int playerId) {
        ChargeEntity chargeEntity = ChargeCache.getInstance().getChargeEntity(playerId);
        if (chargeEntity == null) {
            chargeEntity = ChargeDao.getInstance().getChargeEntityByPlayerId(playerId);
        }
        // 月卡结束时间
        Date now = new Date();
        Date yuekaEndTime = DateUtils.addMonths(now, 1);
        if (chargeEntity == null) {
            chargeEntity = new ChargeEntity();
            chargeEntity.setPlayerId(playerId);
            chargeEntity.setFirstPayTime(now);
            chargeEntity.setBwEndTime(yuekaEndTime);
            ChargeDao.getInstance().addChargeEntity(chargeEntity);
        } else {
            chargeEntity.setBwEndTime(yuekaEndTime);
            ChargeDao.getInstance().updateCharge(chargeEntity);
        }
    }

    public void addQz(PlayingRole playingRole) {
        int playerId = playingRole.getId();
        ChargeEntity chargeEntity = ChargeCache.getInstance().getChargeEntity(playerId);
        Date now = new Date();
        if (chargeEntity == null) {
            chargeEntity = new ChargeEntity();
            chargeEntity.setPlayerId(playerId);
            chargeEntity.setFirstPayTime(now);
            // 月卡结束时间
            Date yuekaEndTime = DateUtils.addMonths(now, 1);
            chargeEntity.setQzEndTime(yuekaEndTime);
            ChargeDao.getInstance().addChargeEntity(chargeEntity);
            ChargeCache.getInstance().addNewEntity(chargeEntity);
        } else {
            Date oldEndTime = chargeEntity.getQzEndTime();
            Date yuekaEndTime = null;
            if (oldEndTime != null && oldEndTime.after(now)) {
                yuekaEndTime = DateUtils.addMonths(oldEndTime, 1);
            } else {
                yuekaEndTime = DateUtils.addMonths(now, 1);
            }
            chargeEntity.setQzEndTime(yuekaEndTime);
            ChargeDao.getInstance().updateCharge(chargeEntity);
        }
        // ret
        sendPaymentInfo(playingRole);
    }

    public void addCz(PlayingRole playingRole) {
        int playerId = playingRole.getId();
        ChargeEntity chargeEntity = ChargeCache.getInstance().getChargeEntity(playerId);
        Date now = new Date();
        if (chargeEntity == null) {
            chargeEntity = new ChargeEntity();
            chargeEntity.setPlayerId(playerId);
            chargeEntity.setFirstPayTime(now);
            chargeEntity.setCzjj(true);
            ChargeDao.getInstance().addChargeEntity(chargeEntity);
            ChargeCache.getInstance().addNewEntity(chargeEntity);
        } else {
            chargeEntity.setCzjj(true);
            ChargeDao.getInstance().updateCharge(chargeEntity);
        }
        // ret
        sendPaymentInfo(playingRole);
    }

    public void addQzOffline(int playerId) {
        ChargeEntity chargeEntity = ChargeCache.getInstance().getChargeEntity(playerId);
        if (chargeEntity == null) {
            chargeEntity = ChargeDao.getInstance().getChargeEntityByPlayerId(playerId);
        }
        // 月卡结束时间
        Date now = new Date();
        Date yuekaEndTime = DateUtils.addMonths(now, 1);
        if (chargeEntity == null) {
            chargeEntity = new ChargeEntity();
            chargeEntity.setPlayerId(playerId);
            chargeEntity.setFirstPayTime(now);
            chargeEntity.setQzEndTime(yuekaEndTime);
            ChargeDao.getInstance().addChargeEntity(chargeEntity);
        } else {
            chargeEntity.setQzEndTime(yuekaEndTime);
            ChargeDao.getInstance().updateCharge(chargeEntity);
        }
    }

    public void addYd(PlayingRole playingRole) {
        int playerId = playingRole.getId();
        ChargeEntity chargeEntity = ChargeCache.getInstance().getChargeEntity(playerId);
        Date now = new Date();
        if (chargeEntity == null) {
            chargeEntity = new ChargeEntity();
            chargeEntity.setPlayerId(playerId);
            chargeEntity.setFirstPayTime(now);
            // 月卡结束时间
            Date yuekaEndTime = DateUtils.addMonths(now, 1);
            chargeEntity.setYdEndTime(yuekaEndTime);
            ChargeDao.getInstance().addChargeEntity(chargeEntity);
            ChargeCache.getInstance().addNewEntity(chargeEntity);
        } else {
            Date oldEndTime = chargeEntity.getYdEndTime();
            Date yuekaEndTime = null;
            if (oldEndTime != null && oldEndTime.after(now)) {
                yuekaEndTime = DateUtils.addMonths(oldEndTime, 1);
            } else {
                yuekaEndTime = DateUtils.addMonths(now, 1);
            }
            chargeEntity.setYdEndTime(yuekaEndTime);
            ChargeDao.getInstance().updateCharge(chargeEntity);
        }
        // ret
        sendPaymentInfo(playingRole);
    }

    public void addJt(PlayingRole playingRole) {
        int playerId = playingRole.getId();
        ChargeEntity chargeEntity = ChargeCache.getInstance().getChargeEntity(playerId);
        Date now = new Date();
        if (chargeEntity == null) {
            chargeEntity = new ChargeEntity();
            chargeEntity.setPlayerId(playerId);
            chargeEntity.setFirstPayTime(now);
            // 月卡结束时间
            Date yuekaEndTime = DateUtils.addMonths(now, 1);
            chargeEntity.setJtEndTime(yuekaEndTime);
            ChargeDao.getInstance().addChargeEntity(chargeEntity);
            ChargeCache.getInstance().addNewEntity(chargeEntity);
        } else {
            Date oldEndTime = chargeEntity.getJtEndTime();
            Date yuekaEndTime = null;
            if (oldEndTime != null && oldEndTime.after(now)) {
                yuekaEndTime = DateUtils.addMonths(oldEndTime, 1);
            } else {
                yuekaEndTime = DateUtils.addMonths(now, 1);
            }
            chargeEntity.setJtEndTime(yuekaEndTime);
            ChargeDao.getInstance().updateCharge(chargeEntity);
        }
        // ret
        sendPaymentInfo(playingRole);
    }

    public void addYdOffline(int playerId) {
        ChargeEntity chargeEntity = ChargeCache.getInstance().getChargeEntity(playerId);
        if (chargeEntity == null) {
            chargeEntity = ChargeDao.getInstance().getChargeEntityByPlayerId(playerId);
        }
        // 月卡结束时间
        Date now = new Date();
        Date yuekaEndTime = DateUtils.addMonths(now, 1);
        if (chargeEntity == null) {
            chargeEntity = new ChargeEntity();
            chargeEntity.setPlayerId(playerId);
            chargeEntity.setFirstPayTime(now);
            chargeEntity.setYdEndTime(yuekaEndTime);
            ChargeDao.getInstance().addChargeEntity(chargeEntity);
        } else {
            chargeEntity.setYdEndTime(yuekaEndTime);
            ChargeDao.getInstance().updateCharge(chargeEntity);
        }
    }

    /**
     * 招贤纳士 武将位加成
     * @param playerId
     * @return
     */
    public int getZxnsAddon(int playerId) {
        ChargeEntity chargeEntity = ChargeCache.getInstance().getChargeEntity(playerId);
        if (chargeEntity != null && chargeEntity.getZxns() != null && chargeEntity.getZxns()) {
            return 25;
        }
        return 0;
    }

    public void addZxns(PlayingRole playingRole) {
        int playerId = playingRole.getId();
        ChargeEntity chargeEntity = ChargeCache.getInstance().getChargeEntity(playerId);
        Date now = new Date();
        if (chargeEntity == null) {
            chargeEntity = new ChargeEntity();
            chargeEntity.setPlayerId(playerId);
            chargeEntity.setFirstPayTime(now);
            chargeEntity.setZxns(true);
            ChargeDao.getInstance().addChargeEntity(chargeEntity);
            ChargeCache.getInstance().addNewEntity(chargeEntity);
        } else {
            chargeEntity.setZxns(true);
            ChargeDao.getInstance().updateCharge(chargeEntity);
        }
        // ret
        sendPaymentInfo(playingRole);
    }

    /**
     * 勤政爱民特权加成
     * @param playerId
     * @return
     */
    public int getQzAddon(int playerId) {
        ChargeEntity chargeEntity = ChargeCache.getInstance().getChargeEntity(playerId);
        Date now = new Date();
        if (chargeEntity != null && chargeEntity.getQzEndTime() != null && chargeEntity.getQzEndTime().after(now)) {
            return 1;
        }
        return 0;
    }

    /**
     * 军团加成
     * @param playerId
     * @return
     */
    public int getJtAddon(int playerId) {
        ChargeEntity chargeEntity = ChargeCache.getInstance().getChargeEntity(playerId);
        Date now = new Date();
        if (chargeEntity != null && chargeEntity.getJtEndTime() != null && chargeEntity.getJtEndTime().after(now)) {
            return 20;
        }
        return 0;
    }

    public boolean isBwAddon(int playerId) {
        ChargeEntity chargeEntity = ChargeCache.getInstance().getChargeEntity(playerId);
        Date now = new Date();
        if (chargeEntity != null && chargeEntity.getBwEndTime() != null && chargeEntity.getBwEndTime().after(now)) {
            return true;
        }
        return false;
//        return true;
    }

    public boolean isYdAddon(int playerId) {
        ChargeEntity chargeEntity = ChargeCache.getInstance().getChargeEntity(playerId);
        Date now = new Date();
        if (chargeEntity != null && chargeEntity.getYdEndTime() != null && chargeEntity.getYdEndTime().after(now)) {
            return true;
        }
        return false;
//        return true;
    }

    public void levelUpAddCzjj(int oldLelvel, int newLevel, int playerId) {
        ChargeEntity chargeEntity = ChargeCache.getInstance().getChargeEntity(playerId);
        if (chargeEntity == null || chargeEntity.getCzjj() == null || !chargeEntity.getCzjj()) {
            return;
        }
        List<MActivityGpTemplate> templateMap = MActivityGpTemplateCache.getInstance().getTemplateMap();
        for (MActivityGpTemplate mActivityGpTemplate : templateMap) {
            if (oldLelvel < mActivityGpTemplate.getLEVEL() && newLevel >= mActivityGpTemplate.getLEVEL()) {
                int ybNum = mActivityGpTemplate.getYBNUM();
                Map<Integer, Integer> mailAtt = new HashMap<>();
                mailAtt.put(GameConfig.PLAYER.YB, ybNum);
                String mailTitle = "Phần thưởng hoạt động tăng trưởng quỹ"; //"成长基金购买奖励"
                String mailContent = String.format("Bạn hoàn thành cấp độ: %1$d, phần thưởng như sau:", mActivityGpTemplate.getLEVEL()); //String.format("您完成了等级：%1$d，奖励内容如下：", mActivityGpTemplate.getLEVEL());
                MailManager.getInstance().sendSysMailToSingle(playerId, mailTitle, mailContent, mailAtt);
                break;
            }
        }
    }

}
