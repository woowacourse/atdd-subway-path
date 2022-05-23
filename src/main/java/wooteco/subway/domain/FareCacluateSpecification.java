package wooteco.subway.domain;

import java.util.List;

public class FareCacluateSpecification {

    private int age;
    private List<Section> sections;

    public FareCacluateSpecification(int age, List<Section> sections) {
        this.age = age;
        this.sections = sections;
    }

    public int getAge() {
        return age;
    }

    public List<Section> getSections() {
        return sections;
    }
}
