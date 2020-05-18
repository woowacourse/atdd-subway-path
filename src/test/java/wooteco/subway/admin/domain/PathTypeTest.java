package wooteco.subway.admin.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import wooteco.subway.admin.exception.NoExistPathTypeException;

class PathTypeTest {
    @DisplayName("존재하지 않는 경로 타입 조회")
    @Test
    void findPathTypeWithInvalidInput() {
        assertThatThrownBy(() -> PathType.findPathType("aaa"))
            .isInstanceOf(NoExistPathTypeException.class)
            .hasMessage("존재하지 않는 경로 타입입니다.");
    }
}