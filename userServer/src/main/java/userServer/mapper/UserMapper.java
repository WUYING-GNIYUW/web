package userServer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import userServer.pojo.User;
import org.apache.ibatis.annotations.*;
import org.springframework.transaction.annotation.Transactional;


@Mapper
@Transactional(rollbackFor = {Exception.class})
public interface UserMapper extends BaseMapper<User> {
}
