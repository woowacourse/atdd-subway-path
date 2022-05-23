package wooteco.subway.domain;

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
