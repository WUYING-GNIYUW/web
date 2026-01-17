package com.wuying.userServer.controller;

import com.wuying.common.pojo.ResourceInfo;
import com.wuying.common.pojo.Result;
import com.wuying.common.pojo.User;
import com.wuying.userServer.service.ResourceServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;


@RestController
@RequestMapping("/user/resource")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ResourceController {
    private final ResourceServiceImpl resourceServiceImpl;

    @GetMapping("/getAvailable")
    public Result<?> getInfoOfConversations(Principal principal) {
        return Result.<List<ResourceInfo>>builder().data(resourceServiceImpl.getAvailable(principal)).build();
    }
}