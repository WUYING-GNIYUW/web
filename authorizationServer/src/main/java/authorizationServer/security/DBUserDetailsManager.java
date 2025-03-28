package authorizationServer.security;


import authorizationServer.constant.UserConstant;
import authorizationServer.exception.LoginException;
import authorizationServer.pojo.User;
import authorizationServer.thread.UseridThread;
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
    private final PasswordEncoder passwordEncoder;
    private final UserConstant userConstant;
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
    public boolean userExists(String username) {
        return false;
    }

    @Override
    public UserDetails loadUserByUsername(String userid) throws UsernameNotFoundException {
        UseridThread.set(userid);
        return org.springframework.security.core.userdetails.User
                .withUsername("user.getUserid()")
                .build();
    }
    public void loadUserByUserid(String userid) throws UsernameNotFoundException {

    }

}
