package wooteco.subway.admin.domain.graph;

import wooteco.subway.admin.domain.Edge;

import java.util.Map;
import java.util.Set;

public class SubwayGraphs {
    private final Map<SubwayGraphKey, Graph> subwayGraphs;

    public SubwayGraphs(Set<Edge> edges, GraphStrategy graphStrategy) {
        this.subwayGraphs = SubwayGraphKey.makeGraph(edges, graphStrategy);
    }

    public PathDetail getPath(Long source, Long target, SubwayGraphKey key) {
        Graph subwayGraph = subwayGraphs.get(key);
        SubwayPath path = subwayGraph.getPath(source, target);
        PathCost pathCost = path.getCost();
        return new PathDetail(path.getPaths(), pathCost.getDistance(), pathCost.getDuration());
    }
}
