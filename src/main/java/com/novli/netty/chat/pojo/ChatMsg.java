package com.novli.netty.chat.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
public class ChatMsg implements Serializable {
    /**
     *   ����
     * Column: chat_msg.id
    @mbggenerated 2019-05-18 22:23:52
     */
    private String id;

    /**
     *   �����û�id
     * Column: chat_msg.send_user_id
    @mbggenerated 2019-05-18 22:23:52
     */
    private String sendUserId;

    /**
     *   ������id
     * Column: chat_msg.accept_user_id
    @mbggenerated 2019-05-18 22:23:52
     */
    private String acceptUserId;

    /**
     *   ��Ϣ����
     * Column: chat_msg.msg
    @mbggenerated 2019-05-18 22:23:52
     */
    private String msg;

    /**
     *   ��Ϣ״̬
     * Column: chat_msg.sign_flag
    @mbggenerated 2019-05-18 22:23:52
     */
    private Integer signFlag;

    /**
     *   ����ʱ��
     * Column: chat_msg.create_time
    @mbggenerated 2019-05-18 22:23:52
     */
    private Date createTime;

    /**
     * Table: chat_msg
    @mbggenerated 2019-05-18 22:23:52
     */
    private static final long serialVersionUID = 1L;
}