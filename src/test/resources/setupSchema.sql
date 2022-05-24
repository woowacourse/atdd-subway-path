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

insert into station (name, deleted) values ('선릉역', false);
insert into station (name, deleted) values ('잠실역', false);
insert into station (name, deleted) values ('강남역', false);