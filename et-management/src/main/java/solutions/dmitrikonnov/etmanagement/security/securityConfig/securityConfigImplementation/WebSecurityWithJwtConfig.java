package solutions.dmitrikonnov.etmanagement.security.securityConfig.securityConfigImplementation;


import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.stereotype.Component;
import solutions.dmitrikonnov.etmanagement.security.sUtils.JWTutils.JwtRequestFilter;
import solutions.dmitrikonnov.etmanagement.security.sUtils.JWTutils.JwtUsernameAndPasswordAuthenticationFilter;

@Component ("webSecurityWithJwtConfig")
@AllArgsConstructor (onConstructor_ ={@Autowired})
@Order(102)
@Lazy
public class WebSecurityWithJwtConfig extends WebSecurityConfigurerAdapter {

    private static final String[]  RESOURCES = new String[]{
            "/", "/home","/pictureCheckCode","/include/**",
            "/css/**","/icons/**","/images/**","/js/**","/layer/**"};

    @Value("${app.jwt.token.secretKey}") private final String secretKey;
    private final DaoAuthenticationProvider daoAuthenticationProvider;



    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .csrf().disable()
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(new JwtUsernameAndPasswordAuthenticationFilter(authenticationManager(), secretKey))
                .addFilterAfter(new JwtRequestFilter(secretKey), JwtUsernameAndPasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers(RESOURCES).permitAll()
                .antMatchers("api/v*/users/**").hasAnyRole("ROOT","SUPERADMIN")
                .antMatchers("/actuator/**").hasAnyRole("ROOT","SUPERADMIN")
                .antMatchers("api/v*/constructors/**").hasAnyRole("ROOT", "SUPERADMIN")
                .antMatchers("api/v*/et_ufzgi/**").hasAnyRole("ROOT", "SUPERADMIN","ADMIN")
                .antMatchers("/api/v*/registration/**").permitAll()
                .anyRequest()
                .authenticated();

    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider);
    }


}








