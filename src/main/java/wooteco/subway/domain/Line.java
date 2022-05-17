package wooteco.subway.domain;

import wooteco.subway.exception.ClientException;

public class Line {

    private static final long BASIC_ID = 0L;

    private final Long id;
    private final String name;
    private final String color;
    private final int extraFare;

    public Line(Long id, String name, String color, int extraFare) {
        validateNull(name, color);
        validateExtraFare(extraFare);
        this.id = id;
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
    }

    public Line(String name, String color, int extraFare) {
        this(BASIC_ID, name, color, extraFare);
    }

    private void validateNull(String name, String color) {
        if (name.isBlank() || color.isBlank()) {
            throw new ClientException("지하철 노선의 이름과 색을 모두 입력해주세요.");
        }
    }

    private void validateExtraFare(int extraFare) {
        if (extraFare < 0) {
            throw new ClientException("추가요금은 음수가 될 수 없습니다.");
        }
    }

    public boolean isSameName(String target) {
        return name.equals(target);
    }

    public long getId() {
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
