package com.qiqiao.basedata.controller;
import com.qiqiao.basedata.service.BaseDataService;
import com.qiqiao.model.Result;
import com.qiqiao.model.basedata.domain.FirstLevelData;
import com.qiqiao.model.basedata.domain.IndexLevelData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Simon
 */

@RestController
@RequestMapping("/basedata")
public class DiseaseController {
    private BaseDataService baseDataService;

    @Autowired
    public void setBaseDataService(BaseDataService baseDataService) {
        this.baseDataService = baseDataService;
    }

    @GetMapping("disease")
    public Result<FirstLevelData> getDisease(){
        return Result.success((FirstLevelData) baseDataService.getDataFromCaffeine("disease"));
    }

    @GetMapping("vaccine")
    public Result<FirstLevelData> getVaccine(){
        return Result.success((FirstLevelData) baseDataService.getDataFromCaffeine("vaccine"));
    }

    @GetMapping("inspect")
    public Result<IndexLevelData> getInspect(){
        return Result.success((IndexLevelData) baseDataService.getDataFromCaffeine("inspect"));
    }

    @GetMapping("treatment")
    public Result<IndexLevelData> getTreatment(){
        return Result.success((IndexLevelData) baseDataService.getDataFromCaffeine("treatment"));
    }
}
