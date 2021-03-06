package com.voyageone.common.configs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * KeyValue 配置文件的专用配置访问类
 * Created by Tester on 4/16/2015.
 */
public class Properties {

    private static final Logger logger = LoggerFactory.getLogger(Properties.class);

    private static Map<String, String> keyValueMap;

    public static void init() throws IOException {

        if (keyValueMap == null) keyValueMap = new HashMap<>();

        // 搜索所有配置文件
        Resource[] resources = new PathMatchingResourcePatternResolver().getResources("classpath*:config/*_keyvalue.properties");

        // 挨个读取
        for (Resource resource : resources) {
            loadProps(resource);
        }

        if (resources.length < 1) {
            logger.warn("没有读取任何 keyvalue 属性文件");
        }
    }

    private static void loadProps(Resource resource) throws IOException {

        java.util.Properties props = new java.util.Properties();

        try (InputStream stream = resource.getInputStream();

             InputStreamReader streamReader = new InputStreamReader(stream, "UTF-8")) {

            props.load(streamReader);

            logger.info("从 " + resource.getFilename() + " 文件读取数量：" + props.size() + " （未消除重复的数量）");

            for (String name : props.stringPropertyNames()) {

                if (keyValueMap.containsKey(name)) {
                    logger.warn("读入 Properties 时，发现了重复的元素：" + name);
                }

                putValue(name, props.getProperty(name));
            }
        }
    }

    /**
     * 修改配置的值, 请尽量不要手动
     */
    static void putValue(String key, String value) {
        keyValueMap.put(key, value);
    }

    /**
     * 读取配置值
     */
    public static String readValue(String key) {
        if (keyValueMap == null) {
            try {
                init();
            } catch (IOException e) {
                logger.warn(e.getMessage());
            }
        }
        return keyValueMap != null ? keyValueMap.get(key) : null;
    }
}