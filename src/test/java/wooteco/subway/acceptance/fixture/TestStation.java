package wooteco.subway.acceptance.fixture;

import static wooteco.subway.acceptance.fixture.HttpRequestUtil.post;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.request.StationRequest;

public enum TestStation {

    GANGNAM("강남역"),
    YEOKSAM("역삼역"),
    SEOLLEUNG("선릉역"),
    SPORTS_COMPLEX("종합운동장역"),
    SEONJEONGNEUNG("선정릉역"),
    SAMJEON("삼전역");

    private final String name;

    TestStation(String name) {
        this.name = name;
    }

    public Station save() {
        Long id = requestSave().jsonPath().getLong("id");
        return new Station(id, name);
    }

    public ExtractableResponse<Response> requestSave() {
        return post(new StationRequest(name), "/stations");
    }

}
