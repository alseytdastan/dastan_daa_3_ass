# Project Report: Minimum Spanning Tree Optimization
## **City Transportation Network Analysis using Prim’s and Kruskal’s Algorithms**
### **Objective:**
The objective of this project is to analyze and optimize a city's transportation network by implementing and comparing Prim's and Kruskal's algorithms for finding the Minimum Spanning Tree (MST). The goal is to minimize the total distance or cost of connecting all locations in the city while ensuring efficient transportation routes. I also analyzed the efficiency of both algorithms and compared their performance.

## Overview
This project implements and compares two classic algorithms for finding the **Minimum Spanning Tree (MST)** of a weighted undirected graph:
- **Prim’s Algorithm**
- **Kruskal’s Algorithm**

## The analysis includes:
- Theoretical background
- Java implementation
- Execution on multiple graph datasets (from small to extra-large)
- Empirical comparison (execution time, operation count, total MST cost)

## 1. Summary of Input Data and Algorithm Results
### Input Data Overview
The project has **28 graphs** in **4 input files**:

| Dataset | Vertices | Edges | # Graphs |
|--------|----------|--------|----------|
| `small.json` | 25       | 90     | 5        |
| `medium.json` | 100      | 1,980  | 10       |
| `large.json` | 1,000    | ~299,700 | 10     |
| `xlarge.json` | 1,500    | ~562,125 | 3        |
| **Total** | — | — | **28** |

### Algorithm Results

| Metric | Prim’s Algorithm | Kruskal’s Algorithm                                                                                            |
|-------|-------------|----------------------------------------------------------------------------------------------------------------|
| **Total Cost** | Identical to Kruskal | Identical to Prim                                                                                              |
| **Execution Time (avg)** | Faster on dense graphs (more edges per node) because it selects edges using a priority queue (min-heap) without needing to sort all edges first. | Slower on dense graphs since it must sort all edges initially, an O(E log E) operation that dominates runtime. |
| **Operation Count (avg)** | Lower, because it uses a greedy expansion of the MST with minimal comparisons and no need for repeated Union-Find operations. | Higher, due to sorting and repeated Union-Find (find & union) operations for cycle detection.                                                                          |

> **Key Observation**: Both algorithms produce **identical MST cost** in **all 28 cases** = **correctness verified!!**

# Algorithms Description
## Prim’s Algorithm
### Idea:
Starts from any node and grow the MST by repeatedly adding the cheapest edge connecting the tree to a new vertex.

#### Steps:
- Pick any starting vertex.
- Use a priority queue (min-heap) to find the smallest adjacent edge.
- Add the edge if it connects a new vertex.
- Repeat until all vertices are included.

### Complexity:
**O(E log V) using a min-heap.**

#### Advantages:
- Efficient for dense graphs.
- No need to sort all edges.

## Kruskal’s Algorithm
### Idea:
Sort all edges by weight and add them one by one, skipping any that form a cycle, until all vertices are connected.

#### Steps:
- Sort all edges by increasing weight.
- Add the next smallest edge to the MST if it doesn’t create a cycle.
- Use Union-Find (Disjoint Set) to detect cycles.
- Stop when MST has (V − 1) edges.

### Complexity:
**O(E log E) due to edge sorting.**

#### Advantages:
- Works well for sparse graphs.
- Simple and intuitive.

# Comparison: Prim’s vs Kruskal’s (Theory & Practice)
| **Comparison**           | **Prim’s Algorithm**                            | **Kruskal’s Algorithm**               |
|--------------------------|-------------------------------------------------|---------------------------------------|
| **Graph Type**           | Performs best on **dense** graphs (`E ≈ V²`)     | Performs best on **sparse** graphs (`E ≈ V`) |
| **Approach**             | Starts from one node and grows MST step-by-step | Considers all edges in sorted order   |
| **Data Structures Used** | Min-heap (priority queue), adjacency list       | Edge list + Disjoint Set (Union-Find) |
| **Cycle Handling**       | Implicitly avoided via visited nodes            | Explicitly handled using Union-Find   |
| **Time Complexity**      | `O(E log V)`                                    | `O(E log E)` ≈ `O(E log V)`           |
| **Space Complexity**     | `O(V)`                                          | `O(V + E)`                            |
| **Implementation**       | Easier to code, efficient with adjacency list   | Slightly more complex (requires sorting + Union-Find) |
| **Best Use Case**        | **Dense** graphs or when graph stored as adjacency list | **Sparse** graphs or when edges given as list |

### In practice (from `results.csv`)

| **Dataset**       | **Prim Time (ms)** | **Kruskal Time (ms)** | **Prim Ops** | **Kruskal Ops** |
|-------------------|--------------------|------------------------|--------------|------------------|
| **Small (25V)**   | ~1.2               | ~1.8                   | ~220         | ~1,200           |
| **Medium (100V)** | ~3.0               | ~6.0                   | ~4,200       | ~32,800          |
| **Large (1000V)** | ~200               | ~500                   | ~600K        | ~6.4M            |
| **XLarge (1500V)**| ~190               | ~380                   | ~1.1M        | ~12.5M           |

> **Prim’s is 2–3x faster** in execution time  
> Kruskal uses **10–12x more operations**


### Conclusions: Which algorithm to prefer?

| **Condition**                  | **Recommended Algorithm** | **Reason**                              |
|--------------------------------|----------------------------|-----------------------------------------|
| **Dense Graphs** (`E ≈ V²`)     | **Prim’s**                 | Fewer edge examinations                 |
| **Sparse Graphs** (`E ≈ V`)     | **Kruskal’s**              | Sorting overhead negligible             |
| **Adjacency List Available**   | **Prim’s**                 | Natural fit                             |
| **Edge List Only**             | **Kruskal’s**              | Works directly on edges                 |
| **Memory Critical**            | **Prim’s**                 | `O(V)` space                            |
| **Parallelization Needed**     | **Kruskal’s**              | Edge sorting can be parallelized        |
| **Implementation Simplicity**  | **Prim’s**                 | No Union-Find needed

### Final Recommendation

> **For city transportation networks** (dense, adjacency list, real-time planning):  
> **Better to use Prim’s Algorithm**
> - **It is faster(2-3x in dense graphs), takes less memory, same correct MST**

> **Use Kruskal’s if**:
>
> Graph is **sparse**, input is **edge list only** and you need **parallel processing**

## References
1. 2000–2022, Robert Sedgewick and Kevin Wayne. https://algs4.cs.princeton.edu/43mst/KruskalMST.java.html
2. Java Documentation: `PriorityQueue`, `HashMap`, `Union-Find (Disjoint Set)`
3. Gson Library: <https://github.com/google/gson>
4. JUnit 5: <https://junit.org/junit5/>

## Command Summary
```bash
# Build & Run
mvn clean compile exec:java

# 3. Output Generated
data/output/assign_3_*.json
data/output/combined_output.json
report/results.csv
report/*.png  (6 visualization screenshots)

# 4. Run Tests
mvn compile
mvn test