package com.seanlindev.springframework.controllers;

import com.seanlindev.springframework.api.dto.AddressDTO;
import com.seanlindev.springframework.api.dto.UserDto;
import com.seanlindev.springframework.api.response.UserResponse;
import com.seanlindev.springframework.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    @InjectMocks
    UserController userController;

    @Mock
    UserService userService;

    private final String userId = "qo08y103314hifh";
    private UserDto userDto;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        userDto = new UserDto();
        userDto.setUserId(userId);
        userDto.setFirstName("Steph");
        userDto.setLastName("Curry");
        userDto.setEmail("test@gmail.com");
        userDto.setEncryptedPassword("14dhu1898014");
        userDto.setAddresses(getAddressDTOList());
    }

    @Test
    void testGetUser() {
        when(userService.getUserByUserId(anyString())).thenReturn(userDto);

        UserResponse userResponse = userController.getUser(userId);
        assertNotNull(userResponse);
        assertEquals(userId, userResponse.getUserId());
        assertEquals(userDto.getFirstName(), userResponse.getFirstName());
        assertEquals(userDto.getAddresses().size(), userResponse.getAddresses().size());
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
