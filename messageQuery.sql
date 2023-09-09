use java_db;
create table charHistory(
message_id int auto_increment not null,
sender varchar(100) ,
message varchar(300),
receiver varchar(100),
primary key(message_id)
);
rename table charHistory to chatHistory;
alter table chatHistory drop column sender;
alter table chatHistory drop column receiver;