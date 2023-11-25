package com.qiqiao.basedata.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Cache;
import com.qiqiao.basedata.mapper.DiseaseRepository;
import com.qiqiao.basedata.mapper.InspectRepository;
import com.qiqiao.basedata.mapper.TreatmentRepository;
import com.qiqiao.basedata.mapper.VaccineRepository;
import com.qiqiao.basedata.service.BaseDataService;
import com.qiqiao.model.basedata.domain.Disease;
import com.qiqiao.model.basedata.domain.Inspect;
import com.qiqiao.model.basedata.domain.Treatment;
import com.qiqiao.model.basedata.domain.Vaccine;
import com.qiqiao.model.basedata.finals.BaseDataFinal;
import com.qiqiao.model.finals.RedisFinals;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;


/**
 * @author Simon
 */
@Service
public class BaseDataServiceImpl implements BaseDataService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private DiseaseRepository diseaseRepository;
    private InspectRepository inspectRepository;
    private VaccineRepository vaccineRepository;
    private TreatmentRepository treatmentRepository;

    private RedisTemplate<String,String> redisTemplate;



    private static final Logger LOGGER = LoggerFactory.getLogger(BaseDataServiceImpl.class);
    private Cache<String,Object> localCache;

    /**
     * 该方法是查询mongo之后写入redis
     * 我们的nginx在访问这个接口时会先去查询redis
     * 如果redis查询失败我们就会走到我们caffeine缓存
     * 我们的单台caffeine缓存我并未设置过期时间所以
     * 如果调用了接口那么就说明redis已经过期了
     * @param key jvm缓存的key
     * @return Object
     */
    @Override
    public Object getDataFromCaffeine(String key) {
        Object data = null;
        switch (key) {
            case BaseDataFinal.DISEASE_DATA_KEY:
            case BaseDataFinal.VACCINE_DATA_KEY:
                data = localCache.get(key,id->{
                    //caffeine无值走mongo
                    LOGGER.info(key + ": task caffeine cache and select mongo");
                    //查询mongo并写入redis
                    if (BaseDataFinal.DISEASE_DATA_KEY.equals(key)){
                        //查mongo
                        Disease disease = diseaseRepository.findAll().get(0);
                        //写转化对象
                        redisCache(RedisFinals.REDIS_DISEASE_KEY,disease);
                        return disease;
                    }else{
                        Vaccine vaccine = vaccineRepository.findAll().get(0);
                        redisCache(RedisFinals.REDIS_VACCINE_KEY,vaccine);
                        return vaccine;
                    }
                });
                //redis无值走caffeine
                if (BaseDataFinal.DISEASE_DATA_KEY.equals(key)){
                    redisCache(RedisFinals.REDIS_DISEASE_KEY,data);
                }else{
                    redisCache(RedisFinals.REDIS_VACCINE_KEY,data);
                }
                break;

            case BaseDataFinal.INSPECT_DATA_KEY:
            case BaseDataFinal.TREATMENT_DATA_KEY:
                data = localCache.get(key,id->{
                    LOGGER.info(key + ": task caffeine cache and select mongo");
                    //查询mongo并写入redis
                    if (BaseDataFinal.INSPECT_DATA_KEY.equals(key)){
                        //查mongo
                        Inspect inspect = inspectRepository.findAll().get(0);
                        //写转化对象
                        redisCache(RedisFinals.REDIS_INSPECT_KEY,inspect);
                        return inspect;
                    }
                    Treatment treatment = treatmentRepository.findAll().get(0);
                    redisCache(RedisFinals.REDIS_TREATMENT_KEY,treatment);
                    return treatment;
                });
                //redis无值走caffeine
                if (BaseDataFinal.INSPECT_DATA_KEY.equals(key)){
                    redisCache(RedisFinals.REDIS_INSPECT_KEY,data);
                }else{
                    redisCache(RedisFinals.REDIS_TREATMENT_KEY,data);
                }
                break;
            default:
                break;
        }
        return data;
    }


    private void redisCache(String key,Object value){
        try {
            redisTemplate.opsForValue().set(key,objectMapper.writeValueAsString(value),
                    RedisFinals.REDIS_BASEDATA_EXPIRE_TIME,TimeUnit.SECONDS);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }




    @Autowired
    public void setLocalCache(Cache<String, Object> localCache) {
        this.localCache = localCache;
    }

    @Autowired
    public void setDiseaseRepository(DiseaseRepository diseaseRepository) {
        this.diseaseRepository = diseaseRepository;
    }

    @Autowired
    public void setInspectRepository(InspectRepository inspectRepository) {
        this.inspectRepository = inspectRepository;
    }

    @Autowired
    public void setTreatmentRepository(TreatmentRepository treatmentRepository) {
        this.treatmentRepository = treatmentRepository;
    }
    @Autowired
    public void setVaccineRepository(VaccineRepository vaccineRepository) {
        this.vaccineRepository = vaccineRepository;
    }

    @Autowired
    public void setRedisTemplate(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
}
