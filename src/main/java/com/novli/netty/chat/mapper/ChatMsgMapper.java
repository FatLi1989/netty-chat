package com.novli.netty.chat.mapper;

import com.novli.netty.chat.pojo.ChatMsg;
import org.apache.ibatis.annotations.Mapper;

public interface ChatMsgMapper {

    int insert(ChatMsg record);

    int insertSelective(ChatMsg record);
}