package cn.bucheng.distributedserver.common;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * @author buchengyin
 * @Date 2018/12/24 22:33
 **/
@Component
public class EnvironmentUtils implements EnvironmentPostProcessor {
    private static Environment environment;


    public static <T> T getProperties(String key, Class<T> clazz) {
        return environment.getProperty(key, clazz);
    }

    public static String getProperties(String key) {
        return environment.getProperty(key);
    }

    public static int getIntValue(String key, int defaultValue) {
        String value = environment.getProperty(key);
        if (null == value || "".equals(value)) {
            return defaultValue;
        }

        return Integer.parseInt(value);
    }

    public static String getStringValue(String key, String defaultValue) {
        String value = environment.getProperty(key);
        if (null == value || "".equals(value)) {
            return defaultValue;
        }
        return value;
    }

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        EnvironmentUtils.environment = environment;
    }
}
