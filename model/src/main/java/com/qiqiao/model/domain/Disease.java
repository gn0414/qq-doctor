package com.qiqiao.model.domain;

import java.util.List;

/**
 * @author Simon
 * 疾病功能对象
 */

public class Disease {
        private static final long serialVersionUID = -3258839839160856613L;
        private static Disease disease;
        private List<DiseaseDepartment> diseaseDepartments;

        static {
                //TODO 做个单例对象,初始化去查库然后返回，后面获取返回同一对象即可
                disease = new Disease();
        }

        /**将单例对象的构造方法隐藏*/
        private Disease(){

        }
        /**单例对象获取*/
        public static Disease getDisease() {
                return disease;
        }
}
