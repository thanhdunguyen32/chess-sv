package login.logic;

import lion.netty4.message.GamePlayer;
import login.LoginServer;
import login.bean.LianYunLoginSessionBean;
import login.bean.LoginClientSessionBean;
import login.bean.QqLoginSessionBean;
import login.cache.LoginSessionCache;
import login.dao.LoginAccountDao;
import login.stat.LoginStat;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sdk.zy.*;
import ws.WsMessageHall.S2CErrorCode;
import ws.WsMessageLogin;
import ws.WsMessageLogin.S2CGuestLogin;
import ws.WsMessageLogin.S2CH5OpenLogin;
import ws.WsMessageLogin.S2COpenLogin;

import java.util.Map;

public class LoginAccountManager {

	private static Logger logger = LoggerFactory.getLogger(LoginAccountManager.class);

	static class SingletonHolder {
		static LoginAccountManager instance = new LoginAccountManager();
	}

	public static LoginAccountManager getInstance() {
		return SingletonHolder.instance;
	}

	public void insert1KAccount() {
		for (int i = 0; i < 1000; i++) {
			LoginAccountDao.getInstance().insertLoginAccount(String.valueOf(i), String.valueOf(1));
		}
	}

	public void yijieLogin(final GamePlayer gamePlayer, final String appId, final String channelid, final String userId,
			final String token) {
		LoginServer.executorService.execute(new Runnable() {
			@Override
			public void run() {
				try {
					// String appId = "BEAE887BE98E168D";
					// String appId = "009318338DE9357E";
					String newAppId = appId;
					newAppId = StringUtils.remove(newAppId, '{');
					newAppId = StringUtils.remove(newAppId, '}');
					newAppId = StringUtils.remove(newAppId, '-');
					String dalanUrlBase = "http://sync.1sdk.cn/login/check.html";
					String newChannelid = StringUtils.remove(channelid, '{');
					newChannelid = StringUtils.remove(newChannelid, '}');
					newChannelid = StringUtils.remove(newChannelid, '-');
					String retStr = YijieApi.getInstance().verifylogin(newChannelid, newAppId, userId, token,
							dalanUrlBase);
					if (retStr == null) {
						logger.warn("network error!");
						S2CErrorCode respMsg = new S2CErrorCode(S2CH5OpenLogin.msgCode, 104);
						gamePlayer.writeAndFlush(respMsg.build(gamePlayer.alloc()));
						return;
					}
					logger.info("yijie inspect session result={}", retStr);
					if (!retStr.equals("0")) {
						logger.warn("error:{}", retStr);
						S2CErrorCode respMsg = new S2CErrorCode(S2CH5OpenLogin.msgCode, 104);
						gamePlayer.writeAndFlush(respMsg.build(gamePlayer.alloc()));
						return;
					}
					long sessionId = LoginServer.getInstance().generateRandomId();
					LoginSessionCache.getInstance().addSession(sessionId, userId);
					S2CH5OpenLogin respMsg = new S2CH5OpenLogin(sessionId);
					gamePlayer.writeAndFlush(respMsg.build(gamePlayer.alloc()));
					LoginStat.getInstance().statLogin(userId);
				} catch (Exception e) {
					logger.error("", e);
					S2CErrorCode respMsg = new S2CErrorCode(S2CH5OpenLogin.msgCode, 104);
					gamePlayer.writeAndFlush(respMsg.build(gamePlayer.alloc()));
					return;
				}
			}
		});
	}

