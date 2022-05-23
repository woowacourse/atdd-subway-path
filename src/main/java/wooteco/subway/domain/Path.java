package wooteco.subway.domain;

import java.util.List;
import java.util.stream.Collectors;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.util.StationIdParser;

public class Path {

    private Stations stations;
    private Sections sections;
    private Lines lines;
    private DijkstraShortestPath<Long, DefaultWeightedEdge> dijkstraShortestPath;

    public Path(List<Station> stations, List<Section> sections) {
        WeightedMultigraph<Long, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        setVertex(stations, graph);
        setEdgeWeight(sections, graph);
        dijkstraShortestPath = new DijkstraShortestPath(graph);
    }

    public Path(Stations stations, Sections sections, Lines lines) {
        this.stations = stations;
        this.sections = sections;
        this.lines = lines;

        WeightedMultigraph<Long, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        setVertex(stations.get(), graph);
        setEdgeWeight(sections.get(), graph);
        dijkstraShortestPath = new DijkstraShortestPath(graph);
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

    public PathResult getPath(long source, long target) {
        var shortestPath = dijkstraShortestPath.getPath(source, target);

        var stations = shortestPath.getVertexList().stream()
                .map(it -> this.stations.find(it))
                .collect(Collectors.toList());

        var distance = shortestPath.getWeight();

        var extraFare = getExtraFare(shortestPath);

        return new PathResult(stations, distance, extraFare);
    }

    private int getExtraFare(GraphPath<Long, DefaultWeightedEdge> shortestPath) {
        return shortestPath.getEdgeList().stream()
                .map(it -> StationIdParser.parse(it.toString()))
                .map(it -> this.sections.findByStationIds(it))
                .map(Section::getLineId)
                .map(lines::find)
                .map(Line::getExtraFare)
                .max(Integer::compareTo)
                .orElse(0);
    }
}
