SELECT * FROM java_db.login;
use java_db;
CREATE USER 'root'@'%' IDENTIFIED BY 'root';
GRANT ALL PRIVILEGES ON java_db.* TO 'root'@'%';
FLUSH PRIVILEGES;
