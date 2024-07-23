package com.bloggingAplication.blog.Service.impl;

import com.bloggingAplication.blog.Converter.PostConverter;
import com.bloggingAplication.blog.Converter.UserConverter;
import com.bloggingAplication.blog.Dtos.PostResponseDto;
import com.bloggingAplication.blog.Dtos.UserRequestDtos;
import com.bloggingAplication.blog.Dtos.UserResponseDtos;
import com.bloggingAplication.blog.Entity.Post;
import com.bloggingAplication.blog.Entity.User;
import com.bloggingAplication.blog.Exception.UserNotFoundException;
import com.bloggingAplication.blog.Repository.UserRepository;
import com.bloggingAplication.blog.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    private static final Logger LOGGER=
            Logger.getLogger(UserServiceImpl.class.getName());

    public UserResponseDtos addUser(UserRequestDtos userRequestDtos){
        //convert requestDto to user;
        User user= UserConverter.RequestDtoToUser(userRequestDtos);

        userRepository.save(user);
        LOGGER.info("Ok User Register Successfully done and safe in database");
        //convert user to userResponseDtos
        UserResponseDtos userResponseDtos=UserConverter.UserToResponseDto(user);

        return userResponseDtos;
    }

    public UserResponseDtos updateUser(UserRequestDtos userRepo, Integer id) throws UserNotFoundException {
           User user;
           try{
               user=userRepository.findById(id).get();
           }
           catch(Exception e){
               throw new UserNotFoundException("Wrong User Id!! "+id);
           }
           //set new details
           user.setName(userRepo.getUser_name());
           user.setUser_mobile(userRepo.getUser_mobile());
           user.setEmail(userRepo.getUser_email());
           user.setAbout(userRepo.getAbout());

           userRepository.save(user);

           UserResponseDtos userResponseDtos=UserConverter.UserToResponseDto(user);

           return userResponseDtos;
    }
    public UserResponseDtos registerNewUser(UserRequestDtos userRequestDtos){

        User user=UserConverter.RequestDtoToUser(userRequestDtos);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        UserResponseDtos responseDtos=UserConverter.UserToResponseDto(user);
        return responseDtos;
    }
    public List<UserResponseDtos> getAllUser(){
        List<User> userList=userRepository.findAll();
        List<UserResponseDtos> listOfUser=new ArrayList<>();
        for(User user:userList){
            UserResponseDtos responseDtos=UserConverter.UserToResponseDto(user);
            listOfUser.add(responseDtos);
        }
        return listOfUser;
    }
    public UserResponseDtos getUserById(int userId) throws UserNotFoundException {
        User user;
        try{
            user=userRepository.findById(userId).get();
        }catch(Exception e){
            throw new UserNotFoundException("User Not Found!!");
        }
        UserResponseDtos userResponseDtos=UserConverter.UserToResponseDto(user);
        return userResponseDtos;
    }
    public List<PostResponseDto> getAllPostByUserId(int userId) throws UserNotFoundException {
        User user;
        try{
            user=userRepository.findById(userId).get();
        }catch(Exception e){
            throw new UserNotFoundException("User id is not valid");
        }
        List<Post> postList=user.getPostList();
        List<PostResponseDto> posts=new ArrayList<>();
        for(Post p:postList){
            PostResponseDto responseDto= PostConverter.postToPostResponseDto(p);
            posts.add(responseDto);
        }
        return posts;
    }
    public UserResponseDtos deleteUserById(int userId) throws UserNotFoundException {
        User user;
        try{
            user=userRepository.findById(userId).get();
            userRepository.delete(user);
        }catch(Exception e){
            throw new UserNotFoundException("User Not Found");
        }
        UserResponseDtos userResponseDtos=UserConverter.UserToResponseDto(user);
        return userResponseDtos;
    }
}
