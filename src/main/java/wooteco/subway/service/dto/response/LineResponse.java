package wooteco.subway.service.dto.response;

import java.util.List;

public class LineResponse {

    private final long id;
    private final List<SectionResponse> sectionResponses;
    private final String name;
    private final String color;

    public LineResponse(long id, List<SectionResponse> sectionDtos, String name, String color) {
        this.id = id;
        this.sectionResponses = sectionDtos;
        this.name = name;
        this.color = color;
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
}
