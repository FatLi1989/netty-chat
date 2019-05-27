package com.novli.netty.chat.vo;

import lombok.Data;

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
