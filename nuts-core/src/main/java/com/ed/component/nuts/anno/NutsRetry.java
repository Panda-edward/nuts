package com.ed.component.nuts.anno;

import com.ed.component.nuts.RetryStrategy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author : Edward
 * @date : 2021/1/20 下午8:29
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NutsRetry {

    /**
     * 重试间隔(s)
     */
    int interval();

    /**
     * 重试策略
     */
    RetryStrategy strategy();

    /**
     * 最大重试次数
     */
    int maxRetryCount() default Integer.MAX_VALUE;
}
