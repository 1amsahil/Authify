package com.eternity.authify.controller;

import com.eternity.authify.io.AuthRequest;
import com.eternity.authify.io.AuthResponse;
import com.eternity.authify.service.AppUserDetailsService;
import com.eternity.authify.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@RestController
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AppUserDetailsService appUserDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request)
    {
       try {
           authenticate(request.getEmail(),request.getPassword());
           UserDetails userDetails = appUserDetailsService.loadUserByUsername(request.getEmail());
           String jwtToken = jwtUtil.generateToken(userDetails);

           ResponseCookie cookie = ResponseCookie.from("jwt",jwtToken)
                   .httpOnly(true)
                   .path("/")
                   .maxAge(Duration.ofDays(1))
                   .sameSite("Strict")
                   .build();

           return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
                   .body(new AuthResponse(request.getEmail(), jwtToken));
       }

       catch (BadCredentialsException e){
           Map<String, Object> error = new HashMap<>();
           error.put("Error",true);
           error.put("message","Email or Password incorrect");
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
       }
       catch (DisabledException e){
           Map<String, Object> error = new HashMap<>();
           error.put("Error",true);
           error.put("message","Account is Disabled");
           return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
       }
       catch (Exception e){
           Map<String, Object> error = new HashMap<>();
           error.put("Error",true);
           error.put("message","Authentication Failed");
           return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
       }
    }

    private void authenticate(String email, String password)
    {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(email,password));
    }
}
