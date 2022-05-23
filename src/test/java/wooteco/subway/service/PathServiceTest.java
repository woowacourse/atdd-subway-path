package wooteco.subway.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.LineRequest;
import wooteco.subway.dto.PathResponse;
import wooteco.subway.service.fakeDao.LineDaoImpl;
import wooteco.subway.service.fakeDao.SectionDaoImpl;
import wooteco.subway.service.fakeDao.StationDaoImpl;

public class PathServiceTest {
    private final PathService pathService
            = new PathService(StationDaoImpl.getInstance(), SectionDaoImpl.getInstance(), LineDaoImpl.getInstance());

    private final Station station1 = new Station("애플역");
    private final Station station2 = new Station("갤럭시역");
    private final Station station3 = new Station("옵티머스역");

    private Section 애플_갤럭시;
    private Section 갤럭시_옵티머스;

    private Long station1_id;
    private Long station2_id;
    private Long station3_id;

    @BeforeEach
    void setUp() {
        final List<Station> stations = StationDaoImpl.getInstance().findAll();
        stations.clear();
        final List<Section> sections = SectionDaoImpl.getInstance().findAll();
        sections.clear();
        final List<Line> lines = LineDaoImpl.getInstance().findAll();
        lines.clear();

        station1_id = StationDaoImpl.getInstance().save(station1);
        station2_id = StationDaoImpl.getInstance().save(station2);
        station3_id = StationDaoImpl.getInstance().save(station3);
    }

    @Test
    @DisplayName("역 사이 최단 경로를 구한다.")
    void calculateDistance() {
        // given
        Long lineId = LineDaoImpl.getInstance().save(new LineRequest("1호선", "red", 1L, 2L, 0, 0));

        애플_갤럭시 = new Section(lineId, station1_id, station2_id, 10);
        갤럭시_옵티머스 = new Section(lineId, station2_id, station3_id, 20);
        SectionDaoImpl.getInstance().save(애플_갤럭시);
        SectionDaoImpl.getInstance().save(갤럭시_옵티머스);

        final PathResponse expected =
                new PathResponse(List.of(station1, station2, station3), 30, 1650);
        // when
        PathResponse actual = pathService.findShortestPath(station1_id, station3_id);

        //then
        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    @DisplayName("추가운임이 있을 경우 최단 경로를 구한다.")
    void calculateDistanceWithExtraFare() {
        // given
        Long lineId = LineDaoImpl.getInstance().save(new LineRequest("1호선", "red", 1L, 2L, 0, 900));

        애플_갤럭시 = new Section(lineId, station1_id, station2_id, 10);
        갤럭시_옵티머스 = new Section(lineId, station2_id, station3_id, 20);
        SectionDaoImpl.getInstance().save(애플_갤럭시);
        SectionDaoImpl.getInstance().save(갤럭시_옵티머스);

        final PathResponse expected =
                new PathResponse(List.of(station1, station2, station3), 30, 2550);
        // when
        PathResponse actual = pathService.findShortestPath(station1_id, station3_id);

        //then
        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    @DisplayName("역 사이 경로가 존재하지 않을 때 예외를 발생시킨다.")
    void calculatePathNotExist() {
        // given
        애플_갤럭시 = new Section(station1_id, station2_id, 100);
        SectionDaoImpl.getInstance().save(애플_갤럭시);

        //when then
        assertThatThrownBy(() -> pathService.findShortestPath(station1_id, station3_id))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 역 사이 경로가 존재하지 않습니다.");
    }

    @Test
    @DisplayName("요청에 해당 역이 존재하지 않을 때 예외를 발생시킨다.")
    void stationNotExistByRequest() {
        assertThatThrownBy(() -> pathService.findShortestPath(station1_id, station3_id + 1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 역이 존재하지 않습니다.");
    }
}
