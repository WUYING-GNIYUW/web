package com.wuying.common.util;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import org.springframework.core.env.Environment;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class Util {
    public static String encodePath(String format, String resourceName) {
        String path = "";
        path = path + "/" + URLEncoder.encode(format, StandardCharsets.UTF_8);
        path = path + "/" + URLEncoder.encode(resourceName, StandardCharsets.UTF_8);
        return path;
    }
    public static NamingService getNamingService(Environment env) throws NacosException {
        return NamingFactory.createNamingService(env.getProperty("spring.cloud.nacos.discovery.server-addr") + ":8848");
    }
}
