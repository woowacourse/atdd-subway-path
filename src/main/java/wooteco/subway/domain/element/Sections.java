package wooteco.subway.domain.element;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Sections {

    public static final int COMBINE_SIZE = 2;
    private static final int UP_SECTION = 0;
    private static final int DOWN_SECTION = 1;

    private final List<Section> sections;

    private Sections(List<Section> sections) {
        this.sections = new ArrayList<>(sections);
    }

    public static Sections createUnSorted(List<Section> sections) {
        return new Sections(sections);
    }

    public static Sections create(List<Section> sections) {
        return new Sections(sort(sections));
    }

    private static List<Section> sort(List<Section> sections) {
        return fillSection(sections, findFirstStation(sections));
    }

    private static Station findFirstStation(List<Section> sections) {
        List<Station> upStations = getAllUpStations(sections);
        List<Station> downStations = getAllDownStations(sections);
        return upStations.stream()
                .filter(upStation -> !downStations.contains(upStation))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("첫번째 역이 존재하지 않습니다."));
    }

    private static List<Station> getAllUpStations(List<Section> sections) {
        return sections.stream()
                .map(Section::getUpStation)
                .collect(Collectors.toList());
    }

    private static List<Station> getAllDownStations(List<Section> sections) {
        return sections.stream()
                .map(Section::getDownStation)
                .collect(Collectors.toList());
    }

    private static List<Section> fillSection(List<Section> sections, Station firstStation) {
        List<Section> result = new ArrayList<>();
        Station nowStation = firstStation;
        while (result.size() != sections.size()) {
            Section nextSection = findNextSection(sections, nowStation);
            result.add(nextSection);
            nowStation = nextSection.getDownStation();
        }
        return result;
    }

    private static Section findNextSection(List<Section> sections, Station upStation) {
        return sections.stream()
                .filter(section -> section.isEqualToUpStation(upStation))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("해당 상행역과 연결된 구간이 없습니다."));
    }

    public Section combine(Line line, List<Section> sections) {
        validateCombineSize(sections);
        Section upSection = sections.get(UP_SECTION);
        Section downSection = sections.get(DOWN_SECTION);
        validateAvailableConnect(upSection, downSection);
        return combineSection(line, upSection, downSection);
    }

    private void validateCombineSize(List<Section> sections) {
        if (sections.size() != COMBINE_SIZE) {
            throw new IllegalArgumentException("2개의 구간을 합칠 수 있습니다.");
        }
    }

    private void validateAvailableConnect(Section upSection, Section downSection) {
        if (!upSection.canConnectToNext(downSection)) {
            throw new IllegalArgumentException("연결할 수 없는 구간입니다.");
        }
    }

    private Section combineSection(Line line, Section upSection, Section downSection) {
        return new Section(
                line,
                upSection.getUpStation(),
                downSection.getDownStation(),
                upSection.getDistance() + downSection.getDistance()
        );
    }

    public List<Section> findUpdatedSections(Section section) {
        validateIsExist(section);
        return findAddedSection(section).split(section);
    }

    private void validateIsExist(Section section) {
        if (containsSection(section)) {
            throw new IllegalArgumentException("기존에 존재하는 구간입니다.");
        }
    }

    private Section findAddedSection(Section section) {
        return sections.stream()
                .filter(it -> it.isEqualToUpStation(section.getUpStation()) || it.isEqualToDownStation(
                        section.getDownStation()))
                .findFirst()
                .orElseGet(() -> isAddableFirstOrEndSection(section));
    }

    private boolean containsSection(Section section) {
        return sections.stream()
                .anyMatch(value -> value.equals(section));
    }

    private Section isAddableFirstOrEndSection(Section section) {
        return sections.stream()
                .filter(it -> it.isEqualToUpStation(section.getDownStation()) || it.isEqualToDownStation(
                        section.getUpStation()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("생성할 수 없는 구간입니다."));
    }

    public List<Section> findDeleteSections(Line line, Station station) {
        return sections.stream()
                .filter(value -> value.isEqualToLine(line) && value.isEqualToUpOrDownStation(station))
                .collect(Collectors.toList());
    }

    public List<Station> getStations() {
        Set<Station> stations = new LinkedHashSet<>();
        for (Section value : sections) {
            stations.add(value.getUpStation());
            stations.add(value.getDownStation());
        }
        return List.copyOf(stations);
    }

    public boolean isStationIn(Station station) {
        return getStations().contains(station);
    }

    public List<Section> getSections() {
        return List.copyOf(sections);
    }
}
