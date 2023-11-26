package com.qiqiao.idbuilder.service.impl;
import com.qiqiao.idbuilder.service.IdBuilderService;
import com.sankuai.inf.leaf.IDGen;
import com.sankuai.inf.leaf.snowflake.SnowflakeIDGenImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


/**
 * @author Simon
 * Leaf美团雪花算法分布式ID
 */
@Service
public class LeafIdBuilderServiceImpl implements IdBuilderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LeafIdBuilderServiceImpl.class);

    private List<IDGen> idGenList;



    @Override
    public Long getSnowFlakeId(){
        IDGen worker;
        Optional<IDGen> idGen = idGenList.stream()
                .findFirst()
                .filter(l -> {
                    return l instanceof SnowflakeIDGenImpl;
                });
        if (idGen.isPresent()){
            worker = idGen.get();
            return worker.get("id").getId();
        }else{
            LOGGER.error("Leaf雪花算法获取失败:位于LeafIdBuilderServiceImpl:28行");
            return -1L;
        }
    }

    //TODO 后续可能补充我们的号段模式用来作为另一种分布式ID生成方案

    @Autowired
    public void setIdGenList(List<IDGen> idGenList) {
        this.idGenList = idGenList;
    }
}
