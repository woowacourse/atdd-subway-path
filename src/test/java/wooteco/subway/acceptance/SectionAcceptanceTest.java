package wooteco.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.acceptance.AcceptanceFixture.낙성대;
import static wooteco.subway.acceptance.AcceptanceFixture.낙성대_사당;
import static wooteco.subway.acceptance.AcceptanceFixture.방배;
import static wooteco.subway.acceptance.AcceptanceFixture.방배_서초;
import static wooteco.subway.acceptance.AcceptanceFixture.봉천;
import static wooteco.subway.acceptance.AcceptanceFixture.봉천_낙성대;
import static wooteco.subway.acceptance.AcceptanceFixture.봉천_사당;
import static wooteco.subway.acceptance.AcceptanceFixture.사당;
import static wooteco.subway.acceptance.AcceptanceFixture.사당_방배;
import static wooteco.subway.acceptance.AcceptanceFixture.사당_서초;
import static wooteco.subway.acceptance.AcceptanceFixture.서울대입구;
import static wooteco.subway.acceptance.AcceptanceFixture.서초;
import static wooteco.subway.acceptance.AcceptanceFixture.이호선;
import static wooteco.subway.acceptance.ResponseCreator.createPostLineResponse;
import static wooteco.subway.acceptance.ResponseCreator.createPostSectionResponse;
import static wooteco.subway.acceptance.ResponseCreator.createPostStationResponse;
import static wooteco.subway.acceptance.ResponseCreator.deleteSectionResponse;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import wooteco.subway.controller.dto.line.LineResponse;
import wooteco.subway.controller.dto.station.StationResponse;

@DisplayName("지하철 구간 기능")
public class SectionAcceptanceTest extends AcceptanceTest {

    @BeforeEach
    void init() {
        createPostStationResponse(낙성대);
        createPostStationResponse(사당);
        createPostStationResponse(방배);
        createPostStationResponse(서초);
        createPostStationResponse(서울대입구);
        createPostStationResponse(봉천);

        createPostLineResponse(이호선);
    }

    @Test
    @DisplayName("같은 호선에 지하철을 등록할 수 있다.")
    void createSection() {
        //given
        //when
        createPostSectionResponse(1L, 사당_방배);
        //then
        checkStationInSection(
                1L,
                new StationResponse(1L, 낙성대.getName()),
                new StationResponse(2L, 사당.getName()),
                new StationResponse(3L, 방배.getName())
        );
    }

    @Test
    @DisplayName("상행 종점 등록이 가능하다.")
    void createFinalUpSection() {
        //given
        //when
        createPostSectionResponse(1L, 봉천_낙성대);
        //then
        checkStationInSection(
                1L,
                new StationResponse(6L, 봉천.getName()),
                new StationResponse(1L, 낙성대.getName()),
                new StationResponse(2L, 사당.getName())
        );
    }

    @Test
    @DisplayName("하행 종점 등록이 가능하다.")
    void createFinalDownSection() {
        //given
        //when
        createPostSectionResponse(1L, 사당_서초);
        //then
        checkStationInSection(
                1L,
                new StationResponse(1L, 낙성대.getName()),
                new StationResponse(2L, 사당.getName()),
                new StationResponse(4L, 서초.getName())
        );
    }

    @Test
    @DisplayName("중간 구간 등록이 가능하다.")
    void createMiddleDownSection() {
        //given
        //when
        createPostSectionResponse(1L, 사당_서초);
        createPostSectionResponse(1L, 사당_방배);
        //then
        checkStationInSection(
                1L,
                new StationResponse(1L, 낙성대.getName()),
                new StationResponse(2L, 사당.getName()),
                new StationResponse(3L, 방배.getName()),
                new StationResponse(4L, 서초.getName())
        );
    }

    @Test
    @DisplayName("이미 존재하는 구간은 등록할 수 없다.")
    void alreadyExistSection() {
        //given
        //when
        ExtractableResponse<Response> response = createPostSectionResponse(1L, 낙성대_사당);
        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("등록되지 않은 지하철역에 구간을 등록할 수 없다.")
    void NotEnrollmentStation() {
        //given
        //when
        ExtractableResponse<Response> response = createPostSectionResponse(1L, 방배_서초);
        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("존재하는 구간보다 구간이 더 긴 구간을 등록할 수 없다.")
    void overDistanceError() {
        //given
        //when
        ExtractableResponse<Response> response = createPostSectionResponse(1L, 봉천_사당);
        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("상행종점 지하철 구간을 제거할 수 있다.")
    void deleteFinalUpStation() {
        //given
        createPostSectionResponse(1L, 봉천_낙성대);
        //when
        ExtractableResponse<Response> response = deleteSectionResponse(1L, 6L);
        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        checkStationInSection(
                1L,
                new StationResponse(1L, 낙성대.getName()),
                new StationResponse(2L, 사당.getName())
        );
    }

    @Test
    @DisplayName("하행종점 지하철 구간을 제거할 수 있다.")
    void deleteFinalDownStation() {
        //given
        createPostSectionResponse(1L, 사당_방배);
        //when
        ExtractableResponse<Response> response = deleteSectionResponse(1L, 3L);
        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        checkStationInSection(
                1L,
                new StationResponse(1L, 낙성대.getName()),
                new StationResponse(2L, 사당.getName())
        );
    }

    @Test
    @DisplayName("중간 지하철 구간을 제거할 수 있다.")
    void deleteMiddleStation() {
        //given
        createPostSectionResponse(1L, 사당_방배);
        //when
        ExtractableResponse<Response> response = deleteSectionResponse(1L, 2L);
        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        checkStationInSection(
                1L,
                new StationResponse(1L, 낙성대.getName()),
                new StationResponse(3L, 방배.getName())
        );
    }

    @Test
    @DisplayName("등록되지 않은 지하철 구간을 제거할 수 없다.")
    void NotEnrollmentDeleteStation() {
        //given
        //when
        ExtractableResponse<Response> response = deleteSectionResponse(1L, 3L);
        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("구간이 하나일 경우 제거할 수 없다")
    void oneSection() {
        //given
        //when
        ExtractableResponse<Response> response = deleteSectionResponse(1L, 2L);
        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private void checkStationInSection(Long lineId, StationResponse... stationResponses) {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when()
                .get("/lines/" + lineId)
                .then().log().all()
                .extract();
        final LineResponse expected = new LineResponse(lineId, 이호선.getName(), 이호선.getColor(), List.of(stationResponses));
        final LineResponse actual = response.body().jsonPath().getObject(".", LineResponse.class);

        assertThat(expected)
                .usingRecursiveComparison()
                .isEqualTo(actual);
    }
}
