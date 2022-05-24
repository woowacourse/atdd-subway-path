package wooteco.subway.domain.path;

import java.util.List;
import java.util.stream.Collectors;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.domain.Fare;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Lines;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.Station;
import wooteco.subway.domain.Stations;
import wooteco.subway.util.StationIdParser;

public class SubwayPathFinder implements PathFinder {

    private final Stations stations;
    private final Sections sections;
    private final Lines lines;
    private final DijkstraShortestPath<Long, DefaultWeightedEdge> graph;

    public SubwayPathFinder(Stations stations,
                            Sections sections,
                            Lines lines
    ) {
        this.stations = stations;
        this.sections = sections;
        this.lines = lines;

        WeightedMultigraph<Long, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        setVertex(stations.get(), graph);
        setEdgeWeight(sections.get(), graph);
        this.graph = new DijkstraShortestPath(graph);
    }

    private void setVertex(List<Station> stations, WeightedMultigraph<Long, DefaultWeightedEdge> graph) {
        for (var station : stations) {
            graph.addVertex(station.getId());
        }
    }

    private void setEdgeWeight(List<Section> sections, WeightedMultigraph<Long, DefaultWeightedEdge> graph) {
        for (var section : sections) {
            var upStationId = section.getUpStationId();
            var downStationId = section.getDownStationId();
            var distance = section.getDistance();
            graph.setEdgeWeight(graph.addEdge(upStationId, downStationId), distance);
        }
    }

    @Override
    public PathResult getPath(Long source, Long target, int age) {
        var shortestPath = graph.getPath(source, target);

        var stations = shortestPath.getVertexList().stream()
                .map(this.stations::find)
                .collect(Collectors.toList());

        var distance = shortestPath.getWeight();

        var extraFare = getExtraFare(shortestPath);

        var fare = new Fare(distance, extraFare, age);

        return new PathResult(stations, distance, fare);
    }

    private int getExtraFare(GraphPath<Long, DefaultWeightedEdge> shortestPath) {
        return shortestPath.getEdgeList().stream()
                .map(it -> StationIdParser.parse(it.toString()))
                .map(this.sections::findByStationIds)
                .map(Section::getLineId)
                .map(lines::find)
                .map(Line::getExtraFare)
                .max(Integer::compareTo)
                .orElse(0);
    }
}
