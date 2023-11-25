package com.qiqiao.basedata.mapper;


import com.qiqiao.model.basedata.domain.Vaccine;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author Simon
 */
public interface VaccineRepository extends MongoRepository<Vaccine,String> {
}
