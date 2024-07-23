package com.bloggingAplication.blog.Service.impl;

import com.bloggingAplication.blog.Dtos.UserResponseDtos;
import com.bloggingAplication.blog.Entity.User;
import com.bloggingAplication.blog.Exception.UserNotFoundException;
import com.bloggingAplication.blog.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
@SpringBootTest
class UserServiceImplTest {
    @Autowired
    UserServiceImpl userService;
    @MockBean
    UserRepository userRepository;

    @BeforeEach
    void setUp(){
        User user=User.builder()
                .name("Avishek Sarkar")
                .id(2)
                .user_mobile("(062037206")
                .email("savishek@gmail.com")
                .about("Software Developer")
                .build();
        Mockito.when(userRepository.findById(2))
                .thenReturn(Optional.ofNullable(user));

        List<User> userList=new ArrayList<>();
        userList.add(user);
        Mockito.when(userRepository.findAll())
                .thenReturn(userList);

    }
    @Test
    @DisplayName("Test The User With The Correct Id Pass")
    void getUserByIdCheckIfPresentIdDatabase() throws UserNotFoundException {
         int userId=2;
         UserResponseDtos findById=userService.getUserById(userId);
         assertEquals(userId,findById.getUserId());
    }

    @Test
    @DisplayName("Check Get All User Are Present Or Not")
    void getAllUserLengthNot0(){
        int length=1;
        List<UserResponseDtos> list=userService.getAllUser();
        assertEquals(length,list.size());
    }
}