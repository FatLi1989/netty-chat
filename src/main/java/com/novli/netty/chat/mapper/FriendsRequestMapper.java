package com.novli.netty.chat.mapper;


import com.novli.netty.chat.pojo.FriendsRequest;
import org.apache.ibatis.annotations.Mapper;

public interface FriendsRequestMapper {

    int deleteByPrimaryKey(String id);

    int insert(FriendsRequest record);

    int insertSelective(FriendsRequest record);

    int updateByPrimaryKeySelective(FriendsRequest record);

    int updateByPrimaryKey(FriendsRequest record);
}