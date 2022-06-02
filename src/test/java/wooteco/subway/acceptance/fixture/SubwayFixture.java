package wooteco.subway.acceptance.fixture;

import wooteco.subway.dto.request.LineCreateRequest;
import wooteco.subway.dto.request.SectionRequest;
import wooteco.subway.dto.request.StationRequest;
import wooteco.subway.dto.response.StationResponse;

public class SubwayFixture {
    public static SimpleResponse createStation(StationRequest stationRequest) {
        return SimpleRestAssured.post("/stations", stationRequest);
    }

    public static SimpleResponse createSection(StationResponse upStation, StationResponse downStation, Long lineId) {
        SectionRequest sectionRequest = new SectionRequest(upStation.getId(), downStation.getId(), 7);
        return SimpleRestAssured.post("/lines/" + lineId + "/sections", sectionRequest);
    }

    public static SimpleResponse createLine(StationResponse upStation, StationResponse downStation) {
        LineCreateRequest lineCreateRequest =
                new LineCreateRequest(
                        "신분당선",
                        "bg-red-600",
                        upStation.getId(),
                        downStation.getId(),
                        10,
                        900);
        return SimpleRestAssured.post("/lines", lineCreateRequest);
    }
}
