package com.wuying.userServer.controller;

import com.wuying.common.pojo.ResourceInfo;
import com.wuying.common.pojo.Result;
import com.wuying.common.util.Util;
import com.wuying.userServer.service.ResourceServiceImpl;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.List;


@RestController
@RequestMapping("/user/resource")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ResourceController {
    private final ResourceServiceImpl resourceServiceImpl;
    @GetMapping("/getAvailable")
    public Result<?> getAvailableResource(Principal principal) {
        return Result.<List<ResourceInfo>>builder().data(resourceServiceImpl.getAvailable(principal)).build();
    }

    @GetMapping("/download")
    public ResponseEntity<Result<?>> downloadResource(
            @RequestParam(name = "resource_name", required = true) String resourceName,
            Principal principal) throws MalformedURLException {
        return resourceServiceImpl.download(resourceName, principal);
    }

    @PostMapping("/upload")
    public ResponseEntity<Result<?>> uploadResource(
            @RequestParam(name = "resource_name", required = true) String resourceName,
            @RequestParam(name = "file", required = true) MultipartFile file,
            Principal principal) throws IOException {
        return resourceServiceImpl.upload(resourceName, file, principal);
    }
}