package com.example.taskmanagementapp.service;

import com.example.taskmanagementapp.domain.audit.AuditEventType;
import com.example.taskmanagementapp.domain.audit.TaskAuditLog;
import com.example.taskmanagementapp.domain.audit.TaskAuditLogRepository;
import com.example.taskmanagementapp.domain.task.*;
import com.example.taskmanagementapp.web.form.TaskCreateForm;
import com.example.taskmanagementapp.web.form.TaskUpdateForm;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TaskServiceTest {

    private TaskRepository taskRepository;
    private TaskAuditLogRepository auditLogRepository;
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskRepository = mock(TaskRepository.class);
        auditLogRepository = mock(TaskAuditLogRepository.class);
        taskService = new TaskService(taskRepository, auditLogRepository);
    }

    @Test
    void getOrThrow_notFound_shouldThrow() {
        // given
        when(taskRepository.findById(999L)).thenReturn(Optional.empty());

        // when & then
        assertThrows(EntityNotFoundException.class, () -> taskService.getOrThrow(999L));

        verify(taskRepository, times(1)).findById(999L);
        verifyNoInteractions(auditLogRepository);
    }

    @Test
    void create_shouldSaveTask_andWriteCreateAudit() {
        TaskCreateForm form = new TaskCreateForm();
        form.setTitle("t1");

        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> {
            Task arg = invocation.getArgument(0);
            setIdByReflection(arg, 1L);
            return arg;
        });

        taskService.create(form, "actorA");

        verify(taskRepository, times(1)).save(any(Task.class));
        ArgumentCaptor<TaskAuditLog> captor = ArgumentCaptor.forClass(TaskAuditLog.class);
        verify(auditLogRepository, times(1)).save(captor.capture());
        assertNotNull(captor.getValue());
        assertEquals(AuditEventType.CREATE, captor.getValue().getEventType());
    }

    @Test
    void updateFields_shouldSaveTask_andWriteUpdateAudit() {
        // given
        Task existing = new Task(); // DONEでも編集可の仕様は、Serviceではなくドメイン/仕様側の話なのでここは状態不要

        when(taskRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TaskUpdateForm form = new TaskUpdateForm();
        form.setTitle("new title");
        form.setDescription("new desc");
        form.setDueDate(java.time.LocalDate.now().plusDays(7));
        form.setAssigneeId(20L);

        // when
        Task saved = taskService.updateFields(1L, form, "actorB", 1L, "ADMIN");
        // then
        verify(taskRepository, times(1)).save(existing);
        verify(auditLogRepository, times(1)).save(any(TaskAuditLog.class));
        assertEquals("new title", saved.getTitle());
    }

    @Test
    void operate_validTransition_shouldSaveTask_andWriteStatusChangeAudit() {
        // given
        Task t = mock(Task.class); 

        // applyは何もしない（例外出さない）
        // getStatusは呼ばれても良いように固定値を返す
        when(t.getStatus()).thenReturn(TaskStatus.IN_PROGRESS, TaskStatus.DONE);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(t));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        taskService.operate(1L, TaskOperation.COMPLETE, "actorC", 1L, "ADMIN");
        // then
        verify(t, times(1)).apply(TaskOperation.COMPLETE);
        verify(taskRepository, times(1)).save(t);
        verify(auditLogRepository, times(1)).save(any());
    }

    // 状態遷移エラーのときは、タスクも監査ログも保存されないことを確認
    @Test
    void operate_invalidTransition_shouldThrow_andNotSaveNorAudit() {
        // given
        Task real = new Task();
        Task t = spy(real);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(t));
        doThrow(new IllegalStateException("Invalid transition")).when(t).apply(TaskOperation.COMPLETE);

        // when
        assertThrows(IllegalStateException.class, () ->
        taskService.operate(1L, TaskOperation.COMPLETE, "actorD", 1L, "ADMIN"));

        // then
        verify(taskRepository, never()).save(any(Task.class));
        verify(auditLogRepository, never()).save(any(TaskAuditLog.class));
    }

    @Test
    void delete_shouldWriteDeleteAudit_thenDeleteTask() {
        // given
        Task t = new Task();
        when(taskRepository.findById(1L)).thenReturn(Optional.of(t));

        // when
        taskService.delete(1L, "actorE", 1L, "ADMIN");
        // then（順序まで見るなら InOrder）
        var inOrder = inOrder(auditLogRepository, taskRepository);
        inOrder.verify(auditLogRepository).save(any(TaskAuditLog.class));
        inOrder.verify(taskRepository).delete(t);

        ArgumentCaptor<TaskAuditLog> captor = ArgumentCaptor.forClass(TaskAuditLog.class);
        verify(auditLogRepository).save(captor.capture());
        assertEquals(AuditEventType.DELETE, captor.getValue().getEventType());
    }

    private static void setIdByReflection(Task task, Long id) {
    try {
        java.lang.reflect.Field f = Task.class.getDeclaredField("id");
        f.setAccessible(true);
        f.set(task, id);
    } catch (NoSuchFieldException | IllegalAccessException e) {
        throw new RuntimeException(e);
    }
}
}