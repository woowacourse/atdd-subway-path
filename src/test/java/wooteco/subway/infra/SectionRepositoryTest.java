package wooteco.subway.infra;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static wooteco.subway.SubwayFixtures.GANGNAM_TO_YEOKSAM;
import static wooteco.subway.SubwayFixtures.SUNNEUNG_TO_SUNGDAM;
import static wooteco.subway.SubwayFixtures.YEOKSAM_TO_SUNNEUNG;
import static wooteco.subway.SubwayFixtures.강남역;
import static wooteco.subway.SubwayFixtures.선릉역;
import static wooteco.subway.SubwayFixtures.성담빌딩;
import static wooteco.subway.SubwayFixtures.역삼역;

import java.util.List;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import org.springframework.test.context.jdbc.Sql;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.Station;
import wooteco.subway.infra.dao.SectionDao;
import wooteco.subway.infra.repository.JdbcSectionRepository;
import wooteco.subway.infra.repository.SectionRepository;

@DisplayName("Section 레포지토리")
@Sql("classpath:/schema-test.sql")
@TestConstructor(autowireMode = AutowireMode.ALL)
@JdbcTest
class SectionRepositoryTest {

    private final SectionRepository sectionRepository;
    private final JdbcTemplate jdbcTemplate;

    public SectionRepositoryTest(DataSource dataSource) {
        final JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        this.sectionRepository = new JdbcSectionRepository(
                new SectionDao(jdbcTemplate, dataSource, new NamedParameterJdbcTemplate(dataSource))
        );
        this.jdbcTemplate = jdbcTemplate;
    }

    @BeforeEach
    void setup() {
        jdbcTemplate.update("INSERT INTO Line(name, color) VALUES('4호선', 'bg-600-blue')");
        jdbcTemplate.update("INSERT INTO Line(name, color) VALUES('2호선', 'bg-600-green')");
        jdbcTemplate.update("INSERT INTO Station(name) VALUES('강남역')");
        jdbcTemplate.update("INSERT INTO Station(name) VALUES('역삼역')");
        jdbcTemplate.update("INSERT INTO Station(name) VALUES('선릉역')");
        jdbcTemplate.update("INSERT INTO Station(name) VALUES('삼성역')");
        jdbcTemplate.update("INSERT INTO Station(name) VALUES('성담빌딩')");
    }

    @DisplayName("노선 내 구간 변경 시 사용되는 Sections 저장 테스트")
    @Test
    void saveSections() {
        // given
        final Sections sections = new Sections(List.of(GANGNAM_TO_YEOKSAM, YEOKSAM_TO_SUNNEUNG, SUNNEUNG_TO_SUNGDAM));
        final int expectedSize = sections.getSections().size();
        final List<Section> expectedSections = sections.getSections();

        // when
        sectionRepository.save(sections);
        final Sections sectionsByLineId = sectionRepository.findByLineId(GANGNAM_TO_YEOKSAM.getLineId());

        // then
        final List<Section> savedSections = sectionsByLineId.getSections();

        assertAll(
                () -> assertThat(savedSections.size()).isEqualTo(expectedSize),
                () -> assertThat(savedSections).isEqualTo(expectedSections)
        );
    }

    @DisplayName("신규 노선 추가 시 사용되는 단일 구간 추가 테스트")
    @Test
    void saveSection() {
        // given
        final Long lineId = 2L;
        final Section section = new Section(lineId, 강남역, 역삼역, 10);

        // when
        sectionRepository.save(lineId, section);
        final Sections sectionsByLineId = sectionRepository.findByLineId(lineId);
        final List<Section> sectionsSaved = sectionsByLineId.getSections();
        final List<Station> stationsBySectionsSaved = sectionsByLineId.getStations();

        // then
        assertAll(
                () -> assertThat(sectionsSaved.size()).isOne(),
                () -> assertThat(stationsBySectionsSaved).containsExactly(강남역, 역삼역)
        );
    }

    @DisplayName("노선 조회 시 노선에 조합하기 위해 사용되는 전체 구간 목록 조회 테스트")
    @Test
    void findAll() {
        // given
        final Sections sections = new Sections(List.of(GANGNAM_TO_YEOKSAM, SUNNEUNG_TO_SUNGDAM, YEOKSAM_TO_SUNNEUNG));
        sectionRepository.save(sections);

        // when
        final List<Sections> allSectionsSaved = sectionRepository.findAll();
        final Sections singleSectionsSaved = allSectionsSaved.get(0);

        // then
        final List<Section> sectionsFound = singleSectionsSaved.getSections();
        final List<Station> stations = singleSectionsSaved.getStations();

        assertAll(
                () -> assertThat(allSectionsSaved.size()).isOne(),
                () -> assertThat(sectionsFound).containsExactly(
                        SUNNEUNG_TO_SUNGDAM, YEOKSAM_TO_SUNNEUNG, GANGNAM_TO_YEOKSAM
                ),
                () -> assertThat(stations).containsExactly(성담빌딩, 선릉역, 역삼역, 강남역)
        );
    }

    @DisplayName("단일 노선 조회 시 노선에 조합하기 위해 사용되는 노선 내 구간 조회 테스트")
    @Test
    void findByLineId() {
        // given
        final Sections sections = new Sections(List.of(GANGNAM_TO_YEOKSAM, SUNNEUNG_TO_SUNGDAM, YEOKSAM_TO_SUNNEUNG));
        sectionRepository.save(sections);

        // when
        final Sections sectionsByLineId = sectionRepository.findByLineId(GANGNAM_TO_YEOKSAM.getLineId());

        // then
        final List<Section> sectionsFound = sectionsByLineId.getSections();
        final List<Station> stations = sectionsByLineId.getStations();

        assertAll(
                () -> assertThat(sectionsFound).containsExactly(
                        SUNNEUNG_TO_SUNGDAM, YEOKSAM_TO_SUNNEUNG, GANGNAM_TO_YEOKSAM
                ),
                () -> assertThat(stations).containsExactly(성담빌딩, 선릉역, 역삼역, 강남역)
        );
    }
}
