package com.qiqiao.model.basedata.domain;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author Simon
 */
@Data
@Document( collection = "treatment")
@Builder
@NoArgsConstructor
public class Treatment  extends BaseIndexLevelData implements Serializable {

    private static final long serialVersionUID = -3258839839160856613L;
}
