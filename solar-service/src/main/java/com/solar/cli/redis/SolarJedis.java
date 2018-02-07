package com.solar.cli.redis;

import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.solar.cli.ConfigLoader;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class SolarJedis {
	private static final Logger logger = LoggerFactory.getLogger(SolarJedis.class);

	private JedisPool jedisPool;

	private SolarJedis(Properties redisConfig) {

		String ip = redisConfig.getProperty("redis-server");
		int port = Integer.parseInt(redisConfig.getProperty("redis-port"));

		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxTotal(10);
		config.setMaxIdle(10);
		config.setMaxWaitMillis(1000);
		config.setTestOnBorrow(true);
		config.setTestOnReturn(true);

		logger.info("-----create redis pool {}:{}", ip, port);
		jedisPool = new JedisPool(config, ip, port);
	}

	private static class Inner {
		static Properties loadConf;
		static {
			loadConf = ConfigLoader.loadConf("redis-config.properties");
			if (loadConf == null) {
				throw new RuntimeException("Can not load redis configure");
			}
		}
		static SolarJedis solarJedis = new SolarJedis(loadConf);
	}

	public static SolarJedis getInstance() {
		return Inner.solarJedis;
	}

	public Jedis getRedis() {
		Jedis jedis = jedisPool.getResource();
		return jedis;
	}

	public void release(Jedis redis) {
		if (redis != null) {
			redis.close();
		}
	}

	public String set(String key, String value) {
		Jedis redis = getRedis();
		try {
			String status = redis.set(key, value);
			return status;
		} finally {
			release(redis);
		}
	}

	public Long del(String key) {
		Jedis redis = getRedis();
		try {
			Long n = redis.del(key);
			return n;
		} finally {
			release(redis);
		}
	}

	public Long lpush(String key, String value) {
		Jedis redis = getRedis();
		try {
			Long n = redis.lpush(key, value);
			return n;
		} finally {
			release(redis);
		}
	}

	public List<String> lrange(String key, int start, int end) {
		Jedis redis = getRedis();
		try {
			List<String> ls = redis.lrange(key, start, end);
			return ls;
		} finally {
			release(redis);
		}
	}
}
