package solutions.dmitrikonnov.etmanagement.infrastructure.registration;

import org.springframework.scheduling.annotation.AsyncResult;


import java.util.concurrent.Future;

public interface RegistrationService {

    String registerWithRole(RegistrationRequestWithRole request);

    default Future<String> registerSelf(RegistrationRequest request){
        return new AsyncResult<>(registerWithRole(
                new RegistrationRequestWithRole(request.getFirstName(),
                        request.getLastName(),
                        request.getPassword(),
                        request.getEmail(),
                        "USER"))) ;
    }
}
