package com.seanlindev.springframework.services;

import com.seanlindev.springframework.shared.dto.UserDto;

public interface UserService {
    UserDto createUser(UserDto user);
}
