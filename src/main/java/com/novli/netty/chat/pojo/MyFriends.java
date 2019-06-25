package com.novli.netty.chat.pojo;

import lombok.Data;

import javax.persistence.Id;
import java.io.Serializable;

@Data
public class MyFriends implements Serializable {

    @Id
    private String id;

    /**
     * Column: my_friends.my_user_id
    @mbggenerated 2019-05-18 22:23:52
     */
    private String myUserId;

    /**
     * Column: my_friends.my_friend_user_id
    @mbggenerated 2019-05-18 22:23:52
     */
    private String myFriendUserId;

    /**
     * Table: my_friends
    @mbggenerated 2019-05-18 22:23:52
     */
    private static final long serialVersionUID = 1L;
}
