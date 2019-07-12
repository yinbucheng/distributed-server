package cn.bucheng.distributedserver.registry;

import java.util.Map;

public interface ServiceInstance {

    /**
     * @return the service id as registered.
     */
    String getServiceId();

    /**
     * @return the hostname of the registered ServiceInstance
     */
    String getHost();

    /**
     * @return get path state
     */
    int getState();

    /**
     * @return the port of the registered ServiceInstance
     */
    int getPort();


    /**
     * @return the key value pair metadata associated with the service instance
     */
    Map<String, String> getMetadata();


}