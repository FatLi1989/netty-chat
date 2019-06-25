package com.novli.netty.chat.bo;

import com.novli.netty.chat.util.constant.ResultInfoCons;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author Liyanpeng
 * @date 2019/6/25 17:38
 **/
@Data
public class FindFriendReq implements Serializable {

    @NotBlank(message = ResultInfoCons.USER_ID_IS_BLANK)
    private String userId;

    @NotBlank(message = ResultInfoCons.USER_NAME_IS_BLANK)
    private String userName;


}
