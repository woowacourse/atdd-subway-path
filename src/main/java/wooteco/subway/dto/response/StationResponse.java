package wooteco.subway.dto.response;

import wooteco.subway.domain.station.Station;

public class StationResponse {
    private long id;
    private String name;

    private StationResponse() {
    }

    public StationResponse(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static StationResponse from(long id, Station station) {
        String name = station.getName();
        return new StationResponse(id, name);
    }

    public static StationResponse from(Station station) {
        return from(station.getId(), station);
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "StationResponse{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
