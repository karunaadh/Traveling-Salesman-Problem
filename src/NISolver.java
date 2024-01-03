import java.util.*;

public class NISolver extends TSPSolver {
	// initialize solver
	public NISolver() {
		super();
	}

	// nextNode function
	@Override
	public int[] nextNode(Graph G, ArrayList<Integer> visited) {
		// nextnode[0] = nextnode, nextnode[1] = insertion position
		int nextnode[] = new int[2];

		// get arraylist size
		int visitedSize = visited.size();

		// track cheapest cost
		double cheapestCost = Double.MAX_VALUE;
		double cost = 0.0;
		// index of node with cheapest NN
		int winningNodeIndex = 0;
		// cheapest NN
		int nearestNeighbour = -1;
		int NN = -1;

		// track current node
		int node = 0;

		// check through all nodes
		for (int i = 0; i < visitedSize; i++) {
			// get nearest neighbour to node i
			node = visited.get(i);
			NN = nearestNeighbor(G, visited, node);

			// if NN is found (!= -1)
			if (NN != -1) {
				// get cost
				cost = G.getCost(node, NN);

				// if it's the cheapest neighbour so far and arc exists between current
				// node in position i+1 and NN
				if (cost < cheapestCost) {
					// set as new cheapest cost
					cheapestCost = cost;
					// track index
					winningNodeIndex = i;
					// track cheapest NN
					nearestNeighbour = NN;
				}
			}

		}

		// add nearest neighbor to return array
		// if nearestNeighbour is still -1, run algorithm catches it
		nextnode[0] = nearestNeighbour;

		// set position
		if (winningNodeIndex == 0) {
			// if the first node has NN, add NN to front of path
			nextnode[1] = 0;
		} else {
			// otherwise, if other node has NN add it after the node with NN in path
			nextnode[1] = winningNodeIndex + 1;
		}

		// add nearest neighbor to return array
		// if nearestNeighbour is still -1, run algorithm catches it
		nextnode[0] = nearestNeighbour;

		// return values
		return nextnode;
	}

	// check if node k can be inserted at position i
	public static boolean canBeInserted(Graph G, ArrayList<Integer> visited, int i, int k) {
		// track result
		boolean result = false;

		// check if first and last
		if (i == 0 || i == visited.size() - 1) {
			result = true;
		} else {
			// check middle nodes
			result = G.existsArc(k, visited.get(i + 1));
		}

		// check if arc exists between node k and node at position i+1
		return result;
	}
}
