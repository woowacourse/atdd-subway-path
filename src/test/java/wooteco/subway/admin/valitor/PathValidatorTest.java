package wooteco.subway.admin.valitor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import wooteco.subway.admin.controller.validator.PathValidator;
import wooteco.subway.admin.domain.PathType;
import wooteco.subway.admin.dto.PathRequest;

@ExtendWith(MockitoExtension.class)
public class PathValidatorTest {
    @Mock
    private PathValidator pathValidator;

    @DisplayName("출발역과 도착역을 같은 역으로 입력했을 경우 예외처리")
    @Test
    void sameStationTest() {
        PathRequest pathRequest = new PathRequest(1L, 1L, PathType.DISTANCE);
        pathValidator.valid(pathRequest);
    }
}
