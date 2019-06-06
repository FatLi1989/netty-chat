package com.novli.netty.chat.service.impl;

import com.novli.netty.chat.mapper.UsersMapper;
import com.novli.netty.chat.pojo.Users;
import com.novli.netty.chat.service.UserService;
import com.novli.netty.chat.util.constant.FileConstant;
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
        Users usersVo = usersMapper.selectOne(users);
        return usersVo == null ? false : true;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Users queryUserForLogin(String userName, String password) {

        Users users = new Users();
        users.setUsername(userName);
        users.setPassword(password);
        users = usersMapper.selectOne(users);
        return users;
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

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ChatException.class)
    public Users update(Users users) throws ChatException {
        if (usersMapper.updateByPrimaryKeySelective(users) <= 0) {
            throw new ChatException("修改用户信息失败", 500);
        }
        users = usersMapper.selectByPrimaryKey(users.getId());
        users.setFaceImage(FileConstant.FILE_SERVER + FileConstant.GROUP + users.getFaceImage());
        users.setFaceImageBig(FileConstant.FILE_SERVER + FileConstant.GROUP + users.getFaceImageBig());
        return users;
    }
}
