package wooteco.subway.domain;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Lines {

    private final List<Line> lines;

    public Lines(List<Line> lines) {
        this.lines = lines;
    }

    public List<Line> getLines() {
        return lines;
    }

    public Sections getAllSections() {
        List<Section> sections = new ArrayList<>();
        lines.forEach(line -> sections.addAll(line.getSections().get()));
        return new Sections(sections);
    }

    public Line getLineByMinDistance(Station upStation, Station downStation) {
        return lines.stream()
                .filter(line -> line.getSections().containsSection(upStation, downStation))
                .min(Comparator.comparingInt(Line::getExtraFare))
                .orElseThrow(() -> new IllegalStateException("구간 정보가 잘못되었습니다."));
    }


    public int getMaxExtraFare() {
        return lines.stream()
                .max(Comparator.comparingInt(Line::getExtraFare))
                .orElseThrow(() -> new IllegalStateException("노선이 없습니다."))
                .getExtraFare();
    }
}
