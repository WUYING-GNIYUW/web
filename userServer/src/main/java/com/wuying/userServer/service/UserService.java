package com.wuying.userServer.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.wuying.common.pojo.User;
import com.wuying.common.pojo.UserInfo;
import java.security.Principal;
import java.util.List;
import java.util.Map;


public interface UserService extends IService<User> {
    Boolean addUser(User addUser);
    List<User> getUsers(User queryUser);
    Boolean removeUser(User user);
//    String identifySelfClient(Principal principal);
//    HttpHeaders startSticky(Instance chosenInstance, Jwt jwt, Principal principal);

    Map<String,List<User>> getFriends(Principal principal);
    UserInfo getUserInfo(String userId);
//    List<Map<String, Object>> getUserInfos(Principal principal);

}
