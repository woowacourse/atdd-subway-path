package wooteco.subway.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.SectionEntity;

@JdbcTest
public class JdbcSectionDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private SectionDao sectionDao;
    private StationDao stationDao;
    private LineDao lineDao;

    @BeforeEach
    void setUp() {
        sectionDao = new JdbcSectionDao(jdbcTemplate);
        stationDao = new JdbcStationDao(jdbcTemplate);
        lineDao = new JdbcLineDao(jdbcTemplate);
    }

    @DisplayName("지하철 구간을 생성한다.")
    @Test
    void save() {
        Long lineId = lineDao.save(new Line("2호선", "green", 900)).getId();
        Station station1 = stationDao.save(new Station("선릉역"));
        Station station2 = stationDao.save(new Station("강남역"));
        Section section = new Section(station1, station2, 10);

        sectionDao.save(lineId, section);

        assertThat(sectionDao.findByLine(lineId).size()).isEqualTo(1);
    }

    @DisplayName("지하철 구간을 라인별로 찾는다.")
    @Test
    void findByLine() {
        Long lineId = lineDao.save(new Line("2호선", "green", 900)).getId();
        Station station1 = stationDao.save(new Station("선릉역"));
        Station station2 = stationDao.save(new Station("강남역"));
        Section section = new Section(station1, station2, 10);

        sectionDao.save(lineId, section);
        sectionDao.save(lineId, section);
        sectionDao.save(lineId, section);

        assertThat(sectionDao.findByLine(lineId)).hasSize(3);
    }

    @DisplayName("지하철 구간을 수정한다.")
    @Test
    void update() {
        Long lineId = lineDao.save(new Line("2호선", "green", 900)).getId();
        Station station1 = stationDao.save(new Station("선릉역"));
        Station station2 = stationDao.save(new Station("강남역"));
        Station station3 = stationDao.save(new Station("역삼역"));
        Section section = new Section(station1, station2, 10);

        SectionEntity sectionEntity = sectionDao.save(lineId, section);
        Section newSection = new Section(sectionEntity.getId(), station3, station2, 11);

        sectionDao.update(lineId, newSection);

        SectionEntity updateSection = sectionDao.findById(sectionEntity.getId());
        assertAll(
                () -> assertThat(updateSection.getUpStationId()).isEqualTo(station3.getId()),
                () -> assertThat(updateSection.getDownStationId()).isEqualTo(station2.getId()),
                () -> assertThat(updateSection.getDistance()).isEqualTo(11)
        );
    }

    @DisplayName("특정 노선의 구간들을 삭제한다.")
    @Test
    void deleteAll() {
        Long lineId = lineDao.save(new Line("2호선", "green", 900)).getId();
        Station station1 = stationDao.save(new Station("선릉역"));
        Station station2 = stationDao.save(new Station("강남역"));
        Section section = new Section(station1, station2, 10);

        sectionDao.save(lineId, section);
        assertThat(sectionDao.findByLine(lineId)).hasSize(1);

        sectionDao.deleteAll(lineId);
        assertThat(sectionDao.findByLine(lineId)).hasSize(0);
    }

    @DisplayName("특정 구간을 삭제한다.")
    @Test
    void delete() {
        Long lineId = lineDao.save(new Line("2호선", "green", 900)).getId();
        Station station1 = stationDao.save(new Station("선릉역"));
        Station station2 = stationDao.save(new Station("강남역"));
        Section section = new Section(station1, station2, 10);

        sectionDao.save(lineId, section);
        sectionDao.save(lineId, section);

        List<SectionEntity> sectionEntities = sectionDao.findByLine(lineId);
        SectionEntity sectionEntity = sectionEntities.get(0);

        Section section1 = new Section(sectionEntity.getId(), station1, station2, 10);
        sectionDao.delete(lineId, section1);

        assertThat(sectionDao.findByLine(lineId)).hasSize(1);
    }

    @DisplayName("해당역을 지나는 Section이 있는지 확인한다")
    @Test
    void existSectionUsingStation() {
        Long lineId = lineDao.save(new Line("2호선", "green", 900)).getId();
        Station station1 = stationDao.save(new Station("선릉역"));
        Station station2 = stationDao.save(new Station("강남역"));
        Section section = new Section(station1, station2, 10);

        sectionDao.save(lineId, section);

        assertThat(sectionDao.existSectionUsingStation(station1.getId())).isTrue();
    }

    @DisplayName("해당역을 지나는 Section이 있는지 확인한다_없는 경우")
    @Test
    void existSectionUsingStation_false() {
        Long lineId = lineDao.save(new Line("2호선", "green", 900)).getId();
        Station station1 = stationDao.save(new Station("선릉역"));
        Station station2 = stationDao.save(new Station("강남역"));
        Section section = new Section(station1, station2, 10);

        sectionDao.save(lineId, section);

        assertThat(sectionDao.existSectionUsingStation(3L)).isFalse();
    }
}
