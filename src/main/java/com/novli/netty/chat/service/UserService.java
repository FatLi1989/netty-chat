package com.novli.netty.chat.service;

import com.novli.netty.chat.pojo.Users;
import com.novli.netty.chat.util.exception.ChatException;
import com.novli.netty.chat.vo.UsersVo;
/**
 * @author Liyanpeng
 * @date 2019/6/5 17:18
 **/
public interface UserService {
    /**
     * 查询用户名称是否存在
     * @author Liyanpeng
     * @date 2019/6/5 17:20
     **/
    boolean queryUserNameIsExist(String userName);
    /**
     * 用户登录和注册
     * @author Liyanpeng
     * @date 2019/6/5 17:20
     **/
    UsersVo queryUserForLogin(String userName, String password);
    /**
     * 保存用户信息
     * @author Liyanpeng
     * @date 2019/6/5 17:20
     **/
    Users save(Users users) throws Exception;
    /**
     * 更新用户信息
     * @author Liyanpeng
     * @date 2019/6/5 17:20
     **/
    Users update(Users users) throws ChatException;
}
