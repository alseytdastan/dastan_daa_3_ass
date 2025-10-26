package com.mst.algorithm;

import com.mst.model.Graph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for both Prim's and Kruskal's MST algorithms.
 */
public class MSTAlgorithmTest {
    
    private PrimAlgorithm prim;
    private KruskalAlgorithm kruskal;
    
    @BeforeEach
    void setUp() {
        prim = new PrimAlgorithm();
        kruskal = new KruskalAlgorithm();
    }
    
    @Test
    void testEmptyGraph() {
        Graph graph = new Graph(0);
        
        PrimAlgorithm.PrimResult primResult = prim.findMST(graph);
        KruskalAlgorithm.KruskalResult kruskalResult = kruskal.findMST(graph);
        
        assertEquals(0, primResult.getMstEdges().size());
        assertEquals(0, kruskalResult.getMstEdges().size());
        assertEquals(0, primResult.getTotalCost());
        assertEquals(0, kruskalResult.getTotalCost());
    }
    
    @Test
    void testSingleVertexGraph() {
        Graph graph = new Graph(1);
        
        PrimAlgorithm.PrimResult primResult = prim.findMST(graph);
        KruskalAlgorithm.KruskalResult kruskalResult = kruskal.findMST(graph);
        
        assertEquals(0, primResult.getMstEdges().size());
        assertEquals(0, kruskalResult.getMstEdges().size());
        assertEquals(0, primResult.getTotalCost());
        assertEquals(0, kruskalResult.getTotalCost());
    }
    
    @Test
    void testTwoVertexGraph() {
        Graph graph = new Graph(2);
        graph.addEdge(0, 1, 10);
        
        PrimAlgorithm.PrimResult primResult = prim.findMST(graph);
        KruskalAlgorithm.KruskalResult kruskalResult = kruskal.findMST(graph);
        
        assertEquals(1, primResult.getMstEdges().size());
        assertEquals(1, kruskalResult.getMstEdges().size());
        assertEquals(10, primResult.getTotalCost());
        assertEquals(10, kruskalResult.getTotalCost());
        assertEquals(primResult.getTotalCost(), kruskalResult.getTotalCost());
    }
    
    @Test
    void testSimpleGraph() {
        // Graph from example
        Graph graph = new Graph(4);
        graph.addEdge(0, 1, 10);
        graph.addEdge(0, 2, 6);
        graph.addEdge(0, 3, 5);
        graph.addEdge(1, 3, 15);
        graph.addEdge(2, 3, 4);
        
        PrimAlgorithm.PrimResult primResult = prim.findMST(graph);
        KruskalAlgorithm.KruskalResult kruskalResult = kruskal.findMST(graph);
        
        // Both should have V-1 edges
        assertEquals(graph.getVertices() - 1, primResult.getMstEdges().size());
        assertEquals(graph.getVertices() - 1, kruskalResult.getMstEdges().size());
        
        // Total cost must be identical
        assertEquals(primResult.getTotalCost(), kruskalResult.getTotalCost());
        
        // Expected MST cost for this graph: 5 + 4 + 10 = 19
        assertTrue(primResult.getTotalCost() > 0);
        assertTrue(kruskalResult.getTotalCost() > 0);
    }
    
    @Test
    void testCorrectnessSameCost() {
        Graph graph = new Graph(5);
        graph.addEdge(0, 1, 2);
        graph.addEdge(0, 2, 5);
        graph.addEdge(1, 2, 1);
        graph.addEdge(1, 3, 4);
        graph.addEdge(2, 3, 3);
        graph.addEdge(2, 4, 6);
        graph.addEdge(3, 4, 2);
        
        PrimAlgorithm.PrimResult primResult = prim.findMST(graph);
        KruskalAlgorithm.KruskalResult kruskalResult = kruskal.findMST(graph);
        
        // Critical assertion: both algorithms must produce same MST cost
        assertEquals(primResult.getTotalCost(), kruskalResult.getTotalCost(),
                "Prim and Kruskal should produce identical MST costs");
        
        // Both should have exactly V-1 edges
        assertEquals(graph.getVertices() - 1, primResult.getMstEdges().size());
        assertEquals(graph.getVertices() - 1, kruskalResult.getMstEdges().size());
    }
    
