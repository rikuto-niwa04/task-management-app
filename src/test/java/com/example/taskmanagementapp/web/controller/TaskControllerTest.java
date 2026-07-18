package com.example.taskmanagementapp.web.controller;

import com.example.taskmanagementapp.domain.audit.AuditLog;
import com.example.taskmanagementapp.domain.task.Task;
import com.example.taskmanagementapp.domain.task.TaskOperation;
import com.example.taskmanagementapp.domain.task.TaskStatus;
import com.example.taskmanagementapp.domain.user.User;
import com.example.taskmanagementapp.domain.user.UserRepository;
import com.example.taskmanagementapp.service.TaskService;
import com.example.taskmanagementapp.web.form.TaskCreateForm;
import com.example.taskmanagementapp.web.form.TaskSearchForm;
import com.example.taskmanagementapp.web.form.TaskUpdateForm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Answers.RETURNS_DEEP_STUBS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

class TaskControllerTest {

    private MockMvc mockMvc;

    private TaskService taskService;
    private UserRepository userRepository;
    private Authentication authentication;
    private User loginUser;

    @BeforeEach
    void setUp() {
        /*
         * Controllerが利用するServiceとRepositoryを、
         * 本物ではなくMockitoのモックにする。
         */
        taskService = mock(TaskService.class);
        userRepository = mock(UserRepository.class);
        authentication = mock(Authentication.class);

        /*
         * getRole().name()までモックできるように、
         * RETURNS_DEEP_STUBSを使用する。
         */
        loginUser = mock(User.class, RETURNS_DEEP_STUBS);

        TaskController taskController =
                new TaskController(taskService, userRepository);

        /*
         * TaskController単体をMockMvcに登録する。
         * Spring Boot全体やDBは起動しない。
         */
        mockMvc = MockMvcBuilders
                .standaloneSetup(taskController)
                .build();
    }

    // =========================================================
    // GET /tasks/new
    // =========================================================

