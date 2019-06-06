package com.novli.netty.chat.controller;

import com.novli.netty.chat.bo.UserBO;
import com.novli.netty.chat.pojo.Users;
import com.novli.netty.chat.service.UserService;
import com.novli.netty.chat.util.constant.FileConstant;
import com.novli.netty.chat.util.file.FastDFSClient;
import com.novli.netty.chat.util.file.FileUtils;
import com.novli.netty.chat.util.password.MD5Utils;
import com.novli.netty.chat.util.result.JSONResult;
import com.novli.netty.chat.vo.UsersVo;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.server.Authentication;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@Slf4j
@RestController
@RequestMapping(value = "u")
public class LoginController {

    @Autowired
    UserService userService;

    @Autowired
    FastDFSClient fastDFSClient;

    @RequestMapping(value = "/registOrLogin", method = {RequestMethod.POST})
    public JSONResult login(@RequestBody Users users) throws Exception {
        // 0. 判断用户名和密码不能为空
        if (StringUtils.isBlank(users.getUsername()) || StringUtils.isBlank(users.getPassword())) {
            JSONResult.errorMsg("用户名和密码不能为空");
        }
        // 1. 判断用户名是否存在, 如果存在就登录, 不存在就注册
        boolean userNameIsExist = userService.queryUserNameIsExist(users.getUsername());

        Users userResult = null;
        if (userNameIsExist) {
            // 1.1 登录
            userResult = userService.queryUserForLogin(users.getUsername(),
                    MD5Utils.getMD5Str(users.getPassword()));
            if (userResult == null) {
                return JSONResult.errorMsg("用户名或密码不正确");
            }
        } else {
            // 1.2 注册
            userResult = userService.save(users);
        }
        UsersVo usersVo = new UsersVo();
        BeanUtils.copyProperties(userResult, usersVo);
        return JSONResult.ok(usersVo);
    }

    @RequestMapping(value = "/uploadFaceBase64", method = {RequestMethod.POST})
    public JSONResult uploadFaceBase64(@RequestBody UserBO userBO) throws Exception {
        /*  1. 接收到base64字符串转换成文件
            2. 文件转换成MultipartFile上传到fastDFS
            3. 拿到图片返回路径并保存到数据库
            4. 返回信息给前端    */
        Users result = new Users();
        if (!StringUtils.isEmpty(userBO.getFaceData())) {
            if (FileUtils.base64ToFile(FileConstant.FILE_PTAH, userBO.getFaceData())) {
                MultipartFile file = FileUtils.fileToMultipart(FileConstant.FILE_PTAH);
                Long startTime = System.currentTimeMillis();
                log.info("-----------开始向fastDFS上传文件-----------");
                String filePath = fastDFSClient.uploadBase64(file);
                log.info("-----------向fastDFS上传文件完毕耗时 : {}-----------", System.currentTimeMillis() - startTime );
                String thump = "_150x150.";
                String arr[] = filePath.split("\\.");
                String thumpImgUrl = arr[0] + thump + arr[1];

                result.setId(userBO.getUserId());
                result.setFaceImage(thumpImgUrl);
                result.setFaceImageBig(filePath);

                result = userService.update(result);
            }
        }
        return JSONResult.ok(result);
    }
}
