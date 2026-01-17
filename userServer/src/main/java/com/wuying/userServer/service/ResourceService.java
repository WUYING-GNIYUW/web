package com.wuying.userServer.service;


import com.wuying.common.pojo.ResourceInfo;

import java.security.Principal;
import java.util.List;


public interface ResourceService {

    List<ResourceInfo> getAvailable(Principal principal);
}
