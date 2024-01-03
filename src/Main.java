import java.io.*;
import java.util.*;

public class Main {
	// if graphs loaded
	public static boolean graphsLoaded = false;

	public static void main(String[] args) throws IOException {
		// graph arraylist
		ArrayList<Graph> G = new ArrayList<Graph>();
		// NN solver object
		NNSolver NN = new NNSolver();
		// NI Solver
		NISolver NI = new NISolver();
		// NNFL solver
		NNFLSolver FL = new NNFLSolver();

		// user choice string
		String mainChoice;

		// boolean to track main loop continuation
		boolean continueProgram = true;

		// menu options
		ArrayList<String> mainMenuOptions = new ArrayList<String>(Arrays.asList("l", "i", "c", "r", "d", "x", "q"));

		// main loop
		while (continueProgram) {
			displayMenu();

			// get user choice
			mainChoice = BasicFunctions.getString("Enter choice: ", true);

			// check for valid input
			if (!mainMenuOptions.contains(mainChoice)) {
				System.out.println("");
				System.out.println("ERROR: Invalid menu choice!");
				System.out.println("");
				continue;
			}

			// ----route choices to respective functions----
			// create graph
			if (mainChoice.equals("q")) {
				continueProgram = false;
			} else if (mainChoice.equals("l")) {
				// space
				System.out.println("");

				// loadfile function
				if (loadFile(G)) {
					// if successful, set graphs loaded as true
					NN.init(G);
					FL.init(G);
					NI.init(G);
					// set name
					NN.setName("NN");
					FL.setName("NN-FL");
					NI.setName("NI");

					// set graphs loaded as true
					graphsLoaded = true;
				}

				// if any other choice except "q" or "l" is chosen
			} else {
				// handle routing for managing graph
				manageTSP(mainChoice, G, NN, FL, NI);
			}

		}

		// goodbye message
		System.out.println("");
		System.out.println("Ciao!");

	}

	// display the menu
	public static void displayMenu() {
		// print menu options
		System.out.println("   JAVA TRAVELING SALESMAN PROBLEM V3");
		System.out.println("L - Load graphs from file");
		System.out.println("I - Display graph info");
		System.out.println("C - Clear all graphs");
		System.out.println("R - Run all algorithms");
		System.out.println("D - Display algorithm performance");
		System.out.println("X - Compare average algorithm performance");
		System.out.println("Q - Quit");

		// blank line after menu
		System.out.println("");
	}

	// read in graphs from user-specified file
	public static boolean loadFile(ArrayList<Graph> G) throws IOException, NumberFormatException {
		// track success
		boolean success = true;
		// track number of graphs
		int numGraphs = 0;
		int acceptedGraphs = 0;

		// get filename from user
		String userFile = BasicFunctions.getString("Enter file name (0 to cancel): ", false);

		// check if user exited
		if (userFile.equals("0")) {
			System.out.println("");
			System.out.println("File loading process canceled.");
			System.out.println("");
			success = false;
		} else {
			File fileName = new File(userFile);

			// if file doesn't exist, print error
			if (!fileName.exists()) {
				System.out.println("");
				printError("ERROR: File not found!");
				success = false;
			}
			// else read file
			else {
				// create buffered file reader object
				BufferedReader fin = new BufferedReader(new FileReader(fileName));

				// current line for file
				String currentLine = "";

				// node information
				String name;
				double lat;
				double lon;

				// read each file line until end of file
				while (currentLine != null) {
					// read first line
					currentLine = fin.readLine();

					// take in blank line between graphs
					if (currentLine.isEmpty()) {
						currentLine = fin.readLine();
						if (currentLine == null) {
							continue;
						}
					}

					// number of nodes for graph
					int numNodes = Integer.parseInt(currentLine);
					// increase number of graphs
					numGraphs++;

					// initialize graph with numNodes nodes
					Graph graph = new Graph(numNodes);

					// number of arc lines
					int numArcs = numNodes - 1;

					// track whether graph is invalid
					boolean invalidGraph = false;

					// read till end of that graph's node list
					for (int i = 0; i < numNodes && !invalidGraph; i++) {
						// read line
						currentLine = fin.readLine();

						// get node information
						String[] nodeInfo = currentLine.split(",");
						name = nodeInfo[0];
						lat = Double.parseDouble(nodeInfo[1]);
						lon = Double.parseDouble(nodeInfo[2]);

						// if coordinates aren't valid, set invalid graph as true
						if (!validCoordinates(lat, lon)) {
							invalidGraph = true;
							continue;
						}
						// if coordinates are valid
						else {
							// set node information
							Node node = new Node();
							node.setName(name);
							node.setLat(lat);
							node.setLon(lon);

							// check if node exists in graph
							if (graph.existsNode(node)) {
								// if it already exists, graph is invalid
								invalidGraph = true;
								continue;
							} else {
								// if node is not in graph, add it to graph
								graph.addNode(node);
							}
						}
					}

					// if graph is valid, continue to add arcs
					if (!invalidGraph) {
						// read till end of that graph's arc list info (line # = node #)
						for (int i = 0; i < numArcs && !invalidGraph; i++) {
							// read line
							currentLine = fin.readLine();
							// split line into arc connection for that line's node
							String[] arcs = currentLine.split(",");
							// parse connecting cities to integer
							for (int k = 0; k < arcs.length; k++) {
								// parse
								int j = Integer.parseInt(arcs[k]);
								// change to index values
								j = j - 1;
								// for current graph, add arc if it doesn't exist (i = node #, j = connecting
								// node
								if (!graph.existsArc(i, j)) {
									graph.addArc(i, j);
								}
							}
						}

						// add graph to arraylist
						G.add(graph);
						acceptedGraphs++;

						// if graph is not valid
					} else {
						// take in lines till you reach next graph
						while (currentLine != null && !currentLine.isEmpty()) {
							currentLine = fin.readLine();
						}
					}

				}

				// close file
				fin.close();

				// print number of loaded graphs
				System.out.println("");
				System.out.format("%d of %d graphs loaded!\n", acceptedGraphs, numGraphs);
				System.out.println("");
			}
		}

		// return success status
		return success;
	}

