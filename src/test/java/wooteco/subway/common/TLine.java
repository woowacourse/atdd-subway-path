package wooteco.subway.common;

import io.restassured.RestAssured;
import org.springframework.http.MediaType;
import wooteco.subway.dto.LineRequest;
import wooteco.subway.dto.LineResponse;
import wooteco.subway.dto.SectionRequest;

public enum TLine {

    LINE_SIX("6호선", 200),
    LINE_TWO("2호선", 100),
    LINE_NO_EXTRA("후니선", 0);

    private final String name;
    private final String color;
    private final int extraFare;

    TLine(String name, int extraFare) {
        this.name = name;
        this.color = "bg-red-500";
        this.extraFare = extraFare;
    }

    public LineResponse 노선을등록한다(SectionRequest sectionRequest) {
        LineRequest lineRequest = new LineRequest(name,
                color,
                extraFare,
                sectionRequest.getUpStationId(),
                sectionRequest.getDownStationId(),
                sectionRequest.getDistance());
        return requestLine(lineRequest);
    }

    public LineAddAndRequest 노선을등록하고(SectionRequest sectionRequest) {
        return new LineAddAndRequest(this,
                sectionRequest.getUpStationId(),
                sectionRequest.getDownStationId(),
                sectionRequest.getDistance());
    }

    private static LineResponse requestLine(final LineRequest lineRequest) {
        return RestAssured.given().log().all()
                .body(lineRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then().log().all()
                .extract()
                .as(LineResponse.class);
    }

    public int getExtraFare() {
        return extraFare;
    }
}
