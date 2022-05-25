package com.liang;

import com.liang.web.dao.IpDao;
import com.liang.web.entity.IpEntity;
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
    private IpDao ipDao;

    @org.junit.jupiter.api.Test
    public void test(){
        List<IpEntity> allowedIpEntities = ipDao.selectList(null);
        System.out.println(allowedIpEntities);
    }
}
