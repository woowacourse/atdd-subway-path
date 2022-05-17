package wooteco.subway.domain;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.utils.exception.SectionCreateException;
import wooteco.subway.utils.exception.SectionDeleteException;
import wooteco.subway.utils.exception.SectionNotFoundException;
import wooteco.subway.utils.exception.StationNotFoundException;
import wooteco.subway.utils.exception.SubwayException;

public class Sections {

    public static final int NEED_MERGE_SIZE = 2;

    private static final String SECTION_ALREADY_EXIST_MESSAGE = "이미 존재하는 구간입니다.";
    private static final String SECTION_NOT_CONNECT_MESSAGE = "구간이 연결되지 않습니다";
    private static final String SECTION_MUST_SHORTER_MESSAGE = "기존의 구간보다 긴 구간은 넣을 수 없습니다.";
    private static final int MIN_SIZE = 1;
    private static final int DEFAULT_DISTANCE = 10;
    private static final int DEFAULT_FARE = 1250;
    private static final int OVER_FARE_DISTANCE = 50;
    private static final int STANDARD_UNIT = 5;
    private static final int MAX_UNIT = 8;

    private final List<Section> values;

    public Sections(final List<Section> values) {
        this.values = new ArrayList<>(values);
    }

    public void add(final Section section) {
        validateSectionCreate(section);
        cutInSection(section);
        values.add(section);
    }

    private void validateSectionCreate(final Section section) {
        validateDuplicateSection(section);
        validateSectionConnect(section);
        validateExistSection(section);
    }

    private void validateExistSection(final Section section) {
        Station upStation = section.getUpStation();
        Station downStation = section.getDownStation();
        if (isSectionConnected(upStation, downStation) || isSectionConnected(downStation, upStation)) {
            throw new SectionCreateException(SECTION_ALREADY_EXIST_MESSAGE);
        }
    }

    private boolean isSectionConnected(final Station upStation, final Station downStation) {
        return getUpStations().contains(upStation) && getDownStations().contains(downStation);
    }

    private List<Station> getUpStations() {
        return values.stream()
                .map(Section::getUpStation)
                .collect(toList());
    }

    private List<Station> getDownStations() {
        return values.stream()
                .map(Section::getDownStation)
                .collect(toList());
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

    private void cutInSection(final Section section) {
        values.stream()
                .filter(value -> value.isSameUpStation(section.getUpStation())
                        || value.isSameDownStation(section.getDownStation()))
                .findAny()
                .ifPresent(foundSection -> updateCutInSection(section, foundSection));
    }

    private void updateCutInSection(final Section section, final Section foundSection) {
        validateCutInDistance(section, foundSection);
        if (foundSection.isSameUpStation(section.getUpStation())) {
            updateStationAndDistance(foundSection,
                    section.getDownStation(),
                    foundSection.getDownStation(),
                    section.getDistance());
            return;
        }
        updateStationAndDistance(foundSection,
                foundSection.getUpStation(),
                section.getUpStation(),
                section.getDistance());
    }

    private void validateCutInDistance(Section section, Section foundSection) {
        if (!foundSection.isLongerThan(section.getDistance())) {
            throw new SectionCreateException(SECTION_MUST_SHORTER_MESSAGE);
        }
    }

    private void updateStationAndDistance(final Section section,
            final Station upStation,
            final Station downStation,
            final int distance) {
        section.updateStations(upStation, downStation);
        section.subtractDistance(distance);
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
        validateDelete();
        List<Section> sections = values.stream()
                .filter(value -> value.isSameUpStation(station)
                        || value.isSameDownStation(station))
                .collect(toList());
        values.removeAll(sections);
        if (isSectionNeedMerge(sections)) {
            values.add(sections.get(0).merge(sections.get(1)));
        }
        return sections;
    }

    private boolean isSectionNeedMerge(final List<Section> sections) {
        return sections.size() == NEED_MERGE_SIZE;
    }

    private void validateDelete() {
        if (values.size() <= MIN_SIZE) {
            throw new SectionDeleteException();
        }
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
        List<Station> downStations = getDownStations();
        List<Station> upStations = getUpStations();

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

    public int calculateMinDistance(final Station startStation, final Station endStation) {
        validateExistStation(startStation, endStation);
        try {
            return (int) createSectionDijkstraShortestPath().getPathWeight(startStation, endStation);
        } catch (IllegalArgumentException e) {
            throw new SectionNotFoundException();
        }
    }

    private DijkstraShortestPath<Station, DefaultWeightedEdge> createSectionDijkstraShortestPath() {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph
                = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        for (Station station : sortSections()) {
            graph.addVertex(station);
        }
        for (Section section : values) {
            assignWeight(graph, section);
        }
        return new DijkstraShortestPath<>(graph);
    }

    private void assignWeight(final WeightedMultigraph<Station, DefaultWeightedEdge> graph, final Section section) {
        graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
    }

    private void validateExistStation(Station startStation, Station endStation) {
        if (!isExistStation(startStation) || !isExistStation(endStation)) {
            throw new StationNotFoundException();
        }
    }

    public List<Station> findShortestStations(final Station startStation, final Station endStation) {
        validateExistStation(startStation, endStation);
        try {
            return createSectionDijkstraShortestPath().getPath(startStation, endStation).getVertexList();
        } catch (IllegalArgumentException e) {
            throw new SectionNotFoundException();
        }
    }

    public int calculateFare(final int distance) {
        if (distance <= DEFAULT_DISTANCE) {
            return DEFAULT_FARE;
        }
        if (distance <= OVER_FARE_DISTANCE) {
            return DEFAULT_FARE + calculateOverFare(distance - DEFAULT_DISTANCE, STANDARD_UNIT);
        }
        return DEFAULT_FARE
                + calculateOverFare(OVER_FARE_DISTANCE - DEFAULT_DISTANCE, STANDARD_UNIT)
                + calculateOverFare(distance - OVER_FARE_DISTANCE, MAX_UNIT);
    }

    private int calculateOverFare(final int distance, final int unit) {
        return (int) ((Math.ceil((distance - 1) / unit) + 1) * 100);
    }

    private boolean isExistStation(final Station station) {
        return values.stream()
                .anyMatch(section -> section.have(station));
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
