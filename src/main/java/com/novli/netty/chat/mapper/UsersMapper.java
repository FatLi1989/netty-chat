package com.novli.netty.chat.mapper;


import com.novli.netty.chat.pojo.Users;
import com.novli.netty.chat.vo.UsersVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface UsersMapper {

    int deleteByPrimaryKey(String id);

    int insert(Users record);

    int insertSelective(Users record);

    Users selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Users record);

    int updateByPrimaryKey(Users record);

    UsersVo selectOne(Users users);
}