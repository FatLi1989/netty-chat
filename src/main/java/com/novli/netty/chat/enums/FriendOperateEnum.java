package com.novli.netty.chat.enums;

import lombok.Getter;

/**
 * @author Liyanpeng
 * @date 2019/7/1 14:11
 **/
@Getter
public enum FriendOperateEnum {

    /**
     * 通过好友请求
     **/
    ignore("0", "通过请求"),
    /**
     * 忽略好友请求
     **/
    pass("1", "忽略请求");

    private String status;

    private String msg;

    FriendOperateEnum(String status, String msg) {
        this.status = status;
        this.msg = msg;
    }
}
