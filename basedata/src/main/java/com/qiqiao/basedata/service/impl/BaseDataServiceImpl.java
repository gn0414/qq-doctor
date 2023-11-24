package com.qiqiao.basedata.service.impl;

import com.github.benmanes.caffeine.cache.Cache;
import com.qiqiao.basedata.service.BaseDataService;
import com.qiqiao.model.basedata.finals.BaseDataFinal;
import com.qiqiao.tools.basedata.BaseDataTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @author Simon
 */
@Service
public class BaseDataServiceImpl implements BaseDataService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseDataServiceImpl.class);
    private Cache<String,Object> localCache;

    @Override
    public Object getDataFromCaffeine(String key) {
        Object data = null;
        switch (key) {
            case BaseDataFinal.DISEASE_DATA_KEY:
            case BaseDataFinal.VACCINE_DATA_KEY:
                data = localCache.get(key,id->{
                    LOGGER.info(key + ": task caffeine cache");
                    return BaseDataTools.initLevelData("C:\\Users\\Simon\\Desktop\\qq-doctor-project\\data\\"+key);
                });
                break;

            case BaseDataFinal.INSPECT_DATA_KEY:
            case BaseDataFinal.TREATMENT_DATA_KEY:
                data = localCache.get(key,id->{
                    LOGGER.info(key + ": task caffeine cache");
                    return BaseDataTools.initIndexData("C:\\Users\\Simon\\Desktop\\qq-doctor-project\\data\\"+key);
                });
                break;
            default:
                break;
        }
        return data;
    }



    @Autowired
    public void setLocalCache(Cache<String, Object> localCache) {
        this.localCache = localCache;
    }
}
