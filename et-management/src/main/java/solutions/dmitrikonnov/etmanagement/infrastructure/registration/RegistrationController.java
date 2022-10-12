package solutions.dmitrikonnov.etmanagement.infrastructure.registration;

import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping (path = "api/v2.0.0/registration")
@AllArgsConstructor
public class RegistrationController {

    Map<Integer,String> map = Collections.synchronizedMap(new LinkedHashMap<>(12,0.75f,true));

    RegistrationServiceImpl registrationServiceImpl;

    @PostMapping("/withRole")
    @PreAuthorize("hasAuthority('management:register')")
    public String registerWithRole(@Valid @RequestBody RegistrationRequestWithRole request){

        return registrationServiceImpl.registerWithRole(request);
    }

    @PostMapping("/self")
    public String registerSelf(@Valid @RequestBody RegistrationRequest request) throws InterruptedException, ExecutionException, TimeoutException {

        return registrationServiceImpl.registerSelf(request).get(6, TimeUnit.SECONDS);
    }
    @GetMapping (path = "confirm")
    public String confirm (@RequestParam ("token") String token) {
        return registrationServiceImpl.confirmToken(token);
    }
}
