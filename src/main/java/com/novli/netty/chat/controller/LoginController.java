package com.novli.netty.chat.controller;

import com.novli.netty.chat.bo.FindFriendReq;
import com.novli.netty.chat.bo.UserBO;
import com.novli.netty.chat.enums.SearchFriendEnum;
import com.novli.netty.chat.pojo.Users;
import com.novli.netty.chat.service.UserService;
import com.novli.netty.chat.util.constant.FileCons;
import com.novli.netty.chat.util.file.FastDFSClient;
import com.novli.netty.chat.util.file.FileUtils;
import com.novli.netty.chat.util.password.MD5Utils;
import com.novli.netty.chat.util.result.JSONResult;
import com.novli.netty.chat.vo.FriendReqVo;
import com.novli.netty.chat.vo.UsersVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Liyanpeng
 * @date 2019/6/29 10:36
 **/
@Slf4j
@RestController
@RequestMapping(value = "u")
public class LoginController {

    @Autowired
    UserService userService;

    @Autowired
    FastDFSClient fastDFSClient;

    /**
     * @param users
     * @return JSONResult
     * @author NovLi
     * @description 登录或注册
     * @date 2019/6/23
     **/
    @RequestMapping(value = "/registOrLogin", method = {RequestMethod.POST})
    public JSONResult login(@RequestBody @Valid Users users, BindingResult result) throws Exception {
        // 0. 判断用户名和密码不能为空
        if (result.hasErrors()) {
            return JSONResult.errorMsg(result.getFieldError().getDefaultMessage());
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

    /**
     * @param userBO
     * @return JSONResult
     * @author NovLi
     * @description 上传图片
     * @date 2019/6/23
     **/
    @RequestMapping(value = "/uploadFaceBase64", method = {RequestMethod.POST})
    public JSONResult uploadFaceBase64(@RequestBody UserBO userBO) throws Exception {
        /*  1. 接收到base64字符串转换成文件
            2. 文件转换成MultipartFile上传到fastDFS
            3. 拿到图片返回路径并保存到数据库
            4. 返回信息给前端    */
        Users result = new Users();
        if (!StringUtils.isEmpty(userBO.getFaceData())) {
            if (FileUtils.base64ToFile(FileCons.FILE_PTAH, userBO.getFaceData())) {
                MultipartFile file = FileUtils.fileToMultipart(FileCons.FILE_PTAH);
                Long startTime = System.currentTimeMillis();
                log.info("-----------开始向fastDFS上传文件-----------");
                String filePath = fastDFSClient.uploadBase64(file);
                log.info("-----------向fastDFS上传文件完毕耗时 : {}-----------", System.currentTimeMillis() - startTime);
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

    /**
     * @param findFriendReq
     * @return setNickName
     * @author NovLi
     * @description 通过名字搜索好友
     * @date 2019/6/23
     **/
    @RequestMapping(value = "/search/friend", method = {RequestMethod.POST})
    public JSONResult searchFriend(@RequestBody @Valid FindFriendReq findFriendReq, BindingResult result) {
        /**
         * 1. 搜索好友 -> id 和 好友名字校验
         * 2. 好友名称不存在 返回该用户不存在
         * 3. 好友名称是自己 返回不可以添加自己为好友
         * 4. 还有已经是你的好友 返回该用户已经是你的好友了
         * 5. 可以添加为好友
         **/
        if (result.hasErrors()) {
            return JSONResult.errorMsg(result.getFieldError().getDefaultMessage());
        }
        //查找添加还有前置条件没通过
        Integer status = userService.perConditionSearchFriends(findFriendReq);
        if (!status.equals(SearchFriendEnum.SUCCESS.getStatus())) {
            return JSONResult.errorMsg(SearchFriendEnum.getMsgBykey(status));
        }

        Users users = userService.queryUserInfoByUserName(findFriendReq.getUserName());
        UsersVo usersVo = new UsersVo();
        BeanUtils.copyProperties(users, usersVo);

        return JSONResult.ok(usersVo);
    }


    /**
     * @param findFriendReq
     * @return setNickName
     * @author NovLi
     * @description 通过名字搜索好友
     * @date 2019/6/23
     **/
    @RequestMapping(value = "/addFriendReq", method = {RequestMethod.POST})
    public JSONResult addFriendReq(@RequestBody @Valid FindFriendReq findFriendReq, BindingResult result) {
        /**
         * 1. 搜索好友 -> id 和 好友名字校验
         * 2. 好友名称不存在 返回该用户不存在
         * 3. 好友名称是自己 返回不可以添加自己为好友
         * 4. 还有已经是你的好友 返回该用户已经是你的好友了
         * 5. 添加好友
         **/
        if (result.hasErrors()) {
            return JSONResult.errorMsg(result.getFieldError().getDefaultMessage());
        }
        //查找添加还有前置条件没通过
        Integer status = userService.perConditionSearchFriends(findFriendReq);
        if (!status.equals(SearchFriendEnum.SUCCESS.getStatus())) {
            return JSONResult.errorMsg(SearchFriendEnum.getMsgBykey(status));
        }
        //添加好友
        userService.sendFriendRequest(findFriendReq);

        return JSONResult.ok();
    }

    /**
     * @param userBO
     * @return JSONResult
     * @author NovLi
     * @description 设置好友昵称
     * @date 2019/6/23
     **/
    @RequestMapping(value = "/set/nickname", method = {RequestMethod.POST})
    public JSONResult setNickName(@RequestBody UserBO userBO) {
        /*  1. 修改nickname
            4. 返回信息给前端    */
        if (StringUtils.isBlank(userBO.getUserId())) {
            return JSONResult.errorMsg("修改失败 缺少用户标识");
        }
        Users users = new Users();
        users.setId(userBO.getUserId());
        users.setNickname(userBO.getNickName());
        users = userService.update(users);
        return JSONResult.ok(users);
    }


    /**
     * @param userBO
     * @return JSONResult
     * @author NovLi
     * @description 加载好友请求列表
     * @date 2019/6/23
     **/
    @RequestMapping(value = "/queryFriendsReq", method = {RequestMethod.POST})
    public JSONResult queryFriendsReq(@RequestBody UserBO userBO) {
        //加载好友请求列表
        if (StringUtils.isBlank(userBO.getUserId())) {
            return JSONResult.errorMsg("查询失败哦 没收到用户标识");
        }
        List<FriendReqVo> usersVoList = userService.queryFriendsReq(userBO.getUserId());
        return JSONResult.ok(usersVoList);
    }

}
