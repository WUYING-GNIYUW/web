package security.controller;
import security.pojo.Result;
import security.pojo.User;
import security.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RequestMapping("/user")
public class UserController {
    private final UserServiceImpl userServiceImpl;

    @GetMapping("/test/{id}")
    public Result getbyidJustFortest(@PathVariable(value = "id") String id) {
        User user = userServiceImpl.getById(id);
        return Result.<User>builder().data(user).build();
    }

    @PostMapping("/add")
    public Result addUser(@RequestBody User addUser) {
        return userServiceImpl.addUser(addUser);
    }
    @PostMapping("/get")
    public Result getUsers(@RequestBody User queryUser) {
        return userServiceImpl.getUsers(queryUser);
    }
    @PutMapping("/update")
    public Result<Boolean> update(@RequestBody User user) {
        return Result.<Boolean>builder().data(userServiceImpl.updateById(user)).build();
    }
    @PostMapping("/remove")
    public Result removeUser(@RequestBody User user) {
        return userServiceImpl.removeUser(user);
    }
    @PostMapping("/login")
    public Result login(@RequestBody User user) {
        return userServiceImpl.login(user);
    }
    @GetMapping
    public String test() {
        System.out.println("ok");
        return "OK";
    }
}
