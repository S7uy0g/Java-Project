create database Java_db;
use Java_db;
create table Login(
ID int not null auto_increment,
UserName varchar(50) not null,
Password varchar(50) not null,
primary key(ID)
);
insert into Login (UserName,Password) values ('test','test');
select UserName,Password from Login where UserName = 'kist' and Password= 'kist';
ALTER TABLE Login
ADD Email varchar(255) not null;