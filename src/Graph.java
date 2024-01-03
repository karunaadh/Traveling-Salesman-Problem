import java.util.ArrayList;
import java.util.Arrays;

public class Graph {
	// ------Graph variables------
	// number of cities
	private int n;
	// number of arcs
	private int m;
	// array of nodes
	private ArrayList<Node> node;
	// adjacency matrix
	private boolean[][] A;
	// cost matrix
	private double[][] C;
	// check if graph initialized
	private boolean graphExists;

	// ------Graph constructors------
	public Graph() {
		this.init();
		graphExists = false;
	}

	public Graph(int n) {
		graphExists = false;
		this.init(n);
	}

	// -------Graph setters--------
	// set value of n
	public void setN(int n) {
		this.n = n;
	}

	// set value of m
	public void setM(int m) {
		this.m = m;
	}

	// set arc (for "Add arc", send 1 as b. For "Remove arc" send 0 as b.
	public void setArc(int i, int j, boolean b)
	// (i and j range from 0 to n-1)
	{
		A[i][j] = b;
		A[j][i] = b;
	}

	// set cost (c is the cost for i-j)
	// (i and j range from 0 to n-1)
	public void setCost(int i, int j, double c) {
		C[i][j] = c;
		C[j][i] = c;

	}

	// set graphExists
	public void setgraphExists(boolean x) {
		this.graphExists = x;

	}

	// -------Graph getters--------
	// return n value
	public int getN() {
		return this.n;
	}

	// return m value
	public int getM() {
		return this.m;
	}

	// return arc for (i, j) (i.e. does arc i-j exist?)
	// (i and j range from 0 to n-1)
	public boolean getArc(int i, int j) {
		// returns true if A[i][j] = 1, false otherwise.
		return A[i][j];
	}

	// return cost of arc (i and j range from 0 to n-1)
	public double getCost(int i, int j) {
		return C[i][j];
	}

	// return Node (for "Edit cities" and "Display graph info")
	public Node getNode(int i) {
		return node.get(i);
	}

	// check if graph loaded
	public boolean existsGraph() {
		// if isInit hasn't been set to true (inside createGraph() at Pro3_adhika56),
		// graph is empty.
		return (graphExists);
	}

	// -------Graph methods------
	// initialize variables with default values
	public void init() {
		this.n = 0;
		this.m = 0;
	}

	// initialize variables and arrays with n
	public void init(int n) {
		// set size of n
		this.n = n;
		this.m = 0;

		// declare A and C as n x n arrays
		A = new boolean[n][n];
		C = new double[n][n];

		// set node as new array list
		node = new ArrayList<>();

		// fill with 0 (false)
		for (int i = 0; i < A.length; i++) {
			Arrays.fill(A[i], false);
			Arrays.fill(C[i], 0);
		}
	}

	// reset graph
	public void reset() {
		// delete all items in arraylist
		node.clear();

		// set n = 0 for "graph not loaded" and clear m
		this.n = 0;
		this.m = 0;

		// reset A and C
		A = null;
		C = null;

		// set graph exists as false
		graphExists = false;
	}

	// check if arc exists (i and j range from 0 to (n-1)
	public boolean existsArc(int i, int j) {
		// returns true if A[i][j] = 1, false otherwise.
		return A[i][j];
	}

	// check if node exists
	public boolean existsNode(Node t) {
		// assume false
		boolean exists = false;

		// if at any point, the names match or the coordinates match, set exists as true
		// and break
		for (Node x : node) {
			if (t.getName().equals(x.getName()) || (t.getLat() == x.getLat() && t.getLon() == x.getLon())) {
				exists = true;
				break;
			}
		}

		return exists;
	}

	// add arc (i and j range from 0 to n-1)
	public boolean addArc(int i, int j) {
		// set arc in adjacency matrix
		setArc(i, j, true);

		// calculate cost for arc and store it in cost matrix
		double arcCost = Node.distance(node.get(i), node.get(j));
		setCost(i, j, arcCost);

		// increase m
		this.m++;

		// return success
		return true;
	}

	// remove an arc (k ranges from 1 to n - arc number from menu)
	public void removeArc(int k) {
		// track arc number as you go through A matrix
		int count = 0;

		findK:
		// only go through one half of the A matrix (since A[i][j] = A[j][i])
		for (int i = 0; i < n; i++) {
			for (int j = i + 1; j < n; j++) {
				// if arc exists for a particular array element
				if (existsArc(i, j)) {
					count++;
				}

				// if we found the kth arc
				if (count == k) {
					// remove arc from A and C matrix
					setArc(i, j, false);
					setCost(i, j, 0);

					// decrease m
					this.m--;

					// break out of loop block
					break findK;
				}
			}
		}
	}

	// add a node
	public boolean addNode(Node t) {
		node.add(t);
		return true;
	}

