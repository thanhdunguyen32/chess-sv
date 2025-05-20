package game.module.friend.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.io.Files;
import game.common.excel.ExcelTemplateAnn;
import game.module.template.MyFriendExploreTemplate;
import lion.common.JacksonUtils;
import lion.common.Reloadable;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyFriendExploreTemplateCache implements Reloadable {

    private static Logger logger = LoggerFactory.getLogger(MyFriendExploreTemplateCache.class);

    static class SingletonHolder {
        static MyFriendExploreTemplateCache instance = new MyFriendExploreTemplateCache();
    }

    public static MyFriendExploreTemplateCache getInstance() {
        return SingletonHolder.instance;
    }

    private volatile Map<Integer, MyFriendExploreTemplate> templateMap;

    public void reload() {
        try {
            String fileName = MyFriendExploreTemplate.class.getAnnotation(ExcelTemplateAnn.class).file();
            String jsonStr = Files.toString(new File("data/" + fileName), StandardCharsets.UTF_8);
            List<MyFriendExploreTemplate> templateWrapperMap = JacksonUtils.getInstance().readValue(jsonStr,
                    new TypeReference<List<MyFriendExploreTemplate>>() {
                    });
            logger.info("size={}", templateWrapperMap.size());
            templateMap = new HashMap<>();
            for (MyFriendExploreTemplate myFriendExploreTemplate : templateWrapperMap) {
                templateMap.put(myFriendExploreTemplate.getId(), myFriendExploreTemplate);
            }
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    public MyFriendExploreTemplate getFriendExploreConfig(int id) {
        return templateMap.get(id);
    }

    public MyFriendExploreTemplate randFriendBoss() {
        int randIndex = RandomUtils.nextInt(0, templateMap.size());
        List<MyFriendExploreTemplate> retlist = new ArrayList<>(templateMap.values());
        return retlist.get(randIndex);
    }

    public static void main(String[] args) {
        new MyFriendExploreTemplateCache().reload();
    }

}