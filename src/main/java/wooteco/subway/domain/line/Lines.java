package wooteco.subway.domain.line;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class Lines {

    private final List<Line> lines;

    public int maxExtraFare() {
        return lines.stream()
                .mapToInt(line -> line.getExtraFare())
                .max()
                .getAsInt();
    }
}
