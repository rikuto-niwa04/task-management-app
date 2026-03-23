package com.example.taskmanagementapp.service;

import com.example.taskmanagementapp.domain.audit.AuditEventType;
import com.example.taskmanagementapp.domain.audit.TaskAuditLog;
import com.example.taskmanagementapp.domain.audit.TaskAuditLogRepository;
import com.example.taskmanagementapp.domain.task.*;
import com.example.taskmanagementapp.web.form.TaskCreateForm;
import com.example.taskmanagementapp.web.form.TaskUpdateForm;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

//責務：タスク関連のビジネスロジック全般
@Service
// TaskServiceは、Taskの作成・更新・状態変更・削除をまとめて扱い、
// 仕様どおりの業務ルール（作成時TODO固定、許可遷移のみなど）を強制し、
// 保存と同時に監査ログ（CREATE/UPDATE/STATUS_CHANGE/DELETE）を残す層です。
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskAuditLogRepository auditLogRepository;

    public TaskService(TaskRepository taskRepository, TaskAuditLogRepository auditLogRepository) {
        this.taskRepository = taskRepository;
        this.auditLogRepository = auditLogRepository;
    }

    //責務：一覧表示用の取得（読み取り専用）
    @Transactional(readOnly = true)
    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    //責務：IDでTask取得＋存在しない場合の例外（読み取り専用）
    @Transactional(readOnly = true)
    public Task getOrThrow(Long id) {
        return taskRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Task not found: " + id));
    }

    //責務：履歴表示用の取得処理（読み取り専用）
    @Transactional(readOnly = true)
    public List<TaskAuditLog> auditLogs(Long taskId) {
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

        auditLogRepository.save(TaskAuditLog.of(
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
    public Task updateFields(Long id, TaskUpdateForm form, String actor) {
        Task t = getOrThrow(id);

        // 仕様：DONEでも title/description/due_date/assignee_id は編集可
        t.setTitle(form.getTitle());
        t.setDescription(form.getDescription());
        t.setDueDate(form.getDueDate());
        t.setAssigneeId(form.getAssigneeId());

        Task saved = taskRepository.save(t);

        auditLogRepository.save(TaskAuditLog.of(
                saved.getId(),
                AuditEventType.UPDATE,
                actor,
                "updated fields"
        ));
        return saved;
    }

    //責務：タスクの状態遷移＋STATUS_CHANGEログ記録
    @Transactional
    public Task operate(Long id, TaskOperation op, String actor) {
        Task t = getOrThrow(id);

        TaskStatus before = t.getStatus();
        //サービスロジックでは、状態遷移のルールはTaskエンティティ内に実装されたapply()を呼び出すだけが理想的。これにより、状態遷移のルールはエンティティ内にカプセル化され、サービス層は単純に操作を指示するだけになる。
        t.apply(op);
        TaskStatus after = t.getStatus();
        Task saved = taskRepository.save(t);

        auditLogRepository.save(TaskAuditLog.of(
                saved.getId(),
                AuditEventType.STATUS_CHANGE,
                actor,
                "status " + before + " -> " + after + " by " + op
        ));
        return saved;
    }

    //責務：タスク削除＋DELETEログ記録
    @Transactional
    public void delete(Long id, String actor) {
        Task t = getOrThrow(id);

        auditLogRepository.save(TaskAuditLog.of(
                t.getId(),
                AuditEventType.DELETE,
                actor,
                "deleted task"
        ));

        // 仕様：物理削除
        taskRepository.delete(t);
    }
}
