package wooteco.subway.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import wooteco.subway.dao.LineDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.station.Station;
import wooteco.subway.dto.LineRequest;

@SpringBootTest
@Sql("classpath:truncate.sql")
public class LineServiceTest {
    @Autowired
    private StationDao stationDao;

    @Autowired
    private LineDao lineDao;

    @Autowired
    private LineService lineService;

    @BeforeEach
    void setUp() {
        stationDao.insert(new Station("강남역"));
        stationDao.insert(new Station("선릉역"));
        stationDao.insert(new Station("잠실역"));

        lineService.save(new LineRequest("1호선", "blue", 1L, 2L, 30, 300));
        lineService.save(new LineRequest("2호선", "green", 2L, 3L, 20, 300));
    }

    @Test
    @DisplayName("노선을 생성한다.")
    void saveLine(){
        assertThat(lineService.save(new LineRequest("3호선", "red", 1L, 2L, 10, 300))
                .getName()).isEqualTo("3호선");
    }

    @Test
    @DisplayName("노선을 모두 조회한다.")
    void findAllLines(){
        assertThat(lineService.findAll()).hasSize(2);
    }

    @Test
    @DisplayName("노선을 하나 조회한다.")
    void findLine(){
        assertThat(lineService.findById(1L).getName()).isEqualTo("1호선");
    }

    @Test
    @DisplayName("노선을 제거한다.")
    void deleteLine(){
        lineService.delete(1L);
        assertThat(lineService.findAll()).hasSize(1);
    }

    @Test
    @DisplayName("노선을 수정한다.")
    void editLine(){
        lineService.edit(1L, "4호선", "green", 300);
        assertThat(lineService.findById(1L).getName()).isEqualTo("4호선");
    }
}
