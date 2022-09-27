package solutions.dmitrikonnov.etmanagement.verwaltung.registration;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping (path = "api/v2.0.0/registration")
@AllArgsConstructor
public class RegistrationController {

    Map<Integer,String> map = Collections.synchronizedMap(new LinkedHashMap<>(12,0.75f,true));

    RegistrationService registrationService;

    @PostMapping
    public String register(@Valid @RequestBody RegistrationRequest registrationRequest){

        return registrationService.register(registrationRequest);
    }
    @GetMapping (path = "confirm")
    public String confirm (@RequestParam ("token") String token) {
        return registrationService.confirmToken(token);
    }
}
