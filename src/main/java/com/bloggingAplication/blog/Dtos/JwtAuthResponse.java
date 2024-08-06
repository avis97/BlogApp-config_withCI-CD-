package com.bloggingAplication.blog.Dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JwtAuthResponse{
    private String accessToken;
    private String refreshToken;
    private String username;
}