	public void loginRaw(final GamePlayer gamePlayer, final String userName, final String pwd) {
		LoginServer.executorService.execute(() -> {
			boolean existUser = LoginAccountDao.getInstance().checkExist(userName, pwd);
			if (!existUser) {
				// Tự động tạo tài khoản mới nếu chưa tồn tại
				int newAccountId = LoginAccountDao.getInstance().insertLoginAccount(userName, "1");
				if (newAccountId > 0) {
					// Tạo session cho tài khoản mới
					long sessionId = LoginServer.getInstance().generateRandomId();
					LoginSessionCache.getInstance().addSession(sessionId, String.valueOf(newAccountId));
					// Trả về thông tin đăng nhập
					WsMessageLogin.S2CTestLogin respMsg = new WsMessageLogin.S2CTestLogin(newAccountId,sessionId);
					gamePlayer.writeAndFlush(respMsg.build(gamePlayer.alloc()));
					LoginStat.getInstance().statLogin(userName);
				} else {
					// Nếu tạo tài khoản thất bại
					S2CErrorCode error_code = new S2CErrorCode(WsMessageLogin.S2CTestLogin.msgCode, 103);
					gamePlayer.writeAndFlush(error_code.build(gamePlayer.alloc()));
				}
			} else {
				int uid = LoginAccountDao.getInstance().getUidByNamePwd(userName, pwd);
				long sessionId = LoginServer.getInstance().generateRandomId();
				//save login session
				LoginSessionCache.getInstance().addSession(sessionId, String.valueOf(uid));
				//ret
				WsMessageLogin.S2CTestLogin respMsg = new WsMessageLogin.S2CTestLogin(uid,sessionId);
				gamePlayer.writeAndFlush(respMsg.build(gamePlayer.alloc()));
				LoginStat.getInstance().statLogin(userName);
			}
		});
	}

	public void guestLogin(final GamePlayer gamePlayer, final int accountId) {
		LoginServer.executorService.execute(new Runnable() {
			@Override
			public void run() {
				// 创建账号
				int targetAccount = accountId;
				if (targetAccount == 0) {
					targetAccount = LoginAccountDao.getInstance().insertLoginAccount("guest", "1");
				} else {
					// checkExist
					boolean existUser = LoginAccountDao.getInstance().checkGuestExist(targetAccount);
					if (!existUser) {
						S2CErrorCode error_code = new S2CErrorCode(S2CGuestLogin.msgCode, 103);
						gamePlayer.writeAndFlush(error_code.build(gamePlayer.alloc()));
						return;
					}
				}
				long sessionId = LoginServer.getInstance().generateRandomId();
				// tmp save
				LoginSessionCache.getInstance().addSession(sessionId, String.valueOf(targetAccount));
				S2CGuestLogin respBuilder = new S2CGuestLogin(targetAccount,sessionId);
				gamePlayer.writeAndFlush(respBuilder.build(gamePlayer.alloc()));
				LoginStat.getInstance().statLogin(String.valueOf(targetAccount));
			}
		});
	}

	public void openLogin(GamePlayer gamePlayer, int platform_type, String open_id) {
		long sessionId = LoginServer.getInstance().generateRandomId();
		LoginSessionCache.getInstance().addSession(sessionId, open_id);
		// ret
		S2COpenLogin respMsg = new S2COpenLogin(open_id, sessionId,platform_type);
		gamePlayer.writeAndFlush(respMsg.build(gamePlayer.alloc()));
		LoginStat.getInstance().statLogin(open_id);
	}

	public void qunheiLogin(GamePlayer gamePlayer, String username, int serverid, int isadult, int time, String flag) {
		// 判断sigh是否正确
		String qunheiLoginkey = "";
		String md5RawStr = username + serverid + isadult + time
				+ qunheiLoginkey;
		logger.info("md5RawStr={}", md5RawStr);
		String mySighStr = DigestUtils.md5Hex(md5RawStr);
		logger.info("my sign={}", mySighStr);
		if (!mySighStr.equalsIgnoreCase(flag)) {
			S2CErrorCode respMsg = new S2CErrorCode(S2CH5OpenLogin.msgCode, 104);
			gamePlayer.writeAndFlush(respMsg.build(gamePlayer.alloc()));
			return;
		}
		// do
		long sessionId = LoginServer.getInstance().generateRandomId();
		LoginSessionCache.getInstance().addSession(sessionId, username);
		// ret
		S2CH5OpenLogin respMsg = new S2CH5OpenLogin(sessionId);
		gamePlayer.writeAndFlush(respMsg.build(gamePlayer.alloc()));
		LoginStat.getInstance().statLogin(username);
	}

	public void wanbaLogin(final GamePlayer gamePlayer, final String appid, final String openid, final String openkey,
			final int platform, final String pf) {
		LoginServer.executorService.execute(new Runnable() {
			@Override
			public void run() {
				try {
					String appKey = "rAXRn6TTAcGVId2j" + "&";
					String wanbaUrlBase = "https://api.urlshare.cn";
					Map<String, Object> retMap = WanbaOpenApi.getInstance().checkLogin(openid, openkey, appid, pf,
							wanbaUrlBase, appKey);
					if (retMap == null) {
						logger.warn("network error!");
						S2CErrorCode error_code = new S2CErrorCode(S2CH5OpenLogin.msgCode, 103);
						gamePlayer.writeAndFlush(error_code.build(gamePlayer.alloc()));
						return;
					}
					logger.info("wan ba login verify result={}", retMap);
					if (!retMap.containsKey("ret") || (int) (retMap.get("ret")) != 0) {
						S2CErrorCode error_code = new S2CErrorCode(S2CH5OpenLogin.msgCode, 104);
						gamePlayer.writeAndFlush(error_code.build(gamePlayer.alloc()));
						return;
					}
					long sessionId = LoginServer.getInstance().generateRandomId();
					LoginSessionCache.getInstance().addSession(sessionId, platform + "-" + openid);
					// ret
					S2CH5OpenLogin respMsg = new S2CH5OpenLogin(sessionId);
					gamePlayer.writeAndFlush(respMsg.build(gamePlayer.alloc()));
					LoginStat.getInstance().statLogin(openid);
				} catch (Exception e) {
					logger.error("", e);
					S2CErrorCode error_code = new S2CErrorCode(S2CH5OpenLogin.msgCode, 113);
					gamePlayer.writeAndFlush(error_code.build(gamePlayer.alloc()));
				}
			}
		});
	}

	public void zhangmengLogin(GamePlayer gamePlayer, String t, String uid, String sign) {
		// 判断sigh是否正确
		String secret_key = "71fde6d7928fb9b471097e8f59be34ba";
		String md5RawStr = String.format("secret_key=%1$s&t=%2$s&uid=%3$s", secret_key, t, uid);
		logger.info("md5RawStr={}", md5RawStr);
		String mySighStr = DigestUtils.md5Hex(md5RawStr);
		logger.info("my sign={}", mySighStr);
		if (!mySighStr.equalsIgnoreCase(sign)) {
			S2CErrorCode respMsg = new S2CErrorCode(S2CH5OpenLogin.msgCode, 104);
			gamePlayer.writeAndFlush(respMsg.build(gamePlayer.alloc()));
			return;
		}
		// do
		long sessionId = LoginServer.getInstance().generateRandomId();
		LoginSessionCache.getInstance().addSession(sessionId, uid);
		// ret
		S2CH5OpenLogin respMsg = new S2CH5OpenLogin(sessionId);
		gamePlayer.writeAndFlush(respMsg.build(gamePlayer.alloc()));
		LoginStat.getInstance().statLogin(uid);
	}

	public void three33Login(GamePlayer gamePlayer, int gameId, int time, int uid, String userName, String sign) {
		// 判断sigh是否正确
		String login_key = "9e7f17d6ebba2d447cee190fc650145e";
		String md5RawStr = String.format("gameId=%1$d&time=%2$d&uid=%3$d&userName=%4$s&key=%5$s", gameId, time, uid,
				userName, login_key);
		logger.info("md5RawStr={}", md5RawStr);
		String mySighStr = DigestUtils.md5Hex(md5RawStr);
		logger.info("my sign={}", mySighStr);
		if (!mySighStr.equalsIgnoreCase(sign)) {
			S2CErrorCode respMsg = new S2CErrorCode(S2CH5OpenLogin.msgCode, 104);
			gamePlayer.writeAndFlush(respMsg.build(gamePlayer.alloc()));
			return;
		}
		// do
		long sessionId = LoginServer.getInstance().generateRandomId();
		LoginSessionCache.getInstance().addSession(sessionId, String.valueOf(uid));
		// ret
		S2CH5OpenLogin respMsg = new S2CH5OpenLogin(sessionId);
		gamePlayer.writeAndFlush(respMsg.build(gamePlayer.alloc()));
		LoginStat.getInstance().statLogin(String.valueOf(uid));
	}

