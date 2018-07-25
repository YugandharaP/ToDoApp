package com.bridgelabz.todoapplication.utilservice.redisservice;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author yuga
 * @since 19/07/2018
 *        <p>
 *        <b>To store token generated at the time of login operation and
 *        provides setToken,getToken and deleteToken methoods.Redis is an Open
 *        source in-memory data structure store, used as a database, cache and
 *        message broker</b>
 *        </p>
 *
 */
//@Service
public class RedisService {

	/*private RedisTemplate<String, Object> redisTemplate;
	private static HashOperations<String, String, String> hashOperations;
	private static String KEY = "jwtToken";

	@Autowired
	public RedisService(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	private static Logger logger = LoggerFactory.getLogger(RedisService.class);

	*//**
	 * To initialize hash operations and this method MUST be invoked before the class is put into service.
	 *//*
	@PostConstruct
	private void init() {
		hashOperations = redisTemplate.opsForHash();
	}

	*//**
	 * @param clientId
	 * @param jwtToken
	 *//*
	public void setToken(String clientId, String jwtToken) {
		hashOperations.put(KEY, clientId, jwtToken);
		logger.info("Token set in redis");
	}

	*//**
	 * @param clientId
	 * @return
	 *//*
	public String getToken(String clientId) {
		logger.info("Getting token from redis");
		return hashOperations.get(KEY, clientId);
	}

	*//**
	 * @param clientId
	 *//*
	public void deleteToken(String clientId) {
		logger.info("Deleting token from redis");
		hashOperations.delete(KEY, clientId);
	}*/
}
