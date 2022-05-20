package wooteco.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpStatus;
import wooteco.subway.dto.LineRequest;
import wooteco.subway.dto.LineResponse;
import wooteco.subway.dto.StationResponse;

@DisplayName("노선 관련 기능")
class LineAcceptanceTest extends AcceptanceTest {

    @BeforeEach
    void setup() {
        createStation("아차산역");
        createStation("군자역");
        createStation("장한평역");
        createLine("5호선", "bg-purple-600", 1L, 2L, 10);
    }


    @DisplayName("라인을 등록한다.")
    @Test
    void createLine() {
        // when
        LineRequest newLineRequest = new LineRequest("6호선", "bg-yellow-600", 2L, 3L, 20);
        final ExtractableResponse<Response> response = AcceptanceTestFixture.post("/lines", newLineRequest);
        final List<StationResponse> stations = response.jsonPath().getList("stations", StationResponse.class);

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(response.header("Location")).isNotBlank(),
                () -> assertThat(stations).extracting("name")
                        .containsExactly("군자역", "장한평역")
        );
    }

    @DisplayName("라인을 등록할 때 입력값이 올바르지 않으면 예외를 발생한다.")
    @MethodSource("thrownArguments")
    @ParameterizedTest()
    void thrown_invalidArguments(LineRequest newLineRequest, String message) {
        final ExtractableResponse<Response> response = AcceptanceTestFixture.post("/lines", newLineRequest);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(response.jsonPath().getString("message")).isEqualTo(message)
        );
    }

    private static Stream<Arguments> thrownArguments() {
        return Stream.of(
                Arguments.of(new LineRequest("", "bg-purple-600", 1L, 2L, 10), "노선 이름은 공백일 수 없습니다."),
                Arguments.of(new LineRequest(null, "bg-purple-600", 1L, 2L, 10), "노선 이름은 공백일 수 없습니다."),
                Arguments.of(new LineRequest("이름", "", 1L, 2L, 10), "노선 색상은 공백일 수 없습니다."),
                Arguments.of(new LineRequest("이름", null, 1L, 2L, 10), "노선 색상은 공백일 수 없습니다."),
                Arguments.of(new LineRequest("이름", "색상", null, 2L, 10), "상행역은 비어있을 수 없습니다."),
                Arguments.of(new LineRequest("이름", "색상", 1L, null, 10), "하행역은 비어있을 수 없습니다."),
                Arguments.of(new LineRequest("이름", "색상", 1L, 2L, 0), "거리는 양수이어야 합니다.")
        );
    }

    @DisplayName("기존에 존재하는 노선 이름으로 노선을 생성하면 예외를 발생한다.")
    @Test
    void createLineWithDuplicateName() {
        LineRequest newLineRequest = new LineRequest("5호선", "bg-purple-600", 1L, 2L, 10);
        AcceptanceTestFixture.post("/lines", newLineRequest);

        // when
        final ExtractableResponse<Response> response = AcceptanceTestFixture.post("/lines", newLineRequest);

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(response.body().jsonPath().getString("message")).isEqualTo("이미 같은 이름의 노선이 존재합니다.")
        );
    }

    @DisplayName("전체 노선들을 조회한다.")
    @Test
    void findAllLines() {
        // given
        createLine("신분당선", "rgb-yellow-600", 1L, 2L, 10);

        // when
        final ExtractableResponse<Response> response = AcceptanceTestFixture.get("/lines");

        // then
        final List<LineResponse> lines = response.jsonPath().getList(".", LineResponse.class);

        List<Long> expectedLineIds = List.of(1L, 2L);
        List<Long> resultLineIds = lines.stream()
                .map(LineResponse::getId)
                .collect(Collectors.toList());

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(resultLineIds).containsAll(expectedLineIds),
                () -> assertThat(lines.get(0).getStations()).extracting("name")
                        .containsExactly("아차산역", "군자역"),
                () -> assertThat(lines.get(1).getStations()).extracting("name")
                        .containsExactly("아차산역", "군자역")

        );
    }

    @DisplayName("특정 노선을 조회한다.")
    @Test
    void findLine() {
        // when
        final ExtractableResponse<Response> response = AcceptanceTestFixture.get("/lines/1");
        final List<StationResponse> stations = response.jsonPath().getList("stations", StationResponse.class);

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.jsonPath().getLong("id")).isEqualTo(1L),
                () -> assertThat(response.jsonPath().getString("name")).isEqualTo("5호선"),
                () -> assertThat(response.jsonPath().getString("color")).isEqualTo("bg-purple-600"),
                () -> assertThat(stations).extracting("name")
                        .containsExactly("아차산역", "군자역")
        );
    }

    @DisplayName("특정 노선을 수정한다.")
    @Test
    void updateLine() {
        // when
        LineRequest newLineRequest = new LineRequest("6호선", "bg-brown-600");
        final ExtractableResponse<Response> response = AcceptanceTestFixture.put("/lines/1", newLineRequest);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("노선을 제거한다.")
    @Test
    void deleteLine() {
        // when
        final ExtractableResponse<Response> response = AcceptanceTestFixture.delete("/lines/1");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
