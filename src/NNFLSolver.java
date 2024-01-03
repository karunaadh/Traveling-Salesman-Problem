import java.util.*;

public class NNFLSolver extends TSPSolver {
	// initialize solver
	public NNFLSolver() {
		super();
	}

	// nextNode function
	@Override
	public int[] nextNode(Graph G, ArrayList<Integer> visited) {
		// nextnode[0] = nextnode, nextnode[1] = insertion position
		int nextnode[] = new int[2];

		int visitedSize = visited.size();

		// get last visited node number
		int firstVisited = visited.get(0);
		int lastVisited = visited.get(visitedSize - 1);

		// get nearest neighbour of first
		int NF = nearestNeighbor(G, visited, firstVisited);
		int NL = nearestNeighbor(G, visited, lastVisited);

		// check if no nearest last
		if (NL == -1) {
			// send nearest first
			nextnode[0] = NF;
			nextnode[1] = 0;
			// if NF != -1 and NL != -1
		} // check if no nearest to first
		else if (NF == -1) {
			// if NL == -1, that's returned and run function knows there's no solution
			// else the value of NL returned (since no NF, but NL exists)
			nextnode[0] = NL;
			nextnode[1] = visitedSize;

		} else {
			// if cost of NL <= NF, make it next node
			if (G.getCost(lastVisited, NL) <= G.getCost(firstVisited, NF)) {
				nextnode[0] = NL;
				nextnode[1] = visitedSize;

				// otherwise, NL is next node
			} else {
				nextnode[0] = NF;
				nextnode[1] = 0;

			}
		}

		// return values
		return nextnode;
	}

}
