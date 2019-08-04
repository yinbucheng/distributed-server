package cn.bucheng.distributedserver.monitor;

import cn.bucheng.distributedserver.common.constant.TransferConstant;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author buchengyin
 * @create 2019/7/12 23:27
 * @describe
 */
@RestController
public class MonitorController {

    private static Logger logger = LoggerFactory.getLogger(MonitorController.class);

    @Autowired
    private CuratorFramework client;

    @Value("${spring.application.name}")
    private String serviceId;

    @RequestMapping("/index")
    public Object index() {
        try {
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("version",TransferConstant.VERSION);
            byte[] bytes = client.getData().forPath("/" + serviceId + "/" + TransferConstant.LEADER_NAME);
            if (null != bytes) {
                result.put("leader", new String(bytes));
            } else {
                result.put("leader", "no leader");
            }
            Stat stat = client.checkExists().forPath("/" + serviceId + "/" + TransferConstant.INSTANCE_NAME);
            if(stat==null){
                result.put("instance","no instance");
                return result;
            }
            List<String> childs = client.getChildren().forPath("/" + serviceId + "/" + TransferConstant.INSTANCE_NAME);
            if (childs == null || childs.size() == 0) {
                Map<String, String> temp = new LinkedHashMap<>();
                result.put("instance", temp);
                for (String child : childs) {
                    bytes = client.getData().forPath("/" + serviceId + "/" + TransferConstant.INSTANCE_NAME + "/" + child);
                    if (null != bytes) {
                        temp.put(child, new String(bytes));
                    } else {
                        temp.put(child, "no host");
                    }
                }
            } else {
                result.put("instance", "no instance");
            }
            return result;
        } catch (Exception e) {
            logger.error(e.toString());
            return e.toString();
        }
    }
}
