import java.io.*;

public class Node {
	// ----node variables/attributes----
	private String name;
	private double lat;
	private double lon;

	// ----constructors----
	// no initializing values
	public Node() {
		this.name = "";
		this.lat = 0.0;
		this.lon = 0.0;
	}

	// initialize with values
	public Node(String name, double lat, double lon) {
		this.name = name;
		this.lat = lat;
		this.lon = lon;
	}

	// ----setters----
	// set object name as name
	public void setName(String name) {
		this.name = name;
	}

	// set object latitude as lat
	public void setLat(double lat) {
		this.lat = lat;
	}

	// set object longitude as lon
	public void setLon(double lon) {
		this.lon = lon;
	}

	// ----getters----
	// get object name
	public String getName() {
		return this.name;
	}

	// get object latitude
	public double getLat() {
		return this.lat;
	}

	// get object longitude
	public double getLon() {
		return this.lon;
	}

	// ----object methods3----
	// get user info and edit node
	public void userEdit() throws IOException {
		// get name
		this.name = BasicFunctions.getString("   Name: ", false);

		// get latitude between -90 and 90 ----------------------------check
		// prompt---------------------------------
		this.lat = BasicFunctions.getDouble("   latitude: ", -90, 90);

		// get longitude between -180 and 180
		this.lon = BasicFunctions.getDouble("   longitude: ", -180, 180);

		// space
		System.out.println("");
	}

	// print node info as a table row
	public void print() {
		// coordinates string
		String coordinates;

		// get formatted coordinates
		coordinates = "(" + lat + "," + lon + ")";
		// print name and coordinates as row - city name takes 19 spaces, coordinates
		// take 19 spaces
		System.out.format("%19s%19s", name, coordinates);

	}

	// calculate distance between two nodes
	public static double distance(Node i, Node j) {
		// declare R constant
		final double R = 6371.0;

		// retrieve i and j latitude and longitude
		double iLat = i.getLat();
		double jLat = j.getLat();
		double iLon = i.getLon();
		double jLon = j.getLon();

		// convert to degrees
		iLat = Math.toRadians(iLat);
		jLat = Math.toRadians(jLat);
		iLon = Math.toRadians(iLon);
		jLon = Math.toRadians(jLon);

		// find delta x and delta y
		double deltaX = Math.abs(jLat - iLat);
		double deltaY = Math.abs(jLon - iLon);

		// apply haversine formula
		double a = Math.pow(Math.sin(deltaX / 2.0), 2)
				+ Math.cos(iLat) * Math.cos(jLat) * Math.pow(Math.sin(deltaY / 2.0), 2);
		double b = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double c = R * b;

		// return distance between two nodes
		return c;
	}

}
