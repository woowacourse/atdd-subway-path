package wooteco.subway.util;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class StationIdParser {
    public static List<Long> parse(String rowString) {
        var strings = rowString.replaceAll("[()]", "");

        return Arrays.stream(strings.split(":"))
                .map(String::trim)
                .map(Long::parseLong)
                .collect(Collectors.toList());
    }
}
