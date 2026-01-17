package com.wuying.gatewayServer.controller;

import com.wuying.common.util.Util;
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
        System.out.println("Generated internal URI:" + internalUri);

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Accel-Redirect", internalUri);
        return Mono.just(ResponseEntity.ok().headers(headers).build());
    }

    @GetMapping("/downloadResource")
    public Mono<ResponseEntity<Void>> downloadResource(
            @RequestParam("type") String type,
            @RequestParam("format") String format,
            @RequestParam("resource_name") String resourceName,
            @RequestParam("file_extension") String fileExtension,
            Principal principal) throws MalformedURLException {

        Path filePath;
        filePath = Paths.get(Util.encodePath(fileExtension, principal.getName(), type, format)).resolve(resourceName).normalize();
        Resource resource;
        resource = new UrlResource(filePath.toUri());

        if (!resource.exists()) {
            return Mono.just(ResponseEntity.notFound().build());
        }

        String baseInternalUri = "/internal_protected";
        String internalUri = baseInternalUri + Util.encodePath(fileExtension, principal.getName(), type, format, resourceName);

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Accel-Redirect", internalUri);
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + resourceName);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        System.out.println("Generated internal URI:" + internalUri);

        return Mono.just(ResponseEntity.ok().headers(headers).build());
    }
}