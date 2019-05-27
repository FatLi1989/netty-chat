package com.novli.netty.chat.mapper;

import com.novli.netty.chat.pojo.MyFriends;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MyFriendsMapper {

    int insert(MyFriends record);

    int insertSelective(MyFriends record);
}