package wooteco.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.helper.TLine.LINE_NO_EXTRA_FARE;
import static wooteco.subway.helper.TStation.CHANGSIN;
import static wooteco.subway.helper.TStation.DONGMYO;
import static wooteco.subway.helper.TStation.SINDANG;
import static wooteco.subway.helper.TestFixtures.STANDARD_DISTANCE;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.LineResponse;
import wooteco.subway.dto.SectionRequest;
import wooteco.subway.dto.StationResponse;

@DisplayName("섹션 관련 기능")
public class SectionAcceptanceTest extends AcceptanceTest {

    @DisplayName("섹션을 등록하면 200 Ok를 반환한다.")
    @Test
    void createSection() {
        Station 신당역 = SINDANG.역을등록한다();
        Station 동묘앞역 = DONGMYO.역을등록한다();
        Station 창신역 = CHANGSIN.역을등록한다();

        SectionRequest 신당_동묘 = createSectionRequest(신당역, 동묘앞역, STANDARD_DISTANCE);
        SectionRequest 동묘_창신 = createSectionRequest(동묘앞역, 창신역, STANDARD_DISTANCE);

        ExtractableResponse<Response> response = LINE_NO_EXTRA_FARE.노선을등록하고(신당_동묘).구간을등록한다(동묘_창신);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        LineResponse lineResponse = LINE_NO_EXTRA_FARE.노선에서().단건노선을조회한다()
                .as(LineResponse.class);
        assertThat(lineResponse.getStations())
                .containsExactly(
                        new StationResponse(신당역),
                        new StationResponse(동묘앞역),
                        new StationResponse(창신역)
                );
    }

    @DisplayName("거리가 초과하는 섹션을 등록하면 400 BadRequest를 반환한다.")
    @Test
    void createSectionDistanceFail() {
        Station 신당역 = SINDANG.역을등록한다();
        Station 동묘앞역 = DONGMYO.역을등록한다();
        Station 창신역 = CHANGSIN.역을등록한다();

        SectionRequest 신당_창신 = createSectionRequest(신당역, 창신역, STANDARD_DISTANCE);
        SectionRequest 동묘_창신 = createSectionRequest(동묘앞역, 창신역, STANDARD_DISTANCE + 1);

        ExtractableResponse<Response> response = LINE_NO_EXTRA_FARE.노선을등록하고(신당_창신).구간을등록한다(동묘_창신);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("이미 연결된 섹션을 등록하면 400 BadRequest를 반환한다.")
    @Test
    void createSectionAlreadyConnectedFail() {
        Station 신당역 = SINDANG.역을등록한다();
        Station 동묘앞역 = DONGMYO.역을등록한다();

        SectionRequest 신당_동묘 = createSectionRequest(신당역, 동묘앞역, STANDARD_DISTANCE);
        ExtractableResponse<Response> response = LINE_NO_EXTRA_FARE.노선을등록하고(신당_동묘).구간을등록한다(신당_동묘);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("섹션을 삭제하면 200 Ok를 반환한다.")
    @Test
    void deleteSection() {
        Station 신당역 = SINDANG.역을등록한다();
        Station 동묘앞역 = DONGMYO.역을등록한다();
        Station 창신역 = CHANGSIN.역을등록한다();

        SectionRequest 신당_동묘 = createSectionRequest(신당역, 동묘앞역, STANDARD_DISTANCE);
        SectionRequest 동묘_창신 = createSectionRequest(동묘앞역, 창신역, STANDARD_DISTANCE);

        LINE_NO_EXTRA_FARE.노선을등록하고(신당_동묘).구간을등록한다(동묘_창신);
        LINE_NO_EXTRA_FARE.노선에서().구간을삭제한다(동묘앞역, HttpStatus.OK.value());
    }

    @DisplayName("섹션을 하나 뿐일 때 삭제하면 400 Bad Request를 반환한다.")
    @Test
    void deleteSectionException() {
        Station 신당역 = SINDANG.역을등록한다();
        Station 동묘앞역 = DONGMYO.역을등록한다();

        SectionRequest 신당_동묘 = createSectionRequest(신당역, 동묘앞역, STANDARD_DISTANCE);

        LINE_NO_EXTRA_FARE.노선을등록하고(신당_동묘).구간을삭제한다(동묘앞역, HttpStatus.BAD_REQUEST.value());
    }

    private SectionRequest createSectionRequest(Station upStation, Station downStation, int distance) {
        return new SectionRequest(upStation.getId(), downStation.getId(), distance);
    }
}
