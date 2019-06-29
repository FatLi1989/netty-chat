package com.novli.netty.chat.service;

import com.novli.netty.chat.bo.FindFriendReq;
import com.novli.netty.chat.pojo.Users;
import com.novli.netty.chat.util.exception.ChatException;
import com.novli.netty.chat.vo.FriendReqVo;

import java.util.List;

/**
 * @author Liyanpeng
 * @date 2019/6/5 17:18
 **/
public interface UserService {
    /**
     * 查询用户名称是否存在
     *
     * @author Liyanpeng
     * @date 2019/6/5 17:20
     * @param userName
     * @return boolean
     **/
    boolean queryUserNameIsExist(String userName);

    /**
     * 用户登录和注册
     *
     * @author Liyanpeng
     * @date 2019/6/5 17:20
     * @param userName
     * @param password
     * @return Users
     **/
    Users queryUserForLogin(String userName, String password);

    /**
     * 保存用户信息
     *
     * @author Liyanpeng
     * @date 2019/6/5 17:20
     * @param users
     * @return Users
     **/
    Users save(Users users) throws Exception;

    /**
     * 更新用户信息
     *
     * @author Liyanpeng
     * @date 2019/6/5 17:20
     * @param users
     * @return Users
     **/
    Users update(Users users) throws ChatException;

    /**
     * 添加好友前置条件
     *
     * @author Liyanpeng
     * @date 2019/6/25 16:44
     * @param findFriendReq
     * @return Integer
     **/
    Integer perConditionSearchFriends(FindFriendReq findFriendReq);

    /**
     * 通过用户名字查询用户信息
     *
     * @author Liyanpeng
     * @date 2019/6/25 16:43
     * @param userName
     * @return Users
     **/
    Users queryUserInfoByUserName(String userName);

    /**
     * 发送好友添加请求
     *
     * @author Liyanpeng
     * @date 2019/6/25 16:43
     * @param findFriendReq
     **/
    void sendFriendRequest(FindFriendReq findFriendReq);
    /** 查询好友请求列表
     *
     * @author Liyanpeng
     * @date 2019/6/29 10:37
     * @param userId
     * @return List<Users>
     **/
    List<FriendReqVo> queryFriendsReq(String userId);
}
