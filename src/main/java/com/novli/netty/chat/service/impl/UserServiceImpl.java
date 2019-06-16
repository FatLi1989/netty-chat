package com.novli.netty.chat.service.impl;

import com.novli.netty.chat.mapper.UsersMapper;
import com.novli.netty.chat.pojo.Users;
import com.novli.netty.chat.service.UserService;
import com.novli.netty.chat.util.QRcode.QRCodeUtils;
import com.novli.netty.chat.util.constant.FileConstant;
import com.novli.netty.chat.util.exception.ChatException;
import com.novli.netty.chat.util.file.FastDFSClient;
import com.novli.netty.chat.util.file.FileUtils;
import com.novli.netty.chat.util.password.MD5Utils;
import com.novli.netty.chat.vo.UsersVo;
import io.netty.util.internal.ResourcesUtil;
import lombok.extern.slf4j.Slf4j;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
        qrCodeUtils.createQRCode(FileConstant.QRCODE_PTAH,"chat_qrcode:" + users.getUsername());
        MultipartFile multipartFile = FileUtils.fileToMultipart(FileConstant.QRCODE_PTAH);
        Long startTime = System.currentTimeMillis();
        log.info("-----------开始向fastDFS上传文件-----------");
        String qrUrl = fastDFSClient.uploadBase64(multipartFile) ;
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
}
