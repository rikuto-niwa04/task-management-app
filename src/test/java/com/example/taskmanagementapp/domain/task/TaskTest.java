package com.example.taskmanagementapp.domain.task;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    void todo_start_shouldBecomeInProgress() {
        Task t = new Task();
        t.setStatusForTest(TaskStatus.TODO);

        t.apply(TaskOperation.START);

        assertEquals(TaskStatus.IN_PROGRESS, t.getStatus());
    }

    @Test
    void todo_complete_shouldThrowException() {
        Task t = new Task();
        t.setStatusForTest(TaskStatus.TODO);

        assertThrows(IllegalStateException.class, () -> t.apply(TaskOperation.COMPLETE));
    }

    @Test
    void inProgress_complete_shouldBecomeDone() {
        Task t = new Task();
        t.setStatusForTest(TaskStatus.IN_PROGRESS);

        t.apply(TaskOperation.COMPLETE);

        assertEquals(TaskStatus.DONE, t.getStatus());
    }

    @Test
    void done_reopen_shouldBecomeInProgress() {
        Task t = new Task();
        t.setStatusForTest(TaskStatus.DONE);

        t.apply(TaskOperation.REOPEN);

        assertEquals(TaskStatus.IN_PROGRESS, t.getStatus());
    }
}