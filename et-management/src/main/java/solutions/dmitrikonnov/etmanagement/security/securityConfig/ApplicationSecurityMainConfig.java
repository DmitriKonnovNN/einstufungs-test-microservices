package solutions.dmitrikonnov.etmanagement.security.securityConfig;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import solutions.dmitrikonnov.etmanagement.security.sUtils.SpringSecurityAuditorAware;
import solutions.dmitrikonnov.etmanagement.security.securityConfig.securityConfigUtils.WebSecurityAdapterQualifierResolver;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity (prePostEnabled = true)
@EnableJpaAuditing (auditorAwareRef="auditorProvider")
public class ApplicationSecurityMainConfig {

    Logger log = LoggerFactory.getLogger(this.getClass());
    private final String SSC_CONF_MSG = "Current Spring Security Configuration : %s";

    protected final WebSecurityAdapterQualifierResolver resolver;
    protected final  WebSecurityConfigurerAdapter adapter;



    /**
     * creates an instance of {@link WebSecurityConfigurerAdapter} by fetching the concerned bean name of its implementations
     * with JWT-TOKEN {@link solutions.dmitrikonnov.etmanagement.security.securityConfig.securityConfigImplementation.WebSecurityWithJwtConfig}
     * or with LOGIN-FORM {@link solutions.dmitrikonnov.etmanagement.security.securityConfig.securityConfigImplementation.WebSecurityWithLoginConfig }
     * from the properties whose path is configured in {@link solutions.dmitrikonnov.etmanagement.security.securityConfig.ApplicationSecurityMainConfig }.
     *
     * To start with a specific implementation interchange it in the properties with respect to bean's name to go for given as parameter in @Component.
     *
     * */

    public ApplicationSecurityMainConfig(@Autowired WebSecurityAdapterQualifierResolver resolver,
                                         @Value("${app.security.webSecurityAdapterQualifier}") String qualifier) {
        this.resolver = resolver;
        this.adapter = resolver.resolve(qualifier);
        log.warn(String.format(SSC_CONF_MSG,qualifier));
    }

    @Bean
    AuditorAware<String> auditorProvider() {
        return new SpringSecurityAuditorAware();
    }

}
