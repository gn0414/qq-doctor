package com.qiqiao.model.basedata.domain;

import lombok.Data;

import java.util.List;

/**
 * @author Simon
 */
@Data
public abstract class BaseFirstLevelData {

    protected List<SecondLevelData> secondLevelDataList;
}
