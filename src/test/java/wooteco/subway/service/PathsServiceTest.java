package wooteco.subway.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.dao.FakeLineDao;
import wooteco.subway.dao.FakeSectionDao;
import wooteco.subway.dao.FakeStationDao;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.PathsResponse;
import wooteco.subway.exception.requestvalue.AgeValueException;

class PathsServiceTest {

    private PathService pathService;

    @BeforeEach
    void setUp() {
        FakeSectionDao.init();
        FakeStationDao.init();
        pathService = new PathService(new FakeSectionDao(), new FakeStationDao(), new FakeLineDao());
    }

    @DisplayName("경로를 조회한다.")
    @Test
    void showRoute() {
        Long sourceStationId = 4L;
        Long targetStationId = 7L;
        int age = 15;

        FakeStationDao fakeStationDao = new FakeStationDao();
        fakeStationDao.save(new Station("이대역"));
        fakeStationDao.save(new Station("학동역"));
        fakeStationDao.save(new Station("이수역"));
        fakeStationDao.save(new Station("건대역"));
        fakeStationDao.save(new Station("사가정역"));

        FakeLineDao fakeLineDao = new FakeLineDao();
        fakeLineDao.save(new Line("1호선", "blue", 900));
        fakeLineDao.save(new Line("2호선", "green", 900));
        fakeLineDao.save(new Line("3호선", "orange", 900));
        fakeLineDao.save(new Line("4호선", "black", 900));

        FakeSectionDao fakeSectionDao = new FakeSectionDao();
        fakeSectionDao.save(new Section(1L, new Station(4L, "이대역"), new Station(5L, "학동역"), 3));
        fakeSectionDao.save(new Section(2L, new Station(5L, "학동역"), new Station(6L, "이수역"), 3));
        fakeSectionDao.save(new Section(2L, new Station(6L, "이수역"), new Station(7L, "건대역"), 4));
        fakeSectionDao.save(new Section(3L, new Station(7L, "건대역"), new Station(8L, "사가정역"), 5));
        fakeSectionDao.save(new Section(4L, new Station(8L, "사가정역"), new Station(4L, "이대역"), 7));

        PathsResponse pathsResponse = pathService.createPaths(sourceStationId, targetStationId, age);

        assertAll(
                () -> assertThat(createStation(pathsResponse)).isEqualTo(
                        List.of(new Station(4L, "이대역"), new Station(5L, "학동역")
                                , new Station(6L, "이수역"), new Station(7L, "건대역"))
                ),
                () -> assertThat(pathsResponse.getDistance()).isEqualTo(10),
                () -> assertThat(pathsResponse.getFare()).isEqualTo(1790)
        );
    }

    @DisplayName("승객의 나이가 0살 이면 예외를 발생한다.")
    @Test
    void age_exception_zero() {
        Long sourceStationId = 4L;
        Long targetStationId = 7L;
        int age = 0;

        assertThatThrownBy(() -> pathService.createPaths(sourceStationId, targetStationId, age))
                .isInstanceOf(AgeValueException.class)
                .hasMessage("승객의 연령은 한살 이상이여야 합니다.");
    }

    @DisplayName("승객의 나이가 음수 이면 예외를 발생한다.")
    @Test
    void age_exception_under_zero() {
        Long sourceStationId = 4L;
        Long targetStationId = 7L;
        int age = -3;

        assertThatThrownBy(() -> pathService.createPaths(sourceStationId, targetStationId, age))
                .isInstanceOf(AgeValueException.class)
                .hasMessage("승객의 연령은 한살 이상이여야 합니다.");
    }

    private List<Station> createStation(final PathsResponse pathsResponse) {
        return pathsResponse.getStations().stream()
                .map(it -> new Station(it.getId(), it.getName()))
                .collect(Collectors.toList());

    }
}
