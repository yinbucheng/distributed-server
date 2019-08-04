package cn.bucheng.distributedserver.config;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ：yinchong
 * @create ：2019/7/12 21:11
 * @description：
 * @modified By：
 * @version:
 */
@Configuration
public class Config {

    @Bean
    CuratorFramework curatorFramework(ServerProperties properties) {
        String zookeeperURL = properties.getZk().getHost();
        int sessionTimeOut = properties.getZk().getSessionTimeout();
        int connectionTimeOut = properties.getZk().getConnectionTimeout();
        int baseSleepTimeMS = properties.getZk().getSleepTime();
        int maxRetires = properties.getZk().getMaxRetries();
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(baseSleepTimeMS, maxRetires);
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString(zookeeperURL)
                .sessionTimeoutMs(sessionTimeOut)
                .connectionTimeoutMs(connectionTimeOut)
                .retryPolicy(retryPolicy)
                .build();
        client.start();
        return client;
    }
}
