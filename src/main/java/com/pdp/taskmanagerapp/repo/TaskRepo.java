package com.pdp.taskmanagerapp.repo;

import com.pdp.taskmanagerapp.entity.Task;
import com.pdp.taskmanagerapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskRepo extends JpaRepository<Task, Integer> {

    List<Task> findAllByUsersContaining(User user);

    @Query(value = "SELECT COUNT(t) FROM Task t " +
            "JOIN t.users u " +
            "WHERE u.id = :userId " +
            "AND t.deadLine < CURRENT_DATE " +
            "AND t.column.name <> 'Completed'")
    long countByUsersContainingAndIsOverdue(@Param("userId") Integer userId);

    @Query(value = "SELECT COUNT(t) FROM Task t " +
            "JOIN t.users u " +
            "WHERE u.id = :userId " +
            "AND t.deadLine >= CURRENT_DATE " +
            "AND t.column.name = 'Completed'")
    long countByUsersContainingAndIsCompletedOnTime(@Param("userId") Integer userId);

    long countByUsersContaining(User user);
}