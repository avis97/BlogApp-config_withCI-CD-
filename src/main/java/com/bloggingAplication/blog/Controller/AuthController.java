package com.bloggingAplication.blog.Controller;

import com.bloggingAplication.blog.Dtos.*;
import com.bloggingAplication.blog.Entity.RefreshToken;
import com.bloggingAplication.blog.Entity.User;
import com.bloggingAplication.blog.Exception.UserNotFoundException;
import com.bloggingAplication.blog.JwtSecurity.JwtTokenHelper;
import com.bloggingAplication.blog.JwtSecurity.ValidPassword;
import com.bloggingAplication.blog.Service.UserService;
import com.bloggingAplication.blog.Service.impl.RefreshTokenServiceImpl;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController{
    @Autowired
    JwtTokenHelper tokenHelper;
    @Autowired
    UserDetailsService detailsService;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    UserService userService;
    @Autowired
    ValidPassword validPassword;
    @Autowired
    RefreshTokenServiceImpl refreshTokenService;

    private final Logger LOGGER=Logger.getLogger(AuthController.class.getName());

    @PostMapping("/login")
    public ResponseEntity createToken(@RequestBody JwtAuthRequest request) throws Exception{
        try {
            authenticate(request.getUsername(),request.getPassword());
        } catch (UserNotFoundException e){
            LOGGER.info("Ok Username And Password incorrect!!");
            return new ResponseEntity<>("Invalid username or password",HttpStatus.BAD_REQUEST);
        }
        UserDetails userDetails = detailsService.loadUserByUsername(request.getUsername());
        JwtAuthResponse response=new JwtAuthResponse();
        String accessToken = tokenHelper.generateToken(userDetails);
        RefreshToken token=refreshTokenService.createRefreshToken(request.getUsername());
        response.setAccessToken(accessToken);
        response.setRefreshToken(token.getToken());
        response.setUsername(request.getUsername());

//        Cookie cookie=new Cookie("AuthToken",token);
//                cookie.setHttpOnly(true);
//                cookie.setPath("/");
//                cookie.setSecure(true);
//                cookie.setMaxAge(60*60*60);
//                cookie.setDomain("localhost");
//                response.addCookie(cookie);
//        ResponseCookie cookie = ResponseCookie.from("accessToken", token)
//                .httpOnly(true)
//                .secure(false)
//                .path("/api/v1/auth/login")
//                .maxAge(5*60*60)
//                .build();
//        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        LOGGER.info("Ok Username And Password correct!!");
        return new ResponseEntity(response,HttpStatus.ACCEPTED);
    }

    private void authenticate(String username, String password) throws Exception {
        UsernamePasswordAuthenticationToken Token=new
                UsernamePasswordAuthenticationToken(username,password);
        try {
            this.authenticationManager.authenticate(Token);
        }catch (BadCredentialsException e){
            throw new UserNotFoundException("number"+username);
        }
    }
    @PostMapping("/register")
    private ResponseEntity registerNewUser(@RequestBody UserRequestDtos userRequestDtos) throws Exception {
        UserResponseDtos dtos=null;

        if(validPassword.validPassword(userRequestDtos.getPassword())==false){
            return new ResponseEntity<>("Provide a Valid Password.",HttpStatus.BAD_REQUEST);
        }
        try{
            dtos=userService.registerNewUser(userRequestDtos);
        }catch (Exception e){
            return new ResponseEntity<>("Sorry Something wrong here",HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(dtos,HttpStatus.CREATED);
    }
    @PostMapping("refresh-token")
    public ResponseEntity refreshJwtWebToken(@RequestBody RequestTokenDto dto){

        RefreshToken refreshToken;
        String username = null;

        try{
            refreshToken=refreshTokenService.verifyExpiration(dto.getToken());
        }catch(Exception e){
            return new ResponseEntity("Refresh token are expire",HttpStatus.BAD_REQUEST);
        }

        User user=refreshToken.getUser();
        UserDetails userDetails=null;
        userDetails=detailsService.loadUserByUsername(user.getEmail());
        String token1=tokenHelper.generateToken(userDetails);
        return new ResponseEntity(token1,HttpStatus.ACCEPTED);
    }
    @PostMapping("/log-out")
    public ResponseEntity logout(HttpServletRequest request, HttpServletResponse response){
        Cookie[] cookies = request.getCookies();
        System.out.println(cookies.toString());
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("accessToken")) {
                    // Invalidate the token by removing or marking it as invalid
                    cookie.setMaxAge(0); // Remove the cookie
                    cookie.setValue(null);
                    cookie.setPath("/"); // Set the cookie path
                    response.addCookie(cookie);
                    break;
                }
            }
        }
        // Redirect the user to the login page or send a success message
        return ResponseEntity.ok("Logout successful");
    }

}
