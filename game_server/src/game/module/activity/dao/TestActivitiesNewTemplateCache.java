package game.module.activity.dao;

import com.google.common.io.Files;
import game.entity.TemplateList;
import io.netty.util.CharsetUtil;
import lion.common.JacksonUtils;
import lion.common.Reloadable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Map;

public class TestActivitiesNewTemplateCache implements Reloadable {

	private static Logger logger = LoggerFactory.getLogger(TestActivitiesNewTemplateCache.class);

	static class SingletonHolder {
		static TestActivitiesNewTemplateCache instance = new TestActivitiesNewTemplateCache();
	}

	public static TestActivitiesNewTemplateCache getInstance() {
		return SingletonHolder.instance;
	}

	private volatile TemplateList templateMap;

	@Override
	public void reload() {
		try {
			String jsonStr = Files.asCharSource(new File("data/TestActivitiesNew.json"), CharsetUtil.UTF_8).read();
			TemplateList jsonObj = JacksonUtils.getInstance().readValue(jsonStr, TemplateList.class);
			logger.info("size={}", jsonObj.size());
			templateMap = jsonObj;
			// List<Map<String,Object>> rewardsAll =
			// (List<Map<String,Object>>)templateMap.get("rewards");
			// for (Map<String,Object> reward1 : rewardsAll) {
			// reward1.remove("des");
			// }
			// Files.write(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(templateMap),
			// new File("data/" + "Arena" + ".json"), StandardCharsets.UTF_8);
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	public TemplateList getFixedActivitiesAll() {
		return templateMap;
	}

	public Map<String, Object> getFixedActivity(int activityId) {
		return templateMap.get(activityId - 1);
	}

	public static void main(String[] args) {
		new TestActivitiesNewTemplateCache().reload();
	}

}