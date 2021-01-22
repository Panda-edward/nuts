package com.ed.component.nuts.config;


import com.ed.component.nuts.NutsAop;
import com.ed.component.nuts.NutsContext;
import com.ed.component.nuts.NutsHandler;
import com.ed.component.nuts.repository.INutsRepository;
import com.ed.component.nuts.repository.MybatisNutsRepository;
import com.ed.component.nuts.repository.mapper.NutsMapper;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author : Edward
 * @date : 2021/1/20 上午10:53
 */
@Configuration
@MapperScan(basePackages = "com.ed.component.nuts.repository.mapper")
@Slf4j
public class NutsAutoConfiguration {

    @Bean
    public NutsContext nutsContext() {
        return new NutsContext();
    }

    /**
     * 提供NutsRepository默认实现
     * 条件:容器内不存在INutsRepository实现Bean && 容器内存在NutsMapper Bean
     */
    @Bean
    @ConditionalOnMissingBean(INutsRepository.class)
    @ConditionalOnBean(NutsMapper.class)
    public INutsRepository nutsRepository(NutsMapper nutsMapper) {
        log.info("【nuts】容器中无INutsRepository实现bean,注入默认实现");
        MybatisNutsRepository repository = new MybatisNutsRepository();
        repository.setNutsMapper(nutsMapper);
        return repository;
    }

    @Bean
    public NutsHandler nutsHandler(NutsContext context, INutsRepository nutsRepository) {
        NutsHandler handler = new NutsHandler();
        handler.setContext(context);
        handler.setNutsRepository(nutsRepository);
        return handler;
    }

    @Bean
    public NutsAop nutsAop(NutsHandler handler, INutsRepository nutsRepository, NutsContext context) {
        NutsAop nutsAop = new NutsAop();
        nutsAop.setHandler(handler);
        nutsAop.setNutsRepository(nutsRepository);
        nutsAop.setContext(context);
        return nutsAop;
    }
}
