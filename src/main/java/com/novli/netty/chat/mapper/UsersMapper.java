package com.novli.netty.chat.mapper;


import com.novli.netty.chat.pojo.Users;
import com.novli.netty.chat.util.mybaties.ChatMapper;
import com.novli.netty.chat.vo.FriendReqVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;


public interface UsersMapper extends ChatMapper<Users> {

    Users selectOne(Users users);

    /** 查询好友列表
     * @author Liyanpeng
     * @date 2019/6/29 10:42
     * @param userId
     * @return List<Users>
     **/
    List<FriendReqVo> queryFriendsReq(@Param("userId") String userId);
}