	public void chongchongLogin(GamePlayer gamePlayer, String userId, String time, String sign) {
		// 判断sigh是否正确
		int appId = 25384;
		String login_key = "9c9e83fe236bdb3f0d4ba57014173efa";
		String md5RawStr = String.format("appId=%1$dtime=%2$suserId=%3$s%4$s", appId, time, userId, login_key);
		logger.info("md5RawStr={}", md5RawStr);
		String mySighStr = DigestUtils.md5Hex(md5RawStr);
		logger.info("my sign={}", mySighStr);
		if (!mySighStr.equalsIgnoreCase(sign)) {
			S2CErrorCode respMsg = new S2CErrorCode(S2CH5OpenLogin.msgCode, 104);
			gamePlayer.writeAndFlush(respMsg.build(gamePlayer.alloc()));
			return;
		}
		// do
		long sessionId = LoginServer.getInstance().generateRandomId();
		LoginSessionCache.getInstance().addSession(sessionId, userId);
		// ret
		S2CH5OpenLogin respMsg = new S2CH5OpenLogin(sessionId);
		gamePlayer.writeAndFlush(respMsg.build(gamePlayer.alloc()));
		LoginStat.getInstance().statLogin(userId);
	}

	public void open4399Login(GamePlayer gamePlayer, String userId, int time, String sign, int gameId,
			String userName) {
		// 判断sigh是否正确
		String callback_key = "608fb2210cc52bdf6376cc9e415bd58a";
		String md5RawStr = String.format("gameId=%1$dtime=%2$duserId=%3$suserName=%4$s%5$s", gameId, time, userId,
				userName, callback_key);
		logger.info("md5RawStr={}", md5RawStr);
		String mySighStr = DigestUtils.md5Hex(md5RawStr);
		logger.info("my sign={}", mySighStr);
		if (!mySighStr.equalsIgnoreCase(sign)) {
			S2CErrorCode respMsg = new S2CErrorCode(S2CH5OpenLogin.msgCode, 104);
			gamePlayer.writeAndFlush(respMsg.build(gamePlayer.alloc()));
			return;
		}
		// do
		long sessionId = LoginServer.getInstance().generateRandomId();
		LoginSessionCache.getInstance().addSession(sessionId, userId);
		// ret
		S2CH5OpenLogin respMsg = new S2CH5OpenLogin(sessionId);
		gamePlayer.writeAndFlush(respMsg.build(gamePlayer.alloc()));
		LoginStat.getInstance().statLogin(userId);
	}

	public void yunbeeLogin(final GamePlayer gamePlayer, final String user_id, final String token) {
		LoginServer.executorService.execute(new Runnable() {
			@Override
			public void run() {
				try {
					String dalanUrlBase = "http://ios.service.yunbee.cn/sdk.php/LoginNotify/login_verify";
					Map<String, Object> verifyResult = YunbeeApi.getInstance().verifylogin(user_id, token,
							dalanUrlBase);
					if (verifyResult == null) {
						logger.warn("network error!");
						S2CErrorCode respMsg = new S2CErrorCode(S2CH5OpenLogin.msgCode, 104);
						gamePlayer.writeAndFlush(respMsg.build(gamePlayer.alloc()));
						return;
					}
					logger.info("yunbee inspect session result={}", verifyResult);
					int statusCode = (int) verifyResult.get("status");
					if (statusCode != 1) {
						logger.warn("error:{}", statusCode);
						S2CErrorCode respMsg = new S2CErrorCode(S2CH5OpenLogin.msgCode, 104);
						gamePlayer.writeAndFlush(respMsg.build(gamePlayer.alloc()));
						return;
					}
					// 获取用户id
					long sessionId = LoginServer.getInstance().generateRandomId();
					LoginSessionCache.getInstance().addSession(sessionId, user_id);
					S2CH5OpenLogin respMsg = new S2CH5OpenLogin(sessionId);
					gamePlayer.writeAndFlush(respMsg.build(gamePlayer.alloc()));
					LoginStat.getInstance().statLogin(user_id);
				} catch (Exception e) {
					logger.error("", e);
					S2CErrorCode respMsg = new S2CErrorCode(S2CH5OpenLogin.msgCode, 104);
					gamePlayer.writeAndFlush(respMsg.build(gamePlayer.alloc()));
					return;
				}
			}
		});
	}

