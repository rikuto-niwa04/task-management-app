package com.example.taskmanagementapp.domain.task;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
public class Task {

    // 仕様：TaskはID、タイトル、説明、状態、期限日、担当者ID、作成日時、更新日時を持つ
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

    //自分用：staticにしてインスタンスを生成できなくしておく。仕様違反防止
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
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
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
            throw new IllegalStateException("この状態遷移は不正です: " + this.status + " -> " + target + " (op=" + op + ")");
        }

        this.status = target;
    }

    public void changeStatus(TaskOperation operation) {
        if (operation == null) {
            throw new IllegalArgumentException("Operation is required");
        }

        switch (operation) {
            case START -> start();
            case COMPLETE -> complete();
            case REVERT -> returnToTodo();
            case REOPEN -> reopen();
        }
    }

        private void start() {
        if (this.status != TaskStatus.TODO) {
            throw new IllegalStateException(
                    "TODOのタスクのみ着手できます"
            );
        }

        this.status = TaskStatus.IN_PROGRESS;
    }

    private void complete() {
        if (this.status != TaskStatus.IN_PROGRESS) {
            throw new IllegalStateException(
                    "IN_PROGRESSのタスクのみ完了できます"
            );
        }

        this.status = TaskStatus.DONE;
    }

    private void returnToTodo() {
        if (this.status != TaskStatus.IN_PROGRESS) {
            throw new IllegalStateException(
                    "IN_PROGRESSのタスクのみ差し戻しできます"
            );
        }

        this.status = TaskStatus.TODO;
    }

    private void reopen() {
        if (this.status != TaskStatus.DONE) {
            throw new IllegalStateException(
                    "DONEのタスクのみ再開できます"
            );
        }

        this.status = TaskStatus.IN_PROGRESS;
    }
}