package com.qiqiao.model.basedata.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Simon
 * 疾病功能对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Disease {
        private static final long serialVersionUID = -3258839839160856613L;
        private List<DiseaseDepartment> departments;
}
