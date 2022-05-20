package wooteco.subway.service.fakeDao;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.util.ReflectionUtils;
import wooteco.subway.dao.LineDao;
import wooteco.subway.domain.Line;
import wooteco.subway.dto.LineRequest;

public class LineDaoImpl implements LineDao {
    private static final LineDaoImpl lineDao = new LineDaoImpl();
    private static final List<Line> lines = new ArrayList<>();
    private static Long seq = 0L;

    public static LineDaoImpl getInstance() {
        return lineDao;
    }

    @Override
    public Long save(LineRequest lineRequest) {
        final Line line = new Line(0L, lineRequest.getName(), lineRequest.getColor(), 900);
        Line persistLine = createNewObject(line);
        if (hasLine(persistLine.getName())) {
            throw new IllegalArgumentException("같은 이름의 노선이 존재합니다.");
        }
        lines.add(persistLine);
        return persistLine.getId();
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
    public void updateById(Long id, String name, String color, int extraFare) {
        Line line = findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 노선이 존재하지 않습니다."));
        lines.remove(line);
        lines.add(new Line(id, name, color, extraFare));
    }

    @Override
    public void deleteById(Long id) {
        boolean result = lines.removeIf(line -> line.getId() == id);
        if (!result) {
            throw new IllegalArgumentException("해당하는 노선이 존재하지 않습니다.");
        }
    }

    private Line createNewObject(Line line) {
        Field field = ReflectionUtils.findField(Line.class, "id");
        field.setAccessible(true);
        ReflectionUtils.setField(field, line, ++seq);
        return line;
    }
}