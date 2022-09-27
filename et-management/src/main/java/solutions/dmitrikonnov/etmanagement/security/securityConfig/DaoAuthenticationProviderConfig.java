package solutions.dmitrikonnov.etmanagement.security.securityConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import solutions.dmitrikonnov.etmanagement.security.securityConfig.userDetailsService.UDSQualifierResolver;

@Configuration
public class DaoAuthenticationProviderConfig  {

    PasswordEncoder bCryptPasswordEncoder;
    UDSQualifierResolver resolver;
    UserDetailsService userDetailsService;
    Logger log = LoggerFactory.getLogger(this.getClass());
    private final String DAO_PROV_CONF_MSG = "Current Dao Authentication Provider Configuration : %s";


    public DaoAuthenticationProviderConfig(@Autowired PasswordEncoder bCryptPasswordEncoder,
                                           @Value ("${app.security.daoAuthenticationProvider}") String qualifier,
                                           @Autowired UDSQualifierResolver resolver) {
        this.resolver = resolver;
        this.userDetailsService = resolver.resolve(qualifier);
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        log.warn(String.format(DAO_PROV_CONF_MSG, qualifier));

    }
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider (){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(bCryptPasswordEncoder);
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

}
