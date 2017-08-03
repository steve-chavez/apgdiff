create table todos (
	 id integer
);

create policy only_owners on todos for INSERT;
