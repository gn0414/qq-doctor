package com.qiqiao.model.basedata.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @author Simon
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiseaseDepartment {
    private static final long serialVersionUID = -3258839839160856613L;
    private String departName;
    private Map<String,Map<String,List<String>>> innerDepart;

}
