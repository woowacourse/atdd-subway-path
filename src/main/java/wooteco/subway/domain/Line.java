package wooteco.subway.domain;

import java.util.LinkedList;
import java.util.List;

public class Line {

    private static final long TEMPORARY_ID = 0L;

    private final Long id;
    private final Name name;
    private final String color;
    private final int extraFare;
    private final Sections sections;

    public Line(Long id, String name, String color, int extraFare, List<Section> sections) {
        this.id = id;
        this.name = new Name(name);
        this.extraFare = extraFare;
        this.color = color;
        this.sections = new Sections(sections);
    }

    public Line(Long id, String name, String color, int extraFare) {
        this(id, name, color, extraFare, new LinkedList<>());
    }

    public Line(String name, String color, int extraFare, List<Section> sections) {
        this(TEMPORARY_ID, name, color, extraFare, sections);
    }

    public Line createWithSection(List<Section> sections) {
        return new Line(id, name.getValue(), color, extraFare, sections);
    }

    public boolean isSameName(String name) {
        return this.name.isSame(name);
    }

    public boolean isSameId(Long id) {
        return this.id.equals(id);
    }

    public void addSection(Section section) {
        sections.add(section);
    }

    public int sizeOfSection() {
        return sections.size();
    }

    public void deleteSectionByStation(Long stationId) {
        validateSectionSize();
        sections.deleteByStation(stationId);
    }

    private void validateSectionSize() {
        if (sizeOfSection() == 1) {
            throw new IllegalArgumentException("구간이 하나일 땐 삭제할 수 없습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getValue();
    }

    public String getColor() {
        return color;
    }

    public List<Station> findOrderedStations() {
        return sections.sortStations();
    }

    public List<Section> getSections() {
        return sections.getValues();
    }

    public int getExtraFare() {
        return extraFare;
    }
}
