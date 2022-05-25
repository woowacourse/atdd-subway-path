package wooteco.subway.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import wooteco.subway.domain.element.Line;
import wooteco.subway.domain.element.Section;
import wooteco.subway.domain.element.Station;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SectionRepositoryTest {

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private LineRepository lineRepository;

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void setUp() {
        jdbcTemplate.update("delete from section");
        jdbcTemplate.update("delete from line");
        jdbcTemplate.update("delete from station");
    }

    @Test
    @DisplayName("구간을 노선으로 조회한다.")
    void create() {
        Station 성수역 = stationRepository.save(new Station("성수역"));
        Station 건대입구 = stationRepository.save(new Station("건대입구"));
        Line line = lineRepository.save(new Line(1L, "1호선", "blue", 0));

        sectionRepository.save(new Section(line, 성수역, 건대입구, 10));

        List<Section> section = sectionRepository.findSectionByLine(line);
        assertThat(section).hasSize(1);
        assertThat(section.get(0).getUpStation()).isEqualTo(성수역);
        assertThat(section.get(0).getDownStation()).isEqualTo(건대입구);
    }

    @Test
    @DisplayName("구간 전체를 조회한다.")
    void findAll() {
        Station 성수역 = stationRepository.save(new Station("성수역"));
        Station 건대입구 = stationRepository.save(new Station("건대입구"));
        Line line = lineRepository.save(new Line("1호선", "blue", 0));

        Section section = new Section(line, 성수역, 건대입구, 10);
        sectionRepository.save(section);

        assertThat(sectionRepository.findAll()).containsOnly(section);
    }

    @Test
    @DisplayName("해당 노선의 모든 구간을 삭제한다.")
    void deleteSectionByLineId() {
        Station 성수역 = stationRepository.save(new Station("성수역"));
        Station 건대입구 = stationRepository.save(new Station("건대입구"));
        Line line = lineRepository.save(new Line("1호선", "blue", 0));

        Section section = new Section(line, 성수역, 건대입구, 10);
        sectionRepository.save(section);
        sectionRepository.deleteSectionByLineId(line.getId());

        assertThat(sectionRepository.findSectionByLine(line)).isEmpty();
    }
}
