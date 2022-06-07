package wooteco.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import wooteco.subway.dto.LineRequest;
import wooteco.subway.dto.LineResponse;
import wooteco.subway.dto.StationRequest;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("지하철 노선을 생성한다.")
    void createLine() {
        // given
        StationRequest 선릉 = new StationRequest("선릉역");
        StationRequest 잠실 = new StationRequest("잠실역");

        insert(convertRequest(선릉), "/stations");
        insert(convertRequest(잠실), "/stations");

        //when
        LineRequest 일호선 = new LineRequest("1호선", "blue", 1L, 2L, 10, 0);
        ExtractableResponse<Response> response = insert(convertRequest(일호선), "/lines");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    @DisplayName("기존에 존재하는 지하철 노선 이름으로 지하철 노선을 생성할 시 400에러를 발생시킨다.")
    @Test
    void createLineWithDuplicateName() {
        // given
        StationRequest 선릉 = new StationRequest("선릉역");
        StationRequest 잠실 = new StationRequest("잠실역");

        insert(convertRequest(선릉), "/stations");
        insert(convertRequest(잠실), "/stations");

        LineRequest 일호선 = new LineRequest("1호선", "blue", 1L, 2L, 10, 0);
        insert(convertRequest(일호선), "/lines");

        //when
        ExtractableResponse<Response> createResponse = insert(convertRequest(일호선), "/lines");

        // then
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("중복되는 상하행 지하철역 id로 지하철 노선을 생성할 시 400에러를 발생시킨다.")
    @Test
    void createLineWithDuplicateStationId() {
        // given
        StationRequest 선릉 = new StationRequest("선릉역");
        StationRequest 잠실 = new StationRequest("잠실역");

        insert(convertRequest(선릉), "/stations");
        insert(convertRequest(잠실), "/stations");

        //when
        LineRequest 일호선 = new LineRequest("1호선", "blue", 1L, 1L, 10, 0);
        ExtractableResponse<Response> createResponse = insert(convertRequest(일호선), "/lines");

        // then
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }


    @DisplayName("지하철 전체 노선을 조회한다.")
    @Test
    void getLines() {
        /// given
        StationRequest 선릉 = new StationRequest("선릉역");
        StationRequest 잠실 = new StationRequest("잠실역");
        StationRequest 동두천 = new StationRequest("동두천역");

        insert(convertRequest(선릉), "/stations");
        insert(convertRequest(잠실), "/stations");
        insert(convertRequest(동두천), "/stations");

        LineRequest 일호선 = new LineRequest("1호선", "blue", 1L, 2L, 10, 0);
        LineRequest 이호선 = new LineRequest("2호선", "green", 2L, 3L, 10, 0);

        ExtractableResponse<Response> createResponse1 = insert(convertRequest(일호선), "/lines");
        ExtractableResponse<Response> createResponse2 = insert(convertRequest(이호선), "/lines");

        // when
        ExtractableResponse<Response> getResponse = get("/lines");

        // then
        assertThat(getResponse.statusCode()).isEqualTo(HttpStatus.OK.value());

        List<Long> expectedLineIds = Stream.of(createResponse1, createResponse2)
                .map(it -> Long.parseLong(it.header("Location").split("/")[2]))
                .collect(Collectors.toList());
        List<Long> resultLineIds = getResponse.jsonPath().getList(".", LineResponse.class).stream()
                .map(LineResponse::getId)
                .collect(Collectors.toList());
        assertThat(resultLineIds).containsAll(expectedLineIds);
    }


    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getLine() {
        /// given
        StationRequest 선릉 = new StationRequest("선릉역");
        StationRequest 잠실 = new StationRequest("잠실역");

        insert(convertRequest(선릉), "/stations");
        insert(convertRequest(잠실), "/stations");

        LineRequest 일호선 = new LineRequest("1호선", "blue", 1L, 2L, 10, 0);
        insert(convertRequest(일호선), "/lines");

        // when
        ExtractableResponse<Response> response = get("/lines/1");

        Long resultId = response.jsonPath().getLong("id");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(resultId).isEqualTo(1);
    }

    @DisplayName("존재하지 않는 노선을 조회할 시 404에러가 발생한다.")
    @Test
    void getLineNotExist() {
        /// given
        StationRequest 선릉 = new StationRequest("선릉역");
        StationRequest 잠실 = new StationRequest("잠실역");

        insert(convertRequest(선릉), "/stations");
        insert(convertRequest(잠실), "/stations");

        LineRequest 일호선 = new LineRequest("1호선", "blue", 1L, 2L, 10, 0);
        insert(convertRequest(일호선), "/lines");

        // when
        ExtractableResponse<Response> response = get("/lines/2");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }


    @DisplayName("지하철 노선을 제거한다.")
    @Test
    void deleteLine() {
        // given
        StationRequest 선릉 = new StationRequest("선릉역");
        StationRequest 잠실 = new StationRequest("잠실역");

        insert(convertRequest(선릉), "/stations");
        insert(convertRequest(잠실), "/stations");

        LineRequest 일호선 = new LineRequest("1호선", "blue", 1L, 2L, 10, 0);
        ExtractableResponse<Response> createResponse = insert(convertRequest(일호선), "/lines");

        // when
        String path = createResponse.header("Location");
        ExtractableResponse<Response> response = delete(path);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("기존에 존재하지 않는 노선을 삭제할 시 404에러를 발생한다.")
    @Test
    void deleteLineNotExist() {
        // when
        ExtractableResponse<Response> response = delete("/lines/1");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }


    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        StationRequest 선릉 = new StationRequest("선릉역");
        StationRequest 잠실 = new StationRequest("잠실역");

        insert(convertRequest(선릉), "/stations");
        insert(convertRequest(잠실), "/stations");

        LineRequest 일호선 = new LineRequest("1호선", "green", 1L, 2L, 10, 0);
        insert(convertRequest(일호선), "/lines");

        LineRequest 일호선_이호선으로_수정 = new LineRequest("2호선", "blue");

        ExtractableResponse<Response> updateResponse = update(convertRequest(일호선_이호선으로_수정), "/lines/1");

        // then
        ExtractableResponse<Response> findResponse = get("/lines/1");

        LineResponse lineResponse = findResponse.as(LineResponse.class);

        assertThat(updateResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(lineResponse.getName()).isEqualTo("2호선");
        assertThat(lineResponse.getColor()).isEqualTo("blue");
    }

    @DisplayName("기존에 존재하는 지하철 노선명으로 지하철 노선명을 수정할 경우 400에러를 발생시킨다.")
    @Test
    void updateLineWithDuplicateName() {
        /// given
        StationRequest 선릉 = new StationRequest("선릉역");
        StationRequest 잠실 = new StationRequest("잠실역");
        StationRequest 동두천 = new StationRequest("동두천역");

        insert(convertRequest(선릉), "/stations");
        insert(convertRequest(잠실), "/stations");
        insert(convertRequest(동두천), "/stations");

        LineRequest 일호선 = new LineRequest("1호선", "blue", 1L, 2L, 10, 0);
        LineRequest 이호선 = new LineRequest("2호선", "green", 2L, 3L, 10, 0);

        insert(convertRequest(일호선), "/lines");
        insert(convertRequest(이호선), "/lines");

        LineRequest 일호선_이호선으로_수정 = new LineRequest("2호선", "blue");

        //when
        ExtractableResponse<Response> updateResponse = update(convertRequest(일호선_이호선으로_수정), "/lines/1");

        // then
        assertThat(updateResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }


    @DisplayName("기존에 존재하지 않는 노선을 수정할 시 404에러를 발생한다.")
    @Test
    void updateLineNotExist() {
        //given
        LineRequest 일호선_이호선으로_수정 = new LineRequest("2호선", "blue");

        //when
        ExtractableResponse<Response> updateResponse = update(convertRequest(일호선_이호선으로_수정), "/lines/1");

        // then
       assertThat(updateResponse.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

}
