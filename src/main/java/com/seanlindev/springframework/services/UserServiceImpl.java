package com.seanlindev.springframework.services;

import com.seanlindev.springframework.entities.UserEntity;
import com.seanlindev.springframework.repositories.UserRepository;
import com.seanlindev.springframework.shared.dto.UserDto;
import com.seanlindev.springframework.shared.utils.UserUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    UserRepository userRepository;
    UserUtils userUtils;

    public UserServiceImpl(UserRepository userRepository,
                           UserUtils userUtils) {
        this.userRepository = userRepository;
        this.userUtils = userUtils;
    }

    @Override
    public UserDto createUser(UserDto user) {
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new RuntimeException("Record already exists");
        }

        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(user, userEntity);
        userEntity.setUserId(userUtils.generateUserId(30));
        userEntity.setEncryptedPassword("123456");
        UserEntity savedUserEntity = userRepository.save(userEntity);
        UserDto savedUser = new UserDto();
        BeanUtils.copyProperties(savedUserEntity, savedUser);
        return savedUser;
    }
}
