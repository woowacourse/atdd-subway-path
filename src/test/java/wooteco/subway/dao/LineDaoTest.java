package wooteco.subway.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import wooteco.subway.domain.Distance;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.LineRequest;
import wooteco.subway.dto.LineResponse;
import wooteco.subway.service.LineService;

@JdbcTest
class LineDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private LineDao lineDao;

    private Station station1;
    private Station station2;
    private Station station3;
    private Station station4;

    private LineResponse savedLine;

    @BeforeEach
    void setUp() {
        lineDao = new LineDao(jdbcTemplate);
        StationDao stationDao = new StationDao(jdbcTemplate);
        LineService lineService = new LineService(lineDao, stationDao, new SectionDao(jdbcTemplate));

        station1 = new Station("역일역");
        station2 = new Station("역이역");
        station3 = new Station("역삼역");
        station4 = new Station("역사역");

        Station savedStation1 = stationDao.save(station1);
        Station savedStation2 = stationDao.save(station2);
        Station savedStation3 = stationDao.save(station3);
        Station savedStation4 = stationDao.save(station4);

        savedLine = lineService.createLine(
                new LineRequest("1호선", "bg-blue-600", savedStation1.getId(), savedStation2.getId(), 10));
        lineService.createLine(
                new LineRequest("2호선", "bg-green-600", savedStation3.getId(), savedStation4.getId(), 10));
    }

    @Test
    void save() {
        Line line = new Line("신분당선", "bg-red-600", new Section(station1, station2, new Distance(10)));
        Line savedLine = lineDao.save(line);

        assertThat(savedLine).isNotNull();
    }

    @Test
    void findAll() {
        List<Line> lines = lineDao.findAll();
        List<String> lineNames = lines.stream()
                .map(Line::getName)
                .collect(Collectors.toList());
        List<String> lineColors = lines.stream()
                .map(Line::getColor)
                .collect(Collectors.toList());

        assertAll(
                () -> assertThat(lineNames).containsAll(List.of("1호선", "2호선")),
                () -> assertThat(lineColors).containsAll(List.of("bg-blue-600", "bg-green-600"))
        );
    }

    @Test
    void findById() {
        Line actual = lineDao.findById(savedLine.getId()).get();

        assertAll(
                () -> assertThat(actual.getName()).isEqualTo(savedLine.getName()),
                () -> assertThat(actual.getColor()).isEqualTo(savedLine.getColor())
        );
    }

    @Test
    void update() {
        Long targetLineId = savedLine.getId();

        String newLineName = "새로운 노선";
        String newLineColor = "bg-red-500";
        Line newLine = new Line(newLineName, newLineColor, new Section(station2, station4, new Distance(10)));
        lineDao.update(targetLineId, newLine);

        assertAll(
                () -> assertThat(lineDao.findById(targetLineId).get().getName()).isEqualTo(newLineName),
                () -> assertThat(lineDao.findById(targetLineId).get().getColor()).isEqualTo(newLineColor)
        );
    }

    @Test
    void deleteById() {
        lineDao.deleteById(savedLine.getId());

        assertThat(lineDao.findAll()).hasSize(1);
    }
}
