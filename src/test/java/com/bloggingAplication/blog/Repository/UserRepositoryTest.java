package com.bloggingAplication.blog.Repository;

import com.bloggingAplication.blog.Entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;




@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @BeforeEach
    void setUp() {
        User user = User.builder()
                .name("Avishek Sarkar")
                .id(2)
                .user_mobile("062037206")  // Fixed mobile number format
                .email("savishek@gmail.com")
                .about("Software Developer")
                .build();

        testEntityManager.persist(user);
    }

    @Test
    public void findByUserByIdTest() {
        User user = userRepository.findById(2).orElse(null);
        assertEquals(2, user.getId()); // Check null and assert the id
    }
}