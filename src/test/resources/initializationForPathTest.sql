-- CREATE DATABASE subway DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
-- -- USE subway;

DROP TABLE SECTION;

DROP TABLE LINE;

DROP TABLE STATION;

CREATE TABLE if not exists STATION (
    id bigint auto_increment not null,
    name varchar ( 255 ) not null unique,
    primary key ( id )
);

CREATE TABLE if not exists LINE (
    id bigint auto_increment not null,
    name varchar ( 255 ) not null unique,
    color varchar ( 20 ) not null unique,
    primary key ( id )
);

CREATE TABLE if not exists SECTION (
    id bigint auto_increment not null,
    line_id bigint not null,
    up_station_id bigint not null,
    down_station_id bigint not null,
    distance int, primary key ( id )
);

INSERT INTO LINE(name, color) VALUES('OneLine', 'BLUE');
INSERT INTO LINE(name, color) VALUES('TwoLine', 'Green');
INSERT INTO LINE(name, color) VALUES('ShortLine', 'Red');
INSERT INTO STATION(name) VALUES('구로디지털단지역');
INSERT INTO STATION(name) VALUES('대림역');
INSERT INTO STATION(name) VALUES('신도림역');
INSERT INTO STATION(name) VALUES('구로역');
INSERT INTO STATION(name) VALUES('가산디지털단지역');
INSERT INTO SECTION(line_id, up_station_id, down_station_id, distance) VALUES(2, 1, 2, 10);
INSERT INTO SECTION(line_id, up_station_id, down_station_id, distance) VALUES(2, 2, 3, 10);
INSERT INTO SECTION(line_id, up_station_id, down_station_id, distance) VALUES(1, 3, 4, 10);
INSERT INTO SECTION(line_id, up_station_id, down_station_id, distance) VALUES(1, 4, 5, 10);
INSERT INTO SECTION(line_id, up_station_id, down_station_id, distance) VALUES(3, 1, 5, 1);