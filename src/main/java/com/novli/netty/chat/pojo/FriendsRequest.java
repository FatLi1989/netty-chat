package com.novli.netty.chat.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class FriendsRequest implements Serializable {
    /**
     * Column: friends_request.id
    @mbggenerated 2019-05-18 22:23:52
     */
    private String id;

    /**
     *   ������������û�id
     * Column: friends_request.send_user_id
    @mbggenerated 2019-05-18 22:23:52
     */
    private String sendUserId;

    /**
     *   ������������û�Id
     * Column: friends_request.accept_user_id
    @mbggenerated 2019-05-18 22:23:52
     */
    private String acceptUserId;

    /**
     *   ����ʱ��
     * Column: friends_request.request_date_time
    @mbggenerated 2019-05-18 22:23:52
     */
    private Date requestDateTime;

    /**
     * Table: friends_request
    @mbggenerated 2019-05-18 22:23:52
     */
    private static final long serialVersionUID = 1L;
}