	public void liuyeLogin(GamePlayer gamePlayer, String mem_id, String user_token, String app_id, String app_key) {
		LoginServer.executorService.execute(new Runnable() {
			@Override
			public void run() {
				try {
					String dalanUrlBase = "https://dyhapi.1tsdk.com/api/v7/cp/user/check";
					Map<String, Object> verifyResult = LiuyeOpenApi.getInstance().checkLogin(app_id, mem_id, user_token,
							dalanUrlBase, app_key);
					if (verifyResult == null) {
						logger.warn("network error!");
						S2CErrorCode respMsg = new S2CErrorCode(S2CH5OpenLogin.msgCode, 104);
						gamePlayer.writeAndFlush(respMsg.build(gamePlayer.alloc()));
						return;
					}
					logger.info("yunbee inspect session result={}", verifyResult);
					String statusCode = (String) verifyResult.get("status");
					if (!statusCode.equals("1")) {
						logger.warn("error:{}", statusCode);
						S2CErrorCode respMsg = new S2CErrorCode(S2CH5OpenLogin.msgCode, 104);
						gamePlayer.writeAndFlush(respMsg.build(gamePlayer.alloc()));
						return;
					}
					// 获取用户id
					long sessionId = LoginServer.getInstance().generateRandomId();
					LoginSessionCache.getInstance().addSession(sessionId, mem_id);
					S2CH5OpenLogin respMsg = new S2CH5OpenLogin(sessionId);
					gamePlayer.writeAndFlush(respMsg.build(gamePlayer.alloc()));
					LoginStat.getInstance().statLogin(mem_id);
				} catch (Exception e) {
					logger.error("", e);
					S2CErrorCode respMsg = new S2CErrorCode(S2CH5OpenLogin.msgCode, 104);
					gamePlayer.writeAndFlush(respMsg.build(gamePlayer.alloc()));
					return;
				}
			}
		});
	}

	public void pingPingLogin(GamePlayer gamePlayer, String game_id, String user_code, String login_token, String game_key) {
		LoginServer.executorService.execute(new Runnable() {
			@Override
			public void run() {
				try {
					String dalanUrlBase = "http://106.75.141.116/Api/Login/CheckLogin";
					Map<String, Object> verifyResult = PingPingApi.getInstance().verifylogin(game_id, user_code, login_token, dalanUrlBase, game_key);
					if (verifyResult == null) {
						logger.warn("network error!");
						S2CErrorCode respMsg = new S2CErrorCode(S2CH5OpenLogin.msgCode, 104);
						gamePlayer.writeAndFlush(respMsg.build(gamePlayer.alloc()));
						return;
					}
					logger.info("pingping inspect session result={}", verifyResult);
					Integer statusCode = (Integer) verifyResult.get("Status");
					if (statusCode != 0) {
						logger.warn("error:{}", statusCode);
						S2CErrorCode respMsg = new S2CErrorCode(S2CH5OpenLogin.msgCode, 104);
						gamePlayer.writeAndFlush(respMsg.build(gamePlayer.alloc()));
						return;
					}
					// 获取用户id
					long sessionId = LoginServer.getInstance().generateRandomId();
					LoginSessionCache.getInstance().addSession(sessionId, "pp-"+user_code);
					S2CH5OpenLogin respMsg = new S2CH5OpenLogin(sessionId);
					gamePlayer.writeAndFlush(respMsg.build(gamePlayer.alloc()));
					LoginStat.getInstance().statLogin(user_code);
				} catch (Exception e) {
					logger.error("", e);
					S2CErrorCode respMsg = new S2CErrorCode(S2CH5OpenLogin.msgCode, 104);
					gamePlayer.writeAndFlush(respMsg.build(gamePlayer.alloc()));
					return;
				}
			}
		});
	}

	public void yijieSwitchLogin(GamePlayer gamePlayer, String appId, String uin, String token, String appKey, String uidPrefix) {
		LoginServer.executorService.execute(new Runnable() {
			@Override
			public void run() {
				try {
					String dalanUrlBase = "http://119.3.12.107:8080/payserver/account/check_login";
					String retStr = XingTengApi.getInstance().verifylogin(appId, uin, token,System.currentTimeMillis(),
							dalanUrlBase,appKey);
					if (retStr == null) {
						logger.warn("network error!");
						S2CErrorCode respMsg = new S2CErrorCode(S2CH5OpenLogin.msgCode, 104);
						gamePlayer.writeAndFlush(respMsg.build(gamePlayer.alloc()));
						return;
					}
					logger.info("xingteng inspect session result={}", retStr);
					if (!retStr.equals("0")) {
						logger.warn("error:{}", retStr);
						S2CErrorCode respMsg = new S2CErrorCode(S2CH5OpenLogin.msgCode, 104);
						gamePlayer.writeAndFlush(respMsg.build(gamePlayer.alloc()));
						return;
					}
					long sessionId = LoginServer.getInstance().generateRandomId();
					LoginSessionCache.getInstance().addSession(sessionId, uidPrefix+uin);
					S2CH5OpenLogin respMsg = new S2CH5OpenLogin(sessionId);
					gamePlayer.writeAndFlush(respMsg.build(gamePlayer.alloc()));
					LoginStat.getInstance().statLogin(uin);
				} catch (Exception e) {
					logger.error("", e);
					S2CErrorCode respMsg = new S2CErrorCode(S2CH5OpenLogin.msgCode, 104);
					gamePlayer.writeAndFlush(respMsg.build(gamePlayer.alloc()));
					return;
				}
			}
		});
	}

	public void quLeLeLogin(GamePlayer gamePlayer, String appId, String user_id, String session_id, String uidPrefix) {
		LoginServer.executorService.execute(new Runnable() {
			@Override
			public void run() {
				try {
					String dalanUrlBase = "http://gs1.douwanplay.com/admin.php/index/sdkuser/login_verify";
					String appKey = "9e8c1c2f2dc78796c94a78f760526f42";
					Map<String, Object> retStr = QuLeLeApi.getInstance().verifylogin(appId, session_id, appKey, dalanUrlBase);
					if (retStr == null) {
						logger.warn("network error!");
						S2CErrorCode respMsg = new S2CErrorCode(S2CH5OpenLogin.msgCode, 104);
						gamePlayer.writeAndFlush(respMsg.build(gamePlayer.alloc()));
						return;
					}
					logger.info("xingteng inspect session result={}", retStr);
					if (retStr.get("code") == null || !((String)(retStr.get("code"))).equals("0")) {
						logger.warn("error:{}", retStr);
						S2CErrorCode respMsg = new S2CErrorCode(S2CH5OpenLogin.msgCode, 104);
						gamePlayer.writeAndFlush(respMsg.build(gamePlayer.alloc()));
						return;
					}
					long sessionId = LoginServer.getInstance().generateRandomId();
					LoginSessionCache.getInstance().addSession(sessionId, uidPrefix+user_id);
					S2CH5OpenLogin respMsg = new S2CH5OpenLogin(sessionId);
					gamePlayer.writeAndFlush(respMsg.build(gamePlayer.alloc()));
					LoginStat.getInstance().statLogin(user_id);
				} catch (Exception e) {
					logger.error("", e);
					S2CErrorCode respMsg = new S2CErrorCode(S2CH5OpenLogin.msgCode, 104);
					gamePlayer.writeAndFlush(respMsg.build(gamePlayer.alloc()));
					return;
				}
			}
		});
	}

	public void miaoRenLogin(GamePlayer gamePlayer, String appId, String account_id, String token_key, String uidPrefix) {
		LoginServer.executorService.execute(new Runnable() {
			@Override
			public void run() {
				try {
					String dalanUrlBase = "http://sdk.miaorenhy.com/api.php/index/checktoken";
					Map<String, Object> retStr = MoXiApi.getInstance().verifylogin(appId, account_id, token_key, dalanUrlBase);
					if (retStr == null) {
						logger.warn("network error!");
						S2CErrorCode respMsg = new S2CErrorCode(S2CH5OpenLogin.msgCode, 104);
						gamePlayer.writeAndFlush(respMsg.build(gamePlayer.alloc()));
						return;
					}
					logger.info("xingteng inspect session result={}", retStr);
					if (retStr.get("errorcode") == null || !((String)(retStr.get("errorcode"))).equals("1")) {
						logger.warn("error:{}", retStr);
						S2CErrorCode respMsg = new S2CErrorCode(S2CH5OpenLogin.msgCode, 104);
						gamePlayer.writeAndFlush(respMsg.build(gamePlayer.alloc()));
						return;
					}
					long sessionId = LoginServer.getInstance().generateRandomId();
					LoginSessionCache.getInstance().addSession(sessionId, uidPrefix+account_id);
					S2CH5OpenLogin respMsg = new S2CH5OpenLogin(sessionId);
					gamePlayer.writeAndFlush(respMsg.build(gamePlayer.alloc()));
					LoginStat.getInstance().statLogin(account_id);
				} catch (Exception e) {
					logger.error("", e);
					S2CErrorCode respMsg = new S2CErrorCode(S2CH5OpenLogin.msgCode, 104);
					gamePlayer.writeAndFlush(respMsg.build(gamePlayer.alloc()));
					return;
				}
			}
		});
	}

	public void quickLogin(GamePlayer gamePlayer, String token, String product_code, String uid, String channel_code) {
		LoginServer.executorService.execute(new Runnable() {
			@Override
			public void run() {
				try {
					String dalanUrlBase = "http://checkuser.sdk.quicksdk.net/v2/checkUserInfo";
					String retStr = QuickApi.getInstance().verifylogin(token, product_code, uid, dalanUrlBase);
					if (retStr == null) {
						logger.warn("network error!");
						S2CErrorCode respMsg = new S2CErrorCode(S2CH5OpenLogin.msgCode, 104);
						gamePlayer.writeAndFlush(respMsg.build(gamePlayer.alloc()));
						return;
					}
					logger.info("quick inspect session result={}", retStr);
					if (!retStr.equals("1")) {
						logger.warn("error:{}", retStr);
						S2CErrorCode respMsg = new S2CErrorCode(S2CH5OpenLogin.msgCode, 104);
						gamePlayer.writeAndFlush(respMsg.build(gamePlayer.alloc()));
						return;
					}
					long sessionId = LoginServer.getInstance().generateRandomId();
					//save login session
					LoginSessionCache.getInstance().addSession(sessionId, channel_code+"-"+uid);
					S2CH5OpenLogin respMsg = new S2CH5OpenLogin(sessionId);
					gamePlayer.writeAndFlush(respMsg.build(gamePlayer.alloc()));
					LoginStat.getInstance().statLogin(channel_code+"-"+uid);
				} catch (Exception e) {
					logger.error("", e);
					S2CErrorCode respMsg = new S2CErrorCode(S2CH5OpenLogin.msgCode, 104);
					gamePlayer.writeAndFlush(respMsg.build(gamePlayer.alloc()));
					return;
				}
			}
		});
	}

	public void o185syLogin(GamePlayer gamePlayer, String userID, String token) {
		LoginServer.executorService.execute(new Runnable() {
			@Override
			public void run() {
				try {
					String dalanUrlBase = "http://dev.185sy.com/user/verify";
					String RH_AppSecret = "7d2b3dfbb8c11a8f2c5551cd06cd5c23";
					Map<String, Object> ret = O185SyRhApi.getInstance().verifylogin(userID, token, RH_AppSecret,
							dalanUrlBase);
					if (ret == null) {
						logger.warn("network error!");
						S2CErrorCode respMsg = new S2CErrorCode(S2CH5OpenLogin.msgCode, 104);
						gamePlayer.writeAndFlush(respMsg.build(gamePlayer.alloc()));
						return;
					}
					logger.info("185sy rh inspect session result={}", ret);
					if (!ret.containsKey("state") || (Integer) ret.get("state") != 1) {
						logger.warn("error:{}", ret);
						S2CErrorCode respMsg = new S2CErrorCode(S2CH5OpenLogin.msgCode, 104);
						gamePlayer.writeAndFlush(respMsg.build(gamePlayer.alloc()));
						return;
					}
					long sessionId = LoginServer.getInstance().generateRandomId();
					LoginSessionCache.getInstance().addSession(sessionId, userID);
					S2CH5OpenLogin respMsg = new S2CH5OpenLogin(sessionId);
					gamePlayer.writeAndFlush(respMsg.build(gamePlayer.alloc()));
					LoginStat.getInstance().statLogin(userID);
				} catch (Exception e) {
					logger.error("", e);
					S2CErrorCode respMsg = new S2CErrorCode(S2CH5OpenLogin.msgCode, 104);
					gamePlayer.writeAndFlush(respMsg.build(gamePlayer.alloc()));
					return;
				}
			}
		});
	}

}
