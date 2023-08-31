create database Java_db;
use Java_db;
create table Login(
ID int not null auto_increment,
UserName varchar(50) not null,
Password varchar(50) not null,
primary key(ID)
);
insert into Login (UserName,Password) values ('kist','kist'),('bit','bit');
select UserName,Password from Login where UserName = 'kist' and Password= 'kist';