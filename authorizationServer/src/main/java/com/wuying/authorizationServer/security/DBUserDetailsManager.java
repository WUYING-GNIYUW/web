package com.wuying.authorizationServer.security;


import com.wuying.authorizationServer.service.UserServiceImpl;
import com.wuying.common.feign.clients.UserClient;
import com.wuying.common.pojo.Result;
import com.wuying.common.pojo.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class DBUserDetailsManager implements UserDetailsManager, UserDetailsService {
    private final UserServiceImpl userServiceImpl;
    private final UserClient userclient;
    @Override
    public void createUser(UserDetails user) {

    }

    @Override
    public void updateUser(UserDetails user) {

    }

    @Override
    public void deleteUser(String username) {

    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {

    }

    @Override
    public boolean userExists(String userId) {
        Result<List<User>> userInfo = userclient.getUsers(User.builder().userId(Long.parseLong(userId)).build());
        User userInDB = userInfo.getData().get(0);
        return userInfo.getMessage().equals("user exist");
        //User userInDB = User.builder().userId("wuying").password(passwordEncoder.encode("000000")).roles("ADMIN").build();

    }

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
//        Result<List<User>> userInfo = userclient.getUsers(User.builder().userId(Long.parseLong(userId)).build());
        Result<List<User>> userInfo = userServiceImpl.getUsers(User.builder().userId(Long.parseLong(userId)).build());
        User userInDB = userInfo.getData().get(0);
        //User userInDB = User.builder().userId("wuying").password(passwordEncoder.encode("000000")).roles("ADMIN").build();
        return org.springframework.security.core.userdetails.User
                .withUsername(Long.toString(userInDB.getUserId()))
                .password(userInDB.getPassword())
                .accountLocked(false)
                .roles(userInDB.getRoles())
//                .authorities("SCOPE_openid")
                .build();
    }
}
