package wooteco.subway.admin.domain.line.path;

import java.util.List;

public interface Path {
    List<Long> getPath();

    int calculateTotalDistance();

    int calculateTotalDuration();
}