	// print all graph info
	public void print() {
		System.out.format("Number of nodes: %d\n", this.n);
		System.out.format("Number of arcs: %d\n", this.m);
		System.out.println("");
		printNodes();
		printArcs();

	}

	// print out all cities
	public void printNodes() {
		System.out.println("NODE LIST");
		System.out.println("No.               Name        Coordinates");
		System.out.println("-----------------------------------------");

		// print out all cities
		for (int i = 0; i < n; i++) {
			int cityNumber = i + 1;
			// print out node information (city number takes 3 spaces)
			System.out.format("%3d", cityNumber);
			// call print function for rest of the row
			node.get(i).print();

			// new line
			System.out.println("");
		}

		// new line
		System.out.println("");
	}

	// print arcs
	public void printArcs() {
		System.out.println("ARC LIST");
		System.out.println("No.    Cities       Distance");
		System.out.println("----------------------------");

		// arc number
		int arcNumber = 1;
		String arcCities;
		double arcDistance;

		// loop through A matrix (n x n where n is the number of nodes) and check where
		// there's adjacency
		for (int i = 0; i < n; i++) {
			// only check upper half of matrix since symmetric
			for (int j = i + 1; j < n; j++) {
				// if adjacency exists, print out arc (to avoid the program printing 1-2 and
				// 2-1)
				if (existsArc(i, j)) {
					// get arc and distance
					arcCities = (i + 1) + "-" + (j + 1);
					arcDistance = getCost(i, j);

					// print formatted date: node number takes 3 spaces, arc cities takes 10 spaces,
					// distance takes 15 spaces
					System.out.format("%3d%10s%15.2f", arcNumber, arcCities, arcDistance);
					System.out.println("");

					// increase arcNumber
					arcNumber++;
				}
			}
		}

		// print space after list
		System.out.println("\n");
	}

	// check feasibility of path P (values in P range from 1 to n)
	public boolean checkPath(int[] P) {
		boolean validPath = true;

		// ---check if path starts and ends at same city---
		// since the path will always have n+1 items, the first item is P[0] and the
		// last is [n]
		if (P[0] != P[n]) {
			System.out.println("ERROR: Start and end cities must be the same!");
			System.out.println("");
			validPath = false;
			// return is used because the first error from the error list found is printed
			// and the TSP loop ends immediately
			// (no need to check for other errors)
		}

		// ----check if duplicated cities----
		else if (duplicateCities(P)) {
			validPath = false;
		}

		// ------check if there's a path that doesn't exist----
		else if (nonExistentPath(P)) {
			validPath = false;
		}

		// -------else if no error occurs and function end is reached, return true
		// (valid path)----
		return validPath;
	}

	// check if duplicate cities exist (P has values from 1 to n)
	public boolean duplicateCities(int[] P) {
		boolean duplicatedCity = false;

		// search all combinations of P[i], P[j] to see if there are duplicates.
		searchDuplicate: for (int i = 0; i < P.length; i++) {
			for (int j = i + 1; j < P.length; j++) {
				// at first instance of duplicate, print error, set duplicatedCity as true and
				// break loop block
				// only call error if P[i] = P[j] but they're not the 1st and last indices or
				// the same index
				if (P[i] == P[j] && i != j && !(i == 0 && j == P.length - 1)) {
					System.out.println("ERROR: Cities cannot be visited more than once!");
					System.out.println("ERROR: Not all cities are visited!");
					System.out.println("");
					duplicatedCity = true;
					break searchDuplicate;
				}
			}
		}

		// return boolean
		return duplicatedCity;
	}

	// check if any non-existent paths used
	public boolean nonExistentPath(int[] P) {
		boolean notExists = false;

		checkArc:
		// i ranges from 0 to P.length - 2 (the second last index) to compare to the
		// value after it
		for (int i = 0; i < (P.length) - 1; i++) {
			// first invalid path is printed and TSP loop is exited
			// P[i]-1 and P[i+1]-1 used because A index ranges from 0 to n-1, but values in
			// p range from 1 to n.
			int cityOne = P[i] - 1;
			int cityTwo = P[i + 1] - 1;

			// if not exists, print error and break out of checkArc block
			if (!existsArc(cityOne, cityTwo)) {
				System.out.format("ERROR: Arc %d-%d does not exist!\n", P[i], P[i + 1]);
				System.out.println("");
				notExists = true;
				break checkArc;
			}

		}

		return notExists;
	}

	// check cost of path P (values in p range from 0 to n-1)
	public double pathCost(int[] P) {
		double cost = 0;

		// loop through all of P and add the costs for i and i+1 until i+1 equals the
		// last index.
		for (int i = 0; i < (P.length) - 1; i++) {
			// sum up costs
			// C[i,j] is the cost to go from city i to city j and vice versa
			cost += C[P[i]][P[i + 1]];
		}

		// return cost value
		return cost;
	}

	// graph not loaded error
	public void noGraphError() {
		System.out.println("ERROR: No graph has been loaded!");
		System.out.println("");
	}

}
