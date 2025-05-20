package game.module.sign.logic;

import game.GameServer;
import game.entity.PlayingRole;
import game.module.award.logic.AwardUtils;
import game.module.sign.bean.SignIn;
import game.module.sign.dao.SignDao;
import game.module.sign.dao.SignInCache;
import game.module.user.bean.PlayerProp;
import game.module.user.dao.PlayerOtherCache;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Map;

/**
 * 签到逻辑处理
 *
 * @author zhangning
 * @Date 2015年1月12日 下午7:02:30
 */
public class SignManager {

    private static Logger logger = LoggerFactory.getLogger(SignManager.class);

    static class SingletonHolder {
        static SignManager instance = new SignManager();
    }

    public static SignManager getInstance() {
        return SingletonHolder.instance;
    }

    public static final int SIGN_GET_MARK = 90223;

    public static final int SIGN_COUNT_MARK = 90215;

    public void loginUpdateSign(PlayingRole playingRole) {
        int playerId = playingRole.getId();
        SignIn signIn = SignInCache.getInstance().getSign(playerId);
        Date now = new Date();
        if (signIn == null) {
            signIn = new SignIn();
            signIn.setPlayerId(playerId);
            signIn.setLastSignTime(now);
            SignIn finalSignIn = signIn;
            GameServer.executorService.execute(() -> SignDao.getInstance().addSignIn(finalSignIn));
            SignInCache.getInstance().addOneSign(signIn);
            AwardUtils.setRes(playingRole, SignManager.SIGN_COUNT_MARK, 1, false);
        } else if (!DateUtils.isSameDay(now, signIn.getLastSignTime())) {
            signIn.setLastSignTime(now);
            int signCount = 0;
            Map<Integer, PlayerProp> playerOthers = PlayerOtherCache.getInstance().getPlayerOther(playerId);
            if (playerOthers.containsKey(SignManager.SIGN_COUNT_MARK)) {
                signCount = playerOthers.get(SignManager.SIGN_COUNT_MARK).getCount();
            }
            AwardUtils.setRes(playingRole, SignManager.SIGN_COUNT_MARK, signCount + 1, false);
            //
            signIn.setLastSignTime(now);
            SignIn finalSignIn1 = signIn;
            GameServer.executorService.execute(() -> SignDao.getInstance().updateSign(finalSignIn1));
        }
    }
}
