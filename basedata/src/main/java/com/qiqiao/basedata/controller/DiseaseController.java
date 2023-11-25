package com.qiqiao.basedata.controller;
import com.qiqiao.basedata.service.BaseDataService;
import com.qiqiao.model.Result;
import com.qiqiao.model.basedata.domain.*;
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
    public Result<Disease> getDisease(){
        return Result.success((Disease) baseDataService.getDataFromCaffeine("disease"));
    }

    @GetMapping("vaccine")
    public Result<Vaccine> getVaccine(){
        return Result.success((Vaccine) baseDataService.getDataFromCaffeine("vaccine"));
    }

    @GetMapping("inspect")
    public Result<Inspect> getInspect(){
        return Result.success((Inspect) baseDataService.getDataFromCaffeine("inspect"));
    }

    @GetMapping("treatment")
    public Result<Treatment> getTreatment(){
        return Result.success((Treatment) baseDataService.getDataFromCaffeine("treatment"));
    }
}
