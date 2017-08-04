DROP POLICY only_owners ON todos;
CREATE POLICY only_owners ON todos FOR INSERT TO PUBLIC;
ALTER POLICY only_admins ON todos TO admin, manager;
