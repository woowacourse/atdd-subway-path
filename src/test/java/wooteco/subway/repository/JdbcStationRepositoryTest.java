package wooteco.subway.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static wooteco.subway.TestFixture.강남역;
import static wooteco.subway.TestFixture.선릉역;
import static wooteco.subway.TestFixture.역삼역;

import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import wooteco.subway.domain.station.Station;
import wooteco.subway.repository.dao.StationDao;

@TestConstructor(autowireMode = AutowireMode.ALL)
@JdbcTest
class JdbcStationRepositoryTest {

    private StationRepository stationRepository;

    public JdbcStationRepositoryTest(DataSource dataSource) {
        this.stationRepository = new JdbcStationRepository(new StationDao(dataSource));
    }

    @DisplayName("새로운 지하철역을 생성한다")
    @Test
    void saveStation() {
        // given
        final Station station = 강남역;

        // when 
        final Station actual = stationRepository.save(station);

        // then
        assertAll(
                () -> assertThat(actual.getId()).isGreaterThan(0L),
                () -> assertThat(actual.getName()).isEqualTo(actual.getName())
        );

    }

    @DisplayName("전체 지하철역 목록을 조회한다")
    @Test
    void findStations() {
        // given
        final Station savedGangNam = stationRepository.save(강남역);
        final Station savedYeokSam = stationRepository.save(역삼역);
        final Station savedSunNeung = stationRepository.save(선릉역);

        // when
        final List<Station> actual = stationRepository.findAll();

        // then
        assertThat(actual).containsExactly(savedGangNam, savedYeokSam, savedSunNeung);
    }

    @DisplayName("ID로 지하철역 한 개를 조회한다")
    @Test
    void findStationById() {
        // given
        final Station savedGangNam = stationRepository.save(강남역);

        // when
        final Optional<Station> foundById = stationRepository.findById(savedGangNam.getId());

        // then
        assertAll(
                () -> assertThat(foundById).isPresent(),
                () -> assertThat(foundById.get()).usingRecursiveComparison().isEqualTo(savedGangNam)
        );
    }

    @DisplayName("ID로 지하철역을 삭제한다")
    @Test
    void removeStation() {
        // given
        final Station savedGangNam = stationRepository.save(강남역);
        final Optional<Station> beforeDelete = stationRepository.findById(savedGangNam.getId());

        // when
        stationRepository.deleteById(savedGangNam.getId());
        final Optional<Station> afterDelete = stationRepository.findById(savedGangNam.getId());

        // then
        assertAll(
                () -> assertThat(beforeDelete).isPresent(),
                () -> assertThat(afterDelete).isEmpty()
        );
    }
}
