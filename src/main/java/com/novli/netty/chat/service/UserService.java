package com.novli.netty.chat.service;

import com.novli.netty.chat.pojo.Users;
import com.novli.netty.chat.vo.UsersVo;

public interface UserService {

    boolean queryUserNameIsExist(String userName);

    UsersVo queryUserForLogin(String userName, String password);

    Users save(Users users) throws Exception;
}
