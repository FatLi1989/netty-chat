package com.novli.netty.chat.service.impl;

import com.novli.netty.chat.bo.FindFriendReq;
import com.novli.netty.chat.enums.SearchFriendEnum;
import com.novli.netty.chat.mapper.MyFriendsMapper;
import com.novli.netty.chat.mapper.UsersMapper;
import com.novli.netty.chat.pojo.MyFriends;
import com.novli.netty.chat.pojo.Users;
import com.novli.netty.chat.service.UserService;
import com.novli.netty.chat.util.QRcode.QRCodeUtils;
import com.novli.netty.chat.util.constant.FileCons;
import com.novli.netty.chat.util.exception.ChatException;
import com.novli.netty.chat.util.file.FastDFSClient;
import com.novli.netty.chat.util.file.FileUtils;
import com.novli.netty.chat.util.password.MD5Utils;
import lombok.extern.slf4j.Slf4j;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private Sid sid;

    @Autowired
    private QRCodeUtils qrCodeUtils;

    @Autowired
    private FastDFSClient fastDFSClient;

    @Autowired
    private MyFriendsMapper myFriendsMapper;


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
        // chat_qrcode:[username]
        qrCodeUtils.createQRCode(FileCons.QRCODE_PTAH, "chat_qrcode:" + users.getUsername());
        MultipartFile multipartFile = FileUtils.fileToMultipart(FileCons.QRCODE_PTAH);
        Long startTime = System.currentTimeMillis();
        log.info("-----------开始向fastDFS上传文件-----------");
        String qrUrl = fastDFSClient.uploadBase64(multipartFile);
        log.info("-----------向fastDFS上传文件完毕耗时 : {}-----------", System.currentTimeMillis() - startTime);
        users.setQrcode(qrUrl);
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
        users.setFaceImage(users.getFaceImage());
        users.setFaceImageBig(users.getFaceImageBig());
        return users;
    }

    /**
     * @param findFriendReq
     * @return Integer
     * @author NovLi
     * @description 添加好友前置条件
     * @date 2019/6/23
     **/
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Integer perConditionSearchFriends(FindFriendReq findFriendReq) {
        Users users = queryUserInfoByUserName(findFriendReq.getUserName());
        //返回该用户不存在
        if (users == null) {
            return SearchFriendEnum.USER_NOT_EXIST.getStatus();
        }
        //不可以添加自己为好友
        if (findFriendReq.getUserId().equals(users.getId())) {
            return SearchFriendEnum.NOT_YOURSELF.getStatus();
        }
        //添加的用户已经是你的好友了
        Example example = new Example(MyFriends.class);
        Criteria mfc = example.createCriteria();
        mfc.andEqualTo("my_user_id", findFriendReq.getUserId());
        mfc.andEqualTo("my_friend_user_id", users.getId());

        MyFriends myFriends = myFriendsMapper.selectOneByExample(mfc);
        if (myFriends != null) {
            return SearchFriendEnum.ALREADY_BE_FRIENDS.getStatus();
        }
        return SearchFriendEnum.SUCCESS.getStatus();
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = ChatException.class)
    public Users queryUserInfoByUserName(String userName) {
        Example example = new Example(Users.class);
        Criteria uc = example.createCriteria();
        uc.andEqualTo("username", userName);
        return usersMapper.selectOneByExample(uc);
    }
}
