package com.qiqiao.idbuilder.service;

/**
 * @author Simon
 */
public interface IdBuilderService {
    /**
     * fetch 用来获取雪花算法ID
     * @return Long
     */
    Long getSnowFlakeId();
}
