package com.liang;

import com.liang.web.dao.AllowedIPDao;
import com.liang.web.entity.AllowedIpEntity;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @Description: TODO
 * @Author: LiangYang
 * @Date: 2022/5/25 下午3:06
 **/
@SpringBootTest
public class Test {

    @Autowired
    private AllowedIPDao allowedIPDao;

    @org.junit.jupiter.api.Test
    public void test(){
        List<AllowedIpEntity> allowedIpEntities = allowedIPDao.selectList(null);
        System.out.println(allowedIpEntities);
    }
}
