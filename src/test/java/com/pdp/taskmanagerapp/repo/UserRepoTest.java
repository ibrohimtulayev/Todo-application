package com.pdp.taskmanagerapp.repo;

import com.pdp.taskmanagerapp.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)

class UserRepoTest {

    @Autowired
    private UserRepo userRepo;

    @BeforeEach
    void setUp() {

    }


    @Test
    void findByUsername() {

        // Arrange
        User user = User.builder()
                .username("testuser")
                .password("testpassword")
                .role("ROLE_USER")
                .avatar("testavatar.png")
                .overdueTasks(0)
                .successfulTasks(0)
                .allTasks(0)
                .build();

        userRepo.save(user);

        // Act
        User foundUser = userRepo.findByUsername("testuser");
        assertNotNull(foundUser);

    }
}