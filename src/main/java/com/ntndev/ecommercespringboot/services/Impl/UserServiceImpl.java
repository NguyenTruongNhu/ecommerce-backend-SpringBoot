package com.ntndev.ecommercespringboot.services.Impl;

import com.ntndev.ecommercespringboot.components.JwtTokenUtil;
import com.ntndev.ecommercespringboot.dtos.UserDTO;
import com.ntndev.ecommercespringboot.exceptions.DataNotFoundException;
import com.ntndev.ecommercespringboot.models.Role;
import com.ntndev.ecommercespringboot.models.User;
import com.ntndev.ecommercespringboot.repositories.RoleRepository;
import com.ntndev.ecommercespringboot.repositories.UserRepository;
import com.ntndev.ecommercespringboot.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;

    @Override
    public User createUser(UserDTO userDTO) throws DataNotFoundException {
        String phoneNumber = userDTO.getPhoneNumber();
        if (userRepository.existsByPhoneNumber(phoneNumber)) {
            throw new RuntimeException("User with phone number already exists");
        }
        User newUser = User.builder()
                .fullName(userDTO.getFullName())
                .phoneNumber(userDTO.getPhoneNumber())
                .password(userDTO.getPassword())
                .address(userDTO.getAddress())
                .dateOfBirth(userDTO.getDateOfBirth())
                .facebookAccountId(userDTO.getFacebookAccountId())
                .googleAccountId(userDTO.getGoogleAccountId())
                .build();

        Role role = roleRepository.findById(userDTO.getRoleId())
                .orElseThrow(() -> new DataNotFoundException("Role not found"));
        newUser.setRole(role);

        if (userDTO.getFacebookAccountId() == 0 && userDTO.getGoogleAccountId() == 0) {
            String password = userDTO.getPassword();
            String encodedPassword = passwordEncoder.encode(password);
            newUser.setPassword(encodedPassword);

        }

        return userRepository.save(newUser);
    }

    @Override
    public String login(String phoneNumber, String password) throws Exception {
        Optional<User> optUser = userRepository.findByPhoneNumber(phoneNumber);
        if(optUser.isEmpty()){
            throw new DataNotFoundException("Invalid phone number/ password");
        }
        User existingUser = optUser.get();
        //Check password
        if (existingUser.getFacebookAccountId() == 0 && existingUser.getGoogleAccountId() == 0) {
          if(!passwordEncoder.matches(password, existingUser.getPassword())) {
              throw new BadCredentialsException("Wrong phone number or password!");
          }
        }

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        phoneNumber, password, existingUser.getAuthorities());

        authenticationManager.authenticate(authenticationToken);

        return jwtTokenUtil.generateToken(existingUser);
    }
}