	// check if coordinates are valid
	public static boolean validCoordinates(double latitude, double longitude) {
		// validity status
		boolean valid = false;

		// if latitude and longitude are both in a proper range, coordinates are valid
		if ((latitude >= -90 && latitude <= 90) && (longitude >= -180 && longitude <= 180)) {
			valid = true;
		}

		// return validity
		return valid;

	}

	// display summary info for each graph and allow user to select graphs to see
	// detailed info
	public static void displayGraphs(ArrayList<Graph> G) throws IOException {
		// ask user which graph they want to see until they enter "0"
		int i = 0;

		// boolean to track continuing
		boolean continuePrompt = true;
		// loop while i != 0
		do {
			// display summary
			displayGraphSummary(G);

			// get user input
			i = BasicFunctions.getInteger("Enter graph to see details (0 to quit): ", 0, G.size());
			System.out.println("");

			// only run if i != 0
			if (i != 0) {
				// get index of graph
				i = i - 1;
				// print graph details for ith graph
				G.get(i).print();

				// otherwise, exit loop
			} else {
				continuePrompt = false;
			}

		} while (continuePrompt);
	}

	// display summary of all graphs
	public static void displayGraphSummary(ArrayList<Graph> G) {
		// graph list length
		int length = G.size();

		// graph summary
		System.out.println("GRAPH SUMMARY");

		// header
		System.out.println("No.    # nodes    # arcs");
		System.out.println("------------------------");

		// print all rows with graph number, node number, and arc number
		for (int i = 0; i < length; i++) {
			System.out.format("%3d%11d%10d", (i + 1), G.get(i).getN(), G.get(i).getM());
			System.out.println("");
		}
		System.out.println("");
	}

	// print error
	public static void printError(String error) {
		System.out.println(error);
		System.out.println("");
	}

	// handle main menu routing for tsp actions
	public static void manageTSP(String mainChoice, ArrayList<Graph> G, NNSolver NN, NNFLSolver FL, NISolver NI)
			throws IOException {
		System.out.println("");

		// if graphs loaded
		if (graphsLoaded) {

			switch (mainChoice) {
			// display algorithm performance
			case "d":
				// if results !exist
				if (!NN.hasResults() || !NI.hasResults() || !FL.hasResults()) {
					printError("ERROR: Results do not exist for all algorithms!");
					// if results exist
				} else {
					NN.printAll();
					NN.printStats();
					System.out.format("Success rate: %.1f%%\n", NN.successRate());
					System.out.println("\n");
					FL.printAll();
					FL.printStats();
					System.out.format("Success rate: %.1f%%\n", FL.successRate());
					System.out.println("\n");
					NI.printAll();
					NI.printStats();
					System.out.format("Success rate: %.1f%%\n", NI.successRate());
					System.out.println("");
				}
				break;

			// display algorithm performance
			case "x":
				// if results !exist
				if (!NN.hasResults() || !NI.hasResults() || !FL.hasResults()) {
					printError("ERROR: Results do not exist for all algorithms!");
					// if results exist
				} else {
					//call compare function
					compare(NN, FL, NI);
				}
				break;

			// display graph info
			case "i":
				// run displayGraphs function
				displayGraphs(G);
				break;

			// clear graphs
			case "c":
				// reset function
				resetAll(NN, FL, NI);

				// clear all graphs
				G.clear();
				System.out.println("All graphs cleared.");
				System.out.println("");

				break;
			// run algorithm
			case "r":
				// run all algorithms
				runAll(G, NN, FL, NI);
				break;
			}

			// if files not loaded
		} else {
			// if choice is not "d" and files haven't loaded
			if (mainChoice.equals("d") || mainChoice.equals("x")) {
				printError("ERROR: Results do not exist for all algorithms!");
			} else {
				printError("ERROR: No graphs have been loaded!");
			}
		}

	}

	// reset all solvers
	public static void resetAll(NNSolver NN, NNFLSolver FL, NISolver NI) {
		NN.reset();
		FL.reset();
		NI.reset();
		graphsLoaded = false;
	}

	// run all solvers
	public static void runAll(ArrayList<Graph> G, NNSolver NN, NNFLSolver FL, NISolver NI) {
		// NN solver
		NN.runAlgorithm(G);
		System.out.println("Nearest neighbor algorithm done.\n");
		// NNFL solver
		FL.runAlgorithm(G);
		System.out.println("Nearest neighbor first-last algorithm done.\n");
		// NI solver
		NI.runAlgorithm(G);
		System.out.println("Node insertion algorithm done.\n");
	}

	// compare statistics
	public static void compare(NNSolver NN, NNFLSolver FL, NISolver NI) {
		// get winner (default NN)
		String winner = "Unclear";
		
		//have stats ready
		NN.getStats();
		FL.getStats();
		NI.getStats();
		
		//retrieve stats
		double NNcost = NN.getAvgCost();
		double FLcost = FL.getAvgCost();
		double NIcost = NI.getAvgCost();

		// get average times for each solver
		double NNtime = NN.getAvgTime();
		double FLtime = FL.getAvgTime();
		double NItime = NI.getAvgTime();

		// get success rate for each solver
		double NNrate = NN.getSuccessRate();
		double FLrate = FL.getSuccessRate();
		double NIrate = NI.getSuccessRate();

		// get winner for categories
		String costWinner = "NN";
		String timeWinner = "NN";
		String rateWinner = "NN";

		// cost
		if (FLcost < NNcost && FLcost <= NIcost) {
			// FLwins ++;
			costWinner = "NN-FL";
		} else if (NIcost < FLcost && NIcost < NNcost) {
			costWinner = "    NI";
			// NIwins++;
		}
		// also do for NN for overall winner???

		// time
		if (FLtime < NNtime && FLtime <= NItime) {
			timeWinner = " NN-FL";
		} else if (NItime < FLtime && NItime < NNtime) {
			timeWinner = "NI";
		}

		// rate
		if (FLrate > NNrate && FLrate >= NIrate) {
			rateWinner = " NN-FL";
		} else if (NIrate > FLrate && NIrate > NNrate) {
			rateWinner = "NI";
		}

		// overall
		if (costWinner.equals(timeWinner) && costWinner.equals(rateWinner)) {
			winner = costWinner;
		}

		// header
		System.out.println("------------------------------------------------------------\n"
				+ "           Cost (km)     Comp time (ms)     Success rate (%)\n"
				+ "------------------------------------------------------------");
		// NN Results
		System.out.printf("%-11s%9.2f%19.3f%21.1f\n", "NN ", NNcost, NNtime, NNrate);
		// NN-FL Results
		System.out.printf("%-11s%9.2f%19.3f%21.1f\n", "NN-FL ", FLcost, FLtime, FLrate);
		// NI Results
		System.out.printf("%-11s%9.2f%19.3f%21.1f\n", "NI", NIcost, NItime, NIrate);
		// Winner
		System.out.println("------------------------------------------------------------");
		System.out.printf("%-11s%9s%19s%21s\n", "Winner", costWinner, timeWinner, rateWinner);
		System.out.println("------------------------------------------------------------");
		System.out.printf("Overall winner: %s\n", winner);
		System.out.println("");
	}
}
