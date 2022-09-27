package solutions.dmitrikonnov.etmanagement.verwaltung.user.userDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;


@NoArgsConstructor
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
public class UserDtoGetDetails  extends UserDtoUpdateRole {


    @JsonProperty
    private String firstName;
    @JsonProperty
    private String lastName;
    @JsonProperty
    private String email;
    @JsonProperty
    private Boolean active;
    @JsonProperty
    private Boolean locked;
    @JsonProperty
    private Boolean enabled;

/**
 * During refactoring be aware: Replacement of constructor below by @AllArgsConstructor can lead to errors
 * combined with @ResultSetMapping in UserEntity.
 * */
  public UserDtoGetDetails(Long id,
                           String role,
                           String firstName,
                           String lastName,
                           String email,
                           Boolean active,
                           Boolean locked,
                           Boolean enabled) {
        super(id, role);
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.active = active;
        this.locked = locked;
        this.enabled = enabled;
    }
}
