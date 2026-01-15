package com.soumya.main.config;

import com.soumya.main.entity.User;
import com.soumya.main.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    public UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository){
        this.userRepository=userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUser =userRepository.findByEmail(username);
        if (optionalUser!=null && optionalUser.isPresent()){
            User tryToLoginUser = optionalUser.get();
            return new CustomUserDetails(tryToLoginUser);
        }else {
            throw  new UsernameNotFoundException("Failed to SignIn");
        }
    }
}

