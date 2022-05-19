package wooteco.subway.domain;

import java.util.List;

public class Line {
    private static final String ERROR_MESSAGE_NAME_SIZE = "존재할 수 없는 이름입니다.";
    private static final String ERROR_MESSAGE_COLOR_SIZE = "존재할 수 없는 색상입니다.";
    private static final String ERROR_MESSAGE_SECTIONS_SIZE = "구간이 %d개 밖에 없으므로 삭제할 수 없습니다.";
    private static final String ERROR_MESSAGE_FARE_NEGATIVE = "추가 요금은 음수일 수 없습니다.";
    private static final int MINIMUM_SECTIONS_SIZE = 1;
    private static final int NAME_SIZE_LIMIT = 255;
    private static final int COLOR_SIZE_LIMIT = 20;

    private Long id;
    private final String name;
    private final String color;
    private final int extraFare;
    private Sections sections;

    public Line(Long id, String name, String color, int extraFare, Sections sections) {
        validateNameSize(name);
        validateColorSize(color);
        validateFare(extraFare);
        this.id = id;
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
        this.sections = sections;
    }

    public Line(Long id, String name, String color, int extraFare) {
        this(id, name, color, extraFare, null);
    }

    public Line(String name, String color, int extraFare) {
        this(null, name, color, extraFare, null);
    }

    private void validateNameSize(String name) {
        if (name == null || name.isBlank() || name.length() > NAME_SIZE_LIMIT) {
            throw new IllegalArgumentException(ERROR_MESSAGE_NAME_SIZE);
        }
    }

    private void validateColorSize(String color) {
        if (color == null || color.isBlank() || color.length() > COLOR_SIZE_LIMIT) {
            throw new IllegalArgumentException(ERROR_MESSAGE_COLOR_SIZE);
        }
    }

    private void validateFare(int extraFare) {
        if (extraFare < 0) {
            throw new IllegalArgumentException(ERROR_MESSAGE_FARE_NEGATIVE);
        }
    }

    public void updateToAdd(Section section) {
        sections.updateToAdd(section);
    }

    public Section delete(Station station) {
        if (sections.size() <= MINIMUM_SECTIONS_SIZE) {
            throw new IllegalArgumentException(String.format(ERROR_MESSAGE_SECTIONS_SIZE, MINIMUM_SECTIONS_SIZE));
        }

        return sections.removeSection(station);
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

    public Sections getSections() {
        return sections;
    }

    public int getExtraFare() {
        return extraFare;
    }

    public List<Station> getStations() {
        return sections.getStations();
    }
}
