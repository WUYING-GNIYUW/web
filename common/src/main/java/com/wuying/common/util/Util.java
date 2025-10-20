package com.wuying.common.util;

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
}
