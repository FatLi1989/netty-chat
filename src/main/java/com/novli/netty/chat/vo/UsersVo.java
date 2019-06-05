package com.novli.netty.chat.vo;

import lombok.Data;
/**
 * @author Liyanpeng
 * @date 2019/6/5 16:22
 **/
@Data
public class UsersVo {

    private String id;

    private String username;

    private String password;

    private String faceImage;

    private String faceImageBig;

    private String nickname;

    private String qrcode;

}
