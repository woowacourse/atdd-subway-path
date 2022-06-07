package wooteco.subway.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.LineRequest;
import wooteco.subway.dto.LineResponse;
import wooteco.subway.dto.StationRequest;
import wooteco.subway.dto.StationResponse;
import wooteco.subway.exception.NotFoundException;

@SpringBootTest
@Sql(scripts = {"classpath:test-schema.sql"})
class LineServiceTest {
    @Autowired
    LineService lineService;

    @Autowired
    StationService stationService;

    @Test
    @DisplayName("지하철 노선을 추가할 수 있다.")
    void insert() {
        //when
        Station station1 = new Station(1L, "잠실");
        Station station2 = new Station(2L, "선릉");

        StationRequest 잠실역_요청 = new StationRequest("잠실");
        StationRequest 선릉역_요청 = new StationRequest("선릉");

        long 잠실_id = stationService.insert(잠실역_요청).getId();
        long 선릉_id = stationService.insert(선릉역_요청).getId();

        LineRequest 일호선 = new LineRequest("1호선", "blue", 잠실_id, 선릉_id, 10, 0);
        LineResponse lineResponse = lineService.insert(일호선);

        Line line = new Line(lineResponse.getId(), "1호선", "blue", 0);

        List<StationResponse> stationsResponse = List.of(new StationResponse(station1), new StationResponse(station2));
        LineResponse expectedResponse = new LineResponse(line, stationsResponse);

        //then
        assertAll(
                () -> assertThat(expectedResponse.getId()).isEqualTo(lineResponse.getId()),
                () -> assertThat(expectedResponse.getName()).isEqualTo(lineResponse.getName()),
                () -> assertThat(expectedResponse.getColor()).isEqualTo(lineResponse.getColor()),
                () -> assertThat(expectedResponse.getStations()).containsAll(lineResponse.getStations())
        );
    }

    @Test
    @DisplayName("지하철 노선 이름이 중복된다면 등록할 수 없다.")
    void insertErrorByDuplicateName() {
        //given
        StationRequest 잠실역_요청 = new StationRequest("잠실");
        StationRequest 선릉역_요청 = new StationRequest("선릉");

        long 잠실_id = stationService.insert(잠실역_요청).getId();
        long 선릉_id = stationService.insert(선릉역_요청).getId();

        LineRequest 일호선 = new LineRequest("1호선", "blue", 잠실_id, 선릉_id, 10, 0);
        lineService.insert(일호선);

        //then
        assertThatThrownBy(() -> lineService.insert(일호선))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("지하철 노선 이름이 중복될 수 없습니다.");
    }

    @Test
    @DisplayName("지하철 노선 입력 시 id값이 동일하다면 등록할 수 없다.")
    void insertErrorByDuplicateStationId() {
        StationRequest 잠실역_요청 = new StationRequest("잠실");

        long 잠실_id = stationService.insert(잠실역_요청).getId();

        LineRequest 일호선 = new LineRequest("1호선", "blue", 잠실_id, 잠실_id, 10, 0);

        assertThatThrownBy(() -> lineService.insert(일호선))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("상행과 하행의 지하철 역이 같을 수 없습니다.");
    }

    @Test
    @DisplayName("지하철 노선 입력 시 distance값이 0이하라면 등록할 수 없다.")
    void insertErrorByDistanceUnderZero() {
        StationRequest 잠실역_요청 = new StationRequest("잠실");
        StationRequest 선릉역_요청 = new StationRequest("선릉");

        long 잠실_id = stationService.insert(잠실역_요청).getId();
        long 선릉_id = stationService.insert(선릉역_요청).getId();

        LineRequest 일호선 = new LineRequest("1호선", "blue", 잠실_id, 선릉_id, 0, 0);

        assertThatThrownBy(() -> lineService.insert(일호선))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("거리는 양수여야 합니다.");
    }


    @Test
    @DisplayName("지하철 노선 목록을 조회할 수 있다.")
    void findAll() {
        //given
        StationRequest 잠실역_요청 = new StationRequest("잠실");
        StationRequest 선릉역_요청 = new StationRequest("선릉");

        long 잠실_id = stationService.insert(잠실역_요청).getId();
        long 선릉_id = stationService.insert(선릉역_요청).getId();

        LineRequest 일호선 = new LineRequest("1호선", "blue", 잠실_id, 선릉_id, 10, 0);
        LineRequest 이호선 = new LineRequest("2호선", "green", 잠실_id, 선릉_id, 10, 0);

        lineService.insert(일호선);
        lineService.insert(이호선);

        //when
        List<LineResponse> lineResponses = lineService.findAll();

        List<String> names = lineResponses.stream()
                .map(LineResponse::getName)
                .collect(Collectors.toList());

        List<String> colors = lineResponses.stream()
                .map(LineResponse::getColor)
                .collect(Collectors.toList());

        //then
        assertAll(
                () -> assertThat(names).containsOnly("1호선", "2호선"),
                () -> assertThat(colors).containsOnly("blue", "green")
        );
    }

    @Test
    @DisplayName("존재하지 않는 지하철 노선은 조회할 수 없다.")
    void findByIdNotFound() {
        assertThatThrownBy(() -> lineService.findById(10L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("존재하지 않는 노선입니다.");
    }

    @Test
    @DisplayName("존재하지 않는 지하철 노선은 삭제할 수 없다.")
    void deleteByIdNotFound() {
        assertThatThrownBy(() -> lineService.deleteById(30L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("존재하지 않는 노선입니다.");
    }

    @Test
    @DisplayName("존재하지 않는 지하철 노선을 수정할 수 없다.")
    void updateNotFound() {
        //given
        StationRequest 잠실역_요청 = new StationRequest("잠실");
        StationRequest 선릉역_요청 = new StationRequest("선릉");

        long 잠실_id = stationService.insert(잠실역_요청).getId();
        long 선릉_id = stationService.insert(선릉역_요청).getId();

        LineRequest 일호선 = new LineRequest("1호선", "blue", 잠실_id, 선릉_id, 10, 0);
        LineResponse insert = lineService.insert(일호선);

        LineRequest 수인분당선_수정 = new LineRequest("수인분당선", "blue");

        //when & then
        assertThatThrownBy(() -> lineService.update(insert.getId() + 1, 수인분당선_수정))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("존재하지 않는 노선입니다.");
    }

    @Test
    @DisplayName("지하철 노선 이름이 중복된다면 수정할 수 없다.")
    void updateDuplicate() {
        StationRequest 잠실역_요청 = new StationRequest("잠실");
        StationRequest 선릉역_요청 = new StationRequest("선릉");

        long 잠실_id = stationService.insert(잠실역_요청).getId();
        long 선릉_id = stationService.insert(선릉역_요청).getId();

        LineRequest 일호선 = new LineRequest("1호선", "blue", 잠실_id, 선릉_id, 10, 0);
        LineRequest 이호선 = new LineRequest("2호선", "green", 잠실_id, 선릉_id, 10, 0);
        lineService.insert(일호선);
        LineResponse insert = lineService.insert(이호선);

        LineRequest 일호선_수정 = new LineRequest("1호선", "blue");//given


        //when & then
        assertThatThrownBy(() -> lineService.update(insert.getId(), 일호선_수정))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("지하철 노선 이름이 중복될 수 없습니다.");
    }
}