package com.qiqiao.model.finals;


/**
 * @author Simon
 * 注意key的命名是
 * 服务+业务+行为+id(可不存在)
 */
public class RedisFinals {

    public static final String REDIS_BASE_DATA_DISEASE_KEY = "BASEDATA:DISEASE:GET";
    public static final String REDIS_BASE_DATA_INSPECT_KEY = "BASEDATA:INSPECT:GET";
    public static final String REDIS_BASE_DATA_TREATMENT_KEY = "BASEDATA:TREATMENT:GET";
    public static final String REDIS_BASE_DATA_VACCINE_KEY = "BASEDATA:VACCINE:GET";

    public static final String REDIS_USER_CHECK_CODE_NUM = "USER:CHECK_CODE:NUM";

    public static final String REDIS_USER_CHECK_CODE_VALUE = "USER:CHECK_CODE:VALUE";
    public static final String REDISSON_USER_CHECK_CODE_LOCK = "USER:CHECK_CODE:LOCK";
    public static final int REDIS_BASEDATA_EXPIRE_TIME = 30*60;

    public static final int REDIS_USER_CHECK_CODE_EXPIRE = 5*60;

    public static final int REDIS_USER_CHECK_CODE_NUM_EXPIRE = 60*60*24;
}
