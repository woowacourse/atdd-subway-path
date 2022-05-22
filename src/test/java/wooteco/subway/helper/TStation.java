package wooteco.subway.helper;

import io.restassured.RestAssured;
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

    private static StationResponse requestStation(final StationRequest stationRequest) {
        return RestAssured.given().log().all()
                .body(stationRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .extract()
                .as(StationResponse.class);
    }

    public Station 역을등록한다() {
        StationResponse stationResponse = requestStation(new StationRequest(name));
        this.id = stationResponse.getId();
        return new Station(id, stationResponse.getName());
    }

    public PathAndRequest 부터(TStation target) {
        return new PathAndRequest(this, target);
    }

    public Long getId() {
        return id;
    }
}
