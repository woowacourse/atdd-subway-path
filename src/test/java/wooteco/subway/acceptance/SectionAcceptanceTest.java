package wooteco.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpStatus;
import wooteco.subway.dto.SectionRequest;

@DisplayName("지하철 구간 관련 기능")
class SectionAcceptanceTest extends AcceptanceTest {

    @DisplayName("특정 노선의 구간을 추가한다.")
    @Test
    void createSection() {
        // given
        createStation("아차산역");
        createStation("군자역");
        createStation("마장역");
        createLine("5호선", "bg-purple-600", 1L, 2L, 10, 0);

        // when
        SectionRequest sectionRequest = new SectionRequest(2L, 3L, 5);
        final ExtractableResponse<Response> response = AcceptanceTestFixture.post("/lines/1/sections",
                sectionRequest);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("특정 노선의 구간을 추가할 때 입력값이 올바르지 않으면 예외를 발생한다.")
    @ParameterizedTest
    @MethodSource("thrownArguments")
    void thrown_invalidArguments(SectionRequest newSectionRequest, String errorMessage) {
        // given
        createStation("아차산역");
        createStation("군자역");
        createStation("마장역");
        createLine("5호선", "bg-purple-600", 1L, 2L, 10, 0);

        // when
        final ExtractableResponse<Response> response = AcceptanceTestFixture.post("/lines/1/sections",
                newSectionRequest);

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(response.jsonPath().getString("message")).isEqualTo(errorMessage)
        );
    }

    private static Stream<Arguments> thrownArguments() {
        return Stream.of(
                Arguments.of(new SectionRequest(null, 2L, 10), "상행역은 비어있을 수 없습니다."),
                Arguments.of(new SectionRequest(1L, null, 10), "하행역은 비어있을 수 없습니다."),
                Arguments.of(new SectionRequest(1L, 2L, 0), "거리는 양수이어야 합니다.")
        );
    }

    @DisplayName("특정 노선의 구간을 삭제한다.")
    @Test
    void deleteSection() {
        // given
        createStation("아차산역");
        createStation("군자역");
        createStation("마장역");
        createLine("5호선", "bg-purple-600", 1L, 2L, 10, 0);

        // when
        addSection(2L, 3L, 5, 1L);

        // then
        final ExtractableResponse<Response> response = AcceptanceTestFixture.delete("/lines/1/sections?stationId=2");

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
