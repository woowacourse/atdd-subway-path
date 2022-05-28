package wooteco.subway.ui.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import wooteco.subway.service.dto.LineModificationServiceRequest;

public class LineModificationRequest {

    @NotBlank
    private String name;
    @NotBlank
    private String color;
    @NotNull
    private Integer extraFare;

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public Integer getExtraFare() {
        return extraFare;
    }

    public LineModificationServiceRequest toServiceRequest() {
        return new LineModificationServiceRequest(name, color, extraFare);
    }
}
