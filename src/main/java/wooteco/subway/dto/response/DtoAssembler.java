package wooteco.subway.dto.response;

import java.util.List;
import java.util.stream.Collectors;
import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.line.LineInfo;
import wooteco.subway.domain.path.Path;
import wooteco.subway.domain.station.Station;

public class DtoAssembler {

    private DtoAssembler() {
    }

    public static LineResponse assemble(Line line) {
        LineInfo lineInfo = line.getLineInfo();
        Long lineId = lineInfo.getId();
        String lineName = lineInfo.getName();
        String lineColor = lineInfo.getColor();
        List<StationResponse> stations = toStationResponse(line.getSortedStations());
        return new LineResponse(lineId, lineName, lineColor, stations);
    }

    public static PathResponse assemble(Path path){
        List<StationResponse> stations = toStationResponse(path.getStations());
        int distance = path.getTotalDistance();
        int fare = path.calculateFare();
        return new PathResponse(stations, distance, fare);
    }

    private static List<StationResponse> toStationResponse(List<Station> stations) {
        return stations.stream()
                .map(station -> new StationResponse(station.getId(), station.getName()))
                .collect(Collectors.toUnmodifiableList());
    }
}
