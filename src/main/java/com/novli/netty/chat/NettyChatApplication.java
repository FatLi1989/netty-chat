package com.novli.netty.chat;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

@Slf4j
@ComponentScan(basePackages = {"com.novli.netty.chat", "org.n3r.idworker"})
@MapperScan(basePackages = "com.novli.netty.chat.mapper")
@SpringBootApplication
public class NettyChatApplication {

    public static void main(String[] args) {
        log.info("主程序启动服务--------------");
        SpringApplication.run(NettyChatApplication.class, args);
    }

}
