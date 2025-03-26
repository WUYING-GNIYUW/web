package security.security;


import security.constant.UserConstant;
import security.exception.LoginException;
import security.pojo.User;
import security.service.UserService;
import security.thread.UseridThread;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Component;

import java.util.Objects;


@Component
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class DBUserDetailsManager implements UserDetailsManager, UserDetailsService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final UserConstant userConstant;
    @Override
    public void createUser(UserDetails user) {
        userService.save(User.builder()
                        .username(user.getUsername())
                        .password(passwordEncoder.encode(user.getPassword()))
                        .build());
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
    public boolean userExists(String username) {
        return false;
    }

    @Override
    public UserDetails loadUserByUsername(String userid) throws UsernameNotFoundException {
        User user = loadUserByUserid(userid);
        if(!Objects.equals(user.getAvailableState(), userConstant.available)) {
            throw new LoginException("帐号已锁定");//TODO:LoadAccountException("账号不存在")
        }
        UseridThread.set(userid);
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUserid())
                .password(user.getPassword())
                .accountLocked(!Objects.equals(user.getAvailableState(), userConstant.available))
                .roles(user.getRoles().split(","))
                .build();
    }
    public User loadUserByUserid(String userid) throws UsernameNotFoundException {
        User user = userService.getById(userid);
        if(user == null) {
            throw new LoginException("帐号不存在");//TODO:LoadAccountException("账号不存在")
        }
        return user;
    }

}