    @Test
    void newForm_新規作成画面を表示する() throws Exception {
        mockMvc.perform(get("/tasks/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("tasks/new"))
                .andExpect(model().attributeExists("form"));
    }

    // =========================================================
    // GET /tasks
    // =========================================================

    @Test
    void list_タスク一覧画面を表示する() throws Exception {
        // given
        Task task = new Task();
        task.setTitle("一覧表示用タスク");

        Page<Task> taskPage =
                new PageImpl<>(List.of(task));

        when(taskService.search(
                any(TaskSearchForm.class),
                eq(0),
                eq(10),
                eq("dueDate,asc")
        )).thenReturn(taskPage);

        // when & then
        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(view().name("tasks/list"))
                .andExpect(model().attributeExists("tasks"))
                .andExpect(model().attributeExists("taskPage"))
                .andExpect(model().attributeExists("statuses"))
                .andExpect(model().attribute("sort", "dueDate,asc"));

        verify(taskService, times(1))
                .search(
                        any(TaskSearchForm.class),
                        eq(0),
                        eq(10),
                        eq("dueDate,asc")
                );
    }

    @Test
    void list_ページ番号とソート条件をServiceへ渡す() throws Exception {
        // given
        Page<Task> taskPage =
                new PageImpl<>(List.of());

        when(taskService.search(
                any(TaskSearchForm.class),
                eq(2),
                eq(10),
                eq("dueDate,desc")
        )).thenReturn(taskPage);

        // when & then
        mockMvc.perform(
                        get("/tasks")
                                .param("page", "2")
                                .param("sort", "dueDate,desc")
                )
                .andExpect(status().isOk())
                .andExpect(view().name("tasks/list"))
                .andExpect(model().attribute("sort", "dueDate,desc"));

        verify(taskService, times(1))
                .search(
                        any(TaskSearchForm.class),
                        eq(2),
                        eq(10),
                        eq("dueDate,desc")
                );
    }

    // =========================================================
    // GET /tasks/{id}/edit
    // =========================================================

    @Test
    void edit_編集画面を表示する() throws Exception {
        // given
        Task task = new Task();
        task.setTitle("編集前タイトル");
        task.setDescription("編集前の説明");
        task.setDueDate(LocalDate.of(2026, 7, 31));
        task.setAssigneeId(10L);

        when(taskService.getOrThrow(1L))
                .thenReturn(task);

        when(taskService.auditLogs(1L))
                .thenReturn(List.of());

        // when & then
        mockMvc.perform(get("/tasks/1/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("tasks/edit"))
                .andExpect(model().attribute("task", task))
                .andExpect(model().attributeExists("form"))
                .andExpect(model().attributeExists("auditLogs"));

        verify(taskService, times(1))
                .getOrThrow(1L);

        verify(taskService, times(1))
                .auditLogs(1L);
    }

    // =========================================================
    // POST /tasks
    // =========================================================

    @Test
    void create_正常な入力ならタスクを作成して一覧へリダイレクトする()
            throws Exception {

        // given
        prepareLoginUser(
                "userA",
                10L,
                "USER"
        );

        // when & then
        mockMvc.perform(
                        post("/tasks")
                                .principal(authentication)
                                .param("title", "新規タスク")
                                .param("description", "新規タスクの説明")
                                .param("dueDate", "2026-07-31")
                                .param("assigneeId", "10")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/tasks"));

        ArgumentCaptor<TaskCreateForm> formCaptor =
                ArgumentCaptor.forClass(TaskCreateForm.class);

        verify(taskService, times(1))
                .create(
                        formCaptor.capture(),
                        eq("userA")
                );

        TaskCreateForm capturedForm =
                formCaptor.getValue();

        assertEquals(
                "新規タスク",
                capturedForm.getTitle()
        );

        assertEquals(
                "新規タスクの説明",
                capturedForm.getDescription()
        );

        assertEquals(
                LocalDate.of(2026, 7, 31),
                capturedForm.getDueDate()
        );

        assertEquals(
                10L,
                capturedForm.getAssigneeId()
        );
    }

    @Test
    void create_タイトルが空白なら作成せず新規作成画面を再表示する()
            throws Exception {

        mockMvc.perform(
                        post("/tasks")
                                .principal(authentication)
                                .param("title", "   ")
                                .param("description", "説明")
                )
                .andExpect(status().isOk())
                .andExpect(view().name("tasks/new"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors(
                        "form",
                        "title"
                ));

        /*
         * バリデーションエラーでController内の処理が止まるため、
         * Service.create()は呼ばれない。
         */
        verify(taskService, never())
                .create(
                        any(TaskCreateForm.class),
                        any(String.class)
                );

        verify(userRepository, never())
                .findByUsername(any(String.class));
    }

    // =========================================================
    // POST /tasks/{id}
    // =========================================================

    @Test
    void update_正常な入力なら更新して編集画面へリダイレクトする()
            throws Exception {

        // given
        prepareLoginUser(
                "userA",
                10L,
                "USER"
        );

        // when & then
        mockMvc.perform(
                        post("/tasks/1")
                                .principal(authentication)
                                .param("title", "更新後タイトル")
                                .param("description", "更新後の説明")
                                .param("dueDate", "2026-08-10")
                                .param("assigneeId", "10")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/tasks/1/edit"));

        ArgumentCaptor<TaskUpdateForm> formCaptor =
                ArgumentCaptor.forClass(TaskUpdateForm.class);

        verify(taskService, times(1))
                .updateFields(
                        eq(1L),
                        formCaptor.capture(),
                        eq("userA"),
                        eq(10L),
                        eq("USER")
                );

        TaskUpdateForm capturedForm =
                formCaptor.getValue();

        assertEquals(
                "更新後タイトル",
                capturedForm.getTitle()
        );

        assertEquals(
                "更新後の説明",
                capturedForm.getDescription()
        );

        assertEquals(
                LocalDate.of(2026, 8, 10),
                capturedForm.getDueDate()
        );

        assertEquals(
                10L,
                capturedForm.getAssigneeId()
        );
    }

    @Test
    void update_タイトルが空白なら更新せず編集画面を再表示する()
            throws Exception {

        // given
        Task task = new Task();
        task.setTitle("更新前タイトル");

        when(taskService.getOrThrow(1L))
                .thenReturn(task);

        when(taskService.auditLogs(1L))
                .thenReturn(List.of());

        // when & then
        mockMvc.perform(
                        post("/tasks/1")
                                .principal(authentication)
                                .param("title", "   ")
                                .param("description", "説明")
                )
                .andExpect(status().isOk())
                .andExpect(view().name("tasks/edit"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors(
                        "form",
                        "title"
                ))
                .andExpect(model().attribute("task", task))
                .andExpect(model().attributeExists("auditLogs"));

        verify(taskService, never())
                .updateFields(
                        any(Long.class),
                        any(TaskUpdateForm.class),
                        any(String.class),
                        any(Long.class),
                        any(String.class)
                );
    }

    // =========================================================
    // POST /tasks/{id}/delete
    // =========================================================

    @Test
    void delete_ログインユーザー情報を渡して削除する()
            throws Exception {

        // given
        prepareLoginUser(
                "userA",
                10L,
                "USER"
        );

        // when & then
        mockMvc.perform(
                        post("/tasks/1/delete")
                                .principal(authentication)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/tasks"));

        verify(taskService, times(1))
                .delete(
                        1L,
                        "userA",
                        10L,
                        "USER"
                );
    }

    // =========================================================
    // POST /tasks/{id}/status
    // =========================================================

    @Test
    void changeStatus_着手操作をServiceへ渡して一覧へリダイレクトする()
            throws Exception {

        // given
        prepareLoginUser(
                "userA",
                10L,
                "USER"
        );

        // when & then
        mockMvc.perform(
                        post("/tasks/1/status")
                                .principal(authentication)
                                .param(
                                        "operation",
                                        TaskOperation.START.name()
                                )
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/tasks"));

        verify(taskService, times(1))
                .changeStatus(
                        1L,
                        TaskOperation.START,
                        "userA",
                        10L,
                        "USER"
                );
    }

    // =========================================================
    // ログインユーザーがDBに存在しない場合
    // =========================================================

    @Test
        void create_ログインユーザーがDBに存在しない場合は例外になる()
                throws Exception {

        // given
        when(authentication.getName())
                .thenReturn("unknownUser");

        when(userRepository.findByUsername("unknownUser"))
                .thenReturn(Optional.empty());

        // when
        jakarta.servlet.ServletException exception =
                org.junit.jupiter.api.Assertions.assertThrows(
                        jakarta.servlet.ServletException.class,
                        () -> mockMvc.perform(
                                post("/tasks")
                                        .principal(authentication)
                                        .param("title", "新規タスク")
                        )
                );

        // then
        assertEquals(
                IllegalStateException.class,
                exception.getCause().getClass()
        );

        assertEquals(
                "Login user not found: unknownUser",
                exception.getCause().getMessage()
        );

        verify(taskService, never())
                .create(
                        any(TaskCreateForm.class),
                        any(String.class)
                );
        }

    // =========================================================
    // テスト補助メソッド
    // =========================================================

    private void prepareLoginUser(
            String username,
            Long userId,
            String role
    ) {
        when(authentication.getName())
                .thenReturn(username);

        when(userRepository.findByUsername(username))
                .thenReturn(Optional.of(loginUser));

        when(loginUser.getUsername())
                .thenReturn(username);

        when(loginUser.getId())
                .thenReturn(userId);

        when(loginUser.getRole().name())
                .thenReturn(role);
    }
}