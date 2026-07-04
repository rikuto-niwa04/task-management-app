package com.example.taskmanagementapp.domain.task;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.List;

public interface TaskRepository
        extends JpaRepository<Task, Long>,
                JpaSpecificationExecutor<Task> {

    List<Task> findByStatus(TaskStatus status);

    List<Task> findByAssigneeId(Long assigneeId);

    List<Task> findByStatusAndAssigneeId(TaskStatus status, Long assigneeId);
}