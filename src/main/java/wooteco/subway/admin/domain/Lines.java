package wooteco.subway.admin.domain;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Lines implements Iterable<Line> {
    private final List<Line> lines;

    public Lines(final List<Line> lines) {
        this.lines = lines;
    }

    public List<Long> getStationIds() {
        return lines.stream()
                .map(Line::getEdges)
                .flatMap(Collection::stream)
                .map(Edge::getStationId)
                .collect(Collectors.toList());
    }

    public Lines findByStationIdIn(final List<Long> stationIds) {
        return lines.stream()
                .filter(line -> line.containsAll(stationIds))
                .collect(Collectors.collectingAndThen(Collectors.toList(), Lines::new));
    }

    public SubwayGraph getShortestGraph(Long sourceStationId, Long targetStationId) {
        SubwayGraphs subwayGraphs = lines.stream()
                .map(line -> new SubwayGraph(line.getEdges()))
                .collect(Collectors.collectingAndThen(Collectors.toList(), SubwayGraphs::new));
        return subwayGraphs.getShortestPath(sourceStationId, targetStationId);
    }

    @Override
    public void forEach(final Consumer<? super Line> action) {
        lines.forEach(action);
    }

    @Override
    public Spliterator<Line> spliterator() {
        return lines.spliterator();
    }

    @Override
    public Iterator<Line> iterator() {
        return lines.iterator();
    }

}
