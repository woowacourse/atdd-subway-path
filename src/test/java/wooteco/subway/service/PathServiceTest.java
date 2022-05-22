package wooteco.subway.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import wooteco.subway.dto.LineRequest;
import wooteco.subway.dto.LineResponse;
import wooteco.subway.dto.PathResponse;
import wooteco.subway.dto.SectionRequest;
import wooteco.subway.dto.StationRequest;
import wooteco.subway.dto.StationResponse;

@SpringBootTest
@Sql("/truncate.sql")
class PathServiceTest {

    private final PathService pathService;
    private final LineService lineService;
    private final StationService stationService;

    @Autowired
    public PathServiceTest(PathService pathService, LineService lineService, StationService stationService) {
        this.pathService = pathService;
        this.lineService = lineService;
        this.stationService = stationService;
    }

    @DisplayName("경로를 조회한다.")
    @Test
    void findPath() {
        StationResponse 선릉역 = stationService.save(new StationRequest("선릉역"));
        StationResponse 강남역 = stationService.save(new StationRequest("강남역"));
        StationResponse 낙성대역 = stationService.save(new StationRequest("낙성대역"));
        StationResponse 신대방역 = stationService.save(new StationRequest("신대방역"));
        LineRequest lineRequest = new LineRequest("2호선", "green", 선릉역.getId(), 강남역.getId(), 10);
        LineResponse lineResponse = lineService.save(lineRequest);
        SectionRequest sectionRequest1 = new SectionRequest(강남역.getId(), 낙성대역.getId(), 10);
        SectionRequest sectionRequest2 = new SectionRequest(낙성대역.getId(), 신대방역.getId(), 10);
        lineService.addSection(lineResponse.getId(), sectionRequest1);
        lineService.addSection(lineResponse.getId(), sectionRequest2);

        PathResponse pathResponse = pathService.findPath(선릉역.getId(), 신대방역.getId(), 24);

        assertAll(
                () -> assertThat(pathResponse.getStations().size()).isEqualTo(4),
                () -> assertThat(pathResponse.getDistance()).isEqualTo(30),
                () -> assertThat(pathResponse.getFare()).isEqualTo(1650)
        );
    }

    @DisplayName("서로 다른 노선을 포함한 경로를 조회한다.")
    @Test
    void findPathWithCrossLine() {
        StationResponse 건대입구역 = stationService.save(new StationRequest("건대입구역"));
        StationResponse 강남구청역 = stationService.save(new StationRequest("강남구청역"));
        StationResponse 대림역 = stationService.save(new StationRequest("대림역"));
        StationResponse 낙성대역 = stationService.save(new StationRequest("낙성대역"));
        LineRequest line7 = new LineRequest(
                "7호선", "deep green", 건대입구역.getId(), 강남구청역.getId(), 10);
        LineResponse line7Response = lineService.save(line7);
        lineService.addSection(line7Response.getId(), new SectionRequest(강남구청역.getId(), 대림역.getId(), 10));
        LineRequest line2 = new LineRequest(
                "2호선", "green", 건대입구역.getId(), 낙성대역.getId(), 5);
        LineResponse line2Response = lineService.save(line2);
        lineService.addSection(line2Response.getId(), new SectionRequest(낙성대역.getId(), 대림역.getId(), 5));

        PathResponse pathResponse = pathService.findPath(건대입구역.getId(), 대림역.getId(), 24);

        assertAll(
                () -> assertThat(pathResponse.getStations().size()).isEqualTo(3),
                () -> assertThat(pathResponse.getDistance()).isEqualTo(10),
                () -> assertThat(pathResponse.getFare()).isEqualTo(1250)
        );
    }

    @DisplayName("다른 노선의 갈 수 없는 경로를 조회하면 예외를 던진다.")
    @Test
    void findPathCanNotGo() {

        StationResponse 건대입구역 = stationService.save(new StationRequest("건대입구역"));
        StationResponse 강남구청역 = stationService.save(new StationRequest("강남구청역"));
        StationResponse 부천역 = stationService.save(new StationRequest("부천역"));
        StationResponse 중동역 = stationService.save(new StationRequest("중동역"));
        LineRequest line7 = new LineRequest(
                "7호선", "deep green", 건대입구역.getId(), 강남구청역.getId(), 10);
        lineService.save(line7);
        LineRequest line1 = new LineRequest(
                "1호선", "blue", 부천역.getId(), 중동역.getId(), 5);
        lineService.save(line1);

        assertThatThrownBy(() -> pathService.findPath(건대입구역.getId(), 부천역.getId(), 24))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("경로에 존재하지 않는 지하철역을 조회하면 예외를 던진다.")
    @Test
    void findPathNotExistsStation() {
        StationResponse 건대입구역 = stationService.save(new StationRequest("건대입구역"));
        StationResponse 강남구청역 = stationService.save(new StationRequest("강남구청역"));
        LineRequest line7 = new LineRequest(
                "7호선", "deep green", 건대입구역.getId(), 강남구청역.getId(), 10);
        lineService.save(line7);

        StationResponse 센트럴파트역 = stationService.save(new StationRequest("센트럴파트역"));

        assertThatThrownBy(() -> pathService.findPath(건대입구역.getId(), 센트럴파트역.getId(), 24))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
