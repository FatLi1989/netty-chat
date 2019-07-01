package com.novli.netty.chat.enums;

import lombok.Getter;
/**
 * @author Liyanpeng
 * @date 2019/7/1 14:12
 **/
@Getter
public enum SearchFriendEnum {
    /**
     * 查询成功成功
     **/
    SUCCESS(0, "ok"),
    /**
     * 没有此用户
     **/
    USER_NOT_EXIST(1, "无此用户..."),
    /**
     * 不可以添加自己为好友
     **/
    NOT_YOURSELF(2, "不能添加你自己..."),
    /**
     * 该用户已经是你的好友
     **/
    ALREADY_BE_FRIENDS(3, "该用户已经是你的好友...");

    private Integer status;

    private String msg;

    SearchFriendEnum(Integer status, String msg) {
        this.status = status;
        this.msg = msg;
    }


    public static String getMsgBykey(Integer status) {

        for (SearchFriendEnum enums : SearchFriendEnum.values()) {
            if (enums.status.equals(status)) {
                return enums.msg;
            }
        }
        return null;
    }
}
