package com.bloggingAplication.blog.Controller;

import com.bloggingAplication.blog.Dtos.PostResponseDto;
import com.bloggingAplication.blog.Dtos.UserRequestDtos;
import com.bloggingAplication.blog.Dtos.UserResponseDtos;
import com.bloggingAplication.blog.Entity.User;
import com.bloggingAplication.blog.Exception.UserNotFoundException;
import com.bloggingAplication.blog.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
@RestController
@RequestMapping("/user")
public class UserController{
    @Autowired
    UserService userService;

    @PostMapping("/adduser")
    public UserResponseDtos addUser(@Valid @RequestBody UserRequestDtos userRequestDtos){
         return userService.addUser(userRequestDtos);
    }

    @GetMapping("/getAllUser")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity getAllUser(){
        List<UserResponseDtos> userList;
        try {
            userList = userService.getAllUser();
        }catch(Exception e){
            return new ResponseEntity("User is empty",HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(userList,HttpStatus.ACCEPTED);
    }

    @GetMapping("/getUserById/{userId}")
    public ResponseEntity getUserById(@PathVariable("userId") int userId) throws UserNotFoundException {
        UserResponseDtos user;
        try{
            user=userService.getUserById(userId);
        }catch (UserNotFoundException e){
            return new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(user,HttpStatus.ACCEPTED);
    }
    @PutMapping("/updateuser/{id}")
    public ResponseEntity updateUser(@Valid @RequestBody UserRequestDtos userRepo, @PathVariable("id") Integer id) throws UserNotFoundException {
         UserResponseDtos user;
         try{
             user=userService.updateUser(userRepo,id);
         }catch(UserNotFoundException e){
             return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
         }
         return new ResponseEntity(user,HttpStatus.ACCEPTED);
    }
    @GetMapping("/getAllPost/{userId}")
    public ResponseEntity getAllPostByUserId(@PathVariable("userId") int userId){
        List<PostResponseDto> posts;
        try {
           posts= userService.getAllPostByUserId(userId);
        }catch (UserNotFoundException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(posts,HttpStatus.ACCEPTED);
    }
    @DeleteMapping("/deleteUser/{userId}")
    public ResponseEntity deleteUserById(@PathVariable("userId") int userId) throws UserNotFoundException {
        UserResponseDtos user;
        try{
            user=userService.deleteUserById(userId);
        }catch (UserNotFoundException e){
            return new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
        String UserName=user.getUserName();
        return new ResponseEntity(UserName+" This User Is Deleted!!",HttpStatus.ACCEPTED);
    }
}
