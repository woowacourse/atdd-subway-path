package wooteco.subway.domain;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FareCacluateSpecification {

    private int age;
    private List<Section> sections;
    private Map<Long, Line> lineById;

    public FareCacluateSpecification(int age, List<Section> sections, List<Line> lines) {
        this.age = age;
        this.sections = sections;
        lineById = lines.stream()
                .collect(Collectors.toMap(Line::getId, it -> it));
    }

    public int getAge() {
        return age;
    }

    public List<Section> getSections() {
        return sections;
    }

    public Map<Long, Line> getLineById() {
        return lineById;
    }
}
