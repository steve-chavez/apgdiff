
create table todos (
	 id integer
);

create policy only_owners on todos;

create schema data;
create table data.sub_tasks (
	 id integer
);

create policy only_owners on data.sub_tasks;

create policy no_private on data.sub_tasks to anonymous, webuser;
