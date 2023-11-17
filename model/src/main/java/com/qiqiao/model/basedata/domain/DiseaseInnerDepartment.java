package com.qiqiao.model.basedata.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author Simon
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiseaseInnerDepartment {
    private static final long serialVersionUID = -3258839839160856613L;

    private String innerDepartName;

    private Map<String,DiseaseDepartmentIndex> indexDiseases;
}
