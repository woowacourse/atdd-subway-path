package wooteco.subway.domain.line;

public class Line {

    private Long id;
    private final String name;
    private final String color;
    private final int extraFare;

    public Line(Long id, String name, String color, int extraFare) {
        validate(name, color, extraFare);
        this.id = id;
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
    }

    public Line(String name, String color, int extraFare) {
        this(null, name, color, extraFare);
    }

    public void validate(String name, String color, int extraFare) {
        checkNull(name, color);
        checkLength(color);
        checkMinimum(extraFare);
    }

    private void checkNull(String name, String color) {
        if (name == null || color == null) {
            throw new IllegalArgumentException("노선 이름과 색에는 빈 값이 올 수 없습니다.");
        }
    }

    private void checkLength(String color) {
        if (color.length() > 20) {
            throw new IllegalArgumentException("노선 색은 20자 이하만 입력 가능합니다.");
        }
    }

    private void checkMinimum(int extraFare) {
        if (extraFare < 0) {
            throw new IllegalArgumentException("추가 요금에는 0이상의 값만 올 수 있습니다.");
        }
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
