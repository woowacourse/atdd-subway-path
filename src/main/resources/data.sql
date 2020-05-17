insert into line (name, start_time, end_time, interval_time)
values ('1호선', '11:00:00', '23:00:00', 10);
insert into line (name, start_time, end_time, interval_time)
values ('2호선', '11:00:00', '23:00:00', 10);

insert into station (name) values ('삼성역');
insert into station (name) values ('석촌역');
insert into station (name) values ('잠실역');
insert into station (name) values ('강남역');
insert into station (name) values ('역삼역');

insert into edge (line, line_key, pre_station_id, station_id, distance, duration)
values (1, 0, null, 1, 0, 0);
insert into edge (line, line_key, pre_station_id, station_id, distance, duration)
values (1, 1, 1, 2, 10, 10);
insert into edge (line, line_key, pre_station_id, station_id, distance, duration)
values (1, 2, 2, 3, 5, 20);