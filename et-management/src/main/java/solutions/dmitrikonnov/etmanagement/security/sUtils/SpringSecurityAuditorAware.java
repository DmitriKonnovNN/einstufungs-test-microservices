package solutions.dmitrikonnov.etmanagement.security.sUtils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import solutions.dmitrikonnov.etmanagement.infrastructure.user.ETVerwaltungsUser;

import java.util.Optional;

@Service
@Slf4j
public class SpringSecurityAuditorAware implements AuditorAware<String>{

    public Optional<String> getCurrentAuditor() {

        return Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .filter(Authentication::isAuthenticated)
                .map(Authentication::getPrincipal)
                .map(ETVerwaltungsUser.class::cast)
                .map(ETVerwaltungsUser::getFullnameAndRoles);
    }

   /* private ETVerwaltungsUser systemUser;


    @Override
    public Optional<ETVerwaltungsUser> getCurrentAuditor() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ETVerwaltungsUser principal;
        if (authentication == null || !authentication.isAuthenticated()) {
            principal = systemUser;
        } else {
            principal = (ETVerwaltungsUser) authentication.getPrincipal();
        }
        log.info(String.format("Current auditor is >>> %s", principal));
        return Optional.of(principal);
    }*/

 /*   @Override
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        if (this.systemUser == null) {
            log.info("%s >>> loading system user");
            systemUser = this.repository.findOne(getCurrentAuditor());
        }
    }*/
}