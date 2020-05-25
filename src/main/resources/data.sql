insert into STATION (name) values ('강남역');
insert into STATION (name) values ('역삼역');
insert into STATION (name) values ('선릉역');
insert into STATION (name) values ('잠실역');
insert into STATION (name) values ('서울역');
insert into STATION (name) values ('용산역');
insert into STATION (name) values ('노량진역');
insert into STATION (name) values ('시청역');

insert into LINE (name, start_time, end_time, interval_time) values ('2호선', '07:00:00', '19:00:00', '2');
insert into LINE (name, start_time, end_time, interval_time) values ('1호선', '08:00:00', '20:00:00', '3');

insert into LINE_STATION (line, pre_station_id, station_id, distance, duration) values (1, null, 1, 10, 5);
insert into LINE_STATION (line, pre_station_id, station_id, distance, duration) values (1, 1, 2, 10, 5);
insert into LINE_STATION (line, pre_station_id, station_id, distance, duration) values (1, 2, 6, 7, 7);
insert into LINE_STATION (line, pre_station_id, station_id, distance, duration) values (1, 6, 3, 10, 5);
insert into LINE_STATION (line, pre_station_id, station_id, distance, duration) values (1, 3, 4, 10, 5);

insert into LINE_STATION (line, pre_station_id, station_id, distance, duration) values (2, null, 5, 7, 7);
insert into LINE_STATION (line, pre_station_id, station_id, distance, duration) values (2, 5, 6, 7, 7);
insert into LINE_STATION (line, pre_station_id, station_id, distance, duration) values (2, 6, 7, 7, 7);
insert into LINE_STATION (line, pre_station_id, station_id, distance, duration) values (2, 7, 8, 7, 7);