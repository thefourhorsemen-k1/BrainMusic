package com.horsemen.bmserver.controller;

import com.horsemen.bmserver.message.request.LoginForm;
import com.horsemen.bmserver.message.request.SignUpForm;
import com.horsemen.bmserver.message.response.JwtResponse;
import com.horsemen.bmserver.message.response.ResponseMessage;
import com.horsemen.bmserver.model.Role;
import com.horsemen.bmserver.model.RoleName;
import com.horsemen.bmserver.model.User;
import com.horsemen.bmserver.repository.RoleRepository;
import com.horsemen.bmserver.repository.UserRepository;
import com.horsemen.bmserver.security.jwt.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@CrossOrigin(origins = "*")
@RestController
public class AuthRestAPIs {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtProvider jwtProvider;

    @PostMapping("/signin")
    public ResponseEntity <?> authenticateUser(@Valid @RequestBody LoginForm loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.generateJwtToken(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getUsername(), userDetails.getAuthorities()));
    }

    @PostMapping("/signup")
    public ResponseEntity <?> registerUser(@Valid @RequestBody SignUpForm signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return new ResponseEntity <>(new ResponseMessage("Username is already taken!"), HttpStatus.BAD_REQUEST);
        }
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity <>(new ResponseMessage("Email is already in use!"), HttpStatus.BAD_REQUEST);
        }

        User user = new User(signUpRequest.getFullName(), signUpRequest.getUsername(), signUpRequest.getEmail(),
                passwordEncoder.encode(signUpRequest.getPassword()));

        Set <String> strRoles = signUpRequest.getRole();
        Set <Role> roles = new HashSet <>();

        strRoles.forEach(role -> {
            switch (role) {
                case "admin":
                    Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN)
                            .orElseThrow(() -> new RuntimeException("Fail -> Cause: User Role not find."));
                    roles.add(adminRole);
                    break;
                default:
                    Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                            .orElseThrow(() -> new RuntimeException("Fail -> Cause: User Role not find."));
                    roles.add(userRole);
            }
        });
        user.setRoles(roles);
        userRepository.save(user);

        return new ResponseEntity <>(new ResponseMessage("User registered successfully!"), HttpStatus.OK);
    }
}
