package com.eternity.authify.service;

import com.eternity.authify.entity.UserEntity;
import com.eternity.authify.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class AppUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException
    {
        UserEntity existingUser = userRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Email not found : "+ email));

        return new User(existingUser.getEmail(), existingUser.getPassword(), new ArrayList<>());
    }

}
