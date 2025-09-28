package com.wuying.authorizationServer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.wuying.common.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.transaction.annotation.Transactional;


@Mapper
@Transactional(rollbackFor = {Exception.class})
public interface UserMapper extends BaseMapper<User> {
}
