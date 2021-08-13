package com.seanlindev.springframework.bootstrap;

import com.seanlindev.springframework.model.entities.AuthorityEntity;
import com.seanlindev.springframework.model.entities.RoleEntity;
import com.seanlindev.springframework.repositories.AuthorityRepository;
import com.seanlindev.springframework.repositories.RoleRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collection;

@Component
public class UserRoleBootStrap {

    AuthorityRepository authorityRepository;
    RoleRepository roleRepository;

    public UserRoleBootStrap(AuthorityRepository authorityRepository,
                             RoleRepository roleRepository) {
        this.authorityRepository = authorityRepository;
        this.roleRepository = roleRepository;
    }

    @EventListener
    @Transactional
    public void onApplicationReadyEvent(ApplicationReadyEvent event) {
        System.out.println("load authorities...");
        AuthorityEntity readAuthority = createAuthority("READ_AUTHORITY");
        AuthorityEntity writeAuthority = createAuthority("WRITE_AUTHORITY");
        AuthorityEntity deleteAuthority = createAuthority("DELETE_AUTHORITY");

        createRole("ROLE_USER", Arrays.asList(readAuthority, writeAuthority));
        createRole("ROLE_ADMIN", Arrays.asList(readAuthority, writeAuthority, deleteAuthority));
    }

    private AuthorityEntity createAuthority(String name) {
        AuthorityEntity authority = authorityRepository.findByName(name);
        if (authority == null) {
            authority = new AuthorityEntity(name);
            authorityRepository.save(authority);
        }
        return authority;
    }

    private RoleEntity createRole(String name, Collection<AuthorityEntity> authorities) {
        RoleEntity role = roleRepository.findByName(name);
        if (role == null) {
            role = new RoleEntity(name);
            role.setAuthorities(authorities);
            roleRepository.save(role);
        }
        return role;
    }
}
