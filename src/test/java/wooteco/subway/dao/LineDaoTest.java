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
import static wooteco.subway.Fixtures.역삼역;

import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import wooteco.subway.dto.request.LineRequest;
import wooteco.subway.dto.response.LineResponse;
import wooteco.subway.entity.LineEntity;
import wooteco.subway.repository.JdbcLineRepository;
import wooteco.subway.repository.JdbcSectionRepository;
import wooteco.subway.repository.JdbcStationRepository;
import wooteco.subway.repository.LineRepository;
import wooteco.subway.repository.StationRepository;
import wooteco.subway.service.LineService;

@JdbcTest
class LineDaoTest {

    private final LineDao lineDao;
    private final StationRepository stationRepository;
    private final LineService lineService;

    private LineResponse savedLine;

    @Autowired
    public LineDaoTest(JdbcTemplate jdbcTemplate) {
        StationDao stationDao = new StationDao(jdbcTemplate);
        SectionDao sectionDao = new SectionDao(jdbcTemplate);
        lineDao = new LineDao(jdbcTemplate);
        stationRepository = new JdbcStationRepository(stationDao);
        JdbcSectionRepository sectionRepository = new JdbcSectionRepository(sectionDao, stationRepository);
        LineRepository lineRepository = new JdbcLineRepository(lineDao, sectionRepository);
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
        Long id = lineDao.save(삼호선);
        assertThat(id).isNotNull();
    }

    @DisplayName("findAll 메소드는 데이터베이스의 모든 노선을 LineEntity 리스트로 반환한다.")
    @Test
    void findAll() {
        List<LineEntity> lines = lineDao.findAll();
        List<String> lineNames = lines.stream()
                .map(LineEntity::getName)
                .collect(Collectors.toList());
        List<String> lineColors = lines.stream()
                .map(LineEntity::getColor)
                .collect(Collectors.toList());
        List<Integer> extraFares = lines.stream()
                .map(LineEntity::getExtraFare)
                .collect(Collectors.toList());

        assertAll(
                () -> assertThat(lineNames).containsAll(List.of("1호선", "2호선")),
                () -> assertThat(lineColors).containsAll(List.of("bg-blue-600", "bg-green-600")),
                () -> assertThat(extraFares).containsAll(List.of(500, 500))
        );
    }

    @DisplayName("findById 메소드는 id가 일치하는 노선을 데이터베이스에서 찾아 LineEntity로 반환한다.")
    @Test
    void findById() {
        LineEntity actual = lineDao.findById(savedLine.getId());

        assertAll(
                () -> assertThat(actual.getName()).isEqualTo(savedLine.getName()),
                () -> assertThat(actual.getColor()).isEqualTo(savedLine.getColor()),
                () -> assertThat(actual.getExtraFare()).isEqualTo(savedLine.getExtraFare())
        );
    }

    @DisplayName("findByName 메소드는 name이 일치하는 노선을 데이터베이스에서 찾아 LineEntity로 반환한다.")
    @Test
    void findByName() {
        LineEntity actual = lineDao.findByName(savedLine.getName());

        assertAll(
                () -> assertThat(actual.getName()).isEqualTo(savedLine.getName()),
                () -> assertThat(actual.getColor()).isEqualTo(savedLine.getColor()),
                () -> assertThat(actual.getExtraFare()).isEqualTo(savedLine.getExtraFare())
        );
    }

    @DisplayName("update 메소드는 수정할 노선의 id와 새로운 이름, 색상을 전달받아 데이터베이스를 업데이트한다.")
    @Test
    void update() {
        Long targetLineId = savedLine.getId();

        lineDao.update(targetLineId, "새로운 호선", COLOR3);

        assertAll(
                () -> assertThat(lineDao.findById(targetLineId).getName()).isEqualTo("새로운 호선"),
                () -> assertThat(lineDao.findById(targetLineId).getColor()).isEqualTo(COLOR3)
        );
    }

    @DisplayName("deleteById 노선 id를 전달받아 데이터베이스에서 제거한다.")
    @Test
    void deleteById() {
        lineDao.deleteById(savedLine.getId());

        assertThat(lineDao.findAll()).hasSize(1);
    }
}
