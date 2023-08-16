package com.bloggingAplication.blog.Service;

import com.bloggingAplication.blog.Dtos.PostRequestDtos;
import com.bloggingAplication.blog.Dtos.PostResponseDto;
import com.bloggingAplication.blog.Exception.CategoryNotFoundException;
import com.bloggingAplication.blog.Exception.PostNotFoundException;
import com.bloggingAplication.blog.Exception.UserNotFoundException;

public interface PostService{

    PostResponseDto addPost(PostRequestDtos postRequestDtos) throws UserNotFoundException, CategoryNotFoundException;
    PostResponseDto getPostById(Integer postId) throws PostNotFoundException;
    PostResponseDto updatePost(PostRequestDtos requestDto,Integer postId) throws PostNotFoundException;
}
