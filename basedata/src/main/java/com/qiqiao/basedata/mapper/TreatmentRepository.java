package com.qiqiao.basedata.mapper;


import com.qiqiao.model.basedata.domain.Treatment;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author Simon
 */
public interface TreatmentRepository extends MongoRepository<Treatment,String> {
}
