package com.qiqiao.basedata.controller;
import com.qiqiao.basedata.service.BaseDataService;
import com.qiqiao.model.Result;
import com.qiqiao.model.basedata.domain.Disease;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Simon
 */

@RestController
@RequestMapping("/disease")
public class DiseaseController {
    private BaseDataService baseDataService;

    @Autowired
    public void setBaseDataService(BaseDataService baseDataService) {
        this.baseDataService = baseDataService;
    }

    @GetMapping
    public Result<Disease> getDisease(){
        return Result.success((Disease) baseDataService.getDataFromCaffeine("disease"));
    }
}
