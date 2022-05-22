package wooteco.subway.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.dao.LineDao;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.PathRequest;
import wooteco.subway.dto.PathResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

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
    private Station 서울대입구역;
    private Station 아차산역;
    private Station 군자역;
    private Line line3;

    @BeforeEach
    void setUp() {
        신림역 = stationDao.save(new Station("신림역"));
        서울대입구역 = stationDao.save(new Station("서울대입구역"));
        아차산역 = stationDao.save(new Station("아차산역"));
        final Station 봉천역 = stationDao.save(new Station("봉천역"));
        군자역 = stationDao.save(new Station("군자역"));

        final Line line = lineDao.save(new Line("2호선", "bg-green-600", 100));
        final Line line2 = lineDao.save(new Line("3호선", "bg-yellow-600", 0));
        line3 = lineDao.save(new Line("5호선", "bg-purple-600", 300));

        sectionDao.save(new Section(신림역, 봉천역, 5, line));
        sectionDao.save(new Section(봉천역, 서울대입구역, 5, line));
        sectionDao.save(new Section(신림역, 서울대입구역, 100, line2));
        sectionDao.save(new Section(아차산역, 군자역, 10, line3));
    }

    @DisplayName("두 지하철 역의 최단 경로를 반환한다.")
    @Test
    void getPath() {
        PathRequest pathRequest = new PathRequest(서울대입구역.getId(), 신림역.getId(), 26);
        PathResponse pathResponse = pathService.getPath(pathRequest);

        assertAll(
                () -> assertThat(pathResponse.getFare()).isEqualTo(1350),
                () -> assertThat(pathResponse.getDistance()).isEqualTo(10),
                () -> assertThat(pathResponse.getStations()).extracting("name")
                        .containsExactly("서울대입구역", "봉천역", "신림역")
        );
    }

    @DisplayName("없는 경로의 최단 경로를 요청할 경우 예외를 발생한다.")
    @Test
    void thrown_pathNotExist() {
        PathRequest pathRequest = new PathRequest(군자역.getId(), 신림역.getId(), 26);
        assertThatThrownBy(() -> pathService.getPath(pathRequest))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("해당 경로가 존재하지 않습니다.");
    }

    @DisplayName("연령에 따라 요금이 할인된다.")
    @ParameterizedTest
    @CsvSource(value = {"0,0", "6,500", "13,800", "19,1350"})
    void discountFareByAge(int age, int fare) {
        PathRequest pathRequest = new PathRequest(서울대입구역.getId(), 신림역.getId(), age);
        PathResponse pathResponse = pathService.getPath(pathRequest);

        assertAll(
                () -> assertThat(pathResponse.getFare()).isEqualTo(fare),
                () -> assertThat(pathResponse.getDistance()).isEqualTo(10),
                () -> assertThat(pathResponse.getStations()).extracting("name")
                        .containsExactly("서울대입구역", "봉천역", "신림역")
        );
    }

    @DisplayName("노선별 추가요금과 연령별 할인으로 요금을 계산한다.")
    @ParameterizedTest
    @CsvSource(value = {"0,0", "6,800", "13,1280", "19,1950"})
    void discountFareByLineAndAge(int age, int fare) {
        sectionDao.save(new Section(서울대입구역, 아차산역, 10, line3));

        PathRequest pathRequest = new PathRequest(군자역.getId(), 신림역.getId(), age);
        PathResponse pathResponse = pathService.getPath(pathRequest);

        assertAll(
                () -> assertThat(pathResponse.getFare()).isEqualTo(fare),
                () -> assertThat(pathResponse.getDistance()).isEqualTo(30),
                () -> assertThat(pathResponse.getStations()).extracting("name")
                        .containsExactly("군자역", "아차산역", "서울대입구역", "봉천역", "신림역")
        );
    }
}
