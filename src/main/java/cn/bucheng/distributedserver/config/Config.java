package cn.bucheng.distributedserver.config;

import cn.bucheng.distributedserver.common.EnvironmentUtils;
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
    CuratorFramework curatorFramework() {
        String zookeeperURL = EnvironmentUtils.getStringValue("mst.zk.host", "127.0.0.1:2181");
        int sessionTimeOut = EnvironmentUtils.getIntValue("mst.zk.session.timeout", 30000);
        int connectionTimeOut = EnvironmentUtils.getIntValue("mst.zk.connection.timeout", 5000);
        int baseSleepTimeMS = EnvironmentUtils.getIntValue("mst.zk.sleep.time", 5000);
        int maxRetires = EnvironmentUtils.getIntValue("mst.zk.max.retries", 3);
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
