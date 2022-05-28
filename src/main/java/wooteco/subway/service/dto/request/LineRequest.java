package wooteco.subway.service.dto.request;

import java.util.List;

public class LineRequest {

    private final List<SectionRequest> sectionRequests;
    private final String name;
    private final String color;
    private final Long extraFare;

    public LineRequest(List<SectionRequest> sectionRequests, String name, String color, Long extraFare) {
        this.sectionRequests = sectionRequests;
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
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

    public Long getExtraFare() {
        return extraFare;
    }
}
