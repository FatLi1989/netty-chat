package com.novli.netty.chat.vo;

import com.novli.netty.chat.util.constant.ResultInfoCons;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class FriendIdForm {

    @NotBlank(message = ResultInfoCons.USER_ID_IS_BLANK)
    private String userId;
}
