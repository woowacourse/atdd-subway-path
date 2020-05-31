package wooteco.subway.admin.valitor;

import static org.assertj.core.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import wooteco.subway.admin.controller.validator.PathValidator;
import wooteco.subway.admin.domain.PathType;
import wooteco.subway.admin.dto.PathRequest;

public class PathValidatorTest {
    @DisplayName("출발역과 도착역을 같은 역으로 입력했을 경우 예외처리")
    @Test
    void sameStationTest() {
        PathRequest pathRequest = new PathRequest(1L, 1L, PathType.DISTANCE);
        assertThatThrownBy(() -> PathValidator.valid(pathRequest)).isInstanceOf(IllegalArgumentException.class)
            .hasMessage("출발역과 도착역이 동일합니다.");
    }
}