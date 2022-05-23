package wooteco.subway.dao;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.util.ReflectionUtils;
import wooteco.subway.domain.Line;
import wooteco.subway.dao.entity.LineEntity;

public class FakeLineDao implements LineDao {

    private static Long seq = 0L;
    private static List<LineEntity> lines = new ArrayList<>();

    @Override
    public LineEntity save(LineEntity lineEntity) {
        boolean existName = lines.stream()
                .anyMatch(it -> it.getName().equals(lineEntity.getName()));
        if (existName) {
            throw new IllegalArgumentException("이미 존재하는 노선 이름입니다.");
        }
        LineEntity persistLine = createNewObject(lineEntity);
        lines.add(persistLine);
        return persistLine;
    }

    private LineEntity createNewObject(LineEntity lineEntity) {
        Field field = ReflectionUtils.findField(Line.class, "id");
        field.setAccessible(true);
        ReflectionUtils.setField(field, lineEntity, ++seq);
        return lineEntity;
    }

    @Override
    public List<LineEntity> findAll() {
        return lines;
    }

    @Override
    public LineEntity findById(Long id) {
        return lines.stream()
                    .filter(line -> line.getId().equals(id))
                    .findFirst()
                    .orElseThrow(() -> new NoSuchElementException("존재하지 않는 id입니다."));
    }

    @Override
    public LineEntity update(LineEntity updateLine) {
        lines.remove(findById(updateLine.getId()));

        LineEntity line = new LineEntity(updateLine.getId(), updateLine.getName(), updateLine.getColor());
        lines.add(line);

        return line;
    }

    @Override
    public void deleteById(Long id) {
        lines.remove(findById(id));
    }

    private boolean isSameName(Line line1, Line line2) {
        return line1.getName().equals(line2.getName());
    }
}
