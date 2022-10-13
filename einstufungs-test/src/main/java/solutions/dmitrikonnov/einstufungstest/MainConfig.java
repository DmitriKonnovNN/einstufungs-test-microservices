package solutions.dmitrikonnov.einstufungstest;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
@EnableAspectJAutoProxy
@EnableScheduling
@OpenAPIDefinition
@EnableJpaRepositories(basePackages = {
        "solutions.dmitrikonnov.etlimitsrepo",
        "solutions.dmitrikonnov.ettaskrepo",
        "solutions.dmitrikonnov.etresultsrepo"
})
@EnableTransactionManagement
@EnableConfigurationProperties
@Slf4j
@ComponentScan (basePackages = {"solutions.dmitrikonnov.etutils"})
@EntityScan(basePackages = {
        "solutions.dmitrikonnov.etentities",
        "solutions.dmitrikonnov.einstufungtest",
        "solutions.dmitrikonnov.etutils"
})
public class MainConfig {

    private final int avlblPrccrs = Runtime.getRuntime().availableProcessors();
/*  @Bean
    public CacheManagerCustomizer<ConcurrentMapCacheManager> cacheManagerCustomizer() {
        return new CacheManagerCustomizer<ConcurrentMapCacheManager>() {
            @Override
            public void customize(ConcurrentMapCacheManager cacheManager) {
                cacheManager.
            }
        };
    }*/


    @Bean (name = "taskExecutor")
    public Executor taskExecutor (){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(avlblPrccrs);
        executor.setMaxPoolSize(avlblPrccrs*3);
        executor.setKeepAliveSeconds(45);
        executor.setQueueCapacity(512);
        executor.setThreadNamePrefix("MyExecutor-");
        executor.initialize();
        log.debug("Notification AsyncExecutor's bean name = taskExecutor, poolSize = " + avlblPrccrs);
        return executor;
    }


    @Bean
    public OpenAPI springShopOpenAPI() {

        return new OpenAPI()
                .info(new Info().title("Einstungstest Application")
                        .description("Einstufungstest für ÖSD")
                        .version("v1.0.0")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")))
                .externalDocs(new ExternalDocumentation()
                        .description("Einstufungstest für ÖSD")
                        .url("/"));
    }

    //http://localhost:8080/swagger-ui/index.html

}
