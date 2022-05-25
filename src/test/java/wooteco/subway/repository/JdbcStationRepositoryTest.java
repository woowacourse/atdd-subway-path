package wooteco.subway.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.Fixtures.강남역;
import static wooteco.subway.Fixtures.선릉역;
import static wooteco.subway.Fixtures.역삼역;

import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.station.Station;

@JdbcTest
class JdbcStationRepositoryTest {

    private final StationRepository stationRepository;

    @Autowired
    public JdbcStationRepositoryTest(JdbcTemplate jdbcTemplate) {
        StationDao stationDao = new StationDao(jdbcTemplate);
        stationRepository = new JdbcStationRepository(stationDao);
    }

    @Test
    @DisplayName("새로운 지하철 역을 등록할 수 있다.")
    void save() {
        Long saveId = stationRepository.save(선릉역);

        assertThat(saveId).isNotNull();
    }

    @Test
    @DisplayName("등록된 지하철 역들을 반환한다.")
    void findAll() {
        stationRepository.save(강남역);
        stationRepository.save(역삼역);
        stationRepository.save(선릉역);

        List<String> actual = stationRepository.findAll().stream()
                .map(Station::getName)
                .collect(Collectors.toList());
        List<String> expected = List.of(강남역.getName(), 역삼역.getName(), 선릉역.getName());

        assertThat(actual).containsAll(expected);
    }

    @Test
    @DisplayName("등록된 지하철을 삭제한다.")
    void deleteById() {
        Long saveId = stationRepository.save(선릉역);

        stationRepository.deleteById(saveId);

        assertThat(stationRepository.findAll()).hasSize(0);
    }
}
