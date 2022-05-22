package wooteco.subway.domain.fixture;

import java.util.List;

import wooteco.subway.domain.line.Line;

public class LineFixture {

    public static Line getLineAb() {
        return new Line(1L,
            "분당선",
            "RED",
            500,
            List.of(SectionFixture.getSectionAb())
        );
    }

    public static Line getLineAbc() {
        return new Line(1L,
            "분당선",
            "RED",
            500,
            List.of(SectionFixture.getSectionAb(), SectionFixture.getSectionBc())
        );
    }

    public static Line getLineBd() {
        return new Line(2L, "부분당선", "BLUE", 500, List.of(SectionFixture.getSectionBd()));
    }

    public static Line getLineXy() {
        return new Line(2L, "부분당선", "BLUE", 500, List.of(SectionFixture.getSectionXy()));
    }

    public static Line getNewLine() {
        final Line line = new Line("새로운역", "새로운색", 500);
        line.addSection(SectionFixture.getSectionAc());
        return line;
    }

    public static Line getLineAc() {
        return new Line(2L, "부분당선", "BLUE", 500, List.of(SectionFixture.getSectionAc()));
    }
}
