package com.novli.netty.chat.service;

import com.novli.netty.chat.bo.FindFriendReq;
import com.novli.netty.chat.pojo.Users;
import com.novli.netty.chat.util.exception.ChatException;
import com.novli.netty.chat.vo.FriendReqOpeVo;
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
     * @param userName
     * @return boolean
     * @author Liyanpeng
     * @date 2019/6/5 17:20
     **/
    boolean queryUserNameIsExist(String userName);

    /**
     * 用户登录和注册
     *
     * @param userName
     * @param password
     * @return Users
     * @author Liyanpeng
     * @date 2019/6/5 17:20
     **/
    Users queryUserForLogin(String userName, String password);

    /**
     * 保存用户信息
     *
     * @param users
     * @return Users
     * @author Liyanpeng
     * @date 2019/6/5 17:20
     **/
    Users save(Users users) throws Exception;

    /**
     * 更新用户信息
     *
     * @param users
     * @return Users
     * @author Liyanpeng
     * @date 2019/6/5 17:20
     **/
    Users update(Users users) throws ChatException;

    /**
     * 添加好友前置条件
     *
     * @param findFriendReq
     * @return Integer
     * @author Liyanpeng
     * @date 2019/6/25 16:44
     **/
    Integer perConditionSearchFriends(FindFriendReq findFriendReq);

    /**
     * 通过用户名字查询用户信息
     *
     * @param userName
     * @return Users
     * @author Liyanpeng
     * @date 2019/6/25 16:43
     **/
    Users queryUserInfoByUserName(String userName);

    /**
     * 发送好友添加请求
     *
     * @param findFriendReq
     * @author Liyanpeng
     * @date 2019/6/25 16:43
     **/
    void sendFriendRequest(FindFriendReq findFriendReq);

    /**
     * 查询好友请求列表
     *
     * @param userId
     * @return List<Users>
     * @author Liyanpeng
     * @date 2019/6/29 10:37
     **/
    List<FriendReqVo> queryFriendsReq(String userId);

    /**
     * 删除好友列表
     *
     * @param acceptUserId
     * @param sendUserId
     * @author NovLi
     * @date 2019/7/2
     **/
    void delFriendReq(String acceptUserId, String sendUserId);

    /**
     * 好友通过
     *
     * @param acceptUserId
     * @param sendUserId
     * @author NovLi
     * @description //TODO
     * @date 2019/7/2
     **/
    void passFriendReq(String acceptUserId, String sendUserId);
}
