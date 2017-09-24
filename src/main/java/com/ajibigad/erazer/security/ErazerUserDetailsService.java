package com.ajibigad.erazer.security;

import com.ajibigad.erazer.security.model.Role;
import com.ajibigad.erazer.security.repository.UserRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by ajibigad on 13/08/2017.
 */
@Service
public class ErazerUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.ajibigad.erazer.security.model.User user = userRepository.findByUsername(username);
        if(user == null){
            throw new UsernameNotFoundException(username + " not found");
        }
        return User.withUsername(username)
                .password(user.getPassword())
                .authorities(getAuthorities(user.getRoles())).build();
    }

    private List<GrantedAuthority> getAuthorities(Set<Role> roles){
        Set<GrantedAuthority> authorities = new HashSet<>();
        for (Role role :
                roles) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }

        List<GrantedAuthority> result = new ArrayList<GrantedAuthority>(authorities);
        return result;
    }


}
