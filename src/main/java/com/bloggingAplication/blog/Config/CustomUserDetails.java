package com.bloggingAplication.blog.Config;
import com.bloggingAplication.blog.Entity.User;
import com.bloggingAplication.blog.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
@Service
public class CustomUserDetails implements UserDetailsService{
    @Autowired
    UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username){
        User user;
        try{
          user=userRepository.findByEmail(username);
        }catch(Exception e){
            throw new UsernameNotFoundException("Invalid User Id!!");
        }
        return user;
    }
}
