package com.qiqiao.basedata.mapper;

import com.qiqiao.model.basedata.domain.Inspect;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.awt.*;

/**
 * @author Simon
 */
public interface InspectRepository extends MongoRepository<Inspect,String> {
}
