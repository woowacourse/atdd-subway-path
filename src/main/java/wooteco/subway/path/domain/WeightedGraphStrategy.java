package wooteco.subway.path.domain;

import org.jgrapht.WeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.station.domain.Station;

public enum WeightedGraphStrategy {
    MULTI {
        @Override
        public WeightedGraph<Station, DefaultWeightedEdge> match() {
            return new WeightedMultigraph<>(DefaultWeightedEdge.class);
        }
    };

    public abstract WeightedGraph<Station, DefaultWeightedEdge> match();
}