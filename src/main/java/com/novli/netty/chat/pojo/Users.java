package com.novli.netty.chat.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class Users implements Serializable {

    private String id;

    private String username;

    private String password;

    private String faceImage;

    private String faceImageBig;

    private String nickname;

    private String qrcode;

    private String cid;

    private static final long serialVersionUID = 1L;
}