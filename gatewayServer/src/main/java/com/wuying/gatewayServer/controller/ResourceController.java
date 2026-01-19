package com.wuying.gatewayServer.controller;

import com.wuying.common.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
@Slf4j
@RestController
public class ResourceController {
    @GetMapping("/{protected_level}/getResource")
    public Mono<ResponseEntity<Void>> getResource(
            @PathVariable("protected_level") String protectedLevel,
            @RequestParam(name = "resource_name", required = true) String resourceName) {

        String baseInternalUri = "/internal_protected";
        String internalUri = baseInternalUri + Util.encodePath(protectedLevel, resourceName);
        log.atInfo().log("Generated internal URI:" + internalUri);

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Accel-Redirect", internalUri);
        return Mono.just(ResponseEntity.ok().headers(headers).build());
    }

}