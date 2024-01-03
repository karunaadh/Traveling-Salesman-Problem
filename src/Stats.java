public class Stats {
	// calculate average cost
	public static double getAverage(double[] array, int length) {
		// return average (length is number of successes)
		return (getSum(array) / length);
	}

	// get minimum and max value
	public static double[] getMinMax(double[] list, boolean[] countValue) {
		// array of minmax values
		double[] minmax = new double[2];

		// min value
		double minValue = Double.MAX_VALUE;

		// max value
		double maxValue = 0.0;

		// iterate through list
		for (int i = 0; i < list.length; i++) {
			// if we want to count ith value of list
			if (countValue[i]) {
				// update min value if needed
				if (list[i] < minValue) {
					minValue = list[i];
				}
				// update max value if needed
				if (list[i] > maxValue) {
					maxValue = list[i];
				}
			}
		}

		// set minmax values
		minmax[0] = minValue;
		minmax[1] = maxValue;

		// return minmax
		return minmax;
	}

	// get standard deviation
	public static double getSD(double[] array, boolean[] count, int numSuccess, double mean) {
		// track SD
		double SD = 0.0;

		// find sum of (value - mean)^2
		for (int i = 0; i < array.length; i++) {
			// check if solution found
			if (count[i]) {
				// if so, count it
				SD += Math.pow(array[i] - mean, 2);
			}
		}

		// get final SD
		SD = Math.sqrt(SD / (numSuccess - 1));

		// return value
		return SD;
	}

	// get sum of arraylist content
	public static double getSum(double[] list) {
		// track sum
		double sum = 0.0;

		// sum up all values in list
		for (Double value : list) {
			sum += value;
		}

		// return sum of values
		return sum;
	}

}
