package wooteco.subway.admin.domain;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class Edges {
    private final Set<Edge> edges;

    public Edges(final Set<Edge> edges) {
        this.edges = edges;
    }

    public List<Long> getStationIds() {
        return edges.stream()
                .map(Edge::getStationId)
                .collect(Collectors.toList());
    }

    public boolean containsStationIdAll(final List<Long> stationIds) {
        boolean contain = true;
        for (Long stationId : stationIds) {
            contain = contain && containStationId(stationId);
        }
        return contain;
    }

    private boolean containStationId(Long stationId) {
        return edges.stream()
                .anyMatch(edge -> edge.isSameStationId(stationId));
    }

	private Optional<Edge> findByStationId(Long stationId) {
		return edges.stream()
				.filter(it -> Objects.equals(it.getStationId(), stationId))
				.findFirst();
	}

	public Optional<Edge> findByPreStationId(Long preStationId) {
		return edges.stream()
				.filter(it -> Objects.equals(it.getPreStationId(), preStationId))
				.findFirst();
	}

    public void add(Edge edge) {
        findByPreStationId(edge.getPreStationId())
		        .ifPresent(it -> it.updatePreLineStation(edge.getStationId()));;
        edges.add(edge);
    }

	public void remove(Long stationId) {
    	Edge targetEdge = findByStationId(stationId)
			    .orElseThrow(NoSuchElementException::new);

    	findByPreStationId(stationId)
				.ifPresent(it -> it.updatePreLineStation(targetEdge.getPreStationId()));

		edges.remove(targetEdge);
	}

	public int size() {
		return edges.size();
	}

	public Set<Edge> getEdges() {
		return edges;
	}
}
