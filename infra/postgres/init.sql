-- Runs once, when the Postgres volume is first created.
-- DATABASE PER SERVICE: each service owns its schema; no service reads another service's tables.
-- (Locally they share one Postgres server to save memory; in production each would typically get
--  its own instance or at least its own credentials.)
CREATE DATABASE catalog;
CREATE DATABASE inventory;
CREATE DATABASE orders;