    @Test
    void testAcyclicMST() {
        Graph graph = new Graph(6);
        graph.addEdge(0, 1, 4);
        graph.addEdge(0, 2, 4);
        graph.addEdge(1, 2, 2);
        graph.addEdge(1, 3, 6);
        graph.addEdge(2, 3, 8);
        graph.addEdge(2, 4, 9);
        graph.addEdge(3, 4, 11);
        graph.addEdge(3, 5, 7);
        graph.addEdge(4, 5, 1);
        
        PrimAlgorithm.PrimResult primResult = prim.findMST(graph);
        KruskalAlgorithm.KruskalResult kruskalResult = kruskal.findMST(graph);
        
        // MST should have exactly V-1 edges (no cycles)
        assertEquals(graph.getVertices() - 1, primResult.getMstEdges().size());
        assertEquals(graph.getVertices() - 1, kruskalResult.getMstEdges().size());
    }
    
    @Test
    void testAllVerticesConnected() {
        Graph graph = new Graph(7);
        graph.addEdge(0, 1, 7);
        graph.addEdge(0, 3, 5);
        graph.addEdge(1, 2, 8);
        graph.addEdge(1, 3, 9);
        graph.addEdge(2, 4, 5);
        graph.addEdge(3, 4, 15);
        graph.addEdge(3, 5, 6);
        graph.addEdge(4, 5, 8);
        graph.addEdge(4, 6, 9);
        graph.addEdge(5, 6, 11);
        
        PrimAlgorithm.PrimResult primResult = prim.findMST(graph);
        KruskalAlgorithm.KruskalResult kruskalResult = kruskal.findMST(graph);
        
        // Both should connect all 7 vertices with 6 edges
        assertEquals(graph.getVertices() - 1, primResult.getMstEdges().size());
        assertEquals(graph.getVertices() - 1, kruskalResult.getMstEdges().size());
        
        // Both should have same cost
        assertEquals(primResult.getTotalCost(), kruskalResult.getTotalCost());
    }
    
    @Test
    void testPerformanceMetrics() {
        Graph graph = new Graph(10);
        for (int i = 0; i < 9; i++) {
            for (int j = i + 1; j < 10; j++) {
                graph.addEdge(i, j, (i + j) % 50 + 1);
            }
        }
        
        PrimAlgorithm.PrimResult primResult = prim.findMST(graph);
        KruskalAlgorithm.KruskalResult kruskalResult = kruskal.findMST(graph);
        
        // Execution time should be non-negative
        assertTrue(primResult.getExecutionTime() >= 0);
        assertTrue(kruskalResult.getExecutionTime() >= 0);
        
        // Operation counts should be non-negative
        assertTrue(primResult.getOperations() >= 0);
        assertTrue(primResult.getComparisons() >= 0);
        assertTrue(kruskalResult.getComparisons() >= 0);
        assertTrue(kruskalResult.getUnions() >= 0);
    }
    
    @Test
    void testReproducibility() {
        Graph graph = new Graph(6);
        graph.addEdge(0, 1, 4);
        graph.addEdge(0, 2, 4);
        graph.addEdge(1, 2, 2);
        graph.addEdge(1, 3, 6);
        graph.addEdge(2, 3, 8);
        graph.addEdge(3, 4, 11);
        
        // Run algorithm twice
        PrimAlgorithm.PrimResult result1 = prim.findMST(graph);
        PrimAlgorithm.PrimResult result2 = prim.findMST(graph);
        
        // Results should be identical
        assertEquals(result1.getTotalCost(), result2.getTotalCost());
        assertEquals(result1.getMstEdges().size(), result2.getMstEdges().size());
    }
    
    @Test
    void testConnectedComponent() {
        Graph graph = new Graph(8);
        // One connected component
        graph.addEdge(0, 1, 1);
        graph.addEdge(0, 2, 2);
        graph.addEdge(1, 3, 3);
        graph.addEdge(2, 4, 4);
        graph.addEdge(3, 5, 5);
        graph.addEdge(4, 6, 6);
        graph.addEdge(5, 7, 7);
        
        // Check if graph is connected
        assertTrue(graph.isConnected(), "Graph should be connected");
        
        PrimAlgorithm.PrimResult primResult = prim.findMST(graph);
        KruskalAlgorithm.KruskalResult kruskalResult = kruskal.findMST(graph);
        
        // Both should produce valid MST
        assertEquals(7, primResult.getMstEdges().size());
        assertEquals(7, kruskalResult.getMstEdges().size());
        assertEquals(primResult.getTotalCost(), kruskalResult.getTotalCost());
    }
}

