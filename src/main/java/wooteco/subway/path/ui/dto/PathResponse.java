package wooteco.subway.path.ui.dto;

import wooteco.subway.path.ui.dto.valid.NumberValidation;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.beans.ConstructorProperties;
import java.util.List;

public class PathResponse {

    @NotNull
    @Size(min = 2)
    private final List<StationResponse> stations;
    @NumberValidation
    private final int distance;

    @ConstructorProperties({"stations", "distance"})
    public PathResponse(List<StationResponse> stations, int distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

}
