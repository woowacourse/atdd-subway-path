package wooteco.subway.admin.dto;

import java.util.ArrayList;
import java.util.List;

public class PathResponse {

    public List<Object> getEdges() {
        return new ArrayList<>();
    }

    public Integer getTotalDistance() {
        return 1;
    }
}
