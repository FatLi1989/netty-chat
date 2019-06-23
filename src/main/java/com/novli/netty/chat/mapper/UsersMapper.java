package com.novli.netty.chat.mapper;


import com.novli.netty.chat.pojo.Users;
import com.novli.netty.chat.util.mybaties.ChatMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface UsersMapper extends ChatMapper<Users> {

    int deleteByPrimaryKey(String id);

    int insert(Users record);

    int insertSelective(Users record);

    Users selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Users record);

    int updateByPrimaryKey(Users record);

    Users selectOne(Users users);
}
