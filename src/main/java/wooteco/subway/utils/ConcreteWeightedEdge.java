package wooteco.subway.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jgrapht.graph.DefaultWeightedEdge;

@Getter
@AllArgsConstructor
public class ConcreteWeightedEdge<V> extends DefaultWeightedEdge {

    private final V source;
    private final V target;
}
