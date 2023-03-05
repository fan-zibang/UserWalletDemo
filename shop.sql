create database shop;

use shop;

drop table if exists tb_user;
create table tb_user(
	user_id bigint not null auto_increment,
	account varchar(100) not null comment '账号',
	password varchar(100) not null comment '密码',
	account_balance decimal(15,2) default 0.00 not null comment '账户余额',
	primary key(user_id)
);
insert into tb_user(user_id, account, password, account_balance) values(1, 'xiaoming', '123', 100.00);

drop table if exists tb_account_details;
create table tb_account_details(
	account_number bigint not null comment '流水号',
	user_id bigint not null comment '对应用户id',
	account decimal(15,2) not null comment '操作金额',
	balance decimal(15,2) not null comment '余额',
	account_type tinyint not null comment '类型：1-收入 2-支出',
	remark varchar(100) not null comment '备注',
	create_time varchar(100) not null comment '操作时间'
);