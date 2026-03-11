package com.wuying.userServer.controller;

import com.wuying.common.pojo.ResourceInfo;
import com.wuying.common.pojo.Result;
import com.wuying.userServer.service.ResourceServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
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
            @RequestParam(name = "resource_name") String resourceName,
            Principal principal) throws MalformedURLException {
        return resourceServiceImpl.download(resourceName, principal);
    }

    @PostMapping("/upload")
    public ResponseEntity<Result<?>> uploadResource(
            @RequestParam(name = "resource_name") String resourceName,
            @RequestParam(name = "resource") MultipartFile file,
            Principal principal) throws IOException {
        return resourceServiceImpl.upload(resourceName, file, principal);
    }
}