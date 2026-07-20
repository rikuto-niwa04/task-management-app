--
-- PostgreSQL database dump
--

\restrict 1vZo1RZlG3GxfzlNlw5hENqhD1Zru1iPYAIBFllVTfH3W2ZwmH0tlXZ2GFsxtJP

-- Dumped from database version 17.7
-- Dumped by pg_dump version 17.7

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Data for Name: task_audit_logs; Type: TABLE DATA; Schema: public; Owner: app
--

INSERT INTO public.task_audit_logs (id, actor, detail, event_type, occurred_at, task_id) VALUES (1, 'user', 'created task', 'CREATE', '2026-02-01 13:24:02.835585', 1);
INSERT INTO public.task_audit_logs (id, actor, detail, event_type, occurred_at, task_id) VALUES (2, 'user', 'status TODO -> IN_PROGRESS by START', 'STATUS_CHANGE', '2026-02-01 13:24:25.632859', 1);
INSERT INTO public.task_audit_logs (id, actor, detail, event_type, occurred_at, task_id) VALUES (3, 'user', 'status IN_PROGRESS -> DONE by COMPLETE', 'STATUS_CHANGE', '2026-02-01 13:24:39.044779', 1);
INSERT INTO public.task_audit_logs (id, actor, detail, event_type, occurred_at, task_id) VALUES (4, 'user', 'status DONE -> IN_PROGRESS by REOPEN', 'STATUS_CHANGE', '2026-02-01 13:24:41.944808', 1);
INSERT INTO public.task_audit_logs (id, actor, detail, event_type, occurred_at, task_id) VALUES (5, 'user', 'status IN_PROGRESS -> TODO by REVERT', 'STATUS_CHANGE', '2026-02-01 13:24:44.333023', 1);
INSERT INTO public.task_audit_logs (id, actor, detail, event_type, occurred_at, task_id) VALUES (6, 'user', 'status TODO -> IN_PROGRESS by START', 'STATUS_CHANGE', '2026-02-01 13:24:46.942433', 1);
INSERT INTO public.task_audit_logs (id, actor, detail, event_type, occurred_at, task_id) VALUES (7, 'user', 'status IN_PROGRESS -> DONE by COMPLETE', 'STATUS_CHANGE', '2026-02-01 13:24:48.85844', 1);
INSERT INTO public.task_audit_logs (id, actor, detail, event_type, occurred_at, task_id) VALUES (8, 'user', 'deleted task', 'DELETE', '2026-02-01 13:24:57.604838', 1);
INSERT INTO public.task_audit_logs (id, actor, detail, event_type, occurred_at, task_id) VALUES (9, 'user', 'created task', 'CREATE', '2026-02-01 13:25:23.94662', 2);
INSERT INTO public.task_audit_logs (id, actor, detail, event_type, occurred_at, task_id) VALUES (10, 'anonymous', 'status TODO -> IN_PROGRESS by START', 'STATUS_CHANGE', '2026-05-18 14:30:33.377943', 2);
INSERT INTO public.task_audit_logs (id, actor, detail, event_type, occurred_at, task_id) VALUES (11, 'anonymous', 'status IN_PROGRESS -> TODO by REVERT', 'STATUS_CHANGE', '2026-05-18 14:30:40.678371', 2);
INSERT INTO public.task_audit_logs (id, actor, detail, event_type, occurred_at, task_id) VALUES (12, 'anonymous', 'status TODO -> IN_PROGRESS by START', 'STATUS_CHANGE', '2026-05-18 14:30:45.549035', 2);
INSERT INTO public.task_audit_logs (id, actor, detail, event_type, occurred_at, task_id) VALUES (13, 'anonymous', 'status IN_PROGRESS -> DONE by COMPLETE', 'STATUS_CHANGE', '2026-05-18 14:30:48.952689', 2);
INSERT INTO public.task_audit_logs (id, actor, detail, event_type, occurred_at, task_id) VALUES (14, 'anonymous', 'status DONE -> IN_PROGRESS by REOPEN', 'STATUS_CHANGE', '2026-05-18 14:31:01.977122', 2);
INSERT INTO public.task_audit_logs (id, actor, detail, event_type, occurred_at, task_id) VALUES (15, 'anonymous', 'status IN_PROGRESS -> TODO by REVERT', 'STATUS_CHANGE', '2026-05-18 14:31:03.565997', 2);
INSERT INTO public.task_audit_logs (id, actor, detail, event_type, occurred_at, task_id) VALUES (16, 'anonymous', 'status TODO -> IN_PROGRESS by START', 'STATUS_CHANGE', '2026-05-18 14:31:08.124847', 2);
INSERT INTO public.task_audit_logs (id, actor, detail, event_type, occurred_at, task_id) VALUES (17, 'anonymous', 'status IN_PROGRESS -> TODO by REVERT', 'STATUS_CHANGE', '2026-05-18 14:31:13.329814', 2);
INSERT INTO public.task_audit_logs (id, actor, detail, event_type, occurred_at, task_id) VALUES (18, 'anonymous', 'status TODO -> IN_PROGRESS by START', 'STATUS_CHANGE', '2026-05-18 14:31:14.903361', 2);
INSERT INTO public.task_audit_logs (id, actor, detail, event_type, occurred_at, task_id) VALUES (19, 'anonymous', 'status IN_PROGRESS -> DONE by COMPLETE', 'STATUS_CHANGE', '2026-05-18 14:31:16.117373', 2);
INSERT INTO public.task_audit_logs (id, actor, detail, event_type, occurred_at, task_id) VALUES (20, 'anonymous', 'created task', 'CREATE', '2026-05-18 18:19:40.139762', 3);
INSERT INTO public.task_audit_logs (id, actor, detail, event_type, occurred_at, task_id) VALUES (21, 'anonymous', 'created task', 'CREATE', '2026-05-18 18:19:56.065077', 4);
INSERT INTO public.task_audit_logs (id, actor, detail, event_type, occurred_at, task_id) VALUES (22, 'anonymous', 'updated fields', 'UPDATE', '2026-05-18 18:20:09.513758', 4);
INSERT INTO public.task_audit_logs (id, actor, detail, event_type, occurred_at, task_id) VALUES (23, 'anonymous', 'created task', 'CREATE', '2026-05-18 18:40:28.474306', 5);
INSERT INTO public.task_audit_logs (id, actor, detail, event_type, occurred_at, task_id) VALUES (24, 'anonymous', 'status TODO -> IN_PROGRESS by START', 'STATUS_CHANGE', '2026-05-18 18:40:36.733901', 5);
INSERT INTO public.task_audit_logs (id, actor, detail, event_type, occurred_at, task_id) VALUES (25, 'anonymous', 'deleted task', 'DELETE', '2026-07-04 12:33:01.789155', 4);
INSERT INTO public.task_audit_logs (id, actor, detail, event_type, occurred_at, task_id) VALUES (26, 'anonymous', 'created task', 'CREATE', '2026-07-14 18:30:17.605165', 46);
INSERT INTO public.task_audit_logs (id, actor, detail, event_type, occurred_at, task_id) VALUES (27, 'anonymous', 'updated fields', 'UPDATE', '2026-07-14 18:30:39.685505', 46);
INSERT INTO public.task_audit_logs (id, actor, detail, event_type, occurred_at, task_id) VALUES (28, 'anonymous', 'status TODO -> IN_PROGRESS by START', 'STATUS_CHANGE', '2026-07-14 18:30:52.772421', 46);
INSERT INTO public.task_audit_logs (id, actor, detail, event_type, occurred_at, task_id) VALUES (29, 'anonymous', 'status IN_PROGRESS -> TODO by REVERT', 'STATUS_CHANGE', '2026-07-14 18:30:57.236865', 2);
INSERT INTO public.task_audit_logs (id, actor, detail, event_type, occurred_at, task_id) VALUES (30, 'anonymous', 'status TODO -> IN_PROGRESS by START', 'STATUS_CHANGE', '2026-07-14 18:31:01.929244', 2);
INSERT INTO public.task_audit_logs (id, actor, detail, event_type, occurred_at, task_id) VALUES (31, 'anonymous', 'status IN_PROGRESS -> DONE by COMPLETE', 'STATUS_CHANGE', '2026-07-14 18:31:08.123664', 2);
INSERT INTO public.task_audit_logs (id, actor, detail, event_type, occurred_at, task_id) VALUES (32, 'anonymous', 'status DONE -> IN_PROGRESS by REOPEN', 'STATUS_CHANGE', '2026-07-14 18:31:09.828666', 2);
INSERT INTO public.task_audit_logs (id, actor, detail, event_type, occurred_at, task_id) VALUES (33, 'anonymous', 'status IN_PROGRESS -> TODO by REVERT', 'STATUS_CHANGE', '2026-07-14 18:31:11.483677', 2);
INSERT INTO public.task_audit_logs (id, actor, detail, event_type, occurred_at, task_id) VALUES (34, 'anonymous', 'status TODO -> IN_PROGRESS by START', 'STATUS_CHANGE', '2026-07-14 18:31:13.301679', 2);
INSERT INTO public.task_audit_logs (id, actor, detail, event_type, occurred_at, task_id) VALUES (35, 'anonymous', 'status IN_PROGRESS -> DONE by COMPLETE', 'STATUS_CHANGE', '2026-07-14 19:17:20.26511', 46);
INSERT INTO public.task_audit_logs (id, actor, detail, event_type, occurred_at, task_id) VALUES (36, 'anonymous', 'status IN_PROGRESS -> DONE by COMPLETE', 'STATUS_CHANGE', '2026-07-14 19:17:23.300522', 2);
INSERT INTO public.task_audit_logs (id, actor, detail, event_type, occurred_at, task_id) VALUES (37, 'anonymous', 'status DONE -> IN_PROGRESS by REOPEN', 'STATUS_CHANGE', '2026-07-14 19:17:25.132664', 2);
INSERT INTO public.task_audit_logs (id, actor, detail, event_type, occurred_at, task_id) VALUES (38, 'anonymous', 'status TODO -> IN_PROGRESS by START', 'STATUS_CHANGE', '2026-07-14 19:19:58.330562', 3);
INSERT INTO public.task_audit_logs (id, actor, detail, event_type, occurred_at, task_id) VALUES (39, 'anonymous', 'status IN_PROGRESS -> DONE by COMPLETE', 'STATUS_CHANGE', '2026-07-14 19:20:03.534476', 2);
INSERT INTO public.task_audit_logs (id, actor, detail, event_type, occurred_at, task_id) VALUES (40, 'anonymous', 'status IN_PROGRESS -> DONE by COMPLETE', 'STATUS_CHANGE', '2026-07-14 19:20:05.438629', 3);
INSERT INTO public.task_audit_logs (id, actor, detail, event_type, occurred_at, task_id) VALUES (41, 'anonymous', 'status TODO -> IN_PROGRESS by START', 'STATUS_CHANGE', '2026-07-14 19:20:08.544222', 5);
INSERT INTO public.task_audit_logs (id, actor, detail, event_type, occurred_at, task_id) VALUES (42, 'anonymous', 'status IN_PROGRESS -> DONE by COMPLETE', 'STATUS_CHANGE', '2026-07-14 19:20:09.963217', 5);
INSERT INTO public.task_audit_logs (id, actor, detail, event_type, occurred_at, task_id) VALUES (43, 'anonymous', 'status DONE -> IN_PROGRESS by REOPEN', 'STATUS_CHANGE', '2026-07-14 20:11:15.59733', 2);
INSERT INTO public.task_audit_logs (id, actor, detail, event_type, occurred_at, task_id) VALUES (44, 'anonymous', 'status IN_PROGRESS -> DONE by COMPLETE', 'STATUS_CHANGE', '2026-07-14 20:11:17.022573', 2);
INSERT INTO public.task_audit_logs (id, actor, detail, event_type, occurred_at, task_id) VALUES (45, 'anonymous', 'status DONE -> IN_PROGRESS by REOPEN', 'STATUS_CHANGE', '2026-07-14 20:11:28.004601', 2);
INSERT INTO public.task_audit_logs (id, actor, detail, event_type, occurred_at, task_id) VALUES (46, 'anonymous', 'status DONE -> IN_PROGRESS by REOPEN', 'STATUS_CHANGE', '2026-07-14 20:13:31.84035', 3);
INSERT INTO public.task_audit_logs (id, actor, detail, event_type, occurred_at, task_id) VALUES (47, 'anonymous', 'status IN_PROGRESS -> DONE by COMPLETE', 'STATUS_CHANGE', '2026-07-14 20:13:33.762348', 2);
INSERT INTO public.task_audit_logs (id, actor, detail, event_type, occurred_at, task_id) VALUES (48, 'anonymous', 'status DONE -> IN_PROGRESS by REOPEN', 'STATUS_CHANGE', '2026-07-14 20:27:26.471352', 2);
INSERT INTO public.task_audit_logs (id, actor, detail, event_type, occurred_at, task_id) VALUES (49, 'anonymous', 'status IN_PROGRESS -> DONE by COMPLETE', 'STATUS_CHANGE', '2026-07-14 20:27:30.009842', 2);
INSERT INTO public.task_audit_logs (id, actor, detail, event_type, occurred_at, task_id) VALUES (50, 'admin', 'status IN_PROGRESS -> DONE by COMPLETE', 'STATUS_CHANGE', '2026-07-15 16:39:25.494884', 3);
INSERT INTO public.task_audit_logs (id, actor, detail, event_type, occurred_at, task_id) VALUES (51, 'admin', 'status DONE -> IN_PROGRESS by REOPEN', 'STATUS_CHANGE', '2026-07-15 16:39:27.211211', 2);
INSERT INTO public.task_audit_logs (id, actor, detail, event_type, occurred_at, task_id) VALUES (52, 'admin', 'updated fields', 'UPDATE', '2026-07-15 16:39:39.347084', 7);
INSERT INTO public.task_audit_logs (id, actor, detail, event_type, occurred_at, task_id) VALUES (53, 'admin', 'status DONE -> IN_PROGRESS by REOPEN', 'STATUS_CHANGE', '2026-07-15 16:39:42.513531', 7);
INSERT INTO public.task_audit_logs (id, actor, detail, event_type, occurred_at, task_id) VALUES (54, 'admin', 'status TODO -> IN_PROGRESS by START', 'STATUS_CHANGE', '2026-07-15 16:39:47.969847', 12);
INSERT INTO public.task_audit_logs (id, actor, detail, event_type, occurred_at, task_id) VALUES (55, 'user1', 'status IN_PROGRESS -> DONE by COMPLETE', 'STATUS_CHANGE', '2026-07-15 17:52:07.600192', 7);
INSERT INTO public.task_audit_logs (id, actor, detail, event_type, occurred_at, task_id) VALUES (56, 'user1', 'status DONE -> IN_PROGRESS by REOPEN', 'STATUS_CHANGE', '2026-07-15 18:04:02.084242', 7);
INSERT INTO public.task_audit_logs (id, actor, detail, event_type, occurred_at, task_id) VALUES (57, 'user1', 'updated fields', 'UPDATE', '2026-07-15 18:04:12.026142', 7);
INSERT INTO public.task_audit_logs (id, actor, detail, event_type, occurred_at, task_id) VALUES (58, 'user1', 'status IN_PROGRESS -> DONE by COMPLETE', 'STATUS_CHANGE', '2026-07-15 18:04:17.208419', 7);
INSERT INTO public.task_audit_logs (id, actor, detail, event_type, occurred_at, task_id) VALUES (59, 'user1', 'deleted task', 'DELETE', '2026-07-17 06:49:52.622847', 7);


--
-- Data for Name: tasks; Type: TABLE DATA; Schema: public; Owner: app
--

INSERT INTO public.tasks (id, assignee_id, created_at, description, due_date, status, title, updated_at) VALUES (8, NULL, '2026-07-05 16:19:41.311244', '説明', '2026-06-03', 'TODO', 'テスト8', '2026-07-14 16:41:01.396637');
INSERT INTO public.tasks (id, assignee_id, created_at, description, due_date, status, title, updated_at) VALUES (9, 1, '2026-07-05 16:19:41.311244', '説明', '2026-06-04', 'TODO', 'テスト9', '2026-07-14 16:41:03.603615');
INSERT INTO public.tasks (id, assignee_id, created_at, description, due_date, status, title, updated_at) VALUES (2, 1, '2026-02-01 13:25:23.940601', 'テスト説明', '2026-02-03', 'IN_PROGRESS', 'テストタイトル', '2026-07-15 16:39:27.219837');
INSERT INTO public.tasks (id, assignee_id, created_at, description, due_date, status, title, updated_at) VALUES (12, 1, '2026-07-05 16:19:41.311244', '説明', '2026-06-07', 'IN_PROGRESS', 'テスト12', '2026-07-15 16:39:47.975837');
INSERT INTO public.tasks (id, assignee_id, created_at, description, due_date, status, title, updated_at) VALUES (46, NULL, '2026-07-14 18:30:17.476129', 'riririri', NULL, 'DONE', 'ririkuku', '2026-07-14 19:17:20.409521');
INSERT INTO public.tasks (id, assignee_id, created_at, description, due_date, status, title, updated_at) VALUES (10, 2, '2026-07-05 16:19:41.311244', '説明', '2026-06-05', 'DONE', 'テスト10', '2026-07-05 16:19:41.311244');
INSERT INTO public.tasks (id, assignee_id, created_at, description, due_date, status, title, updated_at) VALUES (11, NULL, '2026-07-05 16:19:41.311244', '説明', '2026-06-06', 'IN_PROGRESS', 'テスト11', '2026-07-05 16:19:41.311244');
INSERT INTO public.tasks (id, assignee_id, created_at, description, due_date, status, title, updated_at) VALUES (13, 2, '2026-07-05 16:19:41.311244', '説明', '2026-06-08', 'DONE', 'テスト13', '2026-07-05 16:19:41.311244');
INSERT INTO public.tasks (id, assignee_id, created_at, description, due_date, status, title, updated_at) VALUES (14, NULL, '2026-07-05 16:19:41.311244', '説明', '2026-06-09', 'IN_PROGRESS', 'テスト14', '2026-07-05 16:19:41.311244');
INSERT INTO public.tasks (id, assignee_id, created_at, description, due_date, status, title, updated_at) VALUES (15, 1, '2026-07-05 16:19:41.311244', '説明', '2026-06-10', 'TODO', 'テスト15', '2026-07-05 16:19:41.311244');
INSERT INTO public.tasks (id, assignee_id, created_at, description, due_date, status, title, updated_at) VALUES (16, 1, '2026-07-05 16:28:18.864995', '受信メールを確認する', '2026-06-11', 'TODO', 'メール確認', '2026-07-05 16:28:18.864995');
INSERT INTO public.tasks (id, assignee_id, created_at, description, due_date, status, title, updated_at) VALUES (17, 2, '2026-07-05 16:28:18.864995', '一覧画面のレビュー', '2026-06-12', 'DONE', '画面レビュー', '2026-07-05 16:28:18.864995');
INSERT INTO public.tasks (id, assignee_id, created_at, description, due_date, status, title, updated_at) VALUES (19, NULL, '2026-07-05 16:28:18.864995', 'レイアウトを調整する', '2026-06-14', 'TODO', '画面修正', '2026-07-05 16:28:18.864995');
INSERT INTO public.tasks (id, assignee_id, created_at, description, due_date, status, title, updated_at) VALUES (22, 1, '2026-07-05 16:28:18.864995', 'APIレスポンスを確認する', '2026-06-17', 'TODO', 'API動作確認', '2026-07-05 16:28:18.864995');
INSERT INTO public.tasks (id, assignee_id, created_at, description, due_date, status, title, updated_at) VALUES (23, 2, '2026-07-05 16:28:18.864995', 'SQLの動作確認', '2026-06-18', 'TODO', 'SQL確認', '2026-07-05 16:28:18.864995');
INSERT INTO public.tasks (id, assignee_id, created_at, description, due_date, status, title, updated_at) VALUES (25, NULL, '2026-07-05 16:28:18.864995', '画面結合テスト', '2026-06-20', 'DONE', '結合テスト', '2026-07-05 16:28:18.864995');
INSERT INTO public.tasks (id, assignee_id, created_at, description, due_date, status, title, updated_at) VALUES (28, 1, '2026-07-05 16:28:18.864995', '削除機能を確認する', '2026-06-23', 'DONE', '削除確認', '2026-07-05 16:28:18.864995');
INSERT INTO public.tasks (id, assignee_id, created_at, description, due_date, status, title, updated_at) VALUES (29, 2, '2026-07-05 16:28:18.864995', '登録機能を確認する', '2026-06-24', 'IN_PROGRESS', '登録確認', '2026-07-05 16:28:18.864995');
INSERT INTO public.tasks (id, assignee_id, created_at, description, due_date, status, title, updated_at) VALUES (30, NULL, '2026-07-05 16:28:18.864995', 'CSV出力確認', '2026-06-25', 'TODO', 'CSV確認', '2026-07-05 16:28:18.864995');
INSERT INTO public.tasks (id, assignee_id, created_at, description, due_date, status, title, updated_at) VALUES (34, 1, '2026-07-05 16:28:18.864995', 'タイトル検索確認', '2026-06-29', 'TODO', 'タイトル検索', '2026-07-05 16:28:18.864995');
INSERT INTO public.tasks (id, assignee_id, created_at, description, due_date, status, title, updated_at) VALUES (35, 2, '2026-07-05 16:28:18.864995', '担当者検索確認', '2026-06-30', 'IN_PROGRESS', '担当者検索', '2026-07-05 16:28:18.864995');
INSERT INTO public.tasks (id, assignee_id, created_at, description, due_date, status, title, updated_at) VALUES (36, NULL, '2026-07-05 16:28:18.864995', '期限表示確認', '2026-07-01', 'DONE', '期限確認', '2026-07-05 16:28:18.864995');
INSERT INTO public.tasks (id, assignee_id, created_at, description, due_date, status, title, updated_at) VALUES (40, 1, '2026-07-05 16:28:18.864995', '画面デザイン調整', '2026-07-05', 'TODO', 'UI改善', '2026-07-05 16:28:18.864995');
INSERT INTO public.tasks (id, assignee_id, created_at, description, due_date, status, title, updated_at) VALUES (41, NULL, '2026-07-05 16:28:18.864995', 'テストデータ確認', '2026-07-06', 'IN_PROGRESS', 'データ確認', '2026-07-05 16:28:18.864995');
INSERT INTO public.tasks (id, assignee_id, created_at, description, due_date, status, title, updated_at) VALUES (42, 2, '2026-07-05 16:28:18.864995', 'コード整理', '2026-07-07', 'DONE', 'リファクタリング', '2026-07-05 16:28:18.864995');
INSERT INTO public.tasks (id, assignee_id, created_at, description, due_date, status, title, updated_at) VALUES (43, NULL, '2026-07-05 16:28:18.864995', '例外処理確認', '2026-07-08', 'IN_PROGRESS', '例外確認', '2026-07-14 16:40:25.32303');
INSERT INTO public.tasks (id, assignee_id, created_at, description, due_date, status, title, updated_at) VALUES (3, NULL, '2026-05-18 18:19:40.013215', '二回目タスク', '2026-05-26', 'DONE', 'テストテスト', '2026-07-15 16:39:25.628883');
INSERT INTO public.tasks (id, assignee_id, created_at, description, due_date, status, title, updated_at) VALUES (18, NULL, '2026-07-05 16:28:18.864995', '検索機能を確認する', '2026-06-13', 'IN_PROGRESS', '検索テスト', '2026-07-05 16:28:18.864995');
INSERT INTO public.tasks (id, assignee_id, created_at, description, due_date, status, title, updated_at) VALUES (6, 1, '2026-07-05 16:19:41.311244', '説明', '2026-06-01', 'DONE', 'テスト6', '2026-07-14 16:38:42.447148');
INSERT INTO public.tasks (id, assignee_id, created_at, description, due_date, status, title, updated_at) VALUES (20, NULL, '2026-07-05 16:28:18.864995', '不具合を調査する', '2026-06-15', 'IN_PROGRESS', 'バグ調査', '2026-07-05 16:28:18.864995');
INSERT INTO public.tasks (id, assignee_id, created_at, description, due_date, status, title, updated_at) VALUES (21, NULL, '2026-07-05 16:28:18.864995', '監査ログを確認する', '2026-06-16', 'DONE', '監査ログ確認', '2026-07-05 16:28:18.864995');
INSERT INTO public.tasks (id, assignee_id, created_at, description, due_date, status, title, updated_at) VALUES (5, NULL, '2026-05-18 18:40:28.355661', 'テスト4回目', '2026-06-02', 'DONE', 'テスト', '2026-07-14 19:20:09.971191');
INSERT INTO public.tasks (id, assignee_id, created_at, description, due_date, status, title, updated_at) VALUES (24, NULL, '2026-07-05 16:28:18.864995', '一般ユーザー権限確認', '2026-06-19', 'IN_PROGRESS', '権限テスト', '2026-07-05 16:28:18.864995');
INSERT INTO public.tasks (id, assignee_id, created_at, description, due_date, status, title, updated_at) VALUES (26, NULL, '2026-07-05 16:28:18.864995', '一覧表示を確認する', '2026-06-21', 'TODO', '一覧確認', '2026-07-05 16:28:18.864995');
INSERT INTO public.tasks (id, assignee_id, created_at, description, due_date, status, title, updated_at) VALUES (27, NULL, '2026-07-05 16:28:18.864995', '編集機能を確認する', '2026-06-22', 'TODO', '編集確認', '2026-07-05 16:28:18.864995');
INSERT INTO public.tasks (id, assignee_id, created_at, description, due_date, status, title, updated_at) VALUES (31, NULL, '2026-07-05 16:28:18.864995', 'ページング動作確認', '2026-06-26', 'TODO', 'ページング確認1', '2026-07-05 16:28:18.864995');
INSERT INTO public.tasks (id, assignee_id, created_at, description, due_date, status, title, updated_at) VALUES (32, NULL, '2026-07-05 16:28:18.864995', 'ページング動作確認', '2026-06-27', 'IN_PROGRESS', 'ページング確認2', '2026-07-05 16:28:18.864995');
INSERT INTO public.tasks (id, assignee_id, created_at, description, due_date, status, title, updated_at) VALUES (33, NULL, '2026-07-05 16:28:18.864995', 'ページング動作確認', '2026-06-28', 'DONE', 'ページング確認3', '2026-07-05 16:28:18.864995');
INSERT INTO public.tasks (id, assignee_id, created_at, description, due_date, status, title, updated_at) VALUES (37, NULL, '2026-07-05 16:28:18.864995', '入力フォーム確認', '2026-07-02', 'TODO', 'フォーム確認', '2026-07-05 16:28:18.864995');
INSERT INTO public.tasks (id, assignee_id, created_at, description, due_date, status, title, updated_at) VALUES (38, NULL, '2026-07-05 16:28:18.864995', '入力チェック確認', '2026-07-03', 'IN_PROGRESS', 'バリデーション確認', '2026-07-05 16:28:18.864995');
INSERT INTO public.tasks (id, assignee_id, created_at, description, due_date, status, title, updated_at) VALUES (39, NULL, '2026-07-05 16:28:18.864995', '詳細画面表示確認', '2026-07-04', 'DONE', '詳細画面確認', '2026-07-05 16:28:18.864995');
INSERT INTO public.tasks (id, assignee_id, created_at, description, due_date, status, title, updated_at) VALUES (44, NULL, '2026-07-05 16:28:18.864995', '最終動作確認', '2026-07-09', 'IN_PROGRESS', '画面最終確認', '2026-07-05 16:28:18.864995');
INSERT INTO public.tasks (id, assignee_id, created_at, description, due_date, status, title, updated_at) VALUES (45, NULL, '2026-07-05 16:28:18.864995', 'リリース前確認', '2026-07-10', 'DONE', 'リリース準備', '2026-07-05 16:28:18.864995');


--
-- Data for Name: test_ddl_check; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.users (id, username, password, role) VALUES (1, 'admin', '$2a$10$h94gxTTEbUKN4Gnnv29//eWVnlgtnRh7Ja6vLj5gKRn7Odml1Umhm', 'ADMIN');
INSERT INTO public.users (id, username, password, role) VALUES (2, 'user1', '$2a$10$SLOikg.5ozqYGqqvlSVByOWjetbAv3.RGYZE4rw6mipVew6apanRe', 'USER');


--
-- Name: task_audit_logs_id_seq; Type: SEQUENCE SET; Schema: public; Owner: app
--

SELECT pg_catalog.setval('public.task_audit_logs_id_seq', 59, true);


--
-- Name: tasks_id_seq; Type: SEQUENCE SET; Schema: public; Owner: app
--

SELECT pg_catalog.setval('public.tasks_id_seq', 46, true);


--
-- Name: users_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.users_id_seq', 2, true);


--
-- PostgreSQL database dump complete
--

\unrestrict 1vZo1RZlG3GxfzlNlw5hENqhD1Zru1iPYAIBFllVTfH3W2ZwmH0tlXZ2GFsxtJP

