package com.novli.netty.chat.mapper;


import com.novli.netty.chat.pojo.Users;
import com.novli.netty.chat.util.mybaties.ChatMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;


public interface UsersMapper extends ChatMapper<Users> {

    Users selectOne(Users users);
}
