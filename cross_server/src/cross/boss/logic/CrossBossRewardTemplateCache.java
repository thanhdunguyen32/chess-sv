package cross.boss.logic;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.io.Files;

import game.common.excel.ExcelTemplateAnn;
import game.common.excel.TemplateCollection;
import game.module.template.CrossBossRewardTemplate;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import lion.common.Reloadable;

public class CrossBossRewardTemplateCache implements Reloadable {

	private static Logger logger = LoggerFactory.getLogger(CrossBossRewardTemplateCache.class);

	static class SingletonHolder {
		static CrossBossRewardTemplateCache instance = new CrossBossRewardTemplateCache();
	}

	public static CrossBossRewardTemplateCache getInstance() {
		return SingletonHolder.instance;
	}

	private Map<Integer, List<CrossBossRewardTemplate>> configMap;

	@Override
	public void reload() {
		try {
			Schema<TemplateCollection> schema = RuntimeSchema.getSchema(TemplateCollection.class);
			TemplateCollection tc = new TemplateCollection();
			String fileName = CrossBossRewardTemplate.class.getAnnotation(ExcelTemplateAnn.class).file();
			byte[] pomByte = Files.toByteArray(new File("excel/" + fileName + ".pro"));
			ProtostuffIOUtil.mergeFrom(pomByte, tc, schema);
			List<CrossBossRewardTemplate> tplList = tc.getObjList(CrossBossRewardTemplate.class);
			logger.info("CrossBossRewardTemplate,size={}", tplList.size());
			Map<Integer, List<CrossBossRewardTemplate>> tmpConfigMap = new HashMap<>();
			for (CrossBossRewardTemplate aConfig : tplList) {
				int roomTypeId = aConfig.getRoom_type_id();
				List<CrossBossRewardTemplate> aRoomConfig = tmpConfigMap.get(roomTypeId);
				if (aRoomConfig == null) {
					aRoomConfig = new ArrayList<>();
					tmpConfigMap.put(roomTypeId, aRoomConfig);
				}
				aRoomConfig.add(aConfig);
			}
			//数组排序
			for(List<CrossBossRewardTemplate> aRoomConfig : tmpConfigMap.values()){
				aRoomConfig.sort(new Comparator<CrossBossRewardTemplate>() {
					@Override
					public int compare(CrossBossRewardTemplate o1, CrossBossRewardTemplate o2) {
						return o1.getRank().compareTo(o2.getRank());
					}
				});
			}
			configMap = tmpConfigMap;
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	public List<CrossBossRewardTemplate> getCrossBossRewardTemplateByRoomId(int roomTypeId) {
		return configMap.get(roomTypeId);
	}

	public static void main(String[] args) {
		new CrossBossRewardTemplateCache().reload();

	}

}
