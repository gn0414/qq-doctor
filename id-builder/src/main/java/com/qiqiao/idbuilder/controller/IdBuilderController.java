package com.qiqiao.idbuilder.controller;

import com.qiqiao.idbuilder.service.IdBuilderService;
import com.qiqiao.model.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Simon
 */
@RestController
@RequestMapping("/idFact")
public class IdBuilderController {


    IdBuilderService idBuilderService;

    /**
     * 雪花Id获取接口
     * @return Result
     */
    @GetMapping("/snowFlake")
    public Result<Long> getBuildId(){
        return Result.success(idBuilderService.getSnowFlakeId());
    }

    @Autowired
    public void setIdBuilderService(IdBuilderService idBuilderService) {
        this.idBuilderService = idBuilderService;
    }
}
