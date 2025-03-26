package user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import user.pojo.Result;
import user.pojo.User;

import java.util.List;


public interface UserService extends IService<User> {
    Result<Boolean> addUser(User addUser);
    Result<List<User>> getUsers(User queryUser);
    Result<Boolean> removeUser(User user);

}
