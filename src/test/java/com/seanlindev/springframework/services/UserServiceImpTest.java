package com.seanlindev.springframework.services;

import com.seanlindev.springframework.api.dto.AddressDTO;
import com.seanlindev.springframework.api.dto.UserDto;
import com.seanlindev.springframework.exceptions.UserServiceException;
import com.seanlindev.springframework.model.entities.AddressEntity;
import com.seanlindev.springframework.model.entities.UserEntity;
import com.seanlindev.springframework.repositories.UserRepository;
import com.seanlindev.springframework.shared.utils.PublicIdGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class UserServiceImpTest {
    @InjectMocks
    UserServiceImpl userService;

    @Mock
    UserRepository userRepository;

    @Mock
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    PublicIdGenerator publicIdGenerator;

    private final String userId = "10gfhqd01d01";
    private final String encryptedPaasword = "j23h491071";
    private UserEntity userEntity;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
        userEntity = new UserEntity();
        userEntity.setUserId(userId);
        userEntity.setFirstName("Steph");
        userEntity.setLastName("Curry");
        userEntity.setEncryptedPassword(encryptedPaasword);
    }

    @Test
    void testGetUser() {
        //given
        UserEntity userResult = userEntity;

        //when
        when(userRepository.findByEmail(anyString())).thenReturn(userResult);

        //then
        UserDto userDto = userService.getUser("test@gmail.com");
        assertNotNull(userDto);
        assertEquals(userDto.getUserId(), userId);
        assertEquals(userDto.getFirstName(), "Steph");
        assertEquals(userDto.getLastName(), "Curry");
        assertEquals(userDto.getEncryptedPassword(), encryptedPaasword);
    }

    @Test
    void testGetUserNotFoundException() {
        //when
        when(userRepository.findByEmail(anyString())).thenReturn(null);
        //then
        assertThrows(UsernameNotFoundException.class,
                () -> {
                    userService.getUser("test@gamil.com");
                });
    }

    @Test
    void testCreateUser() {
        //given
        UserDto userDto = new UserDto();
        userDto.setPassword("123456");

        List<AddressDTO> addressDTOList = getAddressDTOList();
        userDto.setAddresses(addressDTOList);

        Type listType = new TypeToken<List<AddressEntity>>() {
        }.getType();
        List<AddressEntity> addresses = new ModelMapper().map(addressDTOList, listType);
        userEntity.setAddresses(addresses);

        //when
        when(userRepository.findByEmail(anyString())).thenReturn(null);
        when(publicIdGenerator.generateAddressId(anyInt())).thenReturn("285u104rupq90uh");
        when(publicIdGenerator.generateUserId(anyInt())).thenReturn(userId);
        when(bCryptPasswordEncoder.encode(anyString())).thenReturn(encryptedPaasword);
        when(userRepository.save(ArgumentMatchers.any(UserEntity.class))).thenReturn(userEntity);

        //then
        UserDto storeUserDetails = userService.createUser(userDto);
        assertNotNull(storeUserDetails);
        assertEquals(userEntity.getFirstName(), storeUserDetails.getFirstName());
        assertEquals(userEntity.getEncryptedPassword(), storeUserDetails.getEncryptedPassword());
        assertEquals(userEntity.getUserId(), storeUserDetails.getUserId());
        assertEquals(userEntity.getEmail(), storeUserDetails.getEmail());
        assertEquals(userEntity.getAddresses().size(), addressDTOList.size());

        verify(publicIdGenerator, times(addressDTOList.size())).generateAddressId(30);
        verify(bCryptPasswordEncoder, times(1)).encode("123456");
        verify(userRepository, times(1)).save(ArgumentMatchers.any(UserEntity.class));
    }

    @Test
    void testCreateUserServiceException() {
        when(userRepository.findByEmail(anyString())).thenReturn(userEntity);
        UserDto userDto = new UserDto();
        userDto.setEmail("test@gmail.com");
        assertThrows(UserServiceException.class,
                () -> {
                    userService.createUser(userDto);
                }
        );
    }

    private List<AddressDTO> getAddressDTOList() {
        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setCity("Vancouver");
        addressDTO.setCountry("Canada");
        addressDTO.setType("shipping");
        addressDTO.setPostalCode("8123");
        addressDTO.setStreetName("123 Street");

        AddressDTO billingAddressDto = new AddressDTO();
        billingAddressDto.setCity("San Fransico");
        billingAddressDto.setCountry("USA");
        billingAddressDto.setType("billing");
        billingAddressDto.setPostalCode("1098");
        billingAddressDto.setStreetName("999 Street");

        List<AddressDTO> addressDTOList = new ArrayList<>();
        addressDTOList.add(addressDTO);
        addressDTOList.add(billingAddressDto);
        return addressDTOList;
    }
}
