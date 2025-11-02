package com.wuying.common.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import com.wuying.common.util.Util;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StaticResourceController {
    @GetMapping("/public/static/{format}/{resourceName}")
    public ResponseEntity<Void> getPublicStaticResource(@PathVariable("format") String format, @PathVariable("resourceName") String resourceName) {
        String baseInternalUri = "/internal_protected/public";
//        String internalUri = baseInternalUri + Util.encodePath(format, resourceName);
        String internalUri = Util.encodePath(format, resourceName);
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Accel-Redirect", internalUri);

        return ResponseEntity.ok().headers(headers).build();
    }

    @GetMapping("/private/static/{format}/{resourceName}")
    public ResponseEntity<Void> getPrivateStaticResource(@PathVariable("format") String format, @PathVariable("resourceName") String resourceName) {
        String baseInternalUri = "/internal_protected/private";
//        String internalUri = baseInternalUri + Util.encodePath(format, resourceName);
        String internalUri = Util.encodePath(format, resourceName);
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Accel-Redirect", internalUri);

        return ResponseEntity.ok().headers(headers).build();
    }
}
