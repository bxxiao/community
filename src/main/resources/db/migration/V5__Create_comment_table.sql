create table comment
(
	id bigint auto_increment,
	parent_id bigint not null,
	type int not null,
	commentator int not null,
	gmt_create bigint,
	gmt_modified bigint,
	like_count bigint default 0,
	constraint comment_pk
		primary key (id)
);

comment on column comment.type is '评论类型，指一级、二级评论';

comment on column comment.like_count is '点赞数';

