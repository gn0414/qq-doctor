package com.qiqiao.server.idbuilder;

import com.qiqiao.model.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Simon
 *分布式id远程调用模块
 */
@FeignClient(name = "id-builder" ,path = "idFact")
public interface IdBuilderFeignClient {

    /**
     * fetch 雪花算法id
     * @return Result
     */
    @GetMapping("/snowFlake")
    public Result<Long> getBuildId();
}
