package com.bloggingAplication.blog.Repository;

import com.bloggingAplication.blog.Entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Integer>{

}
