package com.pdp.taskmanagerapp.repo;

import com.pdp.taskmanagerapp.entity.Column;
import com.pdp.taskmanagerapp.entity.Task;
import com.pdp.taskmanagerapp.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)

class TaskRepoTest {
    @Autowired
    private TaskRepo taskRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ColumnRepo columnRepo;

    @Test
    void findAllByUsersContaining() {
        User user = User.builder()
                .username("testuser")
                .password("testpassword")
                .role("ROLE_USER")
                .build();
        userRepo.save(user);

        Column column = Column.builder()
                .name("In Progress")
                .orderNumber(1)
                .build();
        columnRepo.save(column);

        Task task = Task.builder()
                .name("Test Task")
                .deadLine(LocalDate.now().plusDays(1))
                .column(column)
                .users(Collections.singletonList(user))
                .build();
        taskRepo.save(task);

        List<Task> tasks = taskRepo.findAllByUsersContaining(user);
        assertEquals(1, tasks.size());
        assertEquals(tasks.get(0).getName(), "Test Task");
    }

    @Test
    void countByUsersContainingAndIsOverdue() {
        User user = User.builder()
                .username("testuser")
                .password("testpassword")
                .role("ROLE_USER")
                .build();
        userRepo.save(user);

        Column column = Column.builder()
                .name("In Progress")
                .orderNumber(1)
                .build();
        columnRepo.save(column);

        Task task = Task.builder()
                .name("Test Task")
                .deadLine(LocalDate.now().minusDays(1))
                .column(column)
                .users(Collections.singletonList(user))
                .build();
        taskRepo.save(task);

        // Act
        long overdueCount = taskRepo.countByUsersContainingAndIsOverdue(user.getId());
        assertEquals(1, overdueCount);
    }

    @Test
    void countByUsersContainingAndIsCompletedOnTime() {
        User user = User.builder()
                .username("testuser")
                .password("testpassword")
                .role("ROLE_USER")
                .build();
        userRepo.save(user);

        Column column = Column.builder()
                .name("Completed")
                .orderNumber(1)
                .build();
        columnRepo.save(column);

        Task task = Task.builder()
                .name("Test Task")
                .deadLine(LocalDate.now().plusDays(1))
                .finishDate(LocalDate.now())
                .column(column)
                .users(Collections.singletonList(user))
                .build();
        taskRepo.save(task);

        // Act
        long completedOnTimeCount = taskRepo.countByUsersContainingAndIsCompletedOnTime(user.getId());
        assertEquals(1, completedOnTimeCount);
    }

    @Test
    void countByUsersContaining() {
        User user = User.builder()
                .username("testuser")
                .password("testpassword")
                .role("ROLE_USER")
                .build();
        userRepo.save(user);

        Column column = Column.builder()
                .name("In Progress")
                .orderNumber(1)
                .build();
        columnRepo.save(column);

        Task task = Task.builder()
                .name("Test Task")
                .deadLine(LocalDate.now().plusDays(1))
                .column(column)
                .users(Collections.singletonList(user))
                .build();
        taskRepo.save(task);


        long taskCount = taskRepo.countByUsersContaining(user);
        assertEquals(1, taskCount);
    }
}