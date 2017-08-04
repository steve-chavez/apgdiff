SET search_path = public, pg_catalog;
CREATE POLICY only_owners ON todos FOR ALL TO PUBLIC;

SET search_path = data, pg_catalog;
CREATE POLICY only_owners ON sub_tasks FOR ALL TO PUBLIC;
CREATE POLICY no_private ON sub_tasks FOR ALL TO anonymous, webuser;
