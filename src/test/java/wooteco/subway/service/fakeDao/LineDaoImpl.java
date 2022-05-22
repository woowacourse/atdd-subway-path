package wooteco.subway.service.fakeDao;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.springframework.util.ReflectionUtils;
import wooteco.subway.dao.LineDao;
import wooteco.subway.domain.Line;
import wooteco.subway.exception.NotFoundLineException;

public class LineDaoImpl implements LineDao {

    private static final LineDaoImpl stationDao = new LineDaoImpl();

    private static Long seq = 0L;
    private static final List<Line> lines = new ArrayList<>();

    public static LineDaoImpl getInstance() {
        return stationDao;
    }

    @Override
    public Line save(Line line) {
        Line persistLine = createNewObject(line);
        if (hasLine(persistLine.getName())) {
            throw new IllegalArgumentException("같은 이름의 노선이 존재합니다.");
        }
        lines.add(persistLine);
        return persistLine;
    }

    @Override
    public Optional<Line> findById(Long id) {
        return lines.stream()
                .filter(line -> line.getId() == id)
                .findFirst();
    }

    @Override
    public List<Line> findAll() {
        return lines;
    }

    @Override
    public boolean hasLine(String name) {
        return lines.stream()
                .anyMatch(line -> name.equals(line.getName()));
    }

    @Override
    public void updateById(Long id, String name, String color) {
        Line line = findById(id).
                orElseThrow(() -> new NotFoundLineException("해당하는 노선이 존재하지 않습니다."));
        line.setName(name);
        line.setColor(color);
    }

    @Override
    public void deleteById(Long id) {
        boolean result = lines.removeIf(line -> line.getId() == id);
        if (!result) {
            throw new NoSuchElementException("해당하는 노선이 존재하지 않습니다.");
        }
    }

    private Line createNewObject(Line line) {
        Field field = ReflectionUtils.findField(Line.class, "id");
        field.setAccessible(true);
        ReflectionUtils.setField(field, line, ++seq);
        return line;
    }
}
