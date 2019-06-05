package com.novli.netty.chat.service.impl;

import com.novli.netty.chat.mapper.UsersMapper;
import com.novli.netty.chat.pojo.Users;
import com.novli.netty.chat.service.UserService;
import com.novli.netty.chat.util.exception.ChatException;
import com.novli.netty.chat.util.password.MD5Utils;
import com.novli.netty.chat.vo.UsersVo;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private Sid sid;


    @Override
    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = ChatException.class)
    public boolean queryUserNameIsExist(String userName) {
        Users users = new Users();
        users.setUsername(userName);
        UsersVo usersVo = usersMapper.selectOne(users);
        return usersVo == null ? false : true ;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public UsersVo queryUserForLogin(String userName, String password) {

        Users users = new Users();
        users.setUsername(userName);
        users.setPassword(password);
        UsersVo usersVo = usersMapper.selectOne(users);
        return usersVo;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Users save(Users users) throws Exception {

        String id = sid.nextShort();
        users.setId(id);
        users.setNickname(users.getUsername());
        //TODO 生成二维码
        users.setQrcode("");
        users.setFaceImage("");
        users.setFaceImageBig("");
        users.setPassword(MD5Utils.getMD5Str(users.getPassword()));
        usersMapper.insertSelective(users);
        return users;
    }
}
