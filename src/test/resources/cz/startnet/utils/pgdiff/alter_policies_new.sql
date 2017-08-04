create table todos (
	 id integer
);

create policy only_owners on todos for INSERT;

create policy no_private on todos for ALL TO anonymous;

create policy only_admins on todos TO admin, manager;

create policy only_managers on todos;

create policy only_evens on todos using ( (id % 2) = 0);

create policy only_evens_and_1 on todos using ( (id % 2) = 0 or id = 1 );

create policy only_evens_whitespace on todos using ( (id % 2) = 0 );
