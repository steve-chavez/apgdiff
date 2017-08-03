DROP POLICY only_owners ON todos;
CREATE POLICY only_owners ON todos FOR SELECT;
