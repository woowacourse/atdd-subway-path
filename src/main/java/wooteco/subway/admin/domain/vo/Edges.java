package wooteco.subway.admin.domain.vo;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.relational.core.mapping.MappedCollection;
import wooteco.subway.admin.domain.Edge;

public class Edges {

    @MappedCollection(keyColumn = "line_key")
    private LinkedList<Edge> edges = new LinkedList<>();

    public Edges() {
    }

    public Edges(LinkedList<Edge> edges) {
        this.edges = edges;
    }

    public void addEdge(Edge edge) {
        if (edge.isFirst()) {
            addFirst(edge);
            return;
        }

        if (hasNoSuchPreStation(edge)) {
            throw new NoSuchElementException("이전 역이 등록되지 않았습니다.");
        }

        Optional<Edge> nextStation = findNextStationById(edge.getPreStationId());
        if (nextStation.isPresent()) {
            addBetweenTwo(edge, nextStation.get());
            return;
        }

        edges.add(edge);
    }

    private void addFirst(Edge edge) {
        edges.stream()
            .findFirst()
            .ifPresent(station -> station.updatePreStation(edge.getStationId()));
        edges.addFirst(edge);
    }

    private void addBetweenTwo(Edge edge, Edge nextStation) {
        nextStation.updatePreStation(edge.getStationId());
        int position = edges.indexOf(nextStation);
        edges.add(position, edge);
    }

    private boolean hasNoSuchPreStation(Edge edge) {
        return edges.stream()
            .map(Edge::getStationId)
            .noneMatch(id -> edge.getPreStationId().equals(id));
    }

    public void removeEdgeById(Long stationId) {
        Edge station = findStationById(stationId);
        findNextStationById(stationId)
            .ifPresent(nextStation -> nextStation.updatePreStation(station.getPreStationId()));
        edges.remove(station);
    }

    private Edge findStationById(Long stationId) {
        return edges.stream()
            .filter(Edge -> Edge.getStationId().equals(stationId))
            .findFirst()
            .orElseThrow(() -> new NoSuchElementException("해당 노선에 등록되지 않은 역입니다."));
    }

    private Optional<Edge> findNextStationById(Long stationId) {
        return edges.stream()
            .filter(station -> stationId.equals(station.getPreStationId()))
            .findFirst();
    }

    public List<Long> getStationIds() {
        return edges.stream()
            .map(Edge::getStationId)
            .collect(Collectors.toList());
    }

    public LinkedList<Edge> getEdges() {
        return edges;
    }
}
