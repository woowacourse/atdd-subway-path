package wooteco.subway.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
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
class StationDaoTest {

    private final StationDao stationDao;
    private final LineDao lineDao;
    private final SectionDao sectionDao;

    @Autowired
    StationDaoTest(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.stationDao = new StationDao(namedParameterJdbcTemplate);
        this.lineDao = new LineDao(namedParameterJdbcTemplate);
        this.sectionDao = new SectionDao(namedParameterJdbcTemplate);
    }

    @DisplayName("지하철역을 저장한다.")
    @Test
    void saveStation() {
        // given
        final Station station = new Station("아차산역");

        // when
        final Station savedStation = stationDao.save(station);

        // then
        assertAll(
                () -> assertThat(savedStation.getId()).isNotZero(),
                () -> assertThat(savedStation.getName()).isEqualTo("아차산역")
        );
    }

    @DisplayName("특정 지하철역을 이름으로 조회한다.")
    @Test
    void findByName() {
        // given
        stationDao.save(new Station("강남역"));

        // when
        Optional<Station> wrappedStation = stationDao.findByName("강남역");
        assert (wrappedStation).isPresent();

        // then
        assertAll(
                () -> assertThat(wrappedStation.get().getId()).isNotZero(),
                () -> assertThat(wrappedStation.get().getName()).isEqualTo("강남역")
        );
    }

    @DisplayName("특정 지하철역을 삭제한다.")
    @Test
    void deleteById() {
        // given
        Station savedStation = stationDao.save(new Station("강남역"));

        // when
        stationDao.deleteById(savedStation.getId());

        // then
        Optional<Station> wrappedStation = stationDao.findById(savedStation.getId());
        assertThat(wrappedStation).isEmpty();
    }

    @DisplayName("특정 지하철역을 아이디로 조회한다.")
    @Test
    void findById() {
        // given
        Station savedStation = stationDao.save(new Station("강남역"));

        // when
        Optional<Station> wrappedStation = stationDao.findById(savedStation.getId());
        assert (wrappedStation).isPresent();

        // then
        assertAll(
                () -> assertThat(wrappedStation.get().getId()).isEqualTo(savedStation.getId()),
                () -> assertThat(wrappedStation.get().getName()).isEqualTo(savedStation.getName())
        );
    }

    @DisplayName("특정 노선에 포함되는 지하철역들을 조회한다.")
    @Test
    void findAllByLineId() {
        // given
        final Station savedStation = stationDao.save(new Station("강남역"));
        final Station newStation = stationDao.save(new Station("아차산역"));
        final Line savedLine = lineDao.save(new Line("5호선", "bg-purple-600"));

        // when
        sectionDao.save(new Section(savedStation, newStation, 10, savedLine.getId()));
        final List<Station> savedStations = stationDao.findAllByLineId(savedLine.getId());

        // then
        assertThat(savedStations).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(List.of(savedStation, newStation));
    }
}
