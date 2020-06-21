package com.datapirate.springsecurity.security;

import com.google.common.collect.Sets;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static com.datapirate.springsecurity.security.ApplicationUserPermission.*;

/**
 * @author Akash on 16-Jun-20
 */
public enum ApplicationUserRole {
    ADMIN(Sets.newHashSet()),
    ADMINTRAINEE(Sets.newHashSet(COURSE_READ, STUDENT_READ)),
    STUDENT(Sets.newHashSet(COURSE_READ, COURSE_WRITE, STUDENT_READ, STUDENT_WRITE));

    private final Set<ApplicationUserPermission> permissions;

    ApplicationUserRole(Set<ApplicationUserPermission> permissions) {
        this.permissions = permissions;
    }

    public Set<ApplicationUserPermission> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getGrantedAuthorities() {
        // Set<SimpleGrantedAuthority> permissions = getPermissions().stream()
        // OR
        var permissions = getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
        permissions.add(new SimpleGrantedAuthority("ROLE_"+this.name()));
        return permissions;
    }
}
