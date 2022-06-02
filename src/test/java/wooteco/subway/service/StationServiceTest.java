package wooteco.subway.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import wooteco.subway.dto.LineRequest;
import wooteco.subway.dto.StationRequest;
import wooteco.subway.dto.StationResponse;
import wooteco.subway.exception.NotFoundException;

@SpringBootTest
@Sql(scripts = {"classpath:test-schema.sql"})
class StationServiceTest {

    @Autowired
    private StationService stationService;

    @Autowired
    private LineService lineService;

    @Test
    @DisplayName("지하철 역 이름이 중복되지 않는다면 등록할 수 있다.")
    void save() {
        //when
        StationRequest 선릉역_요청 = new StationRequest("선릉");
        StationRequest 잠실역_요청 = new StationRequest("잠실");

        stationService.insert(선릉역_요청);

        //then
        assertThat(stationService.insert(잠실역_요청).getName()).isEqualTo("잠실");
    }

    @Test
    @DisplayName("지하철 역 이름이 중복된다면 등록할 수 없다.")
    void saveDuplicate() {
        //when
        StationRequest 선릉역_요청 = new StationRequest("선릉");
        StationRequest 잠실역_요청 = new StationRequest("잠실");

        stationService.insert(선릉역_요청);
        stationService.insert(잠실역_요청);

        //then
        assertThatThrownBy(() -> stationService.insert(잠실역_요청))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("지하철 이름이 중복될 수 없습니다.");
    }

    @Test
    @DisplayName("지하철 역 목록을 조회할 수 있다.")
    void findAll() {
        //given
        StationRequest 선릉역_요청 = new StationRequest("선릉");
        StationRequest 잠실역_요청 = new StationRequest("잠실");

        stationService.insert(선릉역_요청);
        stationService.insert(잠실역_요청);

        //when
        List<String> responseNames = stationService.findAll().stream()
                .map(StationResponse::getName)
                .collect(Collectors.toList());

        //then
        assertThat(responseNames).contains("선릉", "잠실");
    }

    @Test
    @DisplayName("존재하는 지하철 역을 삭제할 수 있다.")
    void delete() {
        //when
        StationRequest 선릉역_요청 = new StationRequest("선릉");

        StationResponse insert = stationService.insert(선릉역_요청);

        //then
        assertDoesNotThrow(() -> stationService.delete(insert.getId()));
    }

    @Test
    @DisplayName("존재하지 않는 지하철 역은 삭제할 수 없다.")
    void deleteNotFound() {
        //given
        StationRequest 선릉역_요청 = new StationRequest("선릉");

        stationService.insert(선릉역_요청);

        assertThatThrownBy(() -> stationService.delete(2L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("존재하지 않는 지하철역입니다.");
    }

    @Test
    @DisplayName("노선에 등록된 지하철 역은 삭제할 수 없다.")
    void deleteExistsInSection() {
        //given
        StationRequest 선릉역_요청 = new StationRequest("선릉");
        StationRequest 잠실역_요청 = new StationRequest("잠실");
        
        Long 선릉_id = stationService.insert(선릉역_요청).getId();
        Long 잠실_id = stationService.insert(잠실역_요청).getId();

        LineRequest lineRequest = new LineRequest("1호선", "blue", 선릉_id, 잠실_id, 10, 0);

        lineService.insert(lineRequest);

        assertThatThrownBy(() -> stationService.delete(선릉_id))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 지하철역은 노선에 등록되어 있어 삭제할 수 없습니다.");
    }
}