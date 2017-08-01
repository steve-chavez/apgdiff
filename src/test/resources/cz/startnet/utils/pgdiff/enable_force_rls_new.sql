create table items (
	id integer
);

create table sub_items (
	id integer
);

alter table items enable row level security;
alter table sub_items enable row level security;

create table projects (
	id integer
);

create table sub_projects (
	id integer
);

alter table projects force row level security;
alter table sub_projects force row level security;
