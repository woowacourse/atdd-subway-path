package wooteco.subway.admin.domain.path;

import org.jgrapht.graph.DefaultWeightedEdge;
import wooteco.subway.admin.domain.LineStation;

public class WeightedEdge extends DefaultWeightedEdge {
    private int subWeight = 0;
    private final PathType pathType;

    public WeightedEdge(LineStation lineStation, PathType pathType) {
        super();
        this.pathType = pathType;
        this.subWeight = pathType.findSubWeight(lineStation);
    }

    public int getSubWeight() {
        return subWeight;
    }
}
