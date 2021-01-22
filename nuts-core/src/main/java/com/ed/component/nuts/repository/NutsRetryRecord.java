package com.ed.component.nuts.repository;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author : Edward
 * @date : 2021/1/19 下午5:45
 */
@Data
@Accessors(chain = true)
public class NutsRetryRecord {

    private Long id;

    /**
     * 业务类型
     */
    private String bizType;

    /**
     * 业务主键
     */
    private String bizNo;

    /**
     * 调用参数（json）
     */
    private String params;

    /**
     * 状态
     */
    private Integer retryStatus;

    /**
     * 重试次数
     */
    private Integer retryCount;

    /**
     * 下次重试时间
     */
    private Long nextRetryTime;

    private Date createTime;
    
    private Date updateTime;
}
