package wooteco.subway.domain.path;

import java.util.List;
import java.util.stream.Collectors;
import wooteco.subway.domain.Fare;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Lines;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.Stations;
import wooteco.subway.util.Edge;
import wooteco.subway.util.GraphPathResponse;

public class Path {

    private final Stations stations;
    private final Sections sections;
    private final Lines lines;

    public Path(Stations stations,
                Sections sections,
                Lines lines
    ) {
        this.stations = stations;
        this.sections = sections;
        this.lines = lines;
    }

    public PathResult getPath(GraphPathResponse graphPathResponse, int age) {
        var stations = graphPathResponse.getVertexList().stream()
                .map(this.stations::find)
                .collect(Collectors.toList());

        var distance = graphPathResponse.getWeight();

        var extraFare = getExtraFare(graphPathResponse.getEdgeList());

        var fare = new Fare(distance, extraFare, age);

        return new PathResult(stations, distance, fare);
    }

    private int getExtraFare(List<Edge> edges) {
        return edges.stream()
                .map(it -> List.of(it.getSourceVertex(), it.getTargetVertex()))
                .map(this.sections::findByStationIds)
                .map(Section::getLineId)
                .map(lines::find)
                .map(Line::getExtraFare)
                .max(Integer::compareTo)
                .orElse(0);
    }
}
