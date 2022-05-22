package wooteco.subway.helper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.StationRequest;
import wooteco.subway.dto.StationResponse;

public enum TStation {

    SINDANG("신당역"),
    DONGMYO("동묘앞역"),
    CHANGSIN("창신역"),
    BOMUN("보문역"),
    SANGWANGSIMNI("상왕십리역"),
    WANGSIMNI("왕십리역");

    private final String name;
    private Long id;

    TStation(String name) {
        this.name = name;
    }

    public Station 역을등록한다() {
        StationResponse stationResponse = request(new StationRequest(name));
        this.id = stationResponse.getId();
        return new Station(id, stationResponse.getName());
    }

    public ExtractableResponse<Response> 역을등록한다(int status) {
        ExtractableResponse<Response> response = requestStation(new StationRequest(name));
        assertThat(response.statusCode()).isEqualTo(status);
        return response;
    }

    private StationResponse request(final StationRequest stationRequest) {
        return requestStation(stationRequest).as(StationResponse.class);
    }

    private ExtractableResponse<Response> requestStation(final StationRequest stationRequest) {
        return RestAssured.given().log().all()
                .body(stationRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .extract();
    }

    public StationAddAndRequest 역을등록하고() {
        return new StationAddAndRequest(this);
    }

    public StationResponse 역() {
        return new StationResponse(id, name);
    }

    public PathAndRequest 부터(TStation target) {
        return new PathAndRequest(this, target);
    }

    public Long getId() {
        return id;
    }
}
