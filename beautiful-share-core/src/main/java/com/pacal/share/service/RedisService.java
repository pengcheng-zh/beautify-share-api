package com.pacal.share.service;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class RedisService {

    @Resource
    RedisTemplate<String, String> redisTemplate;

    public String getValue(String redisKey) {
        try {
            return redisTemplate.opsForValue().get( redisKey );
        }catch ( Exception ex ) {
            log.error( "RedisService getValue {} failed", redisKey, ex );
        }
        return "";
    }

    public void setValue(String redisKey, String redisValue, int timeout) {
        try {
            if ( timeout > 0 ) {
                redisTemplate.opsForValue().set( redisKey, redisValue, timeout, TimeUnit.SECONDS );
            } else {
                redisTemplate.opsForValue().set( redisKey, redisValue );
            }
        }catch ( Exception ex ) {
            log.error( "RedisService setValue {} failed {}", redisKey, ex.getMessage() );
        }
    }

    public Boolean setNx(String redisKey, String redisValue, int timeout) {
        try {
            return redisTemplate.opsForValue().setIfAbsent( redisKey, redisValue, timeout, TimeUnit.SECONDS );
        } catch ( Exception ex ) {
            log.error( "RedisService setNx {} failed {}", redisKey, ex.getMessage() );
        }
        return false;
    }

    public void delete(String redisKey) {
        try {
            redisTemplate.delete( redisKey );
        }catch ( Exception ex ) {
            log.error( "RedisService delete {} failed {}", redisKey, ex.getMessage() );
        }
    }

    /**
     * 向 Set 中添加成员，用于 UV 等去重统计。可配合 expire 或 addToSet(key, member, expireSeconds) 设置过期。
     */
    public Long addToSet(String key, String member) {
        try {
            return redisTemplate.opsForSet().add( key, member );
        } catch (Exception ex) {
            log.error( "RedisService addToSet {} failed {}", key, ex.getMessage() );
            return 0L;
        }
    }

    public Set<String> getSet(String key) {
        try {
            return redisTemplate.opsForSet().members( key );
        } catch ( Exception ex ) {
            return Collections.emptySet();
        }
    }

    public Long removeFromSet(String key, String member) {
        try {
            return redisTemplate.opsForSet().remove( key, member );
        } catch ( Exception ex ) {
            return 0L;
        }
    }

    /**
     * 向 Set 中添加成员并设置过期时间（秒），用于按天 UV 统计。
     */
    public Long addToSet(String key, String member, long expireSeconds) {
        try {
            Long added = redisTemplate.opsForSet().add( key, member );
            if (added != null && added > 0) {
                redisTemplate.expire( key, expireSeconds, TimeUnit.SECONDS );
            }
            return added;
        } catch (Exception ex) {
            log.error( "RedisService addToSet {} failed {}", key, ex.getMessage() );
            return 0L;
        }
    }

    /**
     * 获取 Set 的成员数量，用于统计 UV。
     */
    public Long getSetSize(String key) {
        try {
            Long size = redisTemplate.opsForSet().size( key );
            return size != null ? size : 0L;
        } catch (Exception ex) {
            log.error( "RedisService getSetSize {} failed {}", key, ex.getMessage() );
            return 0L;
        }
    }

}
