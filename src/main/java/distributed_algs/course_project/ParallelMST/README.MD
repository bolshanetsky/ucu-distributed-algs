## Parallel minimum spanning tree implementation algorithm implementation
#### Borys Olshanetskyi
#### Distributed Algorithms, UCU
#### June, 2018

##### **Abstract**
This project contains both parallel and sequential implementation of Boruvka's algorithm for finding minimum spanning
tree(MST) for the connected edge-weighted graph. There many known algorithms for finding MST. Boruvka's algorithm is 
one of the oldest, it was selected for implementation for the one particular reason, it is easily and effectively parallelizable.

#####**Short description of the algorithm:**
1) Input is a connected, weighted and directed graph.
2) Initialize all vertices as individual components (or sets).
3) Initialize MST as empty.
4) (Can be done in parallel) While there are more than one components, do following
   for each component.
      a)  Find the closest weight edge that connects this 
          component to any other component.
      b)  Add this closest edge to MST if not already added.  
5) Return MST.

As we can see each iteration of this algorithm doesn't depend on result of previous iteration, that means we can execute
step 4) in parallel manner.

#####**Implementation details**
**Assumptions**
* The graph is connected. The spanning-tree condition in our definition implies that the graph must be connected for 
an MST to exist. If a graph is not connected, we can adapt our algorithms to compute the MSTs of each of its connected 
components, collectively known as a minimum spanning forest.
* The edge weights are all different. If edges can have equal weights, the minimum spanning tree may not be unique. 
Making this assumption simplifies some of our proofs, but all of our our algorithms work properly even in the presence of equal weights.

**Tests**

Correctness of the implementation is tested on two data sets prepared by Princeton's University[1].
Performance test on 2 cores computer showed 30% improvement in parallel implementation comparing to sequential.

#####**References**
1. Test data sets: https://algs4.cs.princeton.edu/43mst/
2. Parallel Implementation of Boruvka’s Minimum Spanning Tree Algorithm, https://www.cs.ubc.ca/~condon/papers/chungcondon96.pdf
3. Minimum Weight Spanning Tree, http://iss.ices.utexas.edu/?p=projects/galois/benchmarks/mst



