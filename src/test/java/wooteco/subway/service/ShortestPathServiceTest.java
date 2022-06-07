package wooteco.subway.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.LineRequest;
import wooteco.subway.dto.PathResponse;
import wooteco.subway.dto.SectionRequest;
import wooteco.subway.dto.StationRequest;

@SpringBootTest
@Sql(scripts = {"classpath:test-schema.sql"})
public class ShortestPathServiceTest {
    @Autowired
    PathService pathService;

    @Autowired
    StationService stationService;

    @Autowired
    LineService lineService;

    @Autowired
    SectionService sectionService;

    @Test
    @DisplayName("역 사이 최단 경로를 구한다.")
    void calculateDistance() {
        // given
        Station 잠실 = new Station(1L, "잠실역");
        Station 잠실새내 = new Station(2L, "잠실새내역");
        Station 종합운동장 = new Station(3L, "종합운동장역");

        insertStation(잠실);
        insertStation(잠실새내);
        insertStation(종합운동장);

        LineRequest 일호선 = new LineRequest("1호선", "red", 1L, 2L, 10, 0);
        lineService.insert(일호선);

        SectionRequest sectionRequest = new SectionRequest(2L, 3L, 20);
        sectionService.insert(sectionRequest, 1L);

        final PathResponse expected =
                new PathResponse(List.of(잠실, 잠실새내, 종합운동장), 30, 1650);
        // when
        PathResponse actual = pathService.findShortestPath(1L, 3L, 20);

        //then
        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    private void insertStation(Station station) {
        StationRequest stationRequest = new StationRequest(station.getName());
        stationService.insert(stationRequest);
    }

    @Test
    @DisplayName("추가운임이 있을 경우 최단 경로를 구한다.")
    void calculateDistanceWithExtraFare() {
        // given
        Station 잠실 = new Station(1L, "잠실역");
        Station 잠실새내 = new Station(2L, "잠실새내역");
        Station 종합운동장 = new Station(3L, "종합운동장역");

        insertStation(잠실);
        insertStation(잠실새내);
        insertStation(종합운동장);

        LineRequest 일호선 = new LineRequest("1호선", "red", 1L, 2L, 10, 900);
        lineService.insert(일호선);

        SectionRequest sectionRequest = new SectionRequest(2L, 3L, 20);
        sectionService.insert(sectionRequest, 1L);

        final PathResponse expected =
                new PathResponse(List.of(잠실, 잠실새내, 종합운동장), 30, 2550);
        // when
        PathResponse actual = pathService.findShortestPath(1L, 3L, 20);

        //then
        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    @DisplayName("청소년인 경우 운임에서 350원을 제한 금액의 20%를 할인한다.")
    void calculateDistanceTeenager() {
        // given
        Station 잠실 = new Station(1L, "잠실역");
        Station 잠실새내 = new Station(2L, "잠실새내역");
        Station 종합운동장 = new Station(3L, "종합운동장역");

        insertStation(잠실);
        insertStation(잠실새내);
        insertStation(종합운동장);

        LineRequest 일호선 = new LineRequest("1호선", "red", 1L, 2L, 10, 0);
        lineService.insert(일호선);

        SectionRequest sectionRequest = new SectionRequest(2L, 3L, 20);
        sectionService.insert(sectionRequest, 1L);

        final PathResponse expected =
                new PathResponse(List.of(잠실, 잠실새내, 종합운동장), 30, 1390);
        // when
        PathResponse actual = pathService.findShortestPath(1L, 3L, 15);

        //then
        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }


    @Test
    @DisplayName("어린이 경우 운임에서 350원을 제한 금액의 50%를 할인한다.")
    void calculateDistanceChild() {
        // given
        Station 잠실 = new Station(1L, "잠실역");
        Station 잠실새내 = new Station(2L, "잠실새내역");
        Station 종합운동장 = new Station(3L, "종합운동장역");

        insertStation(잠실);
        insertStation(잠실새내);
        insertStation(종합운동장);

        LineRequest 일호선 = new LineRequest("1호선", "red", 1L, 2L, 10, 0);
        lineService.insert(일호선);

        SectionRequest sectionRequest = new SectionRequest(2L, 3L, 20);
        sectionService.insert(sectionRequest, 1L);

        final PathResponse expected =
                new PathResponse(List.of(잠실, 잠실새내, 종합운동장), 30, 1000);
        // when
        PathResponse actual = pathService.findShortestPath(1L, 3L, 8);

        //then
        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    @DisplayName("역 사이 경로가 존재하지 않을 때 예외를 발생시킨다.")
    void calculatePathNotExist() {
        // given
        Station 잠실 = new Station(1L, "잠실역");
        Station 잠실새내 = new Station(2L, "잠실새내역");
        Station 종합운동장 = new Station(3L, "종합운동장역");

        insertStation(잠실);
        insertStation(잠실새내);
        insertStation(종합운동장);

        LineRequest 일호선 = new LineRequest("1호선", "red", 1L, 2L, 10, 0);
        lineService.insert(일호선);

        SectionRequest sectionRequest = new SectionRequest(2L, 3L, 20);
        sectionService.insert(sectionRequest, 1L);

        final PathResponse expected =
                new PathResponse(List.of(잠실, 잠실새내, 종합운동장), 30, 1000);
        // when
        PathResponse actual = pathService.findShortestPath(1L, 3L, 8);

        //then
        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    @DisplayName("요청에 해당 역이 존재하지 않을 때 예외를 발생시킨다.")
    void stationNotExistByRequest() {
        // given
        Station 잠실 = new Station(1L, "잠실역");
        Station 잠실새내 = new Station(2L, "잠실새내역");
        Station 종합운동장 = new Station(3L, "종합운동장역");

        insertStation(잠실);
        insertStation(잠실새내);
        insertStation(종합운동장);

        LineRequest 일호선 = new LineRequest("1호선", "red", 1L, 2L, 10, 0);
        lineService.insert(일호선);

        SectionRequest sectionRequest = new SectionRequest(2L, 3L, 20);
        sectionService.insert(sectionRequest, 1L);

        assertThatThrownBy(() -> pathService.findShortestPath(1L, 4L, 20))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 역이 존재하지 않습니다.");
    }
}
