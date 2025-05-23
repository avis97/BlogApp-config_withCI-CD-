package com.bloggingAplication.blog.JwtSecurity;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{

    @Autowired
    UserDetailsService userDetailsService;
    @Autowired
    JwtTokenHelper jwtTokenHelper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException{

        String requestToken;
        requestToken=request.getHeader("Authorization");

        String userName=null;
        String token=null;

        if(requestToken!=null && requestToken.startsWith("Bearer")){

            token=requestToken.substring(7);

            try {
                userName = jwtTokenHelper.getUsernameFromToken(token);
            }catch(IllegalArgumentException e){
                throw new IllegalArgumentException("Unable to open access token");
            }catch(ExpiredJwtException g){
                System.out.println("Token is expired");
            }catch(MalformedJwtException h){
                throw new MalformedJwtException("Invalid Jwt Exception.");
            }
        }else{
            System.out.println("Token is not generated");
        }

        if(userName!=null && SecurityContextHolder.getContext().getAuthentication()==null){

            UserDetails userDetails=userDetailsService.loadUserByUsername(userName);
            if(jwtTokenHelper.validateToken(token,userDetails)){
                UsernamePasswordAuthenticationToken Passwordtoken=new
                        UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                Passwordtoken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(Passwordtoken);

            }else{
                System.out.println("Invalid JWT token");
            }
        }else{
            System.out.println("Username is null or context is not null");
        }

//        String token = null;
//        String username = null;
//        if(request.getCookies() != null){
//            for(Cookie cookie: request.getCookies()){
//                if(cookie.getName().equals("AuthToken")){
//                    token = cookie.getValue();
//                }
//            }
//        }
//        if(token == null){
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        username = jwtTokenHelper.getUsernameFromToken(token);
//
//        if(username != null){
//            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//            if(jwtTokenHelper.validateToken(token, userDetails)){
//                UsernamePasswordAuthenticationToken authenticationToken =
//                        new UsernamePasswordAuthenticationToken(userDetails, null,
//                                userDetails.getAuthorities());
//                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//            }
//
//        }
       filterChain.doFilter(request,response);
    }
}
