package solutions.dmitrikonnov.etmanagement.infrastructure.user;


import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import solutions.dmitrikonnov.etmanagement.infrastructure.user.userDto.UserDtoGetDetails;
import solutions.dmitrikonnov.etmanagement.security.sUtils.UserRole;
import solutions.dmitrikonnov.etutils.Obfuscator;


import javax.persistence.*;
import java.util.Collection;

@Entity
@SqlResultSetMapping(name = "UserDetails",
        classes = @ConstructorResult(targetClass = UserDtoGetDetails.class,
                columns = { @ColumnResult(name = "id", type = Long.class),
                        @ColumnResult(name ="role", type = String.class),
                        @ColumnResult(name ="first_name", type = String.class),
                        @ColumnResult(name ="last_name", type = String.class),
                        @ColumnResult(name ="email", type = String.class),
                        @ColumnResult(name ="active", type = Boolean.class),
                        @ColumnResult(name ="locked", type = Boolean.class),
                        @ColumnResult(name ="enabled", type = Boolean.class )} ) )
@NamedNativeQueries({@NamedNativeQuery(name = "getUserDetails",
        query = "select id, role, first_name, last_name, email, active, locked, enabled from user_entity where id = :id",
        resultSetMapping = "UserDetails" )})
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@ToString
//@Table (name = "\"USER\"")
public class ETManagementUser implements UserDetails {

    @Id
    @Column (name = "id", unique = true, nullable = false, updatable = false)
    @SequenceGenerator(name = "et_management_seq",
            sequenceName = "et_management_seq",
            allocationSize = 1)
    @GeneratedValue (strategy = GenerationType.SEQUENCE,
            generator = "et_management_seq")
    private Long id;

    private String firstName;
    private String lastName;
    @ToString.Exclude
    private String password;
    private String email;
    private Boolean active;
    private Boolean locked = false;
    private Boolean enabled = false;
    @Enumerated (EnumType.STRING)
    private UserRole role;
    private String reasonForLock;

    public ETManagementUser(String firstName,
                            String lastName,
                            String password,
                            String email,
                            UserRole role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    @ToString.Include
    private String email(){
        return Obfuscator.obfuscate(email);
    }

    private boolean isActive() {
        return active;
    }


    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        return role.getAuthorities();
    }

    @Override
    public String getPassword() {
        return password;
    }

    public String getFullnameAndRoles(){return firstName +" "+ lastName + " " + role.getAuthorities();}


    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }


}
