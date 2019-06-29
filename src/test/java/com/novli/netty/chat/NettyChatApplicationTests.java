package com.novli.netty.chat;

import com.novli.netty.chat.service.UserService;
import com.novli.netty.chat.vo.FriendReqVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class NettyChatApplicationTests {


    @Autowired
    private UserService userService;

    @Test
    public void contextLoads() {
        List<FriendReqVo> friendReqVoList = userService.queryFriendsReq("190625CK3RH87Y14");
        log.info(friendReqVoList.toString());
    }

}
