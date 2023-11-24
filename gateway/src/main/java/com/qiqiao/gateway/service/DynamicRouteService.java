package com.qiqiao.gateway.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * @author Simon
 */
@Service
@Slf4j
public class DynamicRouteService implements ApplicationEventPublisherAware {
    /**
     * 路由数据的写入
     * */
    private RouteDefinitionWriter routeDefinitionWriter;
    /**
     * 事件发布器
     * */
    private ApplicationEventPublisher publisher;
//
    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }
    /**
     * @param routeDefinition
     * 追加新的路由配置
     * */
    public boolean add(RouteDefinition routeDefinition){
        //日志输出
        log.info("增加路由配置项,新的路由ID为：{}",routeDefinition.getId());
        try {
            //配置写入
            this.routeDefinitionWriter.save(Mono.just(routeDefinition)).subscribe();
            //发布路由事件
            this.publisher.publishEvent(new RefreshRoutesEvent(this));
        }catch (Exception e){
            log.error("路由增加失败,失败的ID为：{}",routeDefinition.getId());
            return false;
        }
        return true;
    }

    public Mono<ResponseEntity<Object>> delete(String id){//根据id删除
        //日志输出
        log.info("删除路由的配置项,删除的路由ID为：{}",id);
        return this.routeDefinitionWriter.delete(Mono.just(id)).then(Mono.defer(()->{
            return Mono.just(ResponseEntity.ok().build());
        })).onErrorResume((t->{
            return t instanceof NotFoundException;
        }),(r)->{
            return Mono.just(ResponseEntity.notFound().build());
        });
    }
    public boolean update(RouteDefinition routeDefinition){
        //日志输出
        log.info("更新路由配置项,新的路由ID为：{}",routeDefinition.getId());
        try {
            //根据id先删除已有的路由
            this.delete(routeDefinition.getId());
            //配置写入
            this.routeDefinitionWriter.save(Mono.just(routeDefinition)).subscribe();
            //发布路由事件
            this.publisher.publishEvent(new RefreshRoutesEvent(this));
        }catch (Exception e){
            log.error("路由更新失败,失败的ID为：{}",routeDefinition.getId());
            return false;
        }
        return true;
    }
}
