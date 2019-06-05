package com.novli.netty.chat.controller;

import com.novli.netty.chat.bo.UserBO;
import com.novli.netty.chat.pojo.Users;
import com.novli.netty.chat.service.UserService;
import com.novli.netty.chat.util.password.MD5Utils;
import com.novli.netty.chat.util.result.JSONResult;
import com.novli.netty.chat.vo.UsersVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "u")
public class LoginController {

    @Autowired
    UserService userService;

    @RequestMapping(value = "/registOrLogin", method = {RequestMethod.POST})
    public JSONResult login(@RequestBody Users users) throws Exception {
        // 0. 判断用户名和密码不能为空
        if (StringUtils.isBlank(users.getUsername()) || StringUtils.isBlank(users.getPassword())) {
            JSONResult.errorMsg("用户名和密码不能为空");
        }
        // 1. 判断用户名是否存在, 如果存在就登录, 不存在就注册
        boolean userNameIsExist = userService.queryUserNameIsExist(users.getUsername());

        UsersVo userResult = null;
        if (userNameIsExist) {
            // 1.1 登录
            userResult = userService.queryUserForLogin(users.getUsername(),
                    MD5Utils.getMD5Str(users.getPassword()));
            if (userResult == null) {
                return JSONResult.errorMsg("用户名或密码不正确");
            }
        } else {
            // 1.2 注册
            users = userService.save(users);
        }
        BeanUtils.copyProperties(users, userResult);
        return JSONResult.ok(userResult);
    }

    @RequestMapping(value = "/uploadFaceBase64", method = {RequestMethod.POST})
    public JSONResult uploadFaceBase64(@RequestBody UserBO userBO) throws Exception {






        return JSONResult.ok();
    }
}
