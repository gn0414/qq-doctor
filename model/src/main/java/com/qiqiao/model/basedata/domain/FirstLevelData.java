package com.qiqiao.model.basedata.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author Simon
 */

@Data
public class FirstLevelData implements Serializable {
    private static final long serialVersionUID = -3258839839160856613L;
    private List<SecondLevelData> secondLevelDataList;
}
