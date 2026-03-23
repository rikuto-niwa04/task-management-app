package com.example.taskmanagementapp.domain.task;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String title;

    @Column(columnDefinition = "text")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TaskStatus status;

    private LocalDate dueDate;

    private Long assigneeId; // NULL可

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public static Task create(String title, String description, LocalDate dueDate, Long assigneeId) {
        Task task = new Task();
        task.setTitle(title);
        task.setDescription(description);
        task.setDueDate(dueDate);
        task.setAssigneeId(assigneeId);
        task.status = TaskStatus.TODO;
        return task;
    }

    @PrePersist
    public void prePersist() {
        var now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
        if (this.status == null) {
            this.status = TaskStatus.TODO;
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // --- getters/setters ---
    public Long getId() { return id; }

    public String getTitle() { return title; }

    public void setTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("title must not be blank");
        }
        this.title = title;
    }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    // 仕様：状態遷移はapply()メソッドでのみ変更可能（外部からの直接setStatus()は不可）
    public TaskStatus getStatus() { return status; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public Long getAssigneeId() { return assigneeId; }
    public void setAssigneeId(Long assigneeId) { this.assigneeId = assigneeId; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    // テスト用：status初期化（同一パッケージからのみ利用可）
    void setStatusForTest(TaskStatus status) {
        this.status = status;
    }

    // 状態遷移のルールはEntity内に実装する
    public void apply(TaskOperation op) {
        TaskStatus target = TaskStatusTransition.targetStatus(this.status, op);

        if (!TaskStatusTransition.canTransition(this.status, target)) {
            throw new IllegalStateException("Invalid transition: " + this.status + " -> " + target + " (op=" + op + ")");
        }

        this.status = target;
    }
}