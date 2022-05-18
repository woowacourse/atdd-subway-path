package wooteco.subway.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
import wooteco.subway.domain.Fare;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Path;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.PathRequest;
import wooteco.subway.dto.PathResponse;
import wooteco.subway.exception.DuplicatedSourceAndTargetException;

@SpringBootTest
@Transactional
class PathServiceTest {

    @Autowired
    private PathService pathService;

    @Autowired
    private LineDao lineDao;

    @Autowired
    private StationDao stationDao;

    @Autowired
    private SectionDao sectionDao;

    private Station station1;
    private Station station2;
    private Station station3;


    @BeforeEach
    void setUp() {
        final Line savedLine = lineDao.save(new Line("2호선", "bg-green-600"));
        station1 = stationDao.save(new Station("강남역"));
        station2 = stationDao.save(new Station("역삼역"));
        station3 = stationDao.save(new Station("선릉역"));
        sectionDao.save(new Section(station1, station2, 10, savedLine.getId()));
        sectionDao.save(new Section(station2, station3, 10, savedLine.getId()));
    }

    @DisplayName("경로를 생성한다.")
    @Test
    void createPath() {
        final Path path = new Path(List.of(station1, station2, station3), 20, Fare.from(20));
        assertThat(pathService.createPath(new PathRequest(station1.getId(), station3.getId(), 15))).usingRecursiveComparison()
                .isEqualTo(PathResponse.from(path));
    }

    @DisplayName("출발지와 도착지가 같은 경우 예외를 발생한다.")
    @Test
    void createPath_throwsExceptionDuplicatedSourceAndTarget() {
        assertThatThrownBy(() -> pathService.createPath(new PathRequest(station1.getId(), station1.getId(), 15)))
                .isInstanceOf(DuplicatedSourceAndTargetException.class)
                .hasMessage("출발역과 도착역은 같을 수 없습니다.");
    }
}
