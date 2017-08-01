create table items (
	id integer
);

create table sub_items (
	id integer
);

alter table sub_items disable row level security;

create table projects (
	id integer
);

create table sub_projects (
	id integer
);

alter table sub_projects no force row level security;
