package wooteco.subway.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.Station;

@JdbcTest
class SectionDaoTest {

    private static final Station 강남역 = new Station("강남역");
    private static final Station 선릉역 = new Station("선릉역");
    private static final Line _2호선 = new Line("2호선", "green", 0);
    private static final Station 수서역 = new Station("수서역");
    private static final Station 가락시장역 = new Station("가락시장역");
    private static final Line _3호선 = new Line("3호선", "orange", 0);
    private static final int DISTANCE = 10;

    private final StationDao stationDao;
    private final LineDao lineDao;
    private final SectionDao sectionDao;

    private Station upStation;
    private Station downStation;
    private Line line;
    private Section section;

    @Autowired
    public SectionDaoTest(NamedParameterJdbcTemplate jdbcTemplate) {
        this.stationDao = new StationDao(jdbcTemplate);
        this.lineDao = new LineDao(jdbcTemplate);
        this.sectionDao = new SectionDao(jdbcTemplate);
    }

    @BeforeEach
    void setUp() {
        this.upStation = stationDao.save(강남역);
        this.downStation = stationDao.save(선릉역);
        this.line = lineDao.save(_2호선);
        this.section = sectionDao.save(new Section(line.getId(), upStation.getId(), downStation.getId(), DISTANCE));
    }

    @DisplayName("구간을 저장한다.")
    @Test
    void save() {
        assertAll(
                () -> assertThat(section.getLineId()).isEqualTo(line.getId()),
                () -> assertThat(section.getUpStationId()).isEqualTo(upStation.getId()),
                () -> assertThat(section.getDownStationId()).isEqualTo(downStation.getId()),
                () -> assertThat(section.getDistance()).isEqualTo(DISTANCE)
        );
    }

    @DisplayName("노선 id 로 구간을 찾아낸다.")
    @Test
    void findAllByLineId() {
        assertThat(sectionDao.findAllByLineId(line.getId()).size()).isEqualTo(1);
    }

    @DisplayName("같은 상행 종점을 가진 구간을 찾아낸다.")
    @Test
    void findBySameUpStation() {
        final Section sameUpStation = sectionDao.findBy(section.getLineId(), upStation.getId(), 10L)
                .orElseThrow();

        assertThat(sameUpStation.getDownStationId()).isEqualTo(downStation.getId());
    }

    @DisplayName("같은 하행 종점을 가진 구간을 찾아낸다.")
    @Test
    void findBySameDownStation() {
        final Section sameDownStation = sectionDao.findBy(section.getLineId(), 10L, downStation.getId())
                .orElseThrow();

        assertThat(sameDownStation.getUpStationId()).isEqualTo(upStation.getId());
    }

    @DisplayName("같은 상행 또는 하행 종점을 가진 구간을 찾아내지 못할 경우 빈값을 반환한다.")
    @Test
    void findByNotExistingSection() {
        final Optional<Section> section = sectionDao.findBy(1L, 9L, 10L);

        assertThat(section).isEmpty();
    }

    @Test
    @DisplayName("id로 노선을 삭제한다.")
    void deleteById() {
        sectionDao.deleteById(section.getId());

        assertThat(sectionDao.findAllByLineId(line.getId()).size()).isZero();
    }

    @DisplayName("모든 구간을 찾아낸다.")
    @Test
    void findAll() {
        final Station upStation = stationDao.save(수서역);
        final Station downStation = stationDao.save(가락시장역);
        final Line line = lineDao.save(_3호선);

        final Section section = sectionDao.save(
                new Section(line.getId(), upStation.getId(), downStation.getId(), DISTANCE));

        final Sections expected = new Sections(List.of(this.section, section));

        assertThat(sectionDao.findAll()).isEqualTo(expected);
    }

    @DisplayName("모든 구간을 저장한다.")
    @Test
    void saveAll() {
        final Station upStation = stationDao.save(수서역);
        final Station downStation = stationDao.save(가락시장역);
        final Line line = lineDao.save(_3호선);

        sectionDao.saveAll(List.of(new Section(line.getId(), upStation.getId(), downStation.getId(), DISTANCE)));

        assertThat(sectionDao.findAll().size()).isEqualTo(2);
    }
}
