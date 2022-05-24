truncate table section;
drop table if exists station;

create table if not exists STATION
(
    id bigint auto_increment not null,
    name varchar(255) not null unique,
    deleted BOOLEAN not null,
    primary key(id)
);

insert into station (name, deleted) values ('선릉역', false);
insert into station (name, deleted) values ('잠실역', false);
insert into station (name, deleted) values ('강남역', false);

insert into line (name, color, extraFare, deleted) values ('1호선', 'blue', 0, false);

insert into section (line_id, up_station_id, down_station_id, distance, deleted) values (1L, 1L, 2L, 5, false);