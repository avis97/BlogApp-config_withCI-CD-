package com.bloggingAplication.blog.Service.impl;

import com.bloggingAplication.blog.Entity.RefreshToken;
import com.bloggingAplication.blog.Entity.User;
import com.bloggingAplication.blog.Repository.RefreshTokenRepository;
import com.bloggingAplication.blog.Repository.UserRepository;
import com.bloggingAplication.blog.Service.RefreshTokenService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Autowired
    UserRepository userRepository;

    public RefreshToken createRefreshToken(String username){
        User user=userRepository.findByEmail(username);
        RefreshToken refreshToken=user.getRefreshToken();

        if(refreshToken==null){
            refreshToken=RefreshToken.builder()
                    .token(UUID.randomUUID().toString())
                    .expiryDate(Instant.now().plusMillis(60000000))
                    .user(user)
                    .build();
        }else{
            refreshToken.setExpiryDate(Instant.now().plusMillis(60000000));
        }
        user.setRefreshToken(refreshToken);

        refreshTokenRepository.save(refreshToken);

        return refreshToken;
    }

//    public Optional<RefreshToken> findByToken(String token){
//        return refreshTokenRepository.findByToken(token);
//    }

    public RefreshToken verifyExpiration(String token){
        RefreshToken refreshToken=refreshTokenRepository.findByToken(token);
        if(refreshToken.getExpiryDate().compareTo(Instant.now())<0){
            refreshTokenRepository.delete(refreshToken);
            throw new RuntimeException(refreshToken.getToken() + " Refresh token is expired. Please make a new login..!");
        }
        return refreshToken;
    }

}
