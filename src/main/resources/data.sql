insert into station(name) values ('양재시민숲역');
insert into station(name) values ('양재역');
insert into station(name) values ('선릉역');
insert into station(name) values ('역삼역');
insert into station(name) values ('강남역');

insert into line(name, start_time, end_time, interval_time, background_color) values ('2호선', '10:00:00', '20:00:00', 10, 'bg-red-300');
insert into line(name, start_time, end_time, interval_time, background_color)  values ('신분당선', '10:00:00', '20:00:00', 10, 'bg-yellow-300');

insert into LINE_STATION(line, station_id, pre_station_id, distance, duration) values (1L, 5L, null, 0, 0);
insert into LINE_STATION(line, station_id, pre_station_id, distance, duration) values (1L, 4L, 5L, 15, 10);
insert into LINE_STATION(line, station_id, pre_station_id, distance, duration) values (1L, 3L, 4L, 5, 6);

insert into LINE_STATION(line, station_id, pre_station_id, distance, duration) values (2L, 5L, null, 0, 0);
insert into LINE_STATION(line, station_id, pre_station_id, distance, duration) values (2L, 2L, 5L, 15, 10);
insert into LINE_STATION(line, station_id, pre_station_id, distance, duration) values (2L, 1L, 2L, 5, 6);