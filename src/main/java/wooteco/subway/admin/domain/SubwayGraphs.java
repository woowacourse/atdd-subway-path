package wooteco.subway.admin.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SubwayGraphs {
    private final Map<SubwayGraphKey, SubwayGraph> subwayGraphs;

    public SubwayGraphs(Set<Edge> edges) {
        this.subwayGraphs = new HashMap<>();

        this.subwayGraphs.put(SubwayGraphKey.DISTANCE, new SubwayGraph(edges, Edge::getDistance));
        this.subwayGraphs.put(SubwayGraphKey.DURATION, new SubwayGraph(edges, Edge::getDuration));
    }

    public Integer getTotalDistance(Long source, Long target, SubwayGraphKey key) {
        SubwayGraph subwayGraph = subwayGraphs.get(key);
        SubwayPath path = subwayGraph.getPath(source, target);
        return path.sumOfEdge(Edge::getDistance);
    }

    public Integer getTotalDuration(Long source, Long target, SubwayGraphKey key) {
        SubwayGraph subwayGraph = subwayGraphs.get(key);
        SubwayPath path = subwayGraph.getPath(source, target);
        return path.sumOfEdge(Edge::getDuration);
    }

    public List<Long> getPath(Long source, Long target, SubwayGraphKey key) {
        SubwayGraph subwayGraph = subwayGraphs.get(key);
        SubwayPath path = subwayGraph.getPath(source, target);
        return path.getPaths();
    }

    public enum SubwayGraphKey {
        DISTANCE, DURATION
    }
}
