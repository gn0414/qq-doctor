package com.qiqiao.idbuilder;

import com.sankuai.inf.leaf.IDGen;
import com.sankuai.inf.leaf.snowflake.SnowflakeIDGenImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

@SpringBootTest
public class IdGetTest {



    @Autowired
    List<IDGen> idGenList;

    @Test
    public void getSnowFlake(){
        Optional<IDGen> idGen = idGenList.stream()
                .findFirst()
                .filter(l -> {
                    return l instanceof SnowflakeIDGenImpl;
                });
        IDGen worker;
        if (idGen.isPresent()) {
            worker = idGen.get();
            System.out.println(worker.get("id").getId());
        }else{
            System.out.println("获取雪花ID失败");
        }
    }
}
