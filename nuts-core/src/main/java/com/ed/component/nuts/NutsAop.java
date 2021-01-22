package com.ed.component.nuts;

import com.alibaba.fastjson.JSONObject;
import com.ed.component.nuts.anno.Nuts;
import com.ed.component.nuts.anno.NutsBizNo;
import com.ed.component.nuts.anno.NutsRetry;
import com.ed.component.nuts.repository.INutsRepository;
import com.ed.component.nuts.repository.NutsRetryRecord;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.Assert;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author : Edward
 * @date : 2021/1/19 下午5:39
 */
@Aspect
@Setter
@Slf4j
public class NutsAop {

    private INutsRepository nutsRepository;

    private NutsHandler handler;

    private NutsContext context;

    @Around(value = "@annotation(com.ed.component.nuts.anno.Nuts)")
    public void txRecord(ProceedingJoinPoint joinPoint) throws Throwable {
        //如果当前线程不在事务中,不执行拦截逻辑
        if (!TransactionSynchronizationManager.isSynchronizationActive()) {
            joinPoint.proceed(joinPoint.getArgs());
            return;
        }
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        Object[] args = joinPoint.getArgs();
        Nuts nuts = method.getAnnotation(Nuts.class);
        //init && get
        NutsContext.NutsMeta meta = initAndGetNutsMeta(joinPoint.getTarget().getClass(), method, nuts);
        //build & save
        String bizNo = getBizNo(args, meta.getBizNoMeta());
        JSONObject params = new JSONObject();
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < args.length; i++) {
            params.put(parameters[i].getName(), args[i]);
        }
        NutsRetryRecord po = buildBizArgsLog(nuts.name(), params, bizNo);
        nutsRepository.save(po);
        //注册事务触发事件
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                handler.aSynHandle(po);
            }
        });
    }

    private String getBizNo(Object[] args, NutsContext.NutsBizNoMeta bizNoMeta) {
        String bizNo;
        if (bizNoMeta.getIdxType() == NutsContext.BizNoIdxType.BY_ANNOTATION) {
            bizNo = String.valueOf(args[bizNoMeta.getIndex()]);
        } else {
            bizNo = ((NutsVO) args[bizNoMeta.getIndex()]).getBizNo();
        }
        return bizNo;
    }

    private NutsContext.NutsMeta initAndGetNutsMeta(Class<?> type, Method method, Nuts nuts) {
        NutsContext.NutsMeta meta = context.getNutsMeta(nuts.name());
        if (meta == null) {
            meta = new NutsContext.NutsMeta();
            meta.setType(type);
            meta.setMethod(method);
            Parameter[] parameters = method.getParameters();
            //parameterMeta
            List<NutsContext.NutsParameterMeta> parameterMetas = new ArrayList<>();
            for (Parameter parameter : parameters) {
                NutsContext.NutsParameterMeta parameterMeta = new NutsContext.NutsParameterMeta(parameter.getName(), parameter.getType());
                parameterMetas.add(parameterMeta);
            }
            meta.setParameterMetas(parameterMetas);
            //bizNoMeta
            NutsContext.NutsBizNoMeta bizNoMeta = null;
            for (int i = 0; i < parameters.length; i++) {
                if (parameters[i].isAnnotationPresent(NutsBizNo.class)) {
                    bizNoMeta = new NutsContext.NutsBizNoMeta(NutsContext.BizNoIdxType.BY_ANNOTATION, i);
                    break;
                }
                if (NutsVO.class.isAssignableFrom(parameters[i].getType())) {
                    bizNoMeta = new NutsContext.NutsBizNoMeta(NutsContext.BizNoIdxType.BY_INTERFACE, i);
                    break;
                }
            }
            Assert.notNull(bizNoMeta, String.format("nuts:%s元数据不bizNo定义", nuts.name()));
            meta.setBizNoMeta(bizNoMeta);
            //retry strategy
            NutsRetry nutsRetry = nuts.nutsRetry();
            NutsContext.NutsRetryStrategy strategy = new NutsContext.NutsRetryStrategy(nutsRetry.interval(), nutsRetry.strategy(), nutsRetry.maxRetryCount());
            meta.setRetryStrategy(strategy);
            context.registerNutsMeta(nuts.name(), meta);
        }
        return meta;
    }

    private NutsRetryRecord buildBizArgsLog(String bizType, JSONObject params, String bizNo) {
        Date now = new Date();
        return new NutsRetryRecord()
                .setBizType(bizType)
                .setBizNo(bizNo)
                .setParams(params.toJSONString())
                .setRetryStatus(RetryStatus.INIT.getStatus())
                .setRetryCount(0)
                .setNextRetryTime(now.getTime())
                .setCreateTime(now)
                .setUpdateTime(now);
    }
}
