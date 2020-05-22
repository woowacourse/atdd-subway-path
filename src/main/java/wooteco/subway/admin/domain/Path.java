package wooteco.subway.admin.domain;

import java.util.List;
import java.util.stream.Collectors;

public class Path {
    private Stations path;
    private int totalWeight;
    private int totalInformation;

    public Path(Stations path, int totalWeight, int totalInformation) {
        this.path = path;
        this.totalWeight = totalWeight;
        this.totalInformation = totalInformation;
    }

    private Path() {
    }

    public static Path makePath(Lines lines, Stations stations, PathType type,
                                Long sourceId, Long targetId) {
        LineStations lineStations = lines.makeLineStation();
        SubwayMap subwayMap = SubwayGraph.makeGraph(type, stations, lineStations);
        List<Long> shortestPath = subwayMap.findShortestPath(sourceId, targetId);

        Stations pathStations = shortestPath.stream()
                .map(stations::findStation)
                .collect(Collectors.collectingAndThen(Collectors.toList(), Stations::new));

        int weight = subwayMap.getPathWeight(sourceId, targetId);
        int information = lineStations.getInformation(shortestPath, type);

        return new Path(pathStations, weight, information);
    }

    public Stations getPath() {
        return path;
    }

    public int getTotalInformation() {
        return totalInformation;
    }

    public int getTotalWeight() {
        return totalWeight;
    }
}
