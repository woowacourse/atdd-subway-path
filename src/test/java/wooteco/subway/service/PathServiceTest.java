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
import wooteco.subway.service.fakeDao.FakeLineDao;
import wooteco.subway.service.fakeDao.FakeSectionDao;
import wooteco.subway.service.fakeDao.FakeStationDao;

public class PathServiceTest {
    private final PathService pathService
            = new PathService(FakeStationDao.getInstance(), FakeSectionDao.getInstance(), FakeLineDao.getInstance());

    private final Station 애플역 = new Station("애플역");
    private final Station 갤럭시역 = new Station("갤럭시역");
    private final Station 옵티머스역 = new Station("옵티머스역");
    private final Station 샤오미역 = new Station("샤오미역");

    private Section 애플_갤럭시;
    private Section 갤럭시_옵티머스;
    private Section 옵티머스_샤오미;

    private Long 애플역_id;
    private Long 갤럭시역_id;
    private Long 옵티머스역_id;
    private Long 샤오미역_id;

    private int adultAge = 20;

    @BeforeEach
    void setUp() {
        final List<Station> stations = FakeStationDao.getInstance().findAll();
        stations.clear();
        final List<Section> sections = FakeSectionDao.getInstance().findAll();
        sections.clear();
        final List<Line> lines = FakeLineDao.getInstance().findAll();
        lines.clear();

        애플역_id = FakeStationDao.getInstance().save(애플역);
        갤럭시역_id = FakeStationDao.getInstance().save(갤럭시역);
        옵티머스역_id = FakeStationDao.getInstance().save(옵티머스역);
        샤오미역_id = FakeStationDao.getInstance().save(샤오미역);

        Long lineId = FakeLineDao.getInstance().save(new LineRequest("1호선", "red", 1L, 2L, 0, 0));

        애플_갤럭시 = new Section(lineId, 애플역_id, 갤럭시역_id, 10);
        갤럭시_옵티머스 = new Section(lineId, 갤럭시역_id, 옵티머스역_id, 20);
        FakeSectionDao.getInstance().save(애플_갤럭시);
        FakeSectionDao.getInstance().save(갤럭시_옵티머스);
    }

    @Test
    @DisplayName("역 사이 최단 경로를 구한다.")
    void calculateDistance() {
        // given
        final PathResponse expected =
                new PathResponse(List.of(애플역, 갤럭시역, 옵티머스역), 30, 1650);
        // when
        PathResponse actual = pathService.findShortestPath(애플역_id, 옵티머스역_id, adultAge);

        //then
        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    @DisplayName("추가운임이 있을 경우 최단 경로를 구한다.")
    void calculateDistanceWithExtraFare() {
        // given
        final PathResponse expected =
                new PathResponse(List.of(옵티머스역, 샤오미역), 20, 2350);
        Long lineId2 = FakeLineDao.getInstance().save(new LineRequest("2호선", "red", 1L, 2L, 0, 900));

        옵티머스_샤오미 = new Section(lineId2, 옵티머스역_id, 샤오미역_id, 20);
        FakeSectionDao.getInstance().save(옵티머스_샤오미);
        // when
        PathResponse actual = pathService.findShortestPath(옵티머스역_id, 샤오미역_id, adultAge);

        //then
        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    @DisplayName("청소년인 경우 운임에서 350원을 제한 금액의 20%를 할인한다.")
    void calculateDistanceTeenager() {
        // given
        final PathResponse expected =
                new PathResponse(List.of(애플역, 갤럭시역, 옵티머스역), 30, 1390);
        // when
        PathResponse actual = pathService.findShortestPath(애플역_id, 옵티머스역_id, 15);

        //then
        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }


    @Test
    @DisplayName("어린이 경우 운임에서 350원을 제한 금액의 50%를 할인한다.")
    void calculateDistanceChild() {
        // given
        final PathResponse expected =
                new PathResponse(List.of(애플역, 갤럭시역, 옵티머스역), 30, 1000);
        // when
        PathResponse actual = pathService.findShortestPath(애플역_id, 옵티머스역_id, 8);

        //then
        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    @DisplayName("역 사이 경로가 존재하지 않을 때 예외를 발생시킨다.")
    void calculatePathNotExist() {
        // given
        애플_갤럭시 = new Section(애플역_id, 갤럭시역_id, 100);
        FakeSectionDao.getInstance().save(애플_갤럭시);

        //when then
        assertThatThrownBy(() -> pathService.findShortestPath(애플역_id, 샤오미역_id, adultAge))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 역 사이 경로가 존재하지 않습니다.");
    }

    @Test
    @DisplayName("요청에 해당 역이 존재하지 않을 때 예외를 발생시킨다.")
    void stationNotExistByRequest() {
        assertThatThrownBy(() -> pathService.findShortestPath(애플역_id, 샤오미역_id + 1L, adultAge))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 역이 존재하지 않습니다.");
    }
}
