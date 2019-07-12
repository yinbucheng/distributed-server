package cn.bucheng.distributedserver.registry;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class DefaultServiceInstance implements ServiceInstance {

    private final String serviceId;

    private final String host;

    private final int port;

    private final int state;


    private final Map<String, String> metadata;

    public DefaultServiceInstance(String serviceId, String host, int port, int state,
                                  Map<String, String> metadata) {
        this.serviceId = serviceId;
        this.host = host;
        this.port = port;
        this.state = state;
        this.metadata = metadata;
    }

    public DefaultServiceInstance(String serviceId, String host, int port,
                                  int state) {
        this(serviceId, host, port, state, new LinkedHashMap<String, String>());
    }


    @Override
    public Map<String, String> getMetadata() {
        return this.metadata;
    }


    @Override
    public String getServiceId() {
        return serviceId;
    }

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public int getState() {
        return 0;
    }

    @Override
    public int getPort() {
        return port;
    }


    @Override
    public String toString() {
        return "DefaultServiceInstance{" +
                "serviceId='" + serviceId + '\'' +
                ", host='" + host + '\'' +
                ", port=" + port +
                ", state=" + state +
                ", metadata=" + metadata +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DefaultServiceInstance that = (DefaultServiceInstance) o;
        return port == that.port &&
                state == that.state &&
                Objects.equals(serviceId, that.serviceId) &&
                Objects.equals(host, that.host) &&
                Objects.equals(metadata, that.metadata);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serviceId, host, port, state, metadata);
    }
}
