package wooteco.subway.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import wooteco.subway.dto.LineRequest;
import wooteco.subway.dto.SectionRequest;
import wooteco.subway.dto.StationRequest;
import wooteco.subway.exception.NotFoundException;

@SpringBootTest
@Sql(scripts = {"classpath:test-schema.sql"})
class SectionServiceTest {

    @Autowired
    private SectionService sectionService;

    @Autowired
    LineService lineService;

    @Autowired
    StationService stationService;

    @Test
    @DisplayName("구간 등록 시 입력한 id값의 노선이 없으면 에러를 발생시킨다.")
    void insertErrorByLineNotExist() {
        StationRequest 잠실역_요청 = new StationRequest("잠실");
        StationRequest 선릉역_요청 = new StationRequest("선릉");

        long 잠실_id = stationService.insert(잠실역_요청).getId();
        long 선릉_id = stationService.insert(선릉역_요청).getId();

        LineRequest 일호선 = new LineRequest("1호선", "blue", 잠실_id, 선릉_id, 10, 0);
        long 일호선_id = lineService.insert(일호선).getId();

        assertThatThrownBy(() -> sectionService.insert(new SectionRequest(잠실_id, 선릉_id, 10), 일호선_id + 1))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("존재하지 않는 노선입니다.");
    }

    @Test
    @DisplayName("구간 등록 시 입력한 id값의 지하철 역이 없으면 에러를 발생시킨다.")
    void insertErrorByStationNotExist() {
        StationRequest 잠실역_요청 = new StationRequest("잠실");
        StationRequest 선릉역_요청 = new StationRequest("선릉");

        long 잠실_id = stationService.insert(잠실역_요청).getId();
        long 선릉_id = stationService.insert(선릉역_요청).getId();

        LineRequest 일호선 = new LineRequest("1호선", "blue", 잠실_id, 선릉_id, 10, 0);
        long 일호선_id = lineService.insert(일호선).getId();

        assertThatThrownBy(() -> sectionService.insert(new SectionRequest(잠실_id, 선릉_id + 1, 10), 일호선_id))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("존재하지 않는 지하철역입니다.");
    }

    @Test
    @DisplayName("구간 등록 시 입력한 station 값이 같으면 에러를 발생시킨다.")
    void insertErrorBySameStationId() {
        StationRequest 잠실역_요청 = new StationRequest("잠실");
        StationRequest 선릉역_요청 = new StationRequest("선릉");

        long 잠실_id = stationService.insert(잠실역_요청).getId();
        long 선릉_id = stationService.insert(선릉역_요청).getId();

        LineRequest 일호선 = new LineRequest("1호선", "blue", 잠실_id, 선릉_id, 10, 0);
        long 일호선_id = lineService.insert(일호선).getId();

        assertThatThrownBy(() -> sectionService.insert(new SectionRequest(잠실_id, 잠실_id, 10), 일호선_id))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("상행과 하행의 지하철 역이 같을 수 없습니다.");
    }

    @Test
    @DisplayName("구간 등록 시 입력한 distance 값이 0 이하라면 에러를 발생시킨다.")
    void insertErrorByDistanceUnderZero() {
        StationRequest 잠실역_요청 = new StationRequest("잠실");
        StationRequest 선릉역_요청 = new StationRequest("선릉");

        long 잠실_id = stationService.insert(잠실역_요청).getId();
        long 선릉_id = stationService.insert(선릉역_요청).getId();

        LineRequest 일호선 = new LineRequest("1호선", "blue", 잠실_id, 선릉_id, 10, 0);
        long 일호선_id = lineService.insert(일호선).getId();

        assertThatThrownBy(() -> sectionService.insert(new SectionRequest(잠실_id, 선릉_id, -1), 일호선_id))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("거리는 양수여야 합니다.");
    }

