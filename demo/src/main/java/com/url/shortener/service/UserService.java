package com.url.shortener.service;

import com.url.shortener.dtos.LoginRequest;
import com.url.shortener.model.User;
import com.url.shortener.repository.UserRepository;
import com.url.shortener.security.jwt.JwtAuthenticationResponse;
import com.url.shortener.security.jwt.JwtUtils;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class UserService {
    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;
    private AuthenticationManager authenticationManager;
    private JwtUtils jwtUtils;

    public User registerUser(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }


    public JwtAuthenticationResponse authenticateUser(LoginRequest loginRequest){
        Authentication authen = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authen);
        UserDetailsImpl userDetails = (UserDetailsImpl) authen.getPrincipal();
        String jwt = jwtUtils.generateJwtToken(userDetails);
        return new JwtAuthenticationResponse(jwt);

    }

    public User findbyUserName(String name){
        return userRepository.findByUserName(name).orElseThrow(()-> new UsernameNotFoundException("User not found with userName" + name));
    }

}
