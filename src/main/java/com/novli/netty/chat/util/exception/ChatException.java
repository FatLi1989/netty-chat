package com.novli.netty.chat.util.exception;

import lombok.Data;

/** 异常类
 * @author Liyanpeng
 * @date 2019/6/5 15:14
 **/
@Data
public class ChatException extends RuntimeException {

    private Integer code;

    public ChatException(String message, Integer code) {
        super(message);
        this.code = code;
    }
}
