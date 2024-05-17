package com.ssonzm.productservice.config;

import com.ssonzm.coremodule.dto.property.RedisProperties;
import io.lettuce.core.ReadFrom;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.MasterSlaveServersConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStaticMasterReplicaConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
    private final RedisProperties redisProperties;

    public RedisConfig(RedisProperties redisProperties) {
        this.redisProperties = redisProperties;
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
                .readFrom(ReadFrom.REPLICA_PREFERRED)
                .build();

        RedisStaticMasterReplicaConfiguration masterReplicaConfig
                = new RedisStaticMasterReplicaConfiguration(redisProperties.getMaster().getHost(),
                redisProperties.getMaster().getPort());

        redisProperties.getSlaves().forEach(slave -> {
            masterReplicaConfig.addNode(slave.getHost(), slave.getPort());
        });

        return new LettuceConnectionFactory(masterReplicaConfig, clientConfig);
    }

    @Bean
    public RedisTemplate<String, Integer> redisTemplate() {
        RedisTemplate<String, Integer> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());

        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericToStringSerializer<>(Integer.class));

        return redisTemplate;
    }

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();

        MasterSlaveServersConfig masterSlaveServersConfig = config.useMasterSlaveServers()
                .setMasterAddress("redis://" + redisProperties.getMaster().getHost() + ":" + redisProperties.getMaster().getPort());

        redisProperties.getSlaves().forEach(slave -> {
            masterSlaveServersConfig.addSlaveAddress("redis://" + slave.getHost() + ":" + slave.getPort());
        });

        return Redisson.create(config);
    }

}