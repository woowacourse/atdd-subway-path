DELETE
FROM Section;
DELETE
FROM Line;
DELETE
FROM Station;

ALTER TABLE Section
    ALTER COLUMN id RESTART WITH 1;
ALTER TABLE Line
    ALTER COLUMN id RESTART WITH 1;
ALTER TABLE Station
    ALTER COLUMN id RESTART WITH 1;
