package wooteco.subway.admin.dto;


import javax.validation.constraints.NotBlank;

public class StationCreateRequest {
    @NotBlank
    private String name;

    public String getName() {
        return name;
    }
}
