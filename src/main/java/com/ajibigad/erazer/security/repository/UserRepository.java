package com.ajibigad.erazer.security.repository;

import com.ajibigad.erazer.security.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by ajibigad on 13/08/2017.
 */
public interface UserRepository extends CrudRepository<User, String> {

    public User findByUsername(String username);

    public User findByEmail(String email);
}
