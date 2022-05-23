package wooteco.subway.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.function.Consumer;

public class Lines implements Iterable<Line> {
    private final List<Line> lines;

    public Lines(List<Line> lines) {
        this.lines = lines;
    }

    public List<Station> getStations() {
        Set<Station> stations = new HashSet<>();

        for (Line line : lines) {
            stations.addAll(line.getStations());
        }
        return new ArrayList<>(stations);
    }

    public Map<Long, Integer> getExtraFareByIds() {
        Map<Long, Integer> extraFareById = new HashMap<>();
        for (Line line : lines) {
            extraFareById.put(line.getId(), line.getExtraFare());
        }
        return extraFareById;
    }

    private class LinesIterator implements Iterator<Line> {

        private int current = 0;

        @Override
        public boolean hasNext() {
            return current < lines.size();
        }

        @Override
        public Line next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return lines.get(current++);
        }
    }

    @Override
    public Iterator<Line> iterator() {
        return new LinesIterator();
    }

    @Override
    public void forEach(Consumer<? super Line> action) {
        Iterable.super.forEach(action);
    }
}
