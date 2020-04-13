create table like_user
(
	id bigint auto_increment,
	user_id bigint null,
	comment_id bigint null,
	constraint like_user_pk
		primary key (id)
);