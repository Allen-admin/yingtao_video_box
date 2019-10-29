package com.k365.config.shiro;

import com.k365.config.jwt.JwtToken;
import com.k365.video_common.constant.Constants;
import com.k365.video_common.util.JwtUtil;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Collection;
import java.util.Set;

/**
 * @author Gavin
 * @date 2019/7/4 21:30
 * @description：
 */
@AutoConfigureAfter({com.k365.config.cache.RedisConfig.class})
public class ShiroCacheManager implements CacheManager {

    private String cacheKeyPrefix = "shiro:";

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public <K, V> Cache<K, V> getCache(String name) throws CacheException {
        return new ShiroRedisCache<K, V>(cacheKeyPrefix + name);
    }

    public class ShiroRedisCache<K, V> implements Cache<K, V> {

        private String cacheKey;

        public ShiroRedisCache(String cacheKey) {
            this.cacheKey = cacheKey;
        }

        @Override
        public V get(K key) throws CacheException {
            BoundHashOperations<String, K, V> hash = redisTemplate.boundHashOps(cacheKey);
            Object k = hashKey(key);
            return hash.get(k);
        }

        @Override
        public V put(K key, V value) throws CacheException {
            BoundHashOperations<String, K, V> hash = redisTemplate.boundHashOps(cacheKey);
            Object k = hashKey(key);
            hash.put((K) k, value);
            return value;
        }

        @Override
        public V remove(K key) throws CacheException {
            BoundHashOperations<String, K, V> hash = redisTemplate.boundHashOps(cacheKey);

            Object k = hashKey(key);
            V value = hash.get(k);
            hash.delete(k);
            return value;
        }

        @Override
        public void clear() throws CacheException {
            redisTemplate.delete(cacheKey);
        }

        @Override
        public int size() {
            BoundHashOperations<String, K, V> hash = redisTemplate.boundHashOps(cacheKey);
            return hash.size().intValue();
        }

        @Override
        public Set<K> keys() {
            BoundHashOperations<String, K, V> hash = redisTemplate.boundHashOps(cacheKey);
            return hash.keys();
        }

        @Override
        public Collection<V> values() {
            BoundHashOperations<String, K, V> hash = redisTemplate.boundHashOps(cacheKey);
            return hash.values();
        }

        protected Object hashKey(K key) {

            if (key instanceof PrincipalCollection) {//如果key是登录凭证,那么这是访问用户的授权缓存;将登录凭证转为user对象,返回user的id属性做为hash key
                PrincipalCollection pc = (PrincipalCollection) key;
                JwtToken token = (JwtToken) pc.getPrimaryPrincipal();
                return token.getUid();
            }
            return key;
        }

        private String getKey(Object key) {
            return Constants.CACHE_PREFIX_SHIRO + JwtUtil.getClaim(key.toString(), Constants.ACCOUNT);
        }
    }

}