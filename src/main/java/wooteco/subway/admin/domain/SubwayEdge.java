package wooteco.subway.admin.domain;

import java.util.Objects;

import org.jgrapht.graph.DefaultWeightedEdge;

public class SubwayEdge extends DefaultWeightedEdge {
    private LineStation lineStation;

    public int getDuration() {
        if (Objects.isNull(lineStation)) {
            return -1;
        }
        return lineStation.getDuration();
    }

    public int getDistance() {
        if (Objects.isNull(lineStation)) {
            return -1;
        }
        return lineStation.getDistance();
    }

    public void setLineStation(LineStation lineStation) {
        this.lineStation = lineStation;
    }
}
