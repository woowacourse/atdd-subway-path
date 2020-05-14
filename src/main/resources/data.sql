insert into STATION (name) values ('강남역');
insert into STATION (name) values ('역삼역');
insert into STATION (name) values ('선릉역');
insert into STATION (name) values ('잠실역');

insert into LINE (name, start_time, end_time, interval_time) values ('2호선', '07:00:00', '19:00:00', '2');

insert into LINE_STATION (line, pre_station_id, station_id, distance, duration) values (1, null, 1, 10, 5);
insert into LINE_STATION (line, pre_station_id, station_id, distance, duration) values (1, 1, 2, 10, 5);
insert into LINE_STATION (line, pre_station_id, station_id, distance, duration) values (1, 2, 3, 10, 5);
insert into LINE_STATION (line, pre_station_id, station_id, distance, duration) values (1, 3, 4, 10, 5);