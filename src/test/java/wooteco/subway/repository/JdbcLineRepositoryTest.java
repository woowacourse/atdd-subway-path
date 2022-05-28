package wooteco.subway.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static wooteco.subway.Fixtures.COLOR1;
import static wooteco.subway.Fixtures.COLOR2;
import static wooteco.subway.Fixtures.COLOR3;
import static wooteco.subway.Fixtures.강남역;
import static wooteco.subway.Fixtures.부개역;
import static wooteco.subway.Fixtures.부평역;
import static wooteco.subway.Fixtures.삼호선;
import static wooteco.subway.Fixtures.역삼역;

import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import wooteco.subway.dao.LineDao;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.line.Line;
import wooteco.subway.dto.request.LineRequest;
import wooteco.subway.dto.response.LineResponse;
import wooteco.subway.service.LineService;

@JdbcTest
class JdbcLineRepositoryTest {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    private final LineService lineService;
    private LineResponse savedLine;

    @Autowired
    public JdbcLineRepositoryTest(JdbcTemplate jdbcTemplate) {
        StationDao stationDao = new StationDao(jdbcTemplate);
        SectionDao sectionDao = new SectionDao(jdbcTemplate);
        LineDao lineDao = new LineDao(jdbcTemplate);
        stationRepository = new JdbcStationRepository(stationDao);
        JdbcSectionRepository sectionRepository = new JdbcSectionRepository(sectionDao, stationRepository);
        lineRepository = new JdbcLineRepository(lineDao, sectionRepository);
        lineService = new LineService(lineRepository, stationRepository, sectionRepository);
    }

    @BeforeEach
    void setUp() {
        Long 저장된_강남역_ID = stationRepository.save(강남역);
        Long 저장된_역삼역_ID = stationRepository.save(역삼역);
        Long 저장된_부평역_ID = stationRepository.save(부평역);
        Long 저장된_부개역_ID = stationRepository.save(부개역);

        savedLine = lineService.createLine(new LineRequest("1호선", COLOR1, 500, 저장된_부평역_ID, 저장된_부개역_ID, 10));
        lineService.createLine(new LineRequest("2호선", COLOR2, 500, 저장된_강남역_ID, 저장된_역삼역_ID, 10));
    }

    @Test
    void save() {
        Long savedLine = lineRepository.save(삼호선);
        assertThat(savedLine).isNotNull();
    }

    @Test
    void findAll() {
        List<Line> lines = lineRepository.findAll();
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
        Line actual = lineRepository.findById(savedLine.getId()).get();

        assertAll(
                () -> assertThat(actual.getName()).isEqualTo(savedLine.getName()),
                () -> assertThat(actual.getColor()).isEqualTo(savedLine.getColor())
        );
    }

    @Test
    void update() {
        Long targetLineId = savedLine.getId();

        lineRepository.update(targetLineId, "새로운 호선", COLOR3);

        assertAll(
                () -> assertThat(lineRepository.findById(targetLineId).get().getName()).isEqualTo("새로운 호선"),
                () -> assertThat(lineRepository.findById(targetLineId).get().getColor()).isEqualTo(COLOR3)
        );
    }

    @Test
    void deleteById() {
        lineRepository.deleteById(savedLine.getId());

        assertThat(lineRepository.findAll()).hasSize(1);
    }
}
