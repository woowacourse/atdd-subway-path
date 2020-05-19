package wooteco.subway.admin.domain.line.path;

public enum EdgeWeightType implements EdgeWeightStrategy {
    DISTANCE {
        @Override
        public int getWeight(RouteEdge edge) {
            return edge.getDistance();
        }
    },
    DURATION {
        @Override
        public int getWeight(RouteEdge edge) {
            return edge.getDuration();
        }
    }
}
