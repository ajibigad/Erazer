package com.ajibigad.erazer.security.repository;

import com.ajibigad.erazer.security.model.Role;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by ajibigad on 13/08/2017.
 */
public interface RoleRepository extends CrudRepository<Role, Long> {

    public Role findByName(String roleName);
}
