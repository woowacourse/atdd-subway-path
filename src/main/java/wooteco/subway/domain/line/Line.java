package wooteco.subway.domain.line;

public class Line {

    private final Long id;
    private final String name;
    private final String color;
    private final int extraFare;

    public Line(Long id, String name, String color, int extraFare) {
        validateExtraFare(extraFare);
        this.id = id;
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
    }

    public Line(String name, String color, int extraFare) {
        this(null, name, color, extraFare);
    }

    private void validateExtraFare(int extraFare) {
        if (extraFare % 10 != 0) {
            throw new IllegalArgumentException("추가 요금은 10원 단위로 입력해야합니다.");
        }
    }

    public boolean isDifferentId(Line line) {
        return !id.equals(line.getId());
    }

    public boolean isSameName(Line line) {
        return name.equals(line.getName());
    }

    public boolean isSameColor(Line line) {
        return color.equals(line.getColor());
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
