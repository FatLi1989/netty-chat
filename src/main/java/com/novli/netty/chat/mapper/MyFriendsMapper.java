package com.novli.netty.chat.mapper;

import com.novli.netty.chat.pojo.MyFriends;
import com.novli.netty.chat.util.mybaties.ChatMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;


public interface MyFriendsMapper extends ChatMapper<MyFriends> {

    int insert(MyFriends record);

    int insertSelective(MyFriends record);
}
