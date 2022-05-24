package wooteco.subway.acceptance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import wooteco.subway.acceptance.fixture.SimpleResponse;
import wooteco.subway.acceptance.fixture.SimpleRestAssured;
import wooteco.subway.dto.request.LineCreateRequest;
import wooteco.subway.dto.request.SectionRequest;
import wooteco.subway.dto.request.StationRequest;

public class SectionAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("구간을 생성한다.")
    public void createSection() {
        // given
        StationRequest stationRequest1 = new StationRequest("강남역");
        StationRequest stationRequest2 = new StationRequest("역삼역");
        StationRequest stationRequest3 = new StationRequest("선릉역");
        SimpleRestAssured.post("/stations", stationRequest1);
        SimpleRestAssured.post("/stations", stationRequest2);
        SimpleRestAssured.post("/stations", stationRequest3);

        LineCreateRequest lineCreateRequest =
                new LineCreateRequest(
                        "신분당선",
                        "bg-red-600",
                        1L,
                        2L,
                        10,
                        900);
        SimpleRestAssured.post("/lines", lineCreateRequest);

        SectionRequest sectionRequest = new SectionRequest(2L, 3L, 7);
        // when
        SimpleResponse response = SimpleRestAssured.post("/lines/1/sections", sectionRequest);
        // then
        response.assertStatus(HttpStatus.OK);
    }

    @Test
    @DisplayName("구간을 삭제한다.")
    public void deleteSection() {
        // given
        StationRequest stationRequest1 = new StationRequest("강남역");
        StationRequest stationRequest2 = new StationRequest("역삼역");
        StationRequest stationRequest3 = new StationRequest("선릉역");
        SimpleRestAssured.post("/stations", stationRequest1);
        SimpleRestAssured.post("/stations", stationRequest2);
        SimpleRestAssured.post("/stations", stationRequest3);

        LineCreateRequest lineCreateRequest =
                new LineCreateRequest(
                        "신분당선",
                        "bg-red-600",
                        1L,
                        2L,
                        10,
                        900);
        SimpleRestAssured.post("/lines", lineCreateRequest);

        SectionRequest sectionRequest = new SectionRequest(2L, 3L, 7);
        SimpleResponse response = SimpleRestAssured.post("/lines/1/sections", sectionRequest);
        // when
        SimpleRestAssured.delete("/lines/1/sections?stationId=3");
        // then
        response.assertStatus(HttpStatus.OK);
    }
}
