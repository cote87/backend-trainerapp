package com.reffocase.backend.trainerapp.backend_trainerapp.auth.services;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.reffocase.backend.trainerapp.backend_trainerapp.auth.models.entities.Permission;
import com.reffocase.backend.trainerapp.backend_trainerapp.auth.repositories.UserRepository;

@Service
public class JpaUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<com.reffocase.backend.trainerapp.backend_trainerapp.auth.models.entities.User> o = repository.findByUsername(username);

        if (!o.isPresent()) {
            throw new UsernameNotFoundException("Username no existe en el sistema");
        }

        com.reffocase.backend.trainerapp.backend_trainerapp.auth.models.entities.User user = o.orElseThrow();
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole().getName()));
        Set<Permission> permissions = user.getRole().getPermissions();

        permissions.forEach(permission -> authorities.add(new SimpleGrantedAuthority(permission.getName())));

        return new User(
                user.getUsername(),
                user.getPassword(),
                true,
                true,
                true,
                true,
                authorities);
    }

}
