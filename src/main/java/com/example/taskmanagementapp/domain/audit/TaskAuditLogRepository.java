package com.example.taskmanagementapp.domain.audit;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

//責務：タスクの監査ログを保存・検索するためのリポジトリ
public interface TaskAuditLogRepository extends JpaRepository<TaskAuditLog, Long> {
    List<TaskAuditLog> findByTaskIdOrderByOccurredAtDesc(Long taskId);
}
