package com.example.taskmanagementapp.service;

import com.example.taskmanagementapp.domain.audit.AuditEventType;
import com.example.taskmanagementapp.domain.audit.AuditLog;
import com.example.taskmanagementapp.domain.audit.AuditLogRepository;
import com.example.taskmanagementapp.domain.task.Task;
import com.example.taskmanagementapp.domain.task.TaskOperation;
import com.example.taskmanagementapp.domain.task.TaskRepository;
import com.example.taskmanagementapp.domain.task.TaskStatus;
import com.example.taskmanagementapp.web.form.TaskCreateForm;
import com.example.taskmanagementapp.web.form.TaskUpdateForm;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class TaskServiceTest {

    private TaskRepository taskRepository;
    private AuditLogRepository auditLogRepository;
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        // 本物のDBには接続せず、Repositoryの偽物（モック）を作成
        taskRepository = mock(TaskRepository.class);
        auditLogRepository = mock(AuditLogRepository.class);

        // モックを使って、テスト対象のTaskServiceを生成
        taskService = new TaskService(
                taskRepository,
                auditLogRepository
        );
    }

    // =========================================================
    // getOrThrow
    // =========================================================

    @Test
    void getOrThrow_taskが存在しない場合は例外になる() {
        // given：ID 999のタスクは存在しない
        when(taskRepository.findById(999L))
                .thenReturn(Optional.empty());

        // when & then：EntityNotFoundExceptionが発生する
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> taskService.getOrThrow(999L)
        );

        assertEquals(
                "Task not found: 999",
                exception.getMessage()
        );

        verify(taskRepository, times(1))
                .findById(999L);

        verifyNoInteractions(auditLogRepository);
    }

    // =========================================================
    // create
    // =========================================================

    @Test
    void create_タスクを保存してCREATE監査ログを記録する() {
        // given：新規作成フォーム
        TaskCreateForm form = new TaskCreateForm();
        form.setTitle("テストタスク");
        form.setDescription("テスト用の説明");
        form.setDueDate(LocalDate.now().plusDays(7));
        form.setAssigneeId(10L);

        /*
         * taskRepository.save()が呼ばれたら、
         * 渡されたTaskに仮のIDを設定して、そのまま返す。
         */
        when(taskRepository.save(any(Task.class)))
                .thenAnswer(invocation -> {
                    Task task = invocation.getArgument(0);
                    setIdByReflection(task, 1L);
                    return task;
                });

        // when
        Task saved = taskService.create(form, "userA");

        // then：Taskが1回保存されている
        ArgumentCaptor<Task> taskCaptor =
                ArgumentCaptor.forClass(Task.class);

        verify(taskRepository, times(1))
                .save(taskCaptor.capture());

        Task createdTask = taskCaptor.getValue();

        assertEquals("テストタスク", createdTask.getTitle());
        assertEquals("テスト用の説明", createdTask.getDescription());
        assertEquals(10L, createdTask.getAssigneeId());

        // 作成時のstatusは必ずTODO
        assertEquals(TaskStatus.TODO, createdTask.getStatus());

        // 戻り値にも仮IDが設定されている
        assertEquals(1L, saved.getId());

        // CREATE監査ログが保存されている
        ArgumentCaptor<AuditLog> auditCaptor =
                ArgumentCaptor.forClass(AuditLog.class);

        verify(auditLogRepository, times(1))
                .save(auditCaptor.capture());

        AuditLog auditLog = auditCaptor.getValue();

        assertNotNull(auditLog);
        assertEquals(
                AuditEventType.CREATE,
                auditLog.getEventType()
        );
    }

    // =========================================================
    // updateFields
    // =========================================================

    @Test
    void updateFields_ADMINは他人のタスクを更新できる() {
        // given
        Task existing = new Task();
        existing.setTitle("更新前");
        existing.setDescription("更新前の説明");
        existing.setAssigneeId(20L);

        when(taskRepository.findById(1L))
                .thenReturn(Optional.of(existing));

        when(taskRepository.save(any(Task.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        TaskUpdateForm form = new TaskUpdateForm();
        form.setTitle("更新後");
        form.setDescription("更新後の説明");
        form.setDueDate(LocalDate.now().plusDays(5));
        form.setAssigneeId(20L);

        // when
        Task saved = taskService.updateFields(
                1L,
                form,
                "adminA",
                999L,
                "ADMIN"
        );

        // then
        assertEquals("更新後", saved.getTitle());
        assertEquals("更新後の説明", saved.getDescription());
        assertEquals(20L, saved.getAssigneeId());
        assertEquals(form.getDueDate(), saved.getDueDate());

        verify(taskRepository, times(1))
                .save(existing);

        ArgumentCaptor<AuditLog> auditCaptor =
                ArgumentCaptor.forClass(AuditLog.class);

        verify(auditLogRepository, times(1))
                .save(auditCaptor.capture());

        assertEquals(
                AuditEventType.UPDATE,
                auditCaptor.getValue().getEventType()
        );
    }

    @Test
    void updateFields_USERは自分のタスクを更新できる() {
        // given：担当者IDとログインユーザーIDが同じ
        Task existing = new Task();
        existing.setTitle("更新前");
        existing.setAssigneeId(10L);

        when(taskRepository.findById(1L))
                .thenReturn(Optional.of(existing));

        when(taskRepository.save(any(Task.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        TaskUpdateForm form = new TaskUpdateForm();
        form.setTitle("更新後");
        form.setDescription("USERによる更新");
        form.setDueDate(LocalDate.now().plusDays(3));
        form.setAssigneeId(10L);

        // when
        Task saved = taskService.updateFields(
                1L,
                form,
                "userA",
                10L,
                "USER"
        );

        // then
        assertEquals("更新後", saved.getTitle());
        assertEquals("USERによる更新", saved.getDescription());
        assertEquals(10L, saved.getAssigneeId());

        verify(taskRepository, times(1))
                .save(existing);

        verify(auditLogRepository, times(1))
                .save(any(AuditLog.class));
    }

    @Test
    void updateFields_USERは他人のタスクを更新できない() {
        // given：担当者IDとログインユーザーIDが異なる
        Task existing = new Task();
        existing.setTitle("更新前");
        existing.setAssigneeId(20L);

        when(taskRepository.findById(1L))
                .thenReturn(Optional.of(existing));

        TaskUpdateForm form = new TaskUpdateForm();
        form.setTitle("不正な更新");
        form.setDescription("更新されない");
        form.setDueDate(LocalDate.now().plusDays(3));
        form.setAssigneeId(20L);

        // when & then
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> taskService.updateFields(
                        1L,
                        form,
                        "userA",
                        10L,
                        "USER"
                )
        );

        assertEquals(
                "You do not have permission to access this task.",
                exception.getMessage()
        );

        // 権限エラーなので、Taskも監査ログも保存されない
        verify(taskRepository, never())
                .save(any(Task.class));

        verify(auditLogRepository, never())
                .save(any(AuditLog.class));
    }

    // =========================================================
    // changeStatus
    // =========================================================

    @Test
    void changeStatus_正常な状態変更ではタスクと監査ログを保存する() {
        // given
        Task task = mock(Task.class);

        /*
         * changeStatus実行前はTODO、
         * changeStatus実行後はIN_PROGRESSとして返す。
         */
        when(task.getStatus())
                .thenReturn(
                        TaskStatus.TODO,
                        TaskStatus.IN_PROGRESS
                );

        when(taskRepository.findById(1L))
                .thenReturn(Optional.of(task));

        when(taskRepository.save(any(Task.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // when：TODOからIN_PROGRESSへの着手操作
        taskService.changeStatus(
                1L,
                TaskOperation.START,
                "adminA",
                999L,
                "ADMIN"
        );

        // then：Task.changeStatus()が呼ばれている
        verify(task, times(1))
                .changeStatus(TaskOperation.START);

        verify(taskRepository, times(1))
                .save(task);

        ArgumentCaptor<AuditLog> auditCaptor =
                ArgumentCaptor.forClass(AuditLog.class);

        verify(auditLogRepository, times(1))
                .save(auditCaptor.capture());

        assertEquals(
                AuditEventType.STATUS_CHANGE,
                auditCaptor.getValue().getEventType()
        );
    }

    @Test
    void changeStatus_不正な状態変更では例外になり保存しない() {
        // given：実物Taskをspy化
        Task realTask = new Task();
        Task task = spy(realTask);

        when(taskRepository.findById(1L))
                .thenReturn(Optional.of(task));

        /*
         * changeStatus(COMPLETE)が呼ばれたら、
         * 不正な状態遷移として例外を発生させる。
         */
        doThrow(new IllegalStateException("Invalid transition"))
                .when(task)
                .changeStatus(TaskOperation.COMPLETE);

        // when & then
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> taskService.changeStatus(
                        1L,
                        TaskOperation.COMPLETE,
                        "adminA",
                        999L,
                        "ADMIN"
                )
        );

        assertEquals(
                "Invalid transition",
                exception.getMessage()
        );

        // 状態変更に失敗したため保存されない
        verify(taskRepository, never())
                .save(any(Task.class));

        verify(auditLogRepository, never())
                .save(any(AuditLog.class));
    }

    @Test
    void changeStatus_USERは自分のタスクを操作できる() {
        // given
        Task task = mock(Task.class);

        when(task.getAssigneeId())
                .thenReturn(10L);

        when(task.getStatus())
                .thenReturn(
                        TaskStatus.TODO,
                        TaskStatus.IN_PROGRESS
                );

        when(taskRepository.findById(1L))
                .thenReturn(Optional.of(task));

        when(taskRepository.save(any(Task.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // when
        taskService.changeStatus(
                1L,
                TaskOperation.START,
                "userA",
                10L,
                "USER"
        );

        // then
        verify(task, times(1))
                .changeStatus(TaskOperation.START);

        verify(taskRepository, times(1))
                .save(task);

        verify(auditLogRepository, times(1))
                .save(any(AuditLog.class));
    }

    @Test
    void changeStatus_USERは他人のタスクを操作できない() {
        // given
        Task task = mock(Task.class);

        when(task.getAssigneeId())
                .thenReturn(20L);

        when(taskRepository.findById(1L))
                .thenReturn(Optional.of(task));

        // when & then
        assertThrows(
                IllegalStateException.class,
                () -> taskService.changeStatus(
                        1L,
                        TaskOperation.START,
                        "userA",
                        10L,
                        "USER"
                )
        );

        // 権限チェックで止まるので状態変更されない
        verify(task, never())
                .changeStatus(any(TaskOperation.class));

        verify(taskRepository, never())
                .save(any(Task.class));

        verify(auditLogRepository, never())
                .save(any(AuditLog.class));
    }

    @Test
    void changeStatus_ADMINは他人のタスクを操作できる() {
        // given
        Task task = mock(Task.class);

        when(task.getAssigneeId())
                .thenReturn(20L);

        when(task.getStatus())
                .thenReturn(
                        TaskStatus.TODO,
                        TaskStatus.IN_PROGRESS
                );

        when(taskRepository.findById(1L))
                .thenReturn(Optional.of(task));

        when(taskRepository.save(any(Task.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // when
        taskService.changeStatus(
                1L,
                TaskOperation.START,
                "adminA",
                999L,
                "ADMIN"
        );

        // then
        verify(task, times(1))
                .changeStatus(TaskOperation.START);

        verify(taskRepository, times(1))
                .save(task);

        verify(auditLogRepository, times(1))
                .save(any(AuditLog.class));
    }

    // =========================================================
    // delete
    // =========================================================

    @Test
    void delete_ADMINは監査ログを記録してからタスクを削除する() {
        // given
        Task task = new Task();
        task.setAssigneeId(20L);
        setIdByReflection(task, 1L);

        when(taskRepository.findById(1L))
                .thenReturn(Optional.of(task));

        // when
        taskService.delete(
                1L,
                "adminA",
                999L,
                "ADMIN"
        );

        /*
         * DELETE監査ログを保存した後に、
         * Taskが物理削除されることを確認する。
         */
        InOrder order = inOrder(
                auditLogRepository,
                taskRepository
        );

        order.verify(auditLogRepository)
                .save(any(AuditLog.class));

        order.verify(taskRepository)
                .delete(task);

        ArgumentCaptor<AuditLog> auditCaptor =
                ArgumentCaptor.forClass(AuditLog.class);

        verify(auditLogRepository)
                .save(auditCaptor.capture());

        assertEquals(
                AuditEventType.DELETE,
                auditCaptor.getValue().getEventType()
        );
    }

    @Test
    void delete_USERは自分のタスクを削除できる() {
        // given
        Task task = new Task();
        task.setAssigneeId(10L);
        setIdByReflection(task, 1L);

        when(taskRepository.findById(1L))
                .thenReturn(Optional.of(task));

        // when
        taskService.delete(
                1L,
                "userA",
                10L,
                "USER"
        );

        // then
        verify(auditLogRepository, times(1))
                .save(any(AuditLog.class));

        verify(taskRepository, times(1))
                .delete(task);
    }

    @Test
    void delete_USERは他人のタスクを削除できない() {
        // given
        Task task = new Task();
        task.setAssigneeId(20L);
        setIdByReflection(task, 1L);

        when(taskRepository.findById(1L))
                .thenReturn(Optional.of(task));

        // when & then
        assertThrows(
                IllegalStateException.class,
                () -> taskService.delete(
                        1L,
                        "userA",
                        10L,
                        "USER"
                )
        );

        // 権限エラーなので監査ログも削除処理も実行されない
        verify(auditLogRepository, never())
                .save(any(AuditLog.class));

        verify(taskRepository, never())
                .delete(any(Task.class));
    }

    // =========================================================
    // テスト補助メソッド
    // =========================================================

    /*
     * Task.idには通常setterがないため、
     * テスト内だけリフレクションで仮IDを設定する。
     */
    private static void setIdByReflection(Task task, Long id) {
        try {
            java.lang.reflect.Field field =
                    Task.class.getDeclaredField("id");

            field.setAccessible(true);
            field.set(task, id);

        } catch (NoSuchFieldException | IllegalAccessException exception) {
            throw new RuntimeException(exception);
        }
    }
}