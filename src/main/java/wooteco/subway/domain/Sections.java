package wooteco.subway.domain;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import wooteco.subway.exception.SectionCreateException;
import wooteco.subway.exception.SectionDeleteException;
import wooteco.subway.exception.SubwayException;

public class Sections {

    public static final int NEED_MERGE_SIZE = 2;

    private static final String SECTION_ALREADY_EXIST_MESSAGE = "이미 존재하는 구간입니다.";
    private static final String SECTION_NOT_CONNECT_MESSAGE = "구간이 연결되지 않습니다";
    private static final String SECTION_MUST_SHORTER_MESSAGE = "기존의 구간보다 긴 구간은 넣을 수 없습니다.";
    private static final int MIN_SIZE = 1;

    private final List<Section> values;

    public Sections(final List<Section> values) {
        this.values = new ArrayList<>(values);
    }

    public void add(final Section newSection) {
        validateSectionCreate(newSection);
        findNearbySection(newSection)
                .ifPresent(section -> updateSection(newSection, section));
        values.add(newSection);
    }

    private Optional<Section> findNearbySection(final Section section) {
        return values.stream()
                .filter(value -> value.isSameUpStation(section.getUpStation())
                        || value.isSameDownStation(section.getDownStation()))
                .findFirst();
    }

    private void validateSectionCreate(final Section section) {
        validateDuplicateSection(section);
        validateSectionConnect(section);
        validateExistSection(section);
    }

    private void validateDuplicateSection(final Section section) {
        boolean exist = values.stream()
                .anyMatch(value -> value.isSameSection(section));
        if (exist) {
            throw new SectionCreateException(SECTION_NOT_CONNECT_MESSAGE);
        }
    }

    private void validateSectionConnect(final Section section) {
        boolean exist = values.stream()
                .anyMatch(value -> value.haveStation(section));
        if (!exist) {
            throw new SectionCreateException(SECTION_NOT_CONNECT_MESSAGE);
        }
    }

    private void validateExistSection(final Section section) {
        Station upStation = section.getUpStation();
        Station downStation = section.getDownStation();
        if (isSectionConnected(upStation, downStation) || isSectionConnected(downStation, upStation)) {
            throw new SectionCreateException(SECTION_ALREADY_EXIST_MESSAGE);
        }
    }

    private boolean isSectionConnected(final Station upStation, final Station downStation) {
        return findUpStations().contains(upStation) && findDownStations().contains(downStation);
    }

    public List<Station> findUpStations() {
        return values.stream()
                .map(Section::getUpStation)
                .collect(toList());
    }

    public List<Station> findDownStations() {
        return values.stream()
                .map(Section::getDownStation)
                .collect(toList());
    }

    private void updateSection(final Section newSection, final Section foundSection) {
        validateCutInDistance(newSection, foundSection);
        if (foundSection.isSameUpStation(newSection.getUpStation())) {
            Station newUpStation = newSection.getDownStation();
            Station newDownStation = foundSection.getDownStation();
            foundSection.updateSection(newUpStation, newDownStation, newSection.getDistance());
            return;
        }
        Station newUpStation = foundSection.getUpStation();
        Station newDownStation = newSection.getUpStation();
        foundSection.updateSection(newUpStation, newDownStation, newSection.getDistance());
    }

    private void validateCutInDistance(Section section, Section foundSection) {
        if (!foundSection.isLongerThan(section.getDistance())) {
            throw new SectionCreateException(SECTION_MUST_SHORTER_MESSAGE);
        }
    }

    public Optional<Section> pickUpdate(final List<Section> sections) {
        return values.stream()
                .filter(value -> findUpdateSection(sections, value))
                .findFirst();
    }

    private boolean findUpdateSection(final List<Section> sections, final Section section) {
        return sections.stream()
                .anyMatch(value -> value.isUpdate(section));
    }

    public List<Section> delete(final Station station) {
        checkDeletable();
        List<Section> sections = values.stream()
                .filter(value -> value.haveStation(station))
                .collect(toList());
        values.removeAll(sections);
        if (isSectionNeedMerge(sections)) {
            values.add(sections.get(0).merge(sections.get(1)));
        }
        return sections;
    }

    private void checkDeletable() {
        if (values.size() <= MIN_SIZE) {
            throw new SectionDeleteException();
        }
    }

    private boolean isSectionNeedMerge(final List<Section> sections) {
        return sections.size() == NEED_MERGE_SIZE;
    }

    public List<Station> sortSections() {
        List<Station> stationResponses = new ArrayList<>();
        Station firstStation = findFirstStation();
        stationResponses.add(firstStation);
        while (nextStation(firstStation).isPresent()) {
            firstStation = nextStation(firstStation).get();
            stationResponses.add(firstStation);
        }
        return stationResponses;
    }

    private Station findFirstStation() {
        List<Station> downStations = findDownStations();
        List<Station> upStations = findUpStations();

        return upStations.stream()
                .filter(station -> !downStations.contains(station))
                .findFirst()
                .orElseThrow(() -> new SubwayException("[ERROR] 첫번째 구간을 찾을 수 없습니다."));
    }

    private Optional<Station> nextStation(final Station station) {
        return values.stream()
                .filter(value -> value.isSameUpStation(station))
                .map(Section::getDownStation)
                .findFirst();
    }

    public List<Section> getValues() {
        return List.copyOf(values);
    }

    @Override
    public String toString() {
        return "Sections{" +
                "values=" + values +
                '}';
    }
}
