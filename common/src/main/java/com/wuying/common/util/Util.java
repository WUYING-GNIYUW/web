package com.wuying.common.util;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import org.springframework.core.env.Environment;

import java.util.Arrays;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class Util {
    public static String encodePath(String path) {
        String[] parts = path.split("/");
        StringBuilder sb = new StringBuilder();
        Arrays.stream(parts).forEach(p -> sb.append(URLEncoder.encode(p, StandardCharsets.UTF_8)));
        return sb.toString();
    }
    public static NamingService getNamingService(Environment env) throws NacosException {
        return NamingFactory.createNamingService(env.getProperty("spring.cloud.nacos.discovery.server-addr") + ":8848");
    }
}
