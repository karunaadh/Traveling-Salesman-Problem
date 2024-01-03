import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class BasicFunctions {
	// buffered reader
	public static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

	// initialize
	public BasicFunctions() {
	}

	public static int getInteger(String prompt, int LB, int UB) throws IOException {
		// input and output variables
		int userInput = 0;

		// boundary variables
		String strLB = "";
		String strUB = "";

		// valid input
		boolean valid;

		// check if lowerbound is min value and set strLB
		if (LB == -Integer.MAX_VALUE) {
			strLB = "-infinity";
		} else {
			strLB = String.valueOf(LB);
		}

		// check if lowerbound is max value and set strUB
		if (UB == Integer.MAX_VALUE) {
			strUB = "infinity";
		} else {
			strUB = String.valueOf(UB);
		}

		// prompt for input and reprompt until valid input
		do {
			// set valid as true
			valid = true;

			// print prompt
			System.out.print(prompt);

			try {
				// get user input
				userInput = Integer.parseInt(reader.readLine());

				// if parsing is successful but input is out of bounds
				if (!(userInput >= LB && userInput <= UB)) {
					System.out.format("ERROR: Input must be an integer in [%s, %s]!\n\n", strLB, strUB);
					// set valid as false
					valid = false;
				}

				// if input is not integer
			} catch (NumberFormatException e) {
				System.out.format("ERROR: Input must be an integer in [%s, %s]!\n\n", strLB, strUB);
				// set valid as false
				valid = false;
			}

		} while (!valid);

		return userInput;
	}

	public static double getDouble(String prompt, double LB, double UB) throws IOException {
		// variables for input
		double userInput = 0.0;

		// boundary variables
		String strLB = "";
		String strUB = "";

		// valid input
		boolean valid;

		// check if lowerbound is min value and set strLB
		if (LB == -Double.MAX_VALUE) {
			strLB = "-infinity";
		} else {
			strLB = String.format("%.2f", LB);
		}

		// check if lowerbound is max value and set strUB
		if (UB == Double.MAX_VALUE) {
			strUB = "infinity";
		} else {
			strUB = String.format("%.2f", UB);
		}

		// prompt for input and reprompt until valid input
		do {
			// set valid as true
			valid = true;

			// print prompt
			System.out.print(prompt);

			try {
				// get input
				userInput = Double.parseDouble(reader.readLine());

				// if parsing is successful but input is out of bounds
				if (!(userInput >= LB && userInput <= UB)) {
					System.out.format("ERROR: Input must be a real number in [%s, %s]!\n\n", strLB, strUB);
					// set valid as false
					valid = false;
				}
				// if input is not double
			} catch (NumberFormatException e) {
				System.out.format("ERROR: Input must be a real number in [%s, %s]!\n\n", strLB, strUB);
				// set valid as false
				valid = false;
			}
		} while (!valid);

		// return valid value
		return userInput;
	}

	public static String getString(String prompt, boolean menu) throws IOException {
		// print prompt
		System.out.print(prompt);

		// get user input
		String userInput = reader.readLine();

		// lower case for menu options
		if (menu) {
			userInput = userInput.toLowerCase();
		}

		// return value
		return userInput;
	}

}
