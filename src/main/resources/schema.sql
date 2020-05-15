create table if not exists STATION
(
   id bigint auto_increment not null,
   name varchar(255) not null unique,
   created_at datetime,
   primary key(id)
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
    line bigint not null,
    station_id bigint not null,
    pre_station_id bigint,
    distance int,
    duration int,
    created_at datetime,
    updated_at datetime
);

-- insert into LINE (name, start_time, end_time, interval_time)
-- VALUES ('8호선', '05:40', '23:57', '8');
--
-- insert into STATION (name)
-- values ('암사역'),
--        ('천호역'),
--        ('몽촌토성역'),
--        ('잠실역');
--
-- insert into LINE_STATION (line, station_id, pre_station_id, distance, duration)
-- values (1, 1, null, 0, 0),
--        (1, 2, 1, 1, 2),
--        (1, 3, 2, 3, 4),
--        (1, 4, 3, 5, 6);
--
-- insert into LINE (name, start_time, end_time, interval_time)
-- VALUES ('7호선', '06:00', '23:59', '7');
--
-- insert into STATION (name)
-- values ('장암역'),
--        ('도봉산역'),
--        ('수락산역'),
--        ('마들역'),
--        ('노원역');
--
-- insert into LINE_STATION (line, station_id, pre_station_id, distance, duration)
-- values (2, 5, null, 7, 8),
--        (2, 6, 5, 9, 10),
--        (2, 7, 6, 11, 12),
--        (2, 8, 7, 13, 14),
--        (2, 9, 8, 15, 16);
