CREATE DATABASE IF NOT EXISTS `quarkus`;

CREATE USER 'quarkus'@'%' IDENTIFIED BY 'quarkus';

GRANT ALL PRIVILEGES ON `quarkus`.* TO 'quarkus'@'%';

SET GLOBAL max_allowed_packet = 1024 * 1024 * 256;