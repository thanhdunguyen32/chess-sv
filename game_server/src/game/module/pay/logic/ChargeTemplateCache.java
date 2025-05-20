package game.module.pay.logic;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.io.Files;
import game.common.excel.ExcelTemplateAnn;
import game.entity.StringKeyTemplateMap;
import game.module.template.PvpDayRewardTemplate;
import game.module.template.RechargeTemplate;
import lion.common.JacksonUtils;
import lion.common.Reloadable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class ChargeTemplateCache implements Reloadable {

	private static Logger logger = LoggerFactory.getLogger(ChargeTemplateCache.class);

	static class SingletonHolder {
		static ChargeTemplateCache instance = new ChargeTemplateCache();
	}

	public static ChargeTemplateCache getInstance() {
		return SingletonHolder.instance;
	}

	private volatile Map<String,RechargeTemplate> templateMap;

	@Override
	public void reload() {
		try {
			String fileName = RechargeTemplate.class.getAnnotation(ExcelTemplateAnn.class).file();
            logger.info("ChargeTemplateCache fileName={}", fileName);
			String jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
			Map<String,RechargeTemplate> templateWrapperMap = JacksonUtils.getInstance().readValue(jsonStr,
					new TypeReference<Map<String,RechargeTemplate>>() {});
			logger.info("size={}", templateWrapperMap.size());
			templateMap = templateWrapperMap;
		} catch (Exception e) {
			logger.error("", e);
		}
	}

    public RechargeTemplate getRechargeTemplate(String productId) {
        return templateMap.get(productId);
    }
    
    public String getTemplateMapJsonString() {
        return JacksonUtils.getInstance().writeValueAsString(templateMap);
    }

}