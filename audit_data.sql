--
-- PostgreSQL database dump
--

\restrict 4u7AT6QHeQEdHJUWiLZpQmKalCS9GvYALrVu7uVWT3liZx0TKVddoirlckHhGEs

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
-- Name: task_audit_logs_id_seq; Type: SEQUENCE SET; Schema: public; Owner: app
--

SELECT pg_catalog.setval('public.task_audit_logs_id_seq', 59, true);


--
-- PostgreSQL database dump complete
--

\unrestrict 4u7AT6QHeQEdHJUWiLZpQmKalCS9GvYALrVu7uVWT3liZx0TKVddoirlckHhGEs

