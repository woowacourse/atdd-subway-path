drop table section if exists;
drop table line if exists;
drop table station if exists;

create table if not exists station
(
    id   bigint auto_increment not null,
    name varchar(255)          not null unique,
    primary key(id)
);

create table if not exists line
(
    id         bigint auto_increment not null,
    name       varchar(255)          not null unique,
    color      varchar(20)           not null,
    extra_fare int                   not null,
    primary key(id)
);

create table if not exists section
(
    id              bigint auto_increment not null,
    line_id         bigint                not null,
    up_station_id   bigint                not null,
    down_station_id bigint                not null,
    distance        int                   not null,
    primary key (id)
);

insert into station (name) values ('강남역');
insert into station (name) values ('역삼역');
insert into station (name) values ('교대역');

insert into line (name, color, extra_fare) values ('2호선', 'green', 0);

insert into section (line_id, up_station_id, down_station_id, distance) values (1, 1, 2, 10);
insert into section (line_id, up_station_id, down_station_id, distance) values (1, 2, 3, 8);
