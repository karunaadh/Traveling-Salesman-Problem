import java.util.*;

public class TSPSolver {
	// array of solution paths
	private int[][] solnPath;
	// array of solution costs
	private double[] solnCost;
	// array of computation times
	private double[] compTime;
	// array of T/F solns found
	private boolean[] solnFound;
	// name of solver
	private String name;
	// full name
	String fullname = "";
	// whether or not results exist
	private boolean resultsExist = false;
	// track number of successes
	int numSuccess = 0;
	// success rate
	double successRate = 0;
	// average cost
	double avgCost = 0.0;
	// average computation time
	double avgTime = 0.0;

	// ------------constructors-----------
	public TSPSolver() {
	}

	public TSPSolver(ArrayList<Graph> G) {
		// initialize TSPSolver
		this.init(G);
	}

	// ------------getters------------
	// get ith item in solnPath
	public int[] getSolnPath(int i) {
		return solnPath[i];
	}

	// get ith item in solnCost
	public double getSolnCost(int i) {
		return solnCost[i];
	}

	// get ith item in compTime
	public double getCompTime(int i) {
		return compTime[i];
	}

	// get ith item in solnFound
	public boolean getSolnFound(int i) {
		return solnFound[i];
	}

	// get name
	public String getName() {
		return this.name;
	}

	// get resultsExist
	public boolean hasResults() {
		return resultsExist;
	}

	// get average cost
	public double getAvgCost() {
		return this.avgCost;
	}

	// get average time
	public double getAvgTime() {
		return this.avgTime;
	}

	// get success rate
	public double getSuccessRate() {
		return this.successRate;
	}

	// ------------setters------------
	// set ith index of solnPath as solnPath
	public void setSolnPath(int i, int[] solnPath) {
		this.solnPath[i] = solnPath;
	}

	// set ith index of solnCost as solnCost
	public void setSolnCost(int i, double solnCost) {
		this.solnCost[i] = solnCost;
	}

	// set ith index of compTime as compTime
	public void setCompTime(int i, double compTime) {
		this.compTime[i] = compTime;
	}

	// set ith index of solnFound as solnFound
	public void setSolnFound(int i, boolean solnFound) {
		this.solnFound[i] = solnFound;
	}

	// set name
	public void setName(String name) {
		this.name = name;
		if (name.equals("NN")) {
			fullname = "nearest neighbor";
		} else if (name.equals("NN-FL")) {
			fullname = "nearest neighbor first-last";
		} else if (name.equals("NI")) {
			fullname = "node insertion";
		}
	}

	// set resultsExist as b
	public void setHasResults(boolean b) {
		this.resultsExist = b;
	}

	// -----------methods----------------
	// initialize variables and arrays
	public void init(ArrayList<Graph> G) {
		// get number of graphs
		int numGraphs = G.size();
		numSuccess = 0;
		name = "";

		// initialize all arrays
		solnPath = new int[numGraphs][];
		for (int i = 0; i < numGraphs; i++) {
			solnPath[i] = new int[G.get(i).getN() + 1];
		}
		solnCost = new double[numGraphs];
		compTime = new double[numGraphs];
		solnFound = new boolean[numGraphs];
		Arrays.fill(solnFound, false);
		Arrays.fill(solnCost, 0.0);
		Arrays.fill(compTime, 0.0);

		// set hasresults false
		resultsExist = false;
	}

	// reset variables and arrays
	public void reset() {
		// clear all arraylists
		solnPath = null;
		solnCost = null;
		compTime = null;
		solnFound = null;
		name = "";

		// clear resultsExist
		resultsExist = false;

		// set graphs loaded as false
		numSuccess = 0;
	}

	// run nearest neighbor on Graph i
	public void run(ArrayList<Graph> G, int i, boolean suppressOutput) {
		// get ith graph
		Graph graph = G.get(i);

		// array list of visited nodes
		ArrayList<Integer> visited = new ArrayList<Integer>();

		// max capacity is number of nodes + 1
		visited.ensureCapacity(graph.getN() + 1);

		// initialize with node 1 as the first value
		visited.add(0);

		// first visited
		int firstVisited;

		// track if run is complete
		boolean runComplete = false;

		// next nearest neighbour
		int[] nextNearest = new int[2];

		// until nearestNeighbour returns -1 (failed to find path), or firstVisited
		// (last node is first)
		do {
			// send visited and graph to nearestNeighbour
			nextNearest = nextNode(graph, visited);
			firstVisited = visited.get(0);

			// if nearestNeighbour returns first visited or -1
			if (nextNearest[0] == -1 || nextNearest[0] == firstVisited) {
				// run complete is true
				runComplete = true;
			} else {
				// add nextNearest to visited list
				visited.add(nextNearest[1], nextNearest[0]);
			}

		} while (!runComplete);

		// if run is complete and path is successful, set it as solution path.
		if (nextNearest[0] == firstVisited) {

			visited.add(visited.size(), nextNearest[0]);

			// set solnPath for graph i
			int[] visitedArray = visited.stream().mapToInt(Integer::intValue).toArray();
			this.solnPath[i] = visitedArray;

			// set cost for graph i
			this.solnCost[i] = graph.pathCost(visitedArray);

			// set solnFound as true for graph i
			this.solnFound[i] = true;
			numSuccess++;
		} else if (nextNearest[0] == -1) {
			// set solnFound as false for graph i
			this.solnFound[i] = false;

			System.out.format("ERROR: %s did not find a TSP route for Graph %d!\n", name, i + 1);
		}

	}

	// find next node (return 2D array with [0] = next node, [1] = insertion
	// position
	public int[] nextNode(Graph G, ArrayList<Integer> visited) {
		int nextnode[] = new int[2];
		return nextnode;

	}

	// find nearest unvisited neighbor to kth (K RANGES FROM 0 TO N-1) node
	public int nearestNeighbor(Graph G, ArrayList<Integer> visited, int k) {
		// get arraylist size
		int visitedSize = visited.size();

		// get number of nodes
		int numNodes = G.getN();

		// track closest node (default unsuccessful)
		int closestNode = -1;

		// get first visited node number
		int firstVisited = visited.get(0);

		// if no unvisited cities left (visited has all nodes, unrepeated)
		if (visitedSize == numNodes) {
			// if there's a path between the last last visited and the first node
			if (G.existsArc(firstVisited, k) && k == visited.get(visitedSize - 1)) {
				// return first visited node
				closestNode = firstVisited;
			} // otherwise if path doesn't exist, closestNode stays -1 (indication of failure)

		}
		// if unvisited cities are left
		else {
			// track minimum distance
			double minDistance = Double.MAX_VALUE;
			double distance = 0.0;

			// get index of k
			int index = visited.indexOf(k);

			// go through list of node indexes
			for (int i = 0; i < numNodes; i++) {
				// if the node index isn't already in the visited list, and there's an arc
				// between
				// the node in that index and the last visited node
				if (!visited.contains(i) && G.existsArc(k, i) && NISolver.canBeInserted(G, visited, index, i)) {
					// calculate distance between the two nodes
					distance = G.getCost(k, i);
					// if the distance is lower than the minimum distance and minDistance isn't at
					// the default
					if (distance < minDistance) {
						// set minDistance as distance
						minDistance = distance;
						// add i to the end of visited (to become the new lastVisited node)
						closestNode = i;
					}
				}
			}
			// if none of the leftover cities connect to the last visited city, closestNode
			// stays -1
		}

		return closestNode;
	}

	// run nearest neighbour algorithm
	public void runAlgorithm(ArrayList<Graph> G) {
		// num successes gets reset
		numSuccess = 0;
		successRate = 0;

		// graph size
		int graphSize = G.size();

		// initialize time variables
		long start;
		long elapsedTime;

		// go through all graphs and run them
		for (int i = 0; i < graphSize; i++) {
			// track start time
			start = System.currentTimeMillis();

			// run nearest neighbour algorithm on ith graph
			run(G, i, true);

			// get elapsed Time
			elapsedTime = System.currentTimeMillis() - start;

			// check if solution found
			if (solnFound[i]) {
				// store compTime
				compTime[i] = (double) elapsedTime;
			}
		}

		// once algorithm finishes running set resultsExist as true
		this.resultsExist = true;

	}

	// print results for a single graph (accepts input from 0 to # graphs-1)
	public void printSingleResult(int i, boolean rowOnly) {
		// variables for cost, compTime and route
		String cost = "";
		String computationTime = "";
		String route = "";
		int[] solutionPath = solnPath[i];

		// if solution is not found, set "-" for values
		if (!(solnFound[i] == true)) {
			cost = "-";
			computationTime = "-";
			route = "-";
		}
		// if solution is found, set value as cost, comp time and route
		else {
			// solnCost of ith graph
			cost = String.format("%.2f", solnCost[i]);
			// computation time of ith graph
			computationTime = String.format("%.3f", compTime[i]);
			// number of components
			int pathLength = solutionPath.length;
			// route of ith graph
			for (int j = 0; j < pathLength - 1; j++) {
				route += solutionPath[j] + 1;
				route += "-";
			}
			route += solutionPath[pathLength - 1] + 1;
		}

		// print single result
		System.out.format("%3d%17s%19s   %-1s\n", i + 1, cost, computationTime, route);

	}

	// print results for all graphs
	public void printAll() {
		// number of graphs
		int numGraphs = solnFound.length;

		// print header
		System.out.printf("Detailed results for %s:\n", fullname);
		System.out.println("-----------------------------------------------");
		System.out.println("No.        Cost (km)     Comp time (ms)   Route   ");
		System.out.println("-----------------------------------------------");

		// iterate through all graph's information
		for (int i = 0; i < numGraphs; i++) {
			// get ith graph's result
			printSingleResult(i, true);
		}

		// blank line
		System.out.println("");

	}

	// print statistics
	public void printStats() {
		// print headers
		System.out.printf("Statistical summary for %s:\n", fullname);
		System.out.println("---------------------------------------");
		System.out.println("           Cost (km)     Comp time (ms)");
		System.out.println("---------------------------------------");

		// get stats cost
		getStats();

		// get min and max
		double[] minmaxCost = Stats.getMinMax(solnCost, solnFound);
		double[] minmaxTime = Stats.getMinMax(compTime, solnFound);

		// get standard dev (avg passed through to avoid double calc.
		double costSD = Stats.getSD(solnCost, solnFound, numSuccess, avgCost);
		double timeSD = Stats.getSD(compTime, solnFound, numSuccess, avgTime);

		// print statistics
		System.out.format("%-11s%9.2f%19.3f\n", "Average ", avgCost, avgTime);
		System.out.format("%-11s%9.2f%19.3f\n", "St Dev ", costSD, timeSD);
		System.out.format("%-11s%9.2f%19.3f\n", "Min ", minmaxCost[0], minmaxTime[0]);
		System.out.format("%-11s%9.2f%19.3f\n", "Max ", minmaxCost[1], minmaxTime[1]);
		System.out.println("");

	}
	
	//calculate global stats
	public void getStats() {

		// get cost
		this.avgCost = Stats.getAverage(solnCost, numSuccess);

		// get comp time
		this.avgTime = Stats.getAverage(compTime, numSuccess);

		//set success rate
		successRate();
		
	}
	

	// calculate success rate
	public double successRate() {
		// return success rate (number of successes/total trials)*100
		this.successRate = ((double) this.numSuccess / (double) solnFound.length) * 100;

		return successRate;
	}
}
