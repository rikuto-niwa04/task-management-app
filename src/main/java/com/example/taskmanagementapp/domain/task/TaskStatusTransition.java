package com.example.taskmanagementapp.domain.task;

public final class TaskStatusTransition {

    private TaskStatusTransition() {}

    public static boolean canTransition(TaskStatus from, TaskStatus to) {
        if (from == null || to == null) return false;
        //実務想定では同一ステータスへの遷移は無意味なので弾くべきかもしれませんが、ここでは許容します。2026/02/07
        if (from == to) return true;

        return switch (from) {
            case TODO -> (to == TaskStatus.IN_PROGRESS);
            case IN_PROGRESS -> (to == TaskStatus.DONE || to == TaskStatus.TODO);
            case DONE -> (to == TaskStatus.IN_PROGRESS);
        };
    }

    public static TaskStatus targetStatus(TaskStatus current, TaskOperation op) {
        if (current == null || op == null) throw new IllegalArgumentException("status/operation is null");

        return switch (op) {
            case START -> TaskStatus.IN_PROGRESS;
            case COMPLETE -> TaskStatus.DONE;
            case REVERT -> TaskStatus.TODO;
            case REOPEN -> TaskStatus.IN_PROGRESS;
        };
    }
}
