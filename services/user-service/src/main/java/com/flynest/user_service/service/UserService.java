package com.flynest.user_service.service;

import com.flynest.payload.dto.UserDto;
import org.apache.catalina.UserDatabase;

import java.util.List;

public interface UserService {


    UserDto getUserByEmail(String email) throws Exception;
    UserDto getUserById(Long id) throws Exception;
    List<UserDto> getAllUsers();
    UserDto getProfile() throws Exception;
}
