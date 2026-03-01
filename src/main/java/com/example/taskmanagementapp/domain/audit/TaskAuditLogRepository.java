package com.example.taskmanagementapp.domain.audit;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TaskAuditLogRepository extends JpaRepository<TaskAuditLog, Long> {
    List<TaskAuditLog> findByTaskIdOrderByOccurredAtDesc(Long taskId);
}
