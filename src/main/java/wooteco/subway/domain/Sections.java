package wooteco.subway.domain;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Sections {

    private final List<Section> sections;

    public Sections(List<Section> sections) {
        this.sections = sections;
    }

    public static Sections createByStationId(List<Section> sections, Long stationId) {
        if (sections.size() == 1) {
            throw new IllegalArgumentException("[ERROR] 구간이 하나인 노선에서 마지막 구간을 제거할 수 없습니다.");
        }

        var parsedSections = sections.stream()
                .filter(it -> it.isSameStationId(stationId))
                .collect(Collectors.toList());

        return new Sections(parsedSections);
    }

    public Section createSectionBySection(Section section) {
        var sameUpStationSection = sections.stream().filter(it -> it.isSameUpStationId(section)).findAny();
        var downUpStationSection = sections.stream().filter(it -> it.isSameDownStationId(section)).findAny();

        checkValidation(sameUpStationSection, downUpStationSection);

        return sameUpStationSection.map(it -> Section.createWhenSameUpStation(it, section))
                .or(() -> downUpStationSection.map(it -> Section.createWhenSameDownStation(it, section))).get();
    }

    private void checkValidation(Optional<Section> sameUpStationSection, Optional<Section> downUpStationSection) {
        if (sameUpStationSection.isPresent() && downUpStationSection.isPresent()) {
            throw new IllegalArgumentException("[ERROR] 상행역과 하행역이 이미 노선에 모두 등록되어 있습니다.");
        }

        if (sameUpStationSection.isEmpty() && downUpStationSection.isEmpty()) {
            throw new IllegalArgumentException("[ERROR] 상행역과 하행역 둘 중 하나도 포함되어있지 않습니다.");
        }
    }

    public boolean hasOnlyOneSection() {
        return sections.size() == 1;
    }

    public Section createSection() {
        var firstSection = sections.get(0);
        var secondSection = sections.get(1);

        return new Section(
                firstSection.getId(),
                firstSection.getUpStationId(),
                secondSection.getDownStationId(),
                firstSection.plusDistance(secondSection)
        );
    }

    public Section getFirstSection() {
        return sections.get(0);
    }

    public Section getSecondSection() {
        return sections.get(1);
    }
}
