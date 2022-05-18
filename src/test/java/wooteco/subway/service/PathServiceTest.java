package wooteco.subway.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.dao.LineDao;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.PathResponse;
import wooteco.subway.dto.StationResponse;

@SpringBootTest
@Transactional
public class PathServiceTest {

    @Autowired
    private LineDao lineDao;

    @Autowired
    private SectionDao sectionDao;

    @Autowired
    private StationDao stationDao;

    @Autowired
    private PathService pathService;

    private Station 신림역;
    private Station 봉천역;
    private Station 서울대입구역;
    private Station 아차산역;
    private Station 군자역;

    @BeforeEach
    void setUp() {
        신림역 = stationDao.save(new Station("신림역"));
        봉천역 = stationDao.save(new Station("봉천역"));
        서울대입구역 = stationDao.save(new Station("서울대입구역"));
        아차산역 = stationDao.save(new Station("아차산역"));
        군자역 = stationDao.save(new Station("군자역"));

        Line line = lineDao.save(new Line("2호선", "bg-green-600"));
        Line line2 = lineDao.save(new Line("3호선", "bg-yellow-600"));
        Line line3 = lineDao.save(new Line("5호선", "bg-purple-600"));

        sectionDao.save(new Section(신림역, 봉천역, 5, line.getId()));
        sectionDao.save(new Section(봉천역, 서울대입구역, 5, line.getId()));
        sectionDao.save(new Section(신림역, 서울대입구역, 100, line2.getId()));
        sectionDao.save(new Section(아차산역, 군자역, 10, line3.getId()));
    }

    @DisplayName("두 지하철 역의 최단 경로를 반환한다.")
    @Test
    void getPath() {
        PathResponse pathResponse = pathService.getPath(서울대입구역.getId(), 신림역.getId(), 26);

        assertAll(
                () -> assertThat(pathResponse.getFare()).isEqualTo(1250),
                () -> assertThat(pathResponse.getDistance()).isEqualTo(10),
                () -> assertThat(pathResponse.getStations()).usingRecursiveComparison()
                        .isEqualTo(List.of(StationResponse.from(서울대입구역),
                                StationResponse.from(봉천역),
                                StationResponse.from(신림역)))
        );
    }

    @DisplayName("없는 경로의 최단 경로를 요청할 경우 예외를 발생한다.")
    @Test
    void thrown_pathNotExist() {
        assertThatThrownBy(() -> pathService.getPath(아차산역.getId(), 신림역.getId(), 26))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("해당 경로가 존재하지 않습니다.");
    }
}
