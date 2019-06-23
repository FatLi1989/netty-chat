package com.novli.netty.chat.bo;

import com.novli.netty.chat.util.constant.ResultInfoCons;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class FindFriendReq {

    @NotBlank(message = ResultInfoCons.USER_ID_IS_BLANK)
    private String userId;

    @NotBlank(message = ResultInfoCons.USER_NAME_IS_BLANK)
    private String userName;


}
