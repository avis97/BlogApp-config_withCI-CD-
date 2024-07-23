package com.bloggingAplication.blog.Service.impl;

import com.bloggingAplication.blog.Converter.PostConverter;
import com.bloggingAplication.blog.Dtos.PostRequestDtos;
import com.bloggingAplication.blog.Dtos.PostResponseDto;
import com.bloggingAplication.blog.Entity.Category;
import com.bloggingAplication.blog.Entity.Post;
import com.bloggingAplication.blog.Entity.User;
import com.bloggingAplication.blog.Exception.CategoryNotFoundException;
import com.bloggingAplication.blog.Exception.PostNotFoundException;
import com.bloggingAplication.blog.Exception.UserNotFoundException;
import com.bloggingAplication.blog.Repository.CategoryRepository;
import com.bloggingAplication.blog.Repository.PostRepository;
import com.bloggingAplication.blog.Repository.UserRepository;
import com.bloggingAplication.blog.Service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {
    @Autowired
    PostRepository postRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CategoryRepository categoryRepository;

    public PostResponseDto addPost(PostRequestDtos postRequestDtos) throws UserNotFoundException, CategoryNotFoundException {
        User user;
        Category category;
        try{
            user=userRepository.findById(postRequestDtos.getUserId()).get();
        }catch(Exception e){
            throw new UserNotFoundException("Invalid User id");
        }
        try{
            category=categoryRepository.findById(postRequestDtos.getCategoryId()).get();
        }catch(Exception e){
            throw new CategoryNotFoundException("Invalid Id!!!");
        }

        Post post= PostConverter.postRequestDtoToPost(postRequestDtos);
        post.setUser(user);
        post.setCategory(category);

        List<Post> userPostList=user.getPostList();
        userPostList.add(post);

        List<Post> categoryPostList=category.getPostList();
        categoryPostList.add(post);

        postRepository.save(post);

        PostResponseDto responseDto=PostConverter.postToPostResponseDto(post);

        return responseDto;
    }
    //get post by category...

    //gt post by user id....

   public PostResponseDto getPostById(Integer postId) throws PostNotFoundException {
        Post post;
        try{
            post=postRepository.findById(postId).get();
        }catch(Exception e){
            throw new PostNotFoundException("Post Id is invalid...");
        }

        PostResponseDto responseDto=PostConverter.postToPostResponseDto(post);

        return responseDto;
    }

    public PostResponseDto updatePost(PostRequestDtos requestDto,Integer postId) throws PostNotFoundException {
           Post post;
           try{
               post=postRepository.findById(postId).get();
           }catch(Exception e){
               throw new PostNotFoundException("Post Id is invalid...");
           }
           //set new property
           post.setPostTitle(requestDto.getPostTitle());
           post.setContent(requestDto.getContent());
           post.setImageName(requestDto.getImageName());

           postRepository.save(post);

           PostResponseDto responseDto=PostConverter.postToPostResponseDto(post);

           return responseDto;
    }
    public List<PostResponseDto> getAllPost(){
        List<Post> postList=postRepository.findAll();

        List<PostResponseDto> list=new ArrayList<>();
        for(Post key:postList){
            PostResponseDto responseDto=PostConverter.postToPostResponseDto(key);
            list.add(responseDto);
        }
        return list;
    }
}
