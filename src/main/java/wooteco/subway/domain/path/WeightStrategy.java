package wooteco.subway.domain.path;

public interface WeightStrategy {
	Double getWeight(StationWeightEdge e);
}
