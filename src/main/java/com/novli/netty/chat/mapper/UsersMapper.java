package com.novli.netty.chat.mapper;


import com.novli.netty.chat.pojo.Users;
import com.novli.netty.chat.util.mybaties.ChatMapper;
import com.novli.netty.chat.vo.FriendReqVo;
import com.novli.netty.chat.vo.MyFriendsVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface UsersMapper extends ChatMapper<Users> {

    Users selectOne(Users users);

    /**
     * 查询好友请求列表
     *
     * @param userId
     * @return List<Users>
     * @author Liyanpeng
     * @date 2019/6/29 10:42
     **/
    List<FriendReqVo> queryFriendsReq(@Param("userId") String userId);

    /**
     * 查询好友列表
     *
     * @param userId
     * @return List<Users>
     * @author Liyanpeng
     * @date 2019/6/29 10:42
     **/
    List<MyFriendsVo> queryMyFriends(@Param("userId") String userId);
}
