package com.bloggingAplication.blog.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.*;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="users")
@Builder
public class User implements UserDetails{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "user_name",nullable = false,length = 100)
    private String name;
    @Column(name="user_email")
    @Email
    private String email;
    private String user_mobile;
    @Column(unique = true)
    private String password;
    private String about;
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    List<Post> postList=new ArrayList<>();
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    List<Comments> commentsList=new ArrayList<>();
    @ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinTable(name="user_role",joinColumns = @JoinColumn(name="user",referencedColumnName = "id"),
            inverseJoinColumns =@JoinColumn(name="role",referencedColumnName = "roleId"))
    Set<Role> roles=new HashSet<>();
    @OneToOne(mappedBy ="user",cascade = CascadeType.ALL)
    RefreshToken refreshToken;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
       List<SimpleGrantedAuthority> collect = this.roles.stream().map((role)-> new SimpleGrantedAuthority(role.getRoleName())).collect(Collectors.toList());
       return collect;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
