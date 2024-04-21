package com.icode.twitterclonebackend.controllers;

import com.icode.twitterclonebackend.exception.DisabledUserException;
import com.icode.twitterclonebackend.exception.InvalidUserCredentialsException;
import com.icode.twitterclonebackend.models.Response;
import com.icode.twitterclonebackend.services.UserAuthService;
import com.icode.twitterclonebackend.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(value = "http://localhost:4200")
public class UserSignupController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserAuthService userAuthService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/signin")
    public ResponseEntity<Response> generateJwtToken(@RequestBody com.icode.twitterclonebackend.models.User userData,
                                                     Principal principal) {
        Authentication authentication = null;
        try {
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(userData.getUsername(),
                            userData.getPassword()));
        } catch (DisabledException e) {
            throw new DisabledUserException("User Inactive");
        } catch (BadCredentialsException e) {
            throw new InvalidUserCredentialsException("Invalid Credentials");
        }

        User user = (User) authentication.getPrincipal();
        Set<String> roles = user.getAuthorities().stream().map(r -> r.getAuthority()).collect(Collectors.toSet());

        String token = jwtUtil.generateToken(authentication);

        Response response = new Response();
        response.setToken(token);
        response.setRoles(roles.stream().collect(Collectors.toList()));
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody com.icode.twitterclonebackend.models.User userData) {
        String isRegistered = userAuthService.saveUser(userData);

        return new ResponseEntity<String>(isRegistered, HttpStatus.CREATED);
    }
}
