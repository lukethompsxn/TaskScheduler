package se306.a1.scheduler.data;

public class Node {
    private final String label;
    private final int cost;

    public Node(String label, int cost) {
        this.label = label;
        this.cost = cost;
    }

    public String getLabel() {
        return label;
    }

    public int getCost() {
        return cost;
    }

    @Override
    public String toString() {
        return label;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o == this) return true;
        if (!(o instanceof Node)) return false;
        return label.equals(((Node) o).label);
    }

    @Override
    public int hashCode() {
        return label.hashCode();
    }
}
