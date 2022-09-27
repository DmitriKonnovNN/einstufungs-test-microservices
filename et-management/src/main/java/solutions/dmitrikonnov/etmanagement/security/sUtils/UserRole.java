package solutions.dmitrikonnov.etmanagement.security.sUtils;


import com.google.common.collect.Sets;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static solutions.dmitrikonnov.etmanagement.security.sUtils.UserPermission.*;

public enum UserRole {

    USER(Sets.newHashSet(AUFGABE_READ,AUFGABE_WRITE,ITEM_READ,ITEM_WRITE,SCHWELLE_READ)),
    ADMIN(Sets.newHashSet(AUFGABE_ALL,ITEM_ALL,SCHWELLE_ALL,VERWALTUNG_READ)),
    SUPERADMIN (Sets.newHashSet(AUFGABE_ALL,ITEM_ALL,SCHWELLE_ALL,VERWALTUNG_ALL)),
    ROOT(Sets.newHashSet(AUFGABE_ALL,ITEM_ALL,SCHWELLE_ALL,VERWALTUNG_ALL));


    private final Set <UserPermission> permissions;

    UserRole(Set<UserPermission> permissions) {
        this.permissions = permissions;
    }

    public Set<UserPermission> getPermissions() {
        return permissions;
    }

    public Set<? extends GrantedAuthority> getAuthorities () {
        Set<SimpleGrantedAuthority> permissions = getPermissions().stream()
                .map(userPermission -> new SimpleGrantedAuthority(userPermission.getPermission()))
                .collect(Collectors.toSet());
        permissions.add(new SimpleGrantedAuthority("ROLE_"+this.name()));
        return permissions;
    }

}
