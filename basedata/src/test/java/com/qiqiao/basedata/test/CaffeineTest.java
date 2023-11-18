package com.qiqiao.basedata.test;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import com.qiqiao.model.basedata.domain.Disease;
import org.junit.jupiter.api.Test;

import static com.qiqiao.tools.basedata.BaseDataTools.getFirstLetter;
import static com.qiqiao.tools.basedata.BaseDataTools.initDisease;

public class CaffeineTest {

    /*
     * caffeine使用基础测试
     * success
     * */
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

    /*
     * 读取疾病数据集测试并转化为json
     * */
    @Test
    void getDataFromTxtToJson() throws JsonProcessingException {


    }
    /*
     * 测试词语获取首字母大写方法
     * */
    @Test
    void getLetterTest() {
        //测试非汉字型开头
        System.out.println(getFirstLetter("2型糖尿病"));
        //测试汉字型开头
        System.out.println(getFirstLetter("感冒"));
    }

    /*
     * 汉字首字母获取方法
     * */




}
