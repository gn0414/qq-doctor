package com.qiqiao.basedata.service.impl;

import com.github.benmanes.caffeine.cache.Cache;
import com.qiqiao.basedata.service.BaseDataService;
import com.qiqiao.tools.basedata.BaseDataTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Simon
 */
@Service
public class BaseDataServiceImpl implements BaseDataService {
    private Cache<String,Object> localCache;

    @Override
    public Object getDataFromCaffeine(String key) {
        //TODO ok牢弟等下写到Mongo去
        return localCache.get(key,id->{
            System.out.println("没有走缓存");
           return BaseDataTools.initLevelData("");
        });
    }



    @Autowired
    public void setLocalCache(Cache<String, Object> localCache) {
        this.localCache = localCache;
    }
}
