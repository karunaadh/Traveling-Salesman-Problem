import java.util.*;

public class NNSolver extends TSPSolver {
	// initialize solver
	public NNSolver() {
		super();
	}

	// nextNode function
	@Override
	public int[] nextNode(Graph G, ArrayList<Integer> visited) {
		// nextnode[0] = nextnode, nextnode[1] = insertion position
		int nextnode[] = new int[2];

		// get arraylist size
		int visitedSize = visited.size();

		// add nearest neighbour of last visited node to end
		nextnode[0] = nearestNeighbor(G, visited, visited.get(visitedSize - 1));

		// set next node position as last position
		nextnode[1] = visited.size();

		// return values
		return nextnode;
	}

}
