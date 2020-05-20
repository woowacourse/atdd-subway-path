package wooteco.subway.admin.domain;

import org.jgrapht.graph.DefaultWeightedEdge;

public class CustomEdge extends DefaultWeightedEdge {
    private final LineStation lineStation;
    private CriteriaType criteriaType;

    public CustomEdge(LineStation lineStation, CriteriaType criteriaType) {
        this.lineStation = lineStation;
        this.criteriaType = criteriaType;
    }

    public LineStation getLineStation() {
        return lineStation;
    }

    @Override
    protected double getWeight() {
        return criteriaType.get(lineStation);
    }
}
