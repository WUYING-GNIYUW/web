package common.feign.clients;

import common.pojo.Result;
import common.pojo.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;


@FeignClient("user-server")
public interface UserClient {
//    @PostMapping("/user/test/{id}")
//    Result<List<User>> feignGetByIdJustForTest(@PathVariable(value = "id") String id);
    @PostMapping("/api/user/get")
    Result<List<User>> getUsers(@RequestBody User queryUser);

}
