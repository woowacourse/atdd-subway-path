package wooteco.subway.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.station.Station;
import wooteco.subway.dto.StationRequest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Sql("classpath:truncate.sql")
public class StationServiceTest
{
    @Autowired
    private StationDao stationDao;

    @Autowired
    private StationService stationService;

    @BeforeEach
    void setUp() {
        stationDao.insert(new Station("강남역"));
        stationDao.insert(new Station("선릉역"));
        stationDao.insert(new Station("잠실역"));
    }

    @Test
    @DisplayName("역을 생성한다.")
    void saveStation(){
        assertThat(stationService.save(new StationRequest("사당역")).getName()).isEqualTo("사당역");
    }

    @Test
    @DisplayName("역을 모두 조회한다.")
    void findAllStations(){
        assertThat(stationService.findAll()).hasSize(3);
    }

    @Test
    @DisplayName("역을 제거한다.")
    void deleteStation(){
        stationService.delete(1L);
        assertThat(stationService.findAll()).hasSize(2);
    }
}
