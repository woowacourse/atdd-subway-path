package wooteco.subway.admin.domain;

import java.util.ArrayList;
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

    public Object getShortestPath() {
        List<SubwayPath> subwayPaths = new ArrayList<>();
        for (Line line : lines) {
            subwayPaths.add(new SubwayPath(line));
        }

        return null;
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
