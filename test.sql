CREATE DATABASE TestDB;

USE TestDB;

CREATE TABLE Users (
	ID int(10) unsigned NOT NULL,
	name varchar(200) DEFAULT NULL,
	surname varchar(200) DEFAULT NULL,
	rate int(10) NOT NULL,
	published_msg_ids varchar(255) DEFAULT NULL,
	watched_msg_ids varchar(255) DEFAULT NULL,
    PRIMARY KEY (ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE Messages (
    ID int(10) unsigned NOT NULL,
    tag_id int(10) unsigned NOT NULL,
    text varchar(200) DEFAULT NULL,
    publisher_id int(10) NOT NULL,
    rate int(10) NOT NULL,
    PRIMARY KEY (ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE Tags (
    ID int(10) unsigned NOT NULL,
    tag varchar(200) DEFAULT NULL,
    PRIMARY KEY (ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


