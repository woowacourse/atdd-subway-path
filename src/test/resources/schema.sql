create table if not exists STATION
(
    id   bigint auto_increment not null,
    name varchar(255)          not null unique,
    primary key (id)
    );

create table if not exists LINE
(
    id    bigint auto_increment not null,
    name  varchar(255)          not null unique,
    color varchar(20)           not null,
    extraFare int,
    primary key (id)
    );

create table if not exists SECTION
(
    id              bigint auto_increment not null,
    line_id         bigint                not null,
    up_station_id   bigint                not null,
    down_station_id bigint                not null,
    distance        int,
    primary key (id)
    );

TRUNCATE TABLE station RESTART IDENTITY;
ALTER TABLE station ALTER COLUMN id RESTART WITH 1;

TRUNCATE TABLE line RESTART IDENTITY;
ALTER TABLE line ALTER COLUMN id RESTART WITH 1;

TRUNCATE TABLE section RESTART IDENTITY;
ALTER TABLE section ALTER COLUMN id RESTART WITH 1;

/*insert into station (name) values ('강남역');
insert into station (name) values ('왕십리역');
insert into line (name, color) values ('신분당선','red');
insert into line (name, color) values ('2호선','green');
insert into section (line_id, up_station_id, down_station_id, distance) values (1, 2, 1, 5);*/


insert into station (name) values ('1');
insert into station (name) values ('2');
insert into station (name) values ('3');
insert into station (name) values ('4');
insert into station (name) values ('5');
insert into station (name) values ('6');
insert into station (name) values ('7');
insert into station (name) values ('8');
insert into line (name, color, extraFare) values ('1호선', 'black', 200);
insert into line (name, color, extraFare) values ('2호선', 'blue', 100);
insert into line (name, color, extraFare) values ('3호선', 'red', 0);

-- 1호선
insert into section (line_id, up_station_id, down_station_id, distance) values (1, 1, 2, 2);
insert into section (line_id, up_station_id, down_station_id, distance) values (1, 2, 3, 2);
insert into section (line_id, up_station_id, down_station_id, distance) values (1, 3, 4, 2);
insert into section (line_id, up_station_id, down_station_id, distance) values (1, 4, 5, 2);

-- 2호선
insert into section (line_id, up_station_id, down_station_id, distance) values (2, 6, 7, 1);
insert into section (line_id, up_station_id, down_station_id, distance) values (2, 7, 3, 1);
insert into section (line_id, up_station_id, down_station_id, distance) values (2, 3, 8, 1);

-- 3호선
insert into section (line_id, up_station_id, down_station_id, distance) values (3, 2, 4, 1);