    @Test
    @DisplayName("구간 등록 시 입력한 section 내의 stationId 값이 모두 기존에 존재하는 경우 에러를 발생시킨다.")
    void insertErrorByDistanceAlreadyContainsAll() {
        StationRequest 잠실역_요청 = new StationRequest("잠실");
        StationRequest 선릉역_요청 = new StationRequest("선릉");

        long 잠실_id = stationService.insert(잠실역_요청).getId();
        long 선릉_id = stationService.insert(선릉역_요청).getId();

        LineRequest 일호선 = new LineRequest("1호선", "blue", 잠실_id, 선릉_id, 10, 0);
        long 일호선_id = lineService.insert(일호선).getId();

        assertThatThrownBy(() -> sectionService.insert(new SectionRequest(잠실_id, 선릉_id, 10), 일호선_id))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("상행역과 하행역이 이미 모두 노선에 등록되어 있습니다.");
    }

    @Test
    @DisplayName("구간 등록 시 입력한 section 내의 stationId 값이 모두 기존에 존재하지 않는 경우 에러를 발생시킨다.")
    void insertErrorByDistanceNotContains() {
        //given
        StationRequest 잠실역_요청 = new StationRequest("잠실");
        StationRequest 선릉역_요청 = new StationRequest("선릉");
        StationRequest 동두천역_요청 = new StationRequest("동두천");
        StationRequest 지행역_요청 = new StationRequest("지행");

        long 잠실_id = stationService.insert(잠실역_요청).getId();
        long 선릉_id = stationService.insert(선릉역_요청).getId();
        long 동두천_id = stationService.insert(동두천역_요청).getId();
        long 지행역_id = stationService.insert(지행역_요청).getId();

        LineRequest 일호선 = new LineRequest("1호선", "blue", 잠실_id, 선릉_id, 10, 0);
        long 일호선_id = lineService.insert(일호선).getId();

        //then
        assertThatThrownBy(() -> sectionService.insert(new SectionRequest(동두천_id, 지행역_id, 10), 일호선_id))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("상행역과 하행역이 모두 노선에 등록되어 있지 않습니다.");
    }

    @Test
    @DisplayName("구간 삭제 시 입력한 id값의 노선이 없으면 에러를 발생시킨다.")
    void deleteErrorByLineNotExist() {
        StationRequest 잠실역_요청 = new StationRequest("잠실");
        StationRequest 선릉역_요청 = new StationRequest("선릉");

        long 잠실_id = stationService.insert(잠실역_요청).getId();
        long 선릉_id = stationService.insert(선릉역_요청).getId();

        LineRequest 일호선 = new LineRequest("1호선", "blue", 잠실_id, 선릉_id, 10, 0);
        long 일호선_id = lineService.insert(일호선).getId();

        assertThatThrownBy(() -> sectionService.delete(일호선_id + 1, 잠실_id))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("존재하지 않는 노선입니다.");
    }

    @Test
    @DisplayName("구간 삭제 시 입력한 id값의 지하철 역이 없으면 에러를 발생시킨다.")
    void deleteErrorByStationNotExist() {
        StationRequest 잠실역_요청 = new StationRequest("잠실");
        StationRequest 선릉역_요청 = new StationRequest("선릉");

        long 잠실_id = stationService.insert(잠실역_요청).getId();
        long 선릉_id = stationService.insert(선릉역_요청).getId();

        LineRequest 일호선 = new LineRequest("1호선", "blue", 잠실_id, 선릉_id, 10, 0);
        long 일호선_id = lineService.insert(일호선).getId();

        assertThatThrownBy(() -> sectionService.delete(일호선_id, 선릉_id + 1))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("존재하지 않는 지하철역입니다.");
    }

    @Test
    @DisplayName("구간 삭제 시 구간이 하나뿐이라면 에러를 발생시킨다.")
    void deleteErrorBySectionSizeIsOne() {
        StationRequest 잠실역_요청 = new StationRequest("잠실");
        StationRequest 선릉역_요청 = new StationRequest("선릉");

        long 잠실_id = stationService.insert(잠실역_요청).getId();
        long 선릉_id = stationService.insert(선릉역_요청).getId();

        LineRequest 일호선 = new LineRequest("1호선", "blue", 잠실_id, 선릉_id, 10, 0);
        long 일호선_id = lineService.insert(일호선).getId();

        assertThatThrownBy(() -> sectionService.delete(일호선_id, 선릉_id))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 노선은 더 삭제할 수 없습니다.");
    }
}