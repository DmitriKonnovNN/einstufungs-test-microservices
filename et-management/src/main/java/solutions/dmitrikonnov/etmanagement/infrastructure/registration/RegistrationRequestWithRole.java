package solutions.dmitrikonnov.etmanagement.infrastructure.registration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import solutions.dmitrikonnov.etmanagement.security.sUtils.UserPermission;
import solutions.dmitrikonnov.etmanagement.security.sUtils.UserRole;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Getter
public class RegistrationRequestWithRole extends RegistrationRequest {

    @JsonProperty(value = "role")
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Role darf nicht leer sein")
    private final UserRole role;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public RegistrationRequestWithRole(String firstName,
                                       String lastName,
                                       String email,
                                       String password,
                                       @JsonProperty("role") String role) {
        super(firstName, lastName, email, password);

        this.role = UserRole.valueOf(role);
    }
}
