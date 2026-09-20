package com.pacal.share.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
    // 自动注入属性
    @Value("${spring.data.redis.host}")
    private String host;
    @Value("${spring.data.redis.port}")
    private int port;
    @Value("${spring.data.redis.password}")
    private String password;
    @Value("${spring.data.redis.database}")
    private int database;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        // TODO: 这里需要替换成自己的Redis配置
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName( host );
        configuration.setPort( port );
        configuration.setUsername( "default" );
        configuration.setPassword( password );
        configuration.setDatabase( database );
        return new LettuceConnectionFactory( configuration );
    }

    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory( connectionFactory );

        // json序列化配置
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility( PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY );
        objectMapper.activateDefaultTyping(
                LaissezFaireSubTypeValidator.instance,
                ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.WRAPPER_ARRAY );

        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer( objectMapper );

        // String序列化配置
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer( stringRedisSerializer );

        // key和hash的key都采用String的序列化配置
        redisTemplate.setHashKeySerializer( stringRedisSerializer );

        // value和hash的value采用json的序列化配置
        redisTemplate.setValueSerializer( serializer );
        redisTemplate.setHashValueSerializer( serializer );
        redisTemplate.afterPropertiesSet();

        return redisTemplate;
    }
}
