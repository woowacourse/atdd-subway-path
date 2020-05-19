package wooteco.subway.admin.domain;

import org.springframework.stereotype.Component;

import wooteco.subway.admin.dto.FindPathRequest;

@Component
public class DijkstraShortestPathStrategy implements ShortestPathStrategy {
    @Override
    public SubwayPath findShortestPath(Lines lines, Stations stations, FindPathRequest findPathRequest) {
        validateSameStation(findPathRequest.getSource(), findPathRequest.getTarget());
        Station source = stations.findStationByName(findPathRequest.getSource());
        Station target = stations.findStationByName(findPathRequest.getTarget());
        PathType pathType = PathType.of(findPathRequest.getPathType());

        SubwayGraph subwayGraph = new SubwayGraph();
        subwayGraph.addVertices(stations.getStations());
        subwayGraph.addEdges(lines.getLineStations(), stations.generateStationMapper(), pathType);

        return subwayGraph.findShortestPath(source, target);
    }

    private void validateSameStation(String source, String target) {
        if (source.equals(target)) {
            throw new IllegalArgumentException("출발역과 도착역이 같습니다.");
        }
    }
}
