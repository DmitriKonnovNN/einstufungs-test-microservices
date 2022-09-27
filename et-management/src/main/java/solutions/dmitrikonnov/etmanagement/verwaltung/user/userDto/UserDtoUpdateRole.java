package solutions.dmitrikonnov.etmanagement.verwaltung.user.userDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import solutions.dmitrikonnov.einstufungstest.security.sUtils.UserRole;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;


@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class UserDtoUpdateRole implements Serializable {

    private static final long serialVersionUID = -1798070786993154676L;

    @JsonProperty (value = "id")
    private Long id;

    @JsonProperty (value = "role")
    @Enumerated(EnumType.STRING)
    private UserRole role;

    /**
     * During refactoring be aware: Replacement of String by Roles will probably lead to SerializationException
     * combined with ResultSetMapping*/

    public UserDtoUpdateRole(Long id, String role) {
        this.id = id;
        this.role = UserRole.valueOf(role);
    }
}
