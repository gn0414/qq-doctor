package com.qiqiao.model.finals;

/**
 * @author Simon
 * 注意key的命名是
 * 服务+业务+行为+id(可不存在)
 */
public class RedisFinals {

    public static final String REDIS_DISEASE_KEY = "BASEDATA:DISEASE:GET";
    public static final String REDIS_INSPECT_KEY = "BASEDATA:INSPECT:GET";
    public static final String REDIS_TREATMENT_KEY = "BASEDATA:TREATMENT:GET";
    public static final String REDIS_VACCINE_KEY = "BASEDATA:VACCINE:GET";
    public static final int REDIS_BASEDATA_EXPIRE_TIME = 30*60;
}
