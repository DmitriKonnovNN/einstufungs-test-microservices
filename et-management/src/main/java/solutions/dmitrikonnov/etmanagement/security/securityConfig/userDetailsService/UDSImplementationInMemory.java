package solutions.dmitrikonnov.etmanagement.security.securityConfig.userDetailsService;


import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Component;
import solutions.dmitrikonnov.etmanagement.security.sUtils.UserRole;
import solutions.dmitrikonnov.etmanagement.security.securityConfig.PasswordEncoder;

@Component("inMemory")
@AllArgsConstructor
@Lazy
public class UDSImplementationInMemory implements UDSResolverInterface {


    PasswordEncoder bCryptPasswordEncoder;

    @Override
    @Bean  (name = "userDetailsServiceInMemory")
    public UserDetailsService getUserDetailsService() {
        UserDetails user = User.builder()
                .username("dima")
                .password(bCryptPasswordEncoder.bCryptPasswordEncoder().encode("123"))
                .authorities(UserRole.USER.getAuthorities())
                .build();

        UserDetails user1 = User.builder()
                .username("denis")
                .password(bCryptPasswordEncoder.bCryptPasswordEncoder().encode("123"))
                .authorities(UserRole.ADMIN.getAuthorities())
                .build();
        UserDetails user2 = User.builder()
                .username("kirill")
                .password(bCryptPasswordEncoder.bCryptPasswordEncoder().encode("123"))
                .authorities(UserRole.SUPERADMIN.getAuthorities())
                .build();

        return new InMemoryUserDetailsManager(user,user1, user2);
    }
}
