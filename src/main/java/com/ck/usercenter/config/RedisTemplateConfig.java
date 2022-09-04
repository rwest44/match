
package com.ck.usercenter.config;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;


//RedisTemplate 是 Spring Boot 访问 Redis 的核心组件，底层通过 RedisConnectionFactory 对多种 Redis 驱动进行集成，上层通过 XXXOperations 提供丰富的 API ，并结合 Spring4 基于泛型的 bean 注入，极大的提供了便利，成为日常开发的一大利器。
@Configurable
public class RedisTemplateConfig {
    @Bean
    //RedisConnectionFactory connectionFactory 是声明连接工厂,把连接工厂注入给 template 后才能访问数据库,由连接工厂创建与数据库的连接
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory){
        //实例化Bean
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        //设置序列化方式
        redisTemplate.setKeySerializer(RedisSerializer.string());
        return redisTemplate;
    }
}