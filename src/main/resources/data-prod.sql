insert into STATION (name) values
('봉천역'), ('사당역'), ('교대역'), ('강남역'), ('역삼역'), ('잠실역'),
('양재역'), ('판교역'), ('정자역'), ('고속터미널역'), ('신사역');

insert into LINE (name, color) values
('신분당선', 'red lighten-1'), ('2호선', 'green lighten-1'),
('3호선', 'orange lighten-1');

insert into SECTION(line_id, up_station_id, down_station_id, distance) values
(1, 4, 6, 5), (1, 6, 7, 5), (1, 7, 8, 5), -- 강남-양재-판교
(2, 1, 2, 10), (2, 2, 3, 10), (2, 3, 4, 10), (2, 4, 5, 10), (2, 5, 6, 10), -- 봉천-사당-교대-강남-역삼-잠실
(3, 3, 10, 20), (3, 10, 11, 20); -- 교대-고속터미널-신사

insert into MEMBER(email, password, age) values
('email@email.com', 'password', 10)
