package com.qiqiao.model.basedata.domain;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author Simon
 */
@Data
public abstract class BaseIndexLevelData {
    protected Map<String, List<String>> data;
}
