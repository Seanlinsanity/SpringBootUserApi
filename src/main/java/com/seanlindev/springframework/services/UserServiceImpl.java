package com.seanlindev.springframework.services;

import com.seanlindev.springframework.api.dto.AddressDTO;
import com.seanlindev.springframework.api.response.ErrorMessageType;
import com.seanlindev.springframework.exceptions.UserServiceException;
import com.seanlindev.springframework.model.entities.UserEntity;
import com.seanlindev.springframework.repositories.UserRepository;
import com.seanlindev.springframework.api.dto.UserDto;
import com.seanlindev.springframework.shared.utils.PublicIdGenerator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    UserRepository userRepository;
    PublicIdGenerator publicIdGenerator;
    BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           PublicIdGenerator publicIdGenerator,
                           BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.publicIdGenerator = publicIdGenerator;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public UserDto createUser(UserDto user) {
        if (userRepository.findByEmail(user.getEmail()) != null)
            throw new RuntimeException("Record already exists");
        List<AddressDTO> addresses = user.getAddresses().stream().map(addressDTO -> {
            addressDTO.setAddressId(publicIdGenerator.generateAddressId(30));
            addressDTO.setUserDetails(user);
            return addressDTO;
        }).collect(Collectors.toList());
        user.setAddresses(addresses);

        ModelMapper modelMapper = new ModelMapper();
        UserEntity userEntity = modelMapper.map(user, UserEntity.class);
        userEntity.setUserId(publicIdGenerator.generateUserId(30));
        userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        UserEntity savedUserEntity = userRepository.save(userEntity);
        UserDto savedUser = modelMapper.map(savedUserEntity, UserDto.class);
        return savedUser;
    }

    @Override
    public UserDto updateUserByUserId(String id, UserDto user) {
        UserEntity userEntity = userRepository.findByUserId(id);
        if (userEntity == null)
            throw new UserServiceException(ErrorMessageType.NO_RECORD_FOUND.getErrorMessage());

        userEntity.setFirstName(user.getFirstName());
        userEntity.setLastName(user.getLastName());
        UserEntity updatedUserEntity = userRepository.save(userEntity);
        UserDto updatedUser = new UserDto();
        BeanUtils.copyProperties(updatedUserEntity, updatedUser);
        return updatedUser;
    }

    @Override
    public UserDto getUser(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity == null)
            throw new UsernameNotFoundException("User with email: " + email + " not found");
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userEntity, userDto);
        return userDto;
    }

    @Override
    public UserDto getUserByUserId(String userId) {
        UserEntity userEntity = userRepository.findByUserId(userId);
        if (userEntity == null)
            throw new UsernameNotFoundException("User with ID: " + userId + "not found");
        ModelMapper modelMapper = new ModelMapper();
        UserDto userDto = modelMapper.map(userEntity, UserDto.class);
        return userDto;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity == null)
            throw new UsernameNotFoundException(email);
        return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), new ArrayList<>());
    }

    @Override
    public void deleteUserByUserId(String id) {
        UserEntity userEntity = userRepository.findByUserId(id);
        if (userEntity == null)
            throw new UserServiceException(ErrorMessageType.NO_RECORD_FOUND.getErrorMessage());
        userRepository.delete(userEntity);
    }

    @Override
    public List<UserDto> getUsers(int page, int limit) {
        if (page > 0)
            page = page-1;
        PageRequest pageRequest = PageRequest.of(page, limit);
        Pageable pageable = pageRequest.toOptional().get();
        Page<UserEntity> usersPage = userRepository.findAll(pageable);
        return usersPage.get().map(userEntity -> {
            UserDto userDto = new UserDto();
            BeanUtils.copyProperties(userEntity, userDto);
            return userDto;
        }).collect(Collectors.toList());
    }
}
