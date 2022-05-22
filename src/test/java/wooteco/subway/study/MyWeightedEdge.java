package wooteco.subway.study;

import org.jgrapht.graph.DefaultWeightedEdge;

public class MyWeightedEdge extends DefaultWeightedEdge {

    private final String name;
    private final int distance;

    public MyWeightedEdge(String name, int distance) {
        this.name = name;
        this.distance = distance;
    }

    public String getName() {
        return name;
    }

    @Override
    protected double getWeight() {
        return distance;
    }

    @Override
    public String toString() {
        return "MyWeightedEdge{" +
                "name='" + name + '\'' +
                ", distance=" + distance +
                '}';
    }
}
