package com.mst.model;

/**
 * Represents a weighted edge in an undirected graph.
 * Used for modeling potential roads between city districts.
 */
public class Edge implements Comparable<Edge> {
    private int source;
    private int destination;
    private int weight;

    public Edge(int source, int destination, int weight) {
        this.source = source;
        this.destination = destination;
        this.weight = weight;
    }

    public int getSource() {
        return source;
    }

    public int getDestination() {
        return destination;
    }

    public int getWeight() {
        return weight;
    }

    @Override
    public int compareTo(Edge other) {
        return Integer.compare(this.weight, other.weight);
    }

    @Override
    public String toString() {
        return String.format("(%d-%d: %d)", source, destination, weight);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Edge edge = (Edge) obj;
        // Undirected edge equality
        return (source == edge.source && destination == edge.destination && weight == edge.weight) ||
               (source == edge.destination && destination == edge.source && weight == edge.weight);
    }

    @Override
    public int hashCode() {
        // Use a symmetric hash to handle undirected edges
        return source + destination + weight;
    }
}
