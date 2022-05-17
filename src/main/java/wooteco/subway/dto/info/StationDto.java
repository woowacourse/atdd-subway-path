package wooteco.subway.dto.info;

public class StationDto {
    private long id;
    private String name;

    public StationDto(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public StationDto(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
