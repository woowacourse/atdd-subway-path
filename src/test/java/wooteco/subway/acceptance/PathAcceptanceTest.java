package wooteco.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static wooteco.subway.common.TLine.LINE_NO_EXTRA_FARE;
import static wooteco.subway.common.TLine.LINE_SIX;
import static wooteco.subway.common.TLine.LINE_TWO;
import static wooteco.subway.common.TStation.BOMUN;
import static wooteco.subway.common.TStation.CHANGSIN;
import static wooteco.subway.common.TStation.DONGMYO;
import static wooteco.subway.common.TStation.SANGWANGSIMNI;
import static wooteco.subway.common.TStation.SINDANG;
import static wooteco.subway.common.TStation.WANGSIMNI;
import static wooteco.subway.common.TestFixtures.CHILD_DEFAULT_FARE;
import static wooteco.subway.common.TestFixtures.CHILD_MAX_AGE;
import static wooteco.subway.common.TestFixtures.DEFAULT_AGE;
import static wooteco.subway.common.TestFixtures.DEFAULT_FARE;
import static wooteco.subway.common.TestFixtures.HALF_STANDARD_DISTANCE;
import static wooteco.subway.common.TestFixtures.STANDARD_DISTANCE;
import static wooteco.subway.common.TestFixtures.TEEN_DEFAULT_FARE;
import static wooteco.subway.common.TestFixtures.TEEN_MAX_AGE;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.PathResponse;
import wooteco.subway.dto.SectionRequest;
import wooteco.subway.dto.StationResponse;

class PathAcceptanceTest extends AcceptanceTest {

