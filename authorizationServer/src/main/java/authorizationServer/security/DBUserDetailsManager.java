package authorizationServer.security;


//import common.feign.clients.UserClient;
import common.pojo.Result;
import common.pojo.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class DBUserDetailsManager implements UserDetailsManager, UserDetailsService {
    private final PasswordEncoder passwordEncoder;
    //private final UserClient userclient;
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
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        //Result<List<User>> userInfo = userclient.getUsers(User.builder().userId(userId).build());
        //User userInDB = userInfo.getData().get(0);
        User userInDB = User.builder().userId("wuying").password(passwordEncoder.encode("000000")).roles("ADMIN").build();
        return org.springframework.security.core.userdetails.User
                .withUsername(userInDB.getUserId())
                .password(userInDB.getPassword())
                .accountLocked(false)
                .roles(userInDB.getRoles())
                .authorities("SCOPE_openid")
                .build();
    }
}
