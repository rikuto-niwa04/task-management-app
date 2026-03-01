package com.example.taskmanagementapp.domain.audit;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "task_audit_logs")
public class TaskAuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // tasks削除後も残したいので、FKは張らず “参照ID” として保持
    @Column(nullable = false)
    private Long taskId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AuditEventType eventType;

    // 今は users テーブル未実装なので username を文字列で保持（後で userId に差し替え可）
    @Column(nullable = false, length = 120)
    private String actor;

    @Column(columnDefinition = "text")
    private String detail;

    @Column(nullable = false)
    private LocalDateTime occurredAt;

    @PrePersist
    public void prePersist() {
        this.occurredAt = LocalDateTime.now();
    }

    // --- factory ---
    public static TaskAuditLog of(Long taskId, AuditEventType type, String actor, String detail) {
        TaskAuditLog log = new TaskAuditLog();
        log.taskId = taskId;
        log.eventType = type;
        log.actor = actor;
        log.detail = detail;
        return log;
    }

    // --- getters ---
    public Long getId() { return id; }
    public Long getTaskId() { return taskId; }
    public AuditEventType getEventType() { return eventType; }
    public String getActor() { return actor; }
    public String getDetail() { return detail; }
    public LocalDateTime getOccurredAt() { return occurredAt; }
}
