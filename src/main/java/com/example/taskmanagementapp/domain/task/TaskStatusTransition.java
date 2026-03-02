package com.example.taskmanagementapp.domain.task;

public final class TaskStatusTransition {

    private TaskStatusTransition() {}

    public static boolean canTransition(TaskStatus from, TaskStatus to) {
        if (from == null || to == null) return false;

        // 同一ステータスへの遷移は許可しない
        if (from == to) return false;

        switch (from) {
            case TODO:
                return to == TaskStatus.IN_PROGRESS;

            case IN_PROGRESS:
                return to == TaskStatus.DONE || to == TaskStatus.TODO;

            case DONE:
                return to == TaskStatus.IN_PROGRESS;

            default:
                return false;
        }
    }

    public static TaskStatus targetStatus(TaskStatus current, TaskOperation op) {
        if (current == null || op == null) {
            throw new IllegalArgumentException("status/operation is null");
        }

        switch (current) {

            case TODO:
                if (op == TaskOperation.START) {
                    return TaskStatus.IN_PROGRESS;
                }
                return null;

            case IN_PROGRESS:
                if (op == TaskOperation.COMPLETE) {
                    return TaskStatus.DONE;
                }
                if (op == TaskOperation.REVERT) {
                    return TaskStatus.TODO;
                }
                return null;

            case DONE:
                if (op == TaskOperation.REOPEN) {
                    return TaskStatus.IN_PROGRESS;
                }
                return null;

            default:
                return null;
        }
    }
}