package com.datapirate.springsecurity.auth;

import java.util.Optional;

/**
 * @author Akash on 21-Jun-20
 */
public interface ApplicationUserDao {
    Optional<ApplicationUser> selectApplicationUserByUsername(String username);
}
