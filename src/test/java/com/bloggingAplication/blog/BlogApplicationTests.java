package com.bloggingAplication.blog;

import com.bloggingAplication.blog.Repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BlogApplicationTests {
	@Autowired
	UserRepository repo;
	@Test
	void contextLoads(){

	}
	@Test
    public void repoTest(){
		String className=this.repo.getClass().getName();
		System.out.println(className);
	}
}
