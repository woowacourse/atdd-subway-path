package wooteco.subway.admin.domain;

import java.util.List;

public class Path {
    private Stations path;
    private int totalWeight;
    private int totalInformation;
    private PathType pathType;

    public Path(Stations path, int totalWeight, int totalInformation, PathType pathType) {
        this.path = path;
        this.totalWeight = totalWeight;
        this.totalInformation = totalInformation;
        this.pathType = pathType;
    }

    private Path() {
    }

    public static Path makePath(Lines lines, Stations stations, PathType type,
                                Long sourceId, Long targetId) {
        LineStations lineStations = lines.makeLineStation();
        SubwayMap subwayMap = SubwayGraph.makeGraph(type, stations, lineStations);
        List<Long> shortestPath = subwayMap.findShortestPath(sourceId, targetId);

        Stations pathStations = stations.makePathStations(shortestPath);

        int weight = subwayMap.getPathWeight(sourceId, targetId);
        int information = lineStations.getInformation(shortestPath, type);

        return new Path(pathStations, weight, information, type);
    }

    public Stations getPath() {
        return path;
    }

    public List<Station> getPathStations() {
        return path.getStations();
    }

    public int getDistance() {
        if (this.pathType.equals(PathType.DISTANCE)) {
            return totalWeight;
        }
        return totalInformation;
    }

    public int getDuration() {
        if (this.pathType.equals(PathType.DURATION)) {
            return totalWeight;
        }
        return totalInformation;
    }
}
