package com.wuying.userServer.service;


import com.wuying.common.pojo.ResourceInfo;
import com.wuying.common.pojo.Result;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.security.Principal;
import java.util.List;


public interface ResourceService {

    List<ResourceInfo> getAvailable(Principal principal);
    ResponseEntity<Result<?>> download(String resourceName, Principal principal) throws MalformedURLException;
    ResponseEntity<Result<?>> upload(String resourceName, MultipartFile file, Principal principal) throws IOException;
}
