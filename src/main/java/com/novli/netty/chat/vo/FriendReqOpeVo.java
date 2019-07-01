package com.novli.netty.chat.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 好友请求处理
 *
 * @author Liyanpeng
 * @date 2019/7/1 13:57
 **/
@Data
public class FriendReqOpeVo {

    @NotBlank
    private String myUserId;

    @NotBlank
    private String myFriendUserId;

    @NotBlank
    private String operateType;

}
