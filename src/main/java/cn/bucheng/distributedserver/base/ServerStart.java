package cn.bucheng.distributedserver.base;


import cn.bucheng.distributedserver.common.EnvironmentUtils;
import cn.bucheng.distributedserver.common.constant.TransferConstant;
import cn.bucheng.distributedserver.net.NetServer;
import cn.bucheng.distributedserver.registry.DefaultServiceInstance;
import cn.bucheng.distributedserver.registry.ServiceInstance;
import cn.bucheng.distributedserver.registry.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author ：yinchong
 * @create ：2019/7/12 11:11
 * @description：
 * @modified By：
 * @version:
 */
@Component
public class ServerStart implements CommandLineRunner {

    private static Logger logger = LoggerFactory.getLogger(ServerStart.class);

    @Autowired
    private NetServer netServer;

    @Value("${server.port}")
    private Integer port;
    @Autowired
    private ServiceRegistry registry;

    @Value("${spring.application.name}")
    private String serviceId;
    @Value("${mst.server.host}")
    private String host;

    private Integer serverPort;

    @Override
    public void run(String... args) throws Exception {
        startServer();
        startRegistry();
    }

    //端口是先从配置文件中获取，如果获取失败再将server.port的端口加上指定大小启动
    private void startServer() {
        Thread thread = new Thread(() -> {
            while (true) {
                serverPort = EnvironmentUtils.getIntValue("mst.server.port", 0);
                if (serverPort == 0) {
                    serverPort = port + TransferConstant.STEP_PORT;
                }
                netServer.start(serverPort);
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.setName("server_run");
        thread.setDaemon(true);
        thread.start();
    }

    public void startRegistry() {
        Thread thread = new Thread(() -> {
            while (true) {
                ServiceInstance instance = new DefaultServiceInstance(serviceId, host, serverPort, TransferConstant.EPHEMERAL);
                boolean flag = registry.register(instance);
                if (flag) {
                    logger.info("become leader to manager transaction");
                }
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.setDaemon(true);
        thread.setName("register_thread");
        thread.start();

    }
}
