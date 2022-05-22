package wooteco.subway.repository.table;

import wooteco.subway.domain.Line;

public class LineTable {

	private final Long id;
	private final String name;
	private final String color;
	private final int extraFare;

	public LineTable(Long id, String name, String color, int extraFare) {
		this.id = id;
		this.name = name;
		this.color = color;
		this.extraFare = extraFare;
	}

	public static LineTable from(Line line) {
		return new LineTable(line.getId(), line.getName(), line.getColor(), line.getExtraFare());
	}

	public Line toEntity() {
		return new Line(id, name, color);
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getColor() {
		return color;
	}

	public int getExtraFare() {
		return extraFare;
	}
}
