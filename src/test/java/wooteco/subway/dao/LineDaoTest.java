package wooteco.subway.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static wooteco.subway.Fixtures.COLOR1;
import static wooteco.subway.Fixtures.COLOR2;
import static wooteco.subway.Fixtures.COLOR3;
import static wooteco.subway.Fixtures.강남역;
import static wooteco.subway.Fixtures.부개역;
import static wooteco.subway.Fixtures.부평역;
import static wooteco.subway.Fixtures.삼호선;
import static wooteco.subway.Fixtures.선릉_삼성_구간;
import static wooteco.subway.Fixtures.역삼역;

import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import wooteco.subway.domain.fare.Fare;
import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.station.Station;
import wooteco.subway.dto.LineRequest;
import wooteco.subway.dto.LineResponse;
import wooteco.subway.service.LineService;

@JdbcTest
class LineDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private LineDao lineDao;

    private LineResponse savedLine;

    @BeforeEach
    void setUp() {
        lineDao = new LineDao(jdbcTemplate);
        StationDao stationDao = new StationDao(jdbcTemplate);
        LineService lineService = new LineService(lineDao, stationDao, new SectionDao(jdbcTemplate));

        Station 저장된_강남역 = stationDao.save(강남역);
        Station 저장된_역삼역 = stationDao.save(역삼역);
        Station 저장된_부평역 = stationDao.save(부평역);
        Station 저장된_부개역 = stationDao.save(부개역);

        savedLine = lineService.createLine(new LineRequest("1호선", COLOR1, 500, 저장된_부평역.getId(), 저장된_부개역.getId(), 10));
        lineService.createLine(new LineRequest("2호선", COLOR2, 500, 저장된_강남역.getId(), 저장된_역삼역.getId(), 10));
    }

    @Test
    void save() {
        Line savedLine = lineDao.save(삼호선);
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

        Line newLine = new Line("새로운 호선", COLOR3, new Fare(500), 선릉_삼성_구간);
        lineDao.update(targetLineId, newLine);

        assertAll(
                () -> assertThat(lineDao.findById(targetLineId).get().getName()).isEqualTo("새로운 호선"),
                () -> assertThat(lineDao.findById(targetLineId).get().getColor()).isEqualTo(COLOR3)
        );
    }

    @Test
    void deleteById() {
        lineDao.deleteById(savedLine.getId());

        assertThat(lineDao.findAll()).hasSize(1);
    }
}
