package cc.mrbird.febs.system.service;

import cc.mrbird.febs.common.CommonUtils;
import cc.mrbird.febs.common.ItemNotExistException;
import cc.mrbird.febs.system.entity.Activity4Gm;
import cc.mrbird.febs.system.entity.GsEntity;
import cc.mrbird.febs.system.entity.User;
import cc.mrbird.febs.system.service.lan.GmLanManager;
import cc.mrbird.febs.template.ActivityTemplateCache;
import cc.mrbird.febs.template.ItemTemplateCache;
import game.module.activity.bean.*;
import game.module.template.ActivityTemplate;
import io.protostuff.ProtostuffIOUtil;
import lion.common.StringConstants;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class ActivityService {

	private static Logger logger = LoggerFactory.getLogger(ActivityService.class);

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Autowired
	private GmLanManager gmLanManager;

	@Autowired
	private GsService gsConfigService;

	public ActivityRetMsg rechargeAward(Activity4Gm activity4Gm, Model model) {
		try {
			// Thời gian bắt đầu và thời gian kết thúc xử lý
			int templateId = activity4Gm.getTemplateId();
			ActivityTemplate at = ActivityTemplateCache.getInstance().getActivityTemplate(templateId);
			setTime(activity4Gm, at);
			// Có nên bật không
			String isOpenStr = activity4Gm.getIsOpenStr();
			if (isOpenStr != null && isOpenStr.equals("on")) {
				activity4Gm.setIsOpen(1);
			} else {
				activity4Gm.setIsOpen(0);
			}
			// params
			String paramStr = activity4Gm.getParamsStr();
			String[] paramStrList = paramStr.split("\n");
			if (paramStrList.length % 2 != 0) {
				return new ActivityRetMsg("params error!", false);
			}
			DBRechargeAward dbRechargeAward = new DBRechargeAward();
			List<ActivityConfigOne> awardOnes = new ArrayList<ActivityConfigOne>();
			dbRechargeAward.setItemsList(awardOnes);
			for (int i = 0; i < paramStrList.length; i += 2) {
				int rechargeLevel = Integer.valueOf(paramStrList[i]);
				ActivityConfigOne rechargeAwardOne = new ActivityConfigOne(rechargeLevel);
				String awardStr = paramStrList[i + 1];
				String[] awardStrList = StringUtils.split(awardStr, StringConstants.SEPARATOR_SHU);
				List<Award> awardList = new ArrayList<Award>();
				rechargeAwardOne.setAwardList(awardList);
				for (String awardStrPair : awardStrList) {
					String[] awardPairList = StringUtils.split(awardStrPair, StringConstants.SEPARATOR_HENG);
					int itemTplId = Integer.valueOf(awardPairList[0]);
					boolean itemTplExist = ItemTemplateCache.getInstance().checkExist(itemTplId);
					if (!itemTplExist) {
						throw new ItemNotExistException(itemTplId);
					}
					int itemCount = Integer.valueOf(awardPairList[1]);
					Award dbAward = new Award(itemTplId, itemCount);
					awardList.add(dbAward);
				}
				awardOnes.add(rechargeAwardOne);
			}
			// params to byte[]
			byte[] paramsBytes = ProtostuffIOUtil.toByteArray(dbRechargeAward, DBRechargeAward.getSchema(),
					CommonUtils.getProtoBuffer());
			activity4Gm.setParams(paramsBytes);
			// send to Game Server
			send2GameServer(model, activity4Gm);
			return new ActivityRetMsg("", true);
		} catch (ParseException e) {
			logger.error("", e);
			return new ActivityRetMsg("wrong time format!" + e.getMessage(), false);
		} catch (NumberFormatException e) {
			logger.error("", e);
			return new ActivityRetMsg("wrong number format!" + e.getMessage(), false);
		} catch (ArrayIndexOutOfBoundsException e) {
			logger.error("", e);
			return new ActivityRetMsg("wrong award format!" + e.getMessage(), false);
		} catch (ItemNotExistException e) {
			logger.error("", e);
			return new ActivityRetMsg("wrong award format!item not exist:" + e.getMessage(), false);
		}
	}

	/**
	 * 处理开始时间和结束时间
	 * 
	 * @param activity4Gm
	 * @throws ParseException
	 */
	private void setTime(Activity4Gm activity4Gm, ActivityTemplate at) throws ParseException {
		if (at.getType() == 2) {
			String startTimeStr = activity4Gm.getStartDateStr() + " " + activity4Gm.getStartTimeStr();
			String endTimeStr = activity4Gm.getEndDateStr() + " " + activity4Gm.getEndTimeStr();
			Date startTime = sdf.parse(startTimeStr);
			Date endTime = sdf.parse(endTimeStr);
			activity4Gm.setStartTime(startTime);
			activity4Gm.setEndTime(endTime);
		}
	}

	public static final class ActivityRetMsg {
		private String retPath;
		private Boolean isSuccess;

		public ActivityRetMsg(String retPath, Boolean isSuccess) {
			super();
			this.retPath = retPath;
			this.isSuccess = isSuccess;
		}

		public String getRetPath() {
			return retPath;
		}

		public void setRetPath(String retPath) {
			this.retPath = retPath;
		}

		public Boolean getIsSuccess() {
			return isSuccess;
		}

		public void setIsSuccess(Boolean isSuccess) {
			this.isSuccess = isSuccess;
		}

	}

	public String extractRechargeParams(byte[] params) {
		if (params.length == 0) {
			return StringUtils.EMPTY;
		}
		DBRechargeAward dbRechargeAward = new DBRechargeAward();
		ProtostuffIOUtil.mergeFrom(params, dbRechargeAward, DBRechargeAward.getSchema());
		List<ActivityConfigOne> awardOnes = dbRechargeAward.getItemsList();
		List<String> awardAllList = new ArrayList<String>();
		for (ActivityConfigOne rechargeAwardOne : awardOnes) {
			int rechargeCount = rechargeAwardOne.getLevel();
			awardAllList.add(String.valueOf(rechargeCount));
			List<Award> awards = rechargeAwardOne.getAwardList();
			List<String> awardStrList = new ArrayList<String>();
			for (Award award : awards) {
				String awardStr = StringUtils.join(new int[] { award.getItemId(), award.getItemCount() }, '-');
				awardStrList.add(awardStr);
			}
			String awardStrAll = StringUtils.join(awardStrList, '|');
			awardAllList.add(awardStrAll);
		}
		return StringUtils.join(awardAllList, "\n");
	}

	public ActivityRetMsg consumeAward(Activity4Gm activity4Gm, Model model) {
		try {
			// 处理开始时间和结束时间
			int templateId = activity4Gm.getTemplateId();
			ActivityTemplate at = ActivityTemplateCache.getInstance().getActivityTemplate(templateId);
			setTime(activity4Gm, at);
			// 是否开启
			String isOpenStr = activity4Gm.getIsOpenStr();
			if (isOpenStr != null && isOpenStr.equals("on")) {
				activity4Gm.setIsOpen(1);
			} else {
				activity4Gm.setIsOpen(0);
			}
			// params
			String paramStr = activity4Gm.getParamsStr();
			String[] paramStrList = paramStr.split("\n");
			if (paramStrList.length % 2 != 0) {
				return new ActivityRetMsg("params error!", false);
			}
			DBActivityConsume dbActivityConsume = new DBActivityConsume();
			List<ActivityConfigOne> awardOnes = new ArrayList<ActivityConfigOne>();
			dbActivityConsume.setItemsList(awardOnes);
			for (int i = 0; i < paramStrList.length; i += 2) {
				int rechargeLevel = Integer.valueOf(paramStrList[i]);
				ActivityConfigOne rechargeAwardOne = new ActivityConfigOne(rechargeLevel);
				String awardStr = paramStrList[i + 1];
				String[] awardStrList = StringUtils.split(awardStr, StringConstants.SEPARATOR_SHU);
				List<Award> awardList = new ArrayList<Award>();
				rechargeAwardOne.setAwardList(awardList);
				for (String awardStrPair : awardStrList) {
					String[] awardPairList = StringUtils.split(awardStrPair, StringConstants.SEPARATOR_HENG);
					int itemTplId = Integer.valueOf(awardPairList[0]);
					boolean itemTplExist = ItemTemplateCache.getInstance().checkExist(itemTplId);
					if (!itemTplExist) {
						throw new ItemNotExistException(itemTplId);
					}
					int itemCount = Integer.valueOf(awardPairList[1]);
					Award dbAward = new Award(itemTplId, itemCount);
					awardList.add(dbAward);
				}
				awardOnes.add(rechargeAwardOne);
			}
			// params to byte[]
			byte[] paramsBytes = ProtostuffIOUtil.toByteArray(dbActivityConsume, DBActivityConsume.getSchema(),
					CommonUtils.getProtoBuffer());
			activity4Gm.setParams(paramsBytes);
			// send to Game Server
			send2GameServer(model, activity4Gm);
			return new ActivityRetMsg("", true);
		} catch (ParseException e) {
			logger.error("", e);
			return new ActivityRetMsg("wrong time format!" + e.getMessage(), false);
		} catch (NumberFormatException e) {
			logger.error("", e);
			return new ActivityRetMsg("wrong number format!" + e.getMessage(), false);
		} catch (ArrayIndexOutOfBoundsException e) {
			logger.error("", e);
			return new ActivityRetMsg("wrong award format!" + e.getMessage(), false);
		} catch (ItemNotExistException e) {
			logger.error("", e);
			return new ActivityRetMsg("wrong award format!item not exist:" + e.getMessage(), false);
		}
	}

	public ActivityRetMsg loginAward(Activity4Gm activity4Gm, Model model) {
		try {
			// 处理开始时间和结束时间
			int templateId = activity4Gm.getTemplateId();
			ActivityTemplate at = ActivityTemplateCache.getInstance().getActivityTemplate(templateId);
			setTime(activity4Gm, at);
			// 是否开启
			String isOpenStr = activity4Gm.getIsOpenStr();
			if (isOpenStr != null && isOpenStr.equals("on")) {
				activity4Gm.setIsOpen(1);
			} else {
				activity4Gm.setIsOpen(0);
			}
			// params
			String paramStr = activity4Gm.getParamsStr();
			String[] paramStrList = paramStr.split("\n");
			if (paramStrList.length % 2 != 0) {
				return new ActivityRetMsg("params error!", false);
			}
			DBActivityLogin dbActivityLogin = new DBActivityLogin();
			List<ActivityConfigOne> awardOnes = new ArrayList<ActivityConfigOne>();
			dbActivityLogin.setItemsList(awardOnes);
			// dbActivityLogin.setDayCount(activity4Gm.getValidDayCount());
			for (int i = 0; i < paramStrList.length; i += 2) {
				int rechargeLevel = Integer.valueOf(paramStrList[i]);
				ActivityConfigOne rechargeAwardOne = new ActivityConfigOne(rechargeLevel);
				String awardStr = paramStrList[i + 1];
				String[] awardStrList = StringUtils.split(awardStr, StringConstants.SEPARATOR_SHU);
				List<Award> awardList = new ArrayList<Award>();
				rechargeAwardOne.setAwardList(awardList);
				for (String awardStrPair : awardStrList) {
					String[] awardPairList = StringUtils.split(awardStrPair, StringConstants.SEPARATOR_HENG);
					int itemTplId = Integer.valueOf(awardPairList[0]);
					boolean itemTplExist = ItemTemplateCache.getInstance().checkExist(itemTplId);
					if (!itemTplExist) {
						throw new ItemNotExistException(itemTplId);
					}
					int itemCount = Integer.valueOf(awardPairList[1]);
					Award dbAward = new Award(itemTplId, itemCount);
					awardList.add(dbAward);
				}
				awardOnes.add(rechargeAwardOne);
			}
			// params to byte[]
			byte[] paramsBytes = ProtostuffIOUtil.toByteArray(dbActivityLogin, DBActivityLogin.getSchema(),
					CommonUtils.getProtoBuffer());
			activity4Gm.setParams(paramsBytes);
			// send to Game Server
			send2GameServer(model, activity4Gm);
			return new ActivityRetMsg("", true);
		} catch (ParseException e) {
			logger.error("", e);
			return new ActivityRetMsg("wrong time format!" + e.getMessage(), false);
		} catch (NumberFormatException e) {
			logger.error("", e);
			return new ActivityRetMsg("wrong number format!" + e.getMessage(), false);
		} catch (ArrayIndexOutOfBoundsException e) {
			logger.error("", e);
			return new ActivityRetMsg("wrong award format!" + e.getMessage(), false);
		} catch (ItemNotExistException e) {
			logger.error("", e);
			return new ActivityRetMsg("wrong award format!item not exist:" + e.getMessage(), false);
		}
	}

	public Object[] extractLoginParams(byte[] params) {
		if (params.length == 0) {
			return null;
		}
		DBActivityLogin dbActivityLogin = new DBActivityLogin();
		ProtostuffIOUtil.mergeFrom(params, dbActivityLogin, DBActivityLogin.getSchema());
		List<ActivityConfigOne> awardOnes = dbActivityLogin.getItemsList();
		List<String> awardAllList = new ArrayList<String>();
		for (ActivityConfigOne rechargeAwardOne : awardOnes) {
			int rechargeCount = rechargeAwardOne.getLevel();
			awardAllList.add(String.valueOf(rechargeCount));
			List<Award> awards = rechargeAwardOne.getAwardList();
			List<String> awardStrList = new ArrayList<String>();
			for (Award award : awards) {
				String awardStr = StringUtils.join(new int[] { award.getItemId(), award.getItemCount() }, '-');
				awardStrList.add(awardStr);
			}
			String awardStrAll = StringUtils.join(awardStrList, '|');
			awardAllList.add(awardStrAll);
		}
		String paramsStr = StringUtils.join(awardAllList, "\n");
		int validDays = 0;
		return new Object[] { paramsStr, validDays };
	}

	public String extractCunsumeParams(byte[] params) {
		if (params.length == 0) {
			return StringUtils.EMPTY;
		}
		DBActivityConsume dbActivityConsume = new DBActivityConsume();
		ProtostuffIOUtil.mergeFrom(params, dbActivityConsume, DBActivityConsume.getSchema());
		List<ActivityConfigOne> awardOnes = dbActivityConsume.getItemsList();
		List<String> awardAllList = new ArrayList<String>();
		for (ActivityConfigOne rechargeAwardOne : awardOnes) {
			int rechargeCount = rechargeAwardOne.getLevel();
			awardAllList.add(String.valueOf(rechargeCount));
			List<Award> awards = rechargeAwardOne.getAwardList();
			List<String> awardStrList = new ArrayList<String>();
			for (Award award : awards) {
				String awardStr = StringUtils.join(new int[] { award.getItemId(), award.getItemCount() }, '-');
				awardStrList.add(awardStr);
			}
			String awardStrAll = StringUtils.join(awardStrList, '|');
			awardAllList.add(awardStrAll);
		}
		return StringUtils.join(awardAllList, "\n");
	}

	/**
	 * 答题
	 * 
	 * @param params
	 * @return
	 */
	public Object[] extractAnswerParams(byte[] params) {
		if (params.length == 0) {
			return null;
		}
		DBActivityAnswerAward dbActivityAnswerAward = new DBActivityAnswerAward();
		ProtostuffIOUtil.mergeFrom(params, dbActivityAnswerAward, DBActivityAnswerAward.getSchema());
		int totalAnswerCnt = dbActivityAnswerAward.getTotalAnswerCnt();
		String paramStr = StringUtils.join(String.valueOf(totalAnswerCnt), "\n");
		int totalAwardAnswerCnt = dbActivityAnswerAward.getTotalAwardAnswerCnt();
		String answerCnts = StringUtils.join(paramStr, String.valueOf(totalAwardAnswerCnt));

		return new Object[] { answerCnts, dbActivityAnswerAward.getOpenTime(), dbActivityAnswerAward.getCloseTime() };
	}

	/**
	 * 冲级奖励, 参数解析
	 * 
	 * @param params
	 * @return
	 */
	public Object[] extractLevelGoParams(byte[] params) {
		if (params.length == 0) {
			return null;
		}
		DBActivityLevelAward dbActivityLevel = new DBActivityLevelAward();
		ProtostuffIOUtil.mergeFrom(params, dbActivityLevel, DBActivityLevelAward.getSchema());
		List<LevelAward> awardOnes = dbActivityLevel.getLevelAwardList();
		List<String> awardAllList = new ArrayList<String>();
		for (LevelAward levelAward : awardOnes) {
			int level = levelAward.getLevel();
			awardAllList.add(String.valueOf(level));

			List<Award> awards = levelAward.getAwardList();
			List<String> awardStrList = new ArrayList<String>();
			for (Award award : awards) {
				String awardStr = StringUtils.join(new int[] { award.getItemId(), award.getItemCount() }, '-');
				awardStrList.add(awardStr);
			}
			String awardStrAll = StringUtils.join(awardStrList, '|');
			awardAllList.add(awardStrAll);
		}
		String paramsStr = StringUtils.join(awardAllList, "\n");
		int validDays = dbActivityLevel.getDayCount();
		return new Object[] { paramsStr, validDays };
	}

	/**
	 * 奖励翻倍, 参数解析
	 * 
	 * @param params
	 * @return
	 */
	public String extractAwardDoubleParams(byte[] params) {
		if (params.length == 0) {
			return StringUtils.EMPTY;
		}
		DBActivityAwardDouble dbActivityAwardDouble = new DBActivityAwardDouble();
		ProtostuffIOUtil.mergeFrom(params, dbActivityAwardDouble, DBActivityAwardDouble.getSchema());
		List<AwardDouble> awardOnes = dbActivityAwardDouble.getAwardDoubleList();
		List<String> awardAllList = new ArrayList<String>();
		for (AwardDouble awardDouble : awardOnes) {
			int moudle = awardDouble.getMoudelType();
			awardAllList.add(String.valueOf(moudle));
		}
		return StringUtils.join(awardAllList, "\n");
	}

	/**
	 * 钻石矿井, 参数解析
	 * 
	 * @param params
	 * @return
	 */
	public Object[] extractMineParams(byte[] params) {
		if (params.length == 0) {
			return null;
		}
		DBActivityMineAward dbActivityMineAward = new DBActivityMineAward();
		ProtostuffIOUtil.mergeFrom(params, dbActivityMineAward, DBActivityMineAward.getSchema());
		List<MineAward> awardOnes = dbActivityMineAward.getMineAwardList();
		List<String> awardAllList = new ArrayList<String>();
		for (MineAward mineAward : awardOnes) {
			int count = mineAward.getCount();
			awardAllList.add(String.valueOf(count));
			String awardDiamond = StringUtils
					.join(new int[] { mineAward.getAwardDiamond1(), mineAward.getAwardDiamond2() }, ':');
			String awardStr = StringUtils
					.join(new String[] { String.valueOf(mineAward.getCostDiamond()), awardDiamond }, '-');
			awardAllList.add(awardStr);
		}
		String paramsStr = StringUtils.join(awardAllList, "\n");
		int validDays = dbActivityMineAward.getDayCount() == null ? 0 : dbActivityMineAward.getDayCount();
		return new Object[] { paramsStr, validDays };
	}

	/**
	 * 折扣季, 参数解析
	 * 
	 * @param params
	 * @return
	 */
	public String extractSale(byte[] params) {
		if (params.length == 0) {
			return StringUtils.EMPTY;
		}
		DBActivitySale dbActivitySale = new DBActivitySale();
		ProtostuffIOUtil.mergeFrom(params, dbActivitySale, DBActivitySale.getSchema());
		List<Sale> sales = dbActivitySale.getSaleList();
		List<String> awardAllList = new ArrayList<String>();
		for (Sale sale : sales) {
			awardAllList.add(String.valueOf(sale.getStoreType()));
			awardAllList.add(String.valueOf(sale.getSaleResult()));
		}
		return StringUtils.join(awardAllList, "\n");
	}

	/**
	 * 答题
	 * 
	 * @param activity4Gm
	 * @param model
	 * @return
	 */
	public ActivityRetMsg answerAward(Activity4Gm activity4Gm, Model model) {
		try {
			// 开始时间、结束时间
			int templateId = activity4Gm.getTemplateId();
			ActivityTemplate at = ActivityTemplateCache.getInstance().getActivityTemplate(templateId);
			setTime(activity4Gm, at);

			// 是否开启
			String isOpenStr = activity4Gm.getIsOpenStr();
			if (isOpenStr != null && isOpenStr.equals("on")) {
				activity4Gm.setIsOpen(1);
			} else {
				activity4Gm.setIsOpen(0);
			}

			// Params
			DBActivityAnswerAward dbActivityAnswerAward = new DBActivityAnswerAward();
			String paramStr = activity4Gm.getParamsStr();
			if (StringUtils.isNotEmpty(paramStr)) {
				String[] paramStrList = paramStr.split("\n");
				dbActivityAnswerAward.setTotalAnswerCnt(Integer.valueOf(paramStrList[0]));
				dbActivityAnswerAward.setTotalAwardAnswerCnt(Integer.valueOf(paramStrList[1]));
				dbActivityAnswerAward.setOpenTime(Integer.valueOf(activity4Gm.getOpenTime()));
				dbActivityAnswerAward.setCloseTime(Integer.valueOf(activity4Gm.getCloseTime()));
			}
			// params to byte[]
			byte[] paramsBytes = ProtostuffIOUtil.toByteArray(dbActivityAnswerAward, DBActivityAnswerAward.getSchema(),
					CommonUtils.getProtoBuffer());
			activity4Gm.setParams(paramsBytes);

			// send to Game Server
			send2GameServer(model, activity4Gm);
			return new ActivityRetMsg("", true);

		} catch (ParseException e) {
			logger.error("", e);
			return new ActivityRetMsg("wrong time format!", false);
		}
	}

	private void send2GameServer(Model model, Activity4Gm activity4Gm) {
		User user = (User) SecurityUtils.getSubject().getPrincipal();
		if (user.getToSendGsIds() != null) {
			List<Integer> gsIdList = user.getToSendGsIds();
			List<GsEntity> gsAll = gsConfigService.getGsList();
			for (GsEntity gsEntity : gsAll) {
				if (gsIdList.contains(gsEntity.getId())) {
					boolean connectRet = gmLanManager.connect(gsEntity);
					if (connectRet) {
						gmLanManager.updateActivity(gsEntity.getHost(), gsEntity.getPort(), activity4Gm);
					} else {
						model.addAttribute("message", "连接到游戏服务器异常!");
					}
				}
			}
		}
	}

	/**
	 * 冲级奖励
	 * 
	 * @param activity4Gm
	 * @param model
	 * @return
	 */
	public ActivityRetMsg levelGoAward(Activity4Gm activity4Gm, Model model) {

		try {
			// 处理开始时间和结束时间
			int templateId = activity4Gm.getTemplateId();
			ActivityTemplate at = ActivityTemplateCache.getInstance().getActivityTemplate(templateId);
			setTime(activity4Gm, at);

			// 是否开启
			String isOpenStr = activity4Gm.getIsOpenStr();
			if (isOpenStr != null && isOpenStr.equals("on")) {
				activity4Gm.setIsOpen(1);
			} else {
				activity4Gm.setIsOpen(0);
			}

			// params
			String paramStr = activity4Gm.getParamsStr();
			String[] paramStrList = paramStr.split("\n");
			if (paramStrList.length % 2 != 0) {
				return new ActivityRetMsg("params error!", false);
			}
			DBActivityLevelAward dbLevelAward = new DBActivityLevelAward();
			List<LevelAward> awardOnes = new ArrayList<LevelAward>();
			dbLevelAward.setLevelAwardList(awardOnes);
			dbLevelAward.setDayCount(activity4Gm.getValidDayCount());
			for (int i = 0; i < paramStrList.length; i += 2) {
				LevelAward levelAward = new LevelAward();

				int level = Integer.valueOf(paramStrList[i]);
				levelAward.setLevel(level);

				List<Award> awardList = new ArrayList<Award>();
				levelAward.setAwardList(awardList);
				String awardStr = paramStrList[i + 1];
				String[] awardStrList = StringUtils.split(awardStr, StringConstants.SEPARATOR_SHU);
				for (String awardStrPair : awardStrList) {
					String[] awardPairList = StringUtils.split(awardStrPair, StringConstants.SEPARATOR_HENG);
					int itemTplId = Integer.valueOf(awardPairList[0]);
					boolean itemTplExist = ItemTemplateCache.getInstance().checkExist(itemTplId);
					if (!itemTplExist) {
						try {
							throw new ItemNotExistException(itemTplId);
						} catch (ItemNotExistException e) {
							e.printStackTrace();
						}
					}
					int itemCount = Integer.valueOf(awardPairList[1]);
					Award dbAward = new Award(itemTplId, itemCount);
					awardList.add(dbAward);
				}

				awardOnes.add(levelAward);
			}
			// params to byte[]
			byte[] paramsBytes = ProtostuffIOUtil.toByteArray(dbLevelAward, DBActivityLevelAward.getSchema(),
					CommonUtils.getProtoBuffer());
			activity4Gm.setParams(paramsBytes);

			// send to Game Server
			send2GameServer(model, activity4Gm);
			return new ActivityRetMsg("", true);
		} catch (ParseException e) {
			logger.error("", e);
			return new ActivityRetMsg("wrong time format!", false);
		}
	}

	/**
	 * 奖励翻倍
	 * 
	 * @param activity4Gm
	 * @param model
	 * @return
	 */
	public ActivityRetMsg awardDoubleAward(Activity4Gm activity4Gm, Model model) {
		try {
			// 开始时间、结束时间
			int templateId = activity4Gm.getTemplateId();
			ActivityTemplate at = ActivityTemplateCache.getInstance().getActivityTemplate(templateId);
			setTime(activity4Gm, at);

			// 是否开启
			String isOpenStr = activity4Gm.getIsOpenStr();
			if (isOpenStr != null && isOpenStr.equals("on")) {
				activity4Gm.setIsOpen(1);
			} else {
				activity4Gm.setIsOpen(0);
			}

			// params
			String paramStr = activity4Gm.getParamsStr();
			String[] paramStrList = paramStr.split("\n");
			DBActivityAwardDouble awardDouble = new DBActivityAwardDouble();
			List<AwardDouble> awardDoubles = new ArrayList<AwardDouble>(paramStrList.length);
			awardDouble.setAwardDoubleList(awardDoubles);
			for (String module : paramStrList) {
				AwardDouble award = new AwardDouble();
				award.setMoudelType(Integer.valueOf(module));
				award.setMultipleCnt(2);

				awardDoubles.add(award);
			}

			// params to byte[]
			byte[] paramsBytes = ProtostuffIOUtil.toByteArray(awardDouble, DBActivityAwardDouble.getSchema(),
					CommonUtils.getProtoBuffer());
			activity4Gm.setParams(paramsBytes);

			// send to Game Server
			send2GameServer(model, activity4Gm);
			return new ActivityRetMsg("", true);
		} catch (ParseException e) {
			logger.error("", e);
			return new ActivityRetMsg("wrong time format!", false);
		}
	}

	/**
	 * 钻石矿井
	 * 
	 * @param activity4Gm
	 * @param model
	 * @return
	 */
	public ActivityRetMsg mineAward(Activity4Gm activity4Gm, Model model) {
		try {
			// 开始时间、结束时间
			int templateId = activity4Gm.getTemplateId();
			ActivityTemplate at = ActivityTemplateCache.getInstance().getActivityTemplate(templateId);
			setTime(activity4Gm, at);

			// 是否开启
			String isOpenStr = activity4Gm.getIsOpenStr();
			if (isOpenStr != null && isOpenStr.equals("on")) {
				activity4Gm.setIsOpen(1);
			} else {
				activity4Gm.setIsOpen(0);
			}

			// params
			String paramStr = activity4Gm.getParamsStr();
			String[] paramStrList = paramStr.split("\n");
			if (paramStrList.length % 2 != 0) {
				return new ActivityRetMsg("params error!", false);
			}
			DBActivityMineAward mineAward = new DBActivityMineAward();
			List<MineAward> awardOnes = new ArrayList<MineAward>();
			mineAward.setMineAwardList(awardOnes);
			mineAward.setDayCount(0);

			for (int i = 0; i < paramStrList.length; i += 2) {
				MineAward award = new MineAward();

				int count = Integer.valueOf(paramStrList[i]);
				award.setCount(count);

				String awardStr = paramStrList[i + 1];
				String[] awardStrList = StringUtils.split(awardStr, StringConstants.SEPARATOR_HENG);
				award.setCostDiamond(Integer.valueOf(awardStrList[0]));
				String[] awardDiamonds = StringUtils.split(awardStrList[1], ":");
				award.setAwardDiamond1(Integer.valueOf(awardDiamonds[0]));
				award.setAwardDiamond2(Integer.valueOf(awardDiamonds[1]));

				awardOnes.add(award);
			}

			// params to byte[]
			byte[] paramsBytes = ProtostuffIOUtil.toByteArray(mineAward, DBActivityMineAward.getSchema(),
					CommonUtils.getProtoBuffer());
			activity4Gm.setParams(paramsBytes);

			// send to Game Server
			send2GameServer(model, activity4Gm);
			return new ActivityRetMsg("", true);
		} catch (ParseException e) {
			logger.error("", e);
			return new ActivityRetMsg("wrong time format!", false);
		}
	}

	/**
	 * 折扣季
	 * 
	 * @param activity4Gm
	 * @param model
	 * @return
	 */
	public ActivityRetMsg sale(Activity4Gm activity4Gm, Model model) {
		try {
			// 开始时间、结束时间
			int templateId = activity4Gm.getTemplateId();
			ActivityTemplate at = ActivityTemplateCache.getInstance().getActivityTemplate(templateId);
			setTime(activity4Gm, at);

			// 是否开启
			String isOpenStr = activity4Gm.getIsOpenStr();
			if (isOpenStr != null && isOpenStr.equals("on")) {
				activity4Gm.setIsOpen(1);
			} else {
				activity4Gm.setIsOpen(0);
			}

			// params
			String paramStr = activity4Gm.getParamsStr();
			String[] paramStrList = paramStr.split("\n");
			if (paramStrList.length % 2 != 0) {
				return new ActivityRetMsg("params error!", false);
			}

			DBActivitySale dbActivitySale = new DBActivitySale();
			List<Sale> awardDoubles = new ArrayList<Sale>();
			dbActivitySale.setSaleList(awardDoubles);
			for (int i = 0; i < paramStrList.length; i += 2) {
				Sale sale = new Sale();
				sale.setStoreType(Integer.valueOf(paramStrList[i]));
				sale.setSaleResult(Integer.valueOf(paramStrList[i + 1]));

				awardDoubles.add(sale);
			}

			// params to byte[]
			byte[] paramsBytes = ProtostuffIOUtil.toByteArray(dbActivitySale, DBActivitySale.getSchema(),
					CommonUtils.getProtoBuffer());
			activity4Gm.setParams(paramsBytes);

			// send to Game Server
			send2GameServer(model, activity4Gm);
			return new ActivityRetMsg("", true);
		} catch (ParseException e) {
			logger.error("", e);
			return new ActivityRetMsg("wrong time format!", false);
		}
	}

	/**
	 * 
	 * 开服竞技场排名奖励
	 * 
	 * @param activity4Gm
	 * @param model
	 * @return
	 */
	public ActivityRetMsg kaifu(Activity4Gm activity4Gm, Model model) {
		try {
			// 处理开始时间和结束时间
			int templateId = activity4Gm.getTemplateId();
			ActivityTemplate at = ActivityTemplateCache.getInstance().getActivityTemplate(templateId);
			setTime(activity4Gm, at);
			// 是否开启
			String isOpenStr = activity4Gm.getIsOpenStr();
			if (isOpenStr != null && isOpenStr.equals("on")) {
				activity4Gm.setIsOpen(1);
			} else {
				activity4Gm.setIsOpen(0);
			}
			// params
			String paramStr = activity4Gm.getParamsStr();
			String[] paramStrList = paramStr.split("\n");
			if (paramStrList.length % 3 != 0) {
				return new ActivityRetMsg("params error!", false);
			}
			DBActivityKaiFu dbRechargeAward = new DBActivityKaiFu();
			List<ActivityConfigKaifuJinSai> awardOnes = new ArrayList<ActivityConfigKaifuJinSai>();
			dbRechargeAward.setItemsList(awardOnes);
			for (int i = 0; i < paramStrList.length; i += 3) {
				int activityType = Integer.valueOf(paramStrList[i]);
				int level = Integer.valueOf(paramStrList[i + 1]);
				ActivityConfigKaifuJinSai rechargeAwardOne = new ActivityConfigKaifuJinSai(activityType, level);
				String awardStr = paramStrList[i + 2];
				String[] awardStrList = StringUtils.split(awardStr, StringConstants.SEPARATOR_SHU);
				List<Award> awardList = new ArrayList<Award>();
				rechargeAwardOne.setAwardList(awardList);
				for (String awardStrPair : awardStrList) {
					String[] awardPairList = StringUtils.split(awardStrPair, StringConstants.SEPARATOR_HENG);
					int itemTplId = Integer.valueOf(awardPairList[0]);
					boolean itemTplExist = ItemTemplateCache.getInstance().checkExist(itemTplId);
					if (!itemTplExist) {
						throw new ItemNotExistException(itemTplId);
					}
					int itemCount = Integer.valueOf(awardPairList[1]);
					Award dbAward = new Award(itemTplId, itemCount);
					awardList.add(dbAward);
				}
				awardOnes.add(rechargeAwardOne);
			}
			// params to byte[]
			byte[] paramsBytes = ProtostuffIOUtil.toByteArray(dbRechargeAward, DBActivityKaiFu.getSchema(),
					CommonUtils.getProtoBuffer());
			activity4Gm.setParams(paramsBytes);
			// send to Game Server
			send2GameServer(model, activity4Gm);
			return new ActivityRetMsg("", true);
		} catch (ParseException e) {
			logger.error("", e);
			return new ActivityRetMsg("wrong time format!" + e.getMessage(), false);
		} catch (NumberFormatException e) {
			logger.error("", e);
			return new ActivityRetMsg("wrong number format!" + e.getMessage(), false);
		} catch (ArrayIndexOutOfBoundsException e) {
			logger.error("", e);
			return new ActivityRetMsg("wrong award format!" + e.getMessage(), false);
		} catch (ItemNotExistException e) {
			logger.error("", e);
			return new ActivityRetMsg("wrong award format!item not exist:" + e.getMessage(), false);
		}
	}

	public String extractKaifu(byte[] params) {
		if (params.length == 0) {
			return null;
		}
		DBActivityKaiFu dbActivityLevel = new DBActivityKaiFu();
		ProtostuffIOUtil.mergeFrom(params, dbActivityLevel, DBActivityKaiFu.getSchema());
		List<ActivityConfigKaifuJinSai> awardOnes = dbActivityLevel.getItemsList();
		List<String> awardAllList = new ArrayList<String>();
		for (ActivityConfigKaifuJinSai levelAward : awardOnes) {
			int type = levelAward.getType();
			awardAllList.add(String.valueOf(type));
			int level = levelAward.getLevel();
			awardAllList.add(String.valueOf(level));

			List<Award> awards = levelAward.getAwardList();
			List<String> awardStrList = new ArrayList<String>();
			for (Award award : awards) {
				String awardStr = StringUtils.join(new int[] { award.getItemId(), award.getItemCount() }, '-');
				awardStrList.add(awardStr);
			}
			String awardStrAll = StringUtils.join(awardStrList, '|');
			awardAllList.add(awardStrAll);
		}
		String paramsStr = StringUtils.join(awardAllList, "\n");
		return paramsStr;
	}

	public String extractChengZhang(byte[] params) {
		if (params.length == 0) {
			return null;
		}
		DBActivityChengZhangJiJin dbActivityLevel = new DBActivityChengZhangJiJin();
		ProtostuffIOUtil.mergeFrom(params, dbActivityLevel, DBActivityChengZhangJiJin.getSchema());
		List<Integer> awardOnes = dbActivityLevel.getDiamondDailyList();
		String paramsStr = StringUtils.join(awardOnes, "\n");
		return paramsStr;
	}

	public ActivityRetMsg chengZhangJiJin(Activity4Gm activity4Gm, Model model) {
		try {
			// 处理开始时间和结束时间
			int templateId = activity4Gm.getTemplateId();
			ActivityTemplate at = ActivityTemplateCache.getInstance().getActivityTemplate(templateId);
			setTime(activity4Gm, at);
			// 是否开启
			String isOpenStr = activity4Gm.getIsOpenStr();
			if (isOpenStr != null && isOpenStr.equals("on")) {
				activity4Gm.setIsOpen(1);
			} else {
				activity4Gm.setIsOpen(0);
			}
			// params
			String paramStr = activity4Gm.getParamsStr();
			String[] paramStrList = paramStr.split("\n");
			if (paramStrList.length % 2 != 0) {
				return new ActivityRetMsg("params error!", false);
			}
			DBActivityChengZhangJiJin dbRechargeAward = new DBActivityChengZhangJiJin();
			List<Integer> diamondDailyList = new ArrayList<Integer>();
			dbRechargeAward.setDiamondDailyList(diamondDailyList);
			for (int i = 0; i < paramStrList.length; i++) {
				String awardStr = paramStrList[i];
				Integer dailyDiamond = Integer.valueOf(awardStr);
				diamondDailyList.add(dailyDiamond);
			}
			// params to byte[]
			byte[] paramsBytes = ProtostuffIOUtil.toByteArray(dbRechargeAward, DBActivityChengZhangJiJin.getSchema(),
					CommonUtils.getProtoBuffer());
			activity4Gm.setParams(paramsBytes);
			// send to Game Server
			send2GameServer(model, activity4Gm);
			return new ActivityRetMsg("", true);
		} catch (ParseException e) {
			logger.error("", e);
			return new ActivityRetMsg("wrong time format!" + e.getMessage(), false);
		} catch (NumberFormatException e) {
			logger.error("", e);
			return new ActivityRetMsg("wrong number format!" + e.getMessage(), false);
		} catch (ArrayIndexOutOfBoundsException e) {
			logger.error("", e);
			return new ActivityRetMsg("wrong award format!" + e.getMessage(), false);
		}
	}

	//
	public String extractQuanMinFuLi(byte[] params) {
		if (params.length == 0) {
			return null;
		}
		DBActivityQuanMinFuLi dbActivityLevel = new DBActivityQuanMinFuLi();
		ProtostuffIOUtil.mergeFrom(params, dbActivityLevel, DBActivityQuanMinFuLi.getSchema());
		List<ActivityConfigOneFuLi> awardOnes = dbActivityLevel.getItemsList();
		List<String> awardAllList = new ArrayList<String>();
		for (ActivityConfigOneFuLi levelAward : awardOnes) {
			int vipLevel = levelAward.getVipLevel();
			awardAllList.add(String.valueOf(vipLevel));
			int level = levelAward.getLevel();
			awardAllList.add(String.valueOf(level));

			List<Award> awards = levelAward.getAwardList();
			List<String> awardStrList = new ArrayList<String>();
			for (Award award : awards) {
				String awardStr = StringUtils.join(new int[] { award.getItemId(), award.getItemCount() }, '-');
				awardStrList.add(awardStr);
			}
			String awardStrAll = StringUtils.join(awardStrList, '|');
			awardAllList.add(awardStrAll);
		}
		String paramsStr = StringUtils.join(awardAllList, "\n");
		return paramsStr;
	}

	public ActivityRetMsg quanMinFuLi(Activity4Gm activity4Gm, Model model) {
		try {
			// 处理开始时间和结束时间
			int templateId = activity4Gm.getTemplateId();
			ActivityTemplate at = ActivityTemplateCache.getInstance().getActivityTemplate(templateId);
			setTime(activity4Gm, at);
			// 是否开启
			String isOpenStr = activity4Gm.getIsOpenStr();
			if (isOpenStr != null && isOpenStr.equals("on")) {
				activity4Gm.setIsOpen(1);
			} else {
				activity4Gm.setIsOpen(0);
			}
			// params
			String paramStr = activity4Gm.getParamsStr();
			String[] paramStrList = paramStr.split("\n");
			if (paramStrList.length % 3 != 0) {
				return new ActivityRetMsg("params error!", false);
			}
			DBActivityQuanMinFuLi dbRechargeAward = new DBActivityQuanMinFuLi();
			List<ActivityConfigOneFuLi> awardOnes = new ArrayList<ActivityConfigOneFuLi>();
			dbRechargeAward.setItemsList(awardOnes);
			for (int i = 0; i < paramStrList.length; i += 3) {
				int vipLevel = Integer.valueOf(paramStrList[i]);
				int rechargeLevel = Integer.valueOf(paramStrList[i + 1]);
				ActivityConfigOneFuLi rechargeAwardOne = new ActivityConfigOneFuLi(rechargeLevel, vipLevel);
				String awardStr = paramStrList[i + 2];
				String[] awardStrList = StringUtils.split(awardStr, StringConstants.SEPARATOR_SHU);
				List<Award> awardList = new ArrayList<Award>();
				rechargeAwardOne.setAwardList(awardList);
				for (String awardStrPair : awardStrList) {
					String[] awardPairList = StringUtils.split(awardStrPair, StringConstants.SEPARATOR_HENG);
					int itemTplId = Integer.valueOf(awardPairList[0]);
					int itemCount = Integer.valueOf(awardPairList[1]);
					Award dbAward = new Award(itemTplId, itemCount);
					awardList.add(dbAward);
				}
				awardOnes.add(rechargeAwardOne);
			}
			// params to byte[]
			byte[] paramsBytes = ProtostuffIOUtil.toByteArray(dbRechargeAward, DBActivityQuanMinFuLi.getSchema(),
					CommonUtils.getProtoBuffer());
			activity4Gm.setParams(paramsBytes);
			// send to Game Server
			send2GameServer(model, activity4Gm);
			return new ActivityRetMsg("", true);
		} catch (ParseException e) {
			logger.error("", e);
			return new ActivityRetMsg("wrong time format!" + e.getMessage(), false);
		} catch (NumberFormatException e) {
			logger.error("", e);
			return new ActivityRetMsg("wrong number format!" + e.getMessage(), false);
		} catch (ArrayIndexOutOfBoundsException e) {
			logger.error("", e);
			return new ActivityRetMsg("wrong award format!" + e.getMessage(), false);
		}
	}

	public String extractVipLiBao(byte[] params) {
		if (params.length == 0) {
			return null;
		}
		DBActivityVipLiBao dbActivityLevel = new DBActivityVipLiBao();
		ProtostuffIOUtil.mergeFrom(params, dbActivityLevel, DBActivityVipLiBao.getSchema());
		List<ActivityConfigVipLiBao> awardOnes = dbActivityLevel.getItemsList();
		List<String> awardAllList = new ArrayList<String>();
		for (ActivityConfigVipLiBao levelAward : awardOnes) {
			int costDiamond = levelAward.getCostDiamond();
			awardAllList.add(String.valueOf(costDiamond));
			int vipLevel = levelAward.getVipLevel();
			awardAllList.add(String.valueOf(vipLevel));
			List<Award> awards = levelAward.getAwardList();
			List<String> awardStrList = new ArrayList<String>();
			for (Award award : awards) {
				String awardStr = StringUtils.join(new int[] { award.getItemId(), award.getItemCount() }, '-');
				awardStrList.add(awardStr);
			}
			String awardStrAll = StringUtils.join(awardStrList, '|');
			awardAllList.add(awardStrAll);
		}
		String paramsStr = StringUtils.join(awardAllList, "\n");
		return paramsStr;
	}

	public ActivityRetMsg vipLiBao(Activity4Gm activity4Gm, Model model) {
		// 处理开始时间和结束时间
		int templateId = activity4Gm.getTemplateId();
		ActivityTemplate at = ActivityTemplateCache.getInstance().getActivityTemplate(templateId);

		// 是否开启
		String isOpenStr = activity4Gm.getIsOpenStr();
		if (isOpenStr != null && isOpenStr.equals("on")) {
			activity4Gm.setIsOpen(1);
		} else {
			activity4Gm.setIsOpen(0);
		}
		// params
		String paramStr = activity4Gm.getParamsStr();
		String[] paramStrList = paramStr.split("\n");
		if (paramStrList.length % 3 != 0) {
			return new ActivityRetMsg("params error!", false);
		}
		DBActivityVipLiBao dbLevelAward = new DBActivityVipLiBao();
		List<ActivityConfigVipLiBao> awardOnes = new ArrayList<ActivityConfigVipLiBao>();
		dbLevelAward.setItemsList(awardOnes);
		for (int i = 0; i < paramStrList.length; i += 3) {
			ActivityConfigVipLiBao levelAward = new ActivityConfigVipLiBao();
			int costDiamond = Integer.valueOf(paramStrList[i]);
			int vipLevel = Integer.valueOf(paramStrList[i + 1]);
			levelAward.setCostDiamond(costDiamond);
			levelAward.setVipLevel(vipLevel);
			List<Award> awardList = new ArrayList<Award>();
			levelAward.setAwardList(awardList);
			String awardStr = paramStrList[i + 2];
			String[] awardStrList = StringUtils.split(awardStr, StringConstants.SEPARATOR_SHU);
			for (String awardStrPair : awardStrList) {
				String[] awardPairList = StringUtils.split(awardStrPair, StringConstants.SEPARATOR_HENG);
				int itemTplId = Integer.valueOf(awardPairList[0]);
				boolean itemTplExist = ItemTemplateCache.getInstance().checkExist(itemTplId);
				if (!itemTplExist) {
					try {
						throw new ItemNotExistException(itemTplId);
					} catch (ItemNotExistException e) {
						e.printStackTrace();
					}
				}
				int itemCount = Integer.valueOf(awardPairList[1]);
				Award dbAward = new Award(itemTplId, itemCount);
				awardList.add(dbAward);
			}

			awardOnes.add(levelAward);
		}
		// params to byte[]
		byte[] paramsBytes = ProtostuffIOUtil.toByteArray(dbLevelAward, DBActivityVipLiBao.getSchema(),
				CommonUtils.getProtoBuffer());
		activity4Gm.setParams(paramsBytes);

		// send to Game Server
		send2GameServer(model, activity4Gm);
		return new ActivityRetMsg("", true);
	}

	public ActivityRetMsg dailyActive(Activity4Gm activity4Gm, Model model) {
		// 处理开始时间和结束时间
		int templateId = activity4Gm.getTemplateId();
		ActivityTemplate at = ActivityTemplateCache.getInstance().getActivityTemplate(templateId);

		// 是否开启
		String isOpenStr = activity4Gm.getIsOpenStr();
		if (isOpenStr != null && isOpenStr.equals("on")) {
			activity4Gm.setIsOpen(1);
		} else {
			activity4Gm.setIsOpen(0);
		}
		// params
		String paramStr = activity4Gm.getParamsStr();
		String[] paramStrList = paramStr.split("\n");
		if (paramStrList.length % 2 != 0) {
			return new ActivityRetMsg("params error!", false);
		}
		DBActivityDailyActive dbLevelAward = new DBActivityDailyActive();
		List<ActivityConfigOne> awardOnes = new ArrayList<ActivityConfigOne>();
		dbLevelAward.setItemsList(awardOnes);
		for (int i = 0; i < paramStrList.length; i += 2) {
			ActivityConfigOne levelAward = new ActivityConfigOne();
			int level = Integer.valueOf(paramStrList[i]);
			levelAward.setLevel(level);
			List<Award> awardList = new ArrayList<Award>();
			levelAward.setAwardList(awardList);
			String awardStr = paramStrList[i + 1];
			String[] awardStrList = StringUtils.split(awardStr, StringConstants.SEPARATOR_SHU);
			for (String awardStrPair : awardStrList) {
				String[] awardPairList = StringUtils.split(awardStrPair, StringConstants.SEPARATOR_HENG);
				int itemTplId = Integer.valueOf(awardPairList[0]);
				boolean itemTplExist = ItemTemplateCache.getInstance().checkExist(itemTplId);
				if (!itemTplExist) {
					try {
						throw new ItemNotExistException(itemTplId);
					} catch (ItemNotExistException e) {
						e.printStackTrace();
					}
				}
				int itemCount = Integer.valueOf(awardPairList[1]);
				Award dbAward = new Award(itemTplId, itemCount);
				awardList.add(dbAward);
			}

			awardOnes.add(levelAward);
		}
		// params to byte[]
		byte[] paramsBytes = ProtostuffIOUtil.toByteArray(dbLevelAward, DBActivityDailyActive.getSchema(),
				CommonUtils.getProtoBuffer());
		activity4Gm.setParams(paramsBytes);

		// send to Game Server
		send2GameServer(model, activity4Gm);
		return new ActivityRetMsg("", true);
	}

	public String extractDailyActive(byte[] params) {
		if (params.length == 0) {
			return null;
		}
		DBActivityDailyActive dbActivityLevel = new DBActivityDailyActive();
		ProtostuffIOUtil.mergeFrom(params, dbActivityLevel, DBActivityDailyActive.getSchema());
		List<ActivityConfigOne> awardOnes = dbActivityLevel.getItemsList();
		List<String> awardAllList = new ArrayList<String>();
		for (ActivityConfigOne levelAward : awardOnes) {
			int level = levelAward.getLevel();
			awardAllList.add(String.valueOf(level));

			List<Award> awards = levelAward.getAwardList();
			List<String> awardStrList = new ArrayList<String>();
			for (Award award : awards) {
				String awardStr = StringUtils.join(new int[] { award.getItemId(), award.getItemCount() }, '-');
				awardStrList.add(awardStr);
			}
			String awardStrAll = StringUtils.join(awardStrList, '|');
			awardAllList.add(awardStrAll);
		}
		String paramsStr = StringUtils.join(awardAllList, "\n");
		return paramsStr;
	}

	public String extract3DayFirstRecharge(byte[] params) {
		if (params.length == 0) {
			return null;
		}
		DBActivity3DayFirstRecharge dbActivityLevel = new DBActivity3DayFirstRecharge();
		ProtostuffIOUtil.mergeFrom(params, dbActivityLevel, DBActivity3DayFirstRecharge.getSchema());
		List<ActivityConfigOne> awardOnes = dbActivityLevel.getItemsList();
		List<String> awardAllList = new ArrayList<String>();
		for (ActivityConfigOne levelAward : awardOnes) {
			List<Award> awards = levelAward.getAwardList();
			List<String> awardStrList = new ArrayList<String>();
			for (Award award : awards) {
				String awardStr = StringUtils.join(new int[] { award.getItemId(), award.getItemCount() }, '-');
				awardStrList.add(awardStr);
			}
			String awardStrAll = StringUtils.join(awardStrList, '|');
			awardAllList.add(awardStrAll);
		}
		String paramsStr = StringUtils.join(awardAllList, "\n");
		return paramsStr;
	}

	public ActivityRetMsg threeDayFirstRecharge(Activity4Gm activity4Gm, Model model) {
		// 处理开始时间和结束时间
		int templateId = activity4Gm.getTemplateId();
		ActivityTemplate at = ActivityTemplateCache.getInstance().getActivityTemplate(templateId);

		// 是否开启
		String isOpenStr = activity4Gm.getIsOpenStr();
		if (isOpenStr != null && isOpenStr.equals("on")) {
			activity4Gm.setIsOpen(1);
		} else {
			activity4Gm.setIsOpen(0);
		}
		// params
		String paramStr = activity4Gm.getParamsStr();
		String[] paramStrList = paramStr.split("\n");
		DBActivity3DayFirstRecharge dbLevelAward = new DBActivity3DayFirstRecharge();
		List<ActivityConfigOne> awardOnes = new ArrayList<ActivityConfigOne>();
		dbLevelAward.setItemsList(awardOnes);
		for (int i = 0; i < paramStrList.length; i++) {
			ActivityConfigOne levelAward = new ActivityConfigOne();
			levelAward.setLevel(i);
			List<Award> awardList = new ArrayList<Award>();
			levelAward.setAwardList(awardList);
			String awardStr = paramStrList[i];
			String[] awardStrList = StringUtils.split(awardStr, StringConstants.SEPARATOR_SHU);
			for (String awardStrPair : awardStrList) {
				String[] awardPairList = StringUtils.split(awardStrPair, StringConstants.SEPARATOR_HENG);
				int itemTplId = Integer.valueOf(awardPairList[0]);
				boolean itemTplExist = ItemTemplateCache.getInstance().checkExist(itemTplId);
				if (!itemTplExist) {
					try {
						throw new ItemNotExistException(itemTplId);
					} catch (ItemNotExistException e) {
						e.printStackTrace();
					}
				}
				int itemCount = Integer.valueOf(awardPairList[1]);
				Award dbAward = new Award(itemTplId, itemCount);
				awardList.add(dbAward);
			}

			awardOnes.add(levelAward);
		}
		// params to byte[]
		byte[] paramsBytes = ProtostuffIOUtil.toByteArray(dbLevelAward, DBActivity3DayFirstRecharge.getSchema(),
				CommonUtils.getProtoBuffer());
		activity4Gm.setParams(paramsBytes);

		// send to Game Server
		send2GameServer(model, activity4Gm);
		return new ActivityRetMsg("", true);
	}

	/**
	 * 
	 * @param activity4Gm
	 * @param model
	 * @return
	 */
	public ActivityRetMsg buildHeroLibao(Activity4Gm activity4Gm, Model model) {
		try {
			// 处理开始时间和结束时间
			int templateId = activity4Gm.getTemplateId();
			ActivityTemplate at = ActivityTemplateCache.getInstance().getActivityTemplate(templateId);
			setTime(activity4Gm, at);
			// 是否开启
			String isOpenStr = activity4Gm.getIsOpenStr();
			if (isOpenStr != null && isOpenStr.equals("on")) {
				activity4Gm.setIsOpen(1);
			} else {
				activity4Gm.setIsOpen(0);
			}
			// params
			String paramStr = activity4Gm.getParamsStr();
			String[] paramStrList = paramStr.split("\n");
			if (paramStrList.length % 2 != 0) {
				return new ActivityRetMsg("params error!", false);
			}
			DBActivityHeroLibao dbLevelAward = new DBActivityHeroLibao();
			List<ActivityConfigHeroLiBao> awardOnes = new ArrayList<ActivityConfigHeroLiBao>();
			dbLevelAward.setItemsList(awardOnes);
			for (int i = 0; i < paramStrList.length; i += 2) {
				ActivityConfigHeroLiBao levelAward = new ActivityConfigHeroLiBao();
				//
				String param1Str = paramStrList[i];
				String[] param1Lists = StringUtils.split(param1Str, StringConstants.SEPARATOR_HENG);
				int buy_count = Integer.valueOf(param1Lists[0]);
				int favor_rate = Integer.valueOf(param1Lists[1]);
				int price = Integer.valueOf(param1Lists[2]);
				int recharge_id = Integer.valueOf(param1Lists[3]);
				levelAward.setBuyCount(buy_count);
				levelAward.setFavorRate(favor_rate);
				levelAward.setPrice(price);
				levelAward.setRechargeId(recharge_id);
				//
				List<Award> awardList = new ArrayList<Award>();
				levelAward.setAwardList(awardList);
				String awardStr = paramStrList[i + 1];
				String[] awardStrList = StringUtils.split(awardStr, StringConstants.SEPARATOR_SHU);
				for (String awardStrPair : awardStrList) {
					String[] awardPairList = StringUtils.split(awardStrPair, StringConstants.SEPARATOR_HENG);
					int itemTplId = Integer.valueOf(awardPairList[0]);
//					boolean itemTplExist = ItemTemplateCache.getInstance().checkExist(itemTplId);
//					if (!itemTplExist) {
//						try {
//							throw new ItemNotExistException(itemTplId);
//						} catch (ItemNotExistException e) {
//							e.printStackTrace();
//						}
//					}
					int itemCount = Integer.valueOf(awardPairList[1]);
					Award dbAward = new Award(itemTplId, itemCount);
					awardList.add(dbAward);
				}

				awardOnes.add(levelAward);
			}
			// params to byte[]
			byte[] paramsBytes = ProtostuffIOUtil.toByteArray(dbLevelAward, DBActivityHeroLibao.getSchema(),
					CommonUtils.getProtoBuffer());
			activity4Gm.setParams(paramsBytes);

			// send to Game Server
			send2GameServer(model, activity4Gm);
			return new ActivityRetMsg("", true);
		} catch (ParseException e) {
			logger.error("", e);
			return new ActivityRetMsg("wrong time format!", false);
		}
	}

	public String extractHeroLibao(byte[] params) {
		if (params.length == 0) {
			return null;
		}
		DBActivityHeroLibao dbActivityLevel = new DBActivityHeroLibao();
		ProtostuffIOUtil.mergeFrom(params, dbActivityLevel, DBActivityHeroLibao.getSchema());
		List<ActivityConfigHeroLiBao> awardOnes = dbActivityLevel.getItemsList();
		List<String> awardAllList = new ArrayList<String>();
		for (ActivityConfigHeroLiBao levelAward : awardOnes) {
			String param1Str = StringUtils.join(new int[] { levelAward.getBuyCount(), levelAward.getFavorRate(),
					levelAward.getPrice(), levelAward.getRechargeId() }, '-');
			awardAllList.add(param1Str);
			List<Award> awards = levelAward.getAwardList();
			List<String> awardStrList = new ArrayList<String>();
			for (Award award : awards) {
				String awardStr = StringUtils.join(new int[] { award.getItemId(), award.getItemCount() }, '-');
				awardStrList.add(awardStr);
			}
			String awardStrAll = StringUtils.join(awardStrList, '|');
			awardAllList.add(awardStrAll);
		}
		String paramsStr = StringUtils.join(awardAllList, "\n");
		return paramsStr;
	}

	public String extractLotteryWheel(byte[] params) {
		if (params.length == 0) {
			return null;
		}
		DBActivityLotteryWheel dbActivityLevel = new DBActivityLotteryWheel();
		ProtostuffIOUtil.mergeFrom(params, dbActivityLevel, DBActivityLotteryWheel.getSchema());
		int dailyCount = dbActivityLevel.getDailyFreeCount();
		int dailyPayCount = dbActivityLevel.getDailyPayCount();
		int payPrice = dbActivityLevel.getPayPrice();
		String row1Str = dailyCount + StringConstants.SEPARATOR_HENG + dailyPayCount + StringConstants.SEPARATOR_HENG
				+ payPrice;
		List<DBActivityLotteryWheelOne> awardOnes = dbActivityLevel.getAwardItemList();
		List<String> awardAllList = new ArrayList<String>();
		awardAllList.add(row1Str);
		for (DBActivityLotteryWheelOne levelAward : awardOnes) {
			int templateId = levelAward.getTemplateId();
			int count = levelAward.getCount();
			int rate = levelAward.getRate();
			String awardStr = StringUtils.join(new int[] { templateId, count, rate }, '-');
			awardAllList.add(awardStr);
		}
		String paramsStr = StringUtils.join(awardAllList, "\n");
		return paramsStr;
	}

	public ActivityRetMsg buildLotteryWheel(Activity4Gm activity4Gm, Model model) {
		try {
			// 处理开始时间和结束时间
			int templateId = activity4Gm.getTemplateId();
			ActivityTemplate at = ActivityTemplateCache.getInstance().getActivityTemplate(templateId);
			setTime(activity4Gm, at);
			// 是否开启
			String isOpenStr = activity4Gm.getIsOpenStr();
			if (isOpenStr != null && isOpenStr.equals("on")) {
				activity4Gm.setIsOpen(1);
			} else {
				activity4Gm.setIsOpen(0);
			}
			// params
			String paramStr = activity4Gm.getParamsStr();
			String[] paramStrList = paramStr.split("\n");
			DBActivityLotteryWheel dbLevelAward = new DBActivityLotteryWheel();
			String baseConfig = paramStrList[0];
			String[] baseConfigList = StringUtils.split(baseConfig, StringConstants.SEPARATOR_HENG);
			int dailyFreeCount = Integer.valueOf(baseConfigList[0]);
			int dailyPayCount = Integer.valueOf(baseConfigList[1]);
			int payPrice = Integer.valueOf(baseConfigList[2]);
			dbLevelAward.setDailyFreeCount(dailyFreeCount);
			dbLevelAward.setDailyPayCount(dailyPayCount);
			dbLevelAward.setPayPrice(payPrice);
			List<DBActivityLotteryWheelOne> awardOnes = new ArrayList<DBActivityLotteryWheelOne>();
			dbLevelAward.setAwardItemList(awardOnes);
			for (int i = 1; i < paramStrList.length; i++) {
				DBActivityLotteryWheelOne levelAward = new DBActivityLotteryWheelOne();
				String param1Str = paramStrList[i];
				String[] param1Lists = StringUtils.split(param1Str, StringConstants.SEPARATOR_HENG);
				int itemTemplateId = Integer.valueOf(param1Lists[0]);
				int count = Integer.valueOf(param1Lists[1]);
				int rate = Integer.valueOf(param1Lists[2]);
				levelAward.setTemplateId(itemTemplateId);
				levelAward.setCount(count);
				levelAward.setRate(rate);

				awardOnes.add(levelAward);
			}
			// params to byte[]
			byte[] paramsBytes = ProtostuffIOUtil.toByteArray(dbLevelAward, DBActivityLotteryWheel.getSchema(),
					CommonUtils.getProtoBuffer());
			activity4Gm.setParams(paramsBytes);

			// send to Game Server
			send2GameServer(model, activity4Gm);
			return new ActivityRetMsg("", true);
		} catch (ParseException e) {
			logger.error("", e);
			return new ActivityRetMsg("wrong time format!", false);
		}
	}

	public ActivityRetMsg buildFirstRechargeDouble(Activity4Gm activity4Gm, Model model) {
		try {
			// 处理开始时间和结束时间
			int templateId = activity4Gm.getTemplateId();
			ActivityTemplate at = ActivityTemplateCache.getInstance().getActivityTemplate(templateId);
			setTime(activity4Gm, at);
			// 是否开启
			String isOpenStr = activity4Gm.getIsOpenStr();
			if (isOpenStr != null && isOpenStr.equals("on")) {
				activity4Gm.setIsOpen(1);
			} else {
				activity4Gm.setIsOpen(0);
			}
			// params
			String paramStr = activity4Gm.getParamsStr();
			String[] paramStrList = paramStr.split("\n");
			DBActivityFirstRechgeDouble dbLevelAward = new DBActivityFirstRechgeDouble();
			List<ActivityFirstRechgeDouble> awardOnes = new ArrayList<ActivityFirstRechgeDouble>();
			dbLevelAward.setItemsList(awardOnes);
			for (int i = 1; i < paramStrList.length; i++) {
				ActivityFirstRechgeDouble levelAward = new ActivityFirstRechgeDouble();
				String param1Str = paramStrList[i];
				String[] param1Lists = StringUtils.split(param1Str, StringConstants.SEPARATOR_HENG);
				int rechargeId = Integer.valueOf(param1Lists[0]);
				int favorRate = Integer.valueOf(param1Lists[1]);
				levelAward.setRechargeId(rechargeId);
				levelAward.setFavorRate(favorRate);
				awardOnes.add(levelAward);
			}
			// params to byte[]
			byte[] paramsBytes = ProtostuffIOUtil.toByteArray(dbLevelAward, DBActivityFirstRechgeDouble.getSchema(),
					CommonUtils.getProtoBuffer());
			activity4Gm.setParams(paramsBytes);

			// send to Game Server
			send2GameServer(model, activity4Gm);
			return new ActivityRetMsg("", true);
		} catch (ParseException e) {
			logger.error("", e);
			return new ActivityRetMsg("wrong time format!", false);
		}
	}

	public String extractFirstRechargeDouble(byte[] params) {
		if (params.length == 0) {
			return null;
		}
		DBActivityFirstRechgeDouble dbActivityLevel = new DBActivityFirstRechgeDouble();
		ProtostuffIOUtil.mergeFrom(params, dbActivityLevel, DBActivityFirstRechgeDouble.getSchema());
		List<ActivityFirstRechgeDouble> awardOnes = dbActivityLevel.getItemsList();
		List<String> awardAllList = new ArrayList<String>();
		for (ActivityFirstRechgeDouble levelAward : awardOnes) {
			int rechargeId = levelAward.getRechargeId();
			int favorRate = levelAward.getFavorRate();
			String awardStr = StringUtils.join(new int[] { rechargeId, favorRate }, '-');
			awardAllList.add(awardStr);
		}
		String paramsStr = StringUtils.join(awardAllList, "\n");
		return paramsStr;
	}

	public String extractQiZhenYiBao(byte[] params) {
		if (params.length == 0) {
			return null;
		}
		DBActivityQiZhenYiBao dbActivityLevel = new DBActivityQiZhenYiBao();
		ProtostuffIOUtil.mergeFrom(params, dbActivityLevel, DBActivityQiZhenYiBao.getSchema());
		List<String> awardAllList = new ArrayList<String>();
		int playerCount = dbActivityLevel.getPlayerCount();
		awardAllList.add(String.valueOf(playerCount));
		int costDiamond = dbActivityLevel.getCostDiamond();
		awardAllList.add(String.valueOf(costDiamond));
		awardAllList.add(String.valueOf(dbActivityLevel.getBigAwardValue()));
		// fixed award
		List<Award> awardOnes = dbActivityLevel.getFixedAwardList();
		List<String> fixedAwardList = new ArrayList<String>();
		for (Award levelAward : awardOnes) {
			int itemId = levelAward.getItemId();
			int itemCount = levelAward.getItemCount();
			String awardStr = StringUtils.join(new int[] { itemId, itemCount }, '-');
			fixedAwardList.add(awardStr);
		}
		awardAllList.add(StringUtils.join(fixedAwardList, '|'));
		// big award
		int bigAwardId = dbActivityLevel.getBigAward().getItemId();
		int bigAwardCount = dbActivityLevel.getBigAward().getItemCount();
		awardAllList.add(StringUtils.join(new int[] { bigAwardId, bigAwardCount }, '-'));
		String paramsStr = StringUtils.join(awardAllList, "\n");
		return paramsStr;
	}

	public ActivityRetMsg buildQiZhenYiBao(Activity4Gm activity4Gm, Model model) {
		try {
			// 处理开始时间和结束时间
			int templateId = activity4Gm.getTemplateId();
			ActivityTemplate at = ActivityTemplateCache.getInstance().getActivityTemplate(templateId);
			setTime(activity4Gm, at);
			// 是否开启
			String isOpenStr = activity4Gm.getIsOpenStr();
			if (isOpenStr != null && isOpenStr.equals("on")) {
				activity4Gm.setIsOpen(1);
			} else {
				activity4Gm.setIsOpen(0);
			}
			// params
			String paramStr = activity4Gm.getParamsStr();
			String[] paramStrList = paramStr.split("\n");
			DBActivityQiZhenYiBao dbLevelAward = new DBActivityQiZhenYiBao();
			dbLevelAward.setPlayerCount(Integer.valueOf(paramStrList[0]));
			dbLevelAward.setCostDiamond(Integer.valueOf(paramStrList[1]));
			dbLevelAward.setBigAwardValue(Integer.valueOf(paramStrList[2]));
			// fixed award
			List<Award> fixedAwardOnes = new ArrayList<Award>();
			dbLevelAward.setFixedAwardList(fixedAwardOnes);
			ActivityFirstRechgeDouble levelAward = new ActivityFirstRechgeDouble();
			String param1Str = paramStrList[3];
			String[] awardStrList = StringUtils.split(param1Str, StringConstants.SEPARATOR_SHU);
			for (String awardStrPair : awardStrList) {
				String[] awardPairList = StringUtils.split(awardStrPair, StringConstants.SEPARATOR_HENG);
				int itemTplId = Integer.valueOf(awardPairList[0]);
				int itemCount = Integer.valueOf(awardPairList[1]);
				Award dbAward = new Award(itemTplId, itemCount);
				fixedAwardOnes.add(dbAward);
			}
			// big award
			String bigAwardStr = paramStrList[4];
			String[] awardPairList = StringUtils.split(bigAwardStr, StringConstants.SEPARATOR_HENG);
			int itemTplId = Integer.valueOf(awardPairList[0]);
			int itemCount = Integer.valueOf(awardPairList[1]);
			Award dbAward = new Award(itemTplId, itemCount);
			dbLevelAward.setBigAward(dbAward);
			// params to byte[]
			byte[] paramsBytes = ProtostuffIOUtil.toByteArray(dbLevelAward, DBActivityQiZhenYiBao.getSchema(),
					CommonUtils.getProtoBuffer());
			activity4Gm.setParams(paramsBytes);

			// send to Game Server
			send2GameServer(model, activity4Gm);
			return new ActivityRetMsg("", true);
		} catch (ParseException e) {
			logger.error("", e);
			return new ActivityRetMsg("wrong time format!", false);
		}
	}

	public ActivityRetMsg buildCommon(Activity4Gm activity4Gm, Model model) {
		try {
			// 处理开始时间和结束时间
			int templateId = activity4Gm.getTemplateId();
			ActivityTemplate at = ActivityTemplateCache.getInstance().getActivityTemplate(templateId);
			setTime(activity4Gm, at);
			// 是否开启
			String isOpenStr = activity4Gm.getIsOpenStr();
			if (isOpenStr != null && isOpenStr.equals("on")) {
				activity4Gm.setIsOpen(1);
			} else {
				activity4Gm.setIsOpen(0);
			}
			// params
			String paramStr = activity4Gm.getParamsStr();
			String[] paramStrList = paramStr.split("\n");
			// if (paramStrList.length % 2 != 0) {
			// return new ActivityRetMsg("params error!", false);
			// }
			DBActivityCommon dbRechargeAward = new DBActivityCommon();
			List<ActivityConfigOne> awardOnes = new ArrayList<ActivityConfigOne>();
			dbRechargeAward.setItemsList(awardOnes);
			for (int i = 0; i < paramStrList.length; i ++) {
				// int rechargeLevel = Integer.valueOf(paramStrList[i]);
				ActivityConfigOne rechargeAwardOne = new ActivityConfigOne(i + 1);
				String awardStr = paramStrList[i];
				String[] awardStrList = StringUtils.split(awardStr, StringConstants.SEPARATOR_SHU);
				List<Award> awardList = new ArrayList<Award>();
				rechargeAwardOne.setAwardList(awardList);
				for (String awardStrPair : awardStrList) {
					String[] awardPairList = StringUtils.split(awardStrPair, StringConstants.SEPARATOR_HENG);
					int itemTplId = Integer.valueOf(awardPairList[0]);
					int itemCount = Integer.valueOf(awardPairList[1]);
					Award dbAward = new Award(itemTplId, itemCount);
					awardList.add(dbAward);
				}
				awardOnes.add(rechargeAwardOne);
			}
			// params to byte[]
			byte[] paramsBytes = ProtostuffIOUtil.toByteArray(dbRechargeAward, DBActivityCommon.getSchema(),
					CommonUtils.getProtoBuffer());
			activity4Gm.setParams(paramsBytes);
			// send to Game Server
			send2GameServer(model, activity4Gm);
			return new ActivityRetMsg("", true);
		} catch (ParseException e) {
			logger.error("", e);
			return new ActivityRetMsg("wrong time format!" + e.getMessage(), false);
		} catch (NumberFormatException e) {
			logger.error("", e);
			return new ActivityRetMsg("wrong number format!" + e.getMessage(), false);
		} catch (ArrayIndexOutOfBoundsException e) {
			logger.error("", e);
			return new ActivityRetMsg("wrong award format!" + e.getMessage(), false);
		}
	}

	public ActivityRetMsg buildDanBiChongZhi(Activity4Gm activity4Gm, Model model) {
		try {
			// 处理开始时间和结束时间
			int templateId = activity4Gm.getTemplateId();
			ActivityTemplate at = ActivityTemplateCache.getInstance().getActivityTemplate(templateId);
			setTime(activity4Gm, at);
			// 是否开启
			String isOpenStr = activity4Gm.getIsOpenStr();
			if (isOpenStr != null && isOpenStr.equals("on")) {
				activity4Gm.setIsOpen(1);
			} else {
				activity4Gm.setIsOpen(0);
			}
			// params
			String paramStr = activity4Gm.getParamsStr();
			String[] paramStrList = paramStr.split("\n");
			if (paramStrList.length % 2 != 0) {
				return new ActivityRetMsg("params error!", false);
			}
			DBActivityCommon dbRechargeAward = new DBActivityCommon();
			List<ActivityConfigOne> awardOnes = new ArrayList<ActivityConfigOne>();
			dbRechargeAward.setItemsList(awardOnes);
			for (int i = 0; i < paramStrList.length; i += 2) {
				int rechargeLevel = Integer.valueOf(paramStrList[i]);
				ActivityConfigOne rechargeAwardOne = new ActivityConfigOne(rechargeLevel);
				String awardStr = paramStrList[i + 1];
				String[] awardStrList = StringUtils.split(awardStr, StringConstants.SEPARATOR_SHU);
				List<Award> awardList = new ArrayList<Award>();
				rechargeAwardOne.setAwardList(awardList);
				for (String awardStrPair : awardStrList) {
					String[] awardPairList = StringUtils.split(awardStrPair, StringConstants.SEPARATOR_HENG);
					int itemTplId = Integer.valueOf(awardPairList[0]);
//					boolean itemTplExist = ItemTemplateCache.getInstance().checkExist(itemTplId);
//					if (!itemTplExist) {
//						throw new ItemNotExistException(itemTplId);
//					}
					int itemCount = Integer.valueOf(awardPairList[1]);
					Award dbAward = new Award(itemTplId, itemCount);
					awardList.add(dbAward);
				}
				awardOnes.add(rechargeAwardOne);
			}
			// params to byte[]
			byte[] paramsBytes = ProtostuffIOUtil.toByteArray(dbRechargeAward, DBActivityCommon.getSchema(),
					CommonUtils.getProtoBuffer());
			activity4Gm.setParams(paramsBytes);
			// send to Game Server
			send2GameServer(model, activity4Gm);
			return new ActivityRetMsg("", true);
		} catch (ParseException e) {
			logger.error("", e);
			return new ActivityRetMsg("wrong time format!" + e.getMessage(), false);
		} catch (NumberFormatException e) {
			logger.error("", e);
			return new ActivityRetMsg("wrong number format!" + e.getMessage(), false);
		} catch (ArrayIndexOutOfBoundsException e) {
			logger.error("", e);
			return new ActivityRetMsg("wrong award format!" + e.getMessage(), false);
		}// catch (ItemNotExistException e) {
//			logger.error("", e);
//			return new ActivityRetMsg("wrong award format!item not exist:" + e.getMessage(), false);
//		}
	}

	public ActivityRetMsg buildLianXuChongZhi(Activity4Gm activity4Gm, Model model) {
		try {
			// 处理开始时间和结束时间
			int templateId = activity4Gm.getTemplateId();
			ActivityTemplate at = ActivityTemplateCache.getInstance().getActivityTemplate(templateId);
			setTime(activity4Gm, at);
			// 是否开启
			String isOpenStr = activity4Gm.getIsOpenStr();
			if (isOpenStr != null && isOpenStr.equals("on")) {
				activity4Gm.setIsOpen(1);
			} else {
				activity4Gm.setIsOpen(0);
			}
			// params
			String paramStr = activity4Gm.getParamsStr();
			String[] paramStrList = paramStr.split("\n");
			// if (paramStrList.length % 2 != 0) {
			// return new ActivityRetMsg("params error!", false);
			// }
			DBActivityLianXuChongZhi dbRechargeAward = new DBActivityLianXuChongZhi();
			List<ActivityConfigOne> awardOnes = new ArrayList<ActivityConfigOne>();
			dbRechargeAward.setItemsList(awardOnes);
			for (int i = 0; i < paramStrList.length; i += 1) {
				// int rechargeLevel = Integer.valueOf(paramStrList[i]);
				ActivityConfigOne rechargeAwardOne = new ActivityConfigOne(i + 1);
				String awardStr = paramStrList[i];
				String[] awardStrList = StringUtils.split(awardStr, StringConstants.SEPARATOR_SHU);
				List<Award> awardList = new ArrayList<Award>();
				rechargeAwardOne.setAwardList(awardList);
				for (String awardStrPair : awardStrList) {
					String[] awardPairList = StringUtils.split(awardStrPair, StringConstants.SEPARATOR_HENG);
					int itemTplId = Integer.valueOf(awardPairList[0]);
					boolean itemTplExist = ItemTemplateCache.getInstance().checkExist(itemTplId);
					if (!itemTplExist) {
						throw new ItemNotExistException(itemTplId);
					}
					int itemCount = Integer.valueOf(awardPairList[1]);
					Award dbAward = new Award(itemTplId, itemCount);
					awardList.add(dbAward);
				}
				awardOnes.add(rechargeAwardOne);
			}
			// params to byte[]
			byte[] paramsBytes = ProtostuffIOUtil.toByteArray(dbRechargeAward, DBActivityLianXuChongZhi.getSchema(),
					CommonUtils.getProtoBuffer());
			activity4Gm.setParams(paramsBytes);
			// send to Game Server
			send2GameServer(model, activity4Gm);
			return new ActivityRetMsg("", true);
		} catch (ParseException e) {
			logger.error("", e);
			return new ActivityRetMsg("wrong time format!" + e.getMessage(), false);
		} catch (NumberFormatException e) {
			logger.error("", e);
			return new ActivityRetMsg("wrong number format!" + e.getMessage(), false);
		} catch (ArrayIndexOutOfBoundsException e) {
			logger.error("", e);
			return new ActivityRetMsg("wrong award format!" + e.getMessage(), false);
		} catch (ItemNotExistException e) {
			logger.error("", e);
			return new ActivityRetMsg("wrong award format!item not exist:" + e.getMessage(), false);
		}
	}

	public ActivityRetMsg buildShiLianJiangLi(Activity4Gm activity4Gm, Model model) {
		try {
			// 处理开始时间和结束时间
			int templateId = activity4Gm.getTemplateId();
			ActivityTemplate at = ActivityTemplateCache.getInstance().getActivityTemplate(templateId);
			setTime(activity4Gm, at);
			// 是否开启
			String isOpenStr = activity4Gm.getIsOpenStr();
			if (isOpenStr != null && isOpenStr.equals("on")) {
				activity4Gm.setIsOpen(1);
			} else {
				activity4Gm.setIsOpen(0);
			}
			// params
			String paramStr = activity4Gm.getParamsStr();
			String[] paramStrList = paramStr.split("\n");
			if (paramStrList.length % 2 != 0) {
				return new ActivityRetMsg("params error!", false);
			}
			DBActivityShiLianJiangLi dbRechargeAward = new DBActivityShiLianJiangLi();
			List<ActivityConfigOne> awardOnes = new ArrayList<ActivityConfigOne>();
			dbRechargeAward.setItemsList(awardOnes);
			for (int i = 0; i < paramStrList.length; i += 2) {
				int rechargeLevel = Integer.valueOf(paramStrList[i]);
				ActivityConfigOne rechargeAwardOne = new ActivityConfigOne(rechargeLevel);
				String awardStr = paramStrList[i + 1];
				String[] awardStrList = StringUtils.split(awardStr, StringConstants.SEPARATOR_SHU);
				List<Award> awardList = new ArrayList<Award>();
				rechargeAwardOne.setAwardList(awardList);
				for (String awardStrPair : awardStrList) {
					String[] awardPairList = StringUtils.split(awardStrPair, StringConstants.SEPARATOR_HENG);
					int itemTplId = Integer.valueOf(awardPairList[0]);
					boolean itemTplExist = ItemTemplateCache.getInstance().checkExist(itemTplId);
					if (!itemTplExist) {
						throw new ItemNotExistException(itemTplId);
					}
					int itemCount = Integer.valueOf(awardPairList[1]);
					Award dbAward = new Award(itemTplId, itemCount);
					awardList.add(dbAward);
				}
				awardOnes.add(rechargeAwardOne);
			}
			// params to byte[]
			byte[] paramsBytes = ProtostuffIOUtil.toByteArray(dbRechargeAward, DBActivityShiLianJiangLi.getSchema(),
					CommonUtils.getProtoBuffer());
			activity4Gm.setParams(paramsBytes);
			// send to Game Server
			send2GameServer(model, activity4Gm);
			return new ActivityRetMsg("", true);
		} catch (ParseException e) {
			logger.error("", e);
			return new ActivityRetMsg("wrong time format!" + e.getMessage(), false);
		} catch (NumberFormatException e) {
			logger.error("", e);
			return new ActivityRetMsg("wrong number format!" + e.getMessage(), false);
		} catch (ArrayIndexOutOfBoundsException e) {
			logger.error("", e);
			return new ActivityRetMsg("wrong award format!" + e.getMessage(), false);
		} catch (ItemNotExistException e) {
			logger.error("", e);
			return new ActivityRetMsg("wrong award format!item not exist:" + e.getMessage(), false);
		}
	}

	public ActivityRetMsg buildChongZhiBang(Activity4Gm activity4Gm, Model model) {
		try {
			// 处理开始时间和结束时间
			int templateId = activity4Gm.getTemplateId();
			ActivityTemplate at = ActivityTemplateCache.getInstance().getActivityTemplate(templateId);
			setTime(activity4Gm, at);
			// 是否开启
			String isOpenStr = activity4Gm.getIsOpenStr();
			if (isOpenStr != null && isOpenStr.equals("on")) {
				activity4Gm.setIsOpen(1);
			} else {
				activity4Gm.setIsOpen(0);
			}
			// params
			String paramStr = activity4Gm.getParamsStr();
			String[] paramStrList = paramStr.split("\n");
//			if (paramStrList.length % 2 != 0) {
//				return new ActivityRetMsg("params error!", false);
//			}
			DBActivityChongZhiBang dbRechargeAward = new DBActivityChongZhiBang();
			List<ActivityConfigOne1> awardOnes = new ArrayList<ActivityConfigOne1>();
			dbRechargeAward.setItemsList(awardOnes);
			for (int i = 0; i < paramStrList.length;) {
				int rechargeLevel = Integer.valueOf(paramStrList[i]);
				ActivityConfigOne1 rechargeAwardOne = new ActivityConfigOne1(rechargeLevel);
				String awardStr = paramStrList[i + 1];
				String[] awardStrList = StringUtils.split(awardStr, StringConstants.SEPARATOR_SHU);
				List<Award> awardList = new ArrayList<Award>();
				rechargeAwardOne.setAwardList(awardList);
				for (String awardStrPair : awardStrList) {
					String[] awardPairList = StringUtils.split(awardStrPair, StringConstants.SEPARATOR_HENG);
					int itemTplId = Integer.valueOf(awardPairList[0]);
					boolean itemTplExist = ItemTemplateCache.getInstance().checkExist(itemTplId);
					if (!itemTplExist) {
						throw new ItemNotExistException(itemTplId);
					}
					int itemCount = Integer.valueOf(awardPairList[1]);
					Award dbAward = new Award(itemTplId, itemCount);
					awardList.add(dbAward);
				}
				int extraLimit = Integer.valueOf(paramStrList[i + 2]);
				rechargeAwardOne.setExtraLimit(extraLimit);
				if (extraLimit > 0) {
					awardStr = paramStrList[i + 3];
					awardStrList = StringUtils.split(awardStr, StringConstants.SEPARATOR_SHU);
					awardList = new ArrayList<Award>();
					rechargeAwardOne.setExtraLimitAwardList(awardList);
					for (String awardStrPair : awardStrList) {
						String[] awardPairList = StringUtils.split(awardStrPair, StringConstants.SEPARATOR_HENG);
						int itemTplId = Integer.valueOf(awardPairList[0]);
						boolean itemTplExist = ItemTemplateCache.getInstance().checkExist(itemTplId);
						if (!itemTplExist) {
							throw new ItemNotExistException(itemTplId);
						}
						int itemCount = Integer.valueOf(awardPairList[1]);
						Award dbAward = new Award(itemTplId, itemCount);
						awardList.add(dbAward);
					}
					i += 4;
				} else {
					i += 3;
				}
				awardOnes.add(rechargeAwardOne);
			}
			// params to byte[]
			byte[] paramsBytes = ProtostuffIOUtil.toByteArray(dbRechargeAward, DBActivityChongZhiBang.getSchema(),
					CommonUtils.getProtoBuffer());
			activity4Gm.setParams(paramsBytes);
			// send to Game Server
			send2GameServer(model, activity4Gm);
			return new ActivityRetMsg("", true);
		} catch (ParseException e) {
			logger.error("", e);
			return new ActivityRetMsg("wrong time format!" + e.getMessage(), false);
		} catch (NumberFormatException e) {
			logger.error("", e);
			return new ActivityRetMsg("wrong number format!" + e.getMessage(), false);
		} catch (ArrayIndexOutOfBoundsException e) {
			logger.error("", e);
			return new ActivityRetMsg("wrong award format!" + e.getMessage(), false);
		} catch (ItemNotExistException e) {
			logger.error("", e);
			return new ActivityRetMsg("wrong award format!item not exist:" + e.getMessage(), false);
		}
	}

	public ActivityRetMsg buildXiaoFeiBang(Activity4Gm activity4Gm, Model model) {
		try {
			// 处理开始时间和结束时间
			int templateId = activity4Gm.getTemplateId();
			ActivityTemplate at = ActivityTemplateCache.getInstance().getActivityTemplate(templateId);
			setTime(activity4Gm, at);
			// 是否开启
			String isOpenStr = activity4Gm.getIsOpenStr();
			if (isOpenStr != null && isOpenStr.equals("on")) {
				activity4Gm.setIsOpen(1);
			} else {
				activity4Gm.setIsOpen(0);
			}
			// params
			String paramStr = activity4Gm.getParamsStr();
			String[] paramStrList = paramStr.split("\n");
//			if (paramStrList.length % 2 != 0) {
//				return new ActivityRetMsg("params error!", false);
//			}
			DBActivityChongZhiBang dbRechargeAward = new DBActivityChongZhiBang();
			List<ActivityConfigOne1> awardOnes = new ArrayList<ActivityConfigOne1>();
			dbRechargeAward.setItemsList(awardOnes);
			for (int i = 0; i < paramStrList.length;) {
				int rechargeLevel = Integer.valueOf(paramStrList[i]);
				ActivityConfigOne1 rechargeAwardOne = new ActivityConfigOne1(rechargeLevel);
				String awardStr = paramStrList[i + 1];
				String[] awardStrList = StringUtils.split(awardStr, StringConstants.SEPARATOR_SHU);
				List<Award> awardList = new ArrayList<Award>();
				rechargeAwardOne.setAwardList(awardList);
				for (String awardStrPair : awardStrList) {
					String[] awardPairList = StringUtils.split(awardStrPair, StringConstants.SEPARATOR_HENG);
					int itemTplId = Integer.valueOf(awardPairList[0]);
					boolean itemTplExist = ItemTemplateCache.getInstance().checkExist(itemTplId);
					if (!itemTplExist) {
						throw new ItemNotExistException(itemTplId);
					}
					int itemCount = Integer.valueOf(awardPairList[1]);
					Award dbAward = new Award(itemTplId, itemCount);
					awardList.add(dbAward);
				}
				int extraLimit = Integer.valueOf(paramStrList[i + 2]);
				rechargeAwardOne.setExtraLimit(extraLimit);
				if (extraLimit > 0) {
					awardStr = paramStrList[i + 3];
					awardStrList = StringUtils.split(awardStr, StringConstants.SEPARATOR_SHU);
					awardList = new ArrayList<Award>();
					rechargeAwardOne.setExtraLimitAwardList(awardList);
					for (String awardStrPair : awardStrList) {
						String[] awardPairList = StringUtils.split(awardStrPair, StringConstants.SEPARATOR_HENG);
						int itemTplId = Integer.valueOf(awardPairList[0]);
						boolean itemTplExist = ItemTemplateCache.getInstance().checkExist(itemTplId);
						if (!itemTplExist) {
							throw new ItemNotExistException(itemTplId);
						}
						int itemCount = Integer.valueOf(awardPairList[1]);
						Award dbAward = new Award(itemTplId, itemCount);
						awardList.add(dbAward);
					}
					i += 4;
				} else {
					i += 3;
				}
				awardOnes.add(rechargeAwardOne);
			}
			// params to byte[]
			byte[] paramsBytes = ProtostuffIOUtil.toByteArray(dbRechargeAward, DBActivityChongZhiBang.getSchema(),
					CommonUtils.getProtoBuffer());
			activity4Gm.setParams(paramsBytes);
			// send to Game Server
			send2GameServer(model, activity4Gm);
			return new ActivityRetMsg("", true);
		} catch (ParseException e) {
			logger.error("", e);
			return new ActivityRetMsg("wrong time format!" + e.getMessage(), false);
		} catch (NumberFormatException e) {
			logger.error("", e);
			return new ActivityRetMsg("wrong number format!" + e.getMessage(), false);
		} catch (ArrayIndexOutOfBoundsException e) {
			logger.error("", e);
			return new ActivityRetMsg("wrong award format!" + e.getMessage(), false);
		} catch (ItemNotExistException e) {
			logger.error("", e);
			return new ActivityRetMsg("wrong award format!item not exist:" + e.getMessage(), false);
		}
	}

	public String extractMeiRiShouChong(byte[] params) {
		if (params.length == 0) {
			return null;
		}
		DBActivityMeiRiShouChong dbActivityLevel = new DBActivityMeiRiShouChong();
		ProtostuffIOUtil.mergeFrom(params, dbActivityLevel, DBActivityMeiRiShouChong.getSchema());
		List<ActivityConfigOne> awardOnes = dbActivityLevel.getItemsList();
		List<String> awardAllList = new ArrayList<String>();
		for (ActivityConfigOne levelAward : awardOnes) {
			// int level = levelAward.getLevel();
			// awardAllList.add(String.valueOf(level));

			List<Award> awards = levelAward.getAwardList();
			List<String> awardStrList = new ArrayList<String>();
			for (Award award : awards) {
				String awardStr = StringUtils.join(new int[] { award.getItemId(), award.getItemCount() }, '-');
				awardStrList.add(awardStr);
			}
			String awardStrAll = StringUtils.join(awardStrList, '|');
			awardAllList.add(awardStrAll);
		}
		String paramsStr = StringUtils.join(awardAllList, "\n");
		return paramsStr;
	}

	public String extractLianXuChongZhi(byte[] params) {
		if (params.length == 0) {
			return null;
		}
		DBActivityLianXuChongZhi dbActivityLevel = new DBActivityLianXuChongZhi();
		ProtostuffIOUtil.mergeFrom(params, dbActivityLevel, DBActivityLianXuChongZhi.getSchema());
		List<ActivityConfigOne> awardOnes = dbActivityLevel.getItemsList();
		List<String> awardAllList = new ArrayList<String>();
		for (ActivityConfigOne levelAward : awardOnes) {
			// int level = levelAward.getLevel();
			// awardAllList.add(String.valueOf(level));

			List<Award> awards = levelAward.getAwardList();
			List<String> awardStrList = new ArrayList<String>();
			for (Award award : awards) {
				String awardStr = StringUtils.join(new int[] { award.getItemId(), award.getItemCount() }, '-');
				awardStrList.add(awardStr);
			}
			String awardStrAll = StringUtils.join(awardStrList, '|');
			awardAllList.add(awardStrAll);
		}
		String paramsStr = StringUtils.join(awardAllList, "\n");
		return paramsStr;
	}

	public String extractShiLianJiangLi(byte[] params) {
		if (params.length == 0) {
			return null;
		}
		DBActivityShiLianJiangLi dbActivityLevel = new DBActivityShiLianJiangLi();
		ProtostuffIOUtil.mergeFrom(params, dbActivityLevel, DBActivityShiLianJiangLi.getSchema());
		List<ActivityConfigOne> awardOnes = dbActivityLevel.getItemsList();
		List<String> awardAllList = new ArrayList<String>();
		for (ActivityConfigOne levelAward : awardOnes) {
			int level = levelAward.getLevel();
			awardAllList.add(String.valueOf(level));

			List<Award> awards = levelAward.getAwardList();
			List<String> awardStrList = new ArrayList<String>();
			for (Award award : awards) {
				String awardStr = StringUtils.join(new int[] { award.getItemId(), award.getItemCount() }, '-');
				awardStrList.add(awardStr);
			}
			String awardStrAll = StringUtils.join(awardStrList, '|');
			awardAllList.add(awardStrAll);
		}
		String paramsStr = StringUtils.join(awardAllList, "\n");
		return paramsStr;
	}

	public String extractChongZhiBang(byte[] params) {
		if (params.length == 0) {
			return null;
		}
		DBActivityChongZhiBang dbActivityLevel = new DBActivityChongZhiBang();
		ProtostuffIOUtil.mergeFrom(params, dbActivityLevel, DBActivityChongZhiBang.getSchema());
		List<ActivityConfigOne1> awardOnes = dbActivityLevel.getItemsList();
		List<String> awardAllList = new ArrayList<String>();
		for (ActivityConfigOne1 levelAward : awardOnes) {
			int level = levelAward.getLevel();
			awardAllList.add(String.valueOf(level));

			List<Award> awards = levelAward.getAwardList();
			List<String> awardStrList = new ArrayList<String>();
			for (Award award : awards) {
				String awardStr = StringUtils.join(new int[] { award.getItemId(), award.getItemCount() }, '-');
				awardStrList.add(awardStr);
			}
			String awardStrAll = StringUtils.join(awardStrList, '|');
			awardAllList.add(awardStrAll);
			// 额外奖励
			Integer extraLimit = levelAward.getExtraLimit();
			awardAllList.add(String.valueOf(extraLimit));
			awards = levelAward.getExtraLimitAwardList();
			if (awards != null && awards.size() > 0) {
				awardStrList = new ArrayList<String>();
				for (Award award : awards) {
					String awardStr = StringUtils.join(new int[] { award.getItemId(), award.getItemCount() }, '-');
					awardStrList.add(awardStr);
				}
				awardStrAll = StringUtils.join(awardStrList, '|');
				awardAllList.add(awardStrAll);
			}
		}
		String paramsStr = StringUtils.join(awardAllList, "\n");
		return paramsStr;
	}

	public String extractXiaoFeiBang(byte[] params) {
		if (params.length == 0) {
			return null;
		}
		DBActivityXiaoFeiBang dbActivityLevel = new DBActivityXiaoFeiBang();
		ProtostuffIOUtil.mergeFrom(params, dbActivityLevel, DBActivityXiaoFeiBang.getSchema());
		List<ActivityConfigOne1> awardOnes = dbActivityLevel.getItemsList();
		List<String> awardAllList = new ArrayList<String>();
		for (ActivityConfigOne1 levelAward : awardOnes) {
			int level = levelAward.getLevel();
			awardAllList.add(String.valueOf(level));

			List<Award> awards = levelAward.getAwardList();
			List<String> awardStrList = new ArrayList<String>();
			for (Award award : awards) {
				String awardStr = StringUtils.join(new int[] { award.getItemId(), award.getItemCount() }, '-');
				awardStrList.add(awardStr);
			}
			String awardStrAll = StringUtils.join(awardStrList, '|');
			awardAllList.add(awardStrAll);
			// 额外奖励
			Integer extraLimit = levelAward.getExtraLimit();
			awardAllList.add(String.valueOf(extraLimit));
			awards = levelAward.getExtraLimitAwardList();
			if (awards != null && awards.size() > 0) {
				awardStrList = new ArrayList<String>();
				for (Award award : awards) {
					String awardStr = StringUtils.join(new int[] { award.getItemId(), award.getItemCount() }, '-');
					awardStrList.add(awardStr);
				}
				awardStrAll = StringUtils.join(awardStrList, '|');
				awardAllList.add(awardStrAll);
			}
		}
		String paramsStr = StringUtils.join(awardAllList, "\n");
		return paramsStr;
	}

	public String extractDanBiChongZhi(byte[] params) {
		if (params.length == 0) {
			return null;
		}
		DBActivityCommon dbActivityLevel = new DBActivityCommon();
		ProtostuffIOUtil.mergeFrom(params, dbActivityLevel, DBActivityCommon.getSchema());
		List<ActivityConfigOne> awardOnes = dbActivityLevel.getItemsList();
		List<String> awardAllList = new ArrayList<String>();
		for (ActivityConfigOne levelAward : awardOnes) {
			int level = levelAward.getLevel();
			awardAllList.add(String.valueOf(level));

			List<Award> awards = levelAward.getAwardList();
			List<String> awardStrList = new ArrayList<String>();
			for (Award award : awards) {
				String awardStr = StringUtils.join(new int[] { award.getItemId(), award.getItemCount() }, '-');
				awardStrList.add(awardStr);
			}
			String awardStrAll = StringUtils.join(awardStrList, '|');
			awardAllList.add(awardStrAll);
		}
		String paramsStr = StringUtils.join(awardAllList, "\n");
		return paramsStr;
	}

	public String extractCrossBoss(byte[] params) {
		if (params.length == 0) {
			return null;
		}
		DBActivityCrossBoss dbActivityLevel = new DBActivityCrossBoss();
		ProtostuffIOUtil.mergeFrom(params, dbActivityLevel, DBActivityCrossBoss.getSchema());
		List<String> awardAllList = new ArrayList<String>();
		awardAllList.add(dbActivityLevel.getStartTimeMin().toString());
		awardAllList.add(dbActivityLevel.getEndTimeMin().toString());
		String paramsStr = StringUtils.join(awardAllList, "\n");
		return paramsStr;
	}

	public ActivityRetMsg buildCrossBoss(Activity4Gm activity4Gm, Model model) {
		try {
			// Thời gian bắt đầu và thời gian kết thúc xử lý
			int templateId = activity4Gm.getTemplateId();
			ActivityTemplate at = ActivityTemplateCache.getInstance().getActivityTemplate(templateId);
			setTime(activity4Gm, at);
			// Có nên bật không
			String isOpenStr = activity4Gm.getIsOpenStr();
			if (isOpenStr != null && isOpenStr.equals("on")) {
				activity4Gm.setIsOpen(1);
			} else {
				activity4Gm.setIsOpen(0);
			}
			// params
			String paramStr = activity4Gm.getParamsStr();
			String[] paramStrList = paramStr.split("\n");
			if (paramStrList.length % 2 != 0) {
				return new ActivityRetMsg("params error!", false);
			}
			DBActivityCrossBoss dbLevelAward = new DBActivityCrossBoss();
			dbLevelAward.setStartTimeMin(Integer.parseInt(paramStrList[0]));
			dbLevelAward.setEndTimeMin(Integer.parseInt(paramStrList[1]));
			// params to byte[]
			byte[] paramsBytes = ProtostuffIOUtil.toByteArray(dbLevelAward, DBActivityCrossBoss.getSchema(),
					CommonUtils.getProtoBuffer());
			activity4Gm.setParams(paramsBytes);

			// send to Game Server
			send2GameServer(model, activity4Gm);
			return new ActivityRetMsg("", true);
		} catch (ParseException e) {
			logger.error("", e);
			return new ActivityRetMsg("wrong time format!", false);
		}
	}

	public ActivityRetMsg buildTianTianHaoLi(Activity4Gm activity4Gm, Model model) {
		try {
			// Thời gian bắt đầu và thời gian kết thúc xử lý
			int templateId = activity4Gm.getTemplateId();
			ActivityTemplate at = ActivityTemplateCache.getInstance().getActivityTemplate(templateId);
			setTime(activity4Gm, at);
			// 是否开启
			String isOpenStr = activity4Gm.getIsOpenStr();
			if (isOpenStr != null && isOpenStr.equals("on")) {
				activity4Gm.setIsOpen(1);
			} else {
				activity4Gm.setIsOpen(0);
			}
			// params
			String paramStr = activity4Gm.getParamsStr();
			String[] paramStrList = paramStr.split("\n");
			if (paramStrList.length % 2 != 0) {
				return new ActivityRetMsg("params error!", false);
			}
			DBActivityTTHL dbRechargeAward = new DBActivityTTHL();
			List<ActivityConfigOne> awardOnes = new ArrayList<ActivityConfigOne>();
			dbRechargeAward.setItemsList(awardOnes);
			for (int i = 0; i < paramStrList.length; i += 2) {
				int rechargeLevel = Integer.valueOf(paramStrList[i]);
				ActivityConfigOne rechargeAwardOne = new ActivityConfigOne(rechargeLevel);
				String awardStr = paramStrList[i + 1];
				String[] awardStrList = StringUtils.split(awardStr, StringConstants.SEPARATOR_SHU);
				List<Award> awardList = new ArrayList<Award>();
				rechargeAwardOne.setAwardList(awardList);
				for (String awardStrPair : awardStrList) {
					String[] awardPairList = StringUtils.split(awardStrPair, StringConstants.SEPARATOR_HENG);
					int itemTplId = Integer.valueOf(awardPairList[0]);
					boolean itemTplExist = ItemTemplateCache.getInstance().checkExist(itemTplId);
					if (!itemTplExist) {
						throw new ItemNotExistException(itemTplId);
					}
					int itemCount = Integer.valueOf(awardPairList[1]);
					Award dbAward = new Award(itemTplId, itemCount);
					awardList.add(dbAward);
				}
				awardOnes.add(rechargeAwardOne);
			}
			// params to byte[]
			byte[] paramsBytes = ProtostuffIOUtil.toByteArray(dbRechargeAward, DBActivityTTHL.getSchema(),
					CommonUtils.getProtoBuffer());
			activity4Gm.setParams(paramsBytes);
			// send to Game Server
			send2GameServer(model, activity4Gm);
			return new ActivityRetMsg("", true);
		} catch (ParseException e) {
			logger.error("", e);
			return new ActivityRetMsg("wrong time format!" + e.getMessage(), false);
		} catch (NumberFormatException e) {
			logger.error("", e);
			return new ActivityRetMsg("wrong number format!" + e.getMessage(), false);
		} catch (ArrayIndexOutOfBoundsException e) {
			logger.error("", e);
			return new ActivityRetMsg("wrong award format!" + e.getMessage(), false);
		} catch (ItemNotExistException e) {
			logger.error("", e);
			return new ActivityRetMsg("wrong award format!item not exist:" + e.getMessage(), false);
		}
	}

	public String extractTianTianHaoLi(byte[] params) {
		if (params.length == 0) {
			return StringUtils.EMPTY;
		}
		DBActivityTTHL dbRechargeAward = new DBActivityTTHL();
		ProtostuffIOUtil.mergeFrom(params, dbRechargeAward, DBActivityTTHL.getSchema());
		List<ActivityConfigOne> awardOnes = dbRechargeAward.getItemsList();
		List<String> awardAllList = new ArrayList<String>();
		for (ActivityConfigOne rechargeAwardOne : awardOnes) {
			int rechargeCount = rechargeAwardOne.getLevel();
			awardAllList.add(String.valueOf(rechargeCount));
			List<Award> awards = rechargeAwardOne.getAwardList();
			List<String> awardStrList = new ArrayList<String>();
			for (Award award : awards) {
				String awardStr = StringUtils.join(new int[] { award.getItemId(), award.getItemCount() }, '-');
				awardStrList.add(awardStr);
			}
			String awardStrAll = StringUtils.join(awardStrList, '|');
			awardAllList.add(awardStrAll);
		}
		return StringUtils.join(awardAllList, "\n");
	}

	public ActivityRetMsg buildQingNianJiJin(Activity4Gm activity4Gm, Model model) {
		try {
			// 处理开始时间和结束时间
			int templateId = activity4Gm.getTemplateId();
			ActivityTemplate at = ActivityTemplateCache.getInstance().getActivityTemplate(templateId);
			setTime(activity4Gm, at);
			// 是否开启
			String isOpenStr = activity4Gm.getIsOpenStr();
			if (isOpenStr != null && isOpenStr.equals("on")) {
				activity4Gm.setIsOpen(1);
			} else {
				activity4Gm.setIsOpen(0);
			}
			// params
			String paramStr = activity4Gm.getParamsStr();
			String[] paramStrList = paramStr.split("\n");
			if (paramStrList.length % 2 != 0) {
				return new ActivityRetMsg("params error!", false);
			}
			DBActivityQCJJ dbLevelAward = new DBActivityQCJJ();
			List<ActivityConfigQCJJ> awardOnes = new ArrayList<ActivityConfigQCJJ>();
			dbLevelAward.setItemsList(awardOnes);
			for (int i = 0; i < paramStrList.length; i += 2) {
				ActivityConfigQCJJ levelAward = new ActivityConfigQCJJ();
				//
				String param1Str = paramStrList[i];
				String[] param1Lists = StringUtils.split(param1Str, StringConstants.SEPARATOR_HENG);
				int buy_count = Integer.valueOf(param1Lists[0]);
				int totalValue = Integer.valueOf(param1Lists[1]);
				int price = Integer.valueOf(param1Lists[2]);
				int recharge_id = Integer.valueOf(param1Lists[3]);
				int extra_diamond = Integer.valueOf(param1Lists[4]);
				levelAward.setBuyCount(buy_count);
				levelAward.setTotalValue(totalValue);
				levelAward.setPrice(price);
				levelAward.setRechargeId(recharge_id);
				levelAward.setExtraDiamond(extra_diamond);
				//
				List<Award> awardList = new ArrayList<Award>();
				levelAward.setAwardList(awardList);
				String awardStr = paramStrList[i + 1];
				String[] awardStrList = StringUtils.split(awardStr, StringConstants.SEPARATOR_SHU);
				for (String awardStrPair : awardStrList) {
					String[] awardPairList = StringUtils.split(awardStrPair, StringConstants.SEPARATOR_HENG);
					int itemTplId = Integer.valueOf(awardPairList[0]);
//					boolean itemTplExist = ItemTemplateCache.getInstance().checkExist(itemTplId);
//					if (!itemTplExist) {
//						try {
//							throw new ItemNotExistException(itemTplId);
//						} catch (ItemNotExistException e) {
//							e.printStackTrace();
//						}
//					}
					int itemCount = Integer.valueOf(awardPairList[1]);
					Award dbAward = new Award(itemTplId, itemCount);
					awardList.add(dbAward);
				}

				awardOnes.add(levelAward);
			}
			// params to byte[]
			byte[] paramsBytes = ProtostuffIOUtil.toByteArray(dbLevelAward, DBActivityQCJJ.getSchema(),
					CommonUtils.getProtoBuffer());
			activity4Gm.setParams(paramsBytes);

			// send to Game Server
			send2GameServer(model, activity4Gm);
			return new ActivityRetMsg("", true);
		} catch (ParseException e) {
			logger.error("", e);
			return new ActivityRetMsg("wrong time format!", false);
		}
	}

	public String extractQingChunJiJin(byte[] params) {
		if (params.length == 0) {
			return null;
		}
		DBActivityQCJJ dbActivityLevel = new DBActivityQCJJ();
		ProtostuffIOUtil.mergeFrom(params, dbActivityLevel, DBActivityQCJJ.getSchema());
		List<ActivityConfigQCJJ> awardOnes = dbActivityLevel.getItemsList();
		List<String> awardAllList = new ArrayList<String>();
		for (ActivityConfigQCJJ levelAward : awardOnes) {
			String param1Str = StringUtils.join(new int[] { levelAward.getBuyCount(), levelAward.getTotalValue(),
					levelAward.getPrice(), levelAward.getRechargeId(), levelAward.getExtraDiamond() }, '-');
			awardAllList.add(param1Str);
			List<Award> awards = levelAward.getAwardList();
			List<String> awardStrList = new ArrayList<String>();
			for (Award award : awards) {
				String awardStr = StringUtils.join(new int[] { award.getItemId(), award.getItemCount() }, '-');
				awardStrList.add(awardStr);
			}
			String awardStrAll = StringUtils.join(awardStrList, '|');
			awardAllList.add(awardStrAll);
		}
		String paramsStr = StringUtils.join(awardAllList, "\n");
		return paramsStr;
	}

	public String extractCommon(byte[] params) {
		if (params.length == 0) {
			return null;
		}
		DBActivityCommon dbActivityLevel = new DBActivityCommon();
		ProtostuffIOUtil.mergeFrom(params, dbActivityLevel, DBActivityCommon.getSchema());
		List<ActivityConfigOne> awardOnes = dbActivityLevel.getItemsList();
		List<String> awardAllList = new ArrayList<String>();
		for (ActivityConfigOne levelAward : awardOnes) {
			// int level = levelAward.getLevel();
			// awardAllList.add(String.valueOf(level));

			List<Award> awards = levelAward.getAwardList();
			List<String> awardStrList = new ArrayList<String>();
			for (Award award : awards) {
				String awardStr = StringUtils.join(new int[] { award.getItemId(), award.getItemCount() }, '-');
				awardStrList.add(awardStr);
			}
			String awardStrAll = StringUtils.join(awardStrList, '|');
			awardAllList.add(awardStrAll);
		}
		String paramsStr = StringUtils.join(awardAllList, "\n");
		return paramsStr;
	}

	public String extractCommonWithLevel(byte[] params) {
		if (params.length == 0) {
			return null;
		}
		DBActivityCommon dbActivityLevel = new DBActivityCommon();
		ProtostuffIOUtil.mergeFrom(params, dbActivityLevel, DBActivityCommon.getSchema());
		List<ActivityConfigOne> awardOnes = dbActivityLevel.getItemsList();
		List<String> awardAllList = new ArrayList<String>();
		for (ActivityConfigOne levelAward : awardOnes) {
			 int level = levelAward.getLevel();
			 awardAllList.add(String.valueOf(level));
			 //
			List<Award> awards = levelAward.getAwardList();
			List<String> awardStrList = new ArrayList<String>();
			for (Award award : awards) {
				String awardStr = StringUtils.join(new int[] { award.getItemId(), award.getItemCount() }, '-');
				awardStrList.add(awardStr);
			}
			String awardStrAll = StringUtils.join(awardStrList, '|');
			awardAllList.add(awardStrAll);
		}
		String paramsStr = StringUtils.join(awardAllList, "\n");
		return paramsStr;
	}

	public ActivityRetMsg buildCommonWithLevel(Activity4Gm activity4Gm, Model model) {
		try {
			// 处理开始时间和结束时间
			int templateId = activity4Gm.getTemplateId();
			ActivityTemplate at = ActivityTemplateCache.getInstance().getActivityTemplate(templateId);
			setTime(activity4Gm, at);
			// 是否开启
			String isOpenStr = activity4Gm.getIsOpenStr();
			if (isOpenStr != null && isOpenStr.equals("on")) {
				activity4Gm.setIsOpen(1);
			} else {
				activity4Gm.setIsOpen(0);
			}
			// params
			String paramStr = activity4Gm.getParamsStr();
			String[] paramStrList = paramStr.split("\n");
			// if (paramStrList.length % 2 != 0) {
			// return new ActivityRetMsg("params error!", false);
			// }
			DBActivityCommon dbRechargeAward = new DBActivityCommon();
			List<ActivityConfigOne> awardOnes = new ArrayList<ActivityConfigOne>();
			dbRechargeAward.setItemsList(awardOnes);
			for (int i = 0; i < paramStrList.length; i += 2) {
				 int configLevel = Integer.valueOf(paramStrList[i]);
				ActivityConfigOne rechargeAwardOne = new ActivityConfigOne(configLevel);
				String awardStr = paramStrList[i+1];
				String[] awardStrList = StringUtils.split(awardStr, StringConstants.SEPARATOR_SHU);
				List<Award> awardList = new ArrayList<Award>();
				rechargeAwardOne.setAwardList(awardList);
				for (String awardStrPair : awardStrList) {
					String[] awardPairList = StringUtils.split(awardStrPair, StringConstants.SEPARATOR_HENG);
					int itemTplId = Integer.valueOf(awardPairList[0]);
					int itemCount = Integer.valueOf(awardPairList[1]);
					Award dbAward = new Award(itemTplId, itemCount);
					awardList.add(dbAward);
				}
				awardOnes.add(rechargeAwardOne);
			}
			// params to byte[]
			byte[] paramsBytes = ProtostuffIOUtil.toByteArray(dbRechargeAward, DBActivityCommon.getSchema(),
					CommonUtils.getProtoBuffer());
			activity4Gm.setParams(paramsBytes);
			// send to Game Server
			send2GameServer(model, activity4Gm);
			return new ActivityRetMsg("", true);
		} catch (ParseException e) {
			logger.error("", e);
			return new ActivityRetMsg("wrong time format!" + e.getMessage(), false);
		} catch (NumberFormatException e) {
			logger.error("", e);
			return new ActivityRetMsg("wrong number format!" + e.getMessage(), false);
		} catch (ArrayIndexOutOfBoundsException e) {
			logger.error("", e);
			return new ActivityRetMsg("wrong award format!" + e.getMessage(), false);
		}
	}

}
