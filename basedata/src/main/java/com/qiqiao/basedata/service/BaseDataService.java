package com.qiqiao.basedata.service;

/**
 * @author Simon
 */
public interface BaseDataService {
    /**
     * 从本地缓存获取数据
     * @param key jvm缓存的key
     * @return Object
     */
    Object getDataFromCaffeine(String key);
}
