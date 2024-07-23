package com.bloggingAplication.blog.Controller;

import java.util.*;
import com.bloggingAplication.blog.Converter.PostConverter;
import com.bloggingAplication.blog.Dtos.PostRequestDtos;
import com.bloggingAplication.blog.Dtos.PostResponseDto;
import com.bloggingAplication.blog.Entity.Post;
import com.bloggingAplication.blog.Exception.CategoryNotFoundException;
import com.bloggingAplication.blog.Exception.PostNotFoundException;
import com.bloggingAplication.blog.Exception.UserNotFoundException;
import com.bloggingAplication.blog.Service.FileService;
import com.bloggingAplication.blog.Service.PostService;
import com.bloggingAplication.blog.Service.impl.PostServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/post")
public class PostController{
    @Autowired
    PostService postService;
    @Autowired
    FileService fileService;
    @Value("${project.image}")
    private String path;

    @PostMapping("/addpost")
    public ResponseEntity addPost(@RequestBody PostRequestDtos postRequestDtos) throws UserNotFoundException, CategoryNotFoundException{
        PostResponseDto postResponseDto;
        try{
            postResponseDto=postService.addPost(postRequestDtos);
            System.out.println(postResponseDto);
        }catch(UserNotFoundException e){

            return new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST);

        }catch(CategoryNotFoundException e){

            return new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST);

        }
        return new ResponseEntity(postResponseDto,HttpStatus.ACCEPTED);
    }
    @GetMapping("/getPosts")
    public ResponseEntity getAllPost(){
        List<PostResponseDto> postList=new ArrayList<>();
        try{
            postList=postService.getAllPost();
        }catch (Exception e){
            return new ResponseEntity("Post Not Found",HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(postList,HttpStatus.ACCEPTED);
    }
    @GetMapping("/getPostById/{postId}")
    public ResponseEntity getPostById(@PathVariable("postId") Integer postId) throws PostNotFoundException {
        PostResponseDto responseDto;
        try{
            responseDto=postService.getPostById(postId);
        }catch (PostNotFoundException e){
            return new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(responseDto,HttpStatus.ACCEPTED);
    }
    @PutMapping("/updatePost/{postId}")
    public ResponseEntity updatePost(@RequestBody PostRequestDtos requestDto,
                                     @PathVariable("postId") Integer postId){

        PostResponseDto responseDto;
         try{
             responseDto=postService.updatePost(requestDto,postId);
         }catch (PostNotFoundException e){
             return new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST);
         }

         return new ResponseEntity(responseDto,HttpStatus.ACCEPTED);
    }
    @PostMapping("/upload/image/{postId}")
    public ResponseEntity insertImage(@RequestParam("image") MultipartFile image ,
                              @PathVariable("postId") Integer postId) throws IOException, PostNotFoundException{
        PostResponseDto data;
                try {
                    data = postService.getPostById(postId);
                }catch (PostNotFoundException e){
                    return new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST);
                }
        String name=fileService.uploadImage(path,image);
        PostRequestDtos oldData= PostConverter.PostResponseToRequestDto(data);
        oldData.setImageName(name);
        PostResponseDto newImage=postService.updatePost(oldData,postId);

        return new ResponseEntity(newImage,HttpStatus.ACCEPTED);
    }
    @GetMapping(value = "/getImage/image/{imageName}",produces = MediaType.IMAGE_JPEG_VALUE)
    public void downloadImage(@PathVariable("imageName") String imageName,
                              HttpServletResponse response)throws IOException{

        InputStream resource=fileService.getResources(path,imageName);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource,response.getOutputStream());
    }

}
