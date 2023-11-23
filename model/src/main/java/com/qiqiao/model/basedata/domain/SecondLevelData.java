package com.qiqiao.model.basedata.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author Simon
 */
@Data
public class SecondLevelData implements Serializable {
    private static final long serialVersionUID = -3258839839160856613L;
    private String name;
    private Map<String, Map<String, List<String>>> secondLevel;
}
