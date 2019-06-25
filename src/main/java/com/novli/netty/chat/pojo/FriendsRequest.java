package com.novli.netty.chat.pojo;

import lombok.Data;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Liyanpeng
 * @date 2019/6/25 16:55
 **/

@Data
public class FriendsRequest implements Serializable {
    /**
     * Column: friends_request.id
     */
    @Id
    private String id;

    /**
     * Column: friends_request.send_user_id
     */
    private String sendUserId;

    /**
     * Column: friends_request.accept_user_id
     */
    private String acceptUserId;

    /**
     * Column: friends_request.request_date_time
     */
    private Date requestDateTime;

    /**
     * Table: friends_request
     */
    private static final long serialVersionUID = 1L;
}
