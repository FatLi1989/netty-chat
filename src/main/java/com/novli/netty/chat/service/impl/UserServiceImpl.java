package com.novli.netty.chat.service.impl;

import com.novli.netty.chat.bo.FindFriendReq;
import com.novli.netty.chat.enums.FriendOperateEnum;
import com.novli.netty.chat.enums.SearchFriendEnum;
import com.novli.netty.chat.mapper.FriendsRequestMapper;
import com.novli.netty.chat.mapper.MyFriendsMapper;
import com.novli.netty.chat.mapper.UsersMapper;
import com.novli.netty.chat.pojo.FriendsRequest;
import com.novli.netty.chat.pojo.MyFriends;
import com.novli.netty.chat.pojo.Users;
import com.novli.netty.chat.service.UserService;
import com.novli.netty.chat.util.QRcode.QRCodeUtils;
import com.novli.netty.chat.util.constant.FileCons;
import com.novli.netty.chat.util.exception.ChatException;
import com.novli.netty.chat.util.file.FastDFSClient;
import com.novli.netty.chat.util.file.FileUtils;
import com.novli.netty.chat.util.password.MD5Utils;
import com.novli.netty.chat.vo.FriendReqOpeVo;
import com.novli.netty.chat.vo.FriendReqVo;
import lombok.extern.slf4j.Slf4j;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import java.util.Date;
import java.util.List;

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

    @Autowired
    private FriendsRequestMapper friendsRequestMapper;


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
        users = usersMapper.selectByPrimaryKey(users);
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
        mfc.andEqualTo("myUserId", findFriendReq.getUserId());
        mfc.andEqualTo("myFriendUserId", users.getId());

        MyFriends myFriends = myFriendsMapper.selectOneByExample(example);
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
        return usersMapper.selectOneByExample(example);
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ChatException.class)
    public void sendFriendRequest(FindFriendReq findFriendReq) {
        /**
         *   查询到添加好友的名称
         **/
        Users friend = queryUserInfoByUserName(findFriendReq.getUserName());

        Example fre = new Example(FriendsRequest.class);
        Criteria frc = fre.createCriteria();
        frc.andEqualTo("acceptUserId", friend.getId());
        frc.andEqualTo("sendUserId", findFriendReq.getUserId());
        FriendsRequest request = friendsRequestMapper.selectOneByExample(fre);

        if (request == null) {
            FriendsRequest friendsRequest = new FriendsRequest();

            friendsRequest.setId(sid.nextShort());
            friendsRequest.setSendUserId(findFriendReq.getUserId());
            friendsRequest.setAcceptUserId(friend.getId());
            friendsRequest.setRequestDateTime(new Date());
            friendsRequestMapper.insert(friendsRequest);
        }

    }

    /**
     * 查询好友请求列表
     *
     * @author Liyanpeng
     * @date 2019/6/29 10:38
     **/
    @Override
    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = ChatException.class)
    public List<FriendReqVo> queryFriendsReq(String userId) {
        return usersMapper.queryFriendsReq(userId);
    }

    /**
     * 删除好友请求
     *
     * @author Liyanpeng
     * @date 2019/7/1 14:26
     **/
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ChatException.class)
    public void delFriendReq(String acceptUserId, String sendUserId) {
        Example fre = new Example(FriendsRequest.class);
        Criteria frc = fre.createCriteria();
        frc.andEqualTo("sendUserId", sendUserId);
        frc.andEqualTo("acceptUserId", acceptUserId);
        friendsRequestMapper.deleteByExample(fre);
    }

    /**
     * 通过好友请求
     *
     * @param sendUserId
     * @param acceptUserId
     * @author NovLi
     * @date 2019/7/2
     **/
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ChatException.class)
    public void passFriendReq(String acceptUserId, String sendUserId) {
        insertFriendList(acceptUserId, sendUserId);
        insertFriendList(sendUserId, acceptUserId);
        //删除好友请求
        delFriendReq(acceptUserId, sendUserId);
    }


    /**
     * 添加到好友列表中
     *
     * @author Liyanpeng
     * @date 2019/7/1 14:26
     **/
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = ChatException.class)
    public void insertFriendList(String myUserId, String myFriendUserId) {
        MyFriends myFriends = new MyFriends();
        myFriends.setId(sid.nextShort());
        myFriends.setMyUserId(myUserId);
        myFriends.setMyFriendUserId(myFriendUserId);
        myFriendsMapper.insert(myFriends);
    }

}
