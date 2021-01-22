package com.ed.component.nuts;

import com.alibaba.fastjson.JSONObject;
import com.ed.component.nuts.repository.INutsRepository;
import com.ed.component.nuts.repository.NutsRetryRecord;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

/**
 * @author : Edward
 * @date : 2021/1/19 下午7:47
 */
@Setter
@Slf4j
public class NutsHandler {

    private NutsContext context;

    private INutsRepository nutsRepository;

    public boolean handle(NutsRetryRecord po) {
        try {
            //获取nuts元数据
            NutsContext.NutsMeta nutsMeta = context.getNutsMeta(po.getBizType());
            Assert.notNull(nutsMeta, String.format("nuts:%s元数据不存在", po.getBizType()));
            //调用
            List<Object> list = new ArrayList<>();
            String params = po.getParams();
            JSONObject param = JSONObject.parseObject(params);
            List<NutsContext.NutsParameterMeta> parameterMetas = nutsMeta.getParameterMetas();
            for (NutsContext.NutsParameterMeta parameterMeta : parameterMetas) {
                String name = parameterMeta.getName();
                Assert.isTrue(param.containsKey(name), String.format("nuts:%s元数据参数不存在,bizNo=%s", po.getBizType(), po.getBizNo()));
                list.add(param.getObject(name, parameterMeta.getType()));
            }
            Method method = nutsMeta.getMethod();
            method.invoke(context.getContext().getBean(nutsMeta.getType()), list.toArray());
            return true;
        } catch (Exception e) {
            log.error("真实执行nuts方法失败,bizType={},bizNo={}", po.getBizType(), po.getBizNo(), e);
            return false;
        }
    }

    public void aSynHandle(NutsRetryRecord po) {
        //获取nuts元数据
        NutsContext.NutsMeta nutsMeta = context.getNutsMeta(po.getBizType());
        Assert.notNull(nutsMeta, String.format("nuts:%s元数据不存在", po.getBizType()));
        //异步执行
        ExecutorService executor = null;
        if (!StringUtils.isEmpty(nutsMeta.getExecutorName())) {
            executor = context.getContext().getBean(nutsMeta.getExecutorName(), ExecutorService.class);
        }
        if (executor == null) {
            CompletableFuture.supplyAsync(() -> handle(po)).whenComplete((ret, e) -> updateRetry(po, nutsMeta.getRetryStrategy(), ret, e));
        } else {
            CompletableFuture.supplyAsync(() -> handle(po), executor).whenComplete((ret, e) -> updateRetry(po, nutsMeta.getRetryStrategy(), ret, e));
        }
    }

    private void updateRetry(NutsRetryRecord po, NutsContext.NutsRetryStrategy strategy, Boolean ret, Throwable e) {
        int retryStatus;
        long nextRetryTime;
        if (e != null || !ret) {
            retryStatus = RetryStatus.FAIL.getStatus();
            if (po.getRetryCount() == strategy.getMaxRetryCount() - 1) {
                nextRetryTime = Long.MAX_VALUE;
            } else if (strategy.getStrategy() == RetryStrategy.FIXED) {
                nextRetryTime = System.currentTimeMillis() + strategy.getInterval() * 1000;
            } else {
                long l = System.currentTimeMillis();
                long interval = strategy.getInterval() * (po.getRetryCount() + 1) * 1000;
                nextRetryTime = Long.MAX_VALUE - l > interval ? l + interval : Long.MAX_VALUE;
            }
            nextRetryTime = Long.MAX_VALUE;
        } else {
            retryStatus = RetryStatus.SUCCESS.getStatus();
            nextRetryTime = Long.MAX_VALUE;
        }
        nutsRepository.updateStatus(po.getId(), nextRetryTime, retryStatus);
    }
}
