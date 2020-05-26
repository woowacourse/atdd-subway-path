drop table if exists STATION;
drop table if exists LINE;
drop table if exists LINE_STATION;

create table if not exists STATION
(
    id         bigint auto_increment not null,
    name       varchar(255)          not null unique,
    created_at datetime,
    primary key (id)
);

create table if not exists LINE
(
   id bigint auto_increment not null,
   name varchar(255) not null,
   start_time time not null,
   end_time time not null,
   interval_time int not null,
   created_at datetime,
   updated_at datetime,
   primary key(id)
);

create table if not exists LINE_STATION
(
    line           bigint not null,
    station_id     bigint not null,
    pre_station_id bigint,
    distance       int,
    duration       int,
    created_at     datetime,
    updated_at     datetime
);

ALTER TABLE LINE
    ALTER COLUMN id RESTART WITH 1;
ALTER TABLE STATION
    ALTER COLUMN id RESTART WITH 1;
