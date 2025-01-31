package com.bloggingAplication.blog.Entity;

import jdk.jfr.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="Posts")
@Builder
public class Post{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int postId;
    @NotEmpty
    private String postTitle;
    @NotEmpty
    private String content;
    private String imageName;
    @CreationTimestamp
    private Date date;
    @ManyToOne
    @JoinColumn(name="category_id")
    private Category category;
    @ManyToOne
    @JoinColumn
    private User user;
    @OneToMany(mappedBy = "post",cascade = CascadeType.ALL)
    List<Comments> commentsList=new ArrayList<>();
}