package com.seanlindev.springframework.services;

import com.seanlindev.springframework.api.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    UserDto createUser(UserDto user);

    UserDto getUser(String email);

    UserDto getUserByUserId(String id);

    UserDto updateUserByUserId(String id, UserDto user);

    void deleteUserByUserId(String id);

    List<UserDto> getUsers(int page, int limit);
}
