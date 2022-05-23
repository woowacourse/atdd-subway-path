package wooteco.subway.domain.section.sortStrategy;

import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.station.Station;

import java.util.*;

public class ConcreteSortStrategy implements SortStrategy {

    public List<Station> sort(List<Section> sections) {
        Queue<Section> sortedSections = sortSections(sections);
        return sortStations(sortedSections);
    }

    private Queue<Section> sortSections(List<Section> sections) {
        Queue<Section> sortedSections = new LinkedList<>();
        Section startSection = findStartSection(sections);
        sortedSections.add(startSection);

        return findAndAdd(sortedSections, new ArrayList<>(sections), startSection);
    }

    private Section findStartSection(List<Section> sections) {
        Station startStation = findStartStation(sections);

        return sections.stream()
                .filter(section -> startStation.equals(section.getUpStation()))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("상행 종점이 존재하지 않습니다."));
    }

    private Queue<Section> findAndAdd(Queue<Section> sortedSections, List<Section> remainSections, Section currentSection) {
        remainSections.remove(currentSection);
        if (remainSections.isEmpty()) {
            return sortedSections;
        }

        Section nextSection = remainSections.stream()
                .filter(section -> Objects.equals(currentSection.getDownStation(), section.getUpStation()))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("해당하는 구간이 존재하지 않습니다."));

        sortedSections.add(nextSection);
        return findAndAdd(sortedSections, remainSections, nextSection);
    }

    private List<Station> sortStations(Queue<Section> sortedSections) {
        Queue<Station> sortedStations = new LinkedList<>();
        Section firstSection = sortedSections.peek();
        Objects.requireNonNull(firstSection);
        Station firstStation = firstSection.getUpStation();

        sortedStations.add(firstStation);
        sortedStations = pollAndAdd(sortedStations, sortedSections);

        return new ArrayList<>(sortedStations);
    }

    private Queue<Station> pollAndAdd(Queue<Station> sortedStations, Queue<Section> sortedSections) {
        if (sortedSections.isEmpty()) {
            return sortedStations;
        }
        Section section = sortedSections.poll();
        sortedStations.add(section.getDownStation());
        return pollAndAdd(sortedStations, sortedSections);
    }

    private Station findStartStation(List<Section> sections) {
        List<Station> upStations = new ArrayList<>();
        List<Station> downStations = new ArrayList<>();

        for (Section section : sections) {
            upStations.add(section.getUpStation());
            downStations.add(section.getDownStation());
        }
        upStations.removeAll(downStations);
        return upStations.get(0);
    }
}
