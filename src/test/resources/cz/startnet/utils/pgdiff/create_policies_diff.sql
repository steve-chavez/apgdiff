SET search_path = public, pg_catalog;
CREATE POLICY only_owners ON todos;

SET search_path = data, pg_catalog;
CREATE POLICY only_owners ON sub_tasks;
