drop table "SECTION" if exists;
drop table LINE if exists;
drop table STATION if exists;

create table if not exists STATION
(
    id bigint auto_increment not null,
    name varchar(255) not null unique,
    primary key(id)
    );

create table if not exists LINE
(
    id bigint auto_increment not null,
    name varchar(255) not null unique,
    color varchar(20) not null unique,
    extra_fare int default 0,
    primary key(id)
    );

create table if not exists "SECTION"
(
    id bigint auto_increment not null,
    line_id bigint not null,
    up_station_id bigint not null,
    down_station_id bigint not null,
    distance int,
    line_order bigint not null,
    primary key (id),
    foreign key (line_id) references LINE(id) on delete cascade,
    foreign key (up_station_id) references STATION(id),
    foreign key (down_station_id) references STATION(id)
    );

INSERT INTO STATION (name)
VALUES ('신도림역');
INSERT INTO STATION (name)
VALUES ('왕십리역');
INSERT INTO STATION (name)
VALUES ('용산역');
INSERT INTO STATION (name)
VALUES ('역곡역');
INSERT INTO STATION (name)
VALUES ('강남역');
INSERT INTO STATION (name)
VALUES ('청계산입구역');
INSERT INTO STATION (name)
VALUES ('상일동역');

INSERT INTO LINE (name, color)
VALUES ('신분당선', 'red');
INSERT INTO SECTION (line_id, up_station_id, down_station_id, distance, line_order)
VALUES (1, 5, 6, 10, 1);

INSERT INTO LINE (name, color)
VALUES ('2호선', 'green');
INSERT INTO SECTION (line_id, up_station_id, down_station_id, distance, line_order)
VALUES (2, 1, 2, 30, 1);

INSERT INTO LINE (name, color)
VALUES ('5호선', 'purple');
INSERT INTO SECTION (line_id, up_station_id, down_station_id, distance, line_order)
VALUES (3, 2, 7, 50, 1);

INSERT INTO LINE (name, color)
VALUES ('1호선', 'dark-blue');
INSERT INTO SECTION (line_id, up_station_id, down_station_id, distance, line_order)
VALUES (1, 3, 4, 20, 1);
