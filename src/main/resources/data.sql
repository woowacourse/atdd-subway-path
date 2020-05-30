insert into STATION (id, name)
values (1, '1역'),
       (2, '2역'),
       (3, '3역'),
       (4, '4역'),
       (5, '5역');

insert into LINE (id, name, start_time, end_time, interval_time, bg_color)
values (1, '1호선', '05:30', '05:30', 10, 'bg-teal-500'),
       (2, '2호선', '05:30', '05:30', 10, 'bg-teal-300');

insert into LINE_STATION (line, pre_station_id, station_id, distance, duration)
values (1, null, 1, 0, 0),
       (1, 1, 2, 10, 2),
       (1, 2, 3, 10, 0),
       (2, null, 2, 0, 0),
       (2, 2, 4, 2, 0),
       (2, 4, 5, 2, 2),
       (2, 5, 3, 2, 0);
