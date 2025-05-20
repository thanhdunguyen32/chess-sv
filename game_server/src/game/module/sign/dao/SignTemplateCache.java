package game.module.sign.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.io.Files;
import game.common.excel.ExcelTemplateAnn;
import game.module.sign.bean.SignInRewardsTemplate;
import lion.common.JacksonUtils;
import lion.common.Reloadable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 签到：配置文件缓存
 *
 * @author zhangning
 * @Date 2015年1月12日 下午6:50:37
 */
public class SignTemplateCache implements Reloadable {

    private static Logger logger = LoggerFactory.getLogger(SignTemplateCache.class);

    static class SingletonHolder {
        static SignTemplateCache instance = new SignTemplateCache();
    }

    public static SignTemplateCache getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * 所有配置签到<br/>
     * Key:月份 Value:本月奖励集合
     */
    private volatile List<SignInRewardsTemplate> templateMap;

    @Override
    public void reload() {
        try {
            String fileName = SignInRewardsTemplate.class.getAnnotation(ExcelTemplateAnn.class).file();
            String jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
            List<SignInRewardsTemplate> templateWrapperMap = JacksonUtils.getInstance().readValue(jsonStr, new TypeReference<List<SignInRewardsTemplate>>() {
            });
            logger.info("size={}", templateWrapperMap.size());
            templateMap = templateWrapperMap;
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    /**
     * 通过天数,查找单个签到信息
     *
     * @param day
     * @return
     */
    public SignInRewardsTemplate getSignTemp(int signIndex) {
//        Date now = new Date();
//        Date newDate = DateUtils.setDays(now, day);
//        String dateStr = FastDateFormat.getInstance("yyyy-MM-dd").format(newDate);
        signIndex %= templateMap.size();
        return templateMap.get(signIndex);
    }

    public static void main(String[] args) {
        SignTemplateCache.getInstance().reload();
        SignInRewardsTemplate signTemp = SignTemplateCache.getInstance().getSignTemp(9);
        System.out.println(signTemp);
    }

}
