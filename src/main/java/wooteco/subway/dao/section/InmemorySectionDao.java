package wooteco.subway.dao.section;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.util.ReflectionUtils;
import wooteco.subway.domain.section.Section;

public class InmemorySectionDao implements SectionDao {

    private static InmemorySectionDao INSTANCE;
    private final Map<Long, Section> sections = new HashMap<>();
    private Long seq = 0L;

    private InmemorySectionDao() {
    }

    public static synchronized InmemorySectionDao getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new InmemorySectionDao();
        }
        return INSTANCE;
    }

    public void clear() {
        sections.clear();
    }

    @Override
    public long save(final Section section) {
        Section persistSection = createNewObject(section);
        sections.put(persistSection.getId(), persistSection);
        return persistSection.getId();
    }

    private Section createNewObject(Section section) {
        Field field = ReflectionUtils.findField(Section.class, "id");
        field.setAccessible(true);
        ReflectionUtils.setField(field, section, ++seq);
        return section;
    }

    @Override
    public List<Section> findAllByLineId(final long lineId) {
        return sections.values()
                .stream()
                .filter(section -> section.getLine().getId() == lineId)
                .collect(Collectors.toList());
    }

    @Override
    public List<Section> findAll() {
        return new ArrayList<>(sections.values());
    }

    @Override
    public int updateSections(final List<Section> sections) {
        clear();
        this.sections.putAll(sections.stream()
                .collect(Collectors.toMap(Section::getId, section -> section)));
        return sections.size();
    }

    @Override
    public int delete(final long sectionId) {
        sections.remove(sectionId);
        return 1;
    }
}
