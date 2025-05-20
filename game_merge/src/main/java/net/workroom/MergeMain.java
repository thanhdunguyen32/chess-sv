package net.workroom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import net.workroom.service.HeroDb;

/**
 * TODO 合服装备uuid处理,card的uuid处理，英雄身上装备uuid处理
 * @author Administrator
 *
 */
public class MergeMain {

	private static Logger logger = LoggerFactory.getLogger(MergeMain.class);

	public static void main(String[] args) {
		ApplicationContext factory = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
		logger.info("init successfully!");
		if (args.length < 3) {
			System.out.println("请传入参数如下：目标mobile_app 源mobile_app [源mobile_app]");
			System.exit(1);
		}
		try {
			String targetDbName = args[0];
			HeroDb heroMerge = factory.getBean(HeroDb.class);
			logger.info("================begin merge====================");
			for (int i = 1; i < args.length; i++) {
				String sourceHeroDb = args[i];
				logger.info("--------target hero_db={},source db={}--------------", targetDbName, sourceHeroDb);
				heroMerge.mergeOneDb(targetDbName, sourceHeroDb);
			}
			logger.info("================finish merge====================");
		} catch (Exception e) {
			logger.error("merge fail!", e);
		}
	}

}
