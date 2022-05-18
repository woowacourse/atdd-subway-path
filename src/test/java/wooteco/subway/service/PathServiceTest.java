package wooteco.subway.service;

import static org.assertj.core.api.Assertions.assertThat;
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
import wooteco.subway.dto.PathResponse;

class PathServiceTest {

    private PathService pathService;

    @BeforeEach
    void setUp() {
        FakeSectionDao.init();
        FakeStationDao.init();
        pathService = new PathService(new FakeSectionDao(), new FakeStationDao());
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

        PathResponse pathResponse = pathService.createPath(sourceStationId, targetStationId, age);

        assertAll(
                () -> assertThat(createStation(pathResponse)).isEqualTo(
                        List.of(new Station(4L, "이대역"), new Station(5L, "학동역")
                                , new Station(6L, "이수역"), new Station(7L, "건대역"))
                ),
                () -> assertThat(pathResponse.getDistance()).isEqualTo(10),
                () -> assertThat(pathResponse.getFare()).isEqualTo(1250)
        );
    }

    private List<Station> createStation(PathResponse pathResponse) {
        return pathResponse.getStations().stream()
                .map(it -> new Station(it.getId(), it.getName()))
                .collect(Collectors.toList());

    }
}
