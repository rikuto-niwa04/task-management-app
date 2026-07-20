CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(120) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL
);

CREATE TABLE tasks (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(120) NOT NULL,
    description TEXT,
    status VARCHAR(20) NOT NULL,
    due_date DATE,
    assignee_id BIGINT,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_tasks_assignee
        FOREIGN KEY (assignee_id)
        REFERENCES users(id)
);

CREATE TABLE task_audit_logs (
    id BIGSERIAL PRIMARY KEY,
    task_id BIGINT NOT NULL,
    event_type VARCHAR(20) NOT NULL,
    actor VARCHAR(120) NOT NULL,
    detail TEXT,
    occurred_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_task_audit_logs_task_id
    ON task_audit_logs(task_id);

CREATE INDEX idx_task_audit_logs_occurred_at
    ON task_audit_logs(occurred_at);