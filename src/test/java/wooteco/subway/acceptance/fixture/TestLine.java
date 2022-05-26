package wooteco.subway.acceptance.fixture;

import static wooteco.subway.acceptance.fixture.HttpRequestUtil.post;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.request.LineCreateRequest;

public enum TestLine {

    LINE_2("2호선", "초록색", 0),
    LINE_9("9호선", "금색", 0),
    SUIN_BUNDANG_LINE("수인분당선", "노란색", 0),
    EXPENSIVE_LINE("비싼노선", "빨간색", 500),
    MORE_EXPENSIVE_LINE("더비싼노선", "검은색", 900);

    private final String name;
    private final String color;
    private final int extraFare;

    TestLine(String name, String color, int extraFare) {
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
    }

    public Line save(Station upStation, Station downStation, int distance) {
        Long id = requestSave(upStation, downStation, distance).jsonPath()
                .getLong("id");
        return new Line(id, name, color, extraFare);
    }

    public ExtractableResponse<Response> requestSave(Station upStation, Station downStation, int distance) {
        LineCreateRequest request = new LineCreateRequest(name, color, upStation.getId(), downStation.getId(), distance,
                extraFare);
        return post(request, "/lines");
    }

}
