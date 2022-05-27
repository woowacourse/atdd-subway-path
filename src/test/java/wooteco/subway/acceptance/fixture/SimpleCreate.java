package wooteco.subway.acceptance.fixture;

import javax.print.DocFlavor;

import wooteco.subway.dto.request.LineCreateRequest;
import wooteco.subway.dto.request.SectionRequest;
import wooteco.subway.dto.request.StationRequest;
import wooteco.subway.dto.response.StationResponse;

public class SimpleCreate {
    public static SimpleResponse createStation(StationRequest stationRequest) {
        return SimpleRestAssured.post("/stations", stationRequest);
    }

    public static SimpleResponse createSection(StationResponse upStation, StationResponse downStation) {
        SectionRequest sectionRequest = new SectionRequest(upStation.getId(), downStation.getId(), 7);
        return SimpleRestAssured.post("/lines/1/sections", sectionRequest);
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
