package wooteco.subway.service.dto.request;

import java.util.List;

public class LineRequest {

    private final List<SectionRequest> sectionRequests;
    private final String name;
    private final String color;

    public LineRequest(List<SectionRequest> sectionRequests, String name, String color) {
        this.sectionRequests = sectionRequests;
        this.name = name;
        this.color = color;
    }

    public List<SectionRequest> getSectionRequests() {
        return sectionRequests;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }
}
