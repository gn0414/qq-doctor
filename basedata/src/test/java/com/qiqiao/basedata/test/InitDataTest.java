package com.qiqiao.basedata.test;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.qiqiao.basedata.mapper.DiseaseRepository;
import com.qiqiao.basedata.mapper.InspectRepository;
import com.qiqiao.basedata.mapper.TreatmentRepository;
import com.qiqiao.basedata.mapper.VaccineRepository;
import com.qiqiao.model.basedata.domain.*;
import com.qiqiao.tools.basedata.BaseDataTools;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;


@SpringBootTest
public class InitDataTest {

    @Autowired
    private DiseaseRepository diseaseRepository;

    @Autowired
    private InspectRepository inspectRepository;

    @Autowired
    private VaccineRepository vaccineRepository;

    @Autowired
    private TreatmentRepository treatmentRepository;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    /**
     * 测试Caffeine
     */
    @Test
    void testBasicOops() {
        //构建cache对象
        Cache<String, String> cache = Caffeine.newBuilder().build();
        //存入数据
        cache.put("simon", "赵熙蒙");
        //取数据
        String name = cache.get("simon", key -> {
            return "安子哥";
        });
        System.out.println(name);
    }
    /**
     * 测试redis
     *
     */
    @Test
    void testRedis(){
        redisTemplate.opsForValue().set("num","1");
        System.out.println(redisTemplate.opsForValue().get("num"));
    }

    /**
     * 只执行一次，将数据初始化到MongoDB
     */
//    @Test
//    void initBaseData(){
//        BaseFirstLevelData diseaseData = BaseDataTools.initLevelData("C:\\Users\\Simon\\Desktop\\qq-doctor-project\\data\\disease","disease");
//        BaseFirstLevelData vaccineData = BaseDataTools.initLevelData("C:\\Users\\Simon\\Desktop\\qq-doctor-project\\data\\vaccine","vaccine");
//        BaseIndexLevelData inspectData = BaseDataTools.initIndexData("C:\\Users\\Simon\\Desktop\\qq-doctor-project\\data\\inspect","inspect");
//        BaseIndexLevelData treatmentData = BaseDataTools.initIndexData("C:\\Users\\Simon\\Desktop\\qq-doctor-project\\data\\treatment","treatment");
//        diseaseRepository.save((Disease) diseaseData);
//        treatmentRepository.save((Treatment) treatmentData);
//        vaccineRepository.save((Vaccine) vaccineData);
//        inspectRepository.save((Inspect) inspectData);
//    }

//    @Test
//    void testMongo(){
//
//    }

}
