package cn.bucheng.distributedserver.registry.zookeeper;

import cn.bucheng.distributedserver.common.constant.TransferConstant;
import cn.bucheng.distributedserver.registry.ServiceInstance;
import cn.bucheng.distributedserver.registry.ServiceRegistry;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author ：yinchong
 * @create ：2019/7/12 20:48
 * @description：
 * @modified By：
 * @version:
 */
@Component
public class ZookeeperServiceRegistry implements ServiceRegistry {

    private static Logger logger = LoggerFactory.getLogger(ZookeeperServiceRegistry.class);

    @Autowired
    private CuratorFramework client;

    @Override
    public boolean register(ServiceInstance instance) {
        CreateMode createMode = CreateMode.EPHEMERAL;
        if (instance.getState() == TransferConstant.PERSISTENT) {
            createMode = CreateMode.PERSISTENT;
        }
        try {
            Stat stat = client.checkExists().forPath("/"+instance.getServiceId() + "/" + TransferConstant.LEADER_NAME);
            if (null != stat) {
                return false;
            }
            client.create().creatingParentsIfNeeded().withMode(createMode).forPath("/"+instance.getServiceId() + "/" + TransferConstant.LEADER_NAME, (instance.getHost() + ":" + instance.getPort()).getBytes());
            return true;
        } catch (Exception e) {
            logger.error(e.toString());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deregister(ServiceInstance instance) {
        try {
            client.delete().guaranteed().deletingChildrenIfNeeded().forPath(instance.getServiceId() + "/" + TransferConstant.LEADER_NAME );
        } catch (Exception e) {
            logger.error(e.toString());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {
        if (null != client) {
            client.close();
        }
    }


}
