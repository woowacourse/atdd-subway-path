package wooteco.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static wooteco.subway.common.TLine.LINE_SIX;
import static wooteco.subway.common.TStation.BOMUN;
import static wooteco.subway.common.TStation.CHANGSIN;
import static wooteco.subway.common.TStation.DONGMYO;
import static wooteco.subway.common.TStation.SINDANG;
import static wooteco.subway.common.TestFixtures.STANDARD_DISTANCE;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.PathResponse;
import wooteco.subway.dto.SectionRequest;
import wooteco.subway.dto.StationResponse;

class PathAcceptanceTest extends AcceptanceTest {

    @DisplayName("경로 조회를 요청하면, 200 OK 와 관련 지하철역 정보, 거리, 요금을 반환한다.")
    @Test
    void getPaths() {
        Station 신당역 = SINDANG.역을등록한다();
        Station 동묘앞역 = DONGMYO.역을등록한다();
        Station 창신역 = CHANGSIN.역을등록한다();

        SectionRequest 신당_동묘 = createSectionRequest(신당역, 동묘앞역, STANDARD_DISTANCE);
        SectionRequest 동묘_창신 = createSectionRequest(동묘앞역, 창신역, STANDARD_DISTANCE);
        LINE_SIX.노선을등록하고(신당_동묘).구간을등록한다(동묘_창신);
        PathResponse pathResponse = SINDANG.에서(CHANGSIN).의최단거리를계산한다(20)
                .as(PathResponse.class);
        assertAll(() -> {
            assertThat(pathResponse.getStations()).containsExactly(new StationResponse(신당역),
                    new StationResponse(동묘앞역),
                    new StationResponse(창신역));
            assertThat(pathResponse.getDistance()).isEqualTo(STANDARD_DISTANCE + STANDARD_DISTANCE);
            assertThat(pathResponse.getFare()).isEqualTo(1650);
        });
    }

    @DisplayName("경로 조회 시, 연결된 구간을 찾을 수 없으면 404 Not Found 에러를 발생한다.")
    @Test
    void getPathsException() {
        Station 신당역 = SINDANG.역을등록한다();
        Station 동묘앞역 = DONGMYO.역을등록한다();
        Station 창신역 = CHANGSIN.역을등록한다();
        Station 보문역 = BOMUN.역을등록한다();

        SectionRequest 신당_동묘 = createSectionRequest(신당역, 동묘앞역, STANDARD_DISTANCE);
        SectionRequest 보문_창신 = createSectionRequest(보문역, 창신역, STANDARD_DISTANCE);
        LINE_SIX.노선을등록하고(신당_동묘).구간을등록한다(보문_창신);
        ExtractableResponse<Response> response = SINDANG.에서(CHANGSIN).의최단거리를계산한다(15);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    private SectionRequest createSectionRequest(Station up, Station down, int distance) {
        return new SectionRequest(up.getId(), down.getId(), distance);
    }
}
