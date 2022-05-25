package wooteco.subway.domain.line;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import wooteco.subway.domain.Name;
import wooteco.subway.domain.line.section.Section;
import wooteco.subway.domain.line.section.Sections;
import wooteco.subway.domain.Station;

public class Line {

	private static final long TEMPORARY_ID = 0L;
	private static final int DEFAULT_FARE = 0;

	private final Long id;
	private final Name name;
	private final String color;
	private final int extraFare;
	private final Sections sections;

	public Line(Long id, String name, String color) {
		this(id, name, color, DEFAULT_FARE, new LinkedList<>());
	}

	public Line(Long id, String name, String color, int extraFare) {
		this(id, name, color, extraFare, new LinkedList<>());
	}

	public Line(String name, String color, int extraFare, List<Section> sections) {
		this(TEMPORARY_ID, name, color, extraFare, sections);
	}

	public Line(Long id, String name, String color, List<Section> sections) {
		this(id, name, color, DEFAULT_FARE,  sections);
	}

	public Line(Long id, String name, String color, int extraFare, List<Section> sections) {
		validateFareAmount(extraFare);
		this.id = id;
		this.name = new Name(name);
		this.color = color;
		this.extraFare = extraFare;
		this.sections = new Sections(sections);
	}

	private void validateFareAmount(int extraFare) {
		if (extraFare < DEFAULT_FARE) {
			throw new IllegalArgumentException("추가 요금은 0언 이상이어야 합니다.");
		}
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

	public boolean containsSection(Section section) {
		return sections.contains(section);
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

	public int getExtraFare() {
		return extraFare;
	}

	public List<Section> getSections() {
		return sections.getValues();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Line line = (Line)o;
		return Objects.equals(getId(), line.getId()) && Objects.equals(getName(), line.getName())
			&& Objects.equals(getColor(), line.getColor()) && Objects.equals(getSections(),
			line.getSections());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getId(), getName(), getColor(), getSections());
	}
}
