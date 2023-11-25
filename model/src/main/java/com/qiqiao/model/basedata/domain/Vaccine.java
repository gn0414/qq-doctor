package com.qiqiao.model.basedata.domain;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;

/**
 * @author Simon
 */
@Data
@Document(collection = "vaccine")
@Builder
@NoArgsConstructor
public class Vaccine extends BaseFirstLevelData implements Serializable{

    private static final long serialVersionUID = -3258839839160856613L;

}
