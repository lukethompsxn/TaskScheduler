package se306.a1.scheduler.data;

import java.util.Objects;

public class Edge {
    private final Node parent;
    private final Node child;
    private final int cost;

    public Edge(Node parent, Node child, int cost) {
        this.parent = parent;
        this.child = child;
        this.cost = cost;
    }

    public Node getParent() {
        return parent;
    }

    public Node getChild() {
        return child;
    }

    public int getCost() {
        return cost;
    }

    @Override
    public String toString() {
        return String.format("\t%s -> %s\t [Weight=%d];", parent, child, cost);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o == this) return true;
        if (!(o instanceof Edge)) return false;
        Edge e = (Edge) o;
        return e.parent.equals(parent) && e.child.equals(child);
    }

    @Override
    public int hashCode() {
        return Objects.hash(parent, child);
    }
}
