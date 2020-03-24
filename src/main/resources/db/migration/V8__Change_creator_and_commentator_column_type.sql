alter table question modify creator bigint null;
alter table comment modify Commentator bigint null comment '评论人id';