    @DisplayName("경로 조회 시, 200 OK 와 관련 지하철역 정보, 거리, 요금을 반환한다.")
    @Test
    void getPaths() {
        Station 신당역 = SINDANG.역을등록한다();
        Station 동묘앞역 = DONGMYO.역을등록한다();
        Station 창신역 = CHANGSIN.역을등록한다();

        SectionRequest 신당_동묘 = createSectionRequest(신당역, 동묘앞역, STANDARD_DISTANCE);
        SectionRequest 동묘_창신 = createSectionRequest(동묘앞역, 창신역, STANDARD_DISTANCE);

        LINE_NO_EXTRA_FARE.노선을등록하고(신당_동묘).구간을등록한다(동묘_창신);

        PathResponse pathResponse = SINDANG.부터(CHANGSIN).의최단거리를계산한다(DEFAULT_AGE, HttpStatus.OK.value())
                .as(PathResponse.class);

        assertAll(() -> {
            assertThat(pathResponse.getStations()).containsExactly(new StationResponse(신당역),
                    new StationResponse(동묘앞역),
                    new StationResponse(창신역));
            assertThat(pathResponse.getDistance()).isEqualTo(STANDARD_DISTANCE + STANDARD_DISTANCE);
            assertThat(pathResponse.getFare()).isEqualTo(DEFAULT_FARE + 200);
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
        SINDANG.부터(CHANGSIN).의최단거리를계산한다(DEFAULT_AGE, HttpStatus.NOT_FOUND.value());
    }

    @DisplayName("경로 조회 시, 환승이 발생하면 최대 추가비용이 추가되고 200 OK를 반환한다.")
    @Test
    void getPathsTransfer() {
        Station 동묘앞역 = DONGMYO.역을등록한다();
        Station 신당역 = SINDANG.역을등록한다();
        Station 상왕십리역 = SANGWANGSIMNI.역을등록한다();

        SectionRequest 신당_동묘 = createSectionRequest(신당역, 동묘앞역, STANDARD_DISTANCE);
        SectionRequest 신당_상왕십리 = createSectionRequest(신당역, 상왕십리역, STANDARD_DISTANCE);

        LINE_SIX.노선을등록한다(신당_동묘);
        LINE_TWO.노선을등록한다(신당_상왕십리);

        PathResponse response = DONGMYO.부터(SANGWANGSIMNI).의최단거리를계산한다(DEFAULT_AGE, HttpStatus.OK.value())
                .as(PathResponse.class);

        assertThat(response.getFare()).isEqualTo(DEFAULT_FARE
                + LINE_SIX.getExtraFare()
                + 200);
    }

    @DisplayName("경로 조회 시, 청소년 요금을 할인하고 200 OK를 반환한다.")
    @Test
    void getPathsTeenAgerDiscount() {
        Station 신당역 = SINDANG.역을등록한다();
        Station 동묘앞역 = DONGMYO.역을등록한다();
        Station 창신역 = CHANGSIN.역을등록한다();

        SectionRequest 신당_동묘 = createSectionRequest(신당역, 동묘앞역, HALF_STANDARD_DISTANCE);
        SectionRequest 동묘_창신 = createSectionRequest(동묘앞역, 창신역, HALF_STANDARD_DISTANCE);

        LINE_NO_EXTRA_FARE.노선을등록하고(신당_동묘).구간을등록한다(동묘_창신);

        PathResponse pathResponse = SINDANG.부터(CHANGSIN).의최단거리를계산한다(TEEN_MAX_AGE, HttpStatus.OK.value())
                .as(PathResponse.class);

        assertThat(pathResponse.getFare()).isEqualTo(TEEN_DEFAULT_FARE);
    }

    @DisplayName("경로 조회 시, 어린이 요금을 할인하고 200 OK를 반환한다.")
    @Test
    void getPathsChildrenDiscount() {
        Station 신당역 = SINDANG.역을등록한다();
        Station 동묘앞역 = DONGMYO.역을등록한다();
        Station 창신역 = CHANGSIN.역을등록한다();

        SectionRequest 신당_동묘 = createSectionRequest(신당역, 동묘앞역, HALF_STANDARD_DISTANCE);
        SectionRequest 동묘_창신 = createSectionRequest(동묘앞역, 창신역, HALF_STANDARD_DISTANCE);

        LINE_NO_EXTRA_FARE.노선을등록하고(신당_동묘).구간을등록한다(동묘_창신);

        PathResponse pathResponse = SINDANG.부터(CHANGSIN).의최단거리를계산한다(CHILD_MAX_AGE, HttpStatus.OK.value())
                .as(PathResponse.class);

        assertThat(pathResponse.getFare()).isEqualTo(CHILD_DEFAULT_FARE);
    }

    @DisplayName("경로 조회 시, 50km 이상의 요금 정책에 따라 계산하고 200 OK를 반환한다.")
    @Test
    void getPathsMaxFareStrategy() {
        Station 왕십리 = WANGSIMNI.역을등록한다();
        Station 상왕십리 = SANGWANGSIMNI.역을등록한다();
        Station 신당역 = SINDANG.역을등록한다();
        Station 동묘앞역 = DONGMYO.역을등록한다();
        Station 창신역 = CHANGSIN.역을등록한다();
        Station 보문역 = BOMUN.역을등록한다();

        SectionRequest 왕십리_상왕십리 = createSectionRequest(왕십리, 상왕십리, STANDARD_DISTANCE);
        SectionRequest 상왕십리_신당 = createSectionRequest(상왕십리, 신당역, STANDARD_DISTANCE);
        SectionRequest 신당_동묘 = createSectionRequest(신당역, 동묘앞역, STANDARD_DISTANCE);
        SectionRequest 동묘_창신 = createSectionRequest(동묘앞역, 창신역, STANDARD_DISTANCE);
        SectionRequest 창신_보문 = createSectionRequest(창신역, 보문역, STANDARD_DISTANCE + 1);

        LINE_NO_EXTRA_FARE.노선을등록하고(왕십리_상왕십리)
                .구간을등록한다(
                        상왕십리_신당,
                        신당_동묘,
                        동묘_창신,
                        창신_보문);

        PathResponse pathResponse = WANGSIMNI.부터(BOMUN).의최단거리를계산한다(DEFAULT_AGE, HttpStatus.OK.value())
                .as(PathResponse.class);

        assertThat(pathResponse.getFare()).isEqualTo(2150);
    }

    private SectionRequest createSectionRequest(Station upStation, Station downStation, int distance) {
        return new SectionRequest(upStation.getId(), downStation.getId(), distance);
    }
}
