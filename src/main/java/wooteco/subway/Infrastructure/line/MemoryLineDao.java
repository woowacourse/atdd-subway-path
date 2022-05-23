package wooteco.subway.Infrastructure.line;

import wooteco.subway.domain.Line;
import wooteco.subway.domain.Station;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class MemoryLineDao implements LineDao {

    private List<Line> lines = new ArrayList<>();

    private AtomicLong sequence = new AtomicLong();

    @Override
    public long save(Line line) {
        lines.add(new Line(sequence.incrementAndGet(), line.getName(), line.getColor(), line.getExtraFare()));
        return sequence.get();
    }

    @Override
    public List<Line> findAll() {
        return Collections.unmodifiableList(lines);
    }

    @Override
    public List<Line> findByIdIn(Collection<Long> lineIds) {
        return this.lines.stream()
                .filter(it -> lineIds.contains(it.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Line> findById(Long id) {
        return lines.stream()
                .filter(line -> line.getId().equals(id))
                .findAny();
    }

    @Override
    public boolean existById(Long id) {
        return lines.stream()
                .anyMatch(line -> line.getId().equals(id));
    }

    @Override
    public boolean existByName(String name) {
        return lines.stream()
                .anyMatch(line -> line.getName().equals(name));
    }

    @Override
    public boolean existByColor(String color) {
        return lines.stream()
                .anyMatch(line -> line.getColor().equals(color));
    }

    @Override
    public void update(Line line) {
        Line found = findById(line.getId())
                .orElse(null);
        if (found == null) {
            return;
        }
        lines.remove(found);
        lines.add(line);
    }

    @Override
    public void deleteById(Long id) {
        Line found = findById(id)
                .orElse(null);
        if (found == null) {
            return;
        }
        lines.remove(found);
    }

    @Override
    public void deleteAll() {
        lines.clear();
    }
}
