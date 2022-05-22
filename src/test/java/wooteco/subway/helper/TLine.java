package wooteco.subway.helper;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;
import wooteco.subway.dto.LineRequest;
import wooteco.subway.dto.LineResponse;
import wooteco.subway.dto.SectionRequest;

public enum TLine {

    LINE_SIX("6호선", 200),
    LINE_TWO("2호선", 100),
    LINE_NO_EXTRA_FARE("후니선", 0);

    private final String name;
    private final String color;
    private final int extraFare;

    private Long id;

    TLine(String name, int extraFare) {
        this.name = name;
        this.color = "bg-red-500";
        this.extraFare = extraFare;
    }

    public LineResponse 노선을등록한다(SectionRequest sectionRequest) {
        LineResponse response = request(createLineRequest(sectionRequest));
        id = response.getId();
        return response;
    }

    public ExtractableResponse<Response> 노선을등록한다(SectionRequest sectionRequest, int status) {
        ExtractableResponse<Response> response = requestLine(createLineRequest(sectionRequest));

        assertThat(response.statusCode()).isEqualTo(status);
        return response;
    }

    private LineResponse request(final LineRequest lineRequest) {
        return requestLine(lineRequest).as(LineResponse.class);
    }

    private ExtractableResponse<Response> requestLine(final LineRequest lineRequest) {
        return RestAssured.given().log().all()
                .body(lineRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then().log().all()
                .extract();
    }

    public LineAddAndRequest 노선을등록하고(SectionRequest sectionRequest) {
        return new LineAddAndRequest(this,
                sectionRequest);
    }

    private LineRequest createLineRequest(SectionRequest sectionRequest) {
        return new LineRequest(name,
                color,
                extraFare,
                sectionRequest.getUpStationId(),
                sectionRequest.getDownStationId(),
                sectionRequest.getDistance());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public int getExtraFare() {
        return extraFare;
    }
}
