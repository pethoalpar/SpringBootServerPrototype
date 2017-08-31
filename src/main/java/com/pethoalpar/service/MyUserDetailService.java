package com.pethoalpar.service;

import com.pethoalpar.entities.User;
import com.pethoalpar.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;

@Service
public class MyUserDetailService implements UserDetailsService {

    Logger logger = LoggerFactory.getLogger(MyUserDetailService.class);

    @Autowired
    private UserRepository userRepository;

    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

        User user = userRepository.findByUserName(userName);
        if(user == null){
            throw new UsernameNotFoundException("User not found");
        }

        GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().getRoleName());
        UserDetails userDetails = (UserDetails) new org.springframework.security.core.userdetails.User(user.getUserName(),user.getPassword(), Arrays.asList(authority));
        logger.info(user.getRole().getRoleName());
        return userDetails;
    }
}
