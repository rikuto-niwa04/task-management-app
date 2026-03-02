package com.example.taskmanagementapp.domain.task;

public enum TaskOperation {
    START,      // TODO ➡ IN_PROGRESS
    COMPLETE,   // IN_PROGRESS ➡ DONE
    REVERT,     // IN_PROGRESS ➡ TODO
    REOPEN      // DONE ➡ IN_PROGRESS
}
