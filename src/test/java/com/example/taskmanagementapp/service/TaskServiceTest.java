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

import java.time.LocalDate;
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
        when(taskRepository.findById(999L)).thenReturn(Optional.empty());

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
        Task existing = new Task();

        when(taskRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TaskUpdateForm form = new TaskUpdateForm();
        form.setTitle("new title");
        form.setDescription("new desc");
        form.setDueDate(LocalDate.now().plusDays(7));
        form.setAssigneeId(20L);

        Task saved = taskService.updateFields(1L, form, "tester", 1L, "ADMIN");

        verify(taskRepository, times(1)).save(existing);
        verify(auditLogRepository, times(1)).save(any(TaskAuditLog.class));
        assertEquals("new title", saved.getTitle());
        assertEquals("new desc", saved.getDescription());
        assertEquals(20L, saved.getAssigneeId());
    }

    @Test
    void operate_validTransition_shouldSaveTask_andWriteStatusChangeAudit() {
        Task t = mock(Task.class);

        when(t.getStatus()).thenReturn(TaskStatus.IN_PROGRESS, TaskStatus.DONE);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(t));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        taskService.operate(1L, TaskOperation.START, "tester", 1L, "ADMIN");

        verify(t, times(1)).apply(TaskOperation.START);
        verify(taskRepository, times(1)).save(t);
        verify(auditLogRepository, times(1)).save(any(TaskAuditLog.class));
    }

    @Test
    void operate_invalidTransition_shouldThrow_andNotSaveNorAudit() {
        Task real = new Task();
        Task t = spy(real);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(t));
        doThrow(new IllegalStateException("Invalid transition")).when(t).apply(TaskOperation.COMPLETE);

        assertThrows(IllegalStateException.class, () ->
                taskService.operate(1L, TaskOperation.COMPLETE, "actorD", 1L, "ADMIN")
        );

        verify(taskRepository, never()).save(any(Task.class));
        verify(auditLogRepository, never()).save(any(TaskAuditLog.class));
    }

    @Test
    void delete_shouldWriteDeleteAudit_thenDeleteTask() {
        Task t = new Task();
        setIdByReflection(t, 1L);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(t));

        taskService.delete(1L, "tester", 1L, "ADMIN");

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

    @Test
    void updateFields_userCanUpdateOwnTask() {
        // given
        Task existing = new Task();
        existing.setTitle("old title");
        existing.setAssigneeId(10L);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TaskUpdateForm form = new TaskUpdateForm();
        form.setTitle("new title");
        form.setDescription("new desc");
        form.setDueDate(java.time.LocalDate.now().plusDays(3));
        form.setAssigneeId(10L);

        // when
        Task saved = taskService.updateFields(1L, form, "userA", 10L, "USER");

        // then
        assertEquals("new title", saved.getTitle());
        assertEquals("new desc", saved.getDescription());
        assertEquals(10L, saved.getAssigneeId());

        verify(taskRepository, times(1)).save(existing);
        verify(auditLogRepository, times(1)).save(any(TaskAuditLog.class));
    }

    @Test
    void updateFields_userCannotUpdateOthersTask() {
        // given
        Task existing = new Task();
        existing.setTitle("old title");
        existing.setAssigneeId(20L); // 自分ではない担当者

        when(taskRepository.findById(1L)).thenReturn(Optional.of(existing));

        TaskUpdateForm form = new TaskUpdateForm();
        form.setTitle("new title");
        form.setDescription("new desc");
        form.setDueDate(java.time.LocalDate.now().plusDays(3));
        form.setAssigneeId(20L);

        // when & then
        IllegalStateException ex = assertThrows(IllegalStateException.class, () ->
                taskService.updateFields(1L, form, "userA", 10L, "USER")
        );

        assertEquals("You do not have permission to access this task.", ex.getMessage());
        verify(taskRepository, never()).save(any(Task.class));
        verify(auditLogRepository, never()).save(any(TaskAuditLog.class));
    }

    @Test
    void updateFields_adminCanUpdateOthersTask() {
        // given
        Task existing = new Task();
        existing.setTitle("old title");
        existing.setAssigneeId(20L); // 他人のタスク

        when(taskRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TaskUpdateForm form = new TaskUpdateForm();
        form.setTitle("admin updated");
        form.setDescription("updated by admin");
        form.setDueDate(java.time.LocalDate.now().plusDays(5));
        form.setAssigneeId(20L);

        // when
        Task saved = taskService.updateFields(1L, form, "adminA", 999L, "ADMIN");

        // then
        assertEquals("admin updated", saved.getTitle());
        assertEquals("updated by admin", saved.getDescription());
        assertEquals(20L, saved.getAssigneeId());

        verify(taskRepository, times(1)).save(existing);
        verify(auditLogRepository, times(1)).save(any(TaskAuditLog.class));
    }

    //① operate：USERが自分のタスク → OK
    @Test
    void operate_userCanOperateOwnTask() {
        // given
        Task t = mock(Task.class);

        when(t.getAssigneeId()).thenReturn(10L);
        when(t.getStatus()).thenReturn(TaskStatus.TODO, TaskStatus.IN_PROGRESS);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(t));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        taskService.operate(1L, TaskOperation.START, "userA", 10L, "USER");

        // then
        verify(t, times(1)).apply(TaskOperation.START);
        verify(taskRepository, times(1)).save(t);
        verify(auditLogRepository, times(1)).save(any(TaskAuditLog.class));
    }

    //② operate：USERが他人のタスク → 例外
    @Test
    void operate_userCannotOperateOthersTask() {
        // given
        Task t = mock(Task.class);

        when(t.getAssigneeId()).thenReturn(20L); // 自分じゃない
        when(taskRepository.findById(1L)).thenReturn(Optional.of(t));

        // when & then
        assertThrows(IllegalStateException.class, () ->
                taskService.operate(1L, TaskOperation.START, "userA", 10L, "USER")
        );

        verify(t, never()).apply(any());
        verify(taskRepository, never()).save(any());
        verify(auditLogRepository, never()).save(any());
    }

    //③ operate：ADMINが他人のタスク → OK
    @Test
    void operate_adminCanOperateOthersTask() {
        // given
        Task t = mock(Task.class);

        when(t.getAssigneeId()).thenReturn(20L);
        when(t.getStatus()).thenReturn(TaskStatus.TODO, TaskStatus.IN_PROGRESS);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(t));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        taskService.operate(1L, TaskOperation.START, "adminA", 999L, "ADMIN");

        // then
        verify(t, times(1)).apply(TaskOperation.START);
        verify(taskRepository, times(1)).save(t);
        verify(auditLogRepository, times(1)).save(any(TaskAuditLog.class));
    }

    //④ delete：USERが自分のタスク → OK
    @Test
    void delete_userCanDeleteOwnTask() {
        // given
        Task t = new Task();
        t.setAssigneeId(10L);
        setIdByReflection(t, 1L);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(t));

        // when
        taskService.delete(1L, "userA", 10L, "USER");

        // then
        verify(auditLogRepository, times(1)).save(any(TaskAuditLog.class));
        verify(taskRepository, times(1)).delete(t);
    }

    //⑤ delete：USERが他人のタスク → 例外
    @Test
    void delete_userCannotDeleteOthersTask() {
        // given
        Task t = new Task();
        t.setAssigneeId(20L); // 自分じゃない

        when(taskRepository.findById(1L)).thenReturn(Optional.of(t));

        // when & then
        assertThrows(IllegalStateException.class, () ->
                taskService.delete(1L, "userA", 10L, "USER")
        );

        verify(taskRepository, never()).delete(any());
        verify(auditLogRepository, never()).save(any());
    }

    //⑥ delete：ADMINが他人のタスク → OK
    @Test
    void delete_adminCanDeleteOthersTask() {
        // given
        Task t = new Task();
        t.setAssigneeId(20L);
        setIdByReflection(t, 1L);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(t));

        // when
        taskService.delete(1L, "adminA", 999L, "ADMIN");

        // then
        verify(auditLogRepository, times(1)).save(any(TaskAuditLog.class));
        verify(taskRepository, times(1)).delete(t);
    }
}