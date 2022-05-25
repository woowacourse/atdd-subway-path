package wooteco.subway.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import wooteco.subway.dao.LineDao;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.station.Station;
import wooteco.subway.dto.PathRequest;
import wooteco.subway.dto.PathResponse;

@SpringBootTest
@Sql("classpath:truncate.sql")
public class PathServiceTest {

    @Autowired
    private StationDao stationDao;

    @Autowired
    private SectionDao sectionDao;

    @Autowired
    private LineDao lineDao;

    @Autowired
    private PathService pathService;

    private Station 강남역 = new Station("강남역");
    private Station 선릉역 = new Station("선릉역");
    private Station 잠실역 = new Station("잠실역");

    private Line _2호선_green_0 = new Line(1L, "2호선", "green", 0);

    private Section _1L_2L_10 = new Section(1L, 1L, 2L, 10);
    private Section _2L_3L_15 = new Section(1L, 2L, 3L, 15);

    @BeforeEach
    void setUp() {
        stationDao.insert(강남역);
        stationDao.insert(선릉역);
        stationDao.insert(잠실역);
        lineDao.insert(_2호선_green_0);
        sectionDao.insert(_1L_2L_10);
        sectionDao.insert(_2L_3L_15);
    }

    @Test
    @DisplayName("경로를 탐색한다.")
    void searchPath() {
        PathResponse pathResponse = pathService.searchPath(new PathRequest(1L, 3L, 25));

        assertThat(pathResponse.getDistance()).isEqualTo(25);
        assertThat(pathResponse.getFare()).isEqualTo(1550);
    }

    @Test
    @DisplayName("요금을 계산한다. - extraFare 존재")
    void searchPathWithExtraFare() {
        PathResponse pathResponse = pathService.searchPath(new PathRequest(1L, 3L, 25));

        assertThat(pathResponse.getDistance()).isEqualTo(25);
        assertThat(pathResponse.getFare()).isEqualTo(1550);
    }

    @Test
    @DisplayName("요금을 계산한다. - 어린이 요금 할인")
    void searchPathWithChildDiscount() {
        PathResponse pathResponse = pathService.searchPath(new PathRequest(1L, 3L, 6));

        assertThat(pathResponse.getDistance()).isEqualTo(25);
        assertThat(pathResponse.getFare()).isEqualTo(600);
    }

    @Test
    @DisplayName("요금을 계산한다. - 청소년 요금 할인")
    void searchPathWithAdolescentDiscount() {
        PathResponse pathResponse = pathService.searchPath(new PathRequest(1L, 3L, 13));

        assertThat(pathResponse.getDistance()).isEqualTo(25);
        assertThat(pathResponse.getFare()).isEqualTo(960);
    }
}
