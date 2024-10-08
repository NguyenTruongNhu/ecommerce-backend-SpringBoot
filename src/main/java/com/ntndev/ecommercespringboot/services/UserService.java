package com.ntndev.ecommercespringboot.services;

import com.ntndev.ecommercespringboot.dtos.UserDTO;
import com.ntndev.ecommercespringboot.exceptions.DataNotFoundException;
import com.ntndev.ecommercespringboot.models.User;

public interface UserService {
    User createUser(UserDTO userDTO) throws Exception;

    String login(String phoneNumber, String password, Long roleId) throws Exception;
}
