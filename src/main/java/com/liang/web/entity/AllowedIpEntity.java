package com.liang.web.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

/**
 * @Description: TODO
 * @Author: LiangYang
 * @Date: 2022/5/25 下午2:58
 **/
@Data
@ToString
@TableName("allowed_ip")
public class AllowedIpEntity {
    private String ip;
    private String address;
    private String createTime;
}
