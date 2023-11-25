package com.qiqiao.basedata.mapper;

import com.qiqiao.model.basedata.domain.Disease;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author Simon
 */
public interface DiseaseRepository extends MongoRepository<Disease,String> {
}
