package com.qiqiao.gateway.controller;

import com.qiqiao.gateway.service.DynamicRouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 * @author Simon
 */
@RestController
@RequestMapping("/routes/*") //访问父路径
public class DynamicRouteController {
    /**
     * 路由业务对像
     * */
    private DynamicRouteService dynamicRouteService;

    @GetMapping("add")
    public Boolean add(@RequestBody RouteDefinition routeDefinition){
        return this.dynamicRouteService.add(routeDefinition);
    }


    @DeleteMapping("delete/{id}")
    public Mono<ResponseEntity<Object>> delete(@PathVariable String id){
        return this.dynamicRouteService.delete(id);
    }

    @PostMapping("update")
    public Boolean update(@RequestBody RouteDefinition routeDefinition){
        return this.dynamicRouteService.update(routeDefinition);
    }

    @Autowired
    public void setDynamicRouteService(DynamicRouteService dynamicRouteService) {
        this.dynamicRouteService = dynamicRouteService;
    }
}
