package com.ed.component.nuts;

import lombok.Data;
import lombok.Getter;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : Edward
 * @date : 2021/1/19 下午7:51
 */
public class NutsContext implements ApplicationContextAware {

    private Map<String, NutsMeta> metas = new HashMap<>();

    private ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    public ApplicationContext getContext() {
        return context;
    }

    @Data
    public static class NutsMeta {

        private Class<?> type;

        private Method method;

        private List<NutsParameterMeta> parameterMetas;

        private NutsBizNoMeta bizNoMeta;

        private NutsRetryStrategy retryStrategy;

        private String executorName;
    }

    @Getter
    public static class NutsParameterMeta {

        private String name;

        private Type type;

        public NutsParameterMeta(String name, Type type) {
            this.name = name;
            this.type = type;
        }
    }

    @Getter
    public static class NutsBizNoMeta {

        private BizNoIdxType idxType;

        private Integer index;

        public NutsBizNoMeta(BizNoIdxType idxType, Integer index) {
            this.idxType = idxType;
            this.index = index;
        }
    }


    public enum BizNoIdxType {
        /**
         * 接口标注
         */
        BY_INTERFACE,
        /**
         * 注解标注
         */
        BY_ANNOTATION,
        ;
    }

    @Getter
    public static class NutsRetryStrategy {
        /**
         * 重试间隔(s)
         */
        private int interval;
        /**
         * 重试策略
         */
        private RetryStrategy strategy;
        /**
         * 最大重试次数
         */
        private int maxRetryCount;

        public NutsRetryStrategy(int interval, RetryStrategy strategy, int maxRetryCount) {
            this.interval = interval;
            this.strategy = strategy;
            this.maxRetryCount = maxRetryCount;
        }
    }


    public NutsMeta getNutsMeta(String bizType) {
        return metas.get(bizType);
    }

    public void registerNutsMeta(String bizType, NutsMeta nutsMeta) {
        metas.put(bizType, nutsMeta);
    }
}
