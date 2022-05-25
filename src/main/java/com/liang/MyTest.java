package com.liang;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Description: TODO
 * @Author: LiangYang
 * @Date: 2022/5/25 上午10:04
 **/
@SpringBootApplication
@MapperScan("com.liang.web.dao")
public class MyTest {
    public static void main(String[] args) {
        SpringApplication.run(MyTest.class, args);
    }



}
