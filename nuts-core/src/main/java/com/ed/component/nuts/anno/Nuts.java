package com.ed.component.nuts.anno;

import com.ed.component.nuts.RetryStrategy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author : Edward
 * @date : 2021/1/19 下午5:35
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Nuts {

    String name();

    String executorName() default "";

    NutsRetry nutsRetry() default @NutsRetry(interval = 30, strategy = RetryStrategy.INCREASE, maxRetryCount = 100);
}
