package user.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import user.constant.UserConstant;
import user.exception.AddUserException;
import user.mapper.UserMapper;
import user.pojo.Result;
import user.pojo.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.*;
@Service
//@Transactional(rollbackFor = {Exception.class})
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class UserServiceImpl extends ServiceImpl<UserMapper,User> implements UserService {
    private final UserMapper userMapper;
    private final UserConstant userConstant;
    private final JwtConstant jwtConstant;

    public Result<Boolean> addUser(User adduser) {
        if(getById(adduser.getUserid())==null){
            save(adduser);
            return Result.<Boolean>builder().data(true).build();
        }
        else {
            throw new AddUserException("账号已存在");
        }
    }

    public Result<List<User>> getUsers(User queryUser) {
        lambdaQuery()
                .eq(queryUser.getUserid() != null, User::getUserid, queryUser.getUserid())
                .like(queryUser.getUsername() != null, User::getUsername, queryUser.getUsername())
                .eq(queryUser.getAvailableState() != null, User::getAvailableState, queryUser.getAvailableState())
                .eq(queryUser.getOnlineState() != null, User::getOnlineState, queryUser.getOnlineState())
                .list();

        Map<String, Object> mapOfUser = BeanUtil.beanToMap(queryUser);
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        List<User> userList= list(userQueryWrapper.allEq(mapOfUser));
        return Result.<List<User>>builder().data(userList).build();
    }

    public Result<Boolean> removeUser(User removeUser) {
/*        List<User> Users = new ArrayList<>();
        List<String> UserIds = new ArrayList<>();
        Users.forEach(User->{UserIds.add(User.getUsername());});
        Users.forEach(System.out::println);*/
        if(getById(removeUser.getUserid()) != null){
            removeById(removeUser);
            return Result.<Boolean>builder().data(true).build();
        }
        else {
            throw new AddUserException("账号不存在");
        }
    }
    public Result<String> login(User loginUser) {
        User user = getById(loginUser.getUserid());
        if(user == null) {
            throw new LoginException("帐号不存在");
        }
        if(Objects.equals(user.getAvailableState(), userConstant.unavailable)) {
            throw new LoginException("该账号已经锁定");
        }
        if(user.getPassword().equals(loginUser.getPassword())) {
            Map<String, Object> claims = new HashMap<>();
            claims.put(jwtConstant.getThread_userid_key(),user.getUserid());
            claims.put(jwtConstant.getThread_authority_key(), "userddd");
            String token = Jwts.builder()
                     .signWith(SignatureAlgorithm.HS256, jwtConstant.getSigning_key())
                     .setClaims(claims)
                     .setExpiration(new Date(System.currentTimeMillis() + 3*3600*1000))
                     .compact();
            return Result.<String>builder().data(jwtConstant.getToken_key() + token).build();
        }
        else {
            throw new LoginException("密码错误");
        }
    }
}
