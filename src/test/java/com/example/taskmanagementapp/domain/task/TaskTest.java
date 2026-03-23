package com.example.taskmanagementapp.domain.task;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    // 正常系（許可された遷移）

    @Test
    void todo_start_shouldBecomeInProgress() {
        Task t = new Task();
        t.setStatusForTest(TaskStatus.TODO);

        t.apply(TaskOperation.START);

        assertEquals(TaskStatus.IN_PROGRESS, t.getStatus());
    }

    @Test
    void inProgress_complete_shouldBecomeDone() {
        Task t = new Task();
        t.setStatusForTest(TaskStatus.IN_PROGRESS);

        t.apply(TaskOperation.COMPLETE);

        assertEquals(TaskStatus.DONE, t.getStatus());
    }

    @Test
    void inProgress_revert_shouldBecomeTodo() {
        Task t = new Task();
        t.setStatusForTest(TaskStatus.IN_PROGRESS);

        t.apply(TaskOperation.REVERT);

        assertEquals(TaskStatus.TODO, t.getStatus());
    }

    @Test
    void done_reopen_shouldBecomeInProgress() {
        Task t = new Task();
        t.setStatusForTest(TaskStatus.DONE);

        t.apply(TaskOperation.REOPEN);

        assertEquals(TaskStatus.IN_PROGRESS, t.getStatus());
    }

    // 異常系（禁止された遷移）

    @Test
    void todo_complete_shouldThrowException() {
        Task t = new Task();
        t.setStatusForTest(TaskStatus.TODO);

        try {
            t.apply(TaskOperation.COMPLETE);
            fail("Expected IllegalStateException was not thrown.");
        } catch (IllegalStateException e) {
            assertTrue(e.getMessage().contains("Invalid"));
        }
    }

    @Test
    void todo_reopen_shouldThrowException() {
        Task t = new Task();
        t.setStatusForTest(TaskStatus.TODO);

        try {
            t.apply(TaskOperation.REOPEN);
            fail("Expected IllegalStateException was not thrown.");
        } catch (IllegalStateException e) {
            assertTrue(true);
        }
    }

    @Test
    void done_revert_shouldThrowException() {
        Task t = new Task();
        t.setStatusForTest(TaskStatus.DONE);

        try {
            t.apply(TaskOperation.REVERT);
            fail("Expected IllegalStateException was not thrown.");
        } catch (IllegalStateException e) {
            assertTrue(true);
        }
    }

    // タイトルのバリデーション（空白禁止など）
    @Test
    void create_shouldSetStatusTodo() {
        Task t = Task.create("test", "desc", null, null);

        assertEquals(TaskStatus.TODO, t.getStatus());
    }

    @Test
    void create_blankTitle_shouldThrowException() {
        try {
            Task.create("", "desc", null, null);
            fail("Expected IllegalArgumentException was not thrown.");
        } catch (IllegalArgumentException e) {
            assertEquals("title must not be blank", e.getMessage());
        }
    }

    @Test
    void create_blankSpacesTitle_shouldThrowException() {
        try {
            Task.create("   ", "desc", null, null);
            fail("Expected IllegalArgumentException was not thrown.");
        } catch (IllegalArgumentException e) {
            assertEquals("title must not be blank", e.getMessage());
        }
    }

    @Test
    void setTitle_blank_shouldThrowException() {
        Task t = Task.create("test", "desc", null, null);

        try {
            t.setTitle("   ");
            fail("Expected IllegalArgumentException was not thrown.");
        } catch (IllegalArgumentException e) {
            assertEquals("title must not be blank", e.getMessage());
        }
    }
}