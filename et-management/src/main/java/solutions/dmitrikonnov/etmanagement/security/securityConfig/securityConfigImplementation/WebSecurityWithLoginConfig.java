package solutions.dmitrikonnov.etmanagement.security.securityConfig.securityConfigImplementation;


import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component("webSecurityWithLoginConfig")
@AllArgsConstructor
@Order(103)
@Lazy
public class WebSecurityWithLoginConfig extends WebSecurityConfigurerAdapter {

    private static final String[]  RESOURCES = new String[]{
        "/", "/home","/pictureCheckCode","/include/**",
                "/css/**","/icons/**","/images/**","/js/**","/layer/**"};
    private final DaoAuthenticationProvider daoAuthenticationProvider;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
       auth.authenticationProvider(daoAuthenticationProvider);
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .csrf().disable()
/*                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .and()*/
                .authorizeRequests()
                    .antMatchers(RESOURCES).permitAll()
                    //.antMatchers("api/v*/et_ufzgi/**").permitAll() // Only for test purposes
                    .antMatchers("api/v*/users/**").hasAnyRole("ROOT","SUPERADMIN")
                    .antMatchers("/actuator/**","swagger-ui/**", "/swagger-resources/**","/swagger-ui.html**","/swagger-ui/","/swagger-ui*/**").hasAnyRole("ROOT","SUPERADMIN")
                    .antMatchers("api/v*/constructor/**").hasAnyRole("ROOT", "SUPERADMIN")
                    .antMatchers("api/v*/et_ufzgi/**").hasAnyRole("ROOT", "SUPERADMIN","ADMIN")
                    .antMatchers("/api/v*/registration/**").permitAll()
                .anyRequest()
                .authenticated().and()
                .formLogin()
                    .loginPage("/login").permitAll()
                    .defaultSuccessUrl("/management")
                    .passwordParameter("password")
                    .usernameParameter("username")
                .and()
                .rememberMe().tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(21)).key("somethingverysecured")
                    .rememberMeParameter("remember-me")
                .and()
                .logout()
                    .logoutUrl("/logout")
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET")) // this line should be deleted if CSRF is ENABLED
                    .clearAuthentication(true)
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID", "remember-me")
                    .logoutSuccessUrl("/login");

    }
}
