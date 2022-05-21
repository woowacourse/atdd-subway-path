package wooteco.subway.service;

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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class PathServiceTest {
    private final PathService pathService
            = new PathService(StationDaoImpl.getInstance(), LineDaoImpl.getInstance(), SectionDaoImpl.getInstance());

    private Station 애플역;
    private Station 갤럭시역;
    private Station 옵티머스역;
    private Long 애플역_id;
    private Long 갤럭시역_id;
    private Long 옵티머스역_id;

    private LineRequest 국내선;
    private LineRequest 해외경유선;

    private Section 애플_갤럭시;
    private Section 갤럭시_옵티머스;

    @BeforeEach
    void setUp() {
        final List<Station> stations = StationDaoImpl.getInstance().findAll();
        stations.clear();
        final List<Line> lines = LineDaoImpl.getInstance().findAll();
        lines.clear();
        final List<Section> sections = SectionDaoImpl.getInstance().findAll();
        sections.clear();

        initStations();
    }

    @Test
    @DisplayName("역 사이 최단 경로를 구한다.")
    void calculateDistance() {
        // given
        initLineAndSectionWithNotExtraFare();
        final PathResponse expected =
                new PathResponse(List.of(애플역, 갤럭시역, 옵티머스역), 30, 1650);
        // when
        PathResponse actual = pathService.findShortestPath(애플역_id, 옵티머스역_id, 20);

        //then
        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    @DisplayName("역 사이 경로가 존재하지 않을 때 예외를 발생시킨다.")
    void calculatePathNotExist() {
        // given
        해외경유선 = new LineRequest("해외경유선", "blue", 애플역_id, 갤럭시역_id, 15, 400);
        LineDaoImpl.getInstance().save(해외경유선);

        애플_갤럭시 = new Section(애플역_id, 갤럭시역_id, 100);
        SectionDaoImpl.getInstance().save(애플_갤럭시);

        //when then
        assertThatThrownBy(() -> pathService.findShortestPath(애플역_id, 옵티머스역_id, 20))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 역 사이 경로가 존재하지 않습니다.");
    }

    @Test
    @DisplayName("요청에 해당 역이 존재하지 않을 때 예외를 발생시킨다.")
    void stationNotExistByRequest() {
        // when then
        assertThatThrownBy(() -> pathService.findShortestPath(애플역_id, 갤럭시역_id + 1L, 20))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 역 사이 경로가 존재하지 않습니다.");
    }

    @Test
    @DisplayName("역 사이 최단 경로의 요금을 올바르게 계산한다.")
    void calculateFareByShortestPath() {
        // given
        initLineAndSectionWithExtraFare();
        final PathResponse expected =
                new PathResponse(List.of(애플역, 갤럭시역, 옵티머스역), 25, 2150);
        // when
        PathResponse actual = pathService.findShortestPath(애플역_id, 옵티머스역_id, 19);

        //then
        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    @DisplayName("어린이가 이용할 경우의 역 사이 최단 경로 요금을 올바르게 계산한다.")
    void calculateChildrenFareByShortestPath() {
        // given
        initLineAndSectionWithExtraFare();
        final PathResponse expected =
                new PathResponse(List.of(애플역, 갤럭시역, 옵티머스역), 25, 1250);
        // when
        PathResponse actual = pathService.findShortestPath(애플역_id, 옵티머스역_id, 6);

        //then
        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }


    @Test
    @DisplayName("청소년이 이용할 경우의 역 사이 최단 경로 요금을 올바르게 계산한다.")
    void calculateTeenagerFareByShortestPath() {
        // given
        initLineAndSectionWithExtraFare();
        final PathResponse expected =
                new PathResponse(List.of(애플역, 갤럭시역, 옵티머스역), 25, 1790);
        // when
        PathResponse actual = pathService.findShortestPath(애플역_id, 옵티머스역_id, 18);

        //then
        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    private void initLineAndSectionWithNotExtraFare() {
        해외경유선 = new LineRequest("해외경유선", "blue", 애플역_id, 갤럭시역_id, 10, 0);
        Long 해외경유선_id = LineDaoImpl.getInstance().save(해외경유선);
        국내선 = new LineRequest("국내선", "red", 갤럭시역_id, 옵티머스역_id, 20, 0);
        Long 국내선_id = LineDaoImpl.getInstance().save(국내선);

        애플_갤럭시 = new Section(해외경유선_id, 애플역_id, 갤럭시역_id, 10);
        SectionDaoImpl.getInstance().save(애플_갤럭시);
        갤럭시_옵티머스 = new Section(국내선_id, 갤럭시역_id, 옵티머스역_id, 20);
        SectionDaoImpl.getInstance().save(갤럭시_옵티머스);
    }

    private void initLineAndSectionWithExtraFare() {
        해외경유선 = new LineRequest("해외경유선", "blue", 애플역_id, 갤럭시역_id, 15, 400);
        Long 해외경유선_id = LineDaoImpl.getInstance().save(해외경유선);
        국내선 = new LineRequest("국내선", "red", 갤럭시역_id, 옵티머스역_id, 10, 600);
        Long 국내선_id = LineDaoImpl.getInstance().save(국내선);

        애플_갤럭시 = new Section(해외경유선_id, 애플역_id, 갤럭시역_id, 15);
        SectionDaoImpl.getInstance().save(애플_갤럭시);
        갤럭시_옵티머스 = new Section(국내선_id, 갤럭시역_id, 옵티머스역_id, 10);
        SectionDaoImpl.getInstance().save(갤럭시_옵티머스);
    }

    private void initStations() {
        애플역 = new Station("애플역");
        갤럭시역 = new Station("갤럭시역");
        옵티머스역 = new Station("옵티머스역");

        애플역_id = StationDaoImpl.getInstance().save(애플역);
        갤럭시역_id = StationDaoImpl.getInstance().save(갤럭시역);
        옵티머스역_id = StationDaoImpl.getInstance().save(옵티머스역);
    }
}
