package com.novli.netty.chat.pojo;

import com.novli.netty.chat.util.constant.ResultInfoCons;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class Users implements Serializable {

    @Id
    private String id;

    @NotBlank(message = ResultInfoCons.USER_NAME_IS_BLANK)
    private String username;

    @NotBlank(message = ResultInfoCons.USER_PASSWORD_IS_BLANK)
    private String password;

    private String faceImage;

    private String faceImageBig;

    private String nickname;

    private String qrcode;

    private String cid;

    private static final long serialVersionUID = 1L;
}
