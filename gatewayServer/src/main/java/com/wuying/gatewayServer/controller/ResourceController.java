package com.wuying.gatewayServer.controller;

import com.wuying.common.util.Util;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class ResourceController {
    @GetMapping("/{protected_level}/getResource")
    public Mono<ResponseEntity<Void>> getResource(
            @PathVariable("protected_level") String protectedLevel,
            @RequestParam("type") String type,
            @RequestParam("format") String format,
            @RequestParam("resource_name") String resourceName,
            @RequestParam("file_extension") String fileExtension) {

        String baseInternalUri = "/internal_protected";
        String internalUri = baseInternalUri + Util.encodePath(fileExtension, protectedLevel, type, format, resourceName);
        System.out.println("resourceName:" + resourceName);
        System.out.println("Generated internal URI:" + internalUri);

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Accel-Redirect", internalUri);
        return Mono.just(ResponseEntity.ok().headers(headers).build());
    }
}