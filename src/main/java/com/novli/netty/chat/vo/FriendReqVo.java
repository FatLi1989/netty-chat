package com.novli.netty.chat.vo;

import lombok.Data;

/**
 * @author Liyanpeng
 * @date 2019/6/29 10:53
 **/
@Data
public class FriendReqVo {
    private String sendUserId;
    private String sendUsername;
    private String sendFaceImage;
    private String sendNickName;
}
