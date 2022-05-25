package wooteco.subway.service.dto.response;

import java.util.List;

public class LineResponse {

    private final long id;
    private final List<SectionResponse> sectionResponses;
    private final String name;
    private final String color;
    private final Long extraFare;

    public LineResponse(long id, List<SectionResponse> sectionResponses, String name, String color, Long extraFare) {
        this.id = id;
        this.sectionResponses = sectionResponses;
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
    }

    public long getId() {
        return id;
    }

    public List<SectionResponse> getSectionResponses() {
        return sectionResponses;
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
