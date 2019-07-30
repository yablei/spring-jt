package com.jt.config;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisSentinelPool;

@Configuration //配置类
@PropertySource("/properties/redis.properties")
@Lazy    //懒加载   
public class RedisConfig {
	/*
	 * @Value("${redis.host}") private String host;
	 * 
	 * @Value("${redis.prot}") private Integer prot;
	 * 
	 * @Bean public Jedis jedis() {
	 * 
	 * return new Jedis(host,prot); }
	 */
	/*@Value("${redis.nodes}")
	private String nodes;
	
	@Bean
	public ShardedJedis shardedJedis() {
		
		ArrayList<JedisShardInfo> shards = new ArrayList<>();
		String[] strNodes = nodes.split(",");
		for(String strNode:strNodes) {
			String[] node = strNode.split(":");
			String host=node[0];
			int port=Integer.parseInt(node[1]);
			shards.add(new JedisShardInfo(host,port));
		}
		return new ShardedJedis(shards);}*/
	
	/*
	 * @Value("${redis.sentinel.masterName}") private String masterName;
	 * 
	 * @Value("${redis.sentinels}") private String nodes;
	 * 
	 * 
	 * @Bean(name="jedisSentinelPool")//单例 public JedisSentinelPool
	 * jedisSentinelPool() { Set<String> sentinels = new HashSet<>();
	 * sentinels.add(nodes); JedisSentinelPool pool = new
	 * JedisSentinelPool(masterName,sentinels); return pool; }
	 * //@Qualifier该注解表示指定bean赋值 用在方法中
	 * 
	 * @Bean
	 * 
	 * @Scope("prototype") //多例对象 public Jedis
	 * jedis(@Qualifier("jedisSentinelPool")JedisSentinelPool pool) {
	 * 
	 * Jedis jedis = pool.getResource(); return jedis; }
	 */
	@Value("${redis.nodes}")
	private String nodes;
	
	
	//搭建redis集群
	@Bean
	public JedisCluster jedisCluster() {
		Set<HostAndPort> nodes =getNodes();
		return new JedisCluster(nodes);
	}

    //set:表示不要有重复数据
	private Set<HostAndPort> getNodes() {
		HashSet<HostAndPort> nodesSet = new HashSet<>();
		String[] strnodes = nodes.split(",");
		for(String redisNodes : strnodes) {
		   String host = redisNodes.split(":")[0];
		   int port =Integer.parseInt(redisNodes.split(":")[1]) ;
		   HostAndPort hostAndPort = new HostAndPort(host, port);
		    nodesSet.add(hostAndPort);
		}
		return nodesSet;
	} 
	
	
}
