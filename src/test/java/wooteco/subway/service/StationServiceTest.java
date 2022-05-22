package wooteco.subway.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.NoSuchElementException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Station;

@JdbcTest
class StationServiceTest {

    private static final String STATION_NAME = "테스트1역";
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private StationService stationService;

    @BeforeEach
    void setUp() {
        stationService = new StationService(new StationDao(jdbcTemplate), new SectionDao(jdbcTemplate));
        stationService.createStation(STATION_NAME);
    }

    @Test
    void createStation() {
        var actual = findStation();

        assertThat(actual.getName()).isEqualTo(STATION_NAME);
    }

    private Station findStation() {
        var sql = "SELECT * FROM station";

        RowMapper<Station> rowMapper = (rs, rowNum) -> {
            var id = rs.getLong("id");
            var name = rs.getString("name");
            return new Station(id, name);
        };

        return jdbcTemplate.query(sql, rowMapper).get(0);
    }

    @Test
    @DisplayName("중복된 이름으로 생성시 예외발생")
    void failCreateStation() {
        Assertions.assertThatThrownBy(() -> stationService.createStation(STATION_NAME))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 이미 존재하는 역 이름 입니다.");
    }

    @Test
    void deleteStation() {
        var stationId = findStation().getId();

        assertDoesNotThrow(() -> stationService.deleteStation(stationId));
    }

    @Test
    @DisplayName("존재하지 않는 역 아이디로 삭제시 예외발생")
    void failDeleteStation() {
        var invalidStationId = -1L;

        Assertions.assertThatThrownBy(() -> stationService.deleteStation(invalidStationId))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("[ERROR] 존재하지 않는 역 입니다.");
    }

    @Test
    @DisplayName("구간에 등록된 역 제거를 시도할 경우 예외발생")
    void failDeleteStation2() {
        var stationId = findStation().getId();
        insertSection(stationId);

        Assertions.assertThatThrownBy(() -> stationService.deleteStation(stationId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 구간에 추가돼 있는 역은 삭제할 수 없습니다.");
    }

    private void insertSection(Long stationId) {
        var sql = "INSERT INTO section (line_id, up_station_id, down_station_id, distance) values(?, ?, ?, ?)";

        jdbcTemplate.update(sql, 0L, stationId, 0L, 0);
    }

    @Test
    void findAll() {
        var actual = stationService.findAll();

        assertAll(
                () -> assertThat(actual.size()).isEqualTo(1),
                () -> assertThat(actual.get(0).getName()).isEqualTo(STATION_NAME)
        );
    }
}
