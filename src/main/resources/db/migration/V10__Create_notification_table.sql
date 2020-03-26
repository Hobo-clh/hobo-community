create table notification
(
	id bigint auto_increment,
	notifier bigint not null comment '发出通知的人',
	receiver bigint not null comment '接收通知的人',
	outerId bigint null comment '被回复的问题或评论的id',
	type int null comment '区分评论或者回复，1代表问题，2代表评论',
	gmt_create bigint null,
	status int default 0 null comment '0代表未读，1代表以读',
	constraint notification_pk
		primary key (id)
);