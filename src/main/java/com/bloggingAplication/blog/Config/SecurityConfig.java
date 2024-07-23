package com.bloggingAplication.blog.Config;

import com.bloggingAplication.blog.JwtSecurity.JwtAuthenticationEntryPoint;
import com.bloggingAplication.blog.JwtSecurity.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebSecurity
@EnableWebMvc
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter{

    public static final String[] PUBLIC_URLS={
        "/api/v1/auth/login",
            "/v3/api-docs",
            "/v2/api-docs",
            "/api/v1/auth/register",
            "/swagger-resources/**",
            "/swagger-ui/**",
            "/webjars/**"
    };
    
    @Autowired
    CustomUserDetails customUserDetails;
    @Autowired
    JwtAuthenticationEntryPoint entryPoint;
    @Autowired
    JwtAuthenticationFilter filter;


    @Override
    protected void configure(HttpSecurity http)throws Exception{
            http
                .csrf().disable()
                .authorizeHttpRequests()
                    .antMatchers(PUBLIC_URLS).permitAll()
                    .anyRequest()
                    .authenticated()
                    .and()//.basicAuth() from here start jwt security auth.
                    .exceptionHandling()
                    .authenticationEntryPoint(entryPoint)
                    .and()
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
          http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
  }
   @Override
   protected void configure(AuthenticationManagerBuilder auth) throws Exception {
       auth.userDetailsService(customUserDetails)
              .passwordEncoder(passwordEncoder());
  }
   @Bean
  public PasswordEncoder passwordEncoder(){
       return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
   public AuthenticationManager authenticationManagerBean() throws Exception{
      return super.authenticationManagerBean();
   }
}
