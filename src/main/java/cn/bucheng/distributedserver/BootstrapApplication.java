package cn.bucheng.distributedserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author ：yinchong
 * @create ：2019/7/12 20:23
 * @description：
 * @modified By：启动类
 * @version:
 */
@SpringBootApplication
public class BootstrapApplication {

    private static Logger logger = LoggerFactory.getLogger(BootstrapApplication.class);

    public static void main(String[] args) {
        logger.info("begin start server ");
        SpringApplication.run(BootstrapApplication.class);
    }
}
