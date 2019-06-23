package com.novli.netty.chat.enums;

import lombok.Getter;

@Getter
public enum SearchFriendEnum {

    SUCCESS(0, "ok"),

    USER_NOT_EXIST(1, "无此用户..."),

    NOT_YOURSELF(2, "不能添加你自己..."),
    
    ALREADY_BE_FRIENDS(3, "该用户已经是你的好友...");

    private Integer status;

    private String msg;

    SearchFriendEnum(Integer status, String msg) {
        this.status = status;
        this.msg = msg;
    }


    public static String getMsgBykey(Integer status) {

        for (SearchFriendEnum enums : SearchFriendEnum.values()) {
            if (enums.status == status) {
                return enums.msg;
            }
        }
        return null;
    }
}
