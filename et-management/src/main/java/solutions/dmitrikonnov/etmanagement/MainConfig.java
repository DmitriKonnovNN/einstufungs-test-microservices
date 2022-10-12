package solutions.dmitrikonnov.etmanagement;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableAsync
@EnableAspectJAutoProxy
@EnableScheduling
@EnableConfigurationProperties
@EnableJpaRepositories(basePackages = {
        "solutions.dmitrikonnov.etmanagement",
        "solutions.dmitrikonnov.etlimitsrepo",
        "solutions.dmitrikonnov.ettaskrepo"})
@ComponentScan(basePackages = {
        "solutions.dmitrikonnov.etlimitsrepo",
        "solutions.dmitrikonnov.ettaskrepo"})
@EntityScan(basePackages = {
        "solutions.dmitrikonnov.etentities",
        "solutions.dmitrikonnov.etmanagement",
        "solutions.dmitrikonnov.etutils",
        "solutions.dmitrikonnov.ettaskrepo",
        "solutions.dmitrikonnov.etlimitsrepo"
})
public class MainConfig {
}
