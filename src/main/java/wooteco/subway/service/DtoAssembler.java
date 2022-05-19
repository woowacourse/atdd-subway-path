package wooteco.subway.service;

import java.util.List;
import java.util.stream.Collectors;
import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.route.Route;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.station.Station;
import wooteco.subway.ui.dto.request.LineRequest;
import wooteco.subway.ui.dto.response.LineResponse;
import wooteco.subway.ui.dto.response.RouteResponse;
import wooteco.subway.ui.dto.response.StationResponse;

public class DtoAssembler {

    public DtoAssembler() {
    }

    public static Line line(Section section, LineRequest lineRequest) {
        return new Line(List.of(section), lineRequest.getName(), lineRequest.getColor());
    }

    public static List<LineResponse> lineResponses(List<Line> lines) {
        return lines.stream()
                .map(DtoAssembler::lineResponse)
                .collect(Collectors.toUnmodifiableList());
    }

    public static LineResponse lineResponse(Line line) {
        return new LineResponse(line.getId(), line.getName(), line.getColor(), stationResponses(line.getStations()));
    }

    public static List<StationResponse> stationResponses(List<Station> stations) {
        return stations.stream()
                .map(DtoAssembler::stationResponse)
                .collect(Collectors.toUnmodifiableList());
    }

    public static StationResponse stationResponse(Station station) {
        return new StationResponse(station.getId(), station.getName());
    }

    public static RouteResponse routeResponse(Route route, Long fare) {
        return new RouteResponse(stationResponses(route.getRoute()), route.getDistance(), fare);
    }
}
