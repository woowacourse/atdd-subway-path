package wooteco.subway.utils;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import wooteco.subway.dto.request.LineRequest;
import wooteco.subway.dto.request.SectionRequest;
import wooteco.subway.dto.request.StationRequest;

import static wooteco.subway.utils.FixtureUtils.*;

public class PathFixtureUtils {

    public long 선릉역_ID;
    public long 선정릉역_ID;
    public long 한티역_ID;
    public long 모란역_ID;
    public long 기흥역_ID;
    public long 강남역_ID;
    public long 노선_1_ID;
    public long 노선_2_ID;
    public long 노선_3_ID;

    public PathFixtureUtils() {
        setUp();
    }

    public void setUp () {
        ExtractableResponse<Response> 선릉역_응답 = post(STATION, new StationRequest("선릉역"));
        ExtractableResponse<Response> 선정릉역_응답 = post(STATION, new StationRequest("선정릉역"));
        ExtractableResponse<Response> 한티역_응답 = post(STATION, new StationRequest("한티역"));
        ExtractableResponse<Response> 모란역_응답 = post(STATION, new StationRequest("모란역"));
        ExtractableResponse<Response> 기흥역_응답 = post(STATION, new StationRequest("기흥역"));
        ExtractableResponse<Response> 강남역_응답 = post(STATION, new StationRequest("강남역"));

        선릉역_ID = extractId(선릉역_응답);
        선정릉역_ID = extractId(선정릉역_응답);
        한티역_ID = extractId(한티역_응답);
        모란역_ID = extractId(모란역_응답);
        기흥역_ID = extractId(기흥역_응답);
        강남역_ID = extractId(강남역_응답);

        LineRequest 노선_1_요청 = LineRequest.builder()
                .name("노선-1")
                .color("red")
                .upStationId(선릉역_ID)
                .downStationId(선정릉역_ID)
                .distance(50)
                .build();
        ExtractableResponse<Response> 노선_1_응답 = post(LINE, 노선_1_요청);
        노선_1_ID = extractId(노선_1_응답);

        SectionRequest 노선_1_구간_1_요청 = SectionRequest.builder()
                .upStationId(선정릉역_ID)
                .downStationId(한티역_ID)
                .distance(8)
                .build();
        post(SECTION_BY_LINE_ID(노선_1_ID), 노선_1_구간_1_요청);

        SectionRequest 노선_1_구간_2_요청 = SectionRequest.builder()
                .upStationId(한티역_ID)
                .downStationId(강남역_ID)
                .distance(20)
                .build();
        post(SECTION_BY_LINE_ID(노선_1_ID), 노선_1_구간_2_요청);

        LineRequest 노선_2_요청 = LineRequest.builder()
                .name("노선-2")
                .color("green")
                .upStationId(선정릉역_ID)
                .downStationId(모란역_ID)
                .distance(6)
                .build();
        ExtractableResponse<Response> 노선_2_응답 = post(LINE, 노선_2_요청);
        노선_2_ID = extractId(노선_2_응답);

        LineRequest 노선_3_요청 = LineRequest.builder()
                .name("노선-3")
                .color("blue")
                .upStationId(기흥역_ID)
                .downStationId(모란역_ID)
                .distance(10)
                .build();
        ExtractableResponse<Response> 노선_3_응답 = post(LINE, 노선_3_요청);
        노선_3_ID = extractId(노선_3_응답);

        SectionRequest 노선_3_구간_1_요청 = SectionRequest.builder()
                .upStationId(모란역_ID)
                .downStationId(강남역_ID)
                .distance(5)
                .build();
        post(SECTION_BY_LINE_ID(노선_3_ID), 노선_3_구간_1_요청);
    }
}
