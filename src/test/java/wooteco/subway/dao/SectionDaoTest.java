package wooteco.subway.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;

@JdbcTest
class SectionDaoTest {

    private final SectionDao sectionDao;
    private final LineDao lineDao;
    private final StationDao stationDao;

    @Autowired
    private SectionDaoTest(final NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.sectionDao = new SectionDao(namedParameterJdbcTemplate);
        this.lineDao = new LineDao(namedParameterJdbcTemplate);
        this.stationDao = new StationDao(namedParameterJdbcTemplate);
    }

    @DisplayName("특정 노선의 구간을 저장한다.")
    @Test
    void save() {
        // given
        final Station station1 = stationDao.save(new Station("아차산역"));
        final Station station2 = stationDao.save(new Station("군자역"));
        final Line line = lineDao.save(new Line("5호선", "bg-purple-600"));

        // when
        final Section section = new Section(station1, station2, 10, line.getId());
        final Section savedSection = sectionDao.save(section);

        // then
        assertAll(
                () -> assertThat(savedSection.getId()).isNotNull(),
                () -> assertThat(savedSection.getUpStation()).isEqualTo(section.getUpStation()),
                () -> assertThat(savedSection.getDownStation()).isEqualTo(section.getDownStation())
        );
    }

    @DisplayName("모든 구간을 불러온다.")
    @Test
    void findAll() {
        // given
        final Station station1 = stationDao.save(new Station("아차산역"));
        final Station station2 = stationDao.save(new Station("군자역"));
        final Station station3 = stationDao.save(new Station("광나루역"));
        final Line line = lineDao.save(new Line("5호선", "bg-purple-600"));

        // when
        final Section section1 = new Section(station1, station2, 10, line.getId());
        final Section section2 = new Section(station2, station3, 10, line.getId());
        sectionDao.save(section1);
        sectionDao.save(section2);

        // then
        assertThat(sectionDao.findAll().size()).isEqualTo(2);
    }

    @DisplayName("특정 라인의 모든 구간을 불러온다.")
    @Test
    void findAllByLineId() {
        // given
        final Station station1 = stationDao.save(new Station("아차산역"));
        final Station station2 = stationDao.save(new Station("군자역"));
        final Station station3 = stationDao.save(new Station("광나루역"));
        final Line line = lineDao.save(new Line("5호선", "bg-purple-600"));

        final Section section1 = new Section(station1, station2, 10, line.getId());
        final Section section2 = new Section(station2, station3, 10, line.getId());

        // when
        sectionDao.save(section1);
        sectionDao.save(section2);

        // then
        assertThat(sectionDao.findAllByLineId(line.getId()).size()).isEqualTo(2);
    }

    @DisplayName("특정 구간을 수정한다.")
    @Test
    void update() {
        // given
        final Station station1 = stationDao.save(new Station("아차산역"));
        final Station station2 = stationDao.save(new Station("군자역"));
        final Station station3 = stationDao.save(new Station("광나루역"));
        final Line line = lineDao.save(new Line("5호선", "bg-purple-600"));

        final Section section1 = new Section(station1, station2, 10, line.getId());
        final Section savedSection = sectionDao.save(section1);
        final Section newSection = new Section(station1, station3, 5, line.getId());

        // when
        sectionDao.update(savedSection.getId(), newSection);
        final Optional<Section> updatedSection = sectionDao.findAllByLineId(line.getId())
                .stream()
                .findFirst();

        // then
        assert (updatedSection.isPresent());

        assertThat(updatedSection.get()).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(newSection);
    }

    @DisplayName("특정 구간을 삭제한다.")
    @Test
    void delete() {
        // given
        final Station station1 = stationDao.save(new Station("아차산역"));
        final Station station2 = stationDao.save(new Station("군자역"));
        final Line line = lineDao.save(new Line("5호선", "bg-purple-600"));

        final Section section1 = new Section(station1, station2, 10, line.getId());
        final Section savedSection = sectionDao.save(section1);

        // when
        sectionDao.deleteById(savedSection.getId());

        // then
        assertThat(sectionDao.findAllByLineId(savedSection.getLineId()).size()).isZero();
    }
}
