package com.ntndev.ecommercespringboot.services.Impl;

import com.ntndev.ecommercespringboot.components.JwtTokenUtils;
import com.ntndev.ecommercespringboot.components.LocalizationUtils;
import com.ntndev.ecommercespringboot.dtos.UserDTO;
import com.ntndev.ecommercespringboot.exceptions.DataNotFoundException;
import com.ntndev.ecommercespringboot.exceptions.PermissionDenyException;
import com.ntndev.ecommercespringboot.models.Role;
import com.ntndev.ecommercespringboot.models.User;
import com.ntndev.ecommercespringboot.repositories.RoleRepository;
import com.ntndev.ecommercespringboot.repositories.UserRepository;
import com.ntndev.ecommercespringboot.services.UserService;
import com.ntndev.ecommercespringboot.utils.MessageKeys;
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
    private final JwtTokenUtils jwtTokenUtil;
    private final AuthenticationManager authenticationManager;
    private final LocalizationUtils localizationUtils;

    // Phương thức tạo User mới
    @Override
    public User createUser(UserDTO userDTO) throws Exception {
        String phoneNumber = userDTO.getPhoneNumber();
        // Kiểm tra nếu số điện thoại đã tồn tại
        if (userRepository.existsByPhoneNumber(phoneNumber)) {
            throw new RuntimeException("User with phone number already exists");
        }

        // Lấy thông tin Role từ RoleRepository
        Role role = roleRepository.findById(userDTO.getRoleId())
                .orElseThrow(() -> new DataNotFoundException("Role not found"));
        // Nếu Role là ADMIN thì không cho phép tạo User
        if (role.getName().toUpperCase().equals(Role.ADMIN)) {
            throw new PermissionDenyException("You cannot create an admin user!");
        }

        // Tạo đối tượng User mới
        User newUser = User.builder()
                .fullName(userDTO.getFullName())
                .phoneNumber(userDTO.getPhoneNumber())
                .password(userDTO.getPassword())
                .address(userDTO.getAddress())
                .dateOfBirth(userDTO.getDateOfBirth())
                .facebookAccountId(userDTO.getFacebookAccountId())
                .googleAccountId(userDTO.getGoogleAccountId())
                .build();

        newUser.setRole(role);

        // Mã hóa mật khẩu nếu không có tài khoản Facebook hoặc Google
        if (userDTO.getFacebookAccountId() == 0 && userDTO.getGoogleAccountId() == 0) {
            String password = userDTO.getPassword();
            String encodedPassword = passwordEncoder.encode(password);
            newUser.setPassword(encodedPassword);
        }

        // Lưu User mới vào UserRepository
        return userRepository.save(newUser);
    }

    // Phương thức đăng nhập
    @Override
    public String login(String phoneNumber, String password) throws Exception {
        Optional<User> optUser = userRepository.findByPhoneNumber(phoneNumber);
        // Kiểm tra nếu User không tồn tại
        if (optUser.isEmpty()) {
            throw new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeys.WRONG_PHONE_PASSWORD));
        }
        User existingUser = optUser.get();
        // Kiểm tra mật khẩu
        if (existingUser.getFacebookAccountId() == 0 && existingUser.getGoogleAccountId() == 0) {
            if (!passwordEncoder.matches(password, existingUser.getPassword())) {
                throw new BadCredentialsException(localizationUtils.getLocalizedMessage(MessageKeys.WRONG_PHONE_PASSWORD));
            }
        }

        // Tạo đối tượng xác thực
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        phoneNumber, password, existingUser.getAuthorities());

        // Xác thực thông tin đăng nhập
        authenticationManager.authenticate(authenticationToken);

        // Trả về token JWT
        return jwtTokenUtil.generateToken(existingUser);
    }
}
