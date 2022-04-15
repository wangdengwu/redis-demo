package io.reids.demo;

import io.reids.demo.annotation.Cluster;
import io.reids.demo.annotation.Sentinel;
import io.reids.demo.annotation.Standalone;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.commands.ProtocolCommand;
import redis.clients.jedis.params.SetParams;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RedisDemoTests {

    private static final int TEN_SECONDS = 10 * 1000;
    private static final SetParams TEN_SECONDS_TO_EXPIRE = SetParams.setParams().ex(TEN_SECONDS);

    @Test
    @Standalone
    public void test_global_dict_rehash() {
        Jedis standalone = new Jedis("standalone", 6379);
        for (int i = 0; i < 18; i++) {
            String key = i + "";
            String value = i + "";
            standalone.set(key, value, TEN_SECONDS_TO_EXPIRE);
        }
        standalone.close();
    }

    @Test
    @Standalone
    public void test_type_and_encoding() {
        Jedis standalone = new Jedis("standalone", 6379);
        String stringKey = "hello";
        standalone.set(stringKey, "world", TEN_SECONDS_TO_EXPIRE);
        assertEquals("string", standalone.type(stringKey), "the value type should be string");
        assertEquals("embstr", standalone.objectEncoding(stringKey), "the value encoding should be embstr");
        standalone.set(stringKey, "1", TEN_SECONDS_TO_EXPIRE);
        assertEquals("int", standalone.objectEncoding(stringKey), "the value encoding should be int");
        //TODO test raw
        //TODO generate 44 char as value
        //TODO generate 45 char as value
        RandomStringUtils.random(44);
        RandomStringUtils.random(45);

        String listKey = "list";
        standalone.lpush(listKey, "1");
        standalone.expire(listKey, TEN_SECONDS);
        assertEquals("list", standalone.type(listKey), "the value type should be list");
        assertEquals("quicklist", standalone.objectEncoding(listKey), "the value encoding should be quicklist");

        String setKey = "set";
        standalone.sadd(setKey, "1");
        standalone.expire(setKey, TEN_SECONDS);
        assertEquals("set", standalone.type(setKey), "the value type should be set");
        assertEquals("intset", standalone.objectEncoding(setKey), "the value encoding should be intset");
        //TODO test hashtable encoding
        //TODO use string as value

        String hashKey = "hash";
        standalone.hset(hashKey, "hello", "world");
        standalone.expire(hashKey, TEN_SECONDS);
        assertEquals("hash", standalone.type(hashKey), "the value type should be hash");
        assertEquals("ziplist", standalone.objectEncoding(hashKey), "the value encoding should be ziplist");
        //TODO test hashtable encoding
        //TODO generate 64 char as value
        //TODO generate 65 char as vale

        String zsetKey = "zset";
        standalone.zadd(zsetKey, 1, "zset");
        standalone.expire(zsetKey, TEN_SECONDS);
        assertEquals("zset", standalone.type(zsetKey), "the value type should be zset");
        assertEquals("ziplist", standalone.objectEncoding(zsetKey), "the value encoding should be ziplist");

    }

    @Test
    @Standalone
    public void test_scan_keys() {
        Jedis standalone = new Jedis("standalone", 6379);
        String scan_keys = "scan_keys";
        standalone.set(scan_keys, scan_keys, TEN_SECONDS_TO_EXPIRE);
        standalone.moduleLoad("/etc/lib/scankeys.so");
        Object result = standalone.sendCommand(new ProtocolCommand() {
            @Override
            public byte[] getRaw() {
                return "scan.keys".getBytes();
            }
        });
        if (result instanceof String) {
            assertTrue(((String) result).contains(scan_keys));
        }
    }

    @Test
    @Standalone
    public void test_redis_lua_eval() {
        StringBuilder sb = new StringBuilder()
                .append("local key1 = KEYS[1]                        ")
                .append("local expireTime = ARGV[1]                  ")
                .append("local count = redis.call('incr', key1)      ")
                .append("if (redis.call('ttl', key1) == -1) then     ")
                .append("redis.call('expire',key1, expireTime)       ")
                .append("end                                         ")
                .append("return count                                ");
        Jedis standalone = new Jedis("standalone", 6379);
        Object result = standalone.eval(sb.toString(), (List<String>) Arrays.asList("hello"), (List<String>) Arrays.asList(TEN_SECONDS + ""));
        assertTrue(Integer.valueOf((String) result) > 0);
    }

    @Test
    @Sentinel
    public void sentinel() {
        System.out.println("sentinel");
    }

    @Test
    @Cluster
    public void cluster() {
        System.out.println("cluster");
    }
}
