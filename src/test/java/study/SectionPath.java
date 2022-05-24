package study;

import org.jgrapht.graph.DefaultWeightedEdge;

public class SectionPath extends DefaultWeightedEdge {

    private final int number;
    private final double weight;

    public SectionPath(int number, int weight) {
        this.number = number;
        this.weight = weight;
    }

    @Override
    protected double getWeight() {
        return weight;
    }
}
