package wooteco.subway.admin.domain;

import java.util.Map;
import java.util.Set;

public class SubwayGraphs {
    private final Map<SubwayGraphKey, SubwayGraph> subwayGraphs;

    public SubwayGraphs(Set<Edge> edges) {
        this.subwayGraphs = SubwayGraphKey.makeGraph(edges);
    }

    public PathDetail getPath(Long source, Long target, SubwayGraphKey key) {
        SubwayGraph subwayGraph = subwayGraphs.get(key);
        SubwayPath path = subwayGraph.getPath(source, target);
        PathCost pathCost = path.getCost();
        return new PathDetail(path.getPaths(), pathCost.getDistance(), pathCost.getDuration());
    }
}
