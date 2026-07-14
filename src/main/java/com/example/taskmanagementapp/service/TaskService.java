package com.example.taskmanagementapp.service;

import com.example.taskmanagementapp.domain.audit.AuditEventType;
import com.example.taskmanagementapp.domain.audit.AuditLog;
import com.example.taskmanagementapp.domain.audit.AuditLogRepository;
import com.example.taskmanagementapp.domain.task.*;
import com.example.taskmanagementapp.web.form.TaskCreateForm;
import com.example.taskmanagementapp.web.form.TaskUpdateForm;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.taskmanagementapp.web.form.TaskSearchForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.domain.Sort;
import java.util.List;

//責務：タスク関連のビジネスロジック全般
@Service
// TaskServiceは、Taskの作成・更新・状態変更・削除をまとめて扱い、
// 仕様どおりの業務ルール（作成時TODO固定、許可遷移のみなど）を強制し、
// 保存と同時に監査ログ（CREATE/UPDATE/STATUS_CHANGE/DELETE）を残す層です。
public class TaskService {

    private final TaskRepository taskRepository;
    private final AuditLogRepository auditLogRepository;

    public TaskService(TaskRepository taskRepository, AuditLogRepository auditLogRepository) {
        this.taskRepository = taskRepository;
        this.auditLogRepository = auditLogRepository;
    }

    //責務：一覧表示用の取得（読み取り専用）
    @Transactional(readOnly = true)
    public Page<Task> search(TaskSearchForm form, int page, int size, String sort) {
        Specification<Task> spec = (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
        if (form.getTitle() != null && !form.getTitle().isBlank()) {
            spec = spec.and(TaskSpecification.titleContains(form.getTitle()));
        }

        if (form.getStatus() != null) {
            spec = spec.and(TaskSpecification.hasStatus(form.getStatus()));
        }

        if (form.getAssigneeId() != null) {
            spec = spec.and(TaskSpecification.hasAssignee(form.getAssigneeId()));
        }

        String[] parts = sort.split(",");

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.fromString(parts[1]), parts[0])
        );

        return taskRepository.findAll(spec, pageable);
    }
    

    //責務：IDでTask取得＋存在しない場合の例外（読み取り専用）
    @Transactional(readOnly = true)
    public Task getOrThrow(Long id) {
        return taskRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Task not found: " + id));
    }

    //責務：履歴表示用の取得処理（読み取り専用）
    @Transactional(readOnly = true)
    public List<AuditLog> auditLogs(Long taskId) {
        return auditLogRepository.findByTaskIdOrderByOccurredAtDesc(taskId);
    }

    //責務：タスク作成＋Todo保存＋CREATEログ記録
    @Transactional
    public Task create(TaskCreateForm form, String actor) {
        Task t = Task.create(
        form.getTitle(),
        form.getDescription(),
        form.getDueDate(),
        form.getAssigneeId()
        );

        Task saved = taskRepository.save(t);

        auditLogRepository.save(AuditLog.of(
                saved.getId(),
                AuditEventType.CREATE,
                actor,
                "created task"
        ));
        return saved;
    }

    //責務：タスク更新＋UPDATEログ記録
    //現状、title必須（空白禁止）のサーバ側保証はここには見えない
    @Transactional
    public Task updateFields(Long id, TaskUpdateForm form, String actor, Long loginUserId, String role) {
        Task t = getOrThrow(id);
        validatePermission(t, loginUserId, role);

        // 仕様：DONEでも title/description/due_date/assignee_id は編集可
        t.setTitle(form.getTitle());
        t.setDescription(form.getDescription());
        t.setDueDate(form.getDueDate());
        t.setAssigneeId(form.getAssigneeId());

        Task saved = taskRepository.save(t);

        auditLogRepository.save(AuditLog.of(
                saved.getId(),
                AuditEventType.UPDATE,
                actor,
                "updated fields"
        ));
        return saved;
    }

    // 責務：タスク状態変更＋STATUS_CHANGEログ記録
    @Transactional
    public Task changeStatus(Long id, TaskOperation operation, String actor, Long loginUserId, String role) {
        Task task = getOrThrow(id);

        validatePermission(task, loginUserId, role);

        TaskStatus before = task.getStatus();

        task.changeStatus(operation);

        TaskStatus after = task.getStatus();

        Task saved = taskRepository.save(task);

        auditLogRepository.save(AuditLog.of(
                saved.getId(),
                AuditEventType.STATUS_CHANGE,
                actor,
                "status " + before + " -> " + after + " by " + operation
        ));

        return saved;
    }

    //責務：タスク削除＋DELETEログ記録
    @Transactional
    public void delete(Long id, String actor, Long loginUserId, String role) {
        Task t = getOrThrow(id);

        validatePermission(t, loginUserId, role);


        auditLogRepository.save(AuditLog.of(
                t.getId(),
                AuditEventType.DELETE,
                actor,
                "deleted task"
        ));

        // 仕様：物理削除
        taskRepository.delete(t);
    }

    public List<Task> findByStatus(TaskStatus status) {
    return taskRepository.findByStatus(status);
    }

    private void validatePermission(Task task, Long loginUserId, String role) {
        if ("ADMIN".equals(role)) {
            return;
        }

        if ("USER".equals(role)) {
            if (task.getAssigneeId() != null && task.getAssigneeId().equals(loginUserId)) {
                return;
            }
        }

        throw new IllegalStateException("You do not have permission to access this task.");
    }

    public List<Task> findByAssigneeId(Long assigneeId) {
    return taskRepository.findByAssigneeId(assigneeId);
    }

    public List<Task> findByStatusAndAssigneeId(TaskStatus status, Long assigneeId) {
        return taskRepository.findByStatusAndAssigneeId(status, assigneeId);
    }
}
