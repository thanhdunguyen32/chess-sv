package cc.mrbird.febs.monitor.service.impl;

import cc.mrbird.febs.common.exception.RedisConnectException;
import cc.mrbird.febs.monitor.entity.RedisInfo;
import cc.mrbird.febs.monitor.service.IRedisService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Redis 工具类，只封装了几个常用的 redis 命令，
 * 可根据实际需要按类似的方式扩展即可。
 *
 * @author MrBird
 */
@Service
public class RedisServiceImpl implements IRedisService {

    private Map<String,String> mapCache = new ConcurrentHashMap<>();

    private static String separator = System.getProperty("line.separator");

    @Override
    public List<RedisInfo> getRedisInfo() throws RedisConnectException {
        List<RedisInfo> infoList = new ArrayList<>();
        return infoList;
    }

    @Override
    public Map<String, Object> getKeysSize() throws RedisConnectException {
        return null;
    }

    @Override
    public Map<String, Object> getMemoryInfo() throws RedisConnectException {
        return null;
    }

    @Override
    public Set<String> getKeys(String pattern) throws RedisConnectException {
        return mapCache.keySet();
    }

    @Override
    public String get(String key) throws RedisConnectException {
        return mapCache.get(key);
    }

    @Override
    public String set(String key, String value) throws RedisConnectException {
        return mapCache.put(key,value);
    }

    @Override
    public String set(String key, String value, Long milliscends) throws RedisConnectException {
        return mapCache.put(key,value);
    }

    @Override
    public Long del(String... key) throws RedisConnectException {
        for(String akey : key){
            mapCache.remove(akey);
        }
        return 0L;
    }

    @Override
    public Boolean exists(String key) throws RedisConnectException {
        return mapCache.containsKey(key);
    }

    @Override
    public Long pttl(String key) throws RedisConnectException {
        return null;
    }

    @Override
    public Long pexpire(String key, Long milliseconds) throws RedisConnectException {
        return null;
    }

    @Override
    public Long zadd(String key, Double score, String member) throws RedisConnectException {
        return null;
    }

    @Override
    public Set<String> zrangeByScore(String key, String min, String max) throws RedisConnectException {
        return null;
    }

    @Override
    public Long zremrangeByScore(String key, String start, String end) throws RedisConnectException {
        return null;
    }

    @Override
    public Long zrem(String key, String... members) throws RedisConnectException {
        return null;
    }

}
