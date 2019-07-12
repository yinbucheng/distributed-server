package cn.bucheng.distributedserver.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.io.Serializable;

/**
 * @author buchengyin
 * @create 2019/7/13 0:17
 * @describe
 */
@ConfigurationProperties(prefix = "mst", ignoreUnknownFields = true)
public class ServerProperties implements Serializable {
    @NestedConfigurationProperty
    private Zk zk = new Zk();

    @NestedConfigurationProperty
    private Server server;

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public Zk getZk() {
        return zk;
    }

    public void setZk(Zk zk) {
        this.zk = zk;
    }

    public static class Server{
        private String host;

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }
    }

    public static class Zk{
        private String host ="127.0.0.1:2181";
        private Integer sessionTimeout=30000;
        private Integer connectionTimeout = 5000;
        private Integer sleepTime = 5000;
        private Integer maxRetries =3;

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public Integer getSessionTimeout() {
            return sessionTimeout;
        }

        public void setSessionTimeout(Integer sessionTimeout) {
            this.sessionTimeout = sessionTimeout;
        }

        public Integer getConnectionTimeout() {
            return connectionTimeout;
        }

        public void setConnectionTimeout(Integer connectionTimeout) {
            this.connectionTimeout = connectionTimeout;
        }

        public Integer getSleepTime() {
            return sleepTime;
        }

        public void setSleepTime(Integer sleepTime) {
            this.sleepTime = sleepTime;
        }

        public Integer getMaxRetries() {
            return maxRetries;
        }

        public void setMaxRetries(Integer maxRetries) {
            this.maxRetries = maxRetries;
        }
    }
}
