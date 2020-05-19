package wooteco.subway.admin.domain.line.path;

@FunctionalInterface
public interface EdgeWeightStrategy {
    int getWeight(RouteEdge edge);
}
