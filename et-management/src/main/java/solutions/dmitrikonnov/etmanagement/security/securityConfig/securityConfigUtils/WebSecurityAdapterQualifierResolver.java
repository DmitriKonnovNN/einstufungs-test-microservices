package solutions.dmitrikonnov.etmanagement.security.securityConfig.securityConfigUtils;

import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@AllArgsConstructor
public class WebSecurityAdapterQualifierResolver {

    private final ApplicationContext context;


    public WebSecurityConfigurerAdapter resolve(String qualifier)
    {
        return (WebSecurityConfigurerAdapter) context.getBean(qualifier);
    }
}
