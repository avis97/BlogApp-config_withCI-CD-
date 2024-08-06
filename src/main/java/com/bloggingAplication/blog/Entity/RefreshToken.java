package com.bloggingAplication.blog.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class RefreshToken{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String token;
    private Instant expiryDate;
    @OneToOne
    @JoinColumn
    User user;
}
