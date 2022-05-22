package wooteco.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.subway.dto.LineResponse;
import wooteco.subway.dto.PathResponse;
import wooteco.subway.dto.SectionRequest;
import wooteco.subway.dto.StationResponse;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class PathAcceptanceTest extends AcceptanceTest {
    /*
     * // given
     * 노선 두 개가 등록되어 있다.
     *
     * // when
     * 경로를 조회한다.
     *
     * // then
     * 경로 응답을 반환한다.
     * */

    @Test
    @DisplayName("출발 역과 도착 역의 경로를 조회하면 200-OK가 발생한다.")
    void findPath() {
        StationResponse station1 = createStation("station1");
        StationResponse station2 = createStation("station2");
        StationResponse station3 = createStation("station3");
        StationResponse station4 = createStation("station4");
        StationResponse station5 = createStation("station5");

        // when
        LineResponse createdLine = createLine("line1", "color1", station1.getId(),
                station2.getId(), 10, 0);

        SectionRequest sectionRequest = new SectionRequest(station2.getId(), station3.getId(), 10);

        createSection(sectionRequest, createdLine);

        LineResponse createdLine2 = createLine("line2", "color2", station2.getId(),
                station4.getId(), 10, 0);

        SectionRequest sectionRequest2 = new SectionRequest(station4.getId(), station5.getId(), 5);

        createSection(sectionRequest2, createdLine2);

        ExtractableResponse<Response> pathResponse = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .queryParam("source", station1.getId())
                .queryParam("target", station5.getId())
                .queryParam("age", 18)
                .log().all()
                .get("/paths")
                .then().log().all()
                .extract();

        PathResponse expected = new PathResponse(List.of(station1, station2, station4, station5), 25, 1550);
        PathResponse response = pathResponse.as(PathResponse.class);
        assertThat(pathResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    @DisplayName("출발 역 id와 도착 역 id가 null이 들어오면 400-badRequest가 발생한다.")
    void findPathFailure() {
        StationResponse station1 = createStation("station1");
        StationResponse station2 = createStation("station2");
        StationResponse station3 = createStation("station3");
        StationResponse station4 = createStation("station4");
        StationResponse station5 = createStation("station5");

        // when
        LineResponse createdLine = createLine("line1", "color1", station1.getId(),
                station2.getId(), 10, 0);

        SectionRequest sectionRequest = new SectionRequest(station2.getId(), station3.getId(), 10);

        createSection(sectionRequest, createdLine);

        LineResponse createdLine2 = createLine("line2", "color2", station2.getId(),
                station4.getId(), 10, 0);

        SectionRequest sectionRequest2 = new SectionRequest(station4.getId(), station5.getId(), 5);

        createSection(sectionRequest2, createdLine2);

        ExtractableResponse<Response> pathResponse = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .queryParam("source", Optional.empty())
                .queryParam("target", Optional.empty())
                .queryParam("age", 18)
                .log().all()
                .get("/paths")
                .then().log().all()
                .extract();

        assertThat(pathResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("나이가 음수로 들어오면 400-badRequest가 발생한다.")
    void findPathFailureWhenAgeIsNull() {
        StationResponse station1 = createStation("station1");
        StationResponse station2 = createStation("station2");
        StationResponse station3 = createStation("station3");
        StationResponse station4 = createStation("station4");
        StationResponse station5 = createStation("station5");

        // when
        LineResponse createdLine = createLine("line1", "color1", station1.getId(),
                station2.getId(), 10, 0);

        SectionRequest sectionRequest = new SectionRequest(station2.getId(), station3.getId(), 10);

        createSection(sectionRequest, createdLine);

        LineResponse createdLine2 = createLine("line2", "color2", station2.getId(),
                station4.getId(), 10, 0);

        SectionRequest sectionRequest2 = new SectionRequest(station4.getId(), station5.getId(), 5);

        createSection(sectionRequest2, createdLine2);

        ExtractableResponse<Response> pathResponse = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .queryParam("source", station1.getId())
                .queryParam("target", station5.getId())
                .queryParam("age", -1)
                .log().all()
                .get("/paths")
                .then().log().all()
                .extract();

        assertThat(pathResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(pathResponse.body().jsonPath().getString("message")).contains("나이는 0이하이면 안됩니다.");
    }

    @Test
    @DisplayName("추가요금이 있는 노선을 환승 하여 이용 할 경우 가장 높은 금액의 추가 요금이 적용되어 조회되고 200-ok를 반환한다.")
    void getPath() {

        //given
        StationResponse station1 = createStation("station1");
        StationResponse station2 = createStation("station2");
        StationResponse station3 = createStation("station3");
        StationResponse station4 = createStation("station4");
        StationResponse station5 = createStation("station5");

        LineResponse createdLine = createLine("line1", "color1", station1.getId(),
                station2.getId(), 10, 500);

        SectionRequest sectionRequest = new SectionRequest(station2.getId(), station3.getId(), 10);
        createSection(sectionRequest, createdLine);

        LineResponse createdLine2 = createLine("line2", "color2", station2.getId(),
                station4.getId(), 10, 900);

        SectionRequest sectionRequest2 = new SectionRequest(station4.getId(), station5.getId(), 5);
        createSection(sectionRequest2, createdLine2);

        ExtractableResponse<Response> pathResponse = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .queryParam("source", station1.getId())
                .queryParam("target", station5.getId())
                .queryParam("age", 21)
                .log().all()
                .get("/paths")
                .then().log().all()
                .extract();

        PathResponse expected = new PathResponse(List.of(station1, station2, station4, station5), 25, 2450);
        PathResponse response = pathResponse.as(PathResponse.class);

        assertThat(response).usingRecursiveComparison().isEqualTo(expected);
        assertThat(pathResponse.statusCode()).isEqualTo(HttpStatus.OK.value());

    }
}
