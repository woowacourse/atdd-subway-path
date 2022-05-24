drop table if exists station;
drop table if exists line;

create table if not exists STATION
(
    id bigint auto_increment not null,
    name varchar(255) not null unique,
    deleted BOOLEAN not null,
    primary key(id)
    );

create table if not exists LINE
(
    id bigint auto_increment not null,
    name varchar(255) not null unique,
    color varchar(20) not null,
    extraFare int not null,
    deleted BOOLEAN not null,
    primary key(id)
    );

create table if not exists SECTION
(
    id bigint auto_increment not null,
    line_id bigint not null,
    up_station_id bigint not null,
    down_station_id bigint not null,
    distance int,
    deleted BOOLEAN not null,
    primary key(id)
    );

insert into station (name, deleted) values ('이대역', false);
insert into station (name, deleted) values ('학동역', false);
insert into station (name, deleted) values ('이수역', false);
insert into station (name, deleted) values ('건대역', false);
insert into station (name, deleted) values ('사가정역', false);

insert into line (name, color, extraFare, deleted) values ('1호선', 'blue', 0, false);
insert into line (name, color, extraFare, deleted) values ('2호선', 'green', 0, false);
insert into line (name, color, extraFare, deleted) values ('3호선', 'orange', 0, false);
insert into line (name, color, extraFare, deleted) values ('4호선', 'black', 0, false);

insert into section (line_id, up_station_id, down_station_id, distance, deleted) values (1L, 1L, 2L, 3, false);
insert into section (line_id, up_station_id, down_station_id, distance, deleted) values (2L, 2L, 3L, 3, false);
insert into section (line_id, up_station_id, down_station_id, distance, deleted) values (2L, 3L, 4L, 4, false);
insert into section (line_id, up_station_id, down_station_id, distance, deleted) values (3L, 4L, 5L, 5, false);
insert into section (line_id, up_station_id, down_station_id, distance, deleted) values (4L, 5L, 1L, 7, false);