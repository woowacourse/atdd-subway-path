package wooteco.subway.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.jdbc.Sql;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.station.Station;
import wooteco.subway.dto.PathResponse;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Sql("classpath:truncate.sql")
public class PathServiceTest {
    @Autowired
    private StationDao stationDao;

    @Autowired
    private SectionDao sectionDao;

    @Autowired
    private PathService pathService;

    @BeforeEach
    void setUp() {
        stationDao.save(new Station("강남역"));
        stationDao.save(new Station("선릉역"));
        stationDao.save(new Station("잠실역"));
        sectionDao.save(new Section(1L, 1L, 2L, 10));
        sectionDao.save(new Section(1L, 2L, 3L, 15));
    }

    @Test
    @DisplayName("경로를 탐색한다.")
    void searchPath() {
        PathResponse pathResponse = pathService.searchPath(1L, 3L, 25);

        assertThat(pathResponse.getDistance()).isEqualTo(25);
        assertThat(pathResponse.getFare()).isEqualTo(1550);
    }
}
