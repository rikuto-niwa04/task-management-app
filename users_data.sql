--
-- PostgreSQL database dump
--

\restrict xABBtJkivWdLKj6aT5vb4UgHlBsvDdnbSeX3f3DP0R9QCPJwg3L2X2tcTdRqt1f

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
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.users (id, username, password, role) VALUES (1, 'admin', '$2a$10$h94gxTTEbUKN4Gnnv29//eWVnlgtnRh7Ja6vLj5gKRn7Odml1Umhm', 'ADMIN');
INSERT INTO public.users (id, username, password, role) VALUES (2, 'user1', '$2a$10$SLOikg.5ozqYGqqvlSVByOWjetbAv3.RGYZE4rw6mipVew6apanRe', 'USER');


--
-- Name: users_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.users_id_seq', 2, true);


--
-- PostgreSQL database dump complete
--

\unrestrict xABBtJkivWdLKj6aT5vb4UgHlBsvDdnbSeX3f3DP0R9QCPJwg3L2X2tcTdRqt1